package com.jiaboyan.collection;


import java.lang.reflect.Field;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;


public class ConcurrentLinkedQueue<E> {

//    extends AbstractQueue<E> implements Queue<E>, java.io.Serializable
    private static final long serialVersionUID = 196745693267521676L;



    private static class Node<E> {
        volatile E item;
        volatile Node<E> next;

        Node(E item) {
            UNSAFE.putObject(this, itemOffset, item);
        }

        boolean casItem(E cmp, E val) {
            return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
        }

        void lazySetNext(Node<E> val) {
            UNSAFE.putOrderedObject(this, nextOffset, val);
        }

        boolean casNext(Node<E> cmp, Node<E> val) {
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
                Class k = Node.class;
                itemOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("item"));
                nextOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    private transient volatile Node<E> head;

    private transient volatile Node<E> tail;

    public ConcurrentLinkedQueue() {
        head = tail = new Node<E>(null);
    }

//    public ConcurrentLinkedQueue(Collection<? extends E> c) {
//        Node<E> h = null, t = null;
//        for (E e : c) {
//            checkNotNull(e);
//            Node<E> newNode = new Node<E>(e);
//            if (h == null)
//                h = t = newNode;
//            else {
//                t.lazySetNext(newNode);
//                t = newNode;
//            }
//        }
//        if (h == null)
//            h = t = new Node<E>(null);
//        head = h;
//        tail = t;
//    }

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

    final void updateHead(Node<E> h, Node<E> p) {
        if (h != p && casHead(h, p))
            h.lazySetNext(h);
    }

    final Node<E> succ(Node<E> p) {
        Node<E> next = p.next;
        return (p == next) ? head : next;
    }

    public boolean offer(E e) {
        checkNotNull(e);
        final Node<E> newNode = new Node<E>(e);
        System.out.println("新节点"+newNode.toString()+"值："+newNode.item);
        for (Node<E> t = tail, p = t;;) {
            Node<E> q = p.next;
            if (q == null) {
                if (p.casNext(null, newNode)) {
                    System.out.println("头结点"+head.item);
//                    System.out.println("头结点的尾指针"+head.next);
//                    System.out.println("头结点的尾指针的结点元素"+head.next.item);
                    System.out.println("尾结点"+tail.item);
//                    System.out.println("尾结点的尾指针"+tail.next);
//                    System.out.println("尾结点的尾指针的结点元素"+tail.next.item);
                    if (p != t) // hop two nodes at a time
                        casTail(t, newNode);  // Failure is OK.
                    return true;
                }
            } else if (p == q) {
                System.out.println("p/q相等吗："+p.item);
                System.out.println("p/q相等吗："+q.item);
                p = (t != (t = tail)) ? t : head;
            } else {
                System.out.println("p/q----"+p);
                System.out.println("p/q----"+q);
                // 这里是为了当P不是尾节点的时候，将P 移到尾节点，方便下一次插入
                // 也就是一直保持向前推进
                p = (p != t && t != (t = tail)) ? t : q;
            }
        }
    }

    public E remove() {
        return null;
    }

