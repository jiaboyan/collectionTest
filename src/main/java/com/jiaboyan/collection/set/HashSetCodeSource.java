//package com.jiaboyan.collection.set;
//
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/8/5.
// */
//public class HashSetCodeSource {
//
//
//    public class HashSet<E>
//            extends AbstractSet<E>
//            implements Set<E>, Cloneable, java.io.Serializable {
//        static final long serialVersionUID = -5024744406713321676L;
//
//        //HashSet底层保存元素的属性：一个HashMap
//        private transient HashMap<E,Object> map;
//
//        //HashSet底层由HashMap实现，新增的元素为map的key，而value则默认为PRESENT。
//        private static final Object PRESENT = new Object();
//
//        //无参构造方法：
//        public HashSet() {
//            //默认new一个HashMap
//            map = new HashMap<>();
//        }
//
//        // 带集合的构造函数
//        public HashSet(Collection<? extends E> c) {
//            // 进行初始化HashMap容量判断，传入的集合长度/0.75+1  与16进行比较，看哪个值大：
//            map = new HashMap<>(Math.max((int) (c.size()/.75f) + 1, 16));
//            addAll(c);
//        }
//
//        // 指定HashSet初始容量和加载因子的构造函数：
//        public HashSet(int initialCapacity, float loadFactor) {
//            map = new HashMap<>(initialCapacity, loadFactor);
//        }
//
//        // 指定HashSet初始容量的构造函数
//        public HashSet(int initialCapacity) {
//            map = new HashMap<>(initialCapacity);
//        }
//
//        HashSet(int initialCapacity, float loadFactor, boolean dummy) {
//            map = new LinkedHashMap<>(initialCapacity, loadFactor);
//        }
//
//        //返回HashSet的迭代器：
//        public Iterator<E> iterator() {
//            return map.keySet().iterator();
//        }
//
//        //调用HashMap的size()方法,返回Entry数组中的元素大小：
//        public int size() {
//            return map.size();
//        }
//
//        //调用HashMap的isEmpty()方法，来判断Entry数组是否有元素：
//        public boolean isEmpty() {
//            return map.isEmpty();
//        }
//
//        //调用HashMap的contains()方法，来判断传入的元素是否存在于HashMap的Entry数组中中
//        public boolean contains(Object o) {
//            return map.containsKey(o);
//        }
//
//        //调用HashMap中的put()方法，添加的元素被当成HashMap的key，而value则默认为一个Object对象；
//        public boolean add(E e) {
//            return map.put(e, PRESENT)==null;
//        }
//
//        //调用HashMap中的remove方法，从Entry<K,V>[]数组中移除以o为key的Entry
//        public boolean remove(Object o) {
//            return map.remove(o)==PRESENT;
//        }
//
//        //调用HashMap中的clear()方法，清空Entry<K,V>[]数组；
//        public void clear() {
//            map.clear();
//        }
//
//        //克隆一个HashSet，并返回Object对象：
//        public Object clone() {
//            try {
//                java.util.HashSet<E> newSet = (java.util.HashSet<E>) super.clone();
//                newSet.map = (HashMap<E, Object>) map.clone();
//                return newSet;
//            } catch (CloneNotSupportedException e) {
//                throw new InternalError();
//            }
//        }
//
//        //序列化对象：其中map属性被transient修饰，需要单独在此方法中进行序列化；
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException {
//            s.defaultWriteObject();
//            s.writeInt(map.capacity());
//            s.writeFloat(map.loadFactor());
//            s.writeInt(map.size());
//            for (E e : map.keySet())
//                s.writeObject(e);
//        }
//        //反序列化对象：
//        private void readObject(java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//            s.defaultReadObject();
//            int capacity = s.readInt();
//            float loadFactor = s.readFloat();
//            map = (((java.util.HashSet)this) instanceof LinkedHashSet ?
//                    new LinkedHashMap<E,Object>(capacity, loadFactor) :
//                    new HashMap<E,Object>(capacity, loadFactor));
//            int size = s.readInt();
//            for (int i=0; i<size; i++) {
//                E e = (E) s.readObject();
//                map.put(e, PRESENT);
//            }
//        }
//    }
//}
