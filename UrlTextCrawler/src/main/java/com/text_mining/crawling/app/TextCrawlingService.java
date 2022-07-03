package com.text_mining.crawling.app;

import com.txt_mining.common.app.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

public class TextCrawlingService {
    public static void main(String[] args){
        BaseApplication.init();
        BaseApplication.loadEnvironment(TextCrawlingService.class,null,"crawler.properties");
        SpringApplication application = new SpringApplicationBuilder(BaseApplication.class).build();
        application.run();
    }
}
