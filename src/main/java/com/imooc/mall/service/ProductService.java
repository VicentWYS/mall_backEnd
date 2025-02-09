package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.AddProductReq;

/**
 * 商品Service
 */
public interface ProductService {

    // 添加商品
    void add(AddProductReq addProductReq) throws ImoocMallException;
}
