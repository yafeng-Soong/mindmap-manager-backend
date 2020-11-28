package com.syf.papermanager.controller;

import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.tag.TagAddVo;
import com.syf.papermanager.bean.vo.tag.TagRenameVo;
import com.syf.papermanager.bean.vo.tag.TagResponseVo;
import com.syf.papermanager.bean.vo.tag.TagSimpleResponseVo;
import com.syf.papermanager.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.controller
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:03
 */
@Api(tags = "脑图节点相关功能的接口")
@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController extends BaseController {
    @Resource
    TagService tagService;
    @ApiOperation("获取一个脑图中的树形节点信息")
    @GetMapping("/getTree")
    public ResponseEntity getTree(int themeId) {
        ResponseEntity response = new ResponseEntity();
        List<TagResponseVo> res = tagService.selectTreeByThemeId(themeId);
        response.setData(res);
        return response;
    }

    @ApiOperation("获取一个脑图中所有节点的id和name信息")
    @GetMapping("/getSimpleList")
    public ResponseEntity getSimpleList(int themeId) {
        ResponseEntity response = new ResponseEntity();
        List<TagSimpleResponseVo> res = tagService.selectSimpleList(themeId);
        response.setData(res);
        return response;
    }

    @ApiOperation("增加节点")
    @PostMapping("/add")
    public ResponseEntity addTag(@RequestBody @Validated TagAddVo addVo,
                                 BindingResult bindingResult) {
        ResponseEntity response = new ResponseEntity();
        if (validateParams(response, bindingResult)) {
            return response;
        }
        User currentUser = getCurrentUser();
        int tagId = tagService.addTag(addVo, currentUser.getId());
        response.setData(tagId);
        return response;
    }

    @ApiOperation("修改节点名称")
    @PostMapping("/rename")
    public ResponseEntity rename(@RequestBody @Validated TagRenameVo renameVo,
                                 BindingResult bindingResult) {
        ResponseEntity response = new ResponseEntity();
        if (validateParams(response, bindingResult)) {
            return response;
        }
        tagService.renameTag(renameVo);
        response.setData("修改节点名成功");
        return response;
    }
}
