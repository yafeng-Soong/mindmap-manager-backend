package com.syf.papermanager.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.syf.papermanager.bean.vo.theme.ThemeAddVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

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
    private String description;
    private Date createTime;
    private Date updateTime;
    public Theme() {
        super();
    }
    public Theme(ThemeAddVo addVo) {
        BeanUtils.copyProperties(addVo, this);
    }
}
