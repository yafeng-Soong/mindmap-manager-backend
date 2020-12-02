package com.syf.papermanager.bean.vo.tag;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/27 10:36
 */
@Data
public class TagAddVo {
//    @NotNull(message = "所属脑图不能为空")
//    private Integer themeId;
    @NotNull(message = "父节点不能为空")
    private Integer fatherId;
    private String name;
    private boolean left;
}
