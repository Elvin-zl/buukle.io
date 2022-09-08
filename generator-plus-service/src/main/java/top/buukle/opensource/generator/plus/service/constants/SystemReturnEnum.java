package top.buukle.opensource.generator.plus.service.constants;


import top.buukle.opensource.generator.plus.commons.call.code.BaseReturnEnum;

/**
 * @description
 * @Author jingkang
 * @Date 2020/12/1
 */
public enum SystemReturnEnum {

    SUCCESS(BaseReturnEnum.SUCCESS),
    FAILED(BaseReturnEnum.FAILED),
    RUD_ID_NULL("F","000000","ID为空"),
    FAILED_401_HEAD_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,通用请求头为空!"),
    FAILED_401_APP_ID_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,APP_ID 为空!"),
    FAILED_401_OPERATOR_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,OPERATOR 为空!"),
    FAILED_401_OPERATOR_ID_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,OPERATOR_ID 为空!"),
    FAILED_401_OPERATOR_SUB_TENANT_IDS_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,OPERATOR_SUB_TENANT_IDS 为空!"),
    FAILED_401_OPERATOR_TENANT_ID_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,OPERATOR_TENANT_ID 为空!"),
    FAILED_401_SIGNATURE_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,SIGNATURE 为空!"),
    FAILED_401_ID_NULL("F",SystemReturnEnum.class.getName() + "0000","请求参数错误,ID 为空!"),


    /** 模板 执行*/
    GEN_CONFIGURES_ID_NULL("F",SystemReturnEnum.class.getName() + "0001","失败,配置id为空"),
    GEN_CONFIGURES_FAILED("F",SystemReturnEnum.class.getName() + "0002","执行失败!"),
    GEN_CONFIGURES_GEN_BAT_SCRIPT_FAILED("F",SystemReturnEnum.class.getName() + "003","生成脚本文件失败!"),
    GEN_CONFIGURES_UPLOAD_FAILED("F",SystemReturnEnum.class.getName() + "004","生成失败,上传oss失败!"),

    /** archetype 执行*/
    GEN_ARCHETYPES_GEN_PARAM_NULL("F",SystemReturnEnum.class.getName() + "0100","生成失败,参数为空!"),
    GEN_ARCHETYPES_GEN_ARCHETYPES_NULL("F",SystemReturnEnum.class.getName() + "0101","生成失败,原型文件不存在!"),
    GEN_ARCHETYPES_UPLOAD_ARCHETYPES_NULL("F",SystemReturnEnum.class.getName() + "0102","生成失败,上传oss失败!"),

    /** templates 执行*/
    TEMPLATES_INFO_GROUP_ID_NULL("F",SystemReturnEnum.class.getName() + "0200","查询失败,模板分组id为空!"),
    TEMPLATES_INFO_GROUP_STATUS_WRONG("F",SystemReturnEnum.class.getName() + "0201","查询失败,模板分组状态不对!"),

    /** configures 编辑*/
    CONFIGURES_INFO_TEMPLATES_GROUP_NULL("F",SystemReturnEnum.class.getName() + "0200","查询失败,模板分组信息为空!"),

    /** datasources */
    DATASOURCES_TEST_LINK_ID_NOT_EXIST("F",SystemReturnEnum.class.getName() + "0300","测试失败,该条记录状态不正确!"),
    ;
    private String status;
    private String code;
    private String msg;

    SystemReturnEnum(BaseReturnEnum baseReturnEnum) {
        this.status = baseReturnEnum.getStatus();
        this.code = baseReturnEnum.getCode();
        this.msg = baseReturnEnum.getMsg();
    }
    SystemReturnEnum(String status, String code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
