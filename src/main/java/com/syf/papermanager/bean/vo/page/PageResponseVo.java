package com.syf.papermanager.bean.vo.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.bean.vo.page
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/24 21:06
 */
@Data
@ApiModel
public class PageResponseVo<T> {
    @ApiModelProperty(value = "当前页数")
    private long currentPage;
    @ApiModelProperty(value = "分页大小")
    private long pageSize;
    @ApiModelProperty(value = "数据总条数")
    private long total;
    @ApiModelProperty(value = "总页数，约等于total/pageSize")
    private long pages;
    @ApiModelProperty(value = "实际数据")
    private List<T> data;

    public PageResponseVo(Page page){
        this.data = page.getRecords();
        currentPage = page.getCurrent();
        pageSize = page.getSize();
        total = page.getTotal();
        pages = page.getPages();
    }

    public PageResponseVo(Page page, List<T> data) {
        this.data = data;
        currentPage = page.getCurrent();
        pageSize = page.getSize();
        total = page.getTotal();
        pages = page.getPages();
    }

    public PageResponseVo(IPage page) {
        this.data = page.getRecords();
        currentPage = page.getCurrent();
        pageSize = page.getSize();
        total = page.getTotal();
        pages = page.getPages();
    }
}