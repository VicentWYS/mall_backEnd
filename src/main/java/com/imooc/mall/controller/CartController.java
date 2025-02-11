package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.filter.UserFilter;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.model.vo.CartVO;
import com.imooc.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * 添加商品到购物车
     * 前提：用户登录
     *
     * @param productId （待添加）商品id
     * @param count     （待添加）商品数量
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("添加商品到购物车")
    @PostMapping("/cart/add")
    @ResponseBody
    public ApiRestResponse add(@RequestParam("productId") Integer productId, @RequestParam("count") Integer count) throws ImoocMallException {
        // 验证用户登录（UserFilter）
        int userId = UserFilter.currentUser.getId(); // 从过滤器中获取登录用户信息

        // 为指定购物车尝试添加商品
        List<CartVO> cartVOS = cartService.add(userId, productId, count);

        return ApiRestResponse.success();
    }
}
