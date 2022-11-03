package top.buukle.opensource.generator.plus.dtvo.dto.archetypesExecute;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * <p>
 * 应用表
 * </p>
 *
 * @author 
 * @since 2021-09-17
 */
@Data
@ApiModel(value = " 应用表 - 查询请求")
public class ArchetypesExecuteUpdateRPCDTO extends ArchetypesExecuteUpdateDTO{

    /**
     * git地址
     */
    @ApiModelProperty(value = "git地址")
    private String gitLocation;

    /**
     * git分支
     */
    @ApiModelProperty(value = "git分支")
    private String gitBranch;

}