package com.jiaboyan.collection.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Created by jiaboyan on 2017/9/12.
 */
public class DequeCodeSource {

    //Double ended queue (双端队列) 的缩写,
    //Deque 继承自 Queue,直接实现了它的有 LinkedList, ArayDeque,
    public interface Deque<E> extends Queue<E> {
        //将指定元素添加到双端队列的头部
        void addFirst(E e);

        //将指定元素添加到双端队列的尾部
        void addLast(E e);

        //将指定元素添加到双端队列的头部
        boolean offerFirst(E e);

        //将指定元素添加到双端队列的尾部
        boolean offerLast(E e);

        //获取并删除该双端队列的第一个元素
        E removeFirst();

        //获取并删除该双端队列的最后一个元素
        E removeLast();

        //将指定元素添加到双端队列的头部
        E pollFirst();

        //将指定元素添加到双端队列的尾部
        E pollLast();

        //获取但不删除双端队列的第一个元素
        E getFirst();

        //获取但不删除双端队列的最后一个元素
        E getLast();

        //获取但不删除双端队列的第一个元素；如果双端队列为空，则返回null
        E peekFirst();

        //获取但不删除双端队列的最后一个元素；如果双端队列为空，则返回null
        E peekLast();

        boolean removeFirstOccurrence(Object o);

        boolean removeLastOccurrence(Object o);

        boolean add(E e);

        boolean offer(E e);

        E remove();

        E poll();

        E element();

        E peek();

        void push(E e);

        E pop();

        boolean remove(Object o);

        boolean contains(Object o);

        public int size();

        Iterator<E> iterator();

        Iterator<E> descendingIterator();

    }









}
