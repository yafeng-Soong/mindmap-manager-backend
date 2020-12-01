package com.syf.papermanager.exception;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.exception
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/21 20:44
 */
public class FileUploadException extends RuntimeException{
    public FileUploadException(String message) {
        super(message);
    }
}
