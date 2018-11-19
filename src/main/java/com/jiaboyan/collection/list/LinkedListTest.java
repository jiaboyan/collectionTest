package com.jiaboyan.collection.list;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiaboyan on 2017/7/27.
 */
public class LinkedListTest {

    public static void main(String[] agrs){
        LinkedList<String> linkedList = new LinkedList<String>();
        System.out.println("LinkedList初始容量："+linkedList.size());
        linkedList.add(null);

        linkedList.add(0,null);
        //添加功能：
        linkedList.add("my");
        linkedList.add("name");
        linkedList.add("is");
        linkedList.add("jiaboyan");
        System.out.println("LinkedList当前容量："+ linkedList.size());

        //修改功能:
        linkedList.set(0,"hello");
        linkedList.set(1,"world");
        System.out.println("LinkedList当前内容："+ linkedList.toString());

        //获取功能：
        String element = linkedList.get(0);
        System.out.println(element);

        //遍历集合：
        Iterator<String> iterator =  linkedList.iterator();
        while(iterator.hasNext()){
            String next = iterator.next();
            System.out.println(next);
        }

        for(String str:linkedList){
            System.out.println(str);
        }

        //判断功能：
        boolean isEmpty = linkedList.isEmpty();
        boolean isContains = linkedList.contains("jiaboyan");

        //长度功能：
        int size = linkedList.size();

        //删除功能：
        linkedList.remove(0);
        linkedList.remove("jiaboyan");
        linkedList.clear();
        System.out.println("LinkedList当前容量：" + linkedList.size());
    }
}
