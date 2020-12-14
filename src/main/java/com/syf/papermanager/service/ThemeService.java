package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.Theme;
import com.syf.papermanager.bean.vo.operation.OperationQueryVo;
import com.syf.papermanager.bean.vo.tag.request.TagRemovedQueryVo;
import com.syf.papermanager.bean.vo.tag.response.TagOperationVo;
import com.syf.papermanager.bean.vo.tag.response.TagRemovedVo;
import com.syf.papermanager.bean.vo.theme.ThemeAddVo;
import com.syf.papermanager.bean.vo.theme.ThemeCombineVo;
import com.syf.papermanager.bean.vo.theme.ThemeQueryVo;
import com.syf.papermanager.bean.vo.theme.ThemeUpdateVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/15 23:16
 */
public interface ThemeService extends IService<Theme> {
    List<Theme> selectList(Integer creatorId, Integer selfId);
    Page<Theme> selectPageList(ThemeQueryVo queryVo, Integer userId);
    Page<TagOperationVo> selectOperations(OperationQueryVo queryVo, Integer userId);
    Page<TagRemovedVo> selectRemovedTagList(TagRemovedQueryVo queryVo, Integer userId);
    int addTheme(ThemeAddVo addVo, Integer userId);
    int updateTheme(ThemeUpdateVo updateVo, Integer userId);
    int deleteTheme(Integer themeId, Integer userId);
    int createFromXmind(MultipartFile file, String themeName, String description,Integer userId) throws RuntimeException;
    int updateThemeState(Integer themeId, Integer userId, Integer stateCode);
    void combineTheme(ThemeCombineVo combineVo, Integer userId);
}
