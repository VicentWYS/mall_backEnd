package com.imooc.mall.config;

import com.imooc.mall.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImoocMallWebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置静态资源的处理规则
     * 静态资源包括图片、CSS、JavaScript 文件等，这些资源不需要经过控制器处理，可以直接通过 URL 访问
     * 以图片为例：若url中包含"/images/**"（由用户自定义设置），Spring MVC 会查找Constant.FILE_UPLOAD_DIR中指定的路径
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 后台管理模块用到的前端文件路径映射
        // registry.addResourceHandler("/admin/**").addResourceLocations(
        //         "classpath:/static/admin/");
        registry.addResourceHandler("/images/**").addResourceLocations(
                "file:" + Constant.FILE_UPLOAD_DIR);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }
}
