package com.imooc.mall.model.query;

import java.util.List;

/**
 * 查询商品列表的具体参数，用于对接 mapper 中的 SQL 查询
 */
public class ProductListQuery {
    private String keyword; // 关键词

    private List<Integer> categoryIds; // 用户选择的商品分类id以及其所有的子孙id

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
