package top.buukle.opensource.generator.plus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.velocity.app.VelocityEngine;
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
import top.buukle.opensource.generator.plus.service.util.DownloadUtil;
import top.buukle.opensource.generator.plus.utils.DateUtil;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.utils.SystemUtil;
import top.buukle.opensource.generator.plus.dao.ArchetypesMapper;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypes.ArchetypesQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypes.ArchetypesUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute.ArchetypesExecuteUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.enums.ArchetypesEnums;
import top.buukle.opensource.generator.plus.dtvo.enums.ArchetypesExecuteEnums;
import top.buukle.opensource.generator.plus.dtvo.enums.ArchetypesExecuteEnums.status;
import top.buukle.opensource.generator.plus.dtvo.vo.archetypes.ArchetypesVO;
import top.buukle.opensource.generator.plus.dtvo.vo.archetypesExecute.ArchetypesExecuteVO;
import top.buukle.opensource.generator.plus.entity.Archetypes;
import top.buukle.opensource.generator.plus.entity.ArchetypesExecute;
import top.buukle.opensource.generator.plus.service.ArchetypesExecuteService;
import top.buukle.opensource.generator.plus.service.ArchetypesService;
import top.buukle.opensource.generator.plus.service.constants.SystemConstants;
import top.buukle.opensource.generator.plus.service.constants.SystemReturnEnum;
import top.buukle.opensource.generator.plus.service.engine.archetypes.MyArchetypeGenerationRequest;
import top.buukle.opensource.generator.plus.service.engine.archetypes.MyFilesetArchetypeGenerator;
import top.buukle.opensource.generator.plus.service.engine.archetypes.MyVelocityComponent;
import top.buukle.opensource.generator.plus.service.exception.SystemException;
import top.buukle.opensource.generator.plus.service.util.ZipUtil;

import java.io.*;
import java.util.*;

/**
* @author elvin
* @description ArchetypesService实现类
*/
@Service("archetypesService")
public class ArchetypesServiceImpl extends ServiceImpl<ArchetypesMapper, Archetypes> implements ArchetypesService<Archetypes, ArchetypesVO, ArchetypesQueryDTO, ArchetypesUpdateDTO> {

    private static final String FRAME_ARCHETYPE_PATH = "/frame/archetype/";

    private static final String ZIP_CONSTANT = "zip";

    @Autowired
    MyFilesetArchetypeGenerator myFilesetArchetypeGenerator;

    @Autowired
    ArchetypesExecuteService archetypesExecuteService;

