package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
}
