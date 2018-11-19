//package com.jiaboyan.collection.queue;
//
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/10/15.
// */
//public class PriorityQueueCodeSource {
//
//    public class PriorityQueue<E> extends AbstractQueue<E>
//            implements java.io.Serializable {
//
//        private static final long serialVersionUID = -7720805057305804111L;
//
//        //默认初始化数组大小：
//        private static final int DEFAULT_INITIAL_CAPACITY = 11;
//
//        //队列底层数据结构：数组
//        private transient Object[] queue;
//
//        //队列长度：
//        private int size = 0;
//
//        //实现元素排序的比较器：
//        private final Comparator<? super E> comparator;
//
//        //对queue的操作次数：
//        private transient int modCount = 0;
//
//        //默认构造函数：
//        public PriorityQueue() {
//            this(DEFAULT_INITIAL_CAPACITY, null);
//        }
//
//        //可设置队列长度的构造函数：
//        public PriorityQueue(int initialCapacity) {
//            this(initialCapacity, null);
//        }
//
//        //可设置队列长度、元素比较器的构造函数：
//        public PriorityQueue(int initialCapacity,
//                             Comparator<? super E> comparator) {
//            if (initialCapacity < 1)
//                throw new IllegalArgumentException();
//            this.queue = new Object[initialCapacity];
//            this.comparator = comparator;
//        }
//
//        @SuppressWarnings("unchecked")
//        public PriorityQueue(Collection<? extends E> c) {
//            if (c instanceof SortedSet<?>) {
//                SortedSet<? extends E> ss = (SortedSet<? extends E>) c;
//                this.comparator = (Comparator<? super E>) ss.comparator();
//                initElementsFromCollection(ss);
//            }
//            else if (c instanceof java.util.PriorityQueue<?>) {
//                java.util.PriorityQueue<? extends E> pq = (java.util.PriorityQueue<? extends E>) c;
//                this.comparator = (Comparator<? super E>) pq.comparator();
//                initFromPriorityQueue(pq);
//            }
//            else {
//                this.comparator = null;
//                initFromCollection(c);
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        public PriorityQueue(java.util.PriorityQueue<? extends E> c) {
//            this.comparator = (Comparator<? super E>) c.comparator();
//            initFromPriorityQueue(c);
//        }
//
//        @SuppressWarnings("unchecked")
//        public PriorityQueue(SortedSet<? extends E> c) {
//            this.comparator = (Comparator<? super E>) c.comparator();
//            initElementsFromCollection(c);
//        }
//
//        private void initFromPriorityQueue(java.util.PriorityQueue<? extends E> c) {
//            if (c.getClass() == java.util.PriorityQueue.class) {
//                this.queue = c.toArray();
//                this.size = c.size();
//            } else {
//                initFromCollection(c);
//            }
//        }
//
//        private void initElementsFromCollection(Collection<? extends E> c) {
//            Object[] a = c.toArray();
//            if (a.getClass() != Object[].class)
//                a = Arrays.copyOf(a, a.length, Object[].class);
//            int len = a.length;
//            if (len == 1 || this.comparator != null)
//                for (int i = 0; i < len; i++)
//                    if (a[i] == null)
//                        throw new NullPointerException();
//            this.queue = a;
//            this.size = a.length;
//        }
//
//        private void initFromCollection(Collection<? extends E> c) {
//            initElementsFromCollection(c);
//            heapify();
//        }
//
//        private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
//
//        //对队列底层数组扩容：
//        private void grow(int minCapacity) {
//            //现阶段数组长度：
//            int oldCapacity = queue.length;
//
//            //计算新数组的长度：
//            // 如果 现阶段数组长度<64，则扩容为现阶段长度的2倍+2；
//            // 如果 现阶段数组>=64，则扩容为现阶段长度的2倍+5；
//            int newCapacity = oldCapacity + ((oldCapacity < 64) ?
//                    (oldCapacity + 2) :
//                    (oldCapacity >> 1));
//            if (newCapacity - MAX_ARRAY_SIZE > 0)
//                newCapacity = hugeCapacity(minCapacity);
//
//            //数组复制：得到新数组
//            queue = Arrays.copyOf(queue, newCapacity);
//        }
//
//        private static int hugeCapacity(int minCapacity) {
//            if (minCapacity < 0)
//                throw new OutOfMemoryError();
//            return (minCapacity > MAX_ARRAY_SIZE) ?
//                    Integer.MAX_VALUE :
//                    MAX_ARRAY_SIZE;
//        }
//
//        //队列添加元素，底层调用offer:插入失败抛出异常
//        public boolean add(E e) {
//            return offer(e);
//        }
//
//        //队列添加元素: 队列失败返回false
//        public boolean offer(E e) {
//            //不支持添加为null的元素：
//            if (e == null)
//                throw new NullPointerException();
//
//            //队列操作数+1：
//            modCount++;
//            int i = size;
//
//            //队列长度 >= 数组长度时，扩容：
//            if (i >= queue.length)
//                grow(i + 1);
//
//            //队列长度+1
//            size = i + 1;
//
//            //i==0，在数组角标为0处插入第一个元素：
//            if (i == 0)
//                queue[0] = e;
//            else
//                //插入的不是第一个元素：
//                siftUp(i, e);
//            return true;
//        }
//
//        //返回队列头部的元素，不删除该元素(如果队列为空，则返回null)
//        public E peek() {
//            if (size == 0)
//                return null;
//            return (E) queue[0];
//        }
//
//        //判断o元素是否存在于数组中：
//        private int indexOf(Object o) {
//            if (o != null) {
//                for (int i = 0; i < size; i++)
//                    if (o.equals(queue[i]))
//                        return i;
//            }
//            return -1;
//        }
//
//        //移除队列中的o元素：
//        public boolean remove(Object o) {
//            //从队列中查找o元素是否存在：
//            int i = indexOf(o);
//            //不存在返回false
//            if (i == -1)
//                return false;
//            else {
//                //存在返回true,进行删除操作：
//                removeAt(i);
//                return true;
//            }
//        }
//
//        boolean removeEq(Object o) {
//            for (int i = 0; i < size; i++) {
//                if (o == queue[i]) {
//                    removeAt(i);
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        //判断队列中是否包含o元素：
//        public boolean contains(Object o) {
//            return indexOf(o) != -1;
//        }
//
//        public Object[] toArray() {
//            return Arrays.copyOf(queue, size);
//        }
//
//        public <T> T[] toArray(T[] a) {
//            if (a.length < size)
//                // Make a new array of a's runtime type, but my contents:
//                return (T[]) Arrays.copyOf(queue, size, a.getClass());
//            System.arraycopy(queue, 0, a, 0, size);
//            if (a.length > size)
//                a[size] = null;
//            return a;
//        }
//
//        //PriorityQueue的迭代器：
//        public Iterator<E> iterator() {
//            return new java.util.PriorityQueue.Itr();
//        }
//
//        //PriorityQueue迭代器实现类：
//        private final class Itr implements Iterator<E> {
//            private int cursor = 0;
//            private int lastRet = -1;
//            private ArrayDeque<E> forgetMeNot = null;
//            private E lastRetElt = null;
//            private int expectedModCount = modCount;
//            public boolean hasNext() {
//                return cursor < size ||
//                        (forgetMeNot != null && !forgetMeNot.isEmpty());
//            }
//            public E next() {
//                if (expectedModCount != modCount)
//                    throw new ConcurrentModificationException();
//                if (cursor < size)
//                    return (E) queue[lastRet = cursor++];
//                if (forgetMeNot != null) {
//                    lastRet = -1;
//                    lastRetElt = forgetMeNot.poll();
//                    if (lastRetElt != null)
//                        return lastRetElt;
//                }
//                throw new NoSuchElementException();
//            }
//            public void remove() {
//                if (expectedModCount != modCount)
//                    throw new ConcurrentModificationException();
//                if (lastRet != -1) {
//                    E moved = java.util.PriorityQueue.this.removeAt(lastRet);
//                    lastRet = -1;
//                    if (moved == null)
//                        cursor--;
//                    else {
//                        if (forgetMeNot == null)
//                            forgetMeNot = new ArrayDeque<>();
//                        forgetMeNot.add(moved);
//                    }
//                } else if (lastRetElt != null) {
//                    java.util.PriorityQueue.this.removeEq(lastRetElt);
//                    lastRetElt = null;
//                } else {
//                    throw new IllegalStateException();
//                }
//                expectedModCount = modCount;
//            }
//        }
//
//        //队列长度：
//        public int size() {
//            return size;
//        }
//
//        //清空队列中的元素：
//        public void clear() {
//            modCount++;
//            for (int i = 0; i < size; i++)
//                queue[i] = null;
//            size = 0;
//        }
//
//        //返回队列头部的元素，并删除该元素(如果队列为空，则返回null)
//        public E poll() {
//            if (size == 0)
//                return null;
//            int s = --size;
//            modCount++;
//            E result = (E) queue[0];
//            E x = (E) queue[s];
//            queue[s] = null;
//            if (s != 0)
//                siftDown(0, x);
//            return result;
//        }
//
//        //队列底层删除元素操作：
//        private E removeAt(int i) {
//            assert i >= 0 && i < size;
//            modCount++;
//            int s = --size;
//            if (s == i) // removed last element
//                queue[i] = null;
//            else {
//                E moved = (E) queue[s];
//                queue[s] = null;
//                siftDown(i, moved);
//                if (queue[i] == moved) {
//                    siftUp(i, moved);
//                    if (queue[i] != moved)
//                        return moved;
//                }
//            }
//            return null;
//        }
//
//        private void siftUp(int k, E x) {
//            //如果元素比较器不为：
//            if (comparator != null)
//                siftUpUsingComparator(k, x);
//            else
//                //元素比较器为null：
//                siftUpComparable(k, x);
//        }
//
//        //默认比较器来实现元素的排序，实现了堆结构
//        private void siftUpComparable(int k, E x) {
//            Comparable<? super E> key = (Comparable<? super E>) x;
//            while (k > 0) {
//                int parent = (k - 1) >>> 1;
//                Object e = queue[parent];
//                if (key.compareTo((E) e) >= 0)
//                    break;
//                queue[k] = e;
//                k = parent;
//            }
//            queue[k] = key;
//        }
//
//        //自定义比较器来实现元素的排序，实现了堆结构
//        private void siftUpUsingComparator(int k, E x) {
//            while (k > 0) {
//                int parent = (k - 1) >>> 1;
//                Object e = queue[parent];
//                if (comparator.compare(x, (E) e) >= 0)
//                    break;
//                queue[k] = e;
//                k = parent;
//            }
//            queue[k] = x;
//        }
//
//        private void siftDown(int k, E x) {
//            if (comparator != null)
//                siftDownUsingComparator(k, x);
//            else
//                siftDownComparable(k, x);
//        }
//
//        private void siftDownComparable(int k, E x) {
//            Comparable<? super E> key = (Comparable<? super E>)x;
//            int half = size >>> 1;        // loop while a non-leaf
//            while (k < half) {
//                int child = (k << 1) + 1; // assume left child is least
//                Object c = queue[child];
//                int right = child + 1;
//                if (right < size &&
//                        ((Comparable<? super E>) c).compareTo((E) queue[right]) > 0)
//                    c = queue[child = right];
//                if (key.compareTo((E) c) <= 0)
//                    break;
//                queue[k] = c;
//                k = child;
//            }
//            queue[k] = key;
//        }
//
//        private void siftDownUsingComparator(int k, E x) {
//            int half = size >>> 1;
//            while (k < half) {
//                int child = (k << 1) + 1;
//                Object c = queue[child];
//                int right = child + 1;
//                if (right < size &&
//                        comparator.compare((E) c, (E) queue[right]) > 0)
//                    c = queue[child = right];
//                if (comparator.compare(x, (E) c) <= 0)
//                    break;
//                queue[k] = c;
//                k = child;
//            }
//            queue[k] = x;
//        }
//
//        private void heapify() {
//            for (int i = (size >>> 1) - 1; i >= 0; i--)
//                siftDown(i, (E) queue[i]);
//        }
//
//        //返回队列中的比较器：
//        public Comparator<? super E> comparator() {
//            return comparator;
//        }
//
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException{
//            s.defaultWriteObject();
//            s.writeInt(Math.max(2, size + 1));
//            for (int i = 0; i < size; i++)
//                s.writeObject(queue[i]);
//        }
//
//        private void readObject(java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//            s.defaultReadObject();
//
//            s.readInt();
//
//            queue = new Object[size];
//
//            for (int i = 0; i < size; i++)
//                queue[i] = s.readObject();
//
//            heapify();
//        }
//    }
//}
