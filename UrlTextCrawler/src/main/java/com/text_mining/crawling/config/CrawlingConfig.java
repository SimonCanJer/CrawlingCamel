package com.text_mining.crawling.config;

import com.text_mining.crawling.async.CrawlingFlowProvider;
import com.txt_mining.common.config.camel.api.ICamelUriInherent;
import com.txt_mining.common.asyncUtills.api.ITaskFunctionProvider;
import com.txt_mining.common.camel.api.ICamelContext;
import com.txt_mining.common.config.ConfigurationBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * main configuration class for all services, which initializes messaging way of Camel
 */
@Configuration
@ComponentScan("com.text_mining.crawling.rest")
public class CrawlingConfig extends ConfigurationBase {
    static final String VM_PRODUCER_QUEUE = "vm://textSend";

    /**
     * just implementing {@link ConfigurationBase#initApplication(ApplicationContext)}
     *
     * @param context -Spring Application Context
     *                Creates camel context and rules to send URL
     */
    @Override
    protected void initApplication(ApplicationContext context) {

        ICamelContext camelContext = context.getBean(ICamelContext.class);
        ICamelUriInherent generator = context.getBean(ICamelUriInherent.class);
        camelContext.addRoute((rb) -> {
            rb.from(VM_PRODUCER_QUEUE).process((e) -> {
                String s = e.getIn().getBody().toString();
                String url = e.getIn().getHeader("url", String.class);
                generator.pack(e, url, s);
            }).to(generator.camelUrlProduce());

        });
        camelContext.start();

    }

    /**
     * exposes bean, which provides task executor which acts
     * way of {@link com.text_mining.crawling.Crawler} on the bottom.
     * Besides, this method connects executive workflow of the implementer
     * to Camel mechanism of data send to any listener.
     * Concrete provider of Camel
     * Is public just 4 documenetation
     *
     * @param producer- Camel producer interface
     * @return - the provider implementation
     */
    @Bean
    public ITaskFunctionProvider taskFunctionPorvider(@Autowired ICamelContext.ICamelProduce producer) {
        CrawlingFlowProvider crawlingFlowProvider = new CrawlingFlowProvider((url, text) -> {
            //this way an URL text will be published
            producer.sendData(text, "url", url);
        });
        return crawlingFlowProvider;

    }

    /**
     * provides bean performing producing over Camel
     *
     * @param context - interface of {@link ICamelContext}
     * @return - Camel Producer interface {@link com.txt_mining.common.camel.api.ICamelContext.ICamelProduce}
     */
    @Bean
    ICamelContext.ICamelProduce camelProducer(ICamelContext context) {
        ICamelContext.ICamelProduce producer = context.producerFor(VM_PRODUCER_QUEUE);
        return producer;
    }

}
