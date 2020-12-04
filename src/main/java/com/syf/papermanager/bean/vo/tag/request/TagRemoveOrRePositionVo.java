package com.syf.papermanager.bean.vo.tag.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/28 16:40
 */
@Data
public class TagRemoveOrRePositionVo {
//    @NotNull(message = "脑图id不能为空")
//    private Integer themeId;
    @NotNull(message = "节点id不能为空")
    private Integer tagId;
}
