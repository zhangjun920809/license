package com.data.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * @Author: tianyong
 * @Date: 2020/7/21 16:14
 * @Description: 拦截器配置类
 */
@Configuration
public class GxInterceptorConfig implements WebMvcConfigurer {

    @Bean
    public VerifyInterceptor getLicenseCheckInterceptor() {
        return new VerifyInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.getLicenseCheckInterceptor()).addPathPatterns(new String[]{"/**"});
    }
}
