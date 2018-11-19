package com.jiaboyan.collection.queue;

import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Created by jiaboyan on 2017/9/12.
 */
public class QueueCodeSource {

    public static void main(String[] agrs){
        Object[] objects = new Object[10];
        int x = objects.length;
        System.out.println(x >> 1);
    }

    //队列接口Queue，Java集合中的一部分
    public interface Queue<E> extends Collection<E> {
        //将指定元素插入到队列的尾部
        boolean add(E e);

        //将指定的元素插入此队列的尾部
        boolean offer(E e);

        //获取队列头部的元素，并删除该元素
        E remove();

        //返回队列头部的元素，并删除该元素。如果队列为空，则返回null
        E poll();

        //获取队列头部的元素,但是不删除该元素
        E element();

        //返回队列头部的元素，但是不删除该元素。如果队列为空，则返回null
        E peek();
    }

}
