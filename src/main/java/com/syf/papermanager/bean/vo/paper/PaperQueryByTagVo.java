package com.syf.papermanager.bean.vo.paper;

import com.syf.papermanager.bean.vo.page.PageQueryVo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.paper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/25 16:50
 */
@ApiModel
@Data
public class PaperQueryByTagVo extends PageQueryVo {
    @NotNull(message = "tagId不能为空")
    private Integer tagId;
}
