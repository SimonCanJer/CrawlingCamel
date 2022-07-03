package com.txt_mining.common.config;

import com.txt_mining.common.camel.api.ICamelContext;
import com.txt_mining.common.camel.implementation.CamelContextUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

/**
 * This is configuration class which exposes bean camel context interface as well as
 * {@link ICamelContext.ICamelProduce}
 * Also the class points to
 *
 */
@ComponentScans({@ComponentScan("${kube8.redirect}"),
        @ComponentScan("com.txt_mining.common.camel.config"),
        @ComponentScan("com.txt_mining.common.asyncUtills.config"),
        @ComponentScan("${camel.uri.builder}")})
public abstract class ConfigurationBase {



    @Bean
    ICamelContext camelContext(){
        CamelContextUtils context= new CamelContextUtils();
        return context;
    }
    @Bean
    ApplicationContextAware onContextReady() {
        return new ApplicationContextAware() {
            @Override
            public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
                initApplication(applicationContext);
                applicationContext.getBean(ICamelContext.class).start();

            }

        };
    }
    abstract protected void initApplication(ApplicationContext context);
}
