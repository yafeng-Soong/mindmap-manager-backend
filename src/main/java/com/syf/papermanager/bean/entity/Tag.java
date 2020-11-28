package com.syf.papermanager.bean.entity;



import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.syf.papermanager.bean.vo.tag.TagAddVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.entity
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 23:12
 */
@Data
public class Tag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer creatorId;
    private String name;
    private Integer fatherId;
    private Integer themeId;
    private Integer state;
    private boolean position;
    private Date createTime;
    private Date updateTime;
    public Tag() {
        super();
    }
    public Tag(TagAddVo addVo) {
        BeanUtils.copyProperties(addVo, this);
        this.position = addVo.isLeft();
    }
}
