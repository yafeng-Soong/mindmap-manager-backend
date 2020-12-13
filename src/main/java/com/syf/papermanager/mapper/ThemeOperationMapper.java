package com.syf.papermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
    List<TagOperationDTO> selectRemovedTag(@Param("themeId") Integer themeId, @Param("state") Integer state);
    List<TagOperationDTO> selectOperations(@Param("themeId") Integer themeId);
    int deleteByTagId(@Param("tagId") Integer tagId);
}
