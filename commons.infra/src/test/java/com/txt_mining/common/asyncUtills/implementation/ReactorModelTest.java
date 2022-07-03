package com.txt_mining.common.asyncUtills.implementation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ReactorModelTest {
    @Test
    public void  test(){
        ReactorModel<String> reactorModel= new ReactorModel();
        AtomicInteger ai= new AtomicInteger();
        Map<Integer,AtomicInteger> calls= new ConcurrentHashMap<>();
        Map<Long,AtomicInteger> threads= new ConcurrentHashMap<>();
        reactorModel.init(3,3,()->{
            int id=ai.getAndIncrement();
            return (s)->{
                String val=String.format("%s-%s",s,id);
                AtomicInteger counter=calls.computeIfAbsent(id,(k)->{return new AtomicInteger(); });
                counter.incrementAndGet();
                log.debug("workers  counter {} ",counter.get());
                counter=threads.computeIfAbsent(Thread.currentThread().getId(),(k)->{return new AtomicInteger(); });
                counter.incrementAndGet();
                //log.debug("thread {} usage counter {}",Thread.currentThread().getId(),counter.get());

                return val;
            };
        });
        AtomicInteger execCounter=new AtomicInteger();
        CountDownLatch latch= new CountDownLatch(10);
        for(int i=0;i<10;i++) {
            int j=i;
            reactorModel.accept("val"+i).subscribe((s)->{
                execCounter.incrementAndGet();
                log.debug("executed  {} times",execCounter.get());
                assertTrue(s.startsWith("val"+j));
                latch.countDown();

            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            fail("interrupted");
        }
        assertTrue(3>=threads.size());
        assertEquals(3,calls.size());
        reactorModel.stop();

    }

}