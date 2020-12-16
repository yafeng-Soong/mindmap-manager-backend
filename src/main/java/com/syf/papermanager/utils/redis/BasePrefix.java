package com.syf.papermanager.utils.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.base
 * @description:
 * @author: songyafeng
 * @create_time: 2020/12/15 16:35
 */
@Data
@AllArgsConstructor
public abstract class BasePrefix implements KeyPrefix {

    // 设置存活期为一天
    private static final Integer EXPIRETIME = 3600 * 24;

    // 键值对期待的存活期间
    private Integer expireSeconds;

    // 给键值对加上特定的前缀 防止错误覆盖
    private String prefix;

    public BasePrefix(String prefix){
        this(EXPIRETIME, prefix);
    }

    public BasePrefix(){
        this.expireSeconds = 0;
        this.prefix = "base";
    }

    public int expireSeconds(){
        return expireSeconds;
    }

    public String prefix(){
        String className = this.getClass().getSimpleName();
        return className + ":" + prefix;
    }
}