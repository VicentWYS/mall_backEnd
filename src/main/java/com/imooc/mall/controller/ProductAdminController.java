package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.Product;
import com.imooc.mall.model.request.AddProductReq;
import com.imooc.mall.model.request.UpdateProductReq;
import com.imooc.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

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
     * @param addProductReq 装新增商品信息的对象
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台添加商品")
    @PostMapping("/admin/product/add")
    @ResponseBody
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) throws ImoocMallException {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     * 后台上传文件
     *
     * @param httpServletRequest 客户端的HTTP请求
     * @param file               代表了客户端上传的文件
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台上传文件")
    @PostMapping("admin/upload/file")
    @ResponseBody
    public ApiRestResponse uploadProduct(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws ImoocMallException {
        // 获取文件后缀（如图片：.jpg）
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        // 生成新文件名：UUID + 后缀
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;

        // 创建目录及文件
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR); // 创建目录对象

        // 检测目录是否存在
        if (!fileDirectory.exists()) { // 若目录不存在
            // 新建目录
            if (!fileDirectory.mkdir()) { // 若创建失败
                throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAILED);
            }
        }

        // 写入文件
        // File destFile = new File(fileDirectory + "\\" + newFileName); // 【Windows开发环境】创建目标文件对象
        File destFile = new File(fileDirectory + "/" + newFileName); // 【线上Linux环境】创建目标文件对象

        // System.out.println("上传图片的地址：" + destFile.getPath());

        try {
            file.transferTo(destFile); // 将上传的文件内容写入到目标文件中
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 生成文件的访问路径并返回
        try {
            // 从请求的 URL 中提取出主机信息（协议、主机名和端口），然后拼接出文件的完整访问路径
            // 这里的"/images/"只是自定义这么写，后面会从配置类中规定它与静态资源目录的映射关系
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/" + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     * 从请求的 URL 中提取出主机信息（协议、主机名和端口）
     * 忽略路径、查询参数和片段
     *
     * @param uri
     * @return
     */
    private URI getHost(URI uri) {
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
        }
        return effectiveURI;
    }

    /**
     * 后台更新商品
     *
     * @param updateProductReq 装待更新商品信息的对象
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台更新商品")
    @PostMapping("/admin/product/update")
    @ResponseBody
    public ApiRestResponse updateProduct(@Valid @RequestBody UpdateProductReq updateProductReq) throws ImoocMallException {
        // 包装成商品类（便于mapper中操作）
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);

        // 更新记录
        productService.update(product);

        return ApiRestResponse.success();
    }

    /**
     * 后台删除商品
     * 慎用删除，业务上多使用将商品下架这种“软删除”方式
     *
     * @param id 待删除商品的id
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台删除商品")
    @PostMapping("/admin/product/delete")
    @ResponseBody
    public ApiRestResponse deleteProduct(@RequestParam Integer id) throws ImoocMallException {
        productService.delete(id);

        return ApiRestResponse.success();
    }

    /**
     * 后台批量上下架商品
     * 即批量修改商品的上下架状态
     *
     * @param ids        需要修改上下架的商品id数组
     * @param sellStatus 需要修改成为的上下架状态
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @ApiOperation("后台批量上下架商品")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    @ResponseBody
    public ApiRestResponse batchUpdateSellStatus(@RequestParam(name = "ids") Integer[] ids, @RequestParam(name = "sellStatus") Integer sellStatus) {
        productService.batchUpdateSellStatus(ids, sellStatus);

        return ApiRestResponse.success();
    }

    /**
     * 后台获取商品列表
     * 采用分页技术
     *
     * @param pageNum  本次调用要获取的页码（只拿这一页的内容）
     * @param pageSize 每页显示记录数
     * @return ApiRestResponse对象
     */
    @ApiOperation("后台获取商品列表")
    @PostMapping("/admin/product/list")
    @ResponseBody
    public ApiRestResponse list(@RequestParam(name = "pageNum") Integer pageNum, @RequestParam(name = "pageSize") Integer pageSize) {
        PageInfo pageInfo = productService.listForAdmin(pageNum, pageSize);

        return ApiRestResponse.success(pageInfo);
    }
}
