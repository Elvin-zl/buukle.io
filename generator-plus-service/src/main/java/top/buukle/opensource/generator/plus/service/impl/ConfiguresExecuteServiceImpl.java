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
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.commons.call.PageResponse;
import top.buukle.opensource.generator.plus.commons.status.StatusConstants;
import top.buukle.opensource.generator.plus.utils.DateUtil;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.dao.ConfiguresExecuteMapper;
import top.buukle.opensource.generator.plus.dtvo.dto.configuresExecute.ConfiguresExecuteQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.configuresExecute.ConfiguresExecuteUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.enums.ConfiguresExecuteEnums;
import top.buukle.opensource.generator.plus.dtvo.vo.configuresExecute.ConfiguresExecuteVO;
import top.buukle.opensource.generator.plus.entity.ConfiguresExecute;
import top.buukle.opensource.generator.plus.service.ConfiguresExecuteService;
import top.buukle.opensource.generator.plus.service.constants.SystemReturnEnum;
import top.buukle.opensource.generator.plus.service.exception.SystemException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author elvin
* @description ConfiguresExecuteService实现类
*/
@Service("configuresExecuteService")
public class ConfiguresExecuteServiceImpl extends ServiceImpl<ConfiguresExecuteMapper, ConfiguresExecute> implements ConfiguresExecuteService<ConfiguresExecute, ConfiguresExecuteVO, ConfiguresExecuteQueryDTO, ConfiguresExecuteUpdateDTO> {


