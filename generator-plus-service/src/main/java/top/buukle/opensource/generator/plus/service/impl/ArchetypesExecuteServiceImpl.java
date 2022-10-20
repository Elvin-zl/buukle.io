package top.buukle.opensource.generator.plus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.buukle.login.cube.session.OperatorUserDTO;
import top.buukle.login.cube.session.SessionUtils;
import top.buukle.login.cube.session.tenant.TenantHelper;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.commons.call.PageResponse;
import top.buukle.opensource.generator.plus.commons.status.StatusConstants;
import top.buukle.opensource.generator.plus.dao.ArchetypesExecuteMapper;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.enums.ArchetypesExecuteEnums;
import top.buukle.opensource.generator.plus.dtvo.vo.archetypesExecute.ArchetypesExecuteVO;
import top.buukle.opensource.generator.plus.entity.ArchetypesExecute;
import top.buukle.opensource.generator.plus.service.ArchetypesExecuteService;
import top.buukle.opensource.generator.plus.service.constants.SystemReturnEnum;
import top.buukle.opensource.generator.plus.service.exception.SystemException;
import top.buukle.opensource.generator.plus.utils.DateUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author elvin
* @description ArchetypesExecuteService实现类
*/
@Service("archetypesExecuteService")
public class ArchetypesExecuteServiceImpl extends ServiceImpl<ArchetypesExecuteMapper, ArchetypesExecute> implements ArchetypesExecuteService<ArchetypesExecute, ArchetypesExecuteVO, ArchetypesExecuteQueryDTO, ArchetypesExecuteUpdateDTO> {


