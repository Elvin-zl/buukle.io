package top.buukle.opensource.generator.plus.dtvo.dto.configuresExecute;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
* @author elvin
* @description  Datasources 查询包装类
*/
@Data
@ApiModel(value = "配置执行 - 查询请求")
public class ConfiguresExecuteQueryDTO {

    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNo = 1;
    /**
     * 页长
     */
    @ApiModelProperty(value = "页长")
    private Integer pageSize = 10;
    /**
     * 起始时间
     */
    @ApiModelProperty(value = "起始时间")
    private String startTime;
    /**
     * 截止时间
     */
    @ApiModelProperty(value = "截止时间")
    private String endTime;
    /**
     * 状态集
     */
    @ApiModelProperty(value = "状态集")
    private String[] states;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Integer id;
    /**
     * 审批记录id
     */
    @ApiModelProperty(value = "审批记录id")
    private Integer auditId;
    /**
     * 配置id
     */
    @ApiModelProperty(value = "配置id")
    private Integer configuresId;
    /**
     * zip包下载位置
     */
    @ApiModelProperty(value = "zip包下载位置")
    private String zipDownUrl;
    /**
     * 应用编码
     */
    @ApiModelProperty(value = "应用编码")
    private String applicationCode;
    /**
     * 目录所在位置
     */
    @ApiModelProperty(value = "目录所在位置")
    private String dirLocation;
    /**
     * 基础包
     */
    @ApiModelProperty(value = "基础包")
    private String basePackage;
    /**
     * 数据源名称
     */
    @ApiModelProperty(value = "数据源名称")
    private String name;
    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String url;
    /**
     * 表名(,分割)
     */
    @ApiModelProperty(value = "表名(,分割)")
    private String tables;
    /**
     * 连接信息
     */
    @ApiModelProperty(value = "连接信息")
    private String databasesInfo;
    /**
     * 模板信息
     */
    @ApiModelProperty(value = "模板信息")
    private String templatesInfo;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 审核状态  -1:待提交 0:提交审核  1:审核中 2: 审核不通过 3:审核通过
     */
    @ApiModelProperty(value = "审核状态  -1:待提交 0:提交审核  1:审核中 2: 审核不通过 3:审核通过")
    private Integer auditStatus;
    /**
     *  状态 -1:已删除 0:创建完成 1:审核中 2: 停用 3: 正常
     */
    @ApiModelProperty(value = "状态 -1:已删除 0:创建完成 1:审核中 2: 停用 3: 正常")
    private Integer status;


}