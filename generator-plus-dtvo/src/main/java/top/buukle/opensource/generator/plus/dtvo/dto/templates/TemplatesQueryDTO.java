package top.buukle.opensource.generator.plus.dtvo.dto.templates;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @author elvin
* @description  Datasources 查询包装类
*/
@Data
@ApiModel(value = "模板 - 查询请求")
public class TemplatesQueryDTO {

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
     * 模板分组id
     */
    @ApiModelProperty(value = "模板分组id")
    private Integer templatesGroupId;
    /**
     *
     */
    @ApiModelProperty(value = "应用编码")
    private String applicationCode;
    /**
     * 数据源名称
     */
    @ApiModelProperty(value = "数据源名称")
    private String name;
    /**
     * 生成路径
     */
    @ApiModelProperty(value = "生成路径")
    private String path;
    /**
     * 开启表名目录
     */
    @ApiModelProperty(value = "开启表名目录 1: 开启,0:不开启")
    private String openTablePath;
    /**
     * 文件名是否追加模板名前段 1: 追加,0:不追加
     */
    @ApiModelProperty(value = "文件名是否追加模板名前段 1: 追加,0:不追加")
    private String addNameSuffix;
    /**
     * 文件地址
     */
    @ApiModelProperty(value = "文件地址")
    private String url;
    /**
     * 模板类型(entity,dao,xml,service,serviceImpl,controller)
     */
    @ApiModelProperty(value = "模板类型(entity,dao,xml,service,serviceImpl,controller)")
    private String type;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
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