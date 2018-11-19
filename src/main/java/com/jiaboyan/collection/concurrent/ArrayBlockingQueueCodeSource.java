//package com.jiaboyan.collection.concurrent;
//
//import java.util.AbstractQueue;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.NoSuchElementException;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.locks.Condition;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Created by jiaboyan on 2017/11/24.
// */
//public class ArrayBlockingQueueCodeSource {
//
//
//    public class ArrayBlockingQueue<E> extends AbstractQueue<E>
//            implements BlockingQueue<E>, java.io.Serializable {
//
//        private static final long serialVersionUID = -817911632652898426L;
//
//        //队列实现：数组
//        final Object[] items;
//
//        //当读取元素时数组的下标(这里称为读下标)
//        int takeIndex;
//
//        //添加元素时数组的下标 (这里称为写小标)
//        int putIndex;
//
//        //队列中元素个数：
//        int count;
//
//        //锁：
//        final ReentrantLock lock;
//
//        // 控制take操作时是否让线程等待
//        private final Condition notEmpty;
//
//        //控制put操作时是否让线程等待
//        private final Condition notFull;
//
//        //增加 putIndex takeIndex的值，如果跟数组长度一致，则返回0
//        final int inc(int i) {
//            return (++i == items.length) ? 0 : i;
//        }
//
//        final int dec(int i) {
//            return ((i == 0) ? items.length : i) - 1;
//        }
//
//        @SuppressWarnings("unchecked")
//        static <E> E cast(Object item) {
//            return (E) item;
//        }
//
//        final E itemAt(int i) {
//            return this.<E>cast(items[i]);
//        }
//        private static void checkNotNull(Object v) {
//            if (v == null)
//                throw new NullPointerException();
//        }
//
//        //插入元素到队尾，调整putIndex，唤起等待的获取线程
//        private void insert(E x) {
//            //向数组中插入元素
//            items[putIndex] = x;
//            //增加putIndex的值
//            putIndex = inc(putIndex);
//            //增加队列元素个数：
//            ++count;
//            notEmpty.signal();
//        }
//
//        //移除并返回队列头部元素，调整takeIndex值，唤起等待的插入线程
//        private E extract() {
//            final Object[] items = this.items;
//            //获取takeIndex下的元素：
//            E x = this.<E>cast(items[takeIndex]);
//            //将此数组角标下的元素置为null
//            items[takeIndex] = null;
//            //增加takeIndex的值：
//            takeIndex = inc(takeIndex);
//            //减少队列中元素的个数：
//            --count;
//            notFull.signal();
//            return x;
//        }
//
//        void removeAt(int i) {
//            final Object[] items = this.items;
//            if (i == takeIndex) {
//                items[takeIndex] = null;
//                takeIndex = inc(takeIndex);
//            } else {
//                for (;;) {
//                    int nexti = inc(i);
//                    if (nexti != putIndex) {
//                        items[i] = items[nexti];
//                        i = nexti;
//                    } else {
//                        items[i] = null;
//                        putIndex = i;
//                        break;
//                    }
//                }
//            }
//            --count;
//            notFull.signal();
//        }
//
//        //初始化队列容量构造：
//        public ArrayBlockingQueue(int capacity) {
//            this(capacity, false);
//        }
//
//        //带初始容量大小和公平锁队列(公平锁通过ReentrantLock实现)：
//        public ArrayBlockingQueue(int capacity, boolean fair) {
//            if (capacity <= 0)
//                throw new IllegalArgumentException();
//            this.items = new Object[capacity];
//            lock = new ReentrantLock(fair);
//            notEmpty = lock.newCondition();
//            notFull =  lock.newCondition();
//        }
//
//
//        public ArrayBlockingQueue(int capacity, boolean fair,
//                                  Collection<? extends E> c) {
//            this(capacity, fair);
//
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                int i = 0;
//                try {
//                    for (E e : c) {
//                        checkNotNull(e);
//                        items[i++] = e;
//                    }
//                } catch (ArrayIndexOutOfBoundsException ex) {
//                    throw new IllegalArgumentException();
//                }
//                count = i;
//                putIndex = (i == capacity) ? 0 : i;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        //向队列尾部添加元素，队列满了抛出异常，插入为null元素抛出异常
//        public boolean add(E e) {
//            return super.add(e);
//        }
//
//        //向队列尾部添加元素，队列满了返回false，插入为null元素抛出异常
//        public boolean offer(E e) {
//            checkNotNull(e);
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                //队列中元素 == 数组长度(队列满了),返回false
//                if (count == items.length)
//                    return false;
//                else {
//                    insert(e);
//                    return true;
//                }
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        //向队列尾部添加元素，如果队列满了，则等待
//        public void put(E e) throws InterruptedException {
//            checkNotNull(e);
//            final ReentrantLock lock = this.lock;
//            lock.lockInterruptibly();
//            try {
//                //队列中元素 == 数组长度(队列满了),线程等待
//                while (count == items.length)
//                    notFull.await();
//                //添加队列元素，增加putIndex的值
//                insert(e);
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        //向队列尾部添加元素，
//        // 如果队列满了，可以设置超时等待时间，如果超过指定时间队列还是满的，则返回false
//        public boolean offer(E e, long timeout, TimeUnit unit)
//                throws InterruptedException {
//
//            checkNotNull(e);
//            long nanos = unit.toNanos(timeout);
//            final ReentrantLock lock = this.lock;
//            lock.lockInterruptibly();
//            try {
//                while (count == items.length) {
//                    if (nanos <= 0)
//                        return false;
//                    nanos = notFull.awaitNanos(nanos);
//                }
//                insert(e);
//                return true;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        //获取队列头部元素，如果队列为空，则返回null.不为空。
//        // 则返回队列头部，并从队列中删除。
//        public E poll() {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                return (count == 0) ? null : extract();
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        //返回队列的头部元素，并从队列中删除。如果队列为空，则等待
//        public E take() throws InterruptedException {
//            final ReentrantLock lock = this.lock;
//            lock.lockInterruptibly();
//            try {
//                //如果队列为空，则进行等待
//                while (count == 0)
//                    notEmpty.await();
//
//                //获取头部元素：
//                return extract();
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        //获取队列头部元素，如果队列为空，则设置线程等待时间，超过指定时间，还为空，则返回null。
//        public E poll(long timeout, TimeUnit unit) throws InterruptedException {
//            long nanos = unit.toNanos(timeout);
//            final ReentrantLock lock = this.lock;
//            lock.lockInterruptibly();
//            try {
//                while (count == 0) {
//                    if (nanos <= 0)
//                        return null;
//                    nanos = notEmpty.awaitNanos(nanos);
//                }
//                return extract();
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public E peek() {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                return (count == 0) ? null : itemAt(takeIndex);
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public int size() {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                return count;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public int remainingCapacity() {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                return items.length - count;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean remove(Object o) {
//            if (o == null) return false;
//            final Object[] items = this.items;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                for (int i = takeIndex, k = count; k > 0; i = inc(i), k--) {
//                    if (o.equals(items[i])) {
//                        removeAt(i);
//                        return true;
//                    }
//                }
//                return false;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public boolean contains(Object o) {
//            if (o == null) return false;
//            final Object[] items = this.items;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                for (int i = takeIndex, k = count; k > 0; i = inc(i), k--)
//                    if (o.equals(items[i]))
//                        return true;
//                return false;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public Object[] toArray() {
//            final Object[] items = this.items;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                final int count = this.count;
//                Object[] a = new Object[count];
//                for (int i = takeIndex, k = 0; k < count; i = inc(i), k++)
//                    a[k] = items[i];
//                return a;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public <T> T[] toArray(T[] a) {
//            final Object[] items = this.items;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                final int count = this.count;
//                final int len = a.length;
//                if (len < count)
//                    a = (T[])java.lang.reflect.Array.newInstance(
//                            a.getClass().getComponentType(), count);
//                for (int i = takeIndex, k = 0; k < count; i = inc(i), k++)
//                    a[k] = (T) items[i];
//                if (len > count)
//                    a[count] = null;
//                return a;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public String toString() {
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                int k = count;
//                if (k == 0)
//                    return "[]";
//
//                StringBuilder sb = new StringBuilder();
//                sb.append('[');
//                for (int i = takeIndex; ; i = inc(i)) {
//                    Object e = items[i];
//                    sb.append(e == this ? "(this Collection)" : e);
//                    if (--k == 0)
//                        return sb.append(']').toString();
//                    sb.append(',').append(' ');
//                }
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public void clear() {
//            final Object[] items = this.items;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                for (int i = takeIndex, k = count; k > 0; i = inc(i), k--)
//                    items[i] = null;
//                count = 0;
//                putIndex = 0;
//                takeIndex = 0;
//                notFull.signalAll();
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public int drainTo(Collection<? super E> c) {
//            checkNotNull(c);
//            if (c == this)
//                throw new IllegalArgumentException();
//            final Object[] items = this.items;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                int i = takeIndex;
//                int n = 0;
//                int max = count;
//                while (n < max) {
//                    c.add(this.<E>cast(items[i]));
//                    items[i] = null;
//                    i = inc(i);
//                    ++n;
//                }
//                if (n > 0) {
//                    count = 0;
//                    putIndex = 0;
//                    takeIndex = 0;
//                    notFull.signalAll();
//                }
//                return n;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public int drainTo(Collection<? super E> c, int maxElements) {
//            checkNotNull(c);
//            if (c == this)
//                throw new IllegalArgumentException();
//            if (maxElements <= 0)
//                return 0;
//            final Object[] items = this.items;
//            final ReentrantLock lock = this.lock;
//            lock.lock();
//            try {
//                int i = takeIndex;
//                int n = 0;
//                int max = (maxElements < count) ? maxElements : count;
//                while (n < max) {
//                    c.add(this.<E>cast(items[i]));
//                    items[i] = null;
//                    i = inc(i);
//                    ++n;
//                }
//                if (n > 0) {
//                    count -= n;
//                    takeIndex = i;
//                    notFull.signalAll();
//                }
//                return n;
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        public Iterator<E> iterator() {
//            return new java.util.concurrent.ArrayBlockingQueue.Itr();
//        }
//
//        private class Itr implements Iterator<E> {
//            private int remaining; // Number of elements yet to be returned
//            private int nextIndex; // Index of element to be returned by next
//            private E nextItem;    // Element to be returned by next call to next
//            private E lastItem;    // Element returned by last call to next
//            private int lastRet;   // Index of last element returned, or -1 if none
//
//            Itr() {
//                final ReentrantLock lock = java.util.concurrent.ArrayBlockingQueue.this.lock;
//                lock.lock();
//                try {
//                    lastRet = -1;
//                    if ((remaining = count) > 0)
//                        nextItem = itemAt(nextIndex = takeIndex);
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public boolean hasNext() {
//                return remaining > 0;
//            }
//
//            public E next() {
//                final ReentrantLock lock = java.util.concurrent.ArrayBlockingQueue.this.lock;
//                lock.lock();
//                try {
//                    if (remaining <= 0)
//                        throw new NoSuchElementException();
//                    lastRet = nextIndex;
//                    E x = itemAt(nextIndex);  // check for fresher value
//                    if (x == null) {
//                        x = nextItem;         // we are forced to report old value
//                        lastItem = null;      // but ensure remove fails
//                    }
//                    else
//                        lastItem = x;
//                    while (--remaining > 0 && // skip over nulls
//                            (nextItem = itemAt(nextIndex = inc(nextIndex))) == null)
//                        ;
//                    return x;
//                } finally {
//                    lock.unlock();
//                }
//            }
//
//            public void remove() {
//                final ReentrantLock lock = java.util.concurrent.ArrayBlockingQueue.this.lock;
//                lock.lock();
//                try {
//                    int i = lastRet;
//                    if (i == -1)
//                        throw new IllegalStateException();
//                    lastRet = -1;
//                    E x = lastItem;
//                    lastItem = null;
//                    // only remove if item still at index
//                    if (x != null && x == items[i]) {
//                        boolean removingHead = (i == takeIndex);
//                        removeAt(i);
//                        if (!removingHead)
//                            nextIndex = dec(nextIndex);
//                    }
//                } finally {
//                    lock.unlock();
//                }
//            }
//        }
//    }
//}
