package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.Theme;

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
}
