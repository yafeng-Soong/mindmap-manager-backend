package com.syf.papermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syf.papermanager.bean.dto.TagOperationDTO;
import com.syf.papermanager.bean.entity.ThemeOperation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.mapper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/28 20:35
 */
public interface ThemeOperationMapper extends BaseMapper<ThemeOperation> {
    IPage<TagOperationDTO> selectRemovedTag(Page page, @Param("themeId") Integer themeId, @Param("state") Integer state);
    IPage<TagOperationDTO> selectOperations(Page page, @Param("themeId") Integer themeId);
    int deleteByTagId(@Param("tagId") Integer tagId);
}
