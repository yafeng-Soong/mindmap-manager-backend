package com.syf.papermanager.bean.vo.user;

import lombok.Data;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.user
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/31 20:11
 */
@Data
public class MemberResponseVo {
    private UserResponseVo creator;
    private List<UserResponseVo> members;
}
