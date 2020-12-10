package com.syf.papermanager.controller;

import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.enums.ResponseEnums;
import com.syf.papermanager.exception.MyAuthenticationException;
import com.syf.papermanager.exception.FileUploadException;
import com.syf.papermanager.exception.TagException;
import com.syf.papermanager.exception.ThemeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    @ExceptionHandler(value = FileUploadException.class)
    public ResponseEntity fileUploadExceptionHandler(FileUploadException e) {
        e.printStackTrace();
        ResponseEntity response = new ResponseEntity();
        response.setCode(ResponseEnums.FILE_UPLOAD_ERROR.getCode());
        response.setMsg(ResponseEnums.FILE_UPLOAD_ERROR.getMsg());
        response.setData(e.getMessage());
        return response;
    }
    @ExceptionHandler(value = TagException.class)
    public ResponseEntity tagException(TagException e) {
        ResponseEntity response = new ResponseEntity();
        response.setErrorResponse();
        response.setMsg(e.getMessage());
        return response;
    }
    @ExceptionHandler(value = ThemeException.class)
    public ResponseEntity themeException(ThemeException e) {
        ResponseEntity response = new ResponseEntity();
        response.setErrorResponse();
        response.setMsg(e.getMessage());
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

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity argumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ResponseEntity response = new ResponseEntity();
        response.setCode(ResponseEnums.VALID_ERROR.getCode());
        response.setMsg(ResponseEnums.VALID_ERROR.getMsg());
        List<String> data = new ArrayList<>();
        BindingResult bindingResult = e.getBindingResult();
        for (ObjectError error : bindingResult.getAllErrors()){
            data.add(((FieldError)error).getField() + "字段，" + error.getDefaultMessage());
        }
        response.setData(data);
        return response;
    }

    @ExceptionHandler(value = MyAuthenticationException.class)
    public ResponseEntity myAuthenticationException(MyAuthenticationException e) {
        ResponseEntity response = new ResponseEntity();
        response.setCode(ResponseEnums.UNAUTHENTICATED.getCode());
        response.setMsg(e.getMessage());
        return response;
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity javaExceptionHandler(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        ResponseEntity response = new ResponseEntity();
        response.setErrorResponse();
        response.setMsg("服务器异常");
        return  response;
    }
}
