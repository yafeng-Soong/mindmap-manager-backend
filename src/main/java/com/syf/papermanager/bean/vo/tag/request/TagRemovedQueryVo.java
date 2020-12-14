package com.syf.papermanager.bean.vo.tag.request;

import com.syf.papermanager.bean.vo.page.PageQueryVo;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.tag.request
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/14 21:00
 */
@Data
public class TagRemovedQueryVo extends PageQueryVo {
    @NotNull(message = "脑图id不能为空")
    private Integer themeId;
}
