package com.syf.papermanager.bean.vo.theme;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/1 14:24
 */
@Data
public class ThemeAddVo {
    @NotNull(message = "脑图名不能为空")
    private String name;
    private String description;
}
