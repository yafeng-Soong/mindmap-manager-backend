package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.mapper.ThemeMapper;
import com.syf.papermanager.service.ThemeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service.Impl
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 23:16
 */
@Service
public class ThemeServiceImpl extends ServiceImpl<ThemeMapper, Theme> implements ThemeService {
    @Resource
    ThemeMapper themeMapper;
    @Override
    public List<Theme> selectList(Integer creatorId) {
        QueryWrapper<Theme> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", creatorId);
        return themeMapper.selectList(queryWrapper);
    }
}
