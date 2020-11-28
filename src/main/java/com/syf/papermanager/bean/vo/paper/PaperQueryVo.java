package com.syf.papermanager.bean.vo.paper;

import com.syf.papermanager.bean.vo.page.PageQueryVo;
import lombok.Data;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.paper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/24 21:14
 */
@Data
public class PaperQueryVo extends PageQueryVo {
    private String author;
    private Integer publishYear;
    private String keyword;
    private String name;
    private Integer tagId;
}
