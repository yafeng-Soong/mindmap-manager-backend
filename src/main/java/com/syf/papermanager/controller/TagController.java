package com.syf.papermanager.controller;

import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.tag.request.*;
import com.syf.papermanager.bean.vo.tag.response.TagOperationVo;
import com.syf.papermanager.bean.vo.tag.response.TagRemovedVo;
import com.syf.papermanager.bean.vo.tag.response.TagSimpleResponseVo;
import com.syf.papermanager.bean.vo.tag.response.TagTreeResponseVo;
import com.syf.papermanager.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity getTree(@RequestParam("themeId") Integer themeId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        List<TagTreeResponseVo> res = tagService.selectTreeByThemeId(themeId, currentUser.getId());
        response.setData(res);
        return response;
    }

    @ApiOperation("获取一个脑图中所有节点的id和name信息")
    @GetMapping("/getSimpleList")
    public ResponseEntity getSimpleList(@RequestParam("themeId") Integer themeId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        List<TagSimpleResponseVo> res = tagService.selectSimpleList(themeId, currentUser.getId());
        response.setData(res);
        return response;
    }

    @ApiOperation("增加节点")
    @PostMapping("/add")
    public ResponseEntity addTag(@RequestBody @Validated TagAddVo addVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        int tagId = tagService.addTag(addVo, currentUser.getId());
        response.setData(tagId);
        return response;
    }

    @ApiOperation("修改节点名称")
    @PostMapping("/rename")
    public ResponseEntity renameTag(@RequestBody @Validated TagRenameVo renameVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        tagService.renameTag(renameVo, currentUser.getId());
        response.setData("修改节点名成功");
        return response;
    }

    @ApiOperation("删除节点")
    @PostMapping("/remove")
    public ResponseEntity removeTag(@RequestBody @Validated TagRemoveOrRePositionVo removeVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        tagService.removeTag(removeVo, currentUser);
        response.setData("删除成功");
        return response;
    }

    @ApiOperation("彻底删除节点")
    @DeleteMapping("/delete")
    public ResponseEntity deleteTag(@RequestParam(value = "tagId") Integer tagId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        tagService.deleteTag(tagId, currentUser.getId());
        response.setData("已彻底删除节点！");
        return response;
    }

    @ApiOperation("节点左右变换")
    @PostMapping("/changePosition")
    public ResponseEntity changePosition(@RequestBody @Validated TagRemoveOrRePositionVo rePositionVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        tagService.changePosition(rePositionVo, currentUser.getId());
        response.setData("改变左右位置成功");
        return response;
    }

    @ApiOperation("改变节点同层顺序")
    @PostMapping("/changeOrder")
    public ResponseEntity reorderTag(@RequestBody @Validated TagReOrderVo reOrderVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        tagService.changeOrder(reOrderVo, currentUser.getId());
        response.setData("改变顺序成功");
        return response;
    }

    @ApiOperation("移动节点改变父节点")
    @PostMapping("/reparent")
    public ResponseEntity reparentTag(@RequestBody @Validated TagReparentVo reparentVo) {
       ResponseEntity response = new ResponseEntity();
       User currentUser = getCurrentUser();
       tagService.reparentTag(reparentVo, currentUser.getId());
       response.setData("改变位置成功");
       return response;
    }

    @ApiOperation("从回收站恢复节点")
    @GetMapping("/recover")
    public ResponseEntity recoverTag(@RequestParam("tagId") Integer tagId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        tagService.recoverTag(tagId, currentUser.getId());
        response.setData("恢复成功");
        return response;
    }
}
