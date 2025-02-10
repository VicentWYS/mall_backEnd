package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.model.pojo.Category;
import com.imooc.mall.model.request.AddCategoryReq;
import com.imooc.mall.model.request.UpdateCategoryReq;
import com.imooc.mall.model.vo.CategoryVO;
import com.imooc.mall.service.CategoryService;
import com.imooc.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;

    /**
     * 新增商品分类
     * 前提：需管理员登录
     *
     * @param session        保存登录信息
     * @param addCategoryReq 包装新增商品分类信息的对象
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台添加商品分类")
    @PostMapping("/admin/category/add")
    @ResponseBody
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq) throws ImoocMallException {
        categoryService.add(addCategoryReq); // 插入数据
        return ApiRestResponse.success();
    }

    /**
     * 更新商品分类
     *
     * @param session           保存登录信息
     * @param updateCategoryReq 包装更新商品分类信息的对象（包含id）
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台更新商品分类")
    @PostMapping("/admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(HttpSession session, @Valid @RequestBody UpdateCategoryReq updateCategoryReq) throws ImoocMallException {
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        categoryService.update(category); // 更新数据
        return ApiRestResponse.success();
    }

    /**
     * 删除商品分类
     *
     * @param id 商品类id
     * @return ApiRestResponse对象
     */
    @ApiOperation("后台删除商品分类")
    @PostMapping("/admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id) throws ImoocMallException {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    /**
     * 获取后台目录列表（给管理员）
     *
     * @param pageNum  本次调用要获取的页码（只拿这一页的内容）
     * @param pageSize 每页显示记录数
     * @return ApiRestResponse对象
     */
    @ApiOperation("获取后台目录列表（给管理员）")
    @PostMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        // 获取分页结果
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo); // 作为返回对象中的data
    }

    /**
     * 获取前台目录列表（给用户）
     *
     * @return ApiRestResponse对象
     */
    @ApiOperation("获取前台目录列表（给用户）")
    @PostMapping("/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer() {
        // 获取分页结果（给前台返回所有目录）
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);

        return ApiRestResponse.success(categoryVOS); // 作为返回对象中的data
    }
}
