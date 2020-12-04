package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.entity.User;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.bean.vo.tag.request.*;
import com.syf.papermanager.bean.vo.tag.response.TagOperationVo;
import com.syf.papermanager.bean.vo.tag.response.TagRemovedVo;
import com.syf.papermanager.bean.vo.tag.response.TagSimpleResponseVo;
import com.syf.papermanager.bean.vo.tag.response.TagTreeResponseVo;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:06
 */
public interface TagService extends IService<Tag> {
    // TODO 最好所有方法都加上userId参数
    List<TagTreeResponseVo> selectTreeByThemeId(Integer themeId);
    List<TagSimpleResponseVo> selectSimpleList(Integer themeId);
    List<TagRemovedVo> selectRemovedList(Integer themeId);
    List<TagOperationVo> selectOperations(Integer themeId);
    int addTag(TagAddVo addVo, Integer userId);
    int renameTag(TagRenameVo renameVo, Integer userId);
    int removeTag(TagRemoveOrRePositionVo removeVo, User user);
    int changePosition(TagRemoveOrRePositionVo rePositionVo, Integer userId);
    int changeOrder(TagReOrderVo reOrderVo, Integer userId);
    int reparentTag(TagReparentVo reparentVo, Integer userId);
    int recoverTag(Integer tagId, Integer userId);
}
