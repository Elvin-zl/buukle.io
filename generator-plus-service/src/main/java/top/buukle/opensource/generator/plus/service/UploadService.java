package top.buukle.opensource.generator.plus.service;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;

/**
* @author elvin
* @description UploadService
*/
public interface UploadService  {

    CommonResponse<String> uploadMultipartFile(MultipartFile file);

}
