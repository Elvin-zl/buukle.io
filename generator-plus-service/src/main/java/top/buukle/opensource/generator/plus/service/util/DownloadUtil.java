package top.buukle.opensource.generator.plus.service.util;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;

public class DownloadUtil {

    /**
     * 下载文件，返回输入流。
     *
     * @param apiUrl api接口
     * @return (文件)输入流
     * @throws Exception
     */
    public static InputStream getStreamDownloadOutFile(String apiUrl) throws Exception {
        InputStream is = null;
        CloseableHttpClient httpClient = HttpClients.createDefault(); //创建默认http客户端
        RequestConfig requestConfig = RequestConfig.DEFAULT; //采用默认请求配置
        HttpGet request = new HttpGet(apiUrl); //通过get方法下载文件流
        request.setConfig(requestConfig); //设置请头求配置
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(request); //执行请求，接收返回信息
            int statusCode = httpResponse.getStatusLine().getStatusCode(); //获取执行状态
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                request.abort();
            } else {
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    is = entity.getContent(); //获取返回内容
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.abort();
        }
        return is;
    }

}
