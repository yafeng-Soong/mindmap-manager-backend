package com.syf.papermanager.bean.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.enums
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 21:21
 */
@Getter
@AllArgsConstructor
public enum ResponseEnums {
    SUCCESS(200, "操作成功"),
    LOGIN_SUCCESS(201, "登录成功"),
    LOGIN_UNKNOWN(202, "用户不存在"),
    LOGIN_ERROR(203, "账号或密码错误"),
    LOGIN_CHECK_ERROR(204, "输入的旧密码不匹配"),
    LOGIN_DISABLE(205, "账号暂未激活"),
    VALID_ERROR(300, "参数错误"),
    FILE_TYPE_ERROR(301, "文件类型不合法"),
    FILE_UPLOAD_ERROR(302, "文件上传失败"),
    ERROR(400, "操作失败"),
    UNAUTHORIZED(401, "您还未登录"),
    NOT_FOUND(404, "资源不存在"),
    INNER_ERROR(500, "系统发生异常");

    @Getter
    private Integer code;
    @Getter
    private String msg;


    ResponseEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
