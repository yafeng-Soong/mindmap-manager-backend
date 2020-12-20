package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.enums.UserState;
import com.syf.papermanager.bean.vo.RegisterAndLoginVo;
import com.syf.papermanager.exception.UserException;
import com.syf.papermanager.mapper.UserMapper;
import com.syf.papermanager.service.EmailService;
import com.syf.papermanager.service.UserService;
import com.syf.papermanager.utils.PasswordHelper;
import com.syf.papermanager.utils.RegexpUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service.Impl
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 23:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    EmailService emailService;
    @Override
    public User selectByEmail(String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public int updateAvatar(User user) {
//        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("email",user.getEmail()).set("avatar", user.getAvatar());
//        return userMapper.update(null, updateWrapper);
        User update = new User();
        update.setId(user.getId());
        update.setAvatar(user.getAvatar());
        return userMapper.updateById(user);
    }

    @Override
    @Transactional
    public int createUser(RegisterAndLoginVo registerVo) throws Exception{
        User user = new User();
        user.setEmail(registerVo.getEmail());
        user.setPassword(registerVo.getPassword());
        user.setUsername(registerVo.getEmail());
        PasswordHelper helper = new PasswordHelper();
        helper.encryptPassword(user);
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            throw new UserException("注册失败," + registerVo.getEmail() + "邮箱已被注册");
        }
        String subject = "尊敬的用户，请激活您的账号";
        emailService.sendTemplateMail(user.getEmail(), "activateAccount", subject);
        return user.getId();
    }

    @Override
    public void sendRestEmail(String email) throws Exception {
        User user = selectByEmail(email);
        if (user == null)
            throw new UserException("账号不存在");
        if (user.getState() == UserState.INACTIVE.getCode())
            throw new UserException("账号还未激活");
        emailService.sendTemplateMail(email, "resetEmail", "重置密码");
    }

    @Override
    public int updateUser(User user) {
        boolean hasName, hasSignature;
        hasName = StringUtils.isNotBlank(user.getUsername());
        hasSignature = StringUtils.isNotBlank(user.getSignature());
        if (!hasName && !hasSignature)
            return 0;
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", user.getEmail())
                .set(hasName, "username", user.getUsername())
                .set(hasSignature, "signature", user.getSignature());
        return userMapper.update(null, updateWrapper);
    }

    @Override
    public int activateUser(String email) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("email", email)
                .set("state", UserState.ACTIVE.getCode());
        return userMapper.update(null, updateWrapper);
    }

    @Override
    public int resetPassword(String email, String password) {
        if (!RegexpUtil.passwordMatcher(password))
            return -1;
        User user = new User();
        user.setPassword(password);
        PasswordHelper helper = new PasswordHelper();
        helper.encryptPassword(user);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", email)
                .set("password", user.getPassword())
                .set("salt", user.getSalt());
        return userMapper.update(null, updateWrapper);
    }
}
