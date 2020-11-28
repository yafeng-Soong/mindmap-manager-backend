package com.syf.papermanager.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 21:19
 */
@ApiModel
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int state;
    private String avatar;
    private String role;
    private String signature;
}
