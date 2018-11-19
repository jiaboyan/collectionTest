//package com.jiaboyan.collection.queue;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/10/15.
// */
//public class ArrayDequeCodeSource {
//
//    public class ArrayDeque<E> extends AbstractCollection<E>
//            implements Deque<E>, Cloneable, Serializable {
//
//        //底层数据结构实现---数组
//        private transient E[] elements;
//
//        //队列头指针：默认为0
//        private transient int head;
//
//        //队列尾指针：默认为0
//        private transient int tail;
//
//        //最小初始化容量值：
//        private static final int MIN_INITIAL_CAPACITY = 8;
//
//        private void allocateElements(int numElements) {
//            int initialCapacity = MIN_INITIAL_CAPACITY;
//            if (numElements >= initialCapacity) {
//                initialCapacity = numElements;
//                initialCapacity |= (initialCapacity >>>  1);
//                initialCapacity |= (initialCapacity >>>  2);
//                initialCapacity |= (initialCapacity >>>  4);
//                initialCapacity |= (initialCapacity >>>  8);
//                initialCapacity |= (initialCapacity >>> 16);
//                initialCapacity++;
//
//                if (initialCapacity < 0)   // Too many elements, must back off
//                    initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
//            }
//            elements = (E[]) new Object[initialCapacity];
//        }
//
//        //扩大数组的长度：扩大为原有长度的2倍；
//        private void doubleCapacity() {
//            //assert断言修饰符--只有当head==tail时候才会进行扩容操作；
//            //(前提是assert开启，否则assert修饰符无效)
//            assert head == tail;
//            int p = head;
//            int n = elements.length;
//            int r = n - p; // number of elements to the right of p
//            int newCapacity = n << 1;
//            if (newCapacity < 0)
//                throw new IllegalStateException("Sorry, deque too big");
//            Object[] a = new Object[newCapacity];
//            //连续调用2次的目的，是为了把原数组中所有的元素全部复制到新数组中(看图说话)
//            System.arraycopy(elements, p, a, 0, r);
//            System.arraycopy(elements, 0, a, r, p);
//            elements = (E[])a;
//            head = 0;
//            tail = n;
//        }
//        private <T> T[] copyElements(T[] a) {
//            if (head < tail) {
//                System.arraycopy(elements, head, a, 0, size());
//            } else if (head > tail) {
//                int headPortionLen = elements.length - head;
//                System.arraycopy(elements, head, a, 0, headPortionLen);
//                System.arraycopy(elements, 0, a, headPortionLen, tail);
//            }
//            return a;
//        }
//
//        //默认构造函数：队列的长度默认为16
//        public ArrayDeque() {
//            elements = (E[]) new Object[16];
//        }
//
//        //可设置队列大小的构造函数：
//        public ArrayDeque(int numElements) {
//            allocateElements(numElements);
//        }
//
//        //带集合的构造函数：
//        public ArrayDeque(Collection<? extends E> c) {
//            allocateElements(c.size());
//            addAll(c);
//        }
//
//        //Deque双端队列方法：
//        //向队列的头部插入元素：初始时从数组的最大角标处插入；
//        public void addFirst(E e) {
//            //插入元素不能为null:
//            if (e == null)
//                throw new NullPointerException();
//            //计算插入的角标：  head = （head-1 与运算 数组长度-1）
//            elements[head = (head - 1) & (elements.length - 1)] = e;
//            //如果头尾指针相同，则进行扩容操作：
//            if (head == tail)
//                doubleCapacity();
//        }
//
//        //向队列的头部插入元素：底层调用addFirst(E e)
//        public boolean offerFirst(E e) {
//            addFirst(e);
//            return true;
//        }
//
//        //向队列的末端插入元素：初始时从数组角标为0处插入；
//        public void addLast(E e) {
//            //插入元素不能为null:
//            if (e == null)
//                throw new NullPointerException();
//            //插入数组中，位置就是尾指针的值；
//            elements[tail] = e;
//            //判断是否需要进行扩容操作:(tail+1 位运算 数组长度-1)是否与head的值相等
//            if ( (tail = (tail + 1) & (elements.length - 1)) == head)
//                //扩容操作：
//                doubleCapacity();
//        }
//
//        //向队列的末端插入元素：底层调用addLast(E e)
//        public boolean offerLast(E e) {
//            addLast(e);
//            return true;
//        }
//
//        //移除队列中第一个元素：移除数组中head指针所指向的元素；
//        public E removeFirst() {
//            E x = pollFirst();
//            //如果队列中没有元素，则抛出异常
//            if (x == null)
//                throw new NoSuchElementException();
//            return x;
//        }
//
//        //移除队列头中的元素，实际就是移除数组中head指针所指向的元素；
//        public E pollFirst() {
//            //获取头指针：
//            int h = head;
//            //获取头指针所处的数组角标元素：
//            E result = elements[h]; // Element is null if deque empty
//            //如果头指针为null，说明队列中没有元素存在，直接返回；
//            if (result == null)
//                return null;
//            //将头指针所指向数组角标置为null：
//            elements[h] = null;     // Must null out slot
//            //修改头指针大小 = 头指针+1 & 数组长度-1
//            //实际上是将现有头指针+1。
//            head = (h + 1) & (elements.length - 1);
//            //返回被删除的对象：
//            return result;
//        }
//        //移除队列中最后一个元素：移除数组中tail指针所指向的元素；
//        public E removeLast() {
//            E x = pollLast();
//            //如果队列中没有元素，则抛出异常
//            if (x == null)
//                throw new NoSuchElementException();
//            return x;
//        }
//
//        //移除队列尾中的元素，实际就是移除数组中tail指针所指向的元素；
//        public E pollLast() {
//            //获取要移除元素的数组角标：如果tail为0，则计算出的t值为数组的最大角标（首尾相连了）
//            int t = (tail - 1) & (elements.length - 1);
//            //获取要移除的数组元素：
//            E result = elements[t];
//            //如果为null，则直接返回；
//            if (result == null)
//                return null;
//            //将对应数组角标的元素置为null：
//            elements[t] = null;
//            //修改tail指针的值：
//            tail = t;
//            return result;
//        }
//
//        //得到队列中的第一个元素，也就是头指针所指向的元素；
//        public E getFirst() {
//            E x = elements[head];
//            //为空的话，抛出异常；
//            if (x == null)
//                throw new NoSuchElementException();
//            return x;
//        }
//
//        //得到队列中的第一个元素，队列空的话返回null
//        public E peekFirst() {
//            return elements[head]; // elements[head] is null if deque empty
//        }
//
//        //得到队列中的最后一个元素，也就是尾指针所指向的元素；
//        public E getLast() {
//            E x = elements[(tail - 1) & (elements.length - 1)];
//            if (x == null)
//                throw new NoSuchElementException();
//            return x;
//        }
//
//        //得到队列中的最后一个元素，队列空的话返回null
//        public E peekLast() {
//            return elements[(tail - 1) & (elements.length - 1)];
//        }
//
//        public boolean removeFirstOccurrence(Object o) {
//            if (o == null)
//                return false;
//            int mask = elements.length - 1;
//            int i = head;
//            E x;
//            while ( (x = elements[i]) != null) {
//                if (o.equals(x)) {
//                    delete(i);
//                    return true;
//                }
//                i = (i + 1) & mask;
//            }
//            return false;
//        }
//
//        public boolean removeLastOccurrence(Object o) {
//            if (o == null)
//                return false;
//            int mask = elements.length - 1;
//            int i = (tail - 1) & mask;
//            E x;
//            while ( (x = elements[i]) != null) {
//                if (o.equals(x)) {
//                    delete(i);
//                    return true;
//                }
//                i = (i - 1) & mask;
//            }
//            return false;
//        }
//
//        //队列方法：
//        //向队列中添加元素：添加到队列头部
//        public boolean add(E e) {
//            addLast(e);
//            return true;
//        }
//
//        //向队列中添加元素：添加到队列末尾
//        public boolean offer(E e) {
//            return offerLast(e);
//        }
//
//        //移除队列中元素，移除队列头部元素，为null抛出异常；
//        public E remove() {
//            return removeFirst();
//        }
//
//        //移除队列中元素，移除队列头部元素，可以为null；
//        public E poll() {
//            return pollFirst();
//        }
//
//        //获取队列头部的元素：如果为null抛出异常；
//        public E element() {
//            return getFirst();
//        }
//
//        //获取队列头部的元素：如果为null，就返回；
//        public E peek() {
//            return peekFirst();
//        }
//
//        //栈方法：
//        //向栈顶压入对象：向数组最大角标处插入对象；
//        public void push(E e) {
//            addFirst(e);
//        }
//
//        //将栈顶对象出栈，移除head指针所属的元素；
//        public E pop() {
//            return removeFirst();
//        }
//
//        private void checkInvariants() {
//            assert elements[tail] == null;
//            assert head == tail ? elements[head] == null :
//                    (elements[head] != null &&
//                            elements[(tail - 1) & (elements.length - 1)] != null);
//            assert elements[(head - 1) & (elements.length - 1)] == null;
//        }
//
//        private boolean delete(int i) {
//            checkInvariants();
//            final E[] elements = this.elements;
//            final int mask = elements.length - 1;
//            final int h = head;
//            final int t = tail;
//            final int front = (i - h) & mask;
//            final int back  = (t - i) & mask;
//
//            // Invariant: head <= i < tail mod circularity
//            if (front >= ((t - h) & mask))
//                throw new ConcurrentModificationException();
//
//            // Optimize for least element motion
//            if (front < back) {
//                if (h <= i) {
//                    System.arraycopy(elements, h, elements, h + 1, front);
//                } else { // Wrap around
//                    System.arraycopy(elements, 0, elements, 1, i);
//                    elements[0] = elements[mask];
//                    System.arraycopy(elements, h, elements, h + 1, mask - h);
//                }
//                elements[h] = null;
//                head = (h + 1) & mask;
//                return false;
//            } else {
//                if (i < t) { // Copy the null tail as well
//                    System.arraycopy(elements, i + 1, elements, i, back);
//                    tail = t - 1;
//                } else { // Wrap around
//                    System.arraycopy(elements, i + 1, elements, i, mask - i);
//                    elements[mask] = elements[0];
//                    System.arraycopy(elements, 1, elements, 0, t);
//                    tail = (t - 1) & mask;
//                }
//                return true;
//            }
//        }
//
//       //集合方法
//        public int size() {
//            return (tail - head) & (elements.length - 1);
//        }
//
//        public boolean isEmpty() {
//            return head == tail;
//        }
//
//        public Iterator<E> iterator() {
//            return new java.util.ArrayDeque.DeqIterator();
//        }
//
//        public Iterator<E> descendingIterator() {
//            return new java.util.ArrayDeque.DescendingIterator();
//        }
//
//        private class DeqIterator implements Iterator<E> {
//            private int cursor = head;
//            private int fence = tail;
//            private int lastRet = -1;
//            public boolean hasNext() {
//                return cursor != fence;
//            }
//            public E next() {
//                if (cursor == fence)
//                    throw new NoSuchElementException();
//                E result = elements[cursor];
//                if (tail != fence || result == null)
//                    throw new ConcurrentModificationException();
//                lastRet = cursor;
//                cursor = (cursor + 1) & (elements.length - 1);
//                return result;
//            }
//            public void remove() {
//                if (lastRet < 0)
//                    throw new IllegalStateException();
//                if (delete(lastRet)) { // if left-shifted, undo increment in next()
//                    cursor = (cursor - 1) & (elements.length - 1);
//                    fence = tail;
//                }
//                lastRet = -1;
//            }
//        }
//
//        private class DescendingIterator implements Iterator<E> {
//            private int cursor = tail;
//            private int fence = head;
//            private int lastRet = -1;
//            public boolean hasNext() {
//                return cursor != fence;
//            }
//            public E next() {
//                if (cursor == fence)
//                    throw new NoSuchElementException();
//                cursor = (cursor - 1) & (elements.length - 1);
//                E result = elements[cursor];
//                if (head != fence || result == null)
//                    throw new ConcurrentModificationException();
//                lastRet = cursor;
//                return result;
//            }
//            public void remove() {
//                if (lastRet < 0)
//                    throw new IllegalStateException();
//                if (!delete(lastRet)) {
//                    cursor = (cursor + 1) & (elements.length - 1);
//                    fence = head;
//                }
//                lastRet = -1;
//            }
//        }
//
//        public boolean contains(Object o) {
//            if (o == null)
//                return false;
//            int mask = elements.length - 1;
//            int i = head;
//            E x;
//            while ( (x = elements[i]) != null) {
//                if (o.equals(x))
//                    return true;
//                i = (i + 1) & mask;
//            }
//            return false;
//        }
//
//        public boolean remove(Object o) {
//            return removeFirstOccurrence(o);
//        }
//
//        public void clear() {
//            int h = head;
//            int t = tail;
//            if (h != t) {
//                head = tail = 0;
//                int i = h;
//                int mask = elements.length - 1;
//                do {
//                    elements[i] = null;
//                    i = (i + 1) & mask;
//                } while (i != t);
//            }
//        }
//
//        public Object[] toArray() {
//            return copyElements(new Object[size()]);
//        }
//
//        public <T> T[] toArray(T[] a) {
//            int size = size();
//            if (a.length < size)
//                a = (T[])java.lang.reflect.Array.newInstance(
//                        a.getClass().getComponentType(), size);
//            copyElements(a);
//            if (a.length > size)
//                a[size] = null;
//            return a;
//        }
//
//        // Object方法
//        public java.util.ArrayDeque<E> clone() {
//            try {
//                java.util.ArrayDeque<E> result = (java.util.ArrayDeque<E>) super.clone();
//                result.elements = Arrays.copyOf(elements, elements.length);
//                return result;
//
//            } catch (CloneNotSupportedException e) {
//                throw new AssertionError();
//            }
//        }
//
//        private static final long serialVersionUID = 2340985798034038923L;
//
//        private void writeObject(ObjectOutputStream s) throws IOException {
//            s.defaultWriteObject();
//            s.writeInt(size());
//            int mask = elements.length - 1;
//            for (int i = head; i != tail; i = (i + 1) & mask)
//                s.writeObject(elements[i]);
//        }
//
//        private void readObject(ObjectInputStream s)
//                throws IOException, ClassNotFoundException {
//            s.defaultReadObject();
//            int size = s.readInt();
//            allocateElements(size);
//            head = 0;
//            tail = size;
//            for (int i = 0; i < size; i++)
//                elements[i] = (E)s.readObject();
//        }
//    }
//
//
//}
