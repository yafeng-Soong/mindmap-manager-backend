package com.syf.papermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syf.papermanager.bean.entity.Paper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.mapper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/18 11:12
 */
public interface PaperMapper extends BaseMapper<Paper> {
    List<Integer> selectOperators(@Param("paperId") Integer paperId);
    Integer selectAssociatedTagNumber(@Param("paperId") Integer paperId);
}
