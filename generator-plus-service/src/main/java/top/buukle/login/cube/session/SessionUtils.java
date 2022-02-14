package top.buukle.login.cube.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class SessionUtils {


    private static ObjectMapper mapper = new ObjectMapper();
    /**
     * @description 从请求中获取用户信息
     * @param
     * @return void
     * @Author zhanglei001
     * @Date 2021/9/2
     */
    public static OperatorUserDTO getOperator() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        OperatorUserDTO userDTO = new OperatorUserDTO();
        userDTO.setUserId(Constants.ANONYMOUS);
        userDTO.setUsername(Constants.ANONYMOUS);
        userDTO.setOnline(false);
        if(request != null){
            String userInfo = request.getHeader(Constants.USER_REQUEST_HEADER_KEY);
            log.debug("网关中的用户信息:{}",userInfo);
            try {
                if(userInfo != null && !"".equals(userInfo)){
                    userDTO = mapper.readValue(userInfo, OperatorUserDTO.class);
                    userDTO.setOnline(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("获取用户会话信息失败,原因:{}",e.getCause() + e.getMessage());
            }
        }
        return userDTO;
    }
}
