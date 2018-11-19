package com.jiaboyan.collection.concurrent;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by jiaboyan on 2017/12/4.
 */
public class ArrayBlockingQueueTest {

    public static ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(10);

    public static void main(String[] args) throws InterruptedException {

        for(int x=0;x<10;x++){

        }
//        new Thread(new Producer(arrayBlockingQueue)).start();
//        new Thread(new Producer(arrayBlockingQueue)).start();
//        new Thread(new Producer(arrayBlockingQueue)).start();
//        new Thread(new Producer(arrayBlockingQueue)).start();
//
//        new Thread(new Consumer(arrayBlockingQueue)).start();
//        new Thread(new Consumer(arrayBlockingQueue)).start();
//        new Thread(new Consumer(arrayBlockingQueue)).start();
//        new Thread(new Consumer(arrayBlockingQueue)).start();
//
//        Thread.sleep(100000);
    }

    //生产者,使用put方法
    static class Producer implements Runnable{

        private ArrayBlockingQueue<String> arrayBlockingQueue;

        public Producer(ArrayBlockingQueue arrayBlockingQueue){
            this.arrayBlockingQueue = arrayBlockingQueue;
        }

        public void run() {
            try {
                String string = "i am producer,produce string";
                arrayBlockingQueue.put(string);
                System.out.println("i am producer,producer "+string);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //消费者，使用take方法
    static class Consumer implements Runnable{

        private ArrayBlockingQueue<String> arrayBlockingQueue;

        public Consumer(ArrayBlockingQueue arrayBlockingQueue){
            this.arrayBlockingQueue = arrayBlockingQueue;
        }

        public void run() {
            try {
                String string = arrayBlockingQueue.take();
                System.out.println("i am consumer,consume "+string);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
