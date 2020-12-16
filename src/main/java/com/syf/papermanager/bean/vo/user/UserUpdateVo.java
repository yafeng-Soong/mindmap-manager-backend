package com.syf.papermanager.bean.vo.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.user
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/16 15:53
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateVo {
    @ApiModelProperty(value = "修改后的用户名")
    private String username;
    @ApiModelProperty(value = "修改后的简介")
    private String signature;
}
