package com.jiaboyan.collection.concurrent;

import com.jiaboyan.collection.App;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jiaboyan on 2017/12/12.
 */
public class ConcurrentLinkedQueueTest {

    public static int threadCount = 100000;

    public static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<String>();

    static class Offer implements Runnable {
        public void run() {
            if(queue.size()==0){
                String ele = new Random().nextInt(Integer.MAX_VALUE)+"";
                queue.offer(ele);
                System.out.println("34324"+ele);
            }
        }
    }

    static class Poll implements Runnable {
        public void run() {
            if(!queue.isEmpty()){
                String ele = queue.poll();
                System.out.println("啊哈哈哈"+ele);
            }
        }
    }

    public static void main(String[] agrs) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for(int x=0;x<threadCount;x++){
            executorService.submit(new Offer());
            executorService.submit(new Poll());
        }
        executorService.shutdown();
    }
}
