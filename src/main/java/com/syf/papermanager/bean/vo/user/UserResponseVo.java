package com.syf.papermanager.bean.vo.user;

import com.syf.papermanager.bean.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.user
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 12:40
 */
@Data
@NoArgsConstructor
public class UserResponseVo {
    private Integer id;
    private String username;
    private String email;
    private String avatar;
    private String signature;
    private String role;

    public UserResponseVo(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
