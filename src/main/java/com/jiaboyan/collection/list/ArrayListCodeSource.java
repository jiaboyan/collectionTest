//package com.jiaboyan.collection.list;
//
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/7/29.
// */
//public class ArrayListCodeSource {
//
//    public class ArrayList<E> extends AbstractList<E>
//            implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
//
//        //实现Serializable接口，生成的序列版本号：
//        private static final long serialVersionUID = 8683452581122892189L;
//
//        //ArrayList初始容量大小：在无参构造中不使用了
//        private static final int DEFAULT_CAPACITY = 10;
//
//        //空数组对象：初始化中默认赋值给elementData
//        private static final Object[] EMPTY_ELEMENTDATA = {};
//
//        //ArrayList中实际存储元素的数组：
//        private transient Object[] elementData;
//
//        //集合实际存储元素长度：
//        private int size;
//
//        //ArrayList有参构造：容量大小
//        public ArrayList(int initialCapacity) {
//            //即父类构造：protected AbstractList() {}空方法
//            super();
//            //如果传递的初始容量小于0 ，抛出异常
//            if (initialCapacity < 0)
//                throw new IllegalArgumentException("Illegal Capacity: "+ initialCapacity);
//            //初始化数据：创建Object数组
//            this.elementData = new Object[initialCapacity];
//        }
//
//        //ArrayList无参构造：
//        public ArrayList() {
//            //即父类构造：protected AbstractList() {}空方法
//            super();
//            //初始化数组：空数组，容量为0
//            this.elementData = EMPTY_ELEMENTDATA;
//        }
//
//        //ArrayList有参构造：Java集合
//        public ArrayList(Collection<? extends E> c) {
//            //将集合转换为数组：
//            elementData = c.toArray();
//            //设置数组的长度：
//            size = elementData.length;
//            if (elementData.getClass() != Object[].class)
//                elementData = Arrays.copyOf(elementData, size, Object[].class);
//        }
//
//        //将ArrayList的数组大小，变更为实际元素大小：
//        public void trimToSize() {
//            //操作数+1
//            modCount++;
//            //如果集合内元素的个数，小于数组的长度，那么将数组中空余元素删除
//            if (size < elementData.length) {
//                elementData = Arrays.copyOf(elementData, size);
//            }
//        }
//
//        public void ensureCapacity(int minCapacity) {
//            int minExpand = (elementData != EMPTY_ELEMENTDATA) ? 0 : DEFAULT_CAPACITY;
//            if (minCapacity > minExpand) {
//                ensureExplicitCapacity(minCapacity);
//            }
//        }
//
//        //ArrayList集合内元素的个数：
//        public int size() {
//            return size;
//        }
//
//        //判断ArrayList集合元素是否为空：
//        public boolean isEmpty() {
//            return size == 0;
//        }
//
//        //判断ArrayList集合包含某个元素：
//        public boolean contains(Object o) {
//            //判断对象o在ArrayList中存在的角标位置
//            return indexOf(o) >= 0;
//        }
//
//        //判断对象o在ArrayList中存在的角标位置：
//        public int indexOf(Object o) {
//            //如果o==null：
//            if (o == null) {
//                //遍历集合，判断哪个元素等于null，则返回对应角标
//                for (int i = 0; i < size; i++)
//                    if (elementData[i]==null)
//                        return i;
//            } else {
//                //同理：
//                for (int i = 0; i < size; i++)
//                    if (o.equals(elementData[i]))
//                        return i;
//            }
//            //如果不存在，则返回-1
//            return -1;
//        }
//
//        //判断对象o在ArrayList中出现的最后一个位置：
//        public int lastIndexOf(Object o) {
//            //如果o==null：
//            if (o == null) {
//                //从集合最后一个元素开始遍历：
//                for (int i = size-1; i >= 0; i--)
//                    if (elementData[i]==null)
//                        return i;
//            } else {
//                //同理：
//                for (int i = size-1; i >= 0; i--)
//                    if (o.equals(elementData[i]))
//                        return i;
//            }
//            //如果不存在，则返回-1
//            return -1;
//        }
//
//        //返回此ArrayList实例的 克隆对象：
//        public Object clone() {
//            try {
//                java.util.ArrayList<E> v = (java.util.ArrayList<E>) super.clone();
//                v.elementData = Arrays.copyOf(elementData, size);
//                v.modCount = 0;
//                return v;
//            } catch (CloneNotSupportedException e) {
//                throw new InternalError();
//            }
//        }
//
//        //将ArrayList里面的元素赋值到一个数组中去 生成Object数组：
//        public Object[] toArray() {
//            return Arrays.copyOf(elementData, size);
//        }
//
//        //将ArrayList里面的元素赋值到一个数组中去，专成对应类型的数组：
//        public <T> T[] toArray(T[] a) {
//            if (a.length < size)
//                return (T[]) Arrays.copyOf(elementData, size, a.getClass());
//            System.arraycopy(elementData, 0, a, 0, size);
//            if (a.length > size)
//                a[size] = null;
//            return a;
//        }
//
//        //获取数组index位置的元素：
//        E elementData(int index) {
//            return (E) elementData[index];
//        }
//
//        //获取index位置的元素
//        public E get(int index) {
//            //检查index是否合法：
//            rangeCheck(index);
//            //获取元素：
//            return elementData(index);
//        }
//
//        //设置index位置的元素值了element，返回该位置的之前的值
//        public E set(int index, E element) {
//            //检查index是否合法：
//            rangeCheck(index);
//            //获取该index原来的元素：
//            E oldValue = elementData(index);
//            //替换成新的元素：
//            elementData[index] = element;
//            //返回旧的元素：
//            return oldValue;
//        }
//
//        //添加元素e
//        public boolean add(E e) {
//            ensureCapacityInternal(size + 1);
//            elementData[size++] = e;
//            return true;
//        }
//
//        //在ArrayList的index位置，添加元素element
//        public void add(int index, E element) {
//            //判断index角标的合法性：
//            rangeCheckForAdd(index);
//            //判断是否需要扩容：传入当前元素大小+1
//            ensureCapacityInternal(size + 1);
//            System.arraycopy(elementData, index, elementData, index + 1, size - index);
//            elementData[index] = element;
//            size++;
//        }
//
//        //得到最小扩容量
//        private void ensureCapacityInternal(int minCapacity) {
//            //如果此时ArrayList是空数组,则将最小扩容大小设置为10：
//            if (elementData == EMPTY_ELEMENTDATA) {
//                minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
//            }
//            //判断是否需要扩容：
//            ensureExplicitCapacity(minCapacity);
//        }
//
//        //判断是否需要扩容
//        private void ensureExplicitCapacity(int minCapacity) {
//            //操作数+1
//            modCount++;
//            //判断最小扩容容量-数组大小是否大于0：
//            if (minCapacity - elementData.length > 0)
//                //扩容：
//                grow(minCapacity);
//        }
//
//        //ArrayList最大容量：
//        private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
//
//        //帮助ArrayList动态扩容的核心方法:
//        private void grow(int minCapacity) {
//            //获取现有数组大小：
//            int oldCapacity = elementData.length;
//            //位运算，得带新的数组容量大小，为原有的1.5倍：
//            int newCapacity = oldCapacity + (oldCapacity >> 1);
//            //如果新扩容的大小依旧小于传入的容量值，那么将传入的值设为新容器大小：
//            if (newCapacity - minCapacity < 0)
//                newCapacity = minCapacity;
//
//            //如果新容器大小，大于ArrayList最大长度：
//            if (newCapacity - MAX_ARRAY_SIZE > 0)
//                //计算出最大容量值：
//                newCapacity = hugeCapacity(minCapacity);
//            //数组复制：
//            elementData = Arrays.copyOf(elementData, newCapacity);
//        }
//        //计算ArrayList最大容量：
//        private static int hugeCapacity(int minCapacity) {
//            if (minCapacity < 0)
//                throw new OutOfMemoryError();
//            //如果新的容量大于MAX_ARRAY_SIZE。将会调用hugeCapacity将int的最大值赋给newCapacity:
//            return (minCapacity > MAX_ARRAY_SIZE) ?
//                    Integer.MAX_VALUE :
//                    MAX_ARRAY_SIZE;
//        }
//
//        //在ArrayList的移除index位置的元素
//        public E remove(int index) {
//            //检查角标是否合法：不合法抛异常
//            rangeCheck(index);
//            //操作数+1：
//            modCount++;
//            //获取当前角标的value:
//            E oldValue = elementData(index);
//            //获取需要删除元素 到最后一个元素的长度，也就是删除元素后，后续元素移动的个数；
//            int numMoved = size - index - 1;
//            //如果移动元素个数大于0 ，也就是说删除的不是最后一个元素：
//            if (numMoved > 0)
//                // 将elementData数组index+1位置开始拷贝到elementData从index开始的空间
//                System.arraycopy(elementData, index+1, elementData, index, numMoved);
//            //size减1，并将最后一个元素置为null
//            elementData[--size] = null;
//            //返回被删除的元素：
//            return oldValue;
//        }
//
//        //在ArrayList的移除对象为O的元素，不返回被删除的元素：
//        public boolean remove(Object o) {
//            //如果o==null，则遍历集合，判断哪个元素为null：
//            if (o == null) {
//                for (int index = 0; index < size; index++)
//                    if (elementData[index] == null) {
//                        //快速删除，和前面的remove（index）一样的逻辑
//                        fastRemove(index);
//                        return true;
//                    }
//            } else {
//                //同理：
//                for (int index = 0; index < size; index++)
//                    if (o.equals(elementData[index])) {
//                        fastRemove(index);
//                        return true;
//                    }
//            }
//            return false;
//        }
//
//        //快速删除：
//        private void fastRemove(int index) {
//            //操作数+1
//            modCount++;
//            //获取需要删除元素 到最后一个元素的长度，也就是删除元素后，后续元素移动的个数；
//            int numMoved = size - index - 1;
//            //如果移动元素个数大于0 ，也就是说删除的不是最后一个元素：
//            if (numMoved > 0)
//                // 将elementData数组index+1位置开始拷贝到elementData从index开始的空间
//                System.arraycopy(elementData, index+1, elementData, index, numMoved);
//            //size减1，并将最后一个元素置为null
//            elementData[--size] = null;
//        }
//
//        //设置全部元素为null值，并设置size为0。
//        public void clear() {
//            modCount++;
//            for (int i = 0; i < size; i++)
//                elementData[i] = null;
//            size = 0;
//        }
//
//        //序列化写入：
//        private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
//            int expectedModCount = modCount;
//            s.defaultWriteObject();
//            s.writeInt(size);
//            for (int i=0; i<size; i++) {
//                s.writeObject(elementData[i]);
//            }
//            if (modCount != expectedModCount) {
//                throw new ConcurrentModificationException();
//            }
//        }
//
//        // 序列化读取：
//        private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
//            elementData = EMPTY_ELEMENTDATA;
//            s.defaultReadObject();
//            s.readInt();
//            if (size > 0) {
//                ensureCapacityInternal(size);
//                Object[] a = elementData;
//                for (int i=0; i<size; i++) {
//                    a[i] = s.readObject();
//                }
//            }
//        }
//       //检查角标是否合法：不合法抛异常
//        private void rangeCheck(int index) {
//            if (index >= size)
//                throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
//        }
//
//        //返回一个Iterator对象，Itr为ArrayList的一个内部类，其实现了Iterator<E>接口
//        public Iterator<E> iterator() {
//            return new java.util.ArrayList.Itr();
//        }
//
//        //其中的Itr是实现了Iterator接口，同时重写了里面的hasNext()，next()，remove()等方法；
//        private class Itr implements Iterator<E> {
//            int cursor; //类似游标，指向迭代器下一个值的位置
//            int lastRet = -1; //迭代器最后一次取出的元素的位置。
//            int expectedModCount = modCount;//Itr初始化时候ArrayList的modCount的值。
//            //modCount用于记录ArrayList内发生结构性改变的次数，
//            // 而Itr每次进行next或remove的时候都会去检查expectedModCount值是否还和现在的modCount值，
//            // 从而保证了迭代器和ArrayList内数据的一致性。
//
//            //利用游标，与size之前的比较，判断迭代器是否还有下一个元素
//            public boolean hasNext() {
//                return cursor != size;
//            }
//
//            //迭代器获取下一个元素：
//            public E next() {
//                //检查modCount是否改变：
//                checkForComodification();
//
//                int i = cursor;
//
//                //游标不会大于等于集合的长度：
//                if (i >= size)
//                    throw new NoSuchElementException();
//
//                Object[] elementData = java.util.ArrayList.this.elementData;
//                //游标不会大于集合中数组的长度：
//                if (i >= elementData.length)
//                    throw new ConcurrentModificationException();
//                //游标+1
//                cursor = i + 1;
//                //取出元素：
//                return (E) elementData[lastRet = i];
//            }
//
//            public void remove() {
//                if (lastRet < 0)
//                    throw new IllegalStateException();
//                //检查modCount是否改变：防止并发操作集合
//                checkForComodification();
//                try {
//                    //删除这个元素：
//                    java.util.ArrayList.this.remove(lastRet);
//                    //删除后，重置游标，和当前指向元素的角标 lastRet
//                    cursor = lastRet;
//                    lastRet = -1;
//                    //重置expectedModCount：
//                    expectedModCount = modCount;
//                } catch (IndexOutOfBoundsException ex) {
//                    throw new ConcurrentModificationException();
//                }
//            }
//
//            //并发检查：在Itr初始化时，将modCount赋值给了expectedModCount
//            //如果后续modCount还有变化，则抛出异常，所以在迭代器迭代过程中，不能调List增删操作；
//            final void checkForComodification() {
//                if (modCount != expectedModCount)
//                    throw new ConcurrentModificationException();
//            }
//        }
//    }
//}
