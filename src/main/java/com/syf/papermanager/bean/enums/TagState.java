package com.syf.papermanager.bean.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.enums
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/2 20:09
 */
@Getter
@AllArgsConstructor
public enum TagState {
    NORMAL("正常", 0),
    REMOVED("被删除（在回收站）", 1),
    LOCKED("被锁定",2),
    DELETED("彻底删除", 3);
    private String state;
    private Integer code;
}
