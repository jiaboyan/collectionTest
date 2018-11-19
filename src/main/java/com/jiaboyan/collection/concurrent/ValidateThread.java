package com.jiaboyan.collection.concurrent;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiaboyan on 2017/10/29.
 */
public class ValidateThread  extends Thread {

    private static TestList testList = new TestList();

    static class TestList{
        private Object[] array;

        public TestList(){
            this.array=new Object[0];
        }

        public void set(String aaa){
            int len = array.length;
            Object[] newElements = Arrays.copyOf(array, len + 1);
            newElements[len] = aaa;
            array = newElements;
        }

        public Object[] getArray(){
            return array;
        }
        public void get(int index){
            Object[] aaa = getArray();
            get(aaa,index);
        }

        public void get(Object[] ar,int index){
            System.out.print(ar);
            System.out.print(ar);
            System.out.print(ar);
            System.out.print(ar);
            System.out.print(ar);
            System.out.print(ar);
            System.out.print(ar);
            System.out.print(ar);
        }
    }
    private static boolean flag = false;
    static int x= 0;

    public static void get() throws InterruptedException {
        testList.get(0);
        testList.get(1);
        testList.get(2);
        testList.get(3);
    }

    public void run() {
        while (!flag){
            x++;
        }
    }

    public static void main(String[] args) throws Exception {


        new ThreadPoolExecutor(11,11,11, TimeUnit.MINUTES,new ArrayBlockingQueue(11),new ThreadPoolExecutor.AbortPolicy()).execute(new Runnable() {
            public void run() {
                for(;;){
                    testList.set("wrewr");
                    System.out.println("111");
                }
            }
        });

        new Runnable() {
            public void run() {
                try {
                    get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.run();


//        new ValidateThread().start();
//        Thread.sleep(2000);
//        flag = true;
//        System.out.println("main-----"+flag);
//        System.out.println(x);
//        System.out.println(x);
//        System.out.println(x);
//        System.out.println(x);
//        System.out.println(x);
//        System.out.println(x);
//        System.out.println(x);
//        System.out.println(x);
//        System.out.println(x);

    }
}
