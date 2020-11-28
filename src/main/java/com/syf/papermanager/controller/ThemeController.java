package com.syf.papermanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.syf.papermanager.base.BaseController;
import com.syf.papermanager.bean.entity.ResponseEntity;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.theme.ThemeResponseVo;
import com.syf.papermanager.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
