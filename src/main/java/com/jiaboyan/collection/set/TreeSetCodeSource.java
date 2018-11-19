//package com.jiaboyan.collection.set;
//
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/8/5.
// */
//public class TreeSetCodeSource {
//
//    public class TreeSet<E> extends AbstractSet<E>
//            implements NavigableSet<E>, Cloneable, java.io.Serializable {
//
//        //TreeSet中保存元素的map对象：
//        private transient NavigableMap<E,Object> m;
//
//        //map对象中保存的value:
//        private static final Object PRESENT = new Object();
//
//        //最底层的构造方法，不对外。NavigableMap是一个接口，常用实现类为TreeMap;
//        TreeSet(NavigableMap<E,Object> m) {
//            this.m = m;
//        }
//
//        //无参构造：传入一个TreeMap对象：
//        public TreeSet() {
//            this(new TreeMap<E,Object>());
//        }
//
//        //传入比较器的构造函数：通常传入一个自定义Comparator的实现类；
//        public TreeSet(Comparator<? super E> comparator) {
//            this(new TreeMap<>(comparator));
//        }
//
//        //将集合Collection传入TreeSet中：
//        public TreeSet(Collection<? extends E> c) {
//            this();
//            addAll(c);
//        }
//        //将集合SortedSet传入TreeSet中：
//        public TreeSet(SortedSet<E> s) {
//            this(s.comparator());
//            addAll(s);
//        }
//
//        //返回TreeSet迭代器：升序
//        public Iterator<E> iterator() {
//            return m.navigableKeySet().iterator();
//        }
//
//        //返回TreeSet迭代器：降序
//        public Iterator<E> descendingIterator() {
//            return m.descendingKeySet().iterator();
//        }
//
//        //返回一个降序的Set集合：
//        public NavigableSet<E> descendingSet() {
//            return new java.util.TreeSet<>(m.descendingMap());
//        }
//
//        // 计算TreeSet的大小
//        public int size() {
//            return m.size();
//        }
//
//        // 判断TreeSet是否为空
//        public boolean isEmpty() {
//            return m.isEmpty();
//        }
//
//        // 判断TreeSet是否包含对象(o)：
//        public boolean contains(Object o) {
//            return m.containsKey(o);
//        }
//
//        //向TreeSet中添加元素：
//        public boolean add(E e) {
//            return m.put(e, PRESENT)==null;
//        }
//
//        //删除TreeSet中元素o:
//        public boolean remove(Object o) {
//            return m.remove(o)==PRESENT;
//        }
//
//        //清空TreeSet集合元素：
//        public void clear() {
//            m.clear();
//        }
//
//        //将集合c添加到TreeSet中：
//        public  boolean addAll(Collection<? extends E> c) {
//            if (m.size()==0 && c.size() > 0 &&
//                    c instanceof SortedSet &&
//                    m instanceof TreeMap) {
//                SortedSet<? extends E> set = (SortedSet<? extends E>) c;
//                TreeMap<E,Object> map = (TreeMap<E, Object>) m;
//                Comparator<? super E> cc = (Comparator<? super E>) set.comparator();
//                Comparator<? super E> mc = map.comparator();
//                if (cc==mc || (cc != null && cc.equals(mc))) {
//                    map.addAllForTreeSet(set, PRESENT);
//                    return true;
//                }
//            }
//            return super.addAll(c);
//        }
//
//        public NavigableSet<E> subSet(E fromElement, boolean fromInclusive,
//                                      E toElement,   boolean toInclusive) {
//            return new java.util.TreeSet<>(m.subMap(fromElement, fromInclusive,
//                    toElement,   toInclusive));
//        }
//
//        public NavigableSet<E> headSet(E toElement, boolean inclusive) {
//            return new java.util.TreeSet<>(m.headMap(toElement, inclusive));
//        }
//
//        public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
//            return new java.util.TreeSet<>(m.tailMap(fromElement, inclusive));
//        }
//
//        //获取子Set集合，区间从fromElement--toElement，含头不含尾
//        public SortedSet<E> subSet(E fromElement, E toElement) {
//            return subSet(fromElement, true, toElement, false);
//        }
//
//        //返回子集合，从头元素开始到toElement：
//        public SortedSet<E> headSet(E toElement) {
//            return headSet(toElement, false);
//        }
//
//        //返回子集合，从fromElement元素开始到尾元素：
//        public SortedSet<E> tailSet(E fromElement) {
//            return tailSet(fromElement, true);
//        }
//
//        // 返回Set的比较器
//        public Comparator<? super E> comparator() {
//            return m.comparator();
//        }
//
//        //返回TreeSet第一个元素：
//        public E first() {
//            return m.firstKey();
//        }
//
//        //返回TreeSet最后一个元素：
//        public E last() {
//            return m.lastKey();
//        }
//
//        //返回TreeSet中小于e的最大元素：
//        public E lower(E e) {
//            return m.lowerKey(e);
//        }
//
//        //返回TreeSet中小于/等于e的元素：
//        public E floor(E e) {
//            return m.floorKey(e);
//        }
//
//        //返回TreeSet中大于/等于e的元素：
//        public E ceiling(E e) {
//            return m.ceilingKey(e);
//        }
//
//        //返回TreeSet中大于e的最小元素：
//        public E higher(E e) {
//            return m.higherKey(e);
//        }
//
//        // 获取第一个元素，并将该元素从TreeMap中删除
//        public E pollFirst() {
//            Map.Entry<E,?> e = m.pollFirstEntry();
//            return (e == null) ? null : e.getKey();
//        }
//
//        // 获取最后一个元素，并将该元素从TreeMap中删除
//        public E pollLast() {
//            Map.Entry<E,?> e = m.pollLastEntry();
//            return (e == null) ? null : e.getKey();
//        }
//
//        //clone对象：
//        public Object clone() {
//            java.util.TreeSet<E> clone = null;
//            try {
//                clone = (java.util.TreeSet<E>) super.clone();
//            } catch (CloneNotSupportedException e) {
//                throw new InternalError();
//            }
//            clone.m = new TreeMap<>(m);
//            return clone;
//        }
//
//        //序列化：
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException {
//            s.defaultWriteObject();
//            s.writeObject(m.comparator());
//            s.writeInt(m.size());
//            for (E e : m.keySet())
//                s.writeObject(e);
//        }
//
//        //反序列化：
//        private void readObject(java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//            s.defaultReadObject();
//            Comparator<? super E> c = (Comparator<? super E>) s.readObject();
//            TreeMap<E,Object> tm;
//            if (c==null)
//                tm = new TreeMap<>();
//            else
//                tm = new TreeMap<>(c);
//            m = tm;
//            int size = s.readInt();
//            tm.readTreeSet(size, s, PRESENT);
//        }
//        private static final long serialVersionUID = -2479143000061671589L;
//    }
//}
