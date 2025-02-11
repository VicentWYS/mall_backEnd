package com.imooc.mall.common;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 存储常量
 */
@Component
public class Constant {
    // 用户密码加密
    public static final String SALT = "d[w$.686dwa[a]w"; // 自定义一个复杂的字符串

    // 用户登录session的key
    public static final String IMOOC_MALL_USER = "imooc_mall_user";

    // 图片文件上传地址
    public static String FILE_UPLOAD_DIR;

    @Value("${file.upload.dir}") // 从配置文件application.properties中获取属性值
    public void setFileUploadDir(String fileUploadDir) {
        FILE_UPLOAD_DIR = fileUploadDir;
    }

    // 定义接口：商品查询结果的排序规则
    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc", "price asc");
    }

    // 商品上下架状态枚举
    public interface SaleStatus {
        int NOT_SALE = 0; // 已下架
        int SALE = 1; // 已上架
    }

    // 购物车中的商品是否被选中状态枚举
    public interface Cart {
        int UN_CHECKED = 0; // 未被选中
        int CHECKED = 1; // 被选中
    }
}