    /**
     * @description 增
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesVO> add(CommonRequest<ArchetypesUpdateDTO> commonRequest) {
        ArchetypesUpdateDTO archetypesUpdateDTO = commonRequest.getBody();
        // 转换DTO
        Archetypes archetypes = new Archetypes();
        BeanUtils.copyProperties(archetypesUpdateDTO,archetypes);
        // 初始字段
        this.savePre(archetypes);
        archetypes.setStatus(ArchetypesEnums.status.PUBLISHED.value());
        // 落库
        super.save(archetypes);
        // 返回
        ArchetypesVO archetypesVO = new ArchetypesVO();
        BeanUtils.copyProperties(archetypes, archetypesVO);
        return new CommonResponse.Builder().buildSuccess(archetypesVO);
    }

    /**
     * @description 增or改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesVO> addOrEdit(CommonRequest<ArchetypesUpdateDTO> commonRequest) {
        ArchetypesUpdateDTO archetypesUpdateDTO = commonRequest.getBody();
        // 增
        if(archetypesUpdateDTO.getId() == null){
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
    public CommonResponse<ArchetypesVO> deleteById(CommonRequest<ArchetypesUpdateDTO> commonRequest) {
        ArchetypesUpdateDTO archetypesUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(archetypesUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        Archetypes archetypes = new Archetypes();
        BeanUtils.copyProperties(archetypesUpdateDTO,archetypes);
        this.updatePre(archetypes);
        archetypes.setStatus(StatusConstants.DELETED);
        // 落库
        super.updateById(archetypes);
        // 返回
        ArchetypesVO archetypesVO = new ArchetypesVO();
        BeanUtils.copyProperties(archetypes, archetypesVO);
        return new CommonResponse.Builder().buildSuccess(archetypesVO);
    }

    /**
     * @description 改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesVO> updateById(CommonRequest<ArchetypesUpdateDTO> commonRequest) {
        ArchetypesUpdateDTO archetypesUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(archetypesUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        Archetypes archetypes = new Archetypes();
        BeanUtils.copyProperties(archetypesUpdateDTO,archetypes);
        // 更新字段
        this.updatePre(archetypes);
        // 落库
        super.updateById(archetypes);
        // 返回
        ArchetypesVO archetypesVO = new ArchetypesVO();
        BeanUtils.copyProperties(archetypes, archetypesVO);
        return new CommonResponse.Builder().buildSuccess(archetypesVO);
    }

    /**
     * @description  查 - 单条
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<top.buukle.generator.entity.vo.archetypes.ArchetypesQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<ArchetypesVO> getById(CommonRequest<ArchetypesQueryDTO> commonRequest) {
        ArchetypesQueryDTO archetypesQueryDTO = commonRequest.getBody();
        // 验证参数
        if(archetypesQueryDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 执行查询
        TenantHelper.startTenant("archetypes");
        Archetypes one = super.getById(archetypesQueryDTO.getId());
        // 转换响应
        ArchetypesVO archetypesVO = new ArchetypesVO();
        BeanUtils.copyProperties(one, archetypesVO);
        CommonResponse<ArchetypesVO> archetypesQueryVOCommonResponse = new CommonResponse.Builder().buildSuccess(archetypesVO);
        return archetypesQueryVOCommonResponse;
    }

    /**
     * @description 查 - 分页
     * @param commonRequest
     * @return top.buukle.opensource.generator.plus.commons.call.PageResponse<top.buukle.opensource.generator.plus.dos.vo.archetypes.ArchetypesVO>
     * @Author zhanglei001
     * @Date 2021/9/17
     */
    @Override
    public PageResponse<ArchetypesVO> getPage(CommonRequest<ArchetypesQueryDTO> commonRequest) {
        // 转换DTO
        ArchetypesQueryDTO archetypesQueryDTO = commonRequest.getBody();
        Archetypes archetypes = new Archetypes();
        BeanUtils.copyProperties(archetypesQueryDTO,archetypes);
        // 条件
        QueryWrapper<Archetypes> queryWrapper = this.assPageParam(archetypesQueryDTO);
        // 查询
        PageHelper.startPage(archetypesQueryDTO.getPageNo(),archetypesQueryDTO.getPageSize());
        TenantHelper.startTenant("archetypes");
        List<Archetypes> list = super.list(queryWrapper);
        PageInfo<Archetypes> pageInfo = new PageInfo<>(list);
        // 分页
        List<ArchetypesVO> queryVOList = new ArrayList<>();
        for (Archetypes archetypesDB : list) {
            ArchetypesVO archetypesVO = new ArchetypesVO();
            BeanUtils.copyProperties(archetypesDB, archetypesVO);
            queryVOList.add(archetypesVO);
        }
        return new PageResponse.Builder().build(queryVOList, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
    }

    /**
     * @description 组装分页条件
     * @param archetypesQueryDTO
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.buukle.generator.entity.model.Archetypes>
     * @Author 17600
     * @Date 2021/9/2
     */
    private QueryWrapper<Archetypes> assPageParam( ArchetypesQueryDTO archetypesQueryDTO) {
        QueryWrapper<Archetypes> queryWrapper = new QueryWrapper<>();
        if(StringUtil.isNotEmpty(archetypesQueryDTO.getStartTime())){
            queryWrapper.ge("gmt_created", DateUtil.parse(archetypesQueryDTO.getStartTime()));
        }
        if(StringUtil.isNotEmpty(archetypesQueryDTO.getEndTime())){
            queryWrapper.le("gmt_created", DateUtil.parse(archetypesQueryDTO.getStartTime()));
        }
        queryWrapper.gt("status",StatusConstants.DELETED);
        queryWrapper.orderByDesc("gmt_modified");
        // 可按需扩展 ...
        return queryWrapper;
    }

    /**
     * @description 增 - 初始化
     * @param archetypes
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void savePre(Archetypes archetypes) {
        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        archetypes.setGmtCreated(date);
        archetypes.setCreator(operator.getUsername());
        archetypes.setCreatorCode(operator.getUserId());
        archetypes.setTenantId(Integer.parseInt(StringUtil.isEmpty(operator.getMainTenant())?"-1":operator.getMainTenant()));
        archetypes.setGmtModified(date);

        archetypes.setModifier(operator.getUsername());
        archetypes.setModifierCode(operator.getUserId());
        archetypes.setStatus(ArchetypesEnums.status.INIT.value());

    }

    /**
     * @description 改 - 初始化
     * @param archetypes
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void updatePre(Archetypes archetypes) {

        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        archetypes.setGmtModified(date);
        archetypes.setModifier(operator.getUsername());
        archetypes.setModifierCode(operator.getUserId());

    }


    /*------------------------------------------------------↑↑↑↑通用可定制代码↑↑↑↑-------------------------------------------------------------*/

    /**
     * @description 生成
     * @param commonRequest
     * @return top.buukle.opensource.generator.plus.commons.call.CommonResponse<top.buukle.opensource.generator.plus.dos.vo.archetypesExecute.ArchetypesExecuteVO>
     * @Author zhanglei001
     * @Date 2021/9/17
     */
    @Override
    public CommonResponse<ArchetypesExecuteVO> execute(CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest) throws Exception{

        ArchetypesExecuteUpdateDTO archetypesExecuteUpdateDTO = this.validateParamForGen(commonRequest);
        String genBatchUuid = UUID.randomUUID().toString().replace( StringUtil.MIDDLE, StringUtil.EMPTY);
        Archetypes archetypes = this.getById(archetypesExecuteUpdateDTO.getArchetypesId());
        if(archetypes == null){
            throw new SystemException(SystemReturnEnum.GEN_ARCHETYPES_GEN_ARCHETYPES_NULL);
        }
        archetypesExecuteUpdateDTO.setName(archetypes.getName());
        // 将web路径转为本地缓存路径
        String url = archetypes.getUrl();
        // 下载文件流
        InputStream tempStream = DownloadUtil.getStreamDownloadOutFile(url);
        // 声明并初始化临时文件名
        String tempArchetypeFilePath = SystemUtil.getStoreDir() + StringUtil.BACKSLASH + genBatchUuid + StringUtil.BACKSLASH + genBatchUuid + ".jar";
        // 声明并初始化临时文件
        File tempArchetypeFile = new File(tempArchetypeFilePath);
        // 写入临时文件
        OutputStream os = new FileOutputStream(tempArchetypeFile);
        int bytesRead ;
        byte[] buffer = new byte[8192];
        while ((bytesRead = tempStream.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        tempStream.close();
        // 执行日志落库
        CommonRequest<ArchetypesExecuteUpdateDTO> executeUpdateDTOCommonRequest = new CommonRequest<>();
        executeUpdateDTOCommonRequest.setHead(commonRequest.getHead());
        executeUpdateDTOCommonRequest.setBody(archetypesExecuteUpdateDTO);
        CommonResponse<ArchetypesExecuteVO> response = archetypesExecuteService.add(executeUpdateDTOCommonRequest);
        ArchetypesExecuteVO archetypesExecuteVO = response.getBody();
        // 更新日志状态 - 执行中
        archetypesExecuteService.updateStatus(status.PUBLISHED.value(), status.EXECUTING.value(),archetypesExecuteVO.getId());
        // 开始执行
        String generatedArchetypeIdDir = SystemUtil.getStoreDir() + FRAME_ARCHETYPE_PATH + archetypesExecuteUpdateDTO.getArchetypesId() + StringUtil.BACKSLASH ;

        String generatedArchetypeIdBatchDir = generatedArchetypeIdDir +  genBatchUuid + StringUtil.BACKSLASH;

        MyArchetypeGenerationRequest archetypeGenerationRequest = new MyArchetypeGenerationRequest();

        archetypeGenerationRequest.setInteractiveMode(false);
        archetypeGenerationRequest.setArtifactId(archetypesExecuteUpdateDTO.getArtifactId());
        archetypeGenerationRequest.setGroupId(archetypesExecuteUpdateDTO.getGroupId());
        archetypeGenerationRequest.setPackage(archetypesExecuteUpdateDTO.getBasePackage());
        archetypeGenerationRequest.setVersion(archetypesExecuteUpdateDTO.getVersion());

        archetypeGenerationRequest.setOutputDirectory(generatedArchetypeIdBatchDir);

        MyVelocityComponent myVelocityComponent = new MyVelocityComponent();

        Properties properties = new Properties();
        properties.setProperty("resource.loader", "jar");
        properties.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");
        properties.setProperty("jar.resource.loader.path", "jar:file:" + tempArchetypeFilePath);

        VelocityEngine velocityEngine = new VelocityEngine(properties);

        myVelocityComponent.setEngine(velocityEngine);
        myVelocityComponent.initialize();

        archetypeGenerationRequest.setMyVelocityComponent(myVelocityComponent);
        try {
            myFilesetArchetypeGenerator.generateArchetype(archetypeGenerationRequest,new File(tempArchetypeFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            // 更新日志状态 - 执行失败
            archetypesExecuteService.updateStatus(status.EXECUTING.value(), status.EXECUTE_FAILED.value(),archetypesExecuteVO.getId());
            throw new SystemException(SystemReturnEnum.FAILED,e.getMessage()+e.getCause());
        }
        String zipFilePath = generatedArchetypeIdDir + genBatchUuid + StringUtil.DOT + ZIP_CONSTANT;
        ZipUtil.compress(generatedArchetypeIdBatchDir,new File(zipFilePath));

        String webZipFileUrl = zipFilePath.replaceFirst(SystemUtil.getStoreDir(), SystemConstants.SOFT_CONTEXT_PATH + "upload/temp");
        // 更新url
        CommonRequest<ArchetypesExecuteUpdateDTO> executeUpdateDTOCommonRequestForUrl = new CommonRequest<>();
        executeUpdateDTOCommonRequestForUrl.setHead(commonRequest.getHead());
        ArchetypesExecuteUpdateDTO archetypesExecuteUpdateDTOForUrl = new ArchetypesExecuteUpdateDTO();
        archetypesExecuteUpdateDTOForUrl.setUrl(webZipFileUrl);
        archetypesExecuteUpdateDTOForUrl.setId(archetypesExecuteVO.getId());
        executeUpdateDTOCommonRequestForUrl.setBody(archetypesExecuteUpdateDTOForUrl);
        archetypesExecuteService.updateById(executeUpdateDTOCommonRequestForUrl);
        // 更新日志状态 - 执行成功
        archetypesExecuteService.updateStatus(ArchetypesExecuteEnums.status.EXECUTING.value(), status.EXECUTE_SUCCESS.value(),archetypesExecuteVO.getId());
        return new CommonResponse.Builder().buildSuccess(archetypesExecuteVO);
    }

    /**
     * @description 获取最近一次的执行日志
     * @param commonRequest
     * @return top.buukle.opensource.generator.plus.commons.call.CommonResponse<top.buukle.opensource.generator.plus.dos.vo.archetypesExecute.ArchetypesExecuteVO>
     * @Author zhanglei001
     * @Date 2021/9/17
     */
    @Override
    public CommonResponse<ArchetypesExecuteVO> getLastedLogById(CommonRequest<ArchetypesQueryDTO> commonRequest) {
        QueryWrapper<ArchetypesExecute> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("archetypes_id",commonRequest.getBody().getId());
        queryWrapper.orderByDesc("gmt_created");
        queryWrapper.last("limit 1");
        ArchetypesExecuteVO archetypesExecuteVO = new ArchetypesExecuteVO();
        ArchetypesExecute archetypesExecute = (ArchetypesExecute) archetypesExecuteService.getOne(queryWrapper);
        BeanUtils.copyProperties(archetypesExecute,archetypesExecuteVO);
        return new CommonResponse.Builder().buildSuccess(archetypesExecuteVO);
    }

    private ArchetypesExecuteUpdateDTO validateParamForGen(CommonRequest<ArchetypesExecuteUpdateDTO> commonRequest) {

        if(commonRequest.getBody() == null || commonRequest == null){
            throw new SystemException(SystemReturnEnum.GEN_ARCHETYPES_GEN_PARAM_NULL);
        }

        return commonRequest.getBody();
    }
}
