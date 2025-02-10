package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前台商品操作
 */
@Controller
public class ProductController {
    @Autowired
    ProductService productService;

    /**
     * 获取指定id的商品详情
     *
     * @param id 待查找商品的id
     * @return ApiRestResponse对象
     */
    @ApiOperation("获取某个商品详情")
    @GetMapping("/product/detail")
    @ResponseBody
    public ApiRestResponse detail(@RequestParam Integer id) {
        // 获取指定id的商品的详情
        Product product = productService.detail(id);

        return ApiRestResponse.success(product);
    }
}
