package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.dto.TagOperationDTO;
import com.syf.papermanager.bean.entity.*;
import com.syf.papermanager.bean.enums.OperationType;
import com.syf.papermanager.bean.enums.TagState;
import com.syf.papermanager.bean.enums.ThemeState;
import com.syf.papermanager.bean.vo.tag.request.*;
import com.syf.papermanager.bean.vo.tag.response.TagOperationVo;
import com.syf.papermanager.bean.vo.tag.response.TagRemovedVo;
import com.syf.papermanager.bean.vo.tag.response.TagSimpleResponseVo;
import com.syf.papermanager.bean.vo.tag.response.TagTreeResponseVo;
import com.syf.papermanager.exception.MyAuthenticationException;
import com.syf.papermanager.exception.TagException;
import com.syf.papermanager.exception.ThemeException;
import com.syf.papermanager.mapper.*;
import com.syf.papermanager.service.FileService;
import com.syf.papermanager.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    PaperTagMapper paperTagMapper;
    @Resource
    PaperMapper paperMapper;
    @Resource
    ThemeMemberMapper themeMemberMapper;
    @Resource
    ThemeMapper themeMapper;
    @Resource
    FileService fileService;
    /***
     * 数据库中每个节点只有自己父节点信息，使用深度优先遍历构建树形结构
     * @param themeId
     * @return 树形结构的taglist
     */
    @Override
    public List<TagTreeResponseVo> selectTreeByThemeId(Integer themeId, Integer userId) {
        Set<Integer> tags = new HashSet<>(); // 记录已经访问过的节点id，防止出现环路
        Queue<TagTreeResponseVo> Q = new LinkedList<>();
        List<TagTreeResponseVo> res = new ArrayList<>();
        Tag tmp = selectAble(themeId, userId);
        TagTreeResponseVo root = new TagTreeResponseVo(tmp);
        Q.add(root);
        while (Q.size() > 0) {
            TagTreeResponseVo top = Q.poll();
            if (tags.contains(top.getTagId()))
                break;
            tags.add(top.getTagId());
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
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
    public List<TagSimpleResponseVo> selectSimpleList(Integer themeId, Integer userId) {
        Set<Integer> tags = new HashSet<>(); // 记录已经访问过的节点id，防止出现环路
        Queue<Integer> Q = new LinkedList<>();
        List<TagSimpleResponseVo> res = new ArrayList<>();
        Tag tmp = selectAble(themeId, userId);
        Q.add(tmp.getId());
        while (Q.size() > 0) {
            int top = Q.poll();
            if (tags.contains(top))
                break;
            tags.add(top);
            Tag tag = tagMapper.selectById(top);
            res.add(new TagSimpleResponseVo(tag));
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("father_id", top);
            queryWrapper.eq("state", TagState.NORMAL.getCode());
            List<Tag> tagList = tagMapper.selectList(queryWrapper);
            tagList.forEach(i -> Q.add(i.getId()));
        }
        tags.clear();
        return res;
    }

    // TODO 待添加组的权限验证
    @Override
    public int addTag(TagAddVo addVo, Integer userId) {
        Tag father = tagMapper.selectById(addVo.getFatherId());
        if (father == null)
            throw new TagException("不存在该父节点");
        groupAble(father.getThemeId(), userId);
        Tag tag = new Tag(addVo);
        tag.setCreatorId(userId);
        tag.setThemeId(father.getThemeId());
        Integer order = tagMapper.selectMaxOrder(tag.getFatherId());
        if (order == null) tag.setInnerOrder(0);
        else tag.setInnerOrder(order+1);
        tagMapper.insert(tag);
        int tagId = tag.getId();
        return tagId;
    }

    @Override
    public int renameTag(TagRenameVo renameVo, Integer userId) {
        Tag tempTag = operable(renameVo.getTagId(), userId);
        Tag tag = new Tag();
        tag.setId(renameVo.getTagId());
        tag.setName(renameVo.getName());
        tag.setPosition(tempTag.isPosition()); // 有默认值
        tagMapper.updateById(tag);
        ThemeOperation operation;
        // 若创建时间等于更新时间，则是新增节点操作（前端新增节点默认名字为空）
        if (tempTag.getCreateTime().compareTo(tempTag.getUpdateTime()) == 0)
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

    @Override
    public int removeTag(TagRemoveOrRePositionVo removeVo, User user) {
        Tag tempTag = operable(removeVo.getTagId(), user.getId());
        if (tempTag.getFatherId() == 0)
            throw new TagException("根节点无法被删除");
        Tag tag = new Tag();
        tag.setId(removeVo.getTagId());
        tag.setState(1);
        tag.setPosition(tempTag.isPosition()); // 默认值
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(user.getId())
                .themeId(tempTag.getThemeId())
                .tagId(tag.getId())
                .type(OperationType.REMOVE.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    @Override
    @Transactional
    public int deleteTag(Integer tagId, Integer userId) {
        Tag tmp = deletable(tagId, userId);
        Queue<Integer> Q = new LinkedList<>();
        List<Integer> deleteIds = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        Q.add(tmp.getId());
        while(Q.size() > 0) {
            int top = Q.poll();
            deleteIds.add(top);
            List<Integer> children = tagMapper.selectChildrenIds(top);
            children.forEach(i -> Q.add(i));
        }
        // 把所有子节点及其关联paper全删除
        for (Integer childId: deleteIds) {
            List<Paper> papers = paperTagMapper.selectAssociatedPaper(childId);
            papers.forEach(i -> {
                // paper只与被删除节点关联则删掉
                int associates = paperTagMapper.selectAssociatedTagNumber(i.getId());
                if (associates == 1) {
                    paperMapper.deleteById(i.getId());
                    filePaths.add(i.getFilePath());
                }
            });
        }
        // 只保留根节点，供操作记录展示
        if (deleteIds.size() > 1)
            deleteIds.remove(0);
        tagMapper.deleteBatchIds(deleteIds);
        // 删除根节点除本次操作外的其他操作记录
        themeOperationMapper.deleteByTagId(tmp.getId());
        // 修改节点状态
        Tag deleted = new Tag();
        deleted.setId(tmp.getId());
        deleted.setPosition(tmp.isPosition());
        deleted.setState(TagState.DELETED.getCode());
        tagMapper.updateById(deleted);
        // 插入一条彻底删除记录
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(tmp.getThemeId())
                .tagId(tmp.getId())
                .type(OperationType.DELETED.getCode())
                .build();
        themeOperationMapper.insert(operation);
        // 文件删除放在最后，因为无法回滚
        filePaths.forEach(i -> fileService.deleteFile(i));
        return deleteIds.size() + 1;
    }

    @Override
    public int changePosition(TagRemoveOrRePositionVo rePositionVo, Integer userId) {
        Tag tempTag = operable(rePositionVo.getTagId(), userId);
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

    @Override
    public int changeOrder(TagReOrderVo reOrderVo, Integer userId) {
        Tag tempTag = operable(reOrderVo.getMovedTagId(), userId);
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
        else
            tag.setPosition(tempTag.isPosition());
        // updateById会把表中的有默认值的字段给更新成默认值
        // position有默认值
        tagMapper.updateById(tag);
        tag.setId(insertTag.getId());
        tag.setInnerOrder(tempTag.getInnerOrder());
        tag.setPosition(insertTag.isPosition());
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
        Tag tempTag = operable(reparentVo.getTagId(), userId);
        Tag father = tagMapper.selectById(reparentVo.getFatherId());
        if (father == null)
            throw new TagException("无法移动节点，因为没有对应父节点");
        if (!tempTag.getThemeId().equals(father.getThemeId()))
            throw new TagException("两个节点必须在同一张脑图中！");
        Tag tag = new Tag();
        tag.setId(reparentVo.getTagId());
        tag.setFatherId(reparentVo.getFatherId());
        tag.setPosition(father.isPosition());
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
        Tag temp = recoverable(tagId, userId);
        Tag tag = new Tag();
        tag.setId(tagId);
        tag.setState(TagState.NORMAL.getCode());
        tag.setPosition(temp.isPosition());
        tagMapper.updateById(tag);
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(temp.getThemeId())
                .tagId(tagId)
                .type(OperationType.RECOVER.getCode())
                .build();
        return themeOperationMapper.insert(operation);
    }

    private Tag operable(Integer tagId, Integer userId) {
        Tag tempTag = tagMapper.selectById(tagId);
        if (tempTag == null)
            throw new TagException("被操作节点不存在");
        groupAble(tempTag.getThemeId(), userId);
        if (tempTag.getState().equals(TagState.LOCKED.getCode()))
            throw new MyAuthenticationException("节点已被锁定，无法操作");
        if (!tempTag.getState().equals(TagState.NORMAL.getCode()))
            throw new TagException("节点不可操作");
        int fatherId = tempTag.getFatherId();
        while (fatherId != 0) {
            Tag father = tagMapper.selectById(fatherId);
            if (father.getState().equals(TagState.REMOVED.getCode()))
                throw new TagException("节点已被删除");
            fatherId = father.getFatherId();
        }
        // TODO 等待添加团队权限
        return tempTag;
    }

    private Tag recoverable(Integer tagId, Integer userId) {
        Tag tmp = tagMapper.selectById(tagId);
        if (tmp == null)
            throw new TagException("节点不存在！");
        groupAble(tmp.getThemeId(), userId);
        if (!tmp.getState().equals(TagState.REMOVED.getCode()))
            throw new TagException("只能恢复被删除节点");
        // TODO 等待添加组权限验证
        return tmp;
    }

    private Tag deletable(Integer tagId, Integer userId) {
        Tag tmp = tagMapper.selectById(tagId);
        if (tmp == null)
            throw new TagException("节点不存在！");
        groupAble(tmp.getThemeId(), userId);
        if (!tmp.getState().equals(TagState.REMOVED.getCode()))
            throw new TagException("节点不在回收站！");
        // TODO 等待添加权限验证
        return tmp;
    }

    private Tag selectAble(Integer themeId, Integer userId) {
        Theme theme = themeMapper.selectById(themeId);
        if (theme == null)
            throw new ThemeException("脑图不存在！");
        if (!theme.getState().equals(ThemeState.NORMAL.getCode()))
            throw new ThemeException("脑图当前状态不可操作");
        QueryWrapper<ThemeMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("theme_id", themeId);
        int count = themeMemberMapper.selectCount(queryWrapper);
        if (count == 0 && !theme.getCreatorId().equals(userId))
            throw new MyAuthenticationException("您没有操作脑图——" + theme.getName() + " 的权限！");
        QueryWrapper<Tag> query = new QueryWrapper<>();
        query.eq("theme_id", themeId);
        query.eq("father_id", 0);
        query.eq("state", TagState.NORMAL.getCode());
        Tag tag = tagMapper.selectOne(query);
        if (tag == null)
            throw new TagException("根节点不存在");
        return tag;
    }

    // 团队验证
    private void groupAble(Integer themeId, Integer userId) {
        Theme tmp = themeMapper.selectById(themeId);
        if (tmp == null)
            throw new ThemeException("脑图不存在！");
        if (!tmp.getState().equals(ThemeState.NORMAL.getCode()))
            throw new ThemeException("脑图当前状态不可操作");
        QueryWrapper<ThemeMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("theme_id", themeId);
        int count = themeMemberMapper.selectCount(queryWrapper);
        if (count == 0 && !tmp.getCreatorId().equals(userId))
            throw new MyAuthenticationException("您没有操作脑图——" + tmp.getName() + " 的权限！");
    }
}
