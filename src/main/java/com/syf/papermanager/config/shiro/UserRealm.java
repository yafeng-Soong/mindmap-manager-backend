package com.syf.papermanager.config.shiro;

import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.config
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 21:42
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 授予权限，暂时没有什么权限好授予
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String email = (String) principalCollection.getPrimaryPrincipal();
        User user = userService.selectByEmail(email);
        info.addRole(user.getRole());
        return info;
    }

    /**
     * 角色认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //Http请求时会进入这里
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.selectByEmail(token.getUsername());
        if (user == null)
            throw new UnknownAccountException("不存在该用户：" + token.getUsername());
        if (user.getState() == 0)
            throw new DisabledAccountException(token.getUsername() + "暂未激活");
        return new SimpleAuthenticationInfo(
                user.getEmail(),
                user.getPassword(),
                ByteSource.Util.bytes(user.getSalt()),
                getName());
    }
}