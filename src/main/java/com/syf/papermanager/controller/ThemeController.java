package com.syf.papermanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.page.PageResponseVo;
import com.syf.papermanager.bean.vo.theme.ThemeAddVo;
import com.syf.papermanager.bean.vo.theme.ThemeQueryVo;
import com.syf.papermanager.bean.vo.theme.ThemeResponseVo;
import com.syf.papermanager.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity getList() {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        List<Theme> list = themeService.selectList(currentUser.getId());
        List<ThemeResponseVo> res = list.stream()
                .map(source -> {
                    ThemeResponseVo target = new ThemeResponseVo();
                    BeanUtils.copyProperties(source, target);
                    target.setCreator(currentUser.getUsername());
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

    @ApiOperation("新增脑图接口")
    @PutMapping("/add")
    public ResponseEntity addTheme(@RequestBody @Validated ThemeAddVo addVo) {
        ResponseEntity response = new ResponseEntity();
        User currentUser = getCurrentUser();
        themeService.addTheme(addVo, currentUser.getId());
        response.setData("新增脑图成功");
        return response;
    }
}
