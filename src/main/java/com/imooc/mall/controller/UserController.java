package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    @ResponseBody
    public User personalPage() {
        return userService.getUser();
    }

    /**
     * 用户注册
     *
     * @param userName 用户名
     * @param password 密码
     * @return ApiRestResponse对象
     */
    @PostMapping("/register")
    @ResponseBody // 返回json格式
    public ApiRestResponse register(@RequestParam("userName") String userName, @RequestParam("password") String password) throws ImoocMallException {
        // 用户名为空
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }

        // 密码为空
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        // 密码长度过短
        if (password.length() < 8) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.PASSWORD_TOO_SHORT);
        }

        // 注册，插入数据
        userService.register(userName, password);

        return ApiRestResponse.success(); // 返回成功消息
    }

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 登录密码
     * @param session  暂存用户对象信息，用于持久登录
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @PostMapping("/login")
    @ResponseBody
    public ApiRestResponse login(@RequestParam("userName") String userName, @RequestParam("password") String password, HttpSession session) throws ImoocMallException {
        // 用户名为空
        if (StringUtils.isEmpty(userName)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_USER_NAME);
        }

        // 密码为空
        if (StringUtils.isEmpty(password)) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_PASSWORD);
        }

        // 登录
        User user = userService.login(userName, password); // 获取该user对象的副本
        user.setPassword(null); // 不保存密码

        // 保存登录信息
        session.setAttribute(Constant.IMOOC_MALL_USER, user); // 参数：key-value

        return ApiRestResponse.success(user);
    }

    /**
     * 更新用户签名
     * 前提：用户已登录，且user对象已保存至session
     *
     * @param session   暂存登录用户信息
     * @param signature 新用户签名
     * @return ApiRestResponse对象
     * @throws ImoocMallException 业务异常
     */
    @PostMapping("/user/update")
    @ResponseBody
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam("signature") String signature) throws ImoocMallException {
        // 获取当前登录用户信息
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }

        // 更新签名
        User user = new User();
        user.setId(currentUser.getId()); // 根据id查找用户
        user.setPersonalizedSignature(signature); // 填充新签名
        userService.updateInformation(user);

        return ApiRestResponse.success();
    }
}
