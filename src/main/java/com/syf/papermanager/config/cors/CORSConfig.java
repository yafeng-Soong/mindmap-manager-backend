package com.syf.papermanager.config.cors;

import com.syf.papermanager.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @project_name: paper-manager
 * @package: com.syf.papermanager.config
 * @description:
 * @author: songyafeng
 * @create_time: 2020/11/13 17:23
 */
@Configuration
public class CORSConfig {
    @Bean
    public WebMvcConfigurer CORSConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        //设置是否允许跨域传cookie
                        .allowCredentials(true)
                        //设置缓存时间，减少重复响应
                        .maxAge(3600);
            }
            /**
             * 配置静态资源路径，主要用于返回图片、文件
             * @param registry
             */
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/imgs/**").addResourceLocations("file:" + Constant.IMG_PATH);
                registry.addResourceHandler("/files/**").addResourceLocations("file:" + Constant.FILE_PATH);
            }
        };
    }
}
