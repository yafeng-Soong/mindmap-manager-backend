package com.syf.papermanager.bean.vo.tag.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/27 11:20
 */
@Data
public class TagRenameVo {
//    @NotNull(message = "脑图id不能为空")
//    private Integer themeId;
    @NotNull(message = "节点Id不能为空")
    private Integer tagId;
    @NotNull(message = "节点名不能为空")
    private String name;
}
