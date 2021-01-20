package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.dto.TagOperationDTO;
import com.syf.papermanager.bean.entity.*;
import com.syf.papermanager.bean.enums.OperationType;
import com.syf.papermanager.bean.enums.TagState;
import com.syf.papermanager.bean.enums.ThemeState;
import com.syf.papermanager.bean.vo.operation.OperationQueryVo;
import com.syf.papermanager.bean.vo.page.PageQueryVo;
import com.syf.papermanager.bean.vo.tag.request.TagRemovedQueryVo;
import com.syf.papermanager.bean.vo.tag.response.TagOperationVo;
import com.syf.papermanager.bean.vo.tag.response.TagRemovedVo;
import com.syf.papermanager.bean.vo.theme.*;
import com.syf.papermanager.bean.vo.user.MemberResponseVo;
import com.syf.papermanager.bean.vo.user.UserResponseVo;
import com.syf.papermanager.exception.FileUploadException;
import com.syf.papermanager.exception.MyAuthenticationException;
import com.syf.papermanager.exception.ThemeException;
import com.syf.papermanager.mapper.*;
import com.syf.papermanager.service.EmailService;
import com.syf.papermanager.service.FileService;
import com.syf.papermanager.service.ThemeService;
import com.syf.papermanager.utils.MyIterables;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xmind.core.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service.Impl
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 23:16
 */
@Slf4j
@Service
public class ThemeServiceImpl extends ServiceImpl<ThemeMapper, Theme> implements ThemeService {
    @Resource
    ThemeMapper themeMapper;
    @Resource
    TagMapper tagMapper;
    @Resource
    PaperTagMapper paperTagMapper;
    @Resource
    PaperMapper paperMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    ThemeMemberMapper themeMemberMapper;
    @Resource
    ThemeOperationMapper themeOperationMapper;
    @Resource
    FileService fileService;
    @Resource
    EmailService emailService;
    @Override
    public List<Theme> selectList(Integer creatorId, Integer selfId) {
//        QueryWrapper<Theme> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("creator_id", creatorId);
//        queryWrapper.eq("state", ThemeState.NORMAL.getCode());
//        queryWrapper.ne(selfId != null, "id", selfId);
//        return themeMapper.selectList(queryWrapper);
        return themeMemberMapper.selectThemeList(creatorId, selfId);
    }

    @Override
    public Page<Theme> selectPageList(ThemeQueryVo queryVo, Integer userId) {
        Theme theme = new Theme();
        theme.setCreatorId(userId);
        QueryWrapper<Theme> queryWrapper = new QueryWrapper<>(theme);
        queryWrapper.like(StringUtils.isNotBlank(queryVo.getName()), "name", queryVo.getName());
        if (queryVo.isRemoved())
            queryWrapper.eq("state", ThemeState.REMOVED.getCode());
        else queryWrapper.eq("state", ThemeState.NORMAL.getCode());
        queryWrapper.orderByDesc("update_time");
        Page<Theme> page = new Page<>(queryVo.getCurrentPage(), queryVo.getPageSize());
        return themeMapper.selectPage(page, queryWrapper);
    }

    @Override
    public IPage<ThemeResponseVo> selectInvitedList(PageQueryVo queryVo, Integer userId) {
       Page<ThemeResponseVo> page = new Page<>(queryVo.getCurrentPage(), queryVo.getPageSize());
       return themeMemberMapper.selectByUserId(page, userId);
    }

    @Override
    public Page<TagOperationVo> selectOperations(OperationQueryVo queryVo, Integer userId) {
        groupAble(queryVo.getThemeId(), userId); // 组成员可以查看操作记录
        Page<TagOperationDTO> page = new Page<>(queryVo.getCurrentPage(), queryVo.getPageSize());
        IPage<TagOperationDTO> data = themeOperationMapper.selectOperations(page, queryVo.getThemeId());
        Page<TagOperationVo> res = new Page<>();
        res.setSize(data.getSize());
        res.setCurrent(data.getCurrent());
        res.setPages(data.getPages());
        res.setTotal(data.getTotal());
        List<TagOperationVo> records = data.getRecords().stream()
                .map(i -> new TagOperationVo(i))
                .collect(Collectors.toList());
        res.setRecords(records);
        return res;
    }

