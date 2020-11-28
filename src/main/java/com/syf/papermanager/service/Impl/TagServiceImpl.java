package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.vo.tag.TagAddVo;
import com.syf.papermanager.bean.vo.tag.TagRenameVo;
import com.syf.papermanager.bean.vo.tag.TagResponseVo;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.bean.vo.tag.TagSimpleResponseVo;
import com.syf.papermanager.mapper.TagMapper;
import com.syf.papermanager.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service.Impl
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:11
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Resource
    TagMapper tagMapper;
    /***
     * 数据库中每个节点只有自己父节点信息，使用深度优先遍历构建树形结构
     * @param themeId
     * @return 树形结构的taglist
     */
    @Override
    public List<TagResponseVo> selectTreeByThemeId(Integer themeId) {
        Set<Integer> tags = new HashSet<>(); // 记录已经访问过的节点id，防止出现环路
        Queue<TagResponseVo> Q = new LinkedList<>();
        List<TagResponseVo> res = new ArrayList<>();
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("theme_id", themeId);
        queryWrapper.eq("father_id", 0);
        Tag tmp = tagMapper.selectOne(queryWrapper);
        if (tmp == null) return res;
        TagResponseVo root = new TagResponseVo(tmp);
        Q.add(root);
        while (Q.size() > 0) {
            TagResponseVo top = Q.poll();
            if (tags.contains(top.getId()))
                break;
            tags.add(top.getId());
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("father_id", top.getId());
            List<Tag> children = tagMapper.selectList(queryWrapper);
            List<TagResponseVo> childrenList = children.stream()
                    .map(source -> new TagResponseVo(source))
                    .collect(Collectors.toList());
            top.setChildren(childrenList);
            childrenList.forEach(i -> Q.add(i));
        }
        tags.clear();
        res.add(root);
        return res;
    }

    @Override
    public List<TagSimpleResponseVo> selectSimpleList(Integer themeId) {
        Set<Integer> tags = new HashSet<>(); // 记录已经访问过的节点id，防止出现环路
        Queue<Integer> Q = new LinkedList<>();
        List<TagSimpleResponseVo> res = new ArrayList<>();
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("theme_id", themeId);
        queryWrapper.eq("father_id", 0);
        Tag tmp = tagMapper.selectOne(queryWrapper);
        if (tmp == null) return res;
        Q.add(tmp.getId());
        while (Q.size() > 0) {
            int top = Q.poll();
            if (tags.contains(top))
                break;
            tags.add(top);
            Tag tag = tagMapper.selectById(top);
            res.add(new TagSimpleResponseVo(tag));
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("father_id", top);
            List<Tag> tagList = tagMapper.selectList(queryWrapper);
            tagList.forEach(i -> Q.add(i.getId()));
        }
        return res;
    }

    @Override
    public int addTag(TagAddVo addVo, Integer userId) {
        Tag tag = new Tag(addVo);
        tag.setCreatorId(userId);
        tagMapper.insert(tag);
        return tag.getId();
    }

    @Override
    public int renameTag(TagRenameVo renameVo) {
        Tag tag = new Tag();
        tag.setId(renameVo.getTagId());
        tag.setName(renameVo.getName());
        tagMapper.updateById(tag);
        return 0;
    }
}
