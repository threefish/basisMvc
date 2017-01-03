package com.sgaop.basis.dao;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/7/1 0001
 * To change this template use File | Settings | File Templates.
 */
public class Pager {

    /**
     * 默认每页条数
     */
    private final static int def_pageSize = 10;

    /**
     * 每页条数
     */
    private int pageSize = 0;

    /**
     * 起始数
     */
    private int pageStart = 0;

    /**
     * 结束数
     */
    private int pageEnd = 0;

    /**
     * @param pageNumber 当前页码
     * @param pageSize   每页条数
     */
    public Pager(int pageNumber, int pageSize) {
        this.compute(pageNumber, pageSize);
        if (this.getPageEnd() - this.getPageStart() > 500) {
            throw new RuntimeException("请检查前端分页数据大小，分页数据过多可能导致检索变慢！");
        }
    }

    /**
     * @param pageNumber 当前页码
     */
    public Pager(int pageNumber) {
        this.compute(pageNumber, def_pageSize);
    }

    private void compute(int pageNumber, int pageSize) {
        if (pageSize == 0) {
            pageSize = def_pageSize;
        }
        this.pageSize = pageSize;
        if (pageNumber == 0 || pageNumber == 1) {
            this.pageStart = 0;
            this.pageEnd = pageSize;
        } else {
            pageNumber = pageNumber - 1;
            this.pageStart = pageNumber * pageSize;
            this.pageEnd = this.pageStart + pageSize;
        }
    }

    public int getPageStart() {
        return pageStart;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public int getPageEnd() {
        return pageEnd;
    }

    public void setPageEnd(int pageEnd) {
        this.pageEnd = pageEnd;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
