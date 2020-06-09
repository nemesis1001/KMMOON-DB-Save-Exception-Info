package com.medium.kmmoon;

import com.medium.kmmoon.filter.CustomExceptionResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KmmoonExceptionResolverApplication {

    public static void main(String[] args) {
        SpringApplication.run(KmmoonExceptionResolverApplication.class, args);
    }

    @Bean
    public CustomExceptionResolver getCustomExceptionResolver()
    {
        CustomExceptionResolver registrationBean = new CustomExceptionResolver();
        // registrationBean.addUrlPatterns("/*"); // 서블릿 등록 빈 처럼 패턴을 지정해 줄 수 있다.
        return registrationBean;
    }
}
