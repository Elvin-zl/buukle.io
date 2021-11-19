package top.buukle.opensource.generator.plus.service.engine.archetypes;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.maven.archetype.common.ArchetypeArtifactManager;
import org.apache.maven.archetype.common.Constants;
import org.apache.maven.archetype.common.PomManager;
import org.apache.maven.archetype.exception.UnknownArchetype;
import org.apache.maven.archetype.metadata.ArchetypeDescriptor;
import org.apache.maven.archetype.metadata.io.xpp3.ArchetypeDescriptorXpp3Reader;
import org.apache.maven.archetype.old.descriptor.ArchetypeDescriptorBuilder;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.model.Model;
import org.apache.maven.project.ProjectBuildingRequest;
import org.codehaus.plexus.logging.AbstractLogEnabled;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

@Component
@Slf4j
public class MyArchetypeArtifactManager
    extends AbstractLogEnabled
    implements ArchetypeArtifactManager
{
    @Autowired
    private PomManager pomManager;

    private Map<String, File> archetypeCache = new TreeMap<>();

    public File getArchetypeFile( final String groupId, final String artifactId, final String version,
                                  ArtifactRepository archetypeRepository, final ArtifactRepository localRepository,
                                  final List<ArtifactRepository> repositories, ProjectBuildingRequest buildingRequest )
        throws UnknownArchetype
    {
         return null;
    }

    public ClassLoader getArchetypeJarLoader( File archetypeFile )
        throws UnknownArchetype
    {
        try
        {
            URL[] urls = new URL[1];

            urls[0] = archetypeFile.toURI().toURL();

            return new URLClassLoader( urls );
        }
        catch ( MalformedURLException e )
        {
            throw new UnknownArchetype( e );
        }
    }

    public Model getArchetypePom( File jar )
        throws XmlPullParserException, UnknownArchetype, IOException
    {
        ZipFile zipFile = null;
        try
        {
            String pomFileName = null;
            zipFile = getArchetypeZipFile( jar );

            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while ( enumeration.hasMoreElements() )
            {
                ZipEntry el = (ZipEntry) enumeration.nextElement();

                String entry = el.getName();
                if ( entry.startsWith( "META-INF" ) && entry.endsWith( "pom.xml" ) )
                {
                    pomFileName = entry;
                }
            }

            if ( pomFileName == null )
            {
                return null;
            }

            ZipEntry pom = zipFile.getEntry( pomFileName );

            if ( pom == null )
            {
                return null;
            }
            return pomManager.readPom( zipFile.getInputStream( pom ) );
        }
        finally
        {
            closeZipFile( zipFile );
        }
    }

    public ZipFile getArchetypeZipFile( File archetypeFile )
        throws UnknownArchetype
    {
        try
        {
            return new ZipFile( archetypeFile );
        }
        catch ( ZipException e )
        {
            throw new UnknownArchetype( e );
        }
        catch ( IOException e )
        {
            throw new UnknownArchetype( e );
        }
    }

    public boolean isFileSetArchetype( File archetypeFile )
    {
        ZipFile zipFile = null;
        try
        {
           log.debug( "checking fileset archetype status on " + archetypeFile );

            zipFile = getArchetypeZipFile( archetypeFile );

            return isFileSetArchetype( zipFile );
        }
        catch ( IOException e )
        {
           log.debug( e.toString() );
            return false;
        }
        catch ( UnknownArchetype e )
        {
           log.debug( e.toString() );
            return false;
        }
        finally
        {
            if ( zipFile != null )
            {
                closeZipFile( zipFile );
            }
        }
    }

    public boolean isFileSetArchetype( String groupId, String artifactId, String version,
                                       ArtifactRepository archetypeRepository, ArtifactRepository localRepository,
                                       List<ArtifactRepository> repositories, ProjectBuildingRequest buildingRequest )
    {
        try
        {
            File archetypeFile = getArchetypeFile( groupId, artifactId, version, archetypeRepository,
                                                   localRepository, repositories, buildingRequest );

            return isFileSetArchetype( archetypeFile );
        }
        catch ( UnknownArchetype e )
        {
           log.debug( e.toString() );
            return false;
        }
    }

    public boolean isOldArchetype( File archetypeFile )
    {
        ZipFile zipFile = null;
        try
        {
           log.debug( "checking old archetype status on " + archetypeFile );

            zipFile = getArchetypeZipFile( archetypeFile );

            return isOldArchetype( zipFile );
        }
        catch ( IOException e )
        {
           log.debug( e.toString() );
            return false;
        }
        catch ( UnknownArchetype e )
        {
           log.debug( e.toString() );
            return false;
        }
        finally
        {
            if ( zipFile != null )
            {
                closeZipFile( zipFile );
            }
        }
    }

    public boolean isOldArchetype( String groupId, String artifactId, String version,
                                   ArtifactRepository archetypeRepository, ArtifactRepository localRepository,
                                   List<ArtifactRepository> repositories, ProjectBuildingRequest buildingRequest )
    {
        try
        {
            File archetypeFile = getArchetypeFile( groupId, artifactId, version, archetypeRepository,
                                                   localRepository, repositories, buildingRequest );

            return isOldArchetype( archetypeFile );
        }
        catch ( UnknownArchetype e )
        {
           log.debug( e.toString() );
            return false;
        }
    }

    public boolean exists( String archetypeGroupId, String archetypeArtifactId, String archetypeVersion,
                           ArtifactRepository archetypeRepository, ArtifactRepository localRepository,
                           List<ArtifactRepository> remoteRepositories, ProjectBuildingRequest buildingRequest )
    {

        return true;
    }

    public String getPostGenerationScript( File archetypeFile ) throws UnknownArchetype
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = getArchetypeZipFile( archetypeFile );
            Reader reader = getDescriptorReader( zipFile, Constants.ARCHETYPE_POST_GENERATION_SCRIPT );
            return reader == null ? null : IOUtils.toString( reader );
        }
        catch ( IOException e )
        {
            throw new UnknownArchetype( e );
        }
        finally
        {
            closeZipFile( zipFile );
        }
    }

    public ArchetypeDescriptor getFileSetArchetypeDescriptor( File archetypeFile )
        throws UnknownArchetype
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = getArchetypeZipFile( archetypeFile );

            return loadFileSetArchetypeDescriptor( zipFile );
        }
        catch ( XmlPullParserException e )
        {
            throw new UnknownArchetype( e );
        }
        catch ( IOException e )
        {
            throw new UnknownArchetype( e );
        }
        finally
        {
            closeZipFile( zipFile );
        }
    }

    public ArchetypeDescriptor getFileSetArchetypeDescriptor( String groupId,
                                                                          String artifactId,
                                                                          String version,
                                                                          ArtifactRepository archetypeRepository,
                                                                          ArtifactRepository localRepository,
                                                                          List<ArtifactRepository> repositories,
                                                                          ProjectBuildingRequest buildingRequest )
        throws UnknownArchetype
    {
        File archetypeFile = getArchetypeFile( groupId, artifactId, version, archetypeRepository, localRepository,
                                               repositories, buildingRequest );

        return getFileSetArchetypeDescriptor( archetypeFile );
    }

    public List<String> getFilesetArchetypeResources( File archetypeFile )
        throws UnknownArchetype
    {
       log.debug( "getFilesetArchetypeResources( \"" + archetypeFile.getAbsolutePath() + "\" )" );
        List<String> archetypeResources = new ArrayList<String>();

        ZipFile zipFile = null;
        try
        {
            zipFile = getArchetypeZipFile( archetypeFile );

            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while ( enumeration.hasMoreElements() )
            {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();

                if ( entry.getName().startsWith( Constants.ARCHETYPE_RESOURCES ) )
                {
                    // not supposed to be file.separator
                    String resource = entry.getName().substring( Constants.ARCHETYPE_RESOURCES.length() + 1 );
                   log.debug( "  - found resource (" + Constants.ARCHETYPE_RESOURCES + "/)" + resource );
                    archetypeResources.add( resource );
                }
                else
                {
                   log.debug( "  - ignored resource " + entry.getName() );
                }
            }
            return archetypeResources;
        }
        finally
        {
            closeZipFile( zipFile );
        }
    }

    public org.apache.maven.archetype.old.descriptor.ArchetypeDescriptor getOldArchetypeDescriptor( File archetypeFile )
        throws UnknownArchetype
    {
        ZipFile zipFile = null;
        try
        {
            zipFile = getArchetypeZipFile( archetypeFile );

            return loadOldArchetypeDescriptor( zipFile );
        }
        catch ( XmlPullParserException e )
        {
            throw new UnknownArchetype( e );
        }
        catch ( IOException e )
        {
            throw new UnknownArchetype( e );
        }
        finally
        {
            closeZipFile( zipFile );
        }
    }

    public org.apache.maven.archetype.old.descriptor.ArchetypeDescriptor getOldArchetypeDescriptor( String groupId,
                                                                            String artifactId,
                                                                            String version,
                                                                            ArtifactRepository archetypeRepository,
                                                                            ArtifactRepository localRepository,
                                                                            List<ArtifactRepository> repositories,
                                                                            ProjectBuildingRequest buildingRequest )
        throws UnknownArchetype
    {
        File archetypeFile = getArchetypeFile( groupId, artifactId, version, archetypeRepository, localRepository,
                                               repositories, buildingRequest );

        return getOldArchetypeDescriptor( archetypeFile );
    }

    private File getArchetype( String archetypeGroupId, String archetypeArtifactId, String archetypeVersion )
    {
        String key = archetypeGroupId + ":" + archetypeArtifactId + ":" + archetypeVersion;

        if ( archetypeCache.containsKey( key ) )
        {
           log.debug( "Found archetype " + key + " in cache: " + archetypeCache.get( key ) );

            return archetypeCache.get( key );
        }

       log.debug( "Not found archetype " + key + " in cache" );
        return null;
    }

    private void setArchetype( String archetypeGroupId, String archetypeArtifactId, String archetypeVersion,
                               File archetype )
    {
        String key = archetypeGroupId + ":" + archetypeArtifactId + ":" + archetypeVersion;

        archetypeCache.put( key, archetype );
    }

    private boolean isFileSetArchetype( ZipFile zipFile )
        throws IOException
    {
        Reader reader = null;
        try
        {
            reader = getArchetypeDescriptorReader( zipFile );

            return ( reader != null );
        }
        finally
        {
            IOUtil.close( reader );
        }
    }

    private boolean isOldArchetype( ZipFile zipFile )
        throws IOException
    {
        Reader reader = null;
        try
        {
            reader = getOldArchetypeDescriptorReader( zipFile );

            return ( reader != null );
        }
        finally
        {
            IOUtil.close( reader );
        }
    }

    private ArchetypeDescriptor loadFileSetArchetypeDescriptor( ZipFile zipFile )
        throws IOException, XmlPullParserException
    {
        Reader reader = null;
        try
        {
            reader = getArchetypeDescriptorReader( zipFile );

            if ( reader == null )
            {
                return null;
            }

            ArchetypeDescriptorXpp3Reader archetypeReader = new ArchetypeDescriptorXpp3Reader();
            return archetypeReader.read( reader, false );
        }
        catch ( IOException e )
        {
            throw e;
        }
        catch ( XmlPullParserException e )
        {
            throw e;
        }
        finally
        {
            IOUtil.close( reader );
        }
    }

    private org.apache.maven.archetype.old.descriptor.ArchetypeDescriptor loadOldArchetypeDescriptor( ZipFile zipFile )
        throws IOException, XmlPullParserException
    {
        Reader reader = null;
        try
        {
            reader = getOldArchetypeDescriptorReader( zipFile );

            if ( reader == null )
            {
                return null;
            }

            ArchetypeDescriptorBuilder builder = new ArchetypeDescriptorBuilder();
            return builder.build( reader );
        }
        catch ( IOException ex )
        {
            throw ex;
        }
        catch ( XmlPullParserException ex )
        {
            throw ex;
        }
        finally
        {
            IOUtil.close( reader );
        }
    }

    private Reader getArchetypeDescriptorReader( ZipFile zipFile )
        throws IOException
    {
        return getDescriptorReader( zipFile, Constants.ARCHETYPE_DESCRIPTOR );
    }

    private Reader getOldArchetypeDescriptorReader( ZipFile zipFile )
        throws IOException
    {
        Reader reader = getDescriptorReader( zipFile, Constants.OLD_ARCHETYPE_DESCRIPTOR );

        if ( reader == null )
        {
            reader = getDescriptorReader( zipFile, Constants.OLDER_ARCHETYPE_DESCRIPTOR );
        }

        return reader;
    }

    private Reader getDescriptorReader( ZipFile zipFile, String descriptor )
        throws IOException
    {
        ZipEntry entry = searchEntry( zipFile, descriptor );

        if ( entry == null )
        {
            return null;
        }

        InputStream is = zipFile.getInputStream( entry );

        if ( is == null )
        {
            throw new IOException( "The " + descriptor + " descriptor cannot be read in " + zipFile.getName() + "." );
        }

        return ReaderFactory.newReader( is, ReaderFactory.UTF_8 );
    }

    private ZipEntry searchEntry( ZipFile zipFile, String searchString )
    {
        Enumeration<? extends ZipEntry> enu = zipFile.entries();
        while ( enu.hasMoreElements() )
        {
            ZipEntry entryfound = (ZipEntry) enu.nextElement();

            if ( searchString.equals( entryfound.getName() ) )
            {
               log.debug( "Entry found" );
                return entryfound;
            }
        }
        return null;
    }

    private void closeZipFile( ZipFile zipFile )
    {
        try
        {
            zipFile.close();
        }
        catch ( Exception e )
        {
           log.error( "Failed to close " + zipFile.getName() + " zipFile." );
        }
    }
}