    /**
     * @description 增
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesExecuteVO> add(CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest) {
        ArchetypesExecuteUpdateDTO archetypesExecuteUpdateDTO = commonRequest.getBody();
        // 转换DTO
        ArchetypesExecute archetypesExecute = new ArchetypesExecute();
        BeanUtils.copyProperties(archetypesExecuteUpdateDTO,archetypesExecute);
        // 初始字段
        this.savePre(archetypesExecute);
        archetypesExecute.setStatus(ArchetypesExecuteEnums.status.PUBLISHED.value());
        // 落库
        super.save(archetypesExecute);
        // 返回
        ArchetypesExecuteVO archetypesExecuteVO = new ArchetypesExecuteVO();
        BeanUtils.copyProperties(archetypesExecute, archetypesExecuteVO);
        return new CommonResponse.Builder().buildSuccess(archetypesExecuteVO);
    }

    /**
     * @description 增or改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesExecuteVO> addOrEdit(CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest) {
        ArchetypesExecuteUpdateDTO archetypesExecuteUpdateDTO = commonRequest.getBody();
        // 增
        if(archetypesExecuteUpdateDTO.getId() == null){
            return this.add(commonRequest);
        }
        // 改
        else{
            return this.updateById(commonRequest);
        }
    }

    /**
     * @description 删
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesExecuteVO> deleteById(CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest) {
        ArchetypesExecuteUpdateDTO archetypesExecuteUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(archetypesExecuteUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        ArchetypesExecute archetypesExecute = new ArchetypesExecute();
        BeanUtils.copyProperties(archetypesExecuteUpdateDTO,archetypesExecute);
        this.updatePre(archetypesExecute);
        archetypesExecute.setStatus(StatusConstants.DELETED);
        // 落库
        super.updateById(archetypesExecute);
        // 返回
        ArchetypesExecuteVO archetypesExecuteVO = new ArchetypesExecuteVO();
        BeanUtils.copyProperties(archetypesExecute, archetypesExecuteVO);
        return new CommonResponse.Builder().buildSuccess(archetypesExecuteVO);
    }

    /**
     * @description 改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesExecuteVO> updateById(CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest) {
        ArchetypesExecuteUpdateDTO archetypesExecuteUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(archetypesExecuteUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        ArchetypesExecute archetypesExecute = new ArchetypesExecute();
        BeanUtils.copyProperties(archetypesExecuteUpdateDTO,archetypesExecute);
        // 更新字段
        this.updatePre(archetypesExecute);
        // 落库
        super.updateById(archetypesExecute);
        // 返回
        ArchetypesExecuteVO archetypesExecuteVO = new ArchetypesExecuteVO();
        BeanUtils.copyProperties(archetypesExecute, archetypesExecuteVO);
        return new CommonResponse.Builder().buildSuccess(archetypesExecuteVO);
    }

    /**
     * @description  查 - 单条
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<top.buukle.generator.entity.vo.archetypesExecute.ArchetypesExecuteQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesExecuteVO> getById(CommonRequest<ArchetypesExecuteQueryDTO> commonRequest) {
        ArchetypesExecuteQueryDTO archetypesExecuteQueryDTO = commonRequest.getBody();
        // 验证参数
        if(archetypesExecuteQueryDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 执行查询
        TenantHelper.startTenant("archetypes_execute");
        ArchetypesExecute one = super.getById(archetypesExecuteQueryDTO.getId());
        // 转换响应
        ArchetypesExecuteVO archetypesExecuteVO = new ArchetypesExecuteVO();
        BeanUtils.copyProperties(one, archetypesExecuteVO);
        CommonResponse<ArchetypesExecuteVO> archetypesExecuteQueryVOCommonResponse = new CommonResponse.Builder().buildSuccess(archetypesExecuteVO);
        return archetypesExecuteQueryVOCommonResponse;
    }

    /**
     * @description 查 - 分页
     * @param commonRequest
     * @return top.buukle.generator.commons.call.PageResponse<top.buukle.generator.entity.vo.archetypesExecute.ArchetypesExecuteQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public PageResponse<ArchetypesExecuteVO> getPage(CommonRequest<ArchetypesExecuteQueryDTO> commonRequest) {
        // 转换DTO
        ArchetypesExecuteQueryDTO archetypesExecuteQueryDTO = commonRequest.getBody();
        ArchetypesExecute archetypesExecute = new ArchetypesExecute();
        BeanUtils.copyProperties(archetypesExecuteQueryDTO,archetypesExecute);
        // 条件
        LambdaQueryWrapper<ArchetypesExecute> archetypesExecuteLambdaQueryWrapper = this.assPageParam(archetypesExecuteQueryDTO);
        // 查询
        PageHelper.startPage(archetypesExecuteQueryDTO.getPageNo(),archetypesExecuteQueryDTO.getPageSize());
        TenantHelper.startTenant(SqlHelper.table(ArchetypesExecute.class).getTableName());
        List<ArchetypesExecute> list = super.list(archetypesExecuteLambdaQueryWrapper);
        PageInfo<ArchetypesExecute> pageInfo = new PageInfo<>(list);
        // 分页
        List<ArchetypesExecuteVO> queryVOList = new ArrayList<>();
        for (ArchetypesExecute archetypesExecuteDB : list) {
            ArchetypesExecuteVO archetypesExecuteVO = new ArchetypesExecuteVO();
            BeanUtils.copyProperties(archetypesExecuteDB, archetypesExecuteVO);
            queryVOList.add(archetypesExecuteVO);
        }
        return new PageResponse.Builder().build(queryVOList, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
    }

    /**
     * @description 组装分页条件
     * @param archetypesExecuteQueryDTO
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.buukle.generator.entity.model.ArchetypesExecute>
     * @Author 17600
     * @Date 2021/9/2
     */
    private LambdaQueryWrapper<ArchetypesExecute> assPageParam(ArchetypesExecuteQueryDTO archetypesExecuteQueryDTO) {
        LambdaQueryWrapper<ArchetypesExecute> queryWrapper = new LambdaQueryWrapper<>();

        if(StringUtil.isNotEmpty(archetypesExecuteQueryDTO.getStartTime())){
            queryWrapper.ge(ArchetypesExecute::getGmtCreated, DateUtil.parse(archetypesExecuteQueryDTO.getStartTime()));
        }
        if(StringUtil.isNotEmpty(archetypesExecuteQueryDTO.getEndTime())){
            queryWrapper.le(ArchetypesExecute::getGmtCreated, DateUtil.parse(archetypesExecuteQueryDTO.getStartTime()));
        }
        if(archetypesExecuteQueryDTO.getStates() != null){
            queryWrapper.in(ArchetypesExecute::getStatus, Arrays.asList(archetypesExecuteQueryDTO.getStates()));
        }


        // 此处不允许数据库生成的DTO出现基本类型属性,否则生成代码会有问题
        if(null != archetypesExecuteQueryDTO.getId()){
            queryWrapper.eq(ArchetypesExecute::getId,archetypesExecuteQueryDTO.getId());
        }
        if(null != archetypesExecuteQueryDTO.getArchetypesId()){
            queryWrapper.eq(ArchetypesExecute::getArchetypesId,archetypesExecuteQueryDTO.getArchetypesId());
        }
        if(null != archetypesExecuteQueryDTO.getAuditId()){
            queryWrapper.eq(ArchetypesExecute::getAuditId,archetypesExecuteQueryDTO.getAuditId());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getApplicationCode())){
            queryWrapper.eq(ArchetypesExecute::getApplicationCode,archetypesExecuteQueryDTO.getApplicationCode());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getName())){
            queryWrapper.eq(ArchetypesExecute::getName,archetypesExecuteQueryDTO.getName());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getUrl())){
            queryWrapper.eq(ArchetypesExecute::getUrl,archetypesExecuteQueryDTO.getUrl());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getBasePackage())){
            queryWrapper.eq(ArchetypesExecute::getBasePackage,archetypesExecuteQueryDTO.getBasePackage());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getGroupId())){
            queryWrapper.eq(ArchetypesExecute::getGroupId,archetypesExecuteQueryDTO.getGroupId());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getArtifactId())){
            queryWrapper.eq(ArchetypesExecute::getArtifactId,archetypesExecuteQueryDTO.getArtifactId());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getVersion())){
            queryWrapper.eq(ArchetypesExecute::getVersion,archetypesExecuteQueryDTO.getVersion());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getDescription())){
            queryWrapper.eq(ArchetypesExecute::getDescription,archetypesExecuteQueryDTO.getDescription());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getRemark())){
            queryWrapper.eq(ArchetypesExecute::getRemark,archetypesExecuteQueryDTO.getRemark());
        }
        if(null != archetypesExecuteQueryDTO.getGmtCreated()){
            queryWrapper.eq(ArchetypesExecute::getGmtCreated,archetypesExecuteQueryDTO.getGmtCreated());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getCreator())){
            queryWrapper.eq(ArchetypesExecute::getCreator,archetypesExecuteQueryDTO.getCreator());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getCreatorCode())){
            queryWrapper.eq(ArchetypesExecute::getCreatorCode,archetypesExecuteQueryDTO.getCreatorCode());
        }
        if(null != archetypesExecuteQueryDTO.getGmtModified()){
            queryWrapper.eq(ArchetypesExecute::getGmtModified,archetypesExecuteQueryDTO.getGmtModified());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getModifier())){
            queryWrapper.eq(ArchetypesExecute::getModifier,archetypesExecuteQueryDTO.getModifier());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getModifierCode())){
            queryWrapper.eq(ArchetypesExecute::getModifierCode,archetypesExecuteQueryDTO.getModifierCode());
        }
        if(null != archetypesExecuteQueryDTO.getAuditStatus()){
            queryWrapper.eq(ArchetypesExecute::getAuditStatus,archetypesExecuteQueryDTO.getAuditStatus());
        }
        if(null != archetypesExecuteQueryDTO.getStatus()){
            queryWrapper.eq(ArchetypesExecute::getStatus,archetypesExecuteQueryDTO.getStatus());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getBak01())){
            queryWrapper.eq(ArchetypesExecute::getBak01,archetypesExecuteQueryDTO.getBak01());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getBak02())){
            queryWrapper.eq(ArchetypesExecute::getBak02,archetypesExecuteQueryDTO.getBak02());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getBak03())){
            queryWrapper.eq(ArchetypesExecute::getBak03,archetypesExecuteQueryDTO.getBak03());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getBak04())){
            queryWrapper.eq(ArchetypesExecute::getBak04,archetypesExecuteQueryDTO.getBak04());
        }
        if(!StringUtil.isEmpty(archetypesExecuteQueryDTO.getBak05())){
            queryWrapper.eq(ArchetypesExecute::getBak05,archetypesExecuteQueryDTO.getBak05());
        }


        queryWrapper.gt(ArchetypesExecute::getStatus,StatusConstants.DELETED);
        queryWrapper.orderByDesc(ArchetypesExecute::getGmtModified);

        return queryWrapper;
    }

    /**
     * @description 增 - 初始化
     * @param archetypesExecute
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void savePre(ArchetypesExecute archetypesExecute) {
        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        archetypesExecute.setGmtCreated(date);
        archetypesExecute.setCreator(operator.getUsername());
        archetypesExecute.setCreatorCode(operator.getUserId());
        archetypesExecute.setTenantId(Integer.parseInt(StringUtil.isEmpty(operator.getMainTenant())?"-1":operator.getMainTenant()));
        archetypesExecute.setGmtModified(date);

        archetypesExecute.setModifier(operator.getUsername());
        archetypesExecute.setModifierCode(operator.getUserId());
        archetypesExecute.setStatus(ArchetypesExecuteEnums.status.INIT.value());

    }

    /**
     * @description 改 - 初始化
     * @param archetypesExecute
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void updatePre(ArchetypesExecute archetypesExecute) {

        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        archetypesExecute.setGmtModified(date);
        archetypesExecute.setModifier(operator.getUsername());
        archetypesExecute.setModifierCode(operator.getUserId());

    }

    @Override
    public void updateStatus(Integer originStatus, Integer targetStatus, Integer id) {
        UpdateWrapper<ArchetypesExecute> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("status",originStatus);
        updateWrapper.eq("id",id);

        ArchetypesExecute configuresExecute = new ArchetypesExecute();
        configuresExecute.setStatus(targetStatus);
        this.updatePre(configuresExecute);

        super.update(configuresExecute,updateWrapper);
    }

    /*------------------------------------------------------↑↑↑↑通用可定制代码↑↑↑↑-------------------------------------------------------------*/

}
