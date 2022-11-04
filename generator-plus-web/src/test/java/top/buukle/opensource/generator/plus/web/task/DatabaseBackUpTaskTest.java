package top.buukle.opensource.generator.plus.web.task;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.buukle.opensource.generator.plus.task.git.DatabaseBackUpTask;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseBackUpTaskTest {

    @Autowired
    DatabaseBackUpTask databaseBackUpTask;
    /**
     * @description 增
     * @param
     * @return void
     * @Author zhanglei001
     * @Date 2021/9/15
     */
    @Test
    public void databaseBackUpTask_test() throws Exception {
        databaseBackUpTask.execute();
    }
}
