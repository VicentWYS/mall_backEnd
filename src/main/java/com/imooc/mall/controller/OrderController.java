package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.CreateOrderReq;
import com.imooc.mall.model.vo.OrderVO;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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

    /**
     * 获取订单列表
     *
     * @param pageNum  分页：当前页码
     * @param pageSize 分页：每页显示的记录数
     * @return 分页对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("获取订单列表")
    @GetMapping("/order/list")
    @ResponseBody
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) throws ImoocMallException {
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);

        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 用户取消订单
     * 只是改变订单状态，不删除记录
     *
     * @param orderNo 待取消的订单号
     * @return 空
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("用户取消订单")
    @PostMapping("/order/cancel")
    @ResponseBody
    public ApiRestResponse cancel(@RequestParam String orderNo) throws ImoocMallException {
        orderService.cancel(orderNo);

        return ApiRestResponse.success();
    }

    /**
     * 生成支付二维码
     *
     * @param orderNo 订单号
     * @return 二维码图片在项目中的地址
     */
    @ApiOperation("生成支付二维码")
    @PostMapping("/order/qrcode")
    @ResponseBody
    public ApiRestResponse qrcode(@RequestParam String orderNo) throws ImoocMallException, IOException, WriterException {
        String qrcodeAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(qrcodeAddress);
    }

    /**
     * 支付订单
     *
     * @param orderNo 订单号
     * @return 无
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("支付订单")
    @PostMapping("/pay")
    @ResponseBody
    public ApiRestResponse pay(@RequestParam String orderNo) throws ImoocMallException {
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }
}
