package com.imooc.mall.config;

import com.imooc.mall.filter.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置：用户身份验证过滤器
 * 具体细节先不管
 */
@Configuration
public class UserFilterConfig {
    @Bean
    public UserFilter userFilter() {
        return new UserFilter();
    }

    @Bean(name = "userFilterConf") // 注意不要和本类类名冲突
    public FilterRegistrationBean userFilterConfig() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(userFilter());
        filterRegistrationBean.addUrlPatterns("/cart/*"); // 购物车相关
        filterRegistrationBean.addUrlPatterns("/order/*"); // 订单相关

        filterRegistrationBean.setName("userFilterConf");
        return filterRegistrationBean;
    }
}
