package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
