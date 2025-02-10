package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.ProductListReq;
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

    /**
     * 前台获取商品列表
     * 返回分页结果，
     * 注意这里暗含的意思是用户视角，
     * 用户选择的有可能是某个父商品类的分类id，
     * 因此查询逻辑中需找出参数的分类id及其所有级别的子类id
     *
     * @param productListReq 查询规则集合对象（用户视角）
     * @return ApiRestResponse对象
     */
    @ApiOperation("前台获取商品列表")
    @GetMapping("/product/list")
    @ResponseBody
    public ApiRestResponse list(ProductListReq productListReq) {
        // 根据查询规则，查询商品
        PageInfo pageInfo = productService.list(productListReq);

        return ApiRestResponse.success(pageInfo);
    }

}
