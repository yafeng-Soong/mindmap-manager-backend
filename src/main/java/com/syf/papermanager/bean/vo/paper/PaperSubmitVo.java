package com.syf.papermanager.bean.vo.paper;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.paper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/21 20:14
 */
@ApiModel
@Data
public class PaperSubmitVo {
    @NotNull(message = "论文名称不能为空")
    private String name;
    @NotNull(message = "作者不能为空")
    private String author;
    @NotNull(message = "关键字不能为空")
    private String keyword;
    @NotNull(message = "论文摘要不能为空")
    private String abstracts;
    private String summary;
    @NotNull(message = "发表年份不能为空")
    private Integer publishYear;
    @Size(min = 1, message = "论文必须要有所属分类")
    private List<Integer> tags;
}
