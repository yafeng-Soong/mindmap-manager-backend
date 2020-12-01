package com.syf.papermanager.bean.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.enums
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/28 20:22
 */
@Getter
@AllArgsConstructor
public enum OperationType {
    ADD("增加节点", 1),
    REMOVE("删除节点",2),
    RENAME("重命名节点",3),
    MOVE("移动节点（同层）",4),
    REPARENT("移动节点（改变父节点）", 5);

    private String description;
    private Integer code;

    public static OperationType of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(e->e.getCode().equals(code))
                .findAny()
                .orElseThrow(()-> new IllegalArgumentException(code + "not exists in OperateType"));
    }

}
