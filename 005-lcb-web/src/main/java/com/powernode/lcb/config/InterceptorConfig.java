package com.powernode.lcb.config;

import com.powernode.lcb.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //使用拦截器的路径表达式，数组
        String[] includePaths={
                "/**"   //应用于所有路径
        };
        //排除拦截器的路径表达式，数组
        String[] excludePaths={
                "/index",
                "/loan/loan",
                "/loan/loanInfo/*",
                "/loan/page/doLogin",
                "/jcaptcha/captcha",
                "/jcaptcha/captcha",
                "/loan/page/register",
                "/sendSmsCode",
                "/checkMessageCode",
                "/register",
                "/checkPhone",
                "/realName",
                "/doRealName",
                "/checkLogin",
                "/loan/page/login",
                "/css/*",
                "/images/*",
                "/img/*",
                "/js/*"

        };
        registry.addInterceptor(myInterceptor)
                .addPathPatterns(includePaths)
                .excludePathPatterns(excludePaths);

    }


}
