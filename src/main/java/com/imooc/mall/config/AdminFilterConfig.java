package com.imooc.mall.config;

import com.imooc.mall.filter.AdminFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置：管理员身份验证过滤器
 * 具体细节先不管
 */
@Configuration
public class AdminFilterConfig {
    @Bean
    public AdminFilter adminFilter() {
        return new AdminFilter();
    }

    @Bean(name = "adminFilterConf") // 注意不要和本类类名冲突
    public FilterRegistrationBean adminFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(adminFilter());
        filterRegistrationBean.addUrlPatterns("/admin/category/*"); // 商品分类url前缀
        filterRegistrationBean.addUrlPatterns("/admin/product/*"); // 商品url前缀
        filterRegistrationBean.addUrlPatterns("/admin/order/*"); // 订单url前缀
        filterRegistrationBean.addUrlPatterns("/admin/upload/*"); // 上传文件url前缀

        filterRegistrationBean.setName("adminFilterConf");
        return filterRegistrationBean;
    }
}
