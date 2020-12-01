package com.syf.papermanager.exception;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.exception
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/28 20:09
 */
public class MyAuthenticationException extends RuntimeException{
    public MyAuthenticationException(String message) {
        super(message);
    }
}
