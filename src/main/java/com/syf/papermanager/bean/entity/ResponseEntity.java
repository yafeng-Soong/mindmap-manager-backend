package com.syf.papermanager.bean.entity;

import com.syf.papermanager.bean.enums.ResponseEnums;
import lombok.Data;

import java.io.Serializable;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.entity
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 21:23
 */
@Data
public class ResponseEntity<T> implements Serializable {
    private Integer code = ResponseEnums.SUCCESS.getCode();
    private String msg = ResponseEnums.SUCCESS.getMsg();
    private T data;

    public ResponseEntity(){
        super();
    }

    public ResponseEntity(T data){
        super();
        this.data = data;
    }

    public  ResponseEntity(Integer code, String msg){
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResponseEntity(Exception e){
        super();
        this.code = ResponseEnums.ERROR.getCode();
        this.msg = e.getMessage();
    }

    public void setErrorResponse(){
        code = ResponseEnums.ERROR.getCode();
        msg = ResponseEnums.ERROR.getMsg();
    }
}
