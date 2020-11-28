package com.syf.papermanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syf.papermanager.bean.vo.tag.TagAddVo;
import com.syf.papermanager.bean.vo.tag.TagRenameVo;
import com.syf.papermanager.bean.vo.tag.TagResponseVo;
import com.syf.papermanager.bean.entity.Tag;
import com.syf.papermanager.bean.vo.tag.TagSimpleResponseVo;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.service
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:06
 */
public interface TagService extends IService<Tag> {
    List<TagResponseVo> selectTreeByThemeId(Integer themeId);
    List<TagSimpleResponseVo> selectSimpleList(Integer themeId);
    int addTag(TagAddVo addVo, Integer userId);
    int renameTag(TagRenameVo renameVo);
}
