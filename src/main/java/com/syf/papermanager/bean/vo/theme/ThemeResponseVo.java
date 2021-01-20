package com.syf.papermanager.bean.vo.theme;

import com.syf.papermanager.bean.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/16 19:24
 */
@Data
@NoArgsConstructor
public class ThemeResponseVo {
    private Integer id;
    private String creator; // 创建者用户名
    private String name;
    private String Description;
    private Date createTime;
    private Date updateTime;

    // 前端选择列表只需返回极简信息
    public ThemeResponseVo(Theme theme) {
        this.id = theme.getId();
        this.name = "No" + theme.getId() + ". " + theme.getName();
    }
}
