package com.imooc.mall.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}
