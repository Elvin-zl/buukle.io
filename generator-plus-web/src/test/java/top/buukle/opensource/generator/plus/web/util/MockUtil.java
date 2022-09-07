package top.buukle.opensource.generator.plus.web.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockUtil {

    public static void initHeader(MockHttpServletRequestBuilder builder, String userInfo) {
        builder.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        builder.header("Content-Type" , "application/json;charset=UTF-8");
//        builder.header("BK_AUTHCOOKIE" , authCookie);
        builder.header("User" , userInfo);
    }

    public static MvcResult request(MockMvc mockMvc, MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder).andExpect(status().isOk()).andReturn();
    }
}
