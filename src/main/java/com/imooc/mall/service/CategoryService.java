package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;

public interface CategoryService {

    void add(AddCategoryReq addCategoryReq) throws ImoocMallException;

    void update(Category updateCategory) throws ImoocMallException;

    void delete(Integer id) throws ImoocMallException;

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);
}
