package com.syf.papermanager.bean.vo.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/24 21:03
 */
@Data
@ApiModel("基础分页对象")
public class PageQueryVo {
    @ApiModelProperty(name = "currentPage", value = "当前页数，从1开始")
    @Min(value = 1)
    @NotNull(message = "当前页数不能为空")
    private Integer currentPage;
    @ApiModelProperty(name = "pageSize", value = "分页大小")
    @Min(value = 1)
    @NotNull(message = "分页大小不能为空")
    private Integer pageSize;
}
