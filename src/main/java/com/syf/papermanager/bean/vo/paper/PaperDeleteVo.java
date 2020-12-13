package com.syf.papermanager.bean.vo.paper;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.paper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/13 20:32
 */
@Data
public class PaperDeleteVo {
    @NotNull(message = "论文id不能为空")
    private Integer paperId;
    @NotNull(message = "关联的节点id不能为空")
    private Integer tagId;
}
