package com.txt_mining.common.camel.config;

import com.txt_mining.common.camel.api.ICamelContext;
import com.txt_mining.common.camel.implementation.CamelContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Exposed camel utilities
 */
@Configuration
public class CamelConfig {

    @Bean
    ICamelContext context(){
        return new CamelContextUtils();
    }
}
