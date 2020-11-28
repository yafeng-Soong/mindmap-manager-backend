package com.syf.papermanager.base;

import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.enums.ResponseEnums;
import org.apache.shiro.SecurityUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.base
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 11:18
 */
public abstract class BaseController {
    /**
     * 校验参数
     * @param response 返回实体类
     * @param bindingResult
     * @return
     */
    public boolean validateParams(ResponseEntity response, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            response.setCode(ResponseEnums.VALID_ERROR.getCode());
            response.setMsg(ResponseEnums.VALID_ERROR.getMsg());
            List<String> data = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()){
                data.add(((FieldError)error).getField() + "字段，" + error.getDefaultMessage());
            }
            response.setData(data);
            return true;
        }
        return false;
    }

    /**
     * 返回当前登录用户信息
     * @return
     */
    protected User getCurrentUser(){
        return (User) SecurityUtils.getSubject().getSession().getAttribute("currentUser");
    }

    /**
     * 设置当前登录用户的信息
     * @param user
     */
    protected void setCurrentUser(User user){
        SecurityUtils.getSubject().getSession().setAttribute("currentUser", user);
    }
}
