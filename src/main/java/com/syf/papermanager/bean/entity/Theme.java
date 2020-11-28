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
 * @create_time: 2020/11/15 23:10
 */
@Data
public class Theme {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer creatorId;
    private String name;
    private String Description;
    private Date createTime;
    private Date updateTime;
}
