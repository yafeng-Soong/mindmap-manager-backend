package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.entity.ThemeOperation;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.enums.OperationType;
import com.syf.papermanager.bean.vo.tag.*;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.exception.MyAuthenticationException;
import com.syf.papermanager.mapper.TagMapper;
import com.syf.papermanager.mapper.ThemeOperationMapper;
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
    private Tag tempTag;
    @Resource
    TagMapper tagMapper;
    @Resource
    ThemeOperationMapper themeOperationMapper;
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
        queryWrapper.eq("state", 0);
        Tag tmp = tagMapper.selectOne(queryWrapper);
        if (tmp == null) return res;
        TagResponseVo root = new TagResponseVo(tmp);
        Q.add(root);
        while (Q.size() > 0) {
            TagResponseVo top = Q.poll();
            if (tags.contains(top.getTagId()))
                break;
            tags.add(top.getTagId());
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("father_id", top.getTagId());
            queryWrapper.eq("state", 0);
            List<Tag> children = tagMapper.selectList(queryWrapper);
            Collections.sort(children);
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
        queryWrapper.eq("state", 0);
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
            queryWrapper.eq("state", 0);
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
        int tagId = tag.getId();
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(addVo.getThemeId())
                .tagId(tagId)
                .type(OperationType.ADD.getCode())
                .build();
        themeOperationMapper.insert(operation);
        return tagId;
    }

    @Override
    public int renameTag(TagRenameVo renameVo, Integer userId) {
        Tag tag = new Tag();
        tag.setId(renameVo.getTagId());
        tag.setName(renameVo.getName());
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(renameVo.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.RENAME.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    @Override
    public int removeTag(TagRemoveOrRePositionVo removeVo, User user) {
        if (!operable(removeVo.getTagId())) return 0;
        Tag tag = new Tag();
        tag.setId(removeVo.getTagId());
        tag.setState(1);
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(user.getId())
                .themeId(removeVo.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.REMOVE.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    @Override
    public int changePosition(TagRemoveOrRePositionVo rePositionVo, Integer userId) {
        if (!operable(rePositionVo.getTagId())) return 0;
        Tag tag = new Tag();
        tag.setId(rePositionVo.getTagId());
        tag.setPosition(!tempTag.isPosition());
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(rePositionVo.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.MOVE.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    @Override
    public int changeOrder(TagReOrderVo reOrderVo, Integer userId) {
        if (!operable(reOrderVo.getMovedTagId())) return 0;
        Tag insertTag = tagMapper.selectById(reOrderVo.getInsertTagId());
        Tag tag = new Tag();
        // 互换节点内部顺序
        tag.setId(tempTag.getId());
        tag.setInnerOrder(insertTag.getInnerOrder());
        if (reOrderVo.isPosition())
            tag.setPosition(!tempTag.isPosition());
        tagMapper.updateById(tag);
        tag.setId(insertTag.getId());
        tag.setInnerOrder(tempTag.getInnerOrder());
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(reOrderVo.getThemeId())
                .tagId(tempTag.getId())
                .type(OperationType.MOVE.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    private boolean operable(Integer tagId) {
        tempTag = tagMapper.selectById(tagId);
        if (tempTag.getState() == 2)
            throw new MyAuthenticationException("节点已被锁定，无法操作");
        if (tempTag == null || tempTag.getState() != 0)
            return false;
        else return true;
    }
}
