package com.jiaboyan.collection.concurrent;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiaboyan on 2017/11/24.
 */
public class BlockingQueueCodeSources {

    public interface BlockingQueue<E> extends Queue<E> {

        boolean add(E e);

        boolean offer(E e);

        void put(E e) throws InterruptedException;

        boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException;

        E take() throws InterruptedException;

        E poll(long timeout, TimeUnit unit) throws InterruptedException;

        int remainingCapacity();

        boolean remove(Object o);

        public boolean contains(Object o);

        int drainTo(Collection<? super E> c);

        int drainTo(Collection<? super E> c, int maxElements);
    }









}
