package com.imooc.mall.model.request;

/**
 * 用于接收 Controller 中接受前端请求查询信息
 * 前台查找商品列表的请求参数集合
 * 内部的属性是查询时的一些参数，包括：关键词、商品分类id、结果排序规则 和 分页参数
 */
public class ProductListReq {

    private String keyword; // 商品名的关键词

    private Integer categoryId; // 商品分类id

    private String orderBy; // 结果排序规则

    private Integer pageNum = 1; // 分页中要获取的页码（设默认值）

    private Integer pageSize = 10; // 分页中每页的记录数（设默认值）

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "ProductListReq{" +
                "keyword='" + keyword + '\'' +
                ", categoryId=" + categoryId +
                ", orderBy='" + orderBy + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}