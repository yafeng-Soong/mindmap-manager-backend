package com.syf.papermanager.bean.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.enums
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/15 20:28
 */
@Getter
@AllArgsConstructor
public enum UserState {
    INACTIVE("未激活", 0),
    ACTIVE("已激活", 1);
    private String state;
    private Integer code;
}
