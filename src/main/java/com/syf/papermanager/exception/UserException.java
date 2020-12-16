package com.syf.papermanager.exception;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.exception
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/15 22:05
 */
public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}
