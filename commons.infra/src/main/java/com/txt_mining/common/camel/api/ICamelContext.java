package com.txt_mining.common.camel.api;


import org.apache.camel.builder.RouteBuilder;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * interface, which declares functionality to create and manage camel context, listening and producing routines
 */

public interface ICamelContext {


    interface ICamelProduce {
        void sendData(Object data);
        void sendData(Object  data,String HeaderId,Object headerValue);

    }

    interface IDynamicRouter{
        <T> void route(String url,T t);
    }

    /**
     * sets escalating error procedure
     * @param escalator    - the procedure, which will escalates error
     * @return             - self
     */
    ICamelContext escalatingErrorWith(BiConsumer<String,Throwable> escalator);

    /**
     * sets delivers consumer, which builds additional routing on
     * @param routeDefiner  routing builder procedure
     */
    void  addRoute(Consumer<RouteBuilder> routeDefiner);

    /**
     *  creates an instance, which handles producer build and binding it to url
     * @param url  - url, to which new producer wil be bound to
     * @return
     */
    ICamelProduce producerFor(String url);

    IDynamicRouter addDynamicRoute(String name);

    /**start context **/
     void start();


     /** stops context*/
    void stop();

    void stopRouting(String id);



}
