package com.bknife.orm;

import java.util.Collection;

import com.bknife.base.util.Jsons;

/**
 * 分页结果
 */
public class PageResult<T> {
    private int current = 1;
    private int pageSize = 15;
    private int total = 0;
    private Collection<T> records;

    public PageResult(int current, int pageSize, int total, Collection<T> records) {
        this.current = current;
        this.pageSize = pageSize;
        this.total = total;
        this.records = records;
    }

    /**
     * 获取当前页，从1开始
     * 
     * @return
     */
    public int getCurrent() {
        return current;
    }

    /**
     * 获取分页尺寸
     * 
     * @return
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 获取总条数
     * 
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * 获取行偏移
     * 
     * @return
     */
    public int getOffset() {
        int currentPage = getCurrent();
        return (currentPage == 0 ? currentPage : currentPage - 1) * getPageSize();
    }

    /**
     * 获取记录列表
     * 
     * @return
     */
    public Collection<T> getRecords() {
        return records;
    }

    @Override
    public String toString() {
        return Jsons.toJson(this);
    }
}
