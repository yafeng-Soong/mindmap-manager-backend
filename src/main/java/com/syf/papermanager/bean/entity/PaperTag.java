package com.syf.papermanager.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.entity
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/18 11:43
 */
@Data
public class PaperTag {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer paperId;
    private Integer tagId;
    private Integer creatorId;
}
