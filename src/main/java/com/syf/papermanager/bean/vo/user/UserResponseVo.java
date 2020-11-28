package com.syf.papermanager.bean.vo.user;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.user
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 12:40
 */
@Data
public class UserResponseVo {
    private Integer id;
    private String username;
    private String email;
    private String avatar;
    private String signature;
    private String role;
}
