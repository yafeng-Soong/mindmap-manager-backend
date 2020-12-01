package com.syf.papermanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.syf.papermanager.bean.entity.Tag;
import org.apache.ibatis.annotations.Select;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.mapper
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/17 0:05
 */
public interface TagMapper extends BaseMapper<Tag> {
    @Select("SELECT MAX(inner_order) FROM tag WHERE father_id = #{fatherId}")
    Integer selectMaxOrder(Integer fatherId);
}
