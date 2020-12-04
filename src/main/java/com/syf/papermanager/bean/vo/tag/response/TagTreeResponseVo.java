package com.syf.papermanager.bean.vo.tag.response;

import com.syf.papermanager.bean.entity.Tag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:42
 */
@Data
public class TagTreeResponseVo {
    private Integer tagId;
    private Integer creatorId;
    private String name;
    private Integer fatherId;
    private Integer themeId;
    private boolean left;
    private List<TagTreeResponseVo> children;
    private Date createTime;
    private Date updateTime;
    public TagTreeResponseVo(Tag source) {
        BeanUtils.copyProperties(source, this);
        this.tagId = source.getId();
        this.left = source.isPosition();
    }
}
