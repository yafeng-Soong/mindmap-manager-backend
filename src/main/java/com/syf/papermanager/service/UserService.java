package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.User;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 23:36
 */
public interface UserService extends IService<User> {
    User selectByEmail(String email);
    int updateAvatar(User user);
}
