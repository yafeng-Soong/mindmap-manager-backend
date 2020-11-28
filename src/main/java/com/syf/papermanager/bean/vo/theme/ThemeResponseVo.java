package com.syf.papermanager.bean.vo.theme;

import lombok.Data;

import java.util.Date;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/16 19:24
 */
@Data
public class ThemeResponseVo {
    private Integer id;
    private String creator; // 创建者用户名
    private String name;
    private String Description;
    private Date createTime;
}
