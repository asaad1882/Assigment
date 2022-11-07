package com.daleel.student.ms.confiuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {
    @Value("${web.authentication.apikey}")
    private String apiKeyVal;
@Bean
public FilterRegistrationBean<AuthenticationHandlerFilter> authFilter(){
    FilterRegistrationBean<AuthenticationHandlerFilter> filterRegistrationBean =new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new AuthenticationHandlerFilter(apiKeyVal));
    filterRegistrationBean.addUrlPatterns("/api/*");
    return filterRegistrationBean;
}
}
    