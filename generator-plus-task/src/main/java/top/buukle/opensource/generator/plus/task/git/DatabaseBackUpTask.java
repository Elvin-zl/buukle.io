package top.buukle.opensource.generator.plus.task.git;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.buukle.opensource.generator.plus.service.DataBackupService;

@Component
@Slf4j
public class DatabaseBackUpTask {


    @Autowired
    DataBackupService dataBackupService;

    @Scheduled( cron = "40 0/59 * * * ? ")
    public void checkStatusCD() {
        log.debug("开始执行mysql数据备份任务!");
        dataBackupService.backup();
        log.debug("执行mysql数据备份任务完成!");
    }

}
