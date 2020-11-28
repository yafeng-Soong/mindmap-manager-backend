package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.Paper;
import com.syf.papermanager.bean.vo.paper.PaperQueryByTagVo;
import com.syf.papermanager.bean.vo.paper.PaperQueryVo;
import com.syf.papermanager.bean.vo.paper.PaperSubmitVo;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/18 11:13
 */
public interface PaperService extends IService<Paper> {
    List<Paper> selectByTagId(int tagId);
    int insertPaper(PaperSubmitVo submitVo, int userId);
    int updateFilePath(String filePath, int paperId);
    Page<Paper> selectPageList(PaperQueryVo queryVo, Integer uerId);
    Page<Paper> selectPageListByTagId(PaperQueryByTagVo queryVo);
    boolean hasPaper(Integer paperId, Integer userId);
}
