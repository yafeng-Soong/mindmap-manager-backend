package com.syf.papermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.entity.ThemeMember;
import com.syf.papermanager.bean.vo.theme.ThemeResponseVo;
import com.syf.papermanager.bean.vo.user.UserResponseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.mapper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/29 19:25
 */
public interface ThemeMemberMapper extends BaseMapper<ThemeMember> {
    List<Integer> selectThemeIds(@Param("userId") Integer userId);
    IPage<ThemeResponseVo> selectByUserId(Page page, @Param("userId") Integer userId);
    List<Theme> selectThemeList(@Param("userId") Integer userId, @Param("selfId") Integer selfId);
    List<UserResponseVo> selectMembers(@Param("themeId") Integer themeId);
}
