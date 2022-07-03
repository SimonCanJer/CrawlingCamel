package com.txt_mining.common.config.camel.impl.fs;

import com.txt_mining.common.config.camel.api.ICamelUriInherent;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * implements  {@link ICamelUriInherent } interface for Camel file system
 */
@Component
public class FsCamelOperations implements ICamelUriInherent {

    public static final String URL_PREFIX = "url:";
    public static final String URL_HEADER = "url";
    public static final String URL_POSTFIX = ":\n\n\n";
    public static final String FILE_TARGET = "/vol_interact/text_mail";
    public static final String FILE_URL = "file:"+FILE_TARGET;
    static  {
        File f= new File(FILE_TARGET);
        if(!f.exists())
            f.mkdirs();

    }

    @Override
    public String camelUrlProduce() {
        return FILE_URL;
    }

    @Override
    public String camelUrlConsume() {
        return FILE_URL;
    }

    @Override
    public void pack(Exchange ex, String url, String text) {
        StringBuffer buffer= new StringBuffer();
        buffer.append(URL_PREFIX).append(ex.getIn().getHeader(URL_HEADER)).append(URL_POSTFIX).append(text);
        ex.getIn().setBody(buffer.toString());

    }

    @Override
    public void upack(Exchange ex, String key, String value, Map<String, String> headers) {

        String url=null;
        if(value.startsWith(URL_PREFIX))
        {
           ///////////// url=value.substring(URL_PREFIX.length());
            int index=value.indexOf(URL_POSTFIX);
            url=value.substring(URL_PREFIX.length(),index);
            index+=URL_POSTFIX.length();
            value =value.substring(index);
            ex.getIn().setHeader(URL_HEADER,url);
            ex.getIn().setBody(value);
        }

    }

}
