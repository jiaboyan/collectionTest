package com.jiaboyan.collection.set;

import com.jiaboyan.collection.App;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jiaboyan on 2017/8/5.
 */
public class HashSetTest {

    public static void main(String[] agrs){


        int y = 15<<1;
        int x= Integer.highestOneBit((16 - 1) << 1);
        System.out.println(x);

        hashCodeAndEquals();

//        //创建HashSet集合：
//        Set<String> hashSet = new HashSet<String>();
//        System.out.println("HashSet初始容量大小："+hashSet.size());
//
//        //元素添加：
//        hashSet.add("my");
//        hashSet.add("name");
//        hashSet.add("is");
//        hashSet.add("jiaboyan");
//        hashSet.add(",");
//        hashSet.add("hello");
//        hashSet.add("world");
//        hashSet.add("!");
//        System.out.println("HashSet容量大小："+hashSet.size());
//
//        //迭代器遍历：
//        Iterator<String> iterator = hashSet.iterator();
//        while (iterator.hasNext()){
//            String str = iterator.next();
//            System.out.println(str);
//        }
//        //增强for循环
//        for(String str:hashSet){
//            System.out.println(str);
//        }
//
//        //元素删除：
//        hashSet.remove("jiaboyan");
//        System.out.println("HashSet元素大小：" + hashSet.size());
//        hashSet.clear();
//        System.out.println("HashSet元素大小：" + hashSet.size());
//
//        //集合判断：
//        boolean isEmpty = hashSet.isEmpty();
//        System.out.println("HashSet是否为空：" + isEmpty);
//        boolean isContains = hashSet.contains("hello");
//        System.out.println("HashSet是否为空：" + isContains);
    }

    public static void hashCodeAndEquals(){
        Set<String> set1 = new HashSet<String>();
        String str1 = new String("jiaboyan");
        String str2 = new String("jiaboyan");
        System.out.println(str1==str2);
        set1.add(str1);
        set1.add(str2);
        System.out.println("长度："+set1.size()+",内容为："+set1);

        Set<App> set2 = new HashSet<App>();
        App app1 = new App();
        app1.setName("jiaboyan");

        App app2 = new App();
        app2.setName("jiaboyan");

        set2.add(app1);
        set2.add(app2);
        System.out.println("长度："+set2.size()+",内容为："+set2);

        Set<App> set3 = new HashSet<App>();
        App app3 = new App();
        app3.setName("jiaboyan");
        set3.add(app3);
        set3.add(app3);
        System.out.println("长度："+set3.size()+",内容为："+set3);
    }



}