    /**
     * @description 增
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ConfiguresExecuteVO> add(CommonRequest<ConfiguresExecuteUpdateDTO> commonRequest) {
        ConfiguresExecuteUpdateDTO configuresExecuteUpdateDTO = commonRequest.getBody();
        // 转换DTO
        ConfiguresExecute configuresExecute = new ConfiguresExecute();
        BeanUtils.copyProperties(configuresExecuteUpdateDTO,configuresExecute);
        // 初始字段
        this.savePre(configuresExecute);
        configuresExecute.setStatus(ConfiguresExecuteEnums.status.PUBLISHED.value());
        // 落库
        super.save(configuresExecute);
        // 返回
        ConfiguresExecuteVO configuresExecuteVO = new ConfiguresExecuteVO();
        BeanUtils.copyProperties(configuresExecute, configuresExecuteVO);
        return new CommonResponse.Builder().buildSuccess(configuresExecuteVO);
    }

    /**
     * @description 增or改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ConfiguresExecuteVO> addOrEdit(CommonRequest<ConfiguresExecuteUpdateDTO> commonRequest) {
        ConfiguresExecuteUpdateDTO configuresExecuteUpdateDTO = commonRequest.getBody();
        // 增
        if(configuresExecuteUpdateDTO.getId() == null){
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
    public CommonResponse<ConfiguresExecuteVO> deleteById(CommonRequest<ConfiguresExecuteUpdateDTO> commonRequest) {
        ConfiguresExecuteUpdateDTO configuresExecuteUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(configuresExecuteUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        ConfiguresExecute configuresExecute = new ConfiguresExecute();
        BeanUtils.copyProperties(configuresExecuteUpdateDTO,configuresExecute);
        this.updatePre(configuresExecute);
        configuresExecute.setStatus(StatusConstants.DELETED);
        // 落库
        super.updateById(configuresExecute);
        // 返回
        ConfiguresExecuteVO configuresExecuteVO = new ConfiguresExecuteVO();
        BeanUtils.copyProperties(configuresExecute, configuresExecuteVO);
        return new CommonResponse.Builder().buildSuccess(configuresExecuteVO);
    }

    /**
     * @description 改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ConfiguresExecuteVO> updateById(CommonRequest<ConfiguresExecuteUpdateDTO> commonRequest) {
        ConfiguresExecuteUpdateDTO configuresExecuteUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(configuresExecuteUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        ConfiguresExecute configuresExecute = new ConfiguresExecute();
        BeanUtils.copyProperties(configuresExecuteUpdateDTO,configuresExecute);
        // 更新字段
        this.updatePre(configuresExecute);
        // 落库
        super.updateById(configuresExecute);
        // 返回
        ConfiguresExecuteVO configuresExecuteVO = new ConfiguresExecuteVO();
        BeanUtils.copyProperties(configuresExecute, configuresExecuteVO);
        return new CommonResponse.Builder().buildSuccess(configuresExecuteVO);
    }

    /**
     * @description  查 - 单条
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<top.buukle.generator.entity.vo.ConfiguresExecute.ConfiguresExecuteQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ConfiguresExecuteVO> getById(CommonRequest<ConfiguresExecuteQueryDTO> commonRequest) {
        ConfiguresExecuteQueryDTO ConfiguresExecuteQueryDTO = commonRequest.getBody();
        // 验证参数
        if(ConfiguresExecuteQueryDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 执行查询
        TenantHelper.startTenant("configures_execute");
        ConfiguresExecute one = super.getById(ConfiguresExecuteQueryDTO.getId());
        // 转换响应
        ConfiguresExecuteVO ConfiguresExecuteVO = new ConfiguresExecuteVO();
        BeanUtils.copyProperties(one, ConfiguresExecuteVO);
        CommonResponse<ConfiguresExecuteVO> ConfiguresExecuteQueryVOCommonResponse = new CommonResponse.Builder().buildSuccess(ConfiguresExecuteVO);
        return ConfiguresExecuteQueryVOCommonResponse;
    }

    /**
     * @description 查 - 分页
     * @param commonRequest
     * @return top.buukle.generator.commons.call.PageResponse<top.buukle.generator.entity.vo.ConfiguresExecute.ConfiguresExecuteQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public PageResponse<ConfiguresExecuteVO> getPage(CommonRequest<ConfiguresExecuteQueryDTO> commonRequest) {
        // 转换DTO
        ConfiguresExecuteQueryDTO configuresExecuteQueryDTO = commonRequest.getBody();
        ConfiguresExecute ConfiguresExecute = new ConfiguresExecute();
        BeanUtils.copyProperties(configuresExecuteQueryDTO,ConfiguresExecute);
        // 条件
        LambdaQueryWrapper<ConfiguresExecute> configuresExecuteLambdaQueryWrapper = this.assPageParam(configuresExecuteQueryDTO);
        // 查询
        PageHelper.startPage(configuresExecuteQueryDTO.getPageNo(),configuresExecuteQueryDTO.getPageSize());
        TenantHelper.startTenant(SqlHelper.table(ConfiguresExecute.class).getTableName());
        List<ConfiguresExecute> list = super.list(configuresExecuteLambdaQueryWrapper);
        PageInfo<ConfiguresExecute> pageInfo = new PageInfo<>(list);
        // 分页
        List<ConfiguresExecuteVO> queryVOList = new ArrayList<>();
        for (ConfiguresExecute ConfiguresExecuteDB : list) {
            ConfiguresExecuteVO ConfiguresExecuteVO = new ConfiguresExecuteVO();
            BeanUtils.copyProperties(ConfiguresExecuteDB, ConfiguresExecuteVO);
            queryVOList.add(ConfiguresExecuteVO);
        }
        return new PageResponse.Builder().build(queryVOList, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
    }

    /**
     * @description 组装分页条件
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.buukle.generator.entity.model.ConfiguresExecute>
     * @Author 17600
     * @Date 2021/9/2
     */
    private LambdaQueryWrapper<ConfiguresExecute> assPageParam(ConfiguresExecuteQueryDTO configuresExecuteQueryDTO) {
        LambdaQueryWrapper<ConfiguresExecute> queryWrapper = new LambdaQueryWrapper<>();

        if(StringUtil.isNotEmpty(configuresExecuteQueryDTO.getStartTime())){
            queryWrapper.ge(ConfiguresExecute::getGmtCreated, DateUtil.parse(configuresExecuteQueryDTO.getStartTime()));
        }
        if(StringUtil.isNotEmpty(configuresExecuteQueryDTO.getEndTime())){
            queryWrapper.le(ConfiguresExecute::getGmtCreated, DateUtil.parse(configuresExecuteQueryDTO.getStartTime()));
        }
        if(configuresExecuteQueryDTO.getStates() != null){
            queryWrapper.in(ConfiguresExecute::getStatus, Arrays.asList(configuresExecuteQueryDTO.getStates()));
        }


        // 此处不允许数据库生成的DTO出现基本类型属性,否则生成代码会有问题
        if(null != configuresExecuteQueryDTO.getId()){
            queryWrapper.eq(ConfiguresExecute::getId,configuresExecuteQueryDTO.getId());
        }
        if(null != configuresExecuteQueryDTO.getAuditId()){
            queryWrapper.eq(ConfiguresExecute::getAuditId,configuresExecuteQueryDTO.getAuditId());
        }
        if(null != configuresExecuteQueryDTO.getConfiguresId()){
            queryWrapper.eq(ConfiguresExecute::getConfiguresId,configuresExecuteQueryDTO.getConfiguresId());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getZipDownUrl())){
            queryWrapper.eq(ConfiguresExecute::getZipDownUrl,configuresExecuteQueryDTO.getZipDownUrl());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getApplicationCode())){
            queryWrapper.eq(ConfiguresExecute::getApplicationCode,configuresExecuteQueryDTO.getApplicationCode());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getDirLocation())){
            queryWrapper.eq(ConfiguresExecute::getDirLocation,configuresExecuteQueryDTO.getDirLocation());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getBasePackage())){
            queryWrapper.eq(ConfiguresExecute::getBasePackage,configuresExecuteQueryDTO.getBasePackage());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getName())){
            queryWrapper.eq(ConfiguresExecute::getName,configuresExecuteQueryDTO.getName());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getUrl())){
            queryWrapper.eq(ConfiguresExecute::getUrl,configuresExecuteQueryDTO.getUrl());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getTables())){
            queryWrapper.eq(ConfiguresExecute::getTables,configuresExecuteQueryDTO.getTables());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getDatabasesInfo())){
            queryWrapper.eq(ConfiguresExecute::getDatabasesInfo,configuresExecuteQueryDTO.getDatabasesInfo());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getTemplatesInfo())){
            queryWrapper.eq(ConfiguresExecute::getTemplatesInfo,configuresExecuteQueryDTO.getTemplatesInfo());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getDescription())){
            queryWrapper.eq(ConfiguresExecute::getDescription,configuresExecuteQueryDTO.getDescription());
        }
        if(!StringUtil.isEmpty(configuresExecuteQueryDTO.getRemark())){
            queryWrapper.eq(ConfiguresExecute::getRemark,configuresExecuteQueryDTO.getRemark());
        }

        queryWrapper.gt(ConfiguresExecute::getStatus,StatusConstants.DELETED);
        queryWrapper.orderByDesc(ConfiguresExecute::getGmtModified);

        return queryWrapper;
    }

    /**
     * @description 增 - 初始化
     * @param ConfiguresExecute
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void savePre(ConfiguresExecute ConfiguresExecute) {
        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        ConfiguresExecute.setGmtCreated(date);
        ConfiguresExecute.setCreator(operator.getUsername());
        ConfiguresExecute.setCreatorCode(operator.getUserId());
        ConfiguresExecute.setTenantId(Integer.parseInt(StringUtil.isEmpty(operator.getMainTenant())?"-1":operator.getMainTenant()));
        ConfiguresExecute.setGmtModified(date);

        ConfiguresExecute.setModifier(operator.getUsername());
        ConfiguresExecute.setModifierCode(operator.getUserId());
        ConfiguresExecute.setStatus(ConfiguresExecuteEnums.status.INIT.value());

    }

    /**
     * @description 改 - 初始化
     * @param ConfiguresExecute
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void updatePre(ConfiguresExecute ConfiguresExecute) {

        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        ConfiguresExecute.setGmtModified(date);
        ConfiguresExecute.setModifier(operator.getUsername());
        ConfiguresExecute.setModifierCode(operator.getUserId());

    }

    /**
     * @description 更新状态 - 行锁
     * @param originStatus
     * @param targetStatus
     * @param id
     * @return void
     * @Author 17600
     * @Date 2021/9/4
     */
    @Override
    public void updateStatus(Integer originStatus, Integer targetStatus, Integer id) {

        UpdateWrapper<ConfiguresExecute> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("status",originStatus);
        updateWrapper.eq("id",id);

        ConfiguresExecute configuresExecute = new ConfiguresExecute();
        configuresExecute.setStatus(targetStatus);

        super.update(configuresExecute,updateWrapper);
    }
}
