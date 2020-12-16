package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.RegisterAndLoginVo;

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
    int createUser(RegisterAndLoginVo registerVo) throws Exception;
    void sendRestEmail(String email) throws Exception;
    int updateUser(User user);
    /**
     * 激活账户
     * @param email
     * @return
     */
    int activateUser(String email);

    /**
     * 修改密码
     * @param email
     * @param password
     * @return
     */
    int resetPassword(String email, String password);
}
