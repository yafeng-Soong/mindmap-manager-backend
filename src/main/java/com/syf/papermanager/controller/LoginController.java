package com.syf.papermanager.controller;

import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.RegisterAndLoginVo;
import com.syf.papermanager.bean.vo.user.UserResponseVo;
import com.syf.papermanager.exception.FileUploadException;
import com.syf.papermanager.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.controller
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 23:52
 */
@Api(tags = "登录相关功能的接口")
@Slf4j
@RestController
public class LoginController extends BaseController {
    @Autowired
    UserService userService;

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public ResponseEntity login(@RequestBody @Validated RegisterAndLoginVo user){
        ResponseEntity response = new ResponseEntity();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getEmail(), user.getPassword());

        subject.login(token);
        User currentUser = userService.selectByEmail(user.getEmail());
        subject.getSession().setAttribute("currentUser", currentUser);
        //设置session过期时间为3天
        subject.getSession().setTimeout(259200000);
        UserResponseVo responseVo = new UserResponseVo();
        BeanUtils.copyProperties(currentUser, responseVo);
        response.setData(responseVo);
        return response;
    }
    @GetMapping("/test")
    @ApiOperation("test")
    public void test() {
        throw new FileUploadException("test");
    }
    @GetMapping("/sayHello")
    public String sayHello(){
        //只是一个测试接口
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        return "(✿✪‿✪｡)ﾉｺﾝﾁｬ♡，" + username + "当你看到这条消息表示你已经登陆了哦!";
    }

    /**
     * 退出登录，实质上是去掉session
     * @return
     */
    @GetMapping("/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().removeAttribute("currentUser");
        subject.logout();
        return "您已经退出登录！";
    }
}
