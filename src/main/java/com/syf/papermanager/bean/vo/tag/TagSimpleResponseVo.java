package com.syf.papermanager.bean.vo.tag;

import com.syf.papermanager.bean.entity.Tag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/22 20:03
 */
@Data
public class TagSimpleResponseVo {
    private Integer tagId;
    private String name;
    public TagSimpleResponseVo(Tag source) {
        BeanUtils.copyProperties(source, this);
        this.tagId = source.getId();
    }
}
