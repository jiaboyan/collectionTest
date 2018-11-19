//package com.jiaboyan.collection.concurrent;
//
//import java.util.AbstractQueue;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.NoSuchElementException;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Created by jiaboyan on 2017/12/7.
// */
//public class LinkedBlockingQueueCodeSource {
//
//
//    public class LinkedBlockingQueue<E> extends AbstractQueue<E>
//            implements BlockingQueue<E>, java.io.Serializable {
//        private static final long serialVersionUID = -6903933977591709194L;
//
//        //队列存储元素的结点(链表结点):
//        static class Node<E> {
//            //队列元素：
//            E item;
//
//            //链表中指向的下一个结点
//            Node<E> next;
//
//            //结点构造：
//            Node(E x) { item = x; }
//        }
//
//        //队列容量大小，默认为Integer.MAX_VALUE
//        private final int capacity;
//
//        //队列中元素个数：(与ArrayBlockingQueue的不同)
//        //出队和入队是两把锁
//        private final AtomicInteger count = new AtomicInteger(0);
//
//        //队列--头结点
//        private transient Node<E> head;
//
//        //队列--尾结点
//        private transient Node<E> last;
//
//        //与ArrayBlockingQueue的不同,两把锁
//        //读取锁
//        private final ReentrantLock takeLock = new ReentrantLock();
//
//        //出队等待条件
//        private final Condition notEmpty = takeLock.newCondition();
//
//        //插入锁
//        private final ReentrantLock putLock = new ReentrantLock();
//
//        //入队等待条件
//        private final Condition notFull = putLock.newCondition();
//
//        private void signalNotEmpty() {
//            final ReentrantLock takeLock = this.takeLock;
//            takeLock.lock();
//            try {
//                notEmpty.signal();
//            } finally {
//                takeLock.unlock();
//            }
//        }
//        private void signalNotFull() {
//            final ReentrantLock putLock = this.putLock;
//            putLock.lock();
//            try {
//                notFull.signal();
//            } finally {
//                putLock.unlock();
//            }
//        }
//
//        //插入到链表的尾部
//        private void enqueue(Node<E> node) {
//            //将当前末尾节点的下一个结点设置成node，并将最后结点设置为node
//            last = last.next = node;
//        }
//
//        //移除队头结点：始终保持head结点元素为null（画个图）
//        private E dequeue() {
//            //获取头部结点：
//            Node<E> h = head;
//            //将头结点指向的下一个结点，赋值给first
//            Node<E> first = h.next;
//            //将头结点的指向下一个结点的引用指向自己
//            h.next = h; // help GC
//            //将下一个结点置为head：
//            head = first;
//            //获取此时头结点的元素：
//            E x = first.item;
//            //将此时头结点元素置为null ：
//            first.item = null;
//            //返回：
//            return x;
//        }
//
//        void fullyLock() {
//            putLock.lock();
//            takeLock.lock();
//        }
//
//        void fullyUnlock() {
//            takeLock.unlock();
//            putLock.unlock();
//        }
//
//        //默认构造函数：
//        public LinkedBlockingQueue() {
//            //默认队列长度为Integer.MAX_VALUE
//            this(Integer.MAX_VALUE);
//        }
//
//        //指定队列长度的构造函数：
//        public LinkedBlockingQueue(int capacity) {
//            //初始化链表长度不能为0
//            if (capacity <= 0) throw new IllegalArgumentException();
//            this.capacity = capacity;
//            //设置头尾结点，元素为null
//            last = head = new Node<E>(null);
//        }
//
//        public LinkedBlockingQueue(Collection<? extends E> c) {
//            this(Integer.MAX_VALUE);
//            final ReentrantLock putLock = this.putLock;
//            putLock.lock();
//            try {
//                int n = 0;
//                for (E e : c) {
//                    if (e == null)
//                        throw new NullPointerException();
//                    if (n == capacity)
//                        throw new IllegalStateException("Queue full");
//                    enqueue(new java.util.concurrent.LinkedBlockingQueue.Node<E>(e));
//                    ++n;
//                }
//                count.set(n);
//            } finally {
//                putLock.unlock();
//            }
//        }
//
//        //获取队列元素个数:
//        public int size() {
//            return count.get();
//        }
//
//        public int remainingCapacity() {
//            return capacity - count.get();
//        }
//
//        //向队列尾部插入元素：队列满了线程等待
//        public void put(E e) throws InterruptedException {
//            //不能插入为null元素：
//            if (e == null) throw new NullPointerException();
//            int c = -1;
//            //创建元素结点：
//            Node<E> node = new Node(e);
//            final ReentrantLock putLock = this.putLock;
//            final AtomicInteger count = this.count;
//            //加插入锁，保证数据的一致性：
//            putLock.lockInterruptibly();
//            try {
//                //当队列元素个数==链表长度
//                while (count.get() == capacity) {
//                    //插入线程等待：
//                    notFull.await();
//                }
//                //插入元素：
//                enqueue(node);
//                //队列元素增加：count+1,但返回+1前的count值：
//                c = count.getAndIncrement();
//                //容量还没满，唤醒生产者线程
//                // (例如链表长度为5，此时第五个元素已经插入，c=4，+1=5，所以超过了队列容量，则不会再唤醒生产者线程)
//                if (c + 1 < capacity)
//                    notFull.signal();
//            } finally {
//                //释放锁：
//                putLock.unlock();
//            }
//            //当c=0时，即意味着之前的队列是空队列,消费者线程都处于等待状态，需要被唤醒进行消费
//            if (c == 0)
//                //唤醒消费者线程：
//                signalNotEmpty();
//        }
//
//        public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
//            if (e == null) throw new NullPointerException();
//            long nanos = unit.toNanos(timeout);
//            int c = -1;
//            final ReentrantLock putLock = this.putLock;
//            final AtomicInteger count = this.count;
//            putLock.lockInterruptibly();
//            try {
//                while (count.get() == capacity) {
//                    if (nanos <= 0)
//                        return false;
//                    nanos = notFull.awaitNanos(nanos);
//                }
//                enqueue(new Node<E>(e));
//                c = count.getAndIncrement();
//                if (c + 1 < capacity)
//                    notFull.signal();
//            } finally {
//                putLock.unlock();
//            }
//            if (c == 0)
//                signalNotEmpty();
//            return true;
//        }
//
//        //向队列尾部插入元素：返回true/false
//        public boolean offer(E e) {
//            //插入元素不能为空
//            if (e == null) throw new NullPointerException();
//            final AtomicInteger count = this.count;
//            //如果队列元素==链表长度，则直接返回false
//            if (count.get() == capacity)
//                return false;
//            int c = -1;
//            //创建元素结点对象：
//            Node<E> node = new Node(e);
//            final ReentrantLock putLock = this.putLock;
//            //加锁，保证数据一致性
//            putLock.lock();
//            try {
//                //队列元素个数 小于 链表长度
//                if (count.get() < capacity) {
//                    //向队列中插入元素：
//                    enqueue(node);
//                    //增加队列元素个数：
//                    c = count.getAndIncrement();
//                    //容量还没满，唤醒生产者线程：
//                    if (c + 1 < capacity)
//                        notFull.signal();
//                }
//            } finally {
//                //释放锁：
//                putLock.unlock();
//            }
//            //此时，代表队列中还有一条数据，可以进行消费，唤醒消费者线程
//            if (c == 0)
//                signalNotEmpty();
//            return c >= 0;
//        }
//
//        //从队列头部获取元素，并返回。队列为null，则一直等待
//        public E take() throws InterruptedException {
//            E x;
//            int c = -1;
//            final AtomicInteger count = this.count;
//            final ReentrantLock takeLock = this.takeLock;
//            //设置读取锁：
//            takeLock.lockInterruptibly();
//            try {
//                //如果此时队列为空，则获取线程等待
//                while (count.get() == 0) {
//                    notEmpty.await();
//                }
//                //从队列头部获取元素：
//                x = dequeue();
//                //减少队列元素-1,返回count减少前的值；
//                c = count.getAndDecrement();
//                //队列中还有可以消费的元素，唤醒其他消费者线程
//                if (c > 1)
//                    notEmpty.signal();
//            } finally {
//                //释放锁：
//                takeLock.unlock();
//            }
//            //队列中出现了空余元素，唤醒生产者进行生产。
//            // (链表长度为5，队列在执行take前有5个元素，执行到此处时候有4个元素了，但是c的值还是5，所以会进入到if中来)
//            if (c == capacity)
//                signalNotFull();
//            return x;
//        }
//
//        public E poll(long timeout, TimeUnit unit) throws InterruptedException {
//            E x = null;
//            int c = -1;
//            long nanos = unit.toNanos(timeout);
//            final AtomicInteger count = this.count;
//            final ReentrantLock takeLock = this.takeLock;
//            takeLock.lockInterruptibly();
//            try {
//                while (count.get() == 0) {
//                    if (nanos <= 0)
//                        return null;
//                    nanos = notEmpty.awaitNanos(nanos);
//                }
//                x = dequeue();
//                c = count.getAndDecrement();
//                if (c > 1)
//                    notEmpty.signal();
//            } finally {
//                takeLock.unlock();
//            }
//            if (c == capacity)
//                signalNotFull();
//            return x;
//        }
//
//        //获取头部元素，并返回。队列为空，则直接返回null
//        public E poll() {
//            final AtomicInteger count = this.count;
//            //如果队列中还没有元素，则直接返回 null
//            if (count.get() == 0)
//                return null;
//            E x = null;
//            int c = -1;
//            final ReentrantLock takeLock = this.takeLock;
//            //加锁，保证数据的安全
//            takeLock.lock();
//            try {
//                //此时在判断，队列元素是否大于0
//                if (count.get() > 0) {
//                    //移除队头元素
//                    x = dequeue();
//                    //减少队列元素个数
//                    c = count.getAndDecrement();
//                    //此时队列中，还有1个元素，唤醒消费者线程继续执行
//                    if (c > 1)
//                        notEmpty.signal();
//                }
//            } finally {
//                //释放锁：
//                takeLock.unlock();
//            }
//            //队列中出现了空余元素，唤醒生产者进行生产。
//            // (链表长度为5，队列在执行take前有5个元素，执行到此处时候有4个元素了，但是c的值还是5，所以会进入到if中来)
//            if (c == capacity)
//                signalNotFull();
//            return x;
//        }
//
//        public E peek() {
//            if (count.get() == 0)
//                return null;
//            final ReentrantLock takeLock = this.takeLock;
//            takeLock.lock();
//            try {
//                Node<E> first = head.next;
//                if (first == null)
//                    return null;
//                else
//                    return first.item;
//            } finally {
//                takeLock.unlock();
//            }
//        }
//
//        void unlink(Node<E> p, Node<E> trail) {
//
//            p.item = null;
//            trail.next = p.next;
//            if (last == p)
//                last = trail;
//            if (count.getAndDecrement() == capacity)
//                notFull.signal();
//        }
//
//
//        public boolean remove(Object o) {
//            if (o == null) return false;
//            fullyLock();
//            try {
//                for (Node<E> trail = head, p = trail.next;
//                     p != null;
//                     trail = p, p = p.next) {
//                    if (o.equals(p.item)) {
//                        unlink(p, trail);
//                        return true;
//                    }
//                }
//                return false;
//            } finally {
//                fullyUnlock();
//            }
//        }
//
//
//        public boolean contains(Object o) {
//            if (o == null) return false;
//            fullyLock();
//            try {
//                for (Node<E> p = head.next; p != null; p = p.next)
//                    if (o.equals(p.item))
//                        return true;
//                return false;
//            } finally {
//                fullyUnlock();
//            }
//        }
//
//
//        public Object[] toArray() {
//            fullyLock();
//            try {
//                int size = count.get();
//                Object[] a = new Object[size];
//                int k = 0;
//                for (Node<E> p = head.next; p != null; p = p.next)
//                    a[k++] = p.item;
//                return a;
//            } finally {
//                fullyUnlock();
//            }
//        }
//
//
//        @SuppressWarnings("unchecked")
//        public <T> T[] toArray(T[] a) {
//            fullyLock();
//            try {
//                int size = count.get();
//                if (a.length < size)
//                    a = (T[])java.lang.reflect.Array.newInstance
//                            (a.getClass().getComponentType(), size);
//
//                int k = 0;
//                for (Node<E> p = head.next; p != null; p = p.next)
//                    a[k++] = (T)p.item;
//                if (a.length > k)
//                    a[k] = null;
//                return a;
//            } finally {
//                fullyUnlock();
//            }
//        }
//
//        public String toString() {
//            fullyLock();
//            try {
//                Node<E> p = head.next;
//                if (p == null)
//                    return "[]";
//
//                StringBuilder sb = new StringBuilder();
//                sb.append('[');
//                for (;;) {
//                    E e = p.item;
//                    sb.append(e == this ? "(this Collection)" : e);
//                    p = p.next;
//                    if (p == null)
//                        return sb.append(']').toString();
//                    sb.append(',').append(' ');
//                }
//            } finally {
//                fullyUnlock();
//            }
//        }
//        public void clear() {
//            fullyLock();
//            try {
//                for (Node<E> p, h = head; (p = h.next) != null; h = p) {
//                    h.next = h;
//                    p.item = null;
//                }
//                head = last;
//                // assert head.item == null && head.next == null;
//                if (count.getAndSet(0) == capacity)
//                    notFull.signal();
//            } finally {
//                fullyUnlock();
//            }
//        }
//
//        public int drainTo(Collection<? super E> c) {
//            return drainTo(c, Integer.MAX_VALUE);
//        }
//
//        public int drainTo(Collection<? super E> c, int maxElements) {
//            if (c == null)
//                throw new NullPointerException();
//            if (c == this)
//                throw new IllegalArgumentException();
//            boolean signalNotFull = false;
//            final ReentrantLock takeLock = this.takeLock;
//            takeLock.lock();
//            try {
//                int n = Math.min(maxElements, count.get());
//                Node<E> h = head;
//                int i = 0;
//                try {
//                    while (i < n) {
//                        Node<E> p = h.next;
//                        c.add(p.item);
//                        p.item = null;
//                        h.next = h;
//                        h = p;
//                        ++i;
//                    }
//                    return n;
//                } finally {
//                    if (i > 0) {
//                        head = h;
//                        signalNotFull = (count.getAndAdd(-i) == capacity);
//                    }
//                }
//            } finally {
//                takeLock.unlock();
//                if (signalNotFull)
//                    signalNotFull();
//            }
//        }
//        public Iterator<E> iterator() {
//            return new java.util.concurrent.LinkedBlockingQueue.Itr();
//        }
//        private class Itr implements Iterator<E> {
//            private java.util.concurrent.LinkedBlockingQueue.Node<E> current;
//            private java.util.concurrent.LinkedBlockingQueue.Node<E> lastRet;
//            private E currentElement;
//            Itr() {
//                fullyLock();
//                try {
//                    current = head.next;
//                    if (current != null)
//                        currentElement = current.item;
//                } finally {
//                    fullyUnlock();
//                }
//            }
//            public boolean hasNext() {
//                return current != null;
//            }
//            private Node<E> nextNode(Node<E> p) {
//                for (;;) {
//                    Node<E> s = p.next;
//                    if (s == p)
//                        return head.next;
//                    if (s == null || s.item != null)
//                        return s;
//                    p = s;
//                }
//            }
//            public E next() {
//                fullyLock();
//                try {
//                    if (current == null)
//                        throw new NoSuchElementException();
//                    E x = currentElement;
//                    lastRet = current;
//                    current = nextNode(current);
//                    currentElement = (current == null) ? null : current.item;
//                    return x;
//                } finally {
//                    fullyUnlock();
//                }
//            }
//            public void remove() {
//                if (lastRet == null)
//                    throw new IllegalStateException();
//                fullyLock();
//                try {
//                    Node<E> node = lastRet;
//                    lastRet = null;
//                    for (Node<E> trail = head, p = trail.next;
//                         p != null;
//                         trail = p, p = p.next) {
//                        if (p == node) {
//                            unlink(p, trail);
//                            break;
//                        }
//                    }
//                } finally {
//                    fullyUnlock();
//                }
//            }
//        }
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException {
//            fullyLock();
//            try {
//                s.defaultWriteObject();
//                for (java.util.concurrent.LinkedBlockingQueue.Node<E> p = head.next; p != null; p = p.next)
//                    s.writeObject(p.item);
//                s.writeObject(null);
//            } finally {
//                fullyUnlock();
//            }
//        }
//        private void readObject(java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//            s.defaultReadObject();
//            count.set(0);
//            last = head = new java.util.concurrent.LinkedBlockingQueue.Node<E>(null);
//            for (;;) {
//                @SuppressWarnings("unchecked")
//                E item = (E)s.readObject();
//                if (item == null)
//                    break;
//                add(item);
//            }
//        }
//    }
//
//
//
//
//
//}
