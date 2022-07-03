package com.txt_mining.common.asyncUtills.api;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * This interface is dedicated to initiakise and execute asynchronously tasks to process
 * list of terms, which are a pint of iterests
 * @param <T>
 */
public interface IAsyncTaskExecutionController<T> {

    /**
     * accept data for single task execution
     * @param t   type of daya
     * @return Mono object which will provide asynchronous access to results.
     */
    Mono<String> accept(T t);

    /**
     * accepts list of data for batch exedcution
     * @param urls   - list of URLs
     * @return       - Mono object for asynchronous result accept
     */
    Mono<List<String>> acceptList(List<T> urls);

    /** Stops execution scheduling */
    void stop();

    /**
     *
     * @param numOfThreads           - number of processing threads
     * @param maxProcessorInstances  - maximal number of processing object instances
     * @param operationSupplier
     */
    void init(int numOfThreads, int  maxProcessorInstances, Supplier<Function<?, String>> operationSupplier);

    /**
     * Number of worker)pr instances
     * @return
     */
    int freeWorkers();

    int pendingTasks();


}
