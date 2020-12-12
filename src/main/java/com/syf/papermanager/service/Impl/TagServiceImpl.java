package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.dto.TagOperationDTO;
import com.syf.papermanager.bean.entity.ThemeOperation;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.enums.OperationType;
import com.syf.papermanager.bean.enums.TagState;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.bean.vo.tag.request.*;
import com.syf.papermanager.bean.vo.tag.response.TagOperationVo;
import com.syf.papermanager.bean.vo.tag.response.TagRemovedVo;
import com.syf.papermanager.bean.vo.tag.response.TagSimpleResponseVo;
import com.syf.papermanager.bean.vo.tag.response.TagTreeResponseVo;
import com.syf.papermanager.exception.MyAuthenticationException;
import com.syf.papermanager.exception.TagException;
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
    public List<TagTreeResponseVo> selectTreeByThemeId(Integer themeId) {
        Set<Integer> tags = new HashSet<>(); // 记录已经访问过的节点id，防止出现环路
        Queue<TagTreeResponseVo> Q = new LinkedList<>();
        List<TagTreeResponseVo> res = new ArrayList<>();
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("theme_id", themeId);
        queryWrapper.eq("father_id", 0);
        queryWrapper.eq("state", TagState.NORMAL.getCode());
        Tag tmp = tagMapper.selectOne(queryWrapper);
        if (tmp == null) return res;
        TagTreeResponseVo root = new TagTreeResponseVo(tmp);
        Q.add(root);
        while (Q.size() > 0) {
            TagTreeResponseVo top = Q.poll();
            if (tags.contains(top.getTagId()))
                break;
            tags.add(top.getTagId());
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("father_id", top.getTagId());
            queryWrapper.eq("state", TagState.NORMAL.getCode());
            List<Tag> children = tagMapper.selectList(queryWrapper);
            Collections.sort(children);
            List<TagTreeResponseVo> childrenList = children.stream()
                    .map(source -> new TagTreeResponseVo(source))
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
        queryWrapper.eq("state", TagState.NORMAL.getCode());
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
            queryWrapper.eq("state", TagState.NORMAL.getCode());
            List<Tag> tagList = tagMapper.selectList(queryWrapper);
            tagList.forEach(i -> Q.add(i.getId()));
        }
        return res;
    }

    @Override
    public List<TagRemovedVo> selectRemovedList(Integer themeId) {
        List<TagOperationDTO> list = themeOperationMapper.selectRemovedTag(themeId, TagState.REMOVED.getCode());
        List<TagRemovedVo> res = list.stream()
                .map(i -> new TagRemovedVo(i))
                .collect(Collectors.toList());
        return res;
    }

    @Override
    public List<TagOperationVo> selectOperations(Integer themeId) {
        List<TagOperationDTO> list = themeOperationMapper.selectOperations(themeId);
        List<TagOperationVo> res = list.stream()
                .map(i -> new TagOperationVo(i))
                .collect(Collectors.toList());
        return res;
    }

    // TODO 待添加组的权限验证
    @Override
    public int addTag(TagAddVo addVo, Integer userId) {
        Tag father = tagMapper.selectById(addVo.getFatherId());
        if (father == null)
            throw new TagException("不存在该父节点");
        Tag tag = new Tag(addVo);
        tag.setCreatorId(userId);
        tag.setThemeId(father.getThemeId());
        Integer order = tagMapper.selectMaxOrder(tag.getFatherId());
        if (order == null) tag.setInnerOrder(0);
        else tag.setInnerOrder(order+1);
        tagMapper.insert(tag);
        int tagId = tag.getId();
//        ThemeOperation operation = ThemeOperation.builder()
//                .operatorId(userId)
//                .themeId(father.getThemeId())
//                .tagId(tagId)
//                .type(OperationType.ADD.getCode())
//                .build();
//        themeOperationMapper.insert(operation);
        return tagId;
    }

    // TODO 待添加组的权限验证
    @Override
    public int renameTag(TagRenameVo renameVo, Integer userId) {
        Tag tempTag = operable(renameVo.getTagId());
        Tag tag = new Tag();
        tag.setId(renameVo.getTagId());
        tag.setName(renameVo.getName());
        tagMapper.updateById(tag);
        ThemeOperation operation;
        // 若之前名字为空，则是新增节点操作（前端新增节点默认名字为空）
        if (StringUtils.isBlank(tempTag.getName()))
            operation = ThemeOperation.builder()
                    .operatorId(userId)
                    .themeId(tempTag.getThemeId())
                    .tagId(tag.getId())
                    .type(OperationType.ADD.getCode())
                    .build();
        else
            operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(tempTag.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.RENAME.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    // TODO 待添加组的权限验证
    @Override
    public int removeTag(TagRemoveOrRePositionVo removeVo, User user) {
        Tag tempTag = operable(removeVo.getTagId());
        if (tempTag.getFatherId() == 0)
            throw new TagException("根节点无法被删除");
        Tag tag = new Tag();
        tag.setId(removeVo.getTagId());
        tag.setState(1);
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(user.getId())
                .themeId(tempTag.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.REMOVE.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    // TODO 待添加组的权限验证
    @Override
    public int changePosition(TagRemoveOrRePositionVo rePositionVo, Integer userId) {
        Tag tempTag = operable(rePositionVo.getTagId());
        Tag tag = new Tag();
        tag.setId(rePositionVo.getTagId());
        tag.setPosition(!tempTag.isPosition());
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(tempTag.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.MOVE.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    // TODO 待添加组的权限验证
    @Override
    public int changeOrder(TagReOrderVo reOrderVo, Integer userId) {
        Tag tempTag = operable(reOrderVo.getMovedTagId());
        Tag insertTag = tagMapper.selectById(reOrderVo.getInsertTagId());
        if (insertTag == null)
            throw new TagException("被交换节点不存在！");
        if (!tempTag.getThemeId().equals(insertTag.getThemeId()))
            throw new TagException("两个节点必须在同一张脑图中！");
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
                .themeId(tempTag.getThemeId())
                .tagId(tempTag.getId())
                .type(OperationType.MOVE.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    @Override
    public int reparentTag(TagReparentVo reparentVo, Integer userId) {
        Tag tempTag = operable(reparentVo.getTagId());
        Tag father = tagMapper.selectById(reparentVo.getFatherId());
        if (father == null)
            throw new TagException("无法移动节点，因为没有对应父节点");
        if (!tempTag.getThemeId().equals(father.getThemeId()))
            throw new TagException("两个节点必须在同一张脑图中！");
        Tag tag = new Tag();
        tag.setId(reparentVo.getTagId());
        tag.setFatherId(reparentVo.getFatherId());
        Integer order = tagMapper.selectMaxOrder(tag.getFatherId());
        if (order == null) tag.setInnerOrder(0);
        else tag.setInnerOrder(order+1);
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(tempTag.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.REPARENT.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    @Override
    public int recoverTag(Integer tagId, Integer userId) {
        Tag temp = tagMapper.selectById(tagId);
        if (!temp.getState().equals(TagState.REMOVED.getCode()))
            throw new TagException("只能恢复被删除节点");
        temp.setState(TagState.NORMAL.getCode());
        return tagMapper.updateById(temp);
    }

    private Tag operable(Integer tagId) {
        Tag tempTag = tagMapper.selectById(tagId);
        if (tempTag.getState() == 2)
            throw new MyAuthenticationException("节点已被锁定，无法操作");
        if (tempTag == null)
            throw new TagException("被操作节点不存在");
        if (tempTag.getState() != 0)
            throw new TagException("节点不可操作");
        return tempTag;
    }
}
