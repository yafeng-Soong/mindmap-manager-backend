package com.syf.papermanager.controller;

import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.enums.ResponseEnums;
import com.syf.papermanager.exception.PaperException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.exception
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/28 16:58
 */
@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = PaperException.class)
    public ResponseEntity paperExceptionHandler(PaperException e) {
        e.printStackTrace();
        ResponseEntity response = new ResponseEntity();
        response.setCode(ResponseEnums.FILE_UPLOAD_ERROR.getCode());
        response.setMsg(ResponseEnums.FILE_UPLOAD_ERROR.getMsg());
        response.setData(e.getMessage());
        return response;
    }
    @ExceptionHandler(value = UnknownAccountException.class)
    public ResponseEntity unknownAccountException(UnknownAccountException e) {
        log.warn(e.getMessage());
        ResponseEntity response = new ResponseEntity();
        response.setCode(ResponseEnums.LOGIN_UNKNOWN.getCode());
        response.setMsg(ResponseEnums.LOGIN_UNKNOWN.getMsg());
        response.setData(e.getMessage());
        return response;
    }

    @ExceptionHandler(value = DisabledAccountException.class)
    public ResponseEntity disabledAccountExceptionHandler(DisabledAccountException e) {
        log.warn(e.getMessage());
        ResponseEntity response = new ResponseEntity();
        response.setCode(ResponseEnums.LOGIN_DISABLE.getCode());
        response.setMsg(ResponseEnums.LOGIN_DISABLE.getMsg());
        return response;
    }

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public ResponseEntity incorrectCredentialsExceptionHandler(IncorrectCredentialsException e) {
        log.warn(e.getMessage());
        ResponseEntity response = new ResponseEntity();
        response.setCode(ResponseEnums.LOGIN_ERROR.getCode());
        response.setMsg(ResponseEnums.LOGIN_ERROR.getMsg());
        return response;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity javaExceptionHandler(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ResponseEntity response = new ResponseEntity();
        response.setErrorResponse();
        response.setData("服务器异常");
        return  response;
    }
}
