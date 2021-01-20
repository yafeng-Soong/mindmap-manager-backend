package com.syf.papermanager.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.enums.ThemeState;
import com.syf.papermanager.bean.vo.operation.OperationQueryVo;
import com.syf.papermanager.bean.vo.page.PageQueryVo;
import com.syf.papermanager.bean.vo.page.PageResponseVo;
import com.syf.papermanager.bean.vo.tag.request.TagRemovedQueryVo;
import com.syf.papermanager.bean.vo.tag.response.TagOperationVo;
import com.syf.papermanager.bean.vo.tag.response.TagRemovedVo;
import com.syf.papermanager.bean.vo.theme.*;
import com.syf.papermanager.bean.vo.user.MemberResponseVo;
import com.syf.papermanager.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.controller
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 23:19
 */
@Api(tags = "脑图相关功能的接口")
@Slf4j
@RestController
@RequestMapping("/theme")
public class ThemeController extends BaseController {
    @Resource
    ThemeService themeService;

    @ApiOperation("查询测试接口")
    @GetMapping("/getList")
    public ResponseEntity getList(@RequestParam(value = "selfId", required = false) Integer selfId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        List<Theme> list = themeService.selectList(currentUser.getId(), selfId);
        List<ThemeResponseVo> res = list.stream()
                .map(source -> {
                    ThemeResponseVo target = new ThemeResponseVo(source);
                    return target;
                }).collect(Collectors.toList());
        response.setData(res);
        return response;
    }

    @ApiOperation("查询分页数据")
    @PostMapping("/getPageList")
    public ResponseEntity getPageList(@RequestBody @Validated ThemeQueryVo queryVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        Page<Theme> res = themeService.selectPageList(queryVo, currentUser.getId());
        List<ThemeResponseVo> list = res.getRecords().stream()
                .map(source -> {
                    ThemeResponseVo target = new ThemeResponseVo();
                    BeanUtils.copyProperties(source, target);
                    target.setCreator(currentUser.getUsername());
                    return target;
                }).collect(Collectors.toList());
        PageResponseVo<ThemeResponseVo> data = new PageResponseVo<>(res, list);
        response.setData(data);
        return response;
    }

    @ApiOperation("查询团队脑图")
    @PostMapping("/getInvitedList")
    public ResponseEntity getInvitedPageList(@RequestBody @Validated PageQueryVo queryVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        IPage<ThemeResponseVo> res = themeService.selectInvitedList(queryVo, currentUser.getId());
        PageResponseVo<ThemeResponseVo> data = new PageResponseVo<>(res);
        response.setData(data);
        return response;
    }

    @ApiOperation("获取脑图操作记录")
    @PostMapping("/getOperations")
    public ResponseEntity getRecentOperations(@RequestBody @Validated OperationQueryVo queryVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        Page<TagOperationVo> res = themeService.selectOperations(queryVo, currentUser.getId());
        PageResponseVo<TagOperationVo> data = new PageResponseVo<>(res);
        response.setData(data);
        return response;
    }

    @ApiOperation("获取一个脑图中被删除的节点列表")
    @PostMapping("/getRemovedTagList")
    public ResponseEntity getRemovedList(@RequestBody @Validated TagRemovedQueryVo queryVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        Page<TagRemovedVo> res = themeService.selectRemovedTagList(queryVo, currentUser.getId());
        PageResponseVo<TagRemovedVo> data = new PageResponseVo<>(res);
        response.setData(data);
        return response;
    }

    @ApiOperation("获取一个脑图的创建者和成员")
    @GetMapping("/getMembers")
    public ResponseEntity getMembers(@RequestParam("themeId") Integer themeId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        MemberResponseVo data = themeService.selectMembers(themeId, currentUser.getId());
        response.setData(data);
        return response;
    }

    @ApiOperation("新增脑图接口")
    @PutMapping("/add")
    public ResponseEntity addTheme(@RequestBody @Validated ThemeAddVo addVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.addTheme(addVo, currentUser.getId());
        response.setData("新增脑图成功");
        return response;
    }

    @ApiOperation("更新脑图信息接口")
    @PostMapping("/update")
    public ResponseEntity updateTheme(@RequestBody @Validated ThemeUpdateVo updateVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.updateTheme(updateVo, currentUser.getId());
        response.setData("更新脑图信息成功");
        return response;
    }

    @ApiOperation("更新文件接口")
    @ApiImplicitParam(name = "file", value = "文件", required = true)
    @PostMapping("/importXmindFile")
    public ResponseEntity importXmind(@RequestParam(value = "file") MultipartFile file,
                                      @RequestParam(value = "name") String themeName,
                                      @RequestParam(value = "description", required = false) String description) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.createFromXmind(file, themeName, description,currentUser.getId());
        response.setData("导入成功！");
        return response;
    }

    @ApiOperation("删除脑图接口（到回收站）")
    @DeleteMapping("/remove")
    public ResponseEntity removeTheme(@RequestParam(value = "themeId") Integer themeId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        Integer removeCode = ThemeState.REMOVED.getCode();
        themeService.updateThemeState(themeId, currentUser.getId(), removeCode);
        response.setData("删除成功!");
        return response;
    }

    @ApiOperation("恢复回收站中的脑图")
    @GetMapping("/recover")
    public ResponseEntity recoverTheme(@RequestParam(value = "themeId") Integer themeId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        Integer normalCode = ThemeState.NORMAL.getCode();
        themeService.updateThemeState(themeId, currentUser.getId(), normalCode);
        response.setData("恢复成功!");
        return response;
    }

    @ApiOperation("合并脑图")
    @PostMapping("/combine")
    public ResponseEntity combineTheme(@RequestBody @Validated ThemeCombineVo combineVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.combineTheme(combineVo, currentUser.getId());
        response.setData("合并成功！");
        return response;
    }

    @ApiOperation("删除脑图")
    @DeleteMapping("/delete")
    public ResponseEntity deleteTheme(@RequestParam(value = "themeId") Integer themeId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.deleteTheme(themeId, currentUser.getId());
        response.setData("删除成功！");
        return response;
    }

    @ApiOperation("邀请成员")
    @PostMapping("/invite")
    public ResponseEntity invite(@RequestBody @Validated ThemeInviteVo inviteVo) throws Exception {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.inviteMember(inviteVo, currentUser);
        response.setData("邀请成功！");
        return response;
    }

    @ApiOperation("退出脑图")
    @GetMapping("/exit")
    public ResponseEntity exit(@RequestParam("themeId") Integer themeId) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.exitTheme(themeId, currentUser.getId());
        response.setData("退出成功");
        return response;
    }

    @ApiOperation("踢出成员")
    @PostMapping("/kickOff")
    public ResponseEntity kickOff(@RequestBody @Validated ThemeKickOffVo kickVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.kickOffMember(kickVo, currentUser.getId());
        response.setData("成功踢出成员");
        return response;
    }
}
