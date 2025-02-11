package com.imooc.mall.filter;

import com.imooc.mall.common.Constant;
import com.imooc.mall.model.pojo.User;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 校验用户身份的过滤器
 */
public class UserFilter implements Filter {
    // 当前登录用户信息
    public static User currentUser; // 暂时不考虑多线程安全问题

    @Autowired
    UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        // Filter.super.destroy();
    }

    // 校验过程
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 拿到session
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        // 校验：需有人登录
        currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER); // 保用属性保存登录信息，便于Controller中调用该属性获取用户信息
        if (currentUser == null) { // 若无人登录
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\":\"10007\",\n" +
                    "    \"msg\":\"NEED_LOGIN\",\n" +
                    "    \"data\":null\n" +
                    "}");
            out.flush();
            out.close();
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
