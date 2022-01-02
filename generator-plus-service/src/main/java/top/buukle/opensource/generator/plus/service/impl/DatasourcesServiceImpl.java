package top.buukle.opensource.generator.plus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import top.buukle.login.cube.session.SessionUtils;

import top.buukle.login.cube.session.tenant.TenantHelper;
import top.buukle.opensource.generator.plus.commons.call.CommonRequest;
import top.buukle.opensource.generator.plus.commons.call.CommonResponse;
import top.buukle.opensource.generator.plus.commons.call.PageResponse;
import top.buukle.opensource.generator.plus.commons.status.StatusConstants;
import top.buukle.opensource.generator.plus.utils.DateUtil;
import top.buukle.opensource.generator.plus.utils.StringUtil;
import top.buukle.opensource.generator.plus.dao.DatasourcesMapper;
import top.buukle.opensource.generator.plus.dtvo.dto.datasources.DatasourcesQueryDTO;
import top.buukle.opensource.generator.plus.dtvo.dto.datasources.DatasourcesUpdateDTO;
import top.buukle.opensource.generator.plus.dtvo.enums.DatasourcesEnums;
import top.buukle.opensource.generator.plus.dtvo.vo.datasources.DatasourcesVO;
import top.buukle.opensource.generator.plus.dtvo.vo.tables.TableVo;
import top.buukle.opensource.generator.plus.entity.Datasources;
import top.buukle.opensource.generator.plus.service.DatasourcesService;
import top.buukle.opensource.generator.plus.service.constants.SystemReturnEnum;
import top.buukle.opensource.generator.plus.service.exception.SystemException;
import top.buukle.opensource.generator.plus.service.util.DataBaseUtil;
import top.buukle.opensource.generator.plus.service.util.enums.DatabaseType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author elvin
* @description DatasourcesService实现类
*/
@Service("datasourcesService")
public class DatasourcesServiceImpl extends ServiceImpl<DatasourcesMapper, Datasources> implements DatasourcesService<Datasources, DatasourcesVO, DatasourcesQueryDTO, DatasourcesUpdateDTO> {


