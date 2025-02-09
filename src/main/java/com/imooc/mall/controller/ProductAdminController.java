package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 后台商品管理
 */
@Controller
public class ProductAdminController {
    @Autowired
    ProductService productService;

    /**
     * 后台添加商品
     *
     * @param session       保存登录信息
     * @param addProductReq 装新增商品信息的对象
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台添加商品")
    @PostMapping("/admin/product/add")
    @ResponseBody
    public ApiRestResponse addProduct(HttpSession session, @Valid @RequestBody AddProductReq addProductReq) throws ImoocMallException {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

}
