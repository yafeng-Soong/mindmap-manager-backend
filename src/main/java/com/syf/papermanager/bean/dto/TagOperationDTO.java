package com.syf.papermanager.bean.dto;

import lombok.Data;

import java.util.Date;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.dto
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/3 15:43
 */
@Data
public class TagOperationDTO {
    private String username;
    private String avatar;
    private String name;
    private Integer tagId;
    private Integer type;
    private Date createTime;
}
