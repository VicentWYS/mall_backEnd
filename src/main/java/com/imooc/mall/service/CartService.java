package com.imooc.mall.service;

import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.vo.CartVO;

import java.util.List;

/**
 * 购物车Service
 */
public interface CartService {

    List<CartVO> list(Integer userId);

    List<CartVO> add(Integer userId, Integer productId, Integer count) throws ImoocMallException;

    List<CartVO> update(Integer userId, Integer productId, Integer count) throws ImoocMallException;
}
