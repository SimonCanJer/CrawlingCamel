package com.text_mining.crawling.config;

import com.txt_mining.common.app.BaseApplication;
import com.txt_mining.common.asyncUtills.api.IAsyncTaskExecutionController;
import com.txt_mining.common.config.camel.impl.fs.FsCamelOperations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CrawlingConfig.class)
class CrawlingConfigTest {
    {
        BaseApplication.loadEnvironment(BaseApplication.class,null,"common.properties");
    }
    @Autowired
    IAsyncTaskExecutionController<String> taskController;
     @Test
     public void test(){

         List<String> data= new ArrayList(){{
             add("https://www.msn.com/en-nz?refurl=%2fen-nz%2ftravel%2ftripideas%2f70-of-the-planets-most-breathtaking-sights%2fss-AAIUpDp%3ffromMaestro%3dtrue");
             add("https://www.radiosport.co.nz/sport-news/rugby/accident-or-one-last-dig-eddie-jones-reveals-hansens-next-job/");
             add("https://www.glamour.de/frisuren/frisurenberatung/haarschnitte");
             add("https://www.bbc.com");
             add("https://www3.forbes.com/business/2020-upcoming-hottest-new-vehicles/13/?nowelcome");
             add("https://www.tvblog.it/post/1681999/valerio-fabrizio-salvatori-gli-inseparabili-chi-sono-pechino-express-2020");;
             add("http://edition.cnn.com");
             add("https://www.starwars.com/news/everything-we-know-about-the-mandalorian");

         }};
         AtomicReference<List> ref= new AtomicReference<>();

         Mono<List<String>> asynResult=taskController.acceptList(data);
         AtomicInteger count= new AtomicInteger();
         asynResult.subscribe((l)->{
             ref.set(l);
             count.incrementAndGet();
             synchronized (ref){
                 ref.notify();
             }
         });
         synchronized (ref){
             try {
                 ref.wait(10000);
             } catch (InterruptedException e) {
                fail("timeout");
             }
         }
         assertEquals(data.size(),ref.get().size());
         File camelDir= new File(FsCamelOperations.FILE_TARGET);
         assertTrue(camelDir.exists());
         assertTrue(camelDir.listFiles().length>0);

     }

}