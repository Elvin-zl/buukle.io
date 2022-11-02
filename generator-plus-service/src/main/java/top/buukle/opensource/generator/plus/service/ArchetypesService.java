package top.buukle.opensource.generator.plus.service;

import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.commons.mvc.service.BaseService;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypes.ArchetypesQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.vo.archetypes.ArchetypesVO;
import top.buukle.opensource.generator.plus.dtvo.vo.archetypesExecute.ArchetypesExecuteVO;

import java.util.List;

/**
* @author elvin
* @description Archetypes 定制化业务接口
*/
public interface  ArchetypesService<MODEL, VO,QUERYDTO,UPDATEDTO> extends BaseService<MODEL, VO,QUERYDTO,UPDATEDTO> {

    CommonResponse<ArchetypesExecuteVO> execute(CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest) throws Exception;

    CommonResponse<ArchetypesExecuteVO> getLastedLogById(CommonRequest<QUERYDTO> commonRequest);

    CommonResponse<List<ArchetypesVO>> loadArchetype(CommonRequest<ArchetypesQueryDTO> commonRequest);
}
