package com.txt_mining.common.app;

import com.txt_mining.common.camel.api.ICamelContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Base Spring application class, which performs common
 * initiaization of Spring mechanism of Spring services, which using the class
 */
@SpringBootApplication(
exclude = { SecurityAutoConfiguration.class,  ManagementWebSecurityAutoConfiguration.class })
@EnableWebMvc
@ComponentScan("${config.root}")
@Slf4j
public class BaseApplication {
    public static final String PROPETY_EXTERNAL_VOLUME = "resources.volume.external";

    /**
     * Defaul value of external volume
     */
    static public final String DEFAULT_EXTERNAL_VOLUME = "/vol_interact";

    static public void init() {
        loadEnvironment(BaseApplication.class, null, "common.properties");
    }

    static public void loadEnvironment(Class cl, String dir, String file) {
        InputStream is = null;
        Properties props = new Properties();
        String prop = System.getProperty(PROPETY_EXTERNAL_VOLUME, DEFAULT_EXTERNAL_VOLUME);
        if (file != null) {
            File f = null;

            if (dir != null) {
                f = new File(prop, dir);
                f = new File(f, file);
            } else
                f = new File(file);

            if (f.exists()) {
                try {
                    is = new FileInputStream(f);

                } catch (Exception e) {
                    log.warn("properties file does not exists in location {}", f.getAbsolutePath());

                }
            }
        }
        if(is==null)
            is = cl.getClassLoader().getResourceAsStream(file);
        if (is == null)
            throw new Error(new FileNotFoundException());
        try {
            props.load(is);
        } catch (Exception e) {
            log.error("A FATAL ERROR WHILE READING PROPERTIES FILE  excpetion {}, Spring bean init will be stopped  ", e.getMessage());
            throw new Error(e);
        }
        try {
            is.close();
        } catch (Exception e) {
            log.warn("cannot close file stream after properties read ");
        }
        props.keySet().forEach((k) -> {
            String key = ((String) k).replaceAll("\\.", "\\_").toUpperCase(Locale.ROOT);
            String env = System.getenv(key);
            if (env != null) {
                props.put(k, env);
            }
        });
        props.entrySet().forEach((e)->{
            System.setProperty((String)e.getKey(),e.getValue().toString());
        });


    }


}
