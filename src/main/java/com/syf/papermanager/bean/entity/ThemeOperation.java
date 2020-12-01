package com.syf.papermanager.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.entity
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/28 20:19
 */
@Data
@Builder
public class ThemeOperation {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer tagId;
    private Integer themeId;
    private Integer operatorId;
    private Integer type;
    private Date createTime;
}
