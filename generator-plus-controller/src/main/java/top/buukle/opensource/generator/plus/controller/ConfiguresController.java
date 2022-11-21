package top.buukle.opensource.generator.plus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.dtvo.dto.configures.ConfiguresQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.configures.ConfiguresUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.vo.configures.ConfiguresVO;
import top.buukle.opensource.generator.plus.dtvo.vo.configuresExecute.ConfiguresExecuteVO;
import top.buukle.opensource.generator.plus.entity.Configures;
import top.buukle.opensource.generator.plus.service.ConfiguresService;
import top.buukle.opensource.generator.plus.service.util.ResponseConvertUtil;
import top.buukle.opensource.generator.plus.service.vo.antd.ANTDPPageResponseVO;

import java.io.IOException;

/**
 * @author elvin
 * @description  Configures controller
 */
@Controller
@RequestMapping("/configures")
@Api(value = "Configures",tags=" Configures Controller")
public class ConfiguresController {

    @Autowired
    private ConfiguresService<Configures, ConfiguresVO, ConfiguresQueryDTO, ConfiguresUpdateDTO> configuresService;

    @PostMapping("/add")
    @ResponseBody
    @ApiOperation(value = "增", httpMethod = "POST")
    public CommonResponse<ConfiguresVO> add(@RequestBody CommonRequest<ConfiguresUpdateDTO> commonRequest) throws Exception {
        return configuresService.add(commonRequest);
    }

    @PostMapping("/deleteById")
    @ResponseBody
    @ApiOperation(value = "删", httpMethod = "POST")
    public CommonResponse<ConfiguresVO> deleteById(@RequestBody CommonRequest<ConfiguresUpdateDTO> commonRequest) throws IOException {
        return configuresService.deleteById(commonRequest);
    }

    @PostMapping("/updateById")
    @ResponseBody
    @ApiOperation(value = "改", httpMethod = "POST")
    public CommonResponse<ConfiguresVO> updateById(@RequestBody CommonRequest<ConfiguresUpdateDTO> commonRequest) throws Exception {
        return configuresService.updateById(commonRequest);
    }


    @PostMapping("/addOrEdit")
    @ResponseBody
    @ApiOperation(value = "增or改", httpMethod = "POST")
    public CommonResponse<ConfiguresVO> addOrEdit(@RequestBody CommonRequest<ConfiguresUpdateDTO> commonRequest) throws Exception {
        return configuresService.addOrEdit(commonRequest);
    }

    @PostMapping("/getById")
    @ResponseBody
    @ApiOperation(value = "查 - 单条", httpMethod = "POST")
    public CommonResponse<ConfiguresVO> getById(@RequestBody CommonRequest<ConfiguresQueryDTO> commonRequest) {
        return configuresService.getById(commonRequest);
    }

    @PostMapping("/getPage")
    @ResponseBody
    @ApiOperation(value = "查 - 分页", httpMethod = "POST")
    public ANTDPPageResponseVO<Configures> getPage(@RequestBody CommonRequest<ConfiguresQueryDTO> commonRequest) throws Exception {
        return ResponseConvertUtil.convert(configuresService.getPage(commonRequest));
    }

    @PostMapping("/gen")
    @ResponseBody
    @ApiOperation(value = "生成", httpMethod = "POST")
    public CommonResponse<ConfiguresExecuteVO> gen(@RequestBody CommonRequest<ConfiguresQueryDTO> commonRequest) {
        return configuresService.gen(commonRequest);
    }

}