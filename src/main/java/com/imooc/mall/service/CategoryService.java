package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.AddCategoryReq;

public interface CategoryService {

    void add(AddCategoryReq addCategoryReq) throws ImoocMallException;
}
