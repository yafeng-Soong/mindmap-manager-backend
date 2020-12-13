package com.syf.papermanager.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syf.papermanager.bean.entity.Paper;
import com.syf.papermanager.bean.entity.PaperTag;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.bean.vo.paper.PaperDeleteVo;
import com.syf.papermanager.bean.vo.paper.PaperQueryByTagVo;
import com.syf.papermanager.bean.vo.paper.PaperQueryVo;
import com.syf.papermanager.bean.vo.paper.PaperSubmitVo;
import com.syf.papermanager.exception.MyAuthenticationException;
import com.syf.papermanager.exception.PaperException;
import com.syf.papermanager.mapper.PaperMapper;
import com.syf.papermanager.mapper.PaperTagMapper;
import com.syf.papermanager.mapper.TagMapper;
import com.syf.papermanager.service.FileService;
import com.syf.papermanager.service.PaperService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service.Impl
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/18 11:13
 */
@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {
    @Resource
    TagMapper tagMapper;
    @Resource
    PaperMapper paperMapper;
    @Resource
    PaperTagMapper paperTagMapper;
    @Resource
    FileService fileService;

    @Override
    public List<Paper> selectByTagId(int tagId) {
        List<Paper> res = new ArrayList<>();
        Set<Integer> inList = new HashSet<>();
        Queue<Integer> Q = new LinkedList<>();
        Tag root = tagMapper.selectById(tagId);
        if (root == null) return  res;
        List<Integer> paperIdList = new ArrayList<>();
        Q.add(root.getId());
        while(Q.size() > 0) {
            int top = Q.poll();
            QueryWrapper<PaperTag> paperTagQuery = new QueryWrapper<>();
            paperTagQuery.eq("tag_id", top);
            List<PaperTag> paperTags = paperTagMapper.selectList(paperTagQuery);
            paperTags.forEach(i -> {
                if (!inList.contains(i.getPaperId())) {
                    paperIdList.add(i.getPaperId());
                    inList.add(i.getPaperId());
                }
            });
            QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
            tagQuery.eq("father_id", top);
            List<Tag> tags = tagMapper.selectList(tagQuery);
            tags.forEach(i -> Q.add(i.getId()));
        }
        Collections.reverse(paperIdList);
        paperIdList.forEach(i -> res.add(paperMapper.selectById(i)));
        return res;
    }

    @Override
    public Page<Paper> selectPageListByTagId(PaperQueryByTagVo queryVo) {
        int tagId = queryVo.getTagId();
        int currentPage = queryVo.getCurrentPage();
        int pageSize = queryVo.getPageSize();
        int start = (currentPage - 1) * pageSize;
        int end = currentPage * pageSize;
        Page<Paper> res = new Page<>();
        res.setCurrent(currentPage);
        res.setSize(pageSize);
        List<Paper> papers = new ArrayList<>();
        Set<Integer> inList = new HashSet<>();
        Queue<Integer> Q = new LinkedList<>();
        Tag root = tagMapper.selectById(tagId);
        if (root == null) {
            res.setTotal(0);
            res.setPages(1);
            return res;
        }
        List<Integer> paperIdList = new ArrayList<>();
        Q.add(root.getId());
        while(Q.size() > 0) {
            int top = Q.poll();
            QueryWrapper<PaperTag> paperTagQuery = new QueryWrapper<>();
            paperTagQuery.eq("tag_id", top);
            List<PaperTag> paperTags = paperTagMapper.selectList(paperTagQuery);
            paperTags.forEach(i -> {
                if (!inList.contains(i.getPaperId())) {
                    paperIdList.add(i.getPaperId());
                    inList.add(i.getPaperId());
                }
            });
            QueryWrapper<Tag> tagQuery = new QueryWrapper<>();
            tagQuery.eq("father_id", top);
            List<Tag> tags = tagMapper.selectList(tagQuery);
            tags.forEach(i -> Q.add(i.getId()));
        }
        int total = paperIdList.size();
        int pages = total/pageSize+1;
        if (start > total) {
            start = total - pageSize;
            end = total;
            res.setCurrent(pages);
        } else if (end > total) {
            end = total;
        }
        Collections.reverse(paperIdList);
        List<Integer> newList = paperIdList.subList(start, end);
        newList.forEach(i -> papers.add(paperMapper.selectById(i)));
        res.setRecords(papers);
        res.setTotal(total);
        res.setPages(pages);
        return res;
    }

    @Override
    public boolean hasPaper(Integer paperId, Integer userId) {
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uploader_id", userId);
        queryWrapper.eq("id", paperId);
        int num = paperMapper.selectCount(queryWrapper);
        return num == 1;
    }

    @Override
    @Transactional
    public int insertPaper(PaperSubmitVo submitVo, int userId) {
        Paper paper = new Paper();
        BeanUtils.copyProperties(submitVo, paper);
        paper.setUploaderId(userId);
        paperMapper.insert(paper);
        int paperId = paper.getId();
        List<Integer> tagIdList = submitVo.getTags();
        for (Integer i : tagIdList) {
            PaperTag paperTag = new PaperTag();
            paperTag.setTagId(i);
            paperTag.setPaperId(paperId);
            paperTag.setCreatorId(userId);
            paperTagMapper.insert(paperTag);
        }
        return paperId;
    }

    @Override
    public int updateFilePath(String filePath, int paperId) {
        Paper paper = new Paper();
        paper.setId(paperId);
        paper.setFilePath(filePath);
        return paperMapper.updateById(paper);
    }

    @Override
    public int deletePaper(Integer paperId, Integer userId) {
        Paper paper = deletable(paperId, userId);
        fileService.deleteFile(paper.getFilePath());
        return paperMapper.deleteById(paperId);
    }

    @Override
    public int deletePaperFromTheme(PaperDeleteVo deleteVo, Integer userId) {
        Paper paper = operable(deleteVo.getPaperId(), userId);
        int associates = paperMapper.selectAssociatedTagNumber(paper.getId());
        int affect;
        // 如果关联的节点不止一个，则只删除关联记录本身，否则删除论文本身
        if (associates > 1) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("tag_id", deleteVo.getTagId());
            updateWrapper.eq("paper_id", paper.getId());
            affect = paperTagMapper.delete(updateWrapper);
        } else {
            affect = paperMapper.deleteById(paper.getId());
            fileService.deleteFile(paper.getFilePath());
        }
        return affect;
    }

    @Override
    public Page<Paper> selectPageList(PaperQueryVo queryVo, Integer userId) {
        Paper paper = new Paper();
        paper.setUploaderId(userId);
        if (queryVo.getPublishYear() != null)
            paper.setPublishYear(queryVo.getPublishYear());
        Page<Paper> page = new Page<>(queryVo.getCurrentPage(), queryVo.getPageSize());
        QueryWrapper<Paper> queryWrapper = new QueryWrapper<>(paper);
        queryWrapper.like(StringUtils.isNotBlank(queryVo.getAuthor()), "author", queryVo.getAuthor());
        queryWrapper.like(StringUtils.isNotBlank(queryVo.getKeyword()), "keyword", queryVo.getKeyword());
        queryWrapper.like(StringUtils.isNotBlank(queryVo.getName()), "name", queryVo.getName());
        queryWrapper.orderByDesc("update_time");
        Page<Paper> res = paperMapper.selectPage(page, queryWrapper);
        return res;
    }

    private Paper deletable(Integer paperId, Integer userId) {
        Paper tmp = paperMapper.selectById(paperId);
        if (tmp == null)
            throw new PaperException("论文不存在！");
        if (!tmp.getUploaderId().equals(userId))
            throw new MyAuthenticationException("您无权限删除该论文");
        return tmp;
    }

    private Paper operable(Integer paperId, Integer userId) {
        Paper tmp = paperMapper.selectById(paperId);
        if (tmp == null)
            throw new PaperException("论文不存在！");
        // TODO 日后修改团队权限
//        if (!tmp.getUploaderId().equals(userId))
//            throw new MyAuthenticationException("您无权限操作该论文");
        if (tmp.getUploaderId().equals(userId))
            return tmp;
        List<Integer> userIds = paperMapper.selectOperators(paperId);
        if (userIds.contains(userId))
            return tmp;
        throw new MyAuthenticationException("您无权限操作论文：" + tmp.getName());
    }

}
