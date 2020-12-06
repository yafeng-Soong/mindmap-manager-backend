package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.vo.theme.ThemeAddVo;
import com.syf.papermanager.bean.vo.theme.ThemeQueryVo;
import com.syf.papermanager.bean.vo.theme.ThemeUpdateVo;
import com.syf.papermanager.exception.ThemeException;
import com.syf.papermanager.mapper.TagMapper;
import com.syf.papermanager.mapper.ThemeMapper;
import com.syf.papermanager.service.ThemeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    TagMapper tagMapper;
    @Override
    public List<Theme> selectList(Integer creatorId) {
        QueryWrapper<Theme> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", creatorId);
        return themeMapper.selectList(queryWrapper);
    }

    @Override
    public Page<Theme> selectPageList(ThemeQueryVo queryVo, Integer userId) {
        Theme theme = new Theme();
        theme.setCreatorId(userId);
        QueryWrapper<Theme> queryWrapper = new QueryWrapper<>(theme);
        queryWrapper.like(StringUtils.isNotBlank(queryVo.getName()), "name", queryVo.getName());
        queryWrapper.orderByDesc("update_time");
        Page<Theme> page = new Page<>(queryVo.getCurrentPage(), queryVo.getPageSize());
        return themeMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public int addTheme(ThemeAddVo addVo, Integer userId) {
        Theme theme = new Theme(addVo);
        theme.setCreatorId(userId);
        themeMapper.insert(theme);
        Tag tag = new Tag();
        tag.setCreatorId(userId);
        tag.setThemeId(theme.getId());
        tag.setInnerOrder(0);
        tag.setName(theme.getName());
        return tagMapper.insert(tag);
    }

    @Override
    public int updateTheme(ThemeUpdateVo updateVo, Integer userId) {
        String name = updateVo.getName();
        String description = updateVo.getDescription();
        Theme theme = new Theme();
        theme.setId(updateVo.getId());
        boolean allBlank = true;
        if (StringUtils.isNotBlank(name)) {
            theme.setName(name);
            allBlank = false;
        }
        if (StringUtils.isNotBlank(description)) {
            theme.setDescription(description);
            allBlank = false;
        }
        if (allBlank) throw new ThemeException("至少一个字段不为空");
        return themeMapper.updateById(theme);
    }
}
