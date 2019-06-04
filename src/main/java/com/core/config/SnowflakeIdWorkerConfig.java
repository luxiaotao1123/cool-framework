package com.core.config;

import com.core.tools.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by vincent on 2019-06-05
 */
@Configuration
public class SnowflakeIdWorkerConfig {

    @Bean
    SnowflakeIdWorker getSnowflakeIdWorker(){
        return new SnowflakeIdWorker(0L, 0L);
    }

}
