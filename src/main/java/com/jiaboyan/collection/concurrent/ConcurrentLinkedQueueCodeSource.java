//package com.jiaboyan.collection.concurrent;
//
//import java.util.*;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
///**
// * Created by jiaboyan on 2017/12/11.
// */
//public class ConcurrentLinkedQueueCodeSource {
//
//
//    public class ConcurrentLinkedQueue<E> extends AbstractQueue<E>
//            implements Queue<E>, java.io.Serializable {
//
//        private static final long serialVersionUID = 196745693267521676L;
//
//        //单向链表结点对象Node：
//        private static class Node<E> {
//            //链表中存储的元素：
//            volatile E item;
//
//            //指向下一个结点的引用：
//            volatile Node<E> next;
//
//            //使用Unsafe机制实现元素存储：
//            Node(E item) {
//                UNSAFE.putObject(this, itemOffset, item);
//            }
//
//            //此是Node类中的方法：替换本结点中的元素值；
//            //cmp是期望值，val是目标值。当本结点中元素的值等于cmp的时，则将其替换为val
//            boolean casItem(E cmp, E val) {
//                //使用itemOffset值快速找到item属性的所在地址，并使用CAS机制替换
//                return UNSAFE.compareAndSwapObject(this, itemOffset, cmp, val);
//            }
//
//            //使用CAS将自身的下一个结点引用指向自己
//            void lazySetNext(Node<E> val) {
//                UNSAFE.putOrderedObject(this, nextOffset, val);
//            }
//
//            //此是Node类中的方法：替换本结点中指向下一个结点的引用
//            //cmp是期望值，val是目标值。当本结点中的指向下一个结点的引用等于cmp时，则将其替换为指向val
//            boolean casNext(Node<E> cmp, Node<E> val) {
//                //使用nextOffset值快速找到next属性的所在地址，并使用CAS机制替换
//                return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
//            }
//
//            private static final sun.misc.Unsafe UNSAFE;
//            private static final long itemOffset;//
//            private static final long nextOffset;//
////         JVM的实现可以自由选择如何实现Java对象的“布局”，也就是在内存里Java对象的各个部分放在哪里，
////        包括对象的实例字段和一些元数据之类。sun.misc.Unsafe里关于对象字段访问的方法把对象布局抽象出来，
////        它提供了objectFieldOffset()方法用于获取某个字段相对Java对象的“起始地址”的偏移量，
////        也提供了getInt、getLong、getObject之类的方法可以使用前面获取的偏移量来访问某个Java对象的某个字段。
//            static {
//                try {
//                    UNSAFE = sun.misc.Unsafe.getUnsafe();
//                    Class k = Node.class;
//                    //结点中元素的起始地址偏移量：
//                    itemOffset = UNSAFE.objectFieldOffset
//                            (k.getDeclaredField("item"));
//                    //结点中指向下一个元素引用的起始地址偏移量：
//                    nextOffset = UNSAFE.objectFieldOffset
//                            (k.getDeclaredField("next"));
//                } catch (Exception e) {
//                    throw new Error(e);
//                }
//            }
//        }
//
//        //队列中头结点：
//        private transient volatile Node<E> head;
//
//        //队列中尾结点：
//        private transient volatile Node<E> tail;
//
//        //默认构造，指定头尾结点元素为null：
//        public ConcurrentLinkedQueue() {
//            head = tail = new Node<E>(null);
//        }
//
//        public ConcurrentLinkedQueue(Collection<? extends E> c) {
//            Node<E> h = null, t = null;
//            for (E e : c) {
//                checkNotNull(e);
//                Node<E> newNode = new Node<E>(e);
//                if (h == null)
//                    h = t = newNode;
//                else {
//                    t.lazySetNext(newNode);
//                    t = newNode;
//                }
//            }
//            if (h == null)
//                h = t = new Node<E>(null);
//            head = h;
//            tail = t;
//        }
//
//        //向队列尾部添加元素(底层调用offer):
//        public boolean add(E e) {
//            return offer(e);
//        }
//
//        //入队：向队列尾部添加元素:
//        public boolean offer(E e) {
//            //不能添加为空元素：抛异常
//            checkNotNull(e);
//            //创建新结点：
//            final Node<E> newNode = new Node<E>(e);
//            //p的类型为Node<E>(这块需要注意，不需要显式声明)
//            for (Node<E> t = tail, p = t;;) {
//                //获取链表中尾部结点的下一个结点：
//                Node<E> q = p.next;
//                //并判断下一个结点是否为null(正常情况下均为null)，为null则说明p是链表中的最后一个节点
//                if (q == null) {
//                    //将p节点中指向下一个结点的引用指向newNode节点（向链表中插入元素）
//                    if (p.casNext(null, newNode)) {
//                        //此处不理解：
//                        if (p != t) // hop two nodes at a time
//                            casTail(t, newNode);  // Failure is OK.
//                        return true;
//                    }//CAS插入失败，则进入下次循环
//                } else if (p == q){
//                    //p结点和p结点的下一个结点相同，则说明队列刚初始化完成 没有节点
//                    p = (t != (t = tail)) ? t : head;
//                } else {
//                    p = (p != t && t != (t = tail)) ? t : q;
//                }
//            }
//        }
//
//        //更新头结点：h为要更新的结点、p为要把h更新成的节点
//        final void updateHead(Node<E> h, Node<E> p) {
//            //h和p不相同，使用CAS指令使得head指向新的头结点
//            if (h != p && casHead(h, p))
//                //将h的下一个结点引用指向自己，利于GC操作
//                h.lazySetNext(h);
//        }
//
//        final Node<E> succ(Node<E> p) {
//            Node<E> next = p.next;
//            return (p == next) ? head : next;
//        }
//
//        //出队：移除队列头部的元素
//        public E poll() {
//            restartFromHead:
//            for (;;) {
//                //p的类型为Node<E>(这块需要注意，不需要显式声明)
//                for (Node<E> h = head, p = h, q;;) {
//                    //头部结点的元素：
//                    E item = p.item;
//                    //如果p节点的元素不为空，使用CAS设置p节点引用的元素为null
//                    if (item != null && p.casItem(item, null)) {
//                        if (p != h) // hop two nodes at a time
//                            //不理解
//                            updateHead(h, ((q = p.next) != null) ? q : p);
//                        return item;
//                    } else if ((q = p.next) == null) {
//                        //如果p的下一个结点为null，则说明队列中没有元素，更新头结点
//                        updateHead(h, p);
//                        return null;
//                    } else if (p == q) {
//                        continue restartFromHead;
//                    } else {
//                        p = q;
//                    }
//                }
//            }
//        }
//
//        //此方法是ConcurrentLinkedQueue中的方法，设置队列中尾结点的引用
//        private boolean casTail(Node<E> cmp, Node<E> val) {
//            return UNSAFE.compareAndSwapObject(this, tailOffset, cmp, val);
//        }
//
//        //此方法是ConcurrentLinkedQueue中的方法，设置队列中头结点的引用
//        private boolean casHead(Node<E> cmp, Node<E> val) {
//            return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
//        }
//
//        private static final sun.misc.Unsafe UNSAFE;
//        private static final long headOffset;
//        private static final long tailOffset;
//        //        JVM的实现可以自由选择如何实现Java对象的“布局”，也就是在内存里Java对象的各个部分放在哪里，
////        包括对象的实例字段和一些元数据之类。sun.misc.Unsafe里关于对象字段访问的方法把对象布局抽象出来，
////        它提供了objectFieldOffset()方法用于获取某个字段相对Java对象的“起始地址”的偏移量，
////        也提供了getInt、getLong、getObject之类的方法可以使用前面获取的偏移量来访问某个Java对象的某个字段。
//        static {
//            try {
//                UNSAFE = sun.misc.Unsafe.getUnsafe();
//                Class k =ConcurrentLinkedQueue.class;
//                headOffset = UNSAFE.objectFieldOffset
//                        (k.getDeclaredField("head"));
//                tailOffset = UNSAFE.objectFieldOffset
//                        (k.getDeclaredField("tail"));
//            } catch (Exception e) {
//                throw new Error(e);
//            }
//        }
//        //获取头部元素，不移除头结点：
//        public E peek() {
//            restartFromHead:
//            for (;;) {
//                for (Node<E> h = head, p = h, q;;) {
//                    E item = p.item;
//                    if (item != null || (q = p.next) == null) {
//                        updateHead(h, p);
//                        return item;
//                    }
//                    else if (p == q)
//                        continue restartFromHead;
//                    else
//                        p = q;
//                }
//            }
//        }
//
//        Node<E> first() {
//            restartFromHead:
//            for (;;) {
//                for (Node<E> h = head, p = h, q;;) {
//                    boolean hasItem = (p.item != null);
//                    if (hasItem || (q = p.next) == null) {
//                        updateHead(h, p);
//                        return hasItem ? p : null;
//                    }
//                    else if (p == q)
//                        continue restartFromHead;
//                    else
//                        p = q;
//                }
//            }
//        }
//
//        //判断队列是否为空：
//        public boolean isEmpty() {
//            return first() == null;
//        }
//
//        //获取队列的长度：
//        public int size() {
//            int count = 0;
//            //for循环，遍历整个队列链表，获取count。队列太大的情况下少用
//            for (Node<E> p = first(); p != null; p = succ(p))
//                //first()获取头结点，succ()获取p节点的下一个结点
//                if (p.item != null)
//                    // Collection.size() spec says to max out
//                    if (++count == Integer.MAX_VALUE)
//                        break;
//            return count;
//        }
//
//        public boolean contains(Object o) {
//            if (o == null) return false;
//            for (Node<E> p = first(); p != null; p = succ(p)) {
//                E item = p.item;
//                if (item != null && o.equals(item))
//                    return true;
//            }
//            return false;
//        }
//
//        public boolean remove(Object o) {
//            if (o == null) return false;
//            Node<E> pred = null;
//            for (Node<E> p = first(); p != null; p = succ(p)) {
//                E item = p.item;
//                if (item != null &&
//                        o.equals(item) &&
//                        p.casItem(item, null)) {
//                    Node<E> next = succ(p);
//                    if (pred != null && next != null)
//                        pred.casNext(p, next);
//                    return true;
//                }
//                pred = p;
//            }
//            return false;
//        }
//
//        public boolean addAll(Collection<? extends E> c) {
//            if (c == this)
//                // As historically specified in AbstractQueue#addAll
//                throw new IllegalArgumentException();
//
//            // Copy c into a private chain of Nodes
//            Node<E> beginningOfTheEnd = null, last = null;
//            for (E e : c) {
//                checkNotNull(e);
//                Node<E> newNode = new Node<E>(e);
//                if (beginningOfTheEnd == null)
//                    beginningOfTheEnd = last = newNode;
//                else {
//                    last.lazySetNext(newNode);
//                    last = newNode;
//                }
//            }
//            if (beginningOfTheEnd == null)
//                return false;
//
//            // Atomically append the chain at the tail of this collection
//            for (Node<E> t = tail, p = t;;) {
//                Node<E> q = p.next;
//                if (q == null) {
//                    // p is last node
//                    if (p.casNext(null, beginningOfTheEnd)) {
//                        // Successful CAS is the linearization point
//                        // for all elements to be added to this queue.
//                        if (!casTail(t, last)) {
//                            // Try a little harder to update tail,
//                            // since we may be adding many elements.
//                            t = tail;
//                            if (last.next == null)
//                                casTail(t, last);
//                        }
//                        return true;
//                    }
//                    // Lost CAS race to another thread; re-read next
//                }
//                else if (p == q)
//                    // We have fallen off list.  If tail is unchanged, it
//                    // will also be off-list, in which case we need to
//                    // jump to head, from which all live nodes are always
//                    // reachable.  Else the new tail is a better bet.
//                    p = (t != (t = tail)) ? t : head;
//                else
//                    // Check for tail updates after two hops.
//                    p = (p != t && t != (t = tail)) ? t : q;
//            }
//        }
//
//        public Object[] toArray() {
//            // Use ArrayList to deal with resizing.
//            ArrayList<E> al = new ArrayList<E>();
//            for (Node<E> p = first(); p != null; p = succ(p)) {
//                E item = p.item;
//                if (item != null)
//                    al.add(item);
//            }
//            return al.toArray();
//        }
//
//        @SuppressWarnings("unchecked")
//        public <T> T[] toArray(T[] a) {
//            // try to use sent-in array
//            int k = 0;
//            Node<E> p;
//            for (p = first(); p != null && k < a.length; p = succ(p)) {
//                E item = p.item;
//                if (item != null)
//                    a[k++] = (T)item;
//            }
//            if (p == null) {
//                if (k < a.length)
//                    a[k] = null;
//                return a;
//            }
//
//            // If won't fit, use ArrayList version
//            ArrayList<E> al = new ArrayList<E>();
//            for (Node<E> q = first(); q != null; q = succ(q)) {
//                E item = q.item;
//                if (item != null)
//                    al.add(item);
//            }
//            return al.toArray(a);
//        }
//
//        public Iterator<E> iterator() {
//            return new Itr();
//        }
//
//        private class Itr implements Iterator<E> {
//            /**
//             * Next node to return item for.
//             */
//            private Node<E> nextNode;
//
//            /**
//             * nextItem holds on to item fields because once we claim
//             * that an element exists in hasNext(), we must return it in
//             * the following next() call even if it was in the process of
//             * being removed when hasNext() was called.
//             */
//            private E nextItem;
//
//            /**
//             * Node of the last returned item, to support remove.
//             */
//            private Node<E> lastRet;
//
//            Itr() {
//                advance();
//            }
//
//            /**
//             * Moves to next valid node and returns item to return for
//             * next(), or null if no such.
//             */
//            private E advance() {
//                lastRet = nextNode;
//                E x = nextItem;
//
//                Node<E> pred, p;
//                if (nextNode == null) {
//                    p = first();
//                    pred = null;
//                } else {
//                    pred = nextNode;
//                    p = succ(nextNode);
//                }
//
//                for (;;) {
//                    if (p == null) {
//                        nextNode = null;
//                        nextItem = null;
//                        return x;
//                    }
//                    E item = p.item;
//                    if (item != null) {
//                        nextNode = p;
//                        nextItem = item;
//                        return x;
//                    } else {
//                        // skip over nulls
//                        Node<E> next = succ(p);
//                        if (pred != null && next != null)
//                            pred.casNext(p, next);
//                        p = next;
//                    }
//                }
//            }
//
//            public boolean hasNext() {
//                return nextNode != null;
//            }
//
//            public E next() {
//                if (nextNode == null) throw new NoSuchElementException();
//                return advance();
//            }
//
//            public void remove() {
//                Node<E> l = lastRet;
//                if (l == null) throw new IllegalStateException();
//                l.item = null;
//                lastRet = null;
//            }
//        }
//
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException {
//            s.defaultWriteObject();
//            for (Node<E> p = first(); p != null; p = succ(p)) {
//                Object item = p.item;
//                if (item != null)
//                    s.writeObject(item);
//            }
//            s.writeObject(null);
//        }
//
//        private void readObject(java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//            s.defaultReadObject();
//            Node<E> h = null, t = null;
//            Object item;
//            while ((item = s.readObject()) != null) {
//                @SuppressWarnings("unchecked")
//                Node<E> newNode = new Node<E>((E) item);
//                if (h == null)
//                    h = t = newNode;
//                else {
//                    t.lazySetNext(newNode);
//                    t = newNode;
//                }
//            }
//            if (h == null)
//                h = t = new Node<E>(null);
//            head = h;
//            tail = t;
//        }
//
//        private static void checkNotNull(Object v) {
//            if (v == null)
//                throw new NullPointerException();
//        }
//    }
//
//
//}
