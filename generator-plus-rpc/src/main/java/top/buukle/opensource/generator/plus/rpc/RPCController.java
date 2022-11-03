package top.buukle.opensource.generator.plus.rpc;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypes.ArchetypesQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteUpdateRPCDTO;
import top.buukle.opensource.generator.plus.dtvo.vo.archetypes.ArchetypesVO;
import top.buukle.opensource.generator.plus.dtvo.vo.archetypesExecute.ArchetypesExecuteVO;
import top.buukle.opensource.generator.plus.service.ArchetypesService;

import java.util.List;

@Controller
@RequestMapping("/rpc")
public class RPCController {

    @Autowired
    ArchetypesService archetypesService;

    @PostMapping("/loadArchetype")
    @ResponseBody
    @ApiOperation(value = "查 - Archetypes", httpMethod = "POST" ,produces = "application/json; charset=utf-8")
    public CommonResponse<List<ArchetypesVO>> loadArchetype(@RequestBody CommonRequest<ArchetypesQueryDTO> commonRequest) throws Exception {
        return archetypesService.loadArchetype(commonRequest);
    }

    @PostMapping("/genArchetype")
    @ResponseBody
    @ApiOperation(value = "生成 - Archetypes", httpMethod = "POST" ,produces = "application/json; charset=utf-8")
    public CommonResponse<ArchetypesExecuteVO> genArchetype(@RequestBody CommonRequest<ArchetypesExecuteUpdateRPCDTO> commonRequest) throws Exception {
        return archetypesService.genArchetype(commonRequest);
    }
}
