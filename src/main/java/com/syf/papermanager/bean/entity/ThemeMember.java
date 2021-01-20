package com.syf.papermanager.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.entity
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/29 19:09
 */
@Data
public class ThemeMember {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer themeId;
    private Date createTime;
}
