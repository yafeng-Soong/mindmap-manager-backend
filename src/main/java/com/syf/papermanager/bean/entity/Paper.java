package com.syf.papermanager.bean.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.entity
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/14 21:28
 */
@Data
//@NoArgsConstructor
public class Paper {
    /**
     * 自增主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 论文名称
     */
    private String name;

    /**
     * 论文作者
     */
    private String author;

    /**
     * 上传论文的用户id
     */
    private Integer uploaderId;

    /**
     * 论文关键字
     */
    private String keyword;

    /**
     * 论文总结
     */
    private String summary;

    /***
     * 论文摘要
     */
    private String abstracts;

    /***
     * 发表年份
     */
    private Integer publishYear;

    /**
     * 文件存储路径
     */
    private String filePath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
//    public Paper(String name, String author, Integer uploaderId, String keyword
//            , String summary, String filePath, Date createTime){
//        this.name = name;
//        this.author = author;
//        this.uploaderId = uploaderId;
//        this.keyword = keyword;
//        this.summary = summary;
//        this.filePath = filePath;
//        this.createTime = createTime;
//        this.updateTime = new Date();
//
//    }

}
