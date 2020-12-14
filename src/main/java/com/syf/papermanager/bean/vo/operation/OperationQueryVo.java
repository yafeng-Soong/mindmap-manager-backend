package com.syf.papermanager.bean.vo.operation;

import com.syf.papermanager.bean.vo.page.PageQueryVo;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.operation
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/14 20:14
 */
@Data
public class OperationQueryVo extends PageQueryVo {
    @NotNull(message = "脑图id不能为空")
    private Integer themeId;
}
