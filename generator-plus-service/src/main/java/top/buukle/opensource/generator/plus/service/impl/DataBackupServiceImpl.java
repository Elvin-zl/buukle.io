package top.buukle.opensource.generator.plus.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.buukle.opensource.generator.plus.service.DataBackupService;
import top.buukle.opensource.generator.plus.service.util.FileUtil;
import top.buukle.opensource.generator.plus.service.util.GitUtil;
import top.buukle.opensource.generator.plus.service.util.MysqlExport;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.utils.SystemUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class DataBackupServiceImpl implements DataBackupService {

    private static final String DATABASE_BACKUP = "DATABASE_BACKUP";

    private static final String GIT_FILE = "GIT_FILE";

    private static final String SQL_FILE = "SQL_FILE";

    @Value("${backup.mysql.host}")
    private String host;
    @Value("${backup.mysql.port}")
    private String port;
    @Value("${backup.mysql.username}")
    private String username;
    @Value("${backup.mysql.password}")
    private String password;
    @Value("${backup.mysql.database-name-list}")
    private String databaseNameList;
    @Value("${backup.mysql.git-location}")
    String gitLocation;

    @Value("${git.username}")
    String gitUsername;

    @Value("${git.password}")
    String gitPassword;

    @Value("${spring.datasource.driver-class-name}")
    String driverClassName;




    @Override
    public void backup() throws IOException, GitAPIException, InterruptedException {
        // 清理历史备份文件
        String basePath = SystemUtil.getStoreDir() + StringUtil.BACKSLASH + DATABASE_BACKUP;
        try {
            FileUtil.deleteFile(new File(basePath));
        } catch (Exception e) {
            log.error("清理旧文件异常,原因:{}",e.getCause() + e.getMessage());
        }
        String random = UUID.randomUUID().toString().replace("-", "");
        String sqlPath = basePath + StringUtil.BACKSLASH + random + StringUtil.BACKSLASH + SQL_FILE;
        log.debug("sqlPath-->",sqlPath);
        String gitPath = basePath + StringUtil.BACKSLASH + random + StringUtil.BACKSLASH + GIT_FILE;
        log.debug("gitPath-->",gitPath);
        Files.createDirectories(Paths.get(sqlPath));
        Files.createDirectories(Paths.get(gitPath));

        // 备份mysql
        for (String databaseName : databaseNameList.split(StringUtil.COMMA)) {
            this.backup(host,port,username,password,databaseName,sqlPath,driverClassName);
        }
        // 提交git
        GitUtil.cloneAndAddFile(sqlPath,gitPath,gitLocation,"master",gitUsername,gitPassword);
    }

    private void backup(String host, String port, String username, String password, String database, String sqlPath,String driverClassName) {
        try {
            MysqlExport.export(host,port,username,password,database,sqlPath,driverClassName);
        } catch (Exception e) {
            log.error("数据库备份出现异常:{}",e.getCause() + e.getMessage());
        }
    }

}
