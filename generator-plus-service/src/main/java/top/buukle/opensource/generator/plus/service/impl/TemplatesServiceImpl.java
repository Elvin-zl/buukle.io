package top.buukle.opensource.generator.plus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.buukle.login.cube.session.OperatorUserDTO;
import top.buukle.login.cube.session.SessionUtils;

import top.buukle.login.cube.session.tenant.TenantHelper;
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.commons.call.PageResponse;
import top.buukle.opensource.generator.plus.commons.status.StatusConstants;
import top.buukle.opensource.generator.plus.dtvo.dto.templatesGroup.TemplatesGroupUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.enums.TemplatesGroupEnums.status;
import top.buukle.opensource.generator.plus.entity.TemplatesGroup;
import top.buukle.opensource.generator.plus.service.TemplatesGroupService;
import top.buukle.opensource.generator.plus.utils.DateUtil;
import top.buukle.opensource.generator.plus.utils.JsonUtil;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.dao.TemplatesMapper;
import top.buukle.opensource.generator.plus.dtvo.dto.templates.TemplatesQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.templates.TemplatesUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.enums.TemplatesEnums;
import top.buukle.opensource.generator.plus.dtvo.vo.templates.TemplatesVO;
import top.buukle.opensource.generator.plus.entity.Templates;
import top.buukle.opensource.generator.plus.service.TemplatesService;
import top.buukle.opensource.generator.plus.service.constants.SystemReturnEnum;
import top.buukle.opensource.generator.plus.service.exception.SystemException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author elvin
* @description TemplatesService实现类
*/
@Service("templatesService")
public class TemplatesServiceImpl extends ServiceImpl<TemplatesMapper, Templates> implements TemplatesService<Templates, TemplatesVO, TemplatesQueryDTO, TemplatesUpdateDTO> {

    @Autowired
    TemplatesGroupService templatesGroupService;

