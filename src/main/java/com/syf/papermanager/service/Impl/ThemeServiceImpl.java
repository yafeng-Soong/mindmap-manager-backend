package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.enums.ThemeState;
import com.syf.papermanager.bean.vo.theme.ThemeAddVo;
import com.syf.papermanager.bean.vo.theme.ThemeQueryVo;
import com.syf.papermanager.bean.vo.theme.ThemeUpdateVo;
import com.syf.papermanager.exception.FileUploadException;
import com.syf.papermanager.exception.ThemeException;
import com.syf.papermanager.mapper.TagMapper;
import com.syf.papermanager.mapper.ThemeMapper;
import com.syf.papermanager.service.ThemeService;
import com.syf.papermanager.utils.MyIterables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xmind.core.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

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
    @Override
    public List<Theme> selectList(Integer creatorId) {
        QueryWrapper<Theme> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", creatorId);
        queryWrapper.eq("state", ThemeState.NORMAL.getCode());
        return themeMapper.selectList(queryWrapper);
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
        Theme tmp = themeMapper.selectById(themeId);
        if (tmp == null)
            throw new ThemeException("脑图不存在");
        if (!tmp.getCreatorId().equals(userId))
            throw new ThemeException("您没有操作权限");
        Theme theme = new Theme();
        theme.setId(themeId);
        theme.setState(stateCode);
        return themeMapper.updateById(theme);
    }

}
