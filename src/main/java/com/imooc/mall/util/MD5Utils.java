package com.imooc.mall.util;

import com.imooc.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 */
public class MD5Utils {
    /**
     * 将字符串转换为哈希值
     * 一个MD5解密网站：https://www.cmd5.com/default.aspx
     *
     * @param strValue 待转换字符串
     * @return 加密后的结果
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 为增加密码复杂度，防止被倒推，这里密码后再拼接上一个复杂字符串（SALT）
        return Base64.encodeBase64String(md5.digest((strValue + Constant.SALT).getBytes()));
    }

    public static void main(String[] args) {
        String md5 = null;

        try {
            md5 = getMD5Str("1234"); // gdyb21LQTcIANtvYMT7QVQ==
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println(md5);
    }
}