    /**
     * @description 增
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<DatasourcesVO> add(CommonRequest<DatasourcesUpdateDTO> commonRequest) {
        DatasourcesUpdateDTO datasourcesUpdateDTO = commonRequest.getBody();
        // 转换DTO
        Datasources datasources = new Datasources();
        BeanUtils.copyProperties(datasourcesUpdateDTO,datasources);
        // 初始字段
        this.savePre(datasources);
        datasources.setStatus(DatasourcesEnums.status.PUBLISHED.value());
        // 落库
        super.save(datasources);
        // 返回
        DatasourcesVO datasourcesVO = new DatasourcesVO();
        BeanUtils.copyProperties(datasources, datasourcesVO);
        return new CommonResponse.Builder().buildSuccess(datasourcesVO);
    }

    /**
     * @description 增or改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<DatasourcesVO> addOrEdit(CommonRequest<DatasourcesUpdateDTO> commonRequest) {
        DatasourcesUpdateDTO datasourcesUpdateDTO = commonRequest.getBody();
        // 增
        if(datasourcesUpdateDTO.getId() == null){
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
    public CommonResponse<DatasourcesVO> deleteById(CommonRequest<DatasourcesUpdateDTO> commonRequest) {
        DatasourcesUpdateDTO datasourcesUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(datasourcesUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        Datasources datasources = new Datasources();
        BeanUtils.copyProperties(datasourcesUpdateDTO,datasources);
        this.updatePre(datasources);
        datasources.setStatus(StatusConstants.DELETED);
        // 落库
        super.updateById(datasources);
        // 返回
        DatasourcesVO datasourcesVO = new DatasourcesVO();
        BeanUtils.copyProperties(datasources, datasourcesVO);
        return new CommonResponse.Builder().buildSuccess(datasourcesVO);
    }

    /**
     * @description 改
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<DatasourcesVO> updateById(CommonRequest<DatasourcesUpdateDTO> commonRequest) {
        DatasourcesUpdateDTO datasourcesUpdateDTO = commonRequest.getBody();
        // 验证参数
        if(datasourcesUpdateDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 转换DTO
        Datasources datasources = new Datasources();
        BeanUtils.copyProperties(datasourcesUpdateDTO,datasources);
        // 更新字段
        this.updatePre(datasources);
        // 落库
        super.updateById(datasources);
        // 返回
        DatasourcesVO datasourcesVO = new DatasourcesVO();
        BeanUtils.copyProperties(datasources, datasourcesVO);
        return new CommonResponse.Builder().buildSuccess(datasourcesVO);
    }

    /**
     * @description  查 - 单条
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<top.buukle.generator.entity.vo.datasources.DatasourcesQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public CommonResponse<DatasourcesVO> getById(CommonRequest<DatasourcesQueryDTO> commonRequest) {
        DatasourcesQueryDTO datasourcesQueryDTO = commonRequest.getBody();
        // 验证参数
        if(datasourcesQueryDTO.getId() == null){
            throw new SystemException(SystemReturnEnum.RUD_ID_NULL);
        }
        // 执行查询
        TenantHelper.startTenant("datasources");
        Datasources one = super.getById(datasourcesQueryDTO.getId());
        // 转换响应
        DatasourcesVO datasourcesVO = new DatasourcesVO();
        BeanUtils.copyProperties(one, datasourcesVO);
        CommonResponse<DatasourcesVO> datasourcesQueryVOCommonResponse = new CommonResponse.Builder().buildSuccess(datasourcesVO);
        return datasourcesQueryVOCommonResponse;
    }

    /**
     * @description 查 - 分页
     * @param commonRequest
     * @return top.buukle.generator.commons.call.PageResponse<top.buukle.generator.entity.vo.datasources.DatasourcesQueryVO>
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public PageResponse<DatasourcesVO> getPage(CommonRequest<DatasourcesQueryDTO> commonRequest) {
        // 转换DTO
        DatasourcesQueryDTO datasourcesQueryDTO = commonRequest.getBody();
        Datasources datasources = new Datasources();
        BeanUtils.copyProperties(datasourcesQueryDTO,datasources);
        // 条件
        QueryWrapper<Datasources> queryWrapper = this.assPageParam(datasourcesQueryDTO);
        // 查询
        PageHelper.startPage(datasourcesQueryDTO.getPageNo(),datasourcesQueryDTO.getPageSize());
        TenantHelper.startTenant("datasources");
        List<Datasources> list = super.list(queryWrapper);
        PageInfo<Datasources> pageInfo = new PageInfo<>(list);
        // 分页
        List<DatasourcesVO> queryVOList = new ArrayList<>();
        for (Datasources datasourcesDB : list) {
            DatasourcesVO datasourcesVO = new DatasourcesVO();
            BeanUtils.copyProperties(datasourcesDB, datasourcesVO);
            queryVOList.add(datasourcesVO);
        }
        return new PageResponse.Builder().build(queryVOList, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
    }

    /**
     * @description 组装分页条件
     * @param datasourcesQueryDTO
     * @return com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<top.buukle.generator.entity.model.Datasources>
     * @Author 17600
     * @Date 2021/9/2
     */
    private QueryWrapper<Datasources> assPageParam( DatasourcesQueryDTO datasourcesQueryDTO) {
        QueryWrapper<Datasources> queryWrapper = new QueryWrapper<>();
        if(StringUtil.isNotEmpty(datasourcesQueryDTO.getStartTime())){
            queryWrapper.ge("gmt_created", DateUtil.parse(datasourcesQueryDTO.getStartTime()));
        }
        if(StringUtil.isNotEmpty(datasourcesQueryDTO.getEndTime())){
            queryWrapper.le("gmt_created", DateUtil.parse(datasourcesQueryDTO.getStartTime()));
        }
        queryWrapper.gt("status",StatusConstants.DELETED);
        queryWrapper.orderByDesc("gmt_modified");
        // 可按需扩展 ...
        return queryWrapper;
    }

