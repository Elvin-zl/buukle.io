package top.buukle.opensource.generator.plus.service;

import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.commons.mvc.service.BaseService;
import top.buukle.opensource.generator.plus.dtvo.vo.configuresExecute.ConfiguresExecuteVO;

/**
* @author elvin
* @description DatasourcesService 定制化业务接口
*/
public interface ConfiguresService<MODEL, VO,QUERYDTO,UPDATEDTO> extends BaseService<MODEL, VO,QUERYDTO,UPDATEDTO> {

    CommonResponse<ConfiguresExecuteVO> gen(CommonRequest<QUERYDTO> commonRequest);

}