    /**
     * @description 增
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<TemplatesVO> add(CommonRequest<TemplatesUpdateDTO> commonRequest) {
        TemplatesUpdateDTO templatesUpdateDTO = commonRequest.getBody();
        // 转换DTO
        Templates templates = new Templates();
        BeanUtils.copyProperties(templatesUpdateDTO,templates);
        // 初始字段
        this.savePre(templates);
        templates.setStatus(TemplatesEnums.status.PUBLISHED.value());
        // 落库
        super.save(templates);
        TemplatesVO templatesVO = new TemplatesVO();
        BeanUtils.copyProperties(templates, templatesVO);
        // 返回
        return new CommonResponse.Builder().buildSuccess(templatesVO);
    }

    /**
     * @description 增or改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<TemplatesVO> addOrEdit(CommonRequest<TemplatesUpdateDTO> commonRequest) {
        TemplatesUpdateDTO templatesUpdateDTO = commonRequest.getBody();
        // 增
        if(templatesUpdateDTO.getId() == null){
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
    public CommonResponse<TemplatesVO> deleteById(CommonRequest<TemplatesUpdateDTO> commonRequest) {
        TemplatesUpdateDTO templatesUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(templatesUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        Templates templates = new Templates();
        BeanUtils.copyProperties(templatesUpdateDTO,templates);
        this.updatePre(templates);
        templates.setStatus(StatusConstants.DELETED);
        // 落库
        super.updateById(templates);
        // 返回
        TemplatesVO templatesVO = new TemplatesVO();
        BeanUtils.copyProperties(templates, templatesVO);
        return new CommonResponse.Builder().buildSuccess(templatesVO);
    }

    /**
     * @description 改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<TemplatesVO> updateById(CommonRequest<TemplatesUpdateDTO> commonRequest) {
        TemplatesUpdateDTO templatesUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(templatesUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        Templates templates = new Templates();
        BeanUtils.copyProperties(templatesUpdateDTO,templates);
        // 更新字段
        this.updatePre(templates);
        // 落库
        super.updateById(templates);
        // 返回
        TemplatesVO templatesVO = new TemplatesVO();
        BeanUtils.copyProperties(templates, templatesVO);
        return new CommonResponse.Builder().buildSuccess(templatesVO);
    }

    /**
     * @description  查 - 单条
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<top.buukle.generator.entity.vo.Templates.TemplatesQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<TemplatesVO> getById(CommonRequest<TemplatesQueryDTO> commonRequest) {
        TemplatesQueryDTO TemplatesQueryDTO = commonRequest.getBody();
        // 验证参数
        if(TemplatesQueryDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 执行查询
        TenantHelper.startTenant("templates");
        Templates one = super.getById(TemplatesQueryDTO.getId());
        // 转换响应
        TemplatesVO templatesVO = new TemplatesVO();
        BeanUtils.copyProperties(one, templatesVO);
        if(one.getTemplatesGroupId() == null){
            throw new SystemException(SystemReturnEnum.TEMPLATES_INFO_GROUP_ID_NULL);
        }
        TemplatesGroup byId = (TemplatesGroup) templatesGroupService.getById(one.getTemplatesGroupId());
        if(byId == null || byId.getStatus() != status.PUBLISHED.value()){
            throw new SystemException(SystemReturnEnum.TEMPLATES_INFO_GROUP_STATUS_WRONG);
        }
        templatesVO.setTemplatesGroupName(byId.getName());
        templatesVO.setTemplatesGroupParamList(JsonUtil.parseArray(byId.getParams(), TemplatesVO.Param.class));
        CommonResponse<TemplatesVO> TemplatesQueryVOCommonResponse = new CommonResponse.Builder().buildSuccess(templatesVO);
        return TemplatesQueryVOCommonResponse;
    }

    /**
     * @description 查 - 分页
     * @param commonRequest
     * @return top.buukle.generator.commons.call.PageResponse<top.buukle.generator.entity.vo.Templates.TemplatesQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public PageResponse<TemplatesVO> getPage(CommonRequest<TemplatesQueryDTO> commonRequest) {
        // 转换DTO
        TemplatesQueryDTO TemplatesQueryDTO = commonRequest.getBody();
        Templates Templates = new Templates();
        BeanUtils.copyProperties(TemplatesQueryDTO,Templates);
        // 条件
        LambdaQueryWrapper<top.buukle.opensource.generator.plus.entity.Templates> templatesLambdaQueryWrapper = this.assPageParam(TemplatesQueryDTO);
        // 查询
        PageHelper.startPage(TemplatesQueryDTO.getPageNo(),TemplatesQueryDTO.getPageSize());
        TenantHelper.startTenant(SqlHelper.table(Templates.class).getTableName());
        List<Templates> list = super.list(templatesLambdaQueryWrapper);
        PageInfo<Templates> pageInfo = new PageInfo<>(list);
        // 分页
        List<TemplatesVO> queryVOList = new ArrayList<>();
        for (Templates templatesDB : list) {
            TemplatesVO templatesVO = new TemplatesVO();
            BeanUtils.copyProperties(templatesDB, templatesVO);
            TemplatesGroup byId = (TemplatesGroup) templatesGroupService.getById(templatesDB.getTemplatesGroupId());
            if(byId == null || byId.getStatus() != status.PUBLISHED.value()){
                throw new SystemException(SystemReturnEnum.TEMPLATES_INFO_GROUP_STATUS_WRONG);
            }
            templatesVO.setTemplatesGroupName(byId.getName());
            queryVOList.add(templatesVO);
        }
        return new PageResponse.Builder().build(queryVOList, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
    }

    /**
     * @description 组装分页条件
     * @param templatesQueryDTO
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.buukle.generator.entity.model.Templates>
     * @Author 17600
     * @Date 2021/9/2
     */
    private LambdaQueryWrapper<Templates> assPageParam(TemplatesQueryDTO templatesQueryDTO) {
        LambdaQueryWrapper<Templates> queryWrapper = new LambdaQueryWrapper<>();

        if(StringUtil.isNotEmpty(templatesQueryDTO.getStartTime())){
            queryWrapper.ge(Templates::getGmtCreated, DateUtil.parse(templatesQueryDTO.getStartTime()));
        }
        if(StringUtil.isNotEmpty(templatesQueryDTO.getEndTime())){
            queryWrapper.le(Templates::getGmtCreated, DateUtil.parse(templatesQueryDTO.getStartTime()));
        }
        if(templatesQueryDTO.getStates() != null){
            queryWrapper.in(Templates::getStatus, Arrays.asList(templatesQueryDTO.getStates()));
        }


        // 此处不允许数据库生成的DTO出现基本类型属性,否则生成代码会有问题
        if(null != templatesQueryDTO.getId()){
            queryWrapper.eq(Templates::getId,templatesQueryDTO.getId());
        }
        if(null != templatesQueryDTO.getAuditId()){
            queryWrapper.eq(Templates::getAuditId,templatesQueryDTO.getAuditId());
        }
        if(null != templatesQueryDTO.getTemplatesGroupId()){
            queryWrapper.eq(Templates::getTemplatesGroupId,templatesQueryDTO.getTemplatesGroupId());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getApplicationCode())){
            queryWrapper.eq(Templates::getApplicationCode,templatesQueryDTO.getApplicationCode());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getName())){
            queryWrapper.eq(Templates::getName,templatesQueryDTO.getName());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getAddNameSuffix())){
            queryWrapper.eq(Templates::getAddNameSuffix,templatesQueryDTO.getAddNameSuffix());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getPath())){
            queryWrapper.eq(Templates::getPath,templatesQueryDTO.getPath());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getOpenTablePath())){
            queryWrapper.eq(Templates::getOpenTablePath,templatesQueryDTO.getOpenTablePath());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getUrl())){
            queryWrapper.eq(Templates::getUrl,templatesQueryDTO.getUrl());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getType())){
            queryWrapper.eq(Templates::getType,templatesQueryDTO.getType());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getContent())){
            queryWrapper.eq(Templates::getContent,templatesQueryDTO.getContent());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getDescription())){
            queryWrapper.eq(Templates::getDescription,templatesQueryDTO.getDescription());
        }
        if(!StringUtil.isEmpty(templatesQueryDTO.getRemark())){
            queryWrapper.eq(Templates::getRemark,templatesQueryDTO.getRemark());
        }
        if(null != templatesQueryDTO.getAuditStatus()){
            queryWrapper.eq(Templates::getAuditStatus,templatesQueryDTO.getAuditStatus());
        }
        if(null != templatesQueryDTO.getStatus()){
            queryWrapper.eq(Templates::getStatus,templatesQueryDTO.getStatus());
        }


        queryWrapper.gt(Templates::getStatus,StatusConstants.DELETED);
        queryWrapper.orderByDesc(Templates::getGmtModified);

        return queryWrapper;
    }

    /**
     * @description 增 - 初始化
     * @param Templates
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void savePre(Templates Templates) {
        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        Templates.setGmtCreated(date);
        Templates.setCreator(operator.getUsername());
        Templates.setCreatorCode(operator.getUserId());
        Templates.setTenantId(Integer.parseInt(StringUtil.isEmpty(operator.getMainTenant())?"-1":operator.getMainTenant()));
        Templates.setGmtModified(date);

        Templates.setModifier(operator.getUsername());
        Templates.setModifierCode(operator.getUserId());
        Templates.setStatus(TemplatesEnums.status.INIT.value());

    }

    /**
     * @description 改 - 初始化
     * @param Templates
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void updatePre(Templates Templates) {

        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        Templates.setGmtModified(date);
        Templates.setModifier(operator.getUsername());
        Templates.setModifierCode(operator.getUserId());

    }

    /**
     * @description 根据groupId查询模板列表
     * @param commonRequest
     * @return top.buukle.opensource.generator.plus.commons.call.CommonResponse<java.util.List<top.buukle.opensource.generator.plus.dos.vo.templates.TemplatesVO>>
     * @Author 17600
     * @Date 2021/10/16
     */
    @Override
    public CommonResponse<List<TemplatesVO>> getTemplatesByGroupId(CommonRequest<TemplatesQueryDTO> commonRequest) {
        QueryWrapper<Templates> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("templates_group_id",commonRequest.getBody().getTemplatesGroupId());
        queryWrapper.gt("status",StatusConstants.DELETED);
        List<Templates> list = super.list(queryWrapper);
        List<TemplatesVO> queryVOList = new ArrayList<>();
        for (Templates TemplatesDB : list) {
            TemplatesVO TemplatesVO = new TemplatesVO();
            BeanUtils.copyProperties(TemplatesDB, TemplatesVO);
            queryVOList.add(TemplatesVO);
        }
        return new CommonResponse.Builder().buildSuccess(queryVOList);
    }

    /**
     * @description 根据分组复制模板
     * @param templatesGroupIdSource
     * @param templatesGroupIdTarget
     * @param templatesGroupUpdateDTO
     * @return void
     * @Author zhanglei001
     * @Date 2021/11/29
     */
    @Override
    public void copyByGroupId(Integer templatesGroupIdSource, Integer templatesGroupIdTarget, TemplatesGroupUpdateDTO templatesGroupUpdateDTO) {
        QueryWrapper<Templates> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("templates_group_id",templatesGroupIdSource);
        queryWrapper.gt("status",StatusConstants.DELETED);
        List<Templates> list = super.list(queryWrapper);
        for (Templates templates : list) {
            templates.setId(null);
            templates.setTemplatesGroupId(templatesGroupIdTarget);
            Integer status = templates.getStatus();
            savePre(templates);
            templates.setStatus(status);
            if(StringUtil.isNotEmpty(templates.getPath()) && StringUtil.isNotEmpty(templatesGroupUpdateDTO.getPathToReplace1())){
                templates.setPath(templates.getPath().replace(templatesGroupUpdateDTO.getPathToReplace1(),templatesGroupUpdateDTO.getPathReplaced1() == null ? "" : templatesGroupUpdateDTO.getPathReplaced1()));
            }

            if(StringUtil.isNotEmpty(templates.getPath()) && StringUtil.isNotEmpty(templatesGroupUpdateDTO.getPathToReplace2())){
                templates.setPath(templates.getPath().replace(templatesGroupUpdateDTO.getPathToReplace2(),templatesGroupUpdateDTO.getPathReplaced2() == null ? "" : templatesGroupUpdateDTO.getPathReplaced2()));
            }

            if(StringUtil.isNotEmpty(templates.getPackageInfo()) && StringUtil.isNotEmpty(templatesGroupUpdateDTO.getPackageInfoToReplace())){
                templates.setPackageInfo(templates.getPackageInfo().replace(templatesGroupUpdateDTO.getPackageInfoToReplace(),templatesGroupUpdateDTO.getPackageInfoReplaced() == null ? "" : templatesGroupUpdateDTO.getPackageInfoReplaced()));
            }
        }
        super.saveBatch(list);
    }

    /**
     * @description 根据分组id删除模板
     * @param templatesGroupId
     * @return void
     * @Author zhanglei001
     * @Date 2021/11/30
     */
    @Override
    public void deleteTemplatesByGroupId(Integer templatesGroupId) {
        UpdateWrapper<Templates> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("templates_group_id",templatesGroupId);

        Date date = new Date();
        OperatorUserDTO operator = SessionUtils.getOperator();

        updateWrapper.set("gmt_modified",date);
        updateWrapper.set("modifier",operator.getUsername());
        updateWrapper.set("modifier_code",operator.getUserId());

        updateWrapper.set("status",TemplatesEnums.status.DELETED.value());

        super.update(updateWrapper);
    }
}
