package com.txt_mining.common.asyncUtills.implementation;

import com.txt_mining.common.asyncUtills.api.IAsyncTaskExecutionController;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * implements {@links IAsynchTaskController}
 * The class controls
 * asynchronous execution of delegated functions, regarding predefined  number of their working instances
 * and predefiend number of working threads.
 * The workflow consists of executor threads, which poll execution functions from a queue with limited number
 * of instances and when an instance available, it executes the instance of function, callbacks
 * listener and returns the instance back to instance poll;
 *
 */
@Slf4j
public class ReactorModel<T> implements IAsyncTaskExecutionController<T> {
    private AtomicBoolean run=new AtomicBoolean(true);

    /** instances of functions, which should make bottom line work **/
    protected ArrayBlockingQueue<Function<?,String>> freeInstances;
    protected final LinkedBlockingQueue<IExchange> pendingTasks= new LinkedBlockingQueue<>();

    ExecutorService taskPoller;
    interface IExchange extends  Consumer<String>{
        <T> T get();
    }

    public ReactorModel(){

    }
    static <T> Mono<String> makeMono(T t, Function<T,String> producer){
        return Mono.fromSupplier(()->producer.apply(t));

    }


    @Override
    public Mono<String> accept(T t) {
        CompletableFuture<String> future= new CompletableFuture<>();
        pendingTasks.add(new IExchange() {
            @Override
            public T get() {
                return  t;
            }

            @Override
            public void accept(String s) {
                future.complete(s);
            }
        });

        return Mono.fromFuture(future);

    }

    @Override
    public Mono<List<String>> acceptList(List<T> urls) {

        CompletableFuture<List<String>> future= new CompletableFuture().orTimeout(100,TimeUnit.SECONDS);
        List<String> res= new CopyOnWriteArrayList<String>();
       long started= System.currentTimeMillis();
        urls.forEach((s)-> {
            pendingTasks.add(new IExchange() {
                @Override
                public T get() {
                    return s;
                }

                @Override
                public void accept(String s) {
                    if(System.currentTimeMillis()-started>100*1000)
                        return;
                    res.add(s);
                    if(res.size()==urls.size()){
                        future.complete(res);
                    }
                }
            });
        });

        return Mono.fromFuture(future);
    }

    @Override
    public void stop() {
        run.set(false);
        taskPoller.shutdown();

    }

    @Override
    public void init(int numOfThreads, int maxInstances, Supplier<Function<?, String>> operationSupplier) {
        freeInstances= new ArrayBlockingQueue(maxInstances);
        taskPoller= Executors.newFixedThreadPool(numOfThreads);
        log.debug("initializing maxthreads {} maxInstances{}", numOfThreads,maxInstances);
         for(int i=0;i<maxInstances;i++){
            freeInstances.add(operationSupplier.get());
        }
        taskPoller.submit(()-> {
            while (run.get()){
                Function<?, String> freeInstance = null;
            try {
                freeInstance = freeInstances.take();
                IExchange exc = pendingTasks.take();
                log.debug("working instance and task are found remained {} working instances and {} tasks",freeInstances.size(),pendingTasks.size());
                exc.accept(freeInstance.apply(exc.get()));

            } catch (Exception e) {
                log.error("exception {} occured while execution task {}",e.getClass(),e.getMessage());
            } finally {
                if (freeInstance != null) {
                    freeInstances.add(freeInstance);
                }
            }
        }

        });
    }

    @Override
    public int freeWorkers() {
        return freeInstances.size();
    }

    @Override
    public int pendingTasks() {
        return pendingTasks.size();
    }
}
