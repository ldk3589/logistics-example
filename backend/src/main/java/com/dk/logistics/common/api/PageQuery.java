package com.dk.logistics.common.api;

public class PageQuery {
    private Long pageNum = 1L;
    private Long pageSize = 10L;

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum == null || pageNum < 1 ? 1L : pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize == null || pageSize < 1 ? 10L : pageSize;
    }
}