    public E poll() {
        restartFromHead:
        for (;;) {
            for (Node<E> h = head, p = h, q = null;;) {
                System.out.println(head.item);
                E item = p.item;
//                q=node(1111)  p=node(1111)   item =1111   p=node(null)  h=node(null)
//                p.next=node(2222) q=node(2222)  update(h,q)---  head-->node(2222)、h.next=h(头结点指向自己)

//                h = node(null) q=node(3333) p=node(null)  p=q=node(3333) p =node(null)
//                        p.next=node(4444) q=node(4444)!=null   update(h,q) head--->q=node(4444)  h.next=h
                try{
                    System.out.println("lkjlkj"+q.item);
                }catch (Exception e){

                }

                if (item != null && p.casItem(item, null)) {
                    System.out.println("11111");
                    if (p != h) // hop two nodes at a time
                        System.out.println("lkjlkj"+q.item);
                        System.out.println("lkjlkj"+q);
                        System.out.println("bjkbjk"+q.next);
                        System.out.println("bjkbjk"+q.next.item);
                        //更新head节点指向，并修改原来的head节点的next属性，将next指向了自己
                        updateHead(h, ((q = p.next) != null) ? q : p);
                    System.out.println("bjkbjk"+q.next);
                    System.out.println(head.toString());
                    System.out.println(tail.toString());
                    System.out.println(h.next);
                    return item;
                } else if ((q = p.next) == null) {
                    System.out.println("22222");
                    updateHead(h, p);
                    return null;
                }
                else if (p == q) {
                    System.out.println("3333");
                    continue restartFromHead;
                }else{
                    System.out.println("4444");
                    p = q;
                }
            }
        }
    }

    public E element() {
        return null;
    }

    public E peek() {
//        restartFromHead:
//        for (;;) {
//            for (Node<E> h = head, p = h, q;;) {
//                E item = p.item;
//                if (item != null || (q = p.next) == null) {
//                    updateHead(h, p);
//                    return item;
//                }
//                else if (p == q)
//                    continue restartFromHead;
//                else
//                    p = q;
//            }
//        }
        return null;
    }

    Node<E> first() {
        restartFromHead:
        for (;;) {
            for (Node<E> h = head, p = h, q;;) {
                boolean hasItem = (p.item != null);
                if (hasItem || (q = p.next) == null) {
                    updateHead(h, p);
                    return hasItem ? p : null;
                }
                else if (p == q)
                    continue restartFromHead;
                else
                    p = q;
            }
        }
    }

    public boolean isEmpty() {
        return first() == null;
    }

    public boolean contains(Object o) {
        return false;
    }

    public Iterator<E> iterator() {
        return null;
    }

    public int size() {
        int count = 0;
        for (Node<E> p = first(); p != null; p = succ(p))
            if (p.item != null)
                // Collection.size() spec says to max out
                if (++count == Integer.MAX_VALUE)
                    break;
        return count;
    }

//    public boolean contains(Object o) {
//        if (o == null) return false;
//        for (Node<E> p = first(); p != null; p = succ(p)) {
//            E item = p.item;
//            if (item != null && o.equals(item))
//                return true;
//        }
//        return false;
//    }

//    public boolean remove(Object o) {
//        if (o == null) return false;
//        Node<E> pred = null;
//        for (Node<E> p = first(); p != null; p = succ(p)) {
//            E item = p.item;
//            if (item != null &&
//                    o.equals(item) &&
//                    p.casItem(item, null)) {
//                Node<E> next = succ(p);
//                if (pred != null && next != null)
//                    pred.casNext(p, next);
//                return true;
//            }
//            pred = p;
//        }
//        return false;
//    }

//    public boolean addAll(Collection<? extends E> c) {
//        if (c == this)
//            throw new IllegalArgumentException();
//
//        Node<E> beginningOfTheEnd = null, last = null;
//        for (E e : c) {
//            checkNotNull(e);
//            Node<E> newNode = new Node<E>(e);
//            if (beginningOfTheEnd == null)
//                beginningOfTheEnd = last = newNode;
//            else {
//                last.lazySetNext(newNode);
//                last = newNode;
//            }
//        }
//        if (beginningOfTheEnd == null)
//            return false;
//
//        for (Node<E> t = tail, p = t;;) {
//            Node<E> q = p.next;
//            if (q == null) {
//                if (p.casNext(null, beginningOfTheEnd)) {
//                    if (!casTail(t, last)) {
//                        t = tail;
//                        if (last.next == null)
//                            casTail(t, last);
//                    }
//                    return true;
//                }
//            }
//            else if (p == q)
//                p = (t != (t = tail)) ? t : head;
//            else
//                p = (p != t && t != (t = tail)) ? t : q;
//        }
//    }

