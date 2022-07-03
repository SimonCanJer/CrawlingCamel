package com.txt_mining.common.config.camel.api;

import org.apache.camel.Exchange;

import java.util.Map;

/**
 * declares operations are inherent for a specific camel uri, which can be
 * any of 150 which  are supported by Camel.
 * Additionally it declared methods, which are dedicated to mashal and unmarshal data to and from Camel Exchange.
 * Marshalling depends on concrete implementation of messaging
 */

public interface ICamelUriInherent {
    /** get camel producing URL*/
    String camelUrlProduce();

    /**  get Camelconsuming url*/
    String camelUrlConsume();

    /**
     * Pack data and value to exchange
     * @param ex               Camel exchange:
     * @param url                URL
     * @param text             text to be packed
     */
    void pack(Exchange ex, String url, String text);

    /**
     * unpack data to exchange
     * @param ex      - Camel's exchange
     * @param key     - message key
     * @param value   - message value
     * @param headers - headers
     */
    void upack(Exchange ex, String key, String value, Map<String,String> headers);
}
