package com.syf.papermanager.bean.vo.theme;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/6 12:54
 */
@Data
public class ThemeUpdateVo {
    @NotNull(message = "脑图id不能为空")
    private Integer id;
    private String name;
    private String description;
}
