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

    /** 模板 执行*/
    GEN_CONFIGURES_ID_NULL("F",SystemReturnEnum.class.getName() + "0001","失败,配置id为空"),
    GEN_CONFIGURES_FAILED("F",SystemReturnEnum.class.getName() + "0002","执行失败!"),
    GEN_CONFIGURES_GEN_BAT_SCRIPT_FAILED("F",SystemReturnEnum.class.getName() + "003","生成脚本文件失败!"),

    /** archetype 执行*/
    GEN_ARCHETYPES_GEN_PARAM_NULL("F",SystemReturnEnum.class.getName() + "0100","生成失败,参数为空!"),
    GEN_ARCHETYPES_GEN_ARCHETYPES_NULL("F",SystemReturnEnum.class.getName() + "0101","生成失败,原型文件不存在!"),

    /** templates 执行*/
    TEMPLATES_INFO_GROUP_ID_NULL("F",SystemReturnEnum.class.getName() + "0200","查询失败,模板分组id为空!"),
    TEMPLATES_INFO_GROUP_STATUS_WRONG("F",SystemReturnEnum.class.getName() + "0201","查询失败,模板分组状态不对!"),

    /** configures 编辑*/
    CONFIGURES_INFO_TEMPLATES_GROUP_NULL("F",SystemReturnEnum.class.getName() + "0200","查询失败,模板分组信息为空!"),
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
