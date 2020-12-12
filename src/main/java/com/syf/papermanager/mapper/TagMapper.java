package com.syf.papermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syf.papermanager.bean.entity.Tag;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.mapper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:05
 */
public interface TagMapper extends BaseMapper<Tag> {
    Integer selectMaxOrder(Integer fatherId);
    List<Integer> selectChildrenIds(Integer fatherId);
}
