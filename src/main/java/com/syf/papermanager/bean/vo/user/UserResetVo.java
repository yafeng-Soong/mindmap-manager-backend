package com.syf.papermanager.bean.vo.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.user
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/16 15:28
 */
@Data
public class UserResetVo {
    @NotNull(message = "token不能为空")
    private String token;
    @NotNull(message = "新密码不能为空")
    private String password;
}
