package com.syf.papermanager.bean.vo.tag;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/1 9:47
 */
@Data
public class TagReparentVo {
//    @NotNull(message = "脑图id不能为空")
//    private Integer themeId;
    @NotNull(message = "父亲节点id不能为空")
    private Integer fatherId;
    @NotNull(message = "节点id不能为空")
    private Integer tagId;
}
