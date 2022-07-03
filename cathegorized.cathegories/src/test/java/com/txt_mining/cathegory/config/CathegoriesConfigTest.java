package com.txt_mining.cathegory.config;

import com.txt_mining.cathegory.flow.data.api.IEstimateUrlCategory;
import com.txt_mining.common.app.BaseApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CathegoriesConfig.class)
class CathegoriesConfigTest {
    static{
        BaseApplication.loadEnvironment(BaseApplication.class,null,"common.properties");
    }
@Autowired
IEstimateUrlCategory cathegory;

 @Test
    public void testCathegories(){

     try {
         Thread.currentThread().sleep(100000);
     } catch (InterruptedException e) {

     }
 }


}