package top.buukle.opensource.generator.plus.service.util;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GitUtil {


	private static final String GIT_TREE = "/.git";

	private static final String GIT = "git";

	private static final String COMMIT = "generator-plus commit";

	public static void cloneAndAddFile(String artifactId, String genBatchUuid, String generatedArchetypeIdBatchDir, String gitLocation, String gitBranch,String gitUsername,String gitPassword) throws IOException, InterruptedException, GitAPIException {
		String gitFileTempPath = SystemUtil.getStoreDir() + StringUtil.BACKSLASH + genBatchUuid + StringUtil.BACKSLASH + GIT ;
		Path directory = Paths.get(gitFileTempPath);
		Files.createDirectories(directory);
		cloneCode(gitFileTempPath,gitLocation,gitBranch,gitUsername,gitPassword);
		FileUtil.copyDirectory(generatedArchetypeIdBatchDir + StringUtil.BACKSLASH + artifactId,gitFileTempPath);
		pushCode(gitFileTempPath,gitUsername,gitPassword);

	}

	public static void cloneAndAddFile(String resourcePath, String targetPath,String gitLocation, String gitBranch,String gitUsername,String gitPassword) throws IOException, InterruptedException, GitAPIException {
		Path directory = Paths.get(targetPath);
		Files.createDirectories(directory);
		cloneCode(targetPath,gitLocation,gitBranch,gitUsername,gitPassword);
		FileUtil.copyDirectory(resourcePath,targetPath);
		pushCode(targetPath,gitUsername,gitPassword);

	}

	/**
	 * 克隆代码
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static void pushCode(String gitFileTempPath ,String gitUsername,String gitPassword) throws IOException, GitAPIException {
		//找到本地的git路径，一般在隐藏.git文件夹下
		Git git = new Git(new FileRepository(gitFileTempPath + GIT_TREE));
		//提交代码
		git.add().addFilepattern(".").call();
		git.commit().setAll(true).setMessage(COMMIT).call();

		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new UsernamePasswordCredentialsProvider(gitUsername,gitPassword);
		//git仓库地址
		git.push().setRemote("origin").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
	}

	public static void cloneCode(String localPath,String remotePath,String branch, String gitUsername,String gitPassword) throws IOException, GitAPIException {

		//设置远程服务器上的用户名和密码
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider =new UsernamePasswordCredentialsProvider(gitUsername,gitPassword);

		//克隆代码库命令
		CloneCommand cloneCommand = Git.cloneRepository();

		Git git = cloneCommand.setURI(remotePath)
				.setBranch(branch)
				.setDirectory(new File(localPath))
				.setCredentialsProvider(usernamePasswordCredentialsProvider)
				.call();
	}

}