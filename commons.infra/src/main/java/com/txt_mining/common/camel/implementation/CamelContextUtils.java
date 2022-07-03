package com.txt_mining.common.camel.implementation;
import lombok.extern.slf4j.Slf4j;
import com.txt_mining.common.camel.api.ICamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.engine.DefaultProducerTemplate;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Singleton class, which creates and manages Camel contex, ProducerTemplate, sets to up routing procedures
 * in the manages context
 */
@Slf4j
@SuppressWarnings({"uncheched"})
public class CamelContextUtils implements ICamelContext {
    private final String DEST_HEADER="dest_header";
    private final String ROUTING_TERMINATION_HEADER = "stop_routing";

    /** used to escalate error occured **/
    private BiConsumer<String, Throwable> escalator;

    /** singletone /per application Camel Context **/
    private final DefaultCamelContext ctx = new DefaultCamelContext();

    private boolean started = false;

    /** regsitry for all Camel producers **/
    ArrayList<DefaultProducerTemplate> producers = new ArrayList<>();

    /**
     * @param escalator - function, which escalates error when occures.
     * @return - this
     */
    @Override
    public ICamelContext escalatingErrorWith(BiConsumer<String, Throwable> escalator) {

        this.escalator = escalator;
        return this;
    }

    /**
     * adds routing to context
     *
     * @param routeDefiner - Consumer, which creates routing in this context
     */
    @Override
    public void addRoute(Consumer<RouteBuilder> routeDefiner) {

        try {
            ctx.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    onException(Exception.class).process((e) -> {
                        Throwable t = (Throwable) e.getProperty(Exchange.EXCEPTION_CAUGHT);
                        t.printStackTrace();
                        if (escalator != null) {
                            escalator.accept("context_rote_error", t);
                        }
                        log.error(t.getMessage());
                    }).handled(true);
                    routeDefiner.accept(this);
                }
            });
        } catch (Exception e) {
            escalator.accept("common_addRoute", e);
        }
    }

    /**
     * create instanceof {@link ICamelProduce}
     *
     * @param url url to bind {@link DefaultProducerTemplate}
     * @return this
     */
    @Override
    public ICamelProduce producerFor(String url) {
        DefaultProducerTemplate pt = new DefaultProducerTemplate(ctx);
        pt.setDefaultEndpointUri(url);
        producers.add(pt);
        if (started) {
            pt.start();
        }
        return new ICamelProduce() {
            @Override
            public void sendData(Object t) {
                pt.sendBody(t);
            }

            @Override
            public void sendData(Object data, String headerId, Object headerValue) {
                pt.sendBodyAndHeader(data, headerId, headerValue);
            }
        };
    }

    @Override
    public IDynamicRouter addDynamicRoute(String name) {
        return new DynamicRouter(name);
    }

    @Override
    public void start() {
        log.info("starting common camel context and registered producers");
        try {
            ctx.start();
            while (!ctx.isStarted()) {

            }
            producers.forEach((p) -> {
                p.start();
            });
            started = true;
            log.info("common camel context started ");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                stop();
            }));
        } catch (Throwable t) {
            log.warn("Failed to start Camel context due to {}  ", t.getMessage());
            if (escalator != null) {
                escalator.accept("camel_context_manager", t);
            }
        }
    }

    @Override
    public void stop() {
        producers.forEach((p) -> {
            p.stop();
        });
        try {
            ctx.stop();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void stopRouting(String id) {
        try {
            ctx.stopRoute(id);
        } catch (Exception e) {
            log.warn("cannot stop routing {} because {}", id, e.getMessage());
        }
    }

    static class KeyVal{
        final String key;
        final Object value;

        KeyVal(String key, Object value) {
            this.key = key;
            this.value = value;
        }

    }
    /**
     * Performs dynamic message routing by handling methods of the
     * @
     */
    private class DynamicRouter implements IDynamicRouter {


        private final String name;

        private ICamelProduce producer;

        private DynamicRouter(String name) {
            this.name = name;
            producer = producerFor(createURL());
            create();
        }

        private String createURL() {
            return String.format("vm:%s", name);
        }

        private void create() {
            try {
                ctx.addRoutes(new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        from(createURL()).process((e)->{
                            KeyVal kv= (KeyVal) e.getIn().getBody();
                            e.getIn().setHeader(DEST_HEADER,kv.key);
                            e.getIn().setBody(kv.value);
                        }).dynamicRouter(method(CamelContextUtils.this,"dispatch"));
                    }
                });
            } catch (Exception e) {
                log.warn("cannot create dynamic routing");
            }
        }

        @Override
        public <T> void route(String url, T t) {
            producer.sendData(new KeyVal(url, t));
        }

    }
    String dispatch(Exchange ex) {
        if (null != ex.getIn().getHeader(ROUTING_TERMINATION_HEADER)) {
            return null;
        }
        ex.getIn().setHeader(ROUTING_TERMINATION_HEADER, ROUTING_TERMINATION_HEADER);
        try {
            return (String) ex.getIn().getHeader(DEST_HEADER);
        } catch (Exception e) {
            log.warn("exchange contains wrong object {}", ex.getIn().getClass());
            return null;
        }
    }
}