package com.jiaboyan.collection;

import java.lang.reflect.Field;
import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created by jiaboyan on 2017/12/18.
 */
public class QueueTest<E> extends AbstractQueue<E>
        implements Queue<E>, java.io.Serializable{


    private static final long serialVersionUID = 6490938188879446758L;

    private static class NodeTest<E> {

        volatile E item;

        volatile NodeTest<E> next;

        NodeTest(E item) {
            UNSAFE.putObject(this, itemOffset, item);
        }

        boolean casNext(NodeTest<E> cmp, NodeTest<E> val) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
        }
        private static sun.misc.Unsafe UNSAFE;
        private static final long itemOffset;
        private static final long nextOffset;
        static {
            //新增代码：反射实例化Unsafe
            try {
                Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                UNSAFE =  (sun.misc.Unsafe)field.get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class k = NodeTest.class;
                itemOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }


    private transient volatile NodeTest<E> head;

    private transient volatile NodeTest<E> tail;

    public QueueTest() {
        head = tail = new NodeTest<E>(null);
    }


    public boolean add(E e) {
        return offer(e);
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }
    public void clear() {

    }

    public boolean offer(E e) {
        final NodeTest<E> newNode = new NodeTest<E>(e);
        for (NodeTest<E> t = tail, p = t; ; ) {
            NodeTest<E> q = p.next;
            if (q == null) {
                if (p.casNext(null, newNode)) {
                    return true;
                }
            } else if (p == q) {
                p = (t != (t = tail)) ? t : head;
            } else {
                p = (p != t && t != (t = tail)) ? t : q;
            }
            return false;
        }
    }

    public E remove() {
        return null;
    }

    public E poll() {
        return null;
    }

    public E element() {
        return null;
    }

    public E peek() {

        return null;
    }

    public boolean contains(Object o) {
        return false;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public int size() {
        return 0;
    }


    // Unsafe mechanics
    private static sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long tailOffset;

    static {
        //新增代码：反射实例化Unsafe
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE =  (sun.misc.Unsafe)field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class k = ConcurrentLinkedQueue.class;
            headOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("head"));
            tailOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("tail"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

}
