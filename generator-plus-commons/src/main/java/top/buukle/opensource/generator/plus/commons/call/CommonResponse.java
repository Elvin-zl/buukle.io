package top.buukle.opensource.generator.plus.commons.call;


import lombok.Data;
import top.buukle.opensource.generator.plus.commons.call.head.ResponseHead;
import top.buukle.opensource.generator.plus.commons.exception.CommonException;

/**
 * @Author elvin
 * @Date Created by elvin on 2018/9/19.
 * @Description : buukle 公共请求规范
 */
@Data
public class CommonResponse<T> {
    /** 响应头*/
    private ResponseHead head;
    /** 响应体*/
    private T body;

    private CommonResponse (){

    }

    public CommonResponse(ResponseHead responseHead) {
        this.head = responseHead;
    }

    public CommonResponse(ResponseHead responseHead, T body) {
        this.head = responseHead;
        this.body = body;
    }

    public boolean isSuccess() {
        return this != null && this.getHead() != null  && "S".equals(this.getHead().getStatus());
    }

    public static class Builder {
        public <T>  CommonResponse<T> buildSuccess(){
            return new CommonResponse<>(new ResponseHead.Builder().buildSuccess());
        }

        public <T>  CommonResponse<T>  buildSuccess(T body){

            return new CommonResponse<>(new ResponseHead.Builder().buildSuccess(), body);
        }

        public  <T>  CommonResponse<T>  buildFailedInner(CommonException commonException) {
            return new CommonResponse<>(new ResponseHead.Builder().buildFailedInner(commonException));
        }

        public  <T>  CommonResponse<T>  buildFailed(Exception exception) {
            return new CommonResponse<>(new ResponseHead.Builder().buildFailed("系统内部错误!"));
        }

        public  <T>  CommonResponse<T>  buildFailedWithOriginMsg(Exception exception) {
            return new CommonResponse<>(new ResponseHead.Builder().buildFailed(exception));
        }

        public  <Boolean>  CommonResponse<Boolean>  buildBoolean(boolean success) {
            return success ? this.buildSuccess() : this.buildFailedInner(new CommonException());
        }
    }
}


