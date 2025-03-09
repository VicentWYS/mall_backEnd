package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.model.vo.OrderVO;

import java.util.List;

/**
 * 订单Service
 */
public interface OrderService {

    // 生成订单
    String create(CreateOrderReq createOrderReq) throws ImoocMallException;

    // 获取订单及订单详情
    OrderVO detail(String orderNo) throws ImoocMallException;

    // 获取指定用户的全部订单
    PageInfo listForCustomer(Integer pageNum, Integer pageSize) throws ImoocMallException;
}
