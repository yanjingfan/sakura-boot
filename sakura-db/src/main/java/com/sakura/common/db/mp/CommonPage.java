package com.sakura.common.db.mp;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther YangFan
 * @Date 2020/12/23 17:31
 *
 * MP分页处理
 */
public class CommonPage<T> {

    private Long page;
    private Long pageSize;
    private Long totalPage;
    private Long total;
    private List<T> list;

    /**
     * 将MP自带分页数据，转成自定义的
     */
    public static <T> CommonPage<T> restPage(IPage<T> pageInfo) {
        //if (pageInfo != null && pageInfo.getTotal() != 0) {
        CommonPage<T> result = new CommonPage<T>();
        result.setTotalPage(pageInfo.getPages());
        result.setPage(pageInfo.getCurrent());
        result.setPageSize(pageInfo.getSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getRecords());
        return result;
        //}
        //return null;
    }

    /**
     * 逻辑分页
     * @param list
     * @param page
     * @param size
     * @param <T>
     * @return
     */
    public static <T> CommonPage<T> restPage(List<T> list, long page, long size) {

        CommonPage<T> result = new CommonPage<>();
        int total = list.size();
        result.setTotal((long)total);
        List<T> tList = list.stream().skip((page - 1) * size).limit(size).collect(Collectors.toList());
        result.setPage(page);
        result.setPageSize(size);
        long totalPages = (total + size - 1) / size;
        result.setTotalPage(totalPages);
        result.setList(tList);
        return result;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
