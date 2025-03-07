package com.imooc.mall.common;

import com.google.common.collect.Sets;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
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

    // 订单状态码枚举
    public enum OrderStatusEnum {
        CANCELED(0, "用户已取消"),
        NOT_PAID(10, "未付款"),
        PAID(20, "已付款"),
        DELIVERED(30, "已发货"),
        FINISHED(40, "交易完成");

        private int code;
        private String value;

        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code) throws ImoocMallException {
            for (OrderStatusEnum orderStatusEnum : values()) {
                if (orderStatusEnum.getCode() == code) {
                    return orderStatusEnum;
                }
            }
            throw new ImoocMallException(ImoocMallExceptionEnum.NO_ENUM);
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
