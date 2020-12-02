package com.syf.papermanager.bean.vo.theme;

import com.syf.papermanager.bean.vo.page.PageQueryVo;
import lombok.Data;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.theme
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/1 15:42
 */
@Data
public class ThemeQueryVo extends PageQueryVo {
    private String name;
}
