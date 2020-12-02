package com.syf.papermanager.bean.vo.tag;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/30 21:30
 */
@Data
public class TagReOrderVo {
//    @NotNull(message = "脑图id不能为空")
//    private Integer themeId;
    @NotNull(message = "被移动节点id不能为空")
    private Integer movedTagId;
    @NotNull(message = "插入位置id不能为空")
    private Integer insertTagId;
    private boolean position;
}
