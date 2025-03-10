package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 后台订单接口
 */
@Controller
public class OrderAdminController {
    @Autowired
    OrderService orderService;

    /**
     * 后台获取订单列表
     *
     * @param pageNum  分页：当前页码
     * @param pageSize 分页：每页数量大小
     * @return 分页对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台获取订单列表")
    @GetMapping("/admin/order/list")
    @ResponseBody
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) throws ImoocMallException {
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    /**
     * 后台订单发货
     * 0：用户已取消，10：未付款（初始状态），20：已付款，30：已发货，40：交易完成
     *
     * @param orderNo 订单号
     * @return 无
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台订单发货")
    @PostMapping("/admin/order/deliver")
    @ResponseBody
    public ApiRestResponse deliver(@RequestParam String orderNo) throws ImoocMallException {
        orderService.deliver(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 完结订单
     * 管理员和用户都可调用
     * 0：用户已取消，10：未付款（初始状态），20：已付款，30：已发货，40：交易完成
     *
     * @param orderNo 订单号
     * @return 无
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("完结订单")
    @PostMapping("/order/finish")
    @ResponseBody
    public ApiRestResponse finish(@RequestParam String orderNo) throws ImoocMallException {
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }
}
