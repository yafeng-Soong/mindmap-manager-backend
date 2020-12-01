package com.syf.papermanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.Paper;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.page.PageResponseVo;
import com.syf.papermanager.bean.vo.paper.PaperQueryByTagVo;
import com.syf.papermanager.bean.vo.paper.PaperQueryVo;
import com.syf.papermanager.bean.vo.paper.PaperSubmitVo;
import com.syf.papermanager.service.FileService;
import com.syf.papermanager.service.PaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.controller
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/18 11:04
 */
@Api(tags = "论文相关接口")
@Slf4j
@RestController
@RequestMapping("/paper")
public class PaperController extends BaseController {
    @Resource
    PaperService paperService;
    @Resource
    FileService fileService;

    @ApiOperation("返回脑图中某个节点下的论文")
    @GetMapping("/getList")
    public ResponseEntity getListByTagId(int tagId) {
        ResponseEntity response = new ResponseEntity();
        List<Paper> papers = paperService.selectByTagId(tagId);
        response.setData(papers);
        return response;
    }

    @ApiOperation("返回脑图中某个节点下的论文")
    @PostMapping("/getListByTagId")
    public ResponseEntity getPageListByTagId(@RequestBody @Validated PaperQueryByTagVo queryByTagVo) {
        ResponseEntity response = new ResponseEntity();
        Page<Paper> res = paperService.selectPageListByTagId(queryByTagVo);
        PageResponseVo<Paper> data = new PageResponseVo<>(res);
        response.setData(data);
        return response;
    }

    @ApiOperation("查询自己上传的论文列表")
    @PostMapping("/getPageList")
    public ResponseEntity getPageList(@RequestBody @Validated PaperQueryVo paperQueryVo) {
        ResponseEntity response = new ResponseEntity();
        Page<Paper> res = paperService.selectPageList(paperQueryVo, getCurrentUser().getId());
        PageResponseVo<Paper> data = new PageResponseVo<>(res);
        response.setData(data);
        return response;
    }

    @ApiOperation("论文上传接口")
    @PostMapping("/submit")
    public ResponseEntity submit(@RequestBody @Validated PaperSubmitVo paperSubmitVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        int paperId = paperService.insertPaper(paperSubmitVo, currentUser.getId());
        response.setData(paperId);
        return response;
    }

    @ApiOperation("上传文件接口")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @PostMapping("/uploadFile")
    public ResponseEntity uploadFile(@RequestParam(value = "file") MultipartFile file,
                                     @RequestParam(value = "paperId") Integer paperId){
        ResponseEntity response = new ResponseEntity();
        String filePath;
        filePath = fileService.uploadFile(file);
        paperService.updateFilePath(filePath, paperId);
        response.setData("论文附件上传成功");
        return response;
    }

    @ApiOperation("更新文件接口")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @PostMapping("/updateFile")
    public ResponseEntity updateFile(@RequestParam(value = "file") MultipartFile file,
                                     @RequestParam(value = "paperId") Integer paperId) {
        ResponseEntity response = new ResponseEntity();
        Paper paper = paperService.getById(paperId);
        if (paper == null) {
            response.setErrorResponse();
            response.setData("论文不存在");
            return response;
        }
        fileService.deleteFile(paper.getFilePath());
        String filePath = fileService.uploadFile(file);
        paperService.updateFilePath(filePath, paperId);
        Paper newPaper = paperService.getById(paperId);
        response.setData(newPaper);
        return response;
    }

    @ApiOperation("文件删除接口")
    @DeleteMapping("/deletePaper")
    public ResponseEntity deletePaper(int paperId) {
        ResponseEntity response = new ResponseEntity();
        Paper paper = paperService.getById(paperId);
        if (paper == null) {
            response.setErrorResponse();
            response.setData("不存在该论文");
            return response;
        }
        User currentUser = getCurrentUser();
        if (paper.getUploaderId() != currentUser.getId()) {
            response.setErrorResponse();
            response.setData("只能删除自己的论文");
            return response;
        }
        fileService.deleteFile(paper.getFilePath());
        paperService.removeById(paperId);
        response.setData("删除成功");
        return response;
    }
}
