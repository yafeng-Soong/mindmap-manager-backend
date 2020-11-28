package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.mapper.UserMapper;
import com.syf.papermanager.service.UserService;
import org.springframework.stereotype.Service;

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
}
