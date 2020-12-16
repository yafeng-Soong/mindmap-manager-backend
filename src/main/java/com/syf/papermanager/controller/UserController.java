package com.syf.papermanager.controller;

import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.RegisterAndLoginVo;
import com.syf.papermanager.bean.vo.user.UserResetVo;
import com.syf.papermanager.bean.vo.user.UserResponseVo;
import com.syf.papermanager.bean.vo.user.UserUpdateVo;
import com.syf.papermanager.exception.UserException;
import com.syf.papermanager.service.RedisService;
import com.syf.papermanager.service.UserService;
import com.syf.papermanager.utils.redis.UserKeyPrefix;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.SendFailedException;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.controller
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/15 20:25
 */
@Api(tags = "用户管理相关接口")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Resource
    UserService userService;
    @Resource
    RedisService redisService;
    @ApiOperation("用户注册接口")
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody @Validated RegisterAndLoginVo registerVo) throws Exception {
        ResponseEntity response = new ResponseEntity();
        userService.createUser(registerVo);
        response.setData("注册成功，快去邮箱激活吧！");
        return response;
    }
    @ApiOperation("激活账号接口，从邮箱转进来的")
    @GetMapping("/activate")
    public String activate(@RequestParam("token") String token) {
        String res;
        String storedEmail = redisService.get(UserKeyPrefix.TOKEN, token, String.class);
        if (storedEmail == null){
            res = "激活链接已过期";
        }else if (userService.activateUser(storedEmail) == 0){
            res = "激活失败";
        }else{
            //激活后删掉redis中的链接
            redisService.delete(UserKeyPrefix.TOKEN, token);
            res = "激活成功";
        }
        return res;
    }
    @ApiOperation("发送重置密码邮件")
    @GetMapping("/resetPassword")
    @ApiImplicitParam(name = "email", value = "要重置密码的邮箱号", required = true)
    public ResponseEntity sendResetEmail(@RequestParam("email") String email) throws Exception{
        ResponseEntity response = new ResponseEntity();
        userService.sendRestEmail(email);
        response.setData("请及时前往邮箱重置密码");
        return response;
    }

    @ApiOperation("重置密码")
    @PostMapping("/resetPassword")
    public ResponseEntity resetPassword(@RequestBody @Validated UserResetVo resetVo) {
        ResponseEntity response = new ResponseEntity();
        String storedEmail = redisService.get(UserKeyPrefix.TOKEN, resetVo.getToken(), String.class);
        if (storedEmail == null)
            throw new UserException("重置密码链接已过期");
        int flag = userService.resetPassword(storedEmail, resetVo.getPassword());
        if (flag == -1){
            response.setErrorResponse();
            response.setData("新密码格式错误");
        }else if (flag == 0){
            response.setErrorResponse();
            response.setData("重置密码失败");
        }else {
            redisService.delete(UserKeyPrefix.TOKEN, resetVo.getToken());
            response.setData("重置密码成功");
        }
        return response;
    }

    @ApiOperation("更新用户信息")
    @PostMapping("/updateUser")
    public  ResponseEntity updateUser(@RequestBody UserUpdateVo updateVo){
        ResponseEntity response = new ResponseEntity();
        User user = new User();
        BeanUtils.copyProperties(updateVo, user);
        user.setEmail(getCurrentUser().getEmail());
        log.info("更新参数：" + user.toString());
        if (userService.updateUser(user) == 0){
            response.setErrorResponse();
            response.setData("更新失败");
        }else{
            // 用户信息更新成功后要修改内存中的currentUser
            UserResponseVo responseVo = new UserResponseVo();
            User currentUser = userService.selectByEmail(user.getEmail());
            setCurrentUser(currentUser);
            BeanUtils.copyProperties(currentUser, responseVo);
            response.setData(responseVo);
        }
        return response;
    }
}
