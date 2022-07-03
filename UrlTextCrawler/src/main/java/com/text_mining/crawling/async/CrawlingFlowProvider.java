package com.text_mining.crawling.async;

import com.text_mining.crawling.Crawler;
import com.txt_mining.common.asyncUtills.api.ITaskFunctionProvider;
import com.txt_mining.common.camel.api.ICamelContext;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * provides crawling flow, using {@link Crawler} class and a publisher(to publish the crawled data), which
 * is injected into the object
 */
public class CrawlingFlowProvider implements ITaskFunctionProvider {


    BiConsumer<String,String> externalPublisher;

    /**
     * constructor, which gets external publisher to publish results for any listeners.
     * Als, see {@link com.text_mining.crawling.config.CrawlingConfig#taskFunctionPorvider(ICamelContext.ICamelProduce)}
     * @param publisher - the publishing  consumer
     */
    public CrawlingFlowProvider(BiConsumer<String, String> publisher)
    {
        this.externalPublisher =publisher;
    }

    /**
     * implements interface method, returnning new instance of the executive function, which
     * uses a new instance of crawler {@link Crawler}.
     * The function obtains the URL text and calls external publisher as weel to publish the text
     * usinhg any mechanism , which is being set in configuration of the publisher
     * @return the funcvtion announced
     */
    @Override
    public Supplier<Function<?, String>> executiveFunction() {
        Crawler cr=new Crawler();
        return ()->(o)->{
            //first, get the URL
            String s= cr.parseThisUrl((String) o);
            try{
                //now publish it
                externalPublisher.accept((String)o,s);

            }
            catch(Throwable t)
            {

            }
            return s;
        };
    }
}
