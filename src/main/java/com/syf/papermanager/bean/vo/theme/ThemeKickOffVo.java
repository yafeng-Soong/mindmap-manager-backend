package com.syf.papermanager.bean.vo.theme;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/31 20:55
 */
@Data
public class ThemeKickOffVo {
    @NotNull(message = "userId不能为空")
    private Integer userId;
    @NotNull(message = "themeId不能为空")
    private Integer themeId;
}
