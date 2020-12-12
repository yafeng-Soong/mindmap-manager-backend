package com.syf.papermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syf.papermanager.bean.entity.PaperTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.mapper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/18 11:45
 */
public interface PaperTagMapper extends BaseMapper<PaperTag> {
    List<PaperTag> selectByTagId(@Param("tagId") Integer tagId);
}
