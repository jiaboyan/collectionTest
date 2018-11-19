package com.jiaboyan.collection.concurrent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jiaboyan on 2017/10/26.
 */
public class CopyOnWriteTest1{

    static List<String> coplist = new ArrayList<String>();

    static int x = 0;

//    public void run() {
//        try {
//            //获取集合中的底层数组：
//            Field field = coplist.getClass().getDeclaredField("array");
//            field.setAccessible(true);
//
//            //此时底层数组对象是：[Ljava.lang.Object;@73549af8
//            Object[] oldArray = (Object[]) field.get(coplist);
//            System.out.println(oldArray);
//
//            //向其中添加元素：产生了数组复制行为
//            coplist.add("jiaboyan");
//
//            //此时底层数组对象是：[Ljava.lang.Object;@378a4aef
//            Object[] newArray = (Object[]) field.get(coplist);
//            System.out.println(newArray);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void get(){
        for(;;){
            System.out.println(x);
        }

//        try {
            //            //获取集合中的底层数组：
//            Field field = coplist.getClass().getDeclaredField("elementData");
//            field.setAccessible(true);
//
//            //此时底层数组对象是：[Ljava.lang.Object;@73549af8
//            System.out.println((Object[]) field.get(coplist));
//            System.out.println((Object[]) field.get(coplist));
//            System.out.println((Object[]) field.get(coplist));
//            System.out.println((Object[]) field.get(coplist));
//            Thread.sleep(3);
//            System.out.println((Object[]) field.get(coplist));
//            System.out.println((Object[]) field.get(coplist));
//        } catch (Exception e) {
//                e.printStackTrace();
//        }
    }

    public static void set(){
        x++;

//        System.out.print("输出完毕");
        try {
            Thread.sleep(111111);
            //            //获取集合中的底层数组：
//            Field field = coplist.getClass().getDeclaredField("array");
//            field.setAccessible(true);
//
//            //此时底层数组对象是：[Ljava.lang.Object;@73549af8
//            Object[] oldArray = (Object[]) field.get(coplist);
//            //向其中添加元素：产生了数组复制行为
//            Thread.sleep(2);
////            System.out.println("线程2前面"+oldArray);
//            coplist.add("jiaboyan");
//            System.out.println("线程2"+coplist);

            //此时底层数组对象是：[Ljava.lang.Object;@73549af8
//            Object[]  newArray = (Object[]) field.get(coplist);
//            System.out.println("线程2后面"+newArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //创建线程：
    public static void main(String[] agrs){

        new Thread(){
            public void run(){
                set();
            }
        }.start();

        new Thread(){
            public void run(){
                get();
            }
        }.start();
    }
}