    @Override
    public Page<TagRemovedVo> selectRemovedTagList(TagRemovedQueryVo queryVo, Integer userId) {
        groupAble(queryVo.getThemeId(), userId); // 组成员可以查看回收站
        Page<TagOperationDTO> page = new Page<>(queryVo.getCurrentPage(), queryVo.getPageSize());
        IPage<TagOperationDTO> data = themeOperationMapper.selectRemovedTag(page, queryVo.getThemeId(), TagState.REMOVED.getCode());
        Page<TagRemovedVo> res = new Page<>();
        res.setSize(data.getSize());
        res.setCurrent(data.getCurrent());
        res.setPages(data.getPages());
        res.setTotal(data.getTotal());
        List<TagRemovedVo> records = data.getRecords().stream()
                .map(i -> new TagRemovedVo(i))
                .collect(Collectors.toList());
        res.setRecords(records);
        return res;
    }

    @Override
    public MemberResponseVo selectMembers(Integer themeId, Integer userId) {
        Theme tmp = groupAble(themeId, userId);
        User creator = userMapper.selectById(tmp.getCreatorId());
        if (creator == null)
            throw new ThemeException("脑图创建者不存在");
        MemberResponseVo members = new MemberResponseVo();
        members.setCreator(new UserResponseVo(creator));
        List<UserResponseVo> memberList = themeMemberMapper.selectMembers(themeId);
        members.setMembers(memberList);
        return members;
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
        themeOperable(updateVo.getId(), userId);
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

    @Override
    @Transactional
    public int deleteTheme(Integer themeId, Integer userId) {
        deleteAble(themeId, userId);
        Integer rootId = tagMapper.selectRootTag(themeId);
        if (rootId == null)
            throw  new ThemeException("根节点不存在！");
        Queue<Integer> Q = new LinkedList<>();
        List<Integer> deleteIds = new ArrayList<>();
        List<String> filePaths = new ArrayList<>();
        Q.add(rootId);
        while (Q.size() > 0) {
            int top = Q.poll();
            deleteIds.add(top);
            List<Integer> children = tagMapper.selectChildrenIds(top);
            children.forEach(i -> Q.add(i));
        }
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
        tagMapper.deleteBatchIds(deleteIds);
        themeMapper.deleteById(themeId);
        filePaths.forEach(i -> fileService.deleteFile(i));
        return deleteIds.size();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int createFromXmind(MultipartFile file, String themeName, String description, Integer userId) throws RuntimeException {
        Queue<ITopic> Q = new LinkedList<>();
        Queue<Integer> QT = new LinkedList<>();
        IWorkbook workbook;
        AtomicInteger rightNumber = new AtomicInteger();
        try {

            IWorkbookBuilder builder = Core.getWorkbookBuilder();
            String[] filename = file.getOriginalFilename().split("\\.");
            log.info(file.getContentType());
            if (!filename[1].equals("xmind"))
                throw new ThemeException("文件格式不正确");
            File xmind = File.createTempFile(filename[0], filename[1]);
            file.transferTo(xmind);
            workbook = builder.loadFromFile(xmind);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileUploadException("xmind文档上传错误");
        } catch (CoreException e) {
            e.printStackTrace();
            throw new ThemeException("xmind解析错误");
        } catch (FileUploadException e) {
            throw e;
        }
        ISheet defSheet = workbook.getPrimarySheet();// 获取主Sheet
        ITopic rootTopic = defSheet.getRootTopic(); // 获取根Topic
        // 默认左右节点对半分
        int childrenNum = rootTopic.getAllChildren().size();
        rightNumber.set(childrenNum/2);
        // 获取右边位置节点数量
        List<ITopicExtension> extensions = rootTopic.getExtensions();
        extensions.forEach(i -> {
            List<ITopicExtensionElement> e = i.getContent().getChildren();
            for (int j = 0; j < e.size(); j++) {
                if (e.get(j).getName().equals("right-number")) {
                    rightNumber.set(Integer.parseInt(e.get(j).getTextContent()));
                    break;
                }
            }
        });
        Theme theme = new Theme();
        theme.setName(themeName);
        theme.setDescription(description);
        theme.setCreatorId(userId);
        themeMapper.insert(theme);
        int themeId = theme.getId();
        Tag rootTag = new Tag();
        rootTag.setName(rootTopic.getTitleText());
        rootTag.setCreatorId(userId);
        rootTag.setThemeId(themeId);
        rootTag.setInnerOrder(0);
        tagMapper.insert(rootTag);
        Q.add(rootTopic);
        QT.add(rootTag.getId());
        int rightTags = rightNumber.get();
        // 广度优先插入数据库
        while (Q.size() > 0) {
            ITopic top = Q.poll();
            int fatherId = QT.poll();
            Tag father = tagMapper.selectById(fatherId);
            List<ITopic> children = top.getAllChildren();
            MyIterables.forEach(children, (index, i) -> {
                Tag tag = new Tag();
                tag.setName(i.getTitleText());
                tag.setFatherId(father.getId());
                tag.setInnerOrder(index);
                if (father.getFatherId() == 0)
                    tag.setPosition(index < rightTags? false: true);
                else tag.setPosition(father.isPosition());
                tag.setThemeId(themeId);
                tag.setCreatorId(userId);
                tagMapper.insert(tag);
                QT.add(tag.getId());
                Q.add(i);
            });
        }
        return themeId;
    }

    @Override
    public int updateThemeState(Integer themeId, Integer userId, Integer stateCode) {
        themeOperable(themeId, userId, stateCode);
        Theme theme = new Theme();
        theme.setId(themeId);
        theme.setState(stateCode);
        return themeMapper.updateById(theme);
    }

    @Override
    @Transactional
    public void combineTheme(ThemeCombineVo combineVo, Integer userId) {
        int themeId = combineVo.getFromThemeId();
        int toTagId = combineVo.getToTagId();
        themeOperable(themeId, userId); // 只能把自己的作为源
        Tag toTag = tagOperable(toTagId, userId);
        int toThemeId = toTag.getThemeId();
        groupAble(toThemeId, userId); // 团队的和自己的都可以作为目标
        if (toThemeId == themeId)
            throw new ThemeException("不能合并到自己");
        boolean position = toTag.isPosition();
        Queue<Pair<Integer, Integer>> QS = new LinkedList<>(); // 记录原始节点id
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("theme_id", themeId);
        queryWrapper.eq("father_id", 0);
        queryWrapper.eq("state", TagState.NORMAL.getCode());
        Tag tmp = tagMapper.selectOne(queryWrapper);
        if (tmp == null)
            throw new ThemeException("源脑图没有节点");
        // 用pair来记录节点信息，k保存原始id，v保存新的fatherId
        QS.add(new Pair<>(tmp.getId(), toTagId));
        while (QS.size() > 0) {
            Pair<Integer, Integer> top = QS.poll();
            Tag source = tagMapper.selectById(top.getKey());
            Tag tag = new Tag(source);
            tag.setThemeId(toThemeId);
            tag.setFatherId(top.getValue());
            tag.setPosition(position);
            tagMapper.insert(tag);
            // 将与脑图关联的paper记录也新建一遍
            List<PaperTag> paperTags = paperTagMapper.selectByTagId(top.getKey());
            paperTags.forEach(i -> {
                i.setTagId(tag.getId());
                paperTagMapper.insert(i);
            });
            List<Integer> childrenIds = tagMapper.selectChildrenIds(top.getKey());
            childrenIds.forEach(i -> QS.add(new Pair<>(i,tag.getId())));
        }
        ThemeOperation operation = ThemeOperation.builder()
                .operatorId(userId)
                .themeId(tmp.getId())
                .tagId(toTagId)
                .type(OperationType.COMBINE.getCode())
                .build();
        themeOperationMapper.insert(operation);
    }

    @Override
    public int inviteMember(ThemeInviteVo inviteVo, User creator) throws Exception {
        int themeId = inviteVo.getThemeId();
        int userId = creator.getId();
        // themeOperable(themeId, userId); // 只有创建者可以邀请
        Theme tmp = groupAble(themeId, userId); // 成员也可以邀请
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", inviteVo.getEmail());
        User invitee = userMapper.selectOne(queryWrapper);
        if (invitee == null)
            throw new ThemeException("用户不存在");
        if (invitee.getId().equals(userId))
            throw new ThemeException("不能邀请自己");
        ThemeMember themeMember = new ThemeMember();
        themeMember.setThemeId(themeId);
        themeMember.setUserId(invitee.getId());
        try {
            themeMemberMapper.insert(themeMember);
        } catch (DuplicateKeyException e) {
            throw new ThemeException("已经邀请过该成员：" + invitee.getUsername());
        } catch (Exception e) {
            throw e;
        }
        String content = "您被" + creator.getUsername() + "邀请参加脑图<" + tmp.getName() + ">的编辑，快去看看吧";
        emailService.sendSimpleMail(inviteVo.getEmail(), content, "脑图邀请通知");
        return 1;
    }

    @Override
    public int exitTheme(Integer themeId, Integer userId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("theme_id", themeId);
        return themeMemberMapper.deleteByMap(map);
    }

    @Override
    public int kickOffMember(ThemeKickOffVo kickVo, Integer userId) {
        int themeId = kickVo.getThemeId();
        themeOperable(themeId, userId);
        int memberId = kickVo.getUserId();
        return exitTheme(themeId, memberId);
    }

    private Theme themeOperable(Integer themeId, Integer userId) {
        Theme tmpTheme = themeMapper.selectById(themeId);
        if (tmpTheme == null)
            throw new ThemeException("脑图不存在");
        if (!tmpTheme.getState().equals(ThemeState.NORMAL.getCode()))
            throw new ThemeException("脑图当前状态不可操作");
        if (!tmpTheme.getCreatorId().equals(userId))
            throw new MyAuthenticationException("您没有操作该脑图——" + tmpTheme.getName() + " 的权限");
        return tmpTheme;
    }

    private Theme deleteAble(Integer themeId, Integer userId) {
        Theme tmpTheme = themeMapper.selectById(themeId);
        if (tmpTheme == null)
            throw new ThemeException("脑图不存在");
        if (!tmpTheme.getState().equals(ThemeState.REMOVED.getCode()))
            throw new ThemeException("脑图不在回收站");
        if (!tmpTheme.getCreatorId().equals(userId))
            throw new MyAuthenticationException("您没有操作该脑图——" + tmpTheme.getName() + " 的权限");
        return tmpTheme;
    }

    private Theme themeOperable(Integer themeId, Integer userId, Integer state) {
        Theme tmp = themeMapper.selectById(themeId);
        if (tmp == null)
            throw new ThemeException("脑图不存在");
        if (tmp.getState().equals(state)) // 当前状态与目标状态一致
            throw new ThemeException("脑图状态有误");
        if (!tmp.getCreatorId().equals(userId))
            throw new MyAuthenticationException("您没有操作该脑图——" + tmp.getName() + " 的权限");
        return tmp;
    }

//    private void combineAble(Integer themeId, Integer userId) {
//        Theme tmp = themeMapper.selectById(themeId);
//        if (tmp == null)
//            throw new ThemeException("脑图不存在！");
//        QueryWrapper<ThemeMember> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", userId);
//        queryWrapper.eq("theme_id", themeId);
//        int count = themeMemberMapper.selectCount(queryWrapper);
//        if (count == 0 && !tmp.getCreatorId().equals(userId))
//            throw new  MyAuthenticationException("您没有合并到目标脑图的权限！");
//    }

    private Theme groupAble(Integer themeId, Integer userId) {
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
        return tmp;
    }

    private Tag tagOperable(Integer tagId, Integer userId) {
        Tag tmp = tagMapper.selectById(tagId);
        if (tmp == null)
            throw new ThemeException("新的父节点不存在");
        // 不用判断对该tag有没有操作权限，只用判断对tag所属的theme有没有操作权限
        return tmp;
    }
}
