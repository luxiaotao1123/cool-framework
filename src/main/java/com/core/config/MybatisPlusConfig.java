package com.core.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.core.common.SpringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vincent on 2019-06-10
 */
@Configuration
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public SpringUtils getSpringUtils(){
        return new SpringUtils();
    }

}
