package com.jiaboyan.collection.concurrent;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiaboyan on 2017/11/5.
 */
public class ConcurrentHashMapTest {

    public static void main(String[] agrs) throws InterruptedException {
        int x=1;
        int retries = -1;
        int RETRIES_BEFORE_LOCK =2;
        for(;;){
            System.out.println(x);
            //先比较在++
            if(++retries == RETRIES_BEFORE_LOCK){
                System.out.println(retries);
            }
            x++;
        }

//        ConcurrentHashMap<String,String> concurrentHashMap = new ConcurrentHashMap<String,String>();

//        concurrentHashMap.put("11","222");



//        final HashMap<String,String> map = new HashMap<String,String>();
//        Thread t = new Thread(new Runnable(){
//            public  void run(){
//                for(int x=0;x<10000;x++){
//                    Thread tt = new Thread(new Runnable(){
//                        public void run(){
//                            map.put(UUID.randomUUID().toString(),"");
//                        }
//                    });
//                    tt.start();
//                    System.out.println(tt.getName());
//                }
//            }
//        });
//        t.start();
//        t.join();
    }
}
