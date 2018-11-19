package com.jiaboyan.collection.list;

import java.util.*;

/**
 * Created by jiaboyan on 2017/7/27.
 */
public class ArrayListTest {

    public static int ITERATION_NUM = 50000000;

    public static void main(String[] agrs) throws InterruptedException {
//        getPerformanceCompare();
        insertPerformanceCompare();
//        List<Integer> arrayList = new ArrayList<Integer>();
//        arrayList.add(42);
//        arrayList.add(32);
//        arrayList.add(64);
//        arrayList.add(234);
//        arrayList.add(656);
//        for(Integer integer:arrayList){
//            System.out.println(integer);
//        }
//        System.out.println(arrayList);
    }

    public static void getPerformanceCompare() throws InterruptedException {
        Thread.sleep(5000);

        List<Integer> arrayList = new ArrayList<Integer>();
        for (int x = 0; x < ITERATION_NUM; x++) {
            arrayList.add(x);
        }
        List<Integer> linkedList = new LinkedList<Integer>();
        for (int x = 0; x < ITERATION_NUM; x++) {
            linkedList.add(x);
        }
        Random random = new Random();
        System.out.println("LinkedList获取测试开始");
        long start = System.nanoTime();
        for (int x = 0; x < ITERATION_NUM; x++) {
            int j = random.nextInt(x + 1);
            int k = linkedList.get(j);
        }
        long end = System.nanoTime();
        System.out.println(end - start);

        System.out.println("ArrayList获取测试开始");
        start = System.nanoTime();
        for (int x = 0; x < ITERATION_NUM; x++) {
            int j = random.nextInt(x + 1);
            int k = arrayList.get(j);
        }
        end = System.nanoTime();
        System.out.println(end - start);
    }

    public static void insertPerformanceCompare() throws InterruptedException {

        Thread.sleep(5000);

        List<Integer> linkedList = new LinkedList<Integer>();
        System.out.println("LinkedList新增测试开始");
        for (int x = 0; x < 10; x++) {
            linkedList.add(x);
        }
        long start = System.nanoTime();
        for (int x = 0; x < ITERATION_NUM; x++) {
            linkedList.add(x);
        }
        long end = System.nanoTime();
        System.out.println(end - start);

        List<Integer> arrayList = new ArrayList<Integer>();
        System.out.println("ArrayList新增测试开始");
        for (int x = 0; x < 10; x++) {
            arrayList.add(x);
        }
        start = System.nanoTime();
        for (int x = 0; x < ITERATION_NUM; x++) {
            arrayList.add(x);
        }
        end = System.nanoTime();
        System.out.println(end - start);
    }

    public static void methodDo(){
        //创建ArrayList集合：
        List<String> list = new ArrayList<String>();
        System.out.println("list集合初始化容量："+list.size());

        //添加功能：
        list.add("Hello");
        list.add("world");
        list.add(2,"!");
        System.out.println("list当前容量："+list.size());

        //修改功能：
        list.set(0,"my");
        list.set(1,"name");
        System.out.println("list当前内容："+list.toString());

        //获取功能：
        String element = list.get(0);
        System.out.println(element);

        Iterator<String> iterator =  list.iterator();
        while(iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
        }

        //判断功能：
        boolean isEmpty = list.isEmpty();
        boolean isContain = list.contains("my");

        //长度功能：
        int size = list.size();


        //把集合转换成数组：
        String[] strArray = list.toArray(new String[]{});

        //删除功能：
        list.remove(0);
        list.remove("world");
        list.clear();
        System.out.println("list当前容量："+list.size());
    }



}
