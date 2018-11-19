package com.jiaboyan.collection.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiaboyan on 2017/8/5.
 */
public class HashMapTest {

        public static void main(String[] agrs) throws InterruptedException {

            Thread.sleep(3000);

            int hash = 4;

            int length = 16;

            long start = System.nanoTime();
            for(int x = 0;x<100000;x++){
    //            int result = hash%length;
                int result = hash&(length-1);
    //            System.out.println(result);
    //共耗时：7515748  8673789  5734321  5426216  7897104
    //共耗时： 3858877  3656493  5005590  3932128  3924576
            }
            long end = System.nanoTime()-start;
            System.out.println("共耗时：" + end);

        //创建HashMap集合：
//        Map<String,String> map = new HashMap<String,String>();
//        System.out.println("HashMap元素大小:"+map.size());
//
//        //元素添加:
//        map.put("hi8","hello");
//        map.put("hi7","hello");
//        map.put("hi6","hello");
//        map.put("hi5","hello");
//        map.put("hi4","hello");
//        map.put("hi3","hello");
//        map.put("hi2","hello");
//        map.put("hi1","hello");
//        map.put("h,,,,,ewei","hello");
//        map.put("m32423y","hello");
//        map.put("namerwere","hello");
//        map.put("i32432s","hello");
//        map.put("jiaboy324234an","hello");
//
//        //遍历1：获取key的Set集合
//        for(String key:map.keySet()){
//            System.out.println("map的key是:"+key);
//            System.out.println("map的value是:"+map.get(key));
//        }
//
//        //遍历2:得到Set集合迭代器
//        Set<Map.Entry<String,String>> mapSet1 = map.entrySet();
//        Iterator<Map.Entry<String,String>> iterator = mapSet1.iterator();
//        while(iterator.hasNext()){
//            Map.Entry<String,String> mapEntry = iterator.next();
//            System.out.println("map的key是:" + mapEntry.getKey());
//            System.out.println("map的value是:" + mapEntry.getValue());
//        }
//
//        //遍历3:转换成Set集合，增强for循环
//        Set<Map.Entry<String,String>> mapSet2 = map.entrySet();
//        for(Map.Entry<String,String> mapEntry : mapSet2){
//            System.out.println("map的key是:" + mapEntry.getKey());
//            System.out.println("map的value是:" + mapEntry.getValue());
//        }
//
//        //元素获取：通过key获取value
//        String keyValue = map.get("jiaboyan");
//        System.out.println("HashMap的key对应的value:" + keyValue);
//
//        //元素替换：map没有提供直接set方法，而是使用新增来完成更新操作
//        map.put("jiaboyan","helloworld");
//        System.out.println("HashMap的key对应的value:" + map.get("jiaboyan"));
//
//        //元素删除：
//        String value = map.remove("jiaboyan");
//        System.out.println("HashMap集合中被删除元素的value" + value);
//        //清空所有元素：
//        map.clear();
//
//        //hashMap是否包含某个key：
//        boolean isContain = map.containsKey("hello");
//        //hashMap是否为空：
//        boolean isEmpty = map.isEmpty();
    }
}
