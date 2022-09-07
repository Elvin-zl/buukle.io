package top.buukle.opensource.generator.plus.service;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;

import java.io.File;

/**
* @author elvin
* @description UploadService
*/
public interface UploadService  {

    CommonResponse<String> uploadMultipartFile(MultipartFile file);

    CommonResponse<String> uploadFile(File file);

}
