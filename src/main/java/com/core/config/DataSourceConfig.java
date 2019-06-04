package com.core.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.core.sql.JdbcExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vincent on 2019-06-05
 */
@Configuration
public class DataSourceConfig {

    @Value("${cool.jdbc.driver}")
    private String jdbcDriver;

    @Value("${cool.jdbc.url}")
    private String jdbcUrl;

    @Value("${cool.jdbc.username}")
    private String username;

    @Value("${cool.jdbc.password}")
    private String password;

    @Bean
    JdbcExecutor getJdbcExecutor(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return new JdbcExecutor(dataSource);
    }

}
