package com.txt_mining.cathegory.app;

import com.txt_mining.common.app.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Main application class whci inits
 */
public class CategorizationServer {

    static public void main(String[] args){
        BaseApplication.init();
        BaseApplication.loadEnvironment(CategorizationServer.class,null,"cathegorization.properties");
        SpringApplication application = new SpringApplicationBuilder(BaseApplication.class).build();
        application.run();
    }

}
