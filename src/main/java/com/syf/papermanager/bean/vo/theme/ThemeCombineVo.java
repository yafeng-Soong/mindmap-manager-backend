package com.syf.papermanager.bean.vo.theme;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/11 15:11
 */
@Data
public class ThemeCombineVo {
    @NotNull(message = "目标节点ID不能为空")
    private Integer toTagId;
    @NotNull(message = "被合并脑图不能为空")
    private Integer fromThemeId;
}