    public Object[] toArray() {
        ArrayList<E> al = new ArrayList<E>();
        for (Node<E> p = first(); p != null; p = succ(p)) {
            E item = p.item;
            if (item != null)
                al.add(item);
        }
        return al.toArray();
    }

    public <T> T[] toArray(T[] a) {
        // try to use sent-in array
        int k = 0;
        Node<E> p;
        for (p = first(); p != null && k < a.length; p = succ(p)) {
            E item = p.item;
            if (item != null)
                a[k++] = (T)item;
        }
        if (p == null) {
            if (k < a.length)
                a[k] = null;
            return a;
        }

        // If won't fit, use ArrayList version
        ArrayList<E> al = new ArrayList<E>();
        for (Node<E> q = first(); q != null; q = succ(q)) {
            E item = q.item;
            if (item != null)
                al.add(item);
        }
        return al.toArray(a);
    }

//    public Iterator<E> iterator() {
//        return new Itr();
//    }

//    public Object[] toArray() {
//        return new Object[0];
//    }
//
//    public <T> T[] toArray(T[] a) {
//        return null;
//    }

//    private class Itr implements Iterator<E> {
//        private Node<E> nextNode;
//
//        private E nextItem;
//
//        private Node<E> lastRet;
//
//        Itr() {
//            advance();
//        }
//
//        private E advance() {
//            lastRet = nextNode;
//            E x = nextItem;
//
//            Node<E> pred, p;
//            if (nextNode == null) {
//                p = first();
//                pred = null;
//            } else {
//                pred = nextNode;
//                p = succ(nextNode);
//            }
//
//            for (;;) {
//                if (p == null) {
//                    nextNode = null;
//                    nextItem = null;
//                    return x;
//                }
//                E item = p.item;
//                if (item != null) {
//                    nextNode = p;
//                    nextItem = item;
//                    return x;
//                } else {
//                    // skip over nulls
//                    Node<E> next = succ(p);
//                    if (pred != null && next != null)
//                        pred.casNext(p, next);
//                    p = next;
//                }
//            }
//        }
//
//        public boolean hasNext() {
//            return nextNode != null;
//        }
//
//        public E next() {
//            if (nextNode == null) throw new NoSuchElementException();
//            return advance();
//        }
//
//        public void remove() {
//            Node<E> l = lastRet;
//            if (l == null) throw new IllegalStateException();
//            // rely on a future traversal to relink.
//            l.item = null;
//            lastRet = null;
//        }
//    }
//
//    private void writeObject(java.io.ObjectOutputStream s)
//            throws java.io.IOException {
//
//        // Write out any hidden stuff
//        s.defaultWriteObject();
//
//        // Write out all elements in the proper order.
//        for (Node<E> p = first(); p != null; p = succ(p)) {
//            Object item = p.item;
//            if (item != null)
//                s.writeObject(item);
//        }
//
//        // Use trailing null as sentinel
//        s.writeObject(null);
//    }
//
//    private void readObject(java.io.ObjectInputStream s)
//            throws java.io.IOException, ClassNotFoundException {
//        s.defaultReadObject();
//
//        // Read in elements until trailing null sentinel found
//        Node<E> h = null, t = null;
//        Object item;
//        while ((item = s.readObject()) != null) {
//            @SuppressWarnings("unchecked")
//            Node<E> newNode = new Node<E>((E) item);
//            if (h == null)
//                h = t = newNode;
//            else {
//                t.lazySetNext(newNode);
//                t = newNode;
//            }
//        }
//        if (h == null)
//            h = t = new Node<E>(null);
//        head = h;
//        tail = t;
//    }

    private static void checkNotNull(Object v) {
        if (v == null)
            throw new NullPointerException();
    }

    private boolean casTail(Node<E> cmp, Node<E> val) {
        return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
    }

    private boolean casHead(Node<E> cmp, Node<E> val) {
        return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
    }


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
