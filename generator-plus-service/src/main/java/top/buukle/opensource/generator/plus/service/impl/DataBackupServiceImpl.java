package top.buukle.opensource.generator.plus.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.buukle.opensource.generator.plus.service.DataBackupService;
import top.buukle.opensource.generator.plus.service.util.FileUtil;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.utils.SystemUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class DataBackupServiceImpl implements DataBackupService {

    private static final String DATABASE_BACKUP = "DATABASE_BACKUP";

    private static final String DATABASE_BACKUP_ERROR = "DATABASE_BACKUP_ERROR";

    private static final String SQL_SUFFIX = ".sql";

    private static final String TXT_SUFFIX = "error.txt";

    public static final String CMD = "mysqldump --databases -h%s -P%s -u%s -p%s %s";

    @Value("${mysql.backup.host}")
    private String host;
    @Value("${mysql.backup.port}")
    private String port;
    @Value("${mysql.backup.username}")
    private String username;
    @Value("${mysql.backup.password}")
    private String password;
    @Value("${mysql.backup.database-name-list}")
    private String databaseNameList;



    private void backup(String host, String port, String username, String password, String database) {
        try {
            String cmd = String.format(CMD,host,port,username,password,database);
            Process process = Runtime.getRuntime().exec(cmd);
            new InputStreamProcessor(process,database).start();
            new ErrorInputStream(process).start();
        } catch (Exception e) {
            log.error("数据库备份出现异常:{}",e.getCause() + e.getMessage());
        }
    }

    @Override
    public void backup() throws IOException {
        // 清理历史备份文件
        String path = SystemUtil.getStoreDir() + StringUtil.BACKSLASH +  DATABASE_BACKUP;
        Path directory = Paths.get(path);
        Files.createDirectories(directory);
        FileUtil.deleteFile(new File(path));
        // 备份
        for (String databaseName : databaseNameList.split(StringUtil.COMMA)) {
            this.backup(host,port,username,password,databaseName);
        }
        // 提交git

    }

    static class InputStreamProcessor extends Thread{

        private final Process process;

        private final String database ;

        public InputStreamProcessor(Process process, String database) {
            this.process = process;
            this.database = database;
        }

        @SneakyThrows
        @Override
        public void run() {
            String directoryPath = SystemUtil.getStoreDir() + StringUtil.BACKSLASH +  DATABASE_BACKUP;
            Path directory = Paths.get(directoryPath);
            Files.createDirectories(directory);
            String path = directoryPath + StringUtil.BACKSLASH + database + SQL_SUFFIX;
            try (InputStream is = process.getInputStream(); FileOutputStream out = new FileOutputStream(path)){
                byte[] b = new byte[1024];
                int len;
                while ((len = is.read(b)) != -1) {
                    out.write(b, 0, len);
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

    static class ErrorInputStream extends Thread{

        private final Process process;

        private final String path = SystemUtil.getStoreDir() + StringUtil.BACKSLASH +  DATABASE_BACKUP_ERROR + StringUtil.BACKSLASH + TXT_SUFFIX;

        public ErrorInputStream(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try (InputStream is = process.getErrorStream();
                 FileOutputStream out = new FileOutputStream(path)){
                byte[] b = new byte[1024];
                int len;
                while ((len = is.read(b)) != -1) {
                    out.write(b, 0, len);
                }
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }
}
