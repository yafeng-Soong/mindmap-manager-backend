package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.vo.tag.*;
import com.syf.papermanager.bean.entity.Tag;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:06
 */
public interface TagService extends IService<Tag> {
    List<TagTreeResponseVo> selectTreeByThemeId(Integer themeId);
    List<TagSimpleResponseVo> selectSimpleList(Integer themeId);
    List<TagSimpleResponseVo> selectRemovedList(Integer themeId);
    int addTag(TagAddVo addVo, Integer userId);
    int renameTag(TagRenameVo renameVo, Integer userId);
    int removeTag(TagRemoveOrRePositionVo removeVo, User user);
    int changePosition(TagRemoveOrRePositionVo rePositionVo, Integer userId);
    int changeOrder(TagReOrderVo reOrderVo, Integer userId);
    int reparentTag(TagReparentVo reparentVo, Integer userId);
}
