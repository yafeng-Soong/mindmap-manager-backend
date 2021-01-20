package com.syf.papermanager.bean.vo.theme;

import com.syf.papermanager.utils.RegexpUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/29 19:14
 */
@Data
public class ThemeInviteVo {
    @NotNull(message = "邮箱字段不能为空")
    @Pattern(regexp = RegexpUtil.REG_EMAIL, message = "请输入正确格式的邮箱")
    private String email;
    @NotNull(message = "脑图Id不能为空")
    private Integer themeId;
}
