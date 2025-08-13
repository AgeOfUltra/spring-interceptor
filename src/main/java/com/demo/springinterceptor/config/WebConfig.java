package com.demo.springinterceptor.config;

import com.demo.springinterceptor.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    web mvc configurer helps us to register our interceptor.
    @Autowired
    LoggingInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

//        Way 1 to register the interceptor

//        registry.addInterceptor(new LoggingInterceptor())
//                .addPathPatterns("/api/**");


//       the second way we can create a bean and inject in this class and do the registry
        registry.addInterceptor(interceptor).addPathPatterns("/api/**");

    }
}
