package com.txt_mining.cathegory.config;
import com.txt_mining.cathegory.flow.data.Category;
import com.txt_mining.cathegory.flow.data.LoadCategories;
import com.txt_mining.cathegory.flow.data.api.ICategorizedUrlStorage;
import com.txt_mining.cathegory.flow.data.api.IEstimateCategories;
import com.txt_mining.cathegory.flow.data.api.IEstimateUrlCategory;
import com.txt_mining.cathegory.flow.data.api.ILoadCategories;
import com.txt_mining.cathegory.flow.data.implementation.CategorizedUrlStorage;
import com.txt_mining.cathegory.flow.data.implementation.EstimateCategories;
import com.txt_mining.cathegory.flow.data.implementation.EstimateUrlCategory;
import com.txt_mining.common.asyncUtills.api.ITaskFunctionProvider;
import com.txt_mining.common.camel.api.ICamelContext;
import com.txt_mining.common.config.ConfigurationBase;
import com.txt_mining.common.config.camel.api.ICamelUriInherent;
import com.txt_mining.common.config.camel.impl.fs.FsCamelOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@ComponentScan("com.txt_mining.cathegory.rest")
public class CathegoriesConfig extends ConfigurationBase {
    @Override
    protected void initApplication(ApplicationContext context) {
        ICamelContext camelContext=context.getBean(ICamelContext.class);
        ICamelUriInherent camelUrlnherent=context.getBean(ICamelUriInherent.class);
        IEstimateUrlCategory cathegorizer= context.getBean(IEstimateUrlCategory.class);
        ICamelUriInherent operations= context.getBean(ICamelUriInherent.class);
        ILoadCategories loader= context.getBean(ILoadCategories.class);
        ICategorizedUrlStorage storage = context.getBean(ICategorizedUrlStorage.class);
        tryLoadCathegories(loader);

        camelContext.addRoute((rb)->{
            rb.from(FsCamelOperations.FILE_TARGET).process((ex)->{
                operations.upack(ex,null,ex.getIn().getBody(String.class),null);
            }).process((ex)->{
                for(Category cat: loader.cathegories()){
                    storage.add(cathegorizer.mapUrl2Cathegory((String)ex.getIn().getHeader("url"),ex.getIn().getBody(String.class)));
                }
            });

        });
        camelContext.addRoute((rb)->{



        });

    }

    private void tryLoadCathegories(ILoadCategories loader) {
        File f= new File("/vol_interact/cathegories/cathegories.json");
        InputStream is=null;
        if(f.exists()){
            try{
               is= new FileInputStream(f);

            }
            catch(Exception e){

            }

        }
        else
        {
           is= this.getClass().getClassLoader().getResourceAsStream("cathegories.json");
        }
        if(is==null)
            throw new Error(new FileNotFoundException("cathegories.json"));

        loader.load(is);
    }

    @Bean
    ICategorizedUrlStorage cathegorizedStorage(){
        return new CategorizedUrlStorage();
    }
    @Bean
    ILoadCategories loadCathegories(){
        return new LoadCategories();
    }

    @Bean
    IEstimateCategories estimator(@Autowired ILoadCategories loadCathegories){
        EstimateCategories estimate= new EstimateCategories();
        estimate.set(loadCathegories);
        return estimate;
    }

    @Bean
    IEstimateUrlCategory estimatUrlCathegory(@Autowired IEstimateCategories estimator){
        EstimateUrlCategory worker= new EstimateUrlCategory();
        worker.set(estimator);
        return worker;
    }
    @Bean
    ITaskFunctionProvider
    functionProvider(){
        return new ITaskFunctionProvider() {
            @Override
            public Supplier<Function<?, String>> executiveFunction() {
                return ()->(o)->"";
            }
        };
    }
}
