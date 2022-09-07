package top.buukle.opensource.generator.plus.web.archetypes;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.head.Head;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteUpdateDTO;
import top.buukle.opensource.generator.plus.utils.JsonUtil;
import top.buukle.opensource.generator.plus.web.util.MockUtil;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ArchetypesTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void execute_test() throws Exception {
        MockHttpServletRequestBuilder builder = post("/archetypes/execute");

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
        archetypesExecuteUpdateDTO.setArtifactId("test");
        String s = JsonUtil.toJSONString(commonRequest);

        log.info("execute Mock入参:{}",s);
        builder.content(s);
        String userInfo = "{\"username\":\"zhanglei001\",\"expire\":\"36000\",\"userId\":\"bk_2a4cb611b112481ba2c52afccc8a0eaf\",\"mainTenant\":\"30\",\"mainTenantLevel\":2,\"online\":false,\"sessionId\":\"05ef484018864a6291fefb87e938c94d\"}";
        MockUtil.initHeader(builder,userInfo);
        MockUtil.request(mockMvc,builder);
    }

}