    /**
     * @description 增 - 初始化
     * @param datasources
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void savePre(Datasources datasources) {
        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        datasources.setGmtCreated(date);
        datasources.setCreator(operator.getUsername());
        datasources.setCreatorCode(operator.getUserId());
        datasources.setTenantId(Integer.parseInt(StringUtil.isEmpty(operator.getMainTenant())?"-1":operator.getMainTenant()));
        datasources.setGmtModified(date);

        datasources.setModifier(operator.getUsername());
        datasources.setModifierCode(operator.getUserId());
        datasources.setStatus(DatasourcesEnums.status.INIT.value());

    }

    /**
     * @description 改 - 初始化
     * @param datasources
     * @return void
     * @Author 17600
     * @Date 2021/9/2
     */
    @Override
    public void updatePre(Datasources datasources) {

        Date date = new Date();

        OperatorUserDTO operator = SessionUtils.getOperator();

        datasources.setGmtModified(date);
        datasources.setModifier(operator.getUsername());
        datasources.setModifierCode(operator.getUserId());

    }

    /*------------------------------------------------------↑↑↑↑通用可定制代码↑↑↑↑-------------------------------------------------------------*/
    /**
     * @description 测试数据源链接
     * @param commonRequest
     * @return top.buukle.generator.commons.call.CommonResponse<java.lang.Boolean>
     * @Author 17600
     * @Date 2021/9/3
     */
    @Override
    public CommonResponse<Boolean> testLink(CommonRequest<DatasourcesUpdateDTO> commonRequest) throws SQLException {
        DatasourcesUpdateDTO body = commonRequest.getBody();
        Connection connection = DataBaseUtil.getConnection(DatabaseType.MySQL, body.getUsername(), body.getPassword(), body.getUrl());
        connection.close();
        return new CommonResponse.Builder().buildSuccess();
    }

    /**
     * @description 为配置详情页获取下拉列表
     * @param commonRequest
     * @return top.buukle.commons.call.CommonResponse<java.util.List<top.buukle.opensource.generator.plus.dos.vo.datasources.DatasourcesQueryVO>>
     * @Author 17600
     * @Date 2021/9/4
     */
    @Override
    public CommonResponse<List<DatasourcesVO>> getDatasourcesForConfigure(CommonRequest<DatasourcesQueryDTO> commonRequest) {
        // 条件
        QueryWrapper<Datasources> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status",DatasourcesEnums.status.PUBLISHED.value());
        // 查询
        List<Datasources> list = super.list(queryWrapper);
        List<DatasourcesVO> queryVOList = new ArrayList<>();
        for (Datasources datasources : list) {
            DatasourcesVO datasourcesVO = new DatasourcesVO();
            BeanUtils.copyProperties(datasources, datasourcesVO);
            datasourcesVO.setName(datasourcesVO.getName() + "(" + datasourcesVO.getId() + ")");
            queryVOList.add(datasourcesVO);
        }
        return new CommonResponse.Builder().buildSuccess(queryVOList);
    }

    /**
     * @description 获取某个id数据源下的表
     * @param commonRequest
     * @return top.buukle.commons.call.CommonResponse<java.util.List<top.buukle.opensource.generator.plus.dos.vo.tables.TableVo>>
     * @Author 17600
     * @Date 2021/9/4
     */
    @Override
    public CommonResponse<List<TableVo>> getTablesListById(CommonRequest<DatasourcesQueryDTO> commonRequest) throws SQLException {

        DatasourcesQueryDTO body = commonRequest.getBody();
        Datasources datasources = super.getById(body.getId());
        Connection connection = DataBaseUtil.getConnection(DatabaseType.MySQL, datasources.getUsername(), datasources.getPassword(), datasources.getUrl());
        List<String> tables = DataBaseUtil.getTables(connection);
        connection.close();
        ArrayList<TableVo> tableVos = new ArrayList<>();
        for (String table : tables) {
            tableVos.add(new TableVo(table,false));
        }
        return new CommonResponse.Builder().buildSuccess(tableVos);
    }
}
