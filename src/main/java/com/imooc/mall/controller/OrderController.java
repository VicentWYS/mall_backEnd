package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {
    @Autowired
    OrderService orderService;

    /**
     * 用户创建订单
     *
     * @param createOrderReq 前台传入参数对象（收货人、收货人手机号、收货地址、邮费、支付方式）
     * @return 订单号
     */
    @ApiOperation("用户创建订单")
    @PostMapping("/order/create")
    @ResponseBody
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq) throws ImoocMallException {
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    /**
     * 获取订单及订单详情
     *
     * @param orderNo 订单号
     * @return 订单及订单详情对象orderVO
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("获取订单及订单详情")
    @GetMapping("/order/detail")
    @ResponseBody
    public ApiRestResponse detail(@RequestParam String orderNo) throws ImoocMallException {
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }
}
