package com.syf.papermanager.exception;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.exception
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/13 13:32
 */
public class PaperException extends RuntimeException {
    public PaperException(String message) {
        super(message);
    }
}
