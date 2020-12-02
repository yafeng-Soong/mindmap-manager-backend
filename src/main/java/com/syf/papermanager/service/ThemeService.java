package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.vo.theme.ThemeAddVo;
import com.syf.papermanager.bean.vo.theme.ThemeQueryVo;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 23:16
 */
public interface ThemeService extends IService<Theme> {
    List<Theme> selectList(Integer creatorId);
    Page<Theme> selectPageList(ThemeQueryVo queryVo, Integer userId);
    int addTheme(ThemeAddVo addVo, Integer userId);
}
