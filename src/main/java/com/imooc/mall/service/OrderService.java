package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.model.vo.OrderVO;

import java.io.IOException;
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

    // 用户取消订单
    void cancel(String orderNo) throws ImoocMallException;

    // 生成订单支付的二维码图片
    String qrcode(String orderNo) throws ImoocMallException, IOException, WriterException;

    // 后台获取订单列表
    PageInfo listForAdmin(Integer pageNum, Integer pageSize) throws ImoocMallException;

    // 支付订单
    void pay(String orderNo) throws ImoocMallException;

    // 后台订单发货
    void deliver(String orderNo) throws ImoocMallException;

    // 完结订单
    void finish(String orderNo) throws ImoocMallException;
}
