package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;

/**
 * 商品Service
 */
public interface ProductService {

    // 添加商品
    void add(AddProductReq addProductReq) throws ImoocMallException;

    // 更新商品
    void update(Product updateProduct) throws ImoocMallException;

    void delete(Integer id) throws ImoocMallException;

    // 批量上下架商品
    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);
}
