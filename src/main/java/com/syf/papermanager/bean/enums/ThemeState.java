package com.syf.papermanager.bean.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.enums
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/10 21:23
 */
@Getter
@AllArgsConstructor
public enum ThemeState {
    NORMAL("正常", 0),
    REMOVED("被删除", 1);
    private String state;
    private Integer code;
}
