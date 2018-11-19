package com.jiaboyan.collection.map;

import java.util.*;

/**
 * Created by jiaboyan on 2017/9/3.
 */
public class TreeMapTest {
    public static void main(String[] agrs){
        //自定义顺序比较
        customSort();
//        //创建TreeMap对象：
//        TreeMap<String,Integer> treeMap = new TreeMap<String,Integer>();
//        System.out.println("初始化后,TreeMap元素个数为：" + treeMap.size());
//
//        //新增元素:
//        treeMap.put("hello",1);
//        treeMap.put("world",2);
//        treeMap.put("my",3);
//        treeMap.put("name",4);
//        treeMap.put("is",5);
//        treeMap.put("jiaboyan",6);
//        treeMap.put("i",6);
//        treeMap.put("am",6);
//        treeMap.put("a",6);
//        treeMap.put("developer",6);
//        System.out.println("添加元素后,TreeMap元素个数为：" + treeMap.size());
//
//        //遍历元素：
//        Set<Map.Entry<String,Integer>> entrySet = treeMap.entrySet();
//        for(Map.Entry<String,Integer> entry : entrySet){
//            String key = entry.getKey();
//            Integer value = entry.getValue();
//            System.out.println("TreeMap元素的key:"+key+",value:"+value);
//        }
//
//        //获取所有的key：
//        Set<String> keySet = treeMap.keySet();
//        for(String strKey:keySet){
//            System.out.println("TreeMap集合中的key:"+strKey);
//        }
//
//        //获取所有的value:
//        Collection<Integer> valueList = treeMap.values();
//        for(Integer intValue:valueList){
//            System.out.println("TreeMap集合中的value:" + intValue);
//        }
//
//        //获取元素：
//        Integer getValue = treeMap.get("jiaboyan");//获取集合内元素key为"jiaboyan"的值
//        String firstKey = treeMap.firstKey();//获取集合内第一个元素
//        String lastKey =treeMap.lastKey();//获取集合内最后一个元素
//        String lowerKey =treeMap.lowerKey("jiaboyan");//获取集合内的key小于"jiaboyan"的key
//        String ceilingKey =treeMap.ceilingKey("jiaboyan");//获取集合内的key大于等于"jiaboyan"的key
//        SortedMap<String,Integer> sortedMap =treeMap.subMap("a","my");//获取集合的key从"a"到"jiaboyan"的元素
//
//        //删除元素：
//        Integer removeValue = treeMap.remove("jiaboyan");//删除集合中key为"jiaboyan"的元素
//        treeMap.clear(); //清空集合元素：
//
//        //判断方法：
//        boolean isEmpty = treeMap.isEmpty();//判断集合是否为空
//        boolean isContain = treeMap.containsKey("jiaboyan");//判断集合的key中是否包含"jiaboyan"
    }

    //自然排序顺序：升序
    public static void naturalSort(){
        //第一种情况：
        TreeMap<Integer,String> treeMapFirst = new TreeMap<Integer, String>();
        treeMapFirst.put(1,"jiaboyan");
        treeMapFirst.put(6,"jiaboyan");
        treeMapFirst.put(3,"jiaboyan");
        treeMapFirst.put(10,"jiaboyan");
        treeMapFirst.put(7,"jiaboyan");
        treeMapFirst.put(13,"jiaboyan");
        System.out.println(treeMapFirst.toString());

        //第二种情况:
        TreeMap<SortedTest,String> treeMapSecond = new TreeMap<SortedTest, String>();
        treeMapSecond.put(new SortedTest(10),"jiaboyan");
        treeMapSecond.put(new SortedTest(1),"jiaboyan");
        treeMapSecond.put(new SortedTest(13),"jiaboyan");
        treeMapSecond.put(new SortedTest(4),"jiaboyan");
        treeMapSecond.put(new SortedTest(0),"jiaboyan");
        treeMapSecond.put(new SortedTest(9),"jiaboyan");
        System.out.println(treeMapSecond.toString());
    }

    //自定义排序顺序：升序
    public static void customSort(){
        TreeMap<SortedTest,String> treeMap = new TreeMap<SortedTest, String>(new SortedTestComparator());
        treeMap.put(new SortedTest(10),"hello");
        treeMap.put(new SortedTest(21),"my");
        treeMap.put(new SortedTest(15),"name");
        treeMap.put(new SortedTest(2),"is");
        treeMap.put(new SortedTest(1),"jiaboyan");
        treeMap.put(new SortedTest(7),"world");
        System.out.println(treeMap.toString());
    }
}
