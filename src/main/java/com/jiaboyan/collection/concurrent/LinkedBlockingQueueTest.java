package com.jiaboyan.collection.concurrent;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by jiaboyan on 2017/12/8.
 */
public class LinkedBlockingQueueTest {

    static class Apple{

        String colour;

        public Apple(String colour){
            this.colour = colour;
        }

        public String getColour() {
            return colour;
        }

        public void setColour(String colour) {
            this.colour = colour;
        }
    }

    //生产者
    static class Producer implements Runnable{

        LinkedBlockingQueue<Apple> queueProducer ;

        Apple apple;

        public Producer( LinkedBlockingQueue<Apple> queueProducer,Apple apple){
            this.queueProducer = queueProducer;
            this.apple = apple;
        }

        public void run() {
            try {
                System.out.println("生产"+apple.getColour()+"的苹果");
                queueProducer.put(apple);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //消费者
    static class Consumer implements Runnable{

        LinkedBlockingQueue<Apple> queueConsumer ;

        public Consumer(LinkedBlockingQueue<Apple> queueConsumer){
            this.queueConsumer = queueConsumer;
        }

        public void run() {
            try {
                Apple apple = queueConsumer.take();
                System.out.println("消费"+apple.getColour()+"的苹果");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Apple> queue = new LinkedBlockingQueue<Apple>();

        Apple appleRed = new Apple("红色");
        Apple appleGreen = new Apple("绿色");

        Producer producer1 = new Producer(queue,appleRed);

        Producer producer2 = new Producer(queue,appleGreen);

        Consumer consumer = new Consumer(queue);

        producer1.run();
        producer2.run();
        consumer.run();

        Thread.sleep(10000);
    }
}
