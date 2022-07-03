package com.txt_mining.common.asyncUtills.config;

import com.txt_mining.common.asyncUtills.api.IAsyncTaskExecutionController;
import com.txt_mining.common.asyncUtills.api.ITaskFunctionProvider;
import com.txt_mining.common.asyncUtills.implementation.ReactorModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * Configures asynchronous execution of data crawling for an URL {@link IAsyncTaskExecutionController}
 */
@Slf4j
@org.springframework.context.annotation.Configuration
public class Configuration {
    @Value("${async.task_poll.max_threads}")
    int maxThreads;
    @Value("${async.task_poll.max_tasks}")
    int maxTasks;

    @Bean
    IAsyncTaskExecutionController taskController(@Autowired ITaskFunctionProvider taskDelivery){
        log.debug("supplying task controller IAsynchTaskController ");
        ReactorModel model= new ReactorModel();
        log.debug("supplying task controller IAsynchTaskController: init model ");
        model.init(maxThreads,maxTasks,taskDelivery.executiveFunction());
        log.debug("IAsynchTaskController supplied!");
        return model;

    }
}
