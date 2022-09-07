package top.buukle.opensource.generator.plus.controller;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.service.UploadService;


@Controller
@RequestMapping("/upload")
@Api(value = "upload",tags=" upload Controller")
@Slf4j
public class UploadController {

    @Autowired
    UploadService uploadService;

    @ResponseBody
    @RequestMapping( "/uploadMultipartFile" )
    public CommonResponse<String> uploadMultipartFile(@RequestParam( "file" ) MultipartFile file) {
        return uploadService.uploadMultipartFile(file);
    }

}
