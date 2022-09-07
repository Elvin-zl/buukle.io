package top.buukle.opensource.generator.plus.web.archetypes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.head.Head;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteUpdateDTO;
import top.buukle.opensource.generator.plus.service.ArchetypesService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GenTest {

    @Autowired
    ArchetypesService archetypesService;

    /**
     * @description 根据archetypes的jar直接生成代码
     * @param
     * @return void
     * @Author zhanglei001
     * @Date 2021/9/15
     */
    @Test
    public void gen_test() throws Exception {
        // {"head":{"operationTime":1662541844939,"appId":"bk_18aef9dae4544e65aa9132abe252b649"},
        // "body":{"archetypesId":4,"groupId":"top.buukle","artifactId":"test","version":"1.0.0","archetypeUrl":"http://oss.buukle.top//mall/7608c7a3a2764f7fb99a42d0a3787eed/template-archetype-1.0.0.jar","basePackage":"test"}}
        CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest = new CommonRequest();

        Head head = Head.Builder.build();
        ArchetypesExecuteUpdateDTO archetypesExecuteUpdateDTO = new ArchetypesExecuteUpdateDTO();

        commonRequest.setHead(head);
        commonRequest.setBody(archetypesExecuteUpdateDTO);

        head.setAppId("bk_18aef9dae4544e65aa9132abe252b649");

        archetypesExecuteUpdateDTO.setArchetypesId(4);
        archetypesExecuteUpdateDTO.setGroupId("top.buukle");
        archetypesExecuteUpdateDTO.setVersion("1.0.0");
        archetypesExecuteUpdateDTO.setBasePackage("test");
        archetypesService.execute(commonRequest);
    }
}
