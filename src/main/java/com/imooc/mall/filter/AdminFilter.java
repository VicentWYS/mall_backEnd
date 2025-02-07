package com.imooc.mall.filter;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
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
 * 校验管理员身份的过滤器
 */
public class AdminFilter implements Filter {
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
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_USER);
        if (currentUser == null) {
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

        // 校验：登录人需是管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) { // 若是管理员
            // 放行
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) servletResponse).getWriter();
            out.write("{\n" +
                    "    \"status\":\"10009\",\n" +
                    "    \"msg\":\"NEED_ADMIN\",\n" +
                    "    \"data\":null\n" +
                    "}");
            out.flush();
            out.close();
        }
    }
}
