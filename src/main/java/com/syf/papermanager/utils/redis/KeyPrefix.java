package com.syf.papermanager.utils.redis;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.utils.redis
 * @description: redis键值对前缀
 * @author: songyafeng
 * @create_time: 2020/12/15 16:33
 */
public interface KeyPrefix {
    /**
     * getExpireSecond
     * @description 返回键值对得存活时间
     * @return int
     * @date 2019/7/7 19:12
     * @version 1.0.0
     */
    int expireSeconds();

    /**
     * getPrefix
     * @description 得到键值得前缀
     * @return java.lang.String
     * @date 2019/7/7 18:43
     * @version 1.0.0
     */
    String prefix();
}
