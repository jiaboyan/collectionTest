//package com.jiaboyan.collection.map;
//
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/8/20.
// */
//public class TreeMapCodeSource {
//
//    public class TreeMap<K,V>
//            extends AbstractMap<K,V>
//            implements NavigableMap<K,V>, Cloneable, java.io.Serializable {
//
//        //自定义的比较器：
//        private final Comparator<? super K> comparator;
//
//        //红黑树的根节点：
//        private transient java.util.TreeMap.Entry<K,V> root = null;
//
//        //集合元素数量：
//        private transient int size = 0;
//
//        //对TreeMap操作的数量：
//        private transient int modCount = 0;
//
//        //无参构造方法：comparator属性置为null
//        //代表使用key的自然顺序来维持TreeMap的顺序，这里要求key必须实现Comparable接口
//        public TreeMap() {
//            comparator = null;
//        }
//
//        //带有比较器的构造方法：初始化comparator属性；
//        public TreeMap(Comparator<? super K> comparator) {
//            this.comparator = comparator;
//        }
//
//        //带有map的构造方法：
//        //同样比较器comparator为空，使用key的自然顺序排序
//        public TreeMap(Map<? extends K, ? extends V> m) {
//            comparator = null;
//            putAll(m);
//        }
//
//        //带有SortedMap的构造方法:
//        //根据SortedMap的比较器来来维持TreeMap的顺序
//        public TreeMap(SortedMap<K, ? extends V> m) {
//            comparator = m.comparator();
//            try {
//                buildFromSorted(m.size(), m.entrySet().iterator(), null, null);
//            } catch (java.io.IOException cannotHappen) {
//            } catch (ClassNotFoundException cannotHappen) {
//            }
//        }
//
//        //返回TreeMap中Entry的长度：
//        public int size() {
//            return size;
//        }
//
//        //判断TreeMap中是否包含了 key；
//        public boolean containsKey(Object key) {
//            //通过key获取Entry对象的核心方法：
//            return getEntry(key) != null;
//        }
//
//        //判断Treemap是否包含这个value:
//        public boolean containsValue(Object value) {
//            for (java.util.TreeMap.Entry<K,V> e = getFirstEntry(); e != null; e = successor(e))
//                if (valEquals(value, e.value))
//                    return true;
//            return false;
//        }
//
//        //通过key获取对应的value：
//        public V get(Object key) {
//            java.util.TreeMap.Entry<K,V> p = getEntry(key);
//            return (p==null ? null : p.value);
//        }
//
//        //返回自定义比较器：
//        public Comparator<? super K> comparator() {
//            return comparator;
//        }
//
//        public K firstKey() {
//            return key(getFirstEntry());
//        }
//
//        public K lastKey() {
//            return key(getLastEntry());
//        }
//
//        public void putAll(Map<? extends K, ? extends V> map) {
//            int mapSize = map.size();
//            if (size==0 && mapSize!=0 && map instanceof SortedMap) {
//                Comparator c = ((SortedMap)map).comparator();
//                if (c == comparator || (c != null && c.equals(comparator))) {
//                    ++modCount;
//                    try {
//                        buildFromSorted(mapSize, map.entrySet().iterator(),
//                                null, null);
//                    } catch (java.io.IOException cannotHappen) {
//                    } catch (ClassNotFoundException cannotHappen) {
//                    }
//                    return;
//                }
//            }
//            super.putAll(map);
//        }
//
//        //通过key获取Entry对象：
//        final java.util.TreeMap.Entry<K,V> getEntry(Object key) {
//            // 如果比较器为空，只是用key作为比较器查询
//            if (comparator != null)
//                return getEntryUsingComparator(key);
//
//            //如果key为null，则抛出异常：
//            if (key == null)
//                throw new NullPointerException();
//
//            //将传入的key转换成Comparable对象：  传入的key必须实现Comparable接口
//            Comparable<? super K> k = (Comparable<? super K>) key;
//            // 取得root节点
//            java.util.TreeMap.Entry<K,V> p = root;
//            // 从root节点开始查找，根据比较器判断是在左子树还是右子树
//            //依次遍历左边/右边的节点：
//            while (p != null) {
//                //传入的key与根节点的key进行大小比较：
//                int cmp = k.compareTo(p.key);
//                //传入的key 小于 根节点的key，小于0
//                if (cmp < 0)
//                    //左边
//                    p = p.left;
//                else if (cmp > 0)
//                    //传入的key 大于 根节点的key，大于0
//                    //右边
//                    p = p.right;
//                else
//                    //传入的key 等于 根节点的key，等于0
//                    //中间
//                    return p;
//            }
//            //如果p节点为null,则返回null
//            return null;
//        }
//
//        final java.util.TreeMap.Entry<K,V> getEntryUsingComparator(Object key) {
//            K k = (K) key;
//            Comparator<? super K> cpr = comparator;
//            if (cpr != null) {
//                java.util.TreeMap.Entry<K,V> p = root;
//                while (p != null) {
//                    int cmp = cpr.compare(k, p.key);
//                    if (cmp < 0)
//                        p = p.left;
//                    else if (cmp > 0)
//                        p = p.right;
//                    else
//                        return p;
//                }
//            }
//            return null;
//        }
//
//        final java.util.TreeMap.Entry<K,V> getCeilingEntry(K key) {
//            java.util.TreeMap.Entry<K,V> p = root;
//            while (p != null) {
//                int cmp = compare(key, p.key);
//                if (cmp < 0) {
//                    if (p.left != null)
//                        p = p.left;
//                    else
//                        return p;
//                } else if (cmp > 0) {
//                    if (p.right != null) {
//                        p = p.right;
//                    } else {
//                        java.util.TreeMap.Entry<K,V> parent = p.parent;
//                        java.util.TreeMap.Entry<K,V> ch = p;
//                        while (parent != null && ch == parent.right) {
//                            ch = parent;
//                            parent = parent.parent;
//                        }
//                        return parent;
//                    }
//                } else
//                    return p;
//            }
//            return null;
//        }
//
//        final java.util.TreeMap.Entry<K,V> getFloorEntry(K key) {
//            java.util.TreeMap.Entry<K,V> p = root;
//            while (p != null) {
//                int cmp = compare(key, p.key);
//                if (cmp > 0) {
//                    if (p.right != null)
//                        p = p.right;
//                    else
//                        return p;
//                } else if (cmp < 0) {
//                    if (p.left != null) {
//                        p = p.left;
//                    } else {
//                        java.util.TreeMap.Entry<K,V> parent = p.parent;
//                        java.util.TreeMap.Entry<K,V> ch = p;
//                        while (parent != null && ch == parent.left) {
//                            ch = parent;
//                            parent = parent.parent;
//                        }
//                        return parent;
//                    }
//                } else
//                    return p;
//
//            }
//            return null;
//        }
//
//        final java.util.TreeMap.Entry<K,V> getHigherEntry(K key) {
//            java.util.TreeMap.Entry<K,V> p = root;
//            while (p != null) {
//                int cmp = compare(key, p.key);
//                if (cmp < 0) {
//                    if (p.left != null)
//                        p = p.left;
//                    else
//                        return p;
//                } else {
//                    if (p.right != null) {
//                        p = p.right;
//                    } else {
//                        java.util.TreeMap.Entry<K,V> parent = p.parent;
//                        java.util.TreeMap.Entry<K,V> ch = p;
//                        while (parent != null && ch == parent.right) {
//                            ch = parent;
//                            parent = parent.parent;
//                        }
//                        return parent;
//                    }
//                }
//            }
//            return null;
//        }
//
//        final java.util.TreeMap.Entry<K,V> getLowerEntry(K key) {
//            java.util.TreeMap.Entry<K,V> p = root;
//            while (p != null) {
//                int cmp = compare(key, p.key);
//                if (cmp > 0) {
//                    if (p.right != null)
//                        p = p.right;
//                    else
//                        return p;
//                } else {
//                    if (p.left != null) {
//                        p = p.left;
//                    } else {
//                        java.util.TreeMap.Entry<K,V> parent = p.parent;
//                        java.util.TreeMap.Entry<K,V> ch = p;
//                        while (parent != null && ch == parent.left) {
//                            ch = parent;
//                            parent = parent.parent;
//                        }
//                        return parent;
//                    }
//                }
//            }
//            return null;
//        }
//
//        //插入key-value:
//        public V put(K key, V value) {
//            //根节点赋值给t:
//            java.util.TreeMap.Entry<K,V> t = root;
//            //如果根节点为null，则创建第一个节点，根节点
//            if (t == null) {
//                //对传入的元素进行比较，验证传入的元素是否实现了Comparable接口；
//                //或者TreeMap定义了Comparator
//                compare(key, key);
//                //根节点赋值：
//                root = new java.util.TreeMap.Entry<>(key, value, null);
//                //长度为1：
//                size = 1;
//                //修改次数+1
//                modCount++;
//                return null;
//            }
//
//            int cmp;
//            java.util.TreeMap.Entry<K,V> parent;
//            Comparator<? super K> cpr = comparator;
//
//            //判断TreeMap中自定义比较器comparator是否为null：
//            if (cpr != null) {
//                // do while循环，查找key节点的父节点
//                do {
//                    // 记录上次循环的节点t，首先将根节点赋值给parent:
//                    parent = t;
//                    //利用自定义比较器的比较方法：传入的key跟当前遍历节点比较：
//                    cmp = cpr.compare(key, t.key);
//                    //left左边 小于父节点： 小于0
//                    if (cmp < 0)
//                        //如果传入的key小于当前节点，则将t设置为当前节点的左节点；
//                        t = t.left;
//                    else if (cmp > 0)//right右边 大于父节点：大于0
//                        //如果传入的key大于当前节点，则将t设置为当前节点的右节点；
//                        t = t.right;
//                    else
//                        //覆盖原有节点的value：
//                        return t.setValue(value);
//                // 只有当t为null，也就是没有要比较节点的时候，代表已经找到新节点要插入的位置
//                } while (t != null);
//            } else {
//                //通常使用默认构造创建TreeMap的，都会走到此处：
//                //key为null，抛异常：
//                if (key == null)
//                    throw new NullPointerException();
//                //将key转换为Comparable对象：
//                Comparable<? super K> k = (Comparable<? super K>) key;
//                //同上：
//                do {
//                    parent = t;
//                    cmp = k.compareTo(t.key);
//                    if (cmp < 0)
//                        t = t.left;
//                    else if (cmp > 0)
//                        t = t.right;
//                    else
//                        return t.setValue(value);
//                } while (t != null);
//            }
//
//            //将传入的key--value创建一个Entry对象：
//            java.util.TreeMap.Entry<K,V> e = new java.util.TreeMap.Entry<>(key, value, parent);
//
//            //根据之前的比较结果，判断key的节点在父节点的左边还是右边
//            if (cmp < 0)
//                // 如果新节点key的值小于父节点key的值，则插在父节点的左侧
//                parent.left = e;
//            else
//                // 如果新节点key的值大于父节点key的值，则插在父节点的右侧
//                parent.right = e;
//            //核心方法：插入新的节点后，为了保持红黑树平衡，对红黑树进行调整
//            fixAfterInsertion(e);
//            size++;
//            modCount++;
//            return null;
//        }
//
//        public V remove(Object key) {
//            java.util.TreeMap.Entry<K,V> p = getEntry(key);
//            if (p == null)
//                return null;
//
//            V oldValue = p.value;
//            deleteEntry(p);
//            return oldValue;
//        }
//
//        public void clear() {
//            modCount++;
//            size = 0;
//            root = null;
//        }
//
//        public Object clone() {
//            java.util.TreeMap<K,V> clone = null;
//            try {
//                clone = (java.util.TreeMap<K,V>) super.clone();
//            } catch (CloneNotSupportedException e) {
//                throw new InternalError();
//            }
//
//            clone.root = null;
//            clone.size = 0;
//            clone.modCount = 0;
//            clone.entrySet = null;
//            clone.navigableKeySet = null;
//            clone.descendingMap = null;
//
//            try {
//                clone.buildFromSorted(size, entrySet().iterator(), null, null);
//            } catch (java.io.IOException cannotHappen) {
//            } catch (ClassNotFoundException cannotHappen) {
//            }
//
//            return clone;
//        }
//
//        public Map.Entry<K,V> firstEntry() {
//            return exportEntry(getFirstEntry());
//        }
//
//        public Map.Entry<K,V> lastEntry() {
//            return exportEntry(getLastEntry());
//        }
//
//        public Map.Entry<K,V> pollFirstEntry() {
//            java.util.TreeMap.Entry<K,V> p = getFirstEntry();
//            Map.Entry<K,V> result = exportEntry(p);
//            if (p != null)
//                deleteEntry(p);
//            return result;
//        }
//
//        public Map.Entry<K,V> pollLastEntry() {
//            java.util.TreeMap.Entry<K,V> p = getLastEntry();
//            Map.Entry<K,V> result = exportEntry(p);
//            if (p != null)
//                deleteEntry(p);
//            return result;
//        }
//
//        public Map.Entry<K,V> lowerEntry(K key) {
//            return exportEntry(getLowerEntry(key));
//        }
//
//        public K lowerKey(K key) {
//            return keyOrNull(getLowerEntry(key));
//        }
//
//        public Map.Entry<K,V> floorEntry(K key) {
//            return exportEntry(getFloorEntry(key));
//        }
//
//        public K floorKey(K key) {
//            return keyOrNull(getFloorEntry(key));
//        }
//
//        public Map.Entry<K,V> ceilingEntry(K key) {
//            return exportEntry(getCeilingEntry(key));
//        }
//
//        public K ceilingKey(K key) {
//            return keyOrNull(getCeilingEntry(key));
//        }
//
//        public Map.Entry<K,V> higherEntry(K key) {
//            return exportEntry(getHigherEntry(key));
//        }
//
//        public K higherKey(K key) {
//            return keyOrNull(getHigherEntry(key));
//        }
//
//        private transient java.util.TreeMap.EntrySet entrySet = null;
//        private transient java.util.TreeMap.KeySet<K> navigableKeySet = null;
//        private transient NavigableMap<K,V> descendingMap = null;
//
//        public Set<K> keySet() {
//            return navigableKeySet();
//        }
//
//        public NavigableSet<K> navigableKeySet() {
//            java.util.TreeMap.KeySet<K> nks = navigableKeySet;
//            return (nks != null) ? nks : (navigableKeySet = new java.util.TreeMap.KeySet(this));
//        }
//
//        public NavigableSet<K> descendingKeySet() {
//            return descendingMap().navigableKeySet();
//        }
//
//        public Collection<V> values() {
//            Collection<V> vs = values;
//            return (vs != null) ? vs : (values = new java.util.TreeMap.Values());
//        }
//
//        public Set<Map.Entry<K,V>> entrySet() {
//            java.util.TreeMap.EntrySet es = entrySet;
//            return (es != null) ? es : (entrySet = new java.util.TreeMap.EntrySet());
//        }
//
//        public NavigableMap<K, V> descendingMap() {
//            NavigableMap<K, V> km = descendingMap;
//            return (km != null) ? km :
//                    (descendingMap = new java.util.TreeMap.DescendingSubMap(this,
//                            true, null, true,
//                            true, null, true));
//        }
//
//        public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
//                                        K toKey,   boolean toInclusive) {
//            return new java.util.TreeMap.AscendingSubMap(this,
//                    false, fromKey, fromInclusive,
//                    false, toKey,   toInclusive);
//        }
//
//        public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
//            return new java.util.TreeMap.AscendingSubMap(this,
//                    true,  null,  true,
//                    false, toKey, inclusive);
//        }
//
//        public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
//            return new java.util.TreeMap.AscendingSubMap(this,
//                    false, fromKey, inclusive,
//                    true,  null,    true);
//        }
//
//        public SortedMap<K,V> subMap(K fromKey, K toKey) {
//            return subMap(fromKey, true, toKey, false);
//        }
//
//        public SortedMap<K,V> headMap(K toKey) {
//            return headMap(toKey, false);
//        }
//
//        public SortedMap<K,V> tailMap(K fromKey) {
//            return tailMap(fromKey, true);
//        }
//
//        class Values extends AbstractCollection<V> {
//            public Iterator<V> iterator() {
//                return new java.util.TreeMap.ValueIterator(getFirstEntry());
//            }
//
//            public int size() {
//                return java.util.TreeMap.this.size();
//            }
//
//            public boolean contains(Object o) {
//                return java.util.TreeMap.this.containsValue(o);
//            }
//
//            public boolean remove(Object o) {
//                for (java.util.TreeMap.Entry<K,V> e = getFirstEntry(); e != null; e = successor(e)) {
//                    if (valEquals(e.getValue(), o)) {
//                        deleteEntry(e);
//                        return true;
//                    }
//                }
//                return false;
//            }
//
//            public void clear() {
//                java.util.TreeMap.this.clear();
//            }
//        }
//
//        class EntrySet extends AbstractSet<Map.Entry<K,V>> {
//            public Iterator<Map.Entry<K,V>> iterator() {
//                return new java.util.TreeMap.EntryIterator(getFirstEntry());
//            }
//
//            public boolean contains(Object o) {
//                if (!(o instanceof Map.Entry))
//                    return false;
//                Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
//                V value = entry.getValue();
//                java.util.TreeMap.Entry<K,V> p = getEntry(entry.getKey());
//                return p != null && valEquals(p.getValue(), value);
//            }
//
//            public boolean remove(Object o) {
//                if (!(o instanceof Map.Entry))
//                    return false;
//                Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
//                V value = entry.getValue();
//                java.util.TreeMap.Entry<K,V> p = getEntry(entry.getKey());
//                if (p != null && valEquals(p.getValue(), value)) {
//                    deleteEntry(p);
//                    return true;
//                }
//                return false;
//            }
//
//            public int size() {
//                return java.util.TreeMap.this.size();
//            }
//
//            public void clear() {
//                java.util.TreeMap.this.clear();
//            }
//        }
//
//    /*
//     * Unlike Values and EntrySet, the KeySet class is static,
//     * delegating to a NavigableMap to allow use by SubMaps, which
//     * outweighs the ugliness of needing type-tests for the following
//     * Iterator methods that are defined appropriately in main versus
//     * submap classes.
//     */
//
//        Iterator<K> keyIterator() {
//            return new java.util.TreeMap.KeyIterator(getFirstEntry());
//        }
//
//        Iterator<K> descendingKeyIterator() {
//            return new java.util.TreeMap.DescendingKeyIterator(getLastEntry());
//        }
//
//        static final class KeySet<E> extends AbstractSet<E> implements NavigableSet<E> {
//            private final NavigableMap<E, Object> m;
//            KeySet(NavigableMap<E,Object> map) { m = map; }
//
//            public Iterator<E> iterator() {
//                if (m instanceof java.util.TreeMap)
//                    return ((java.util.TreeMap<E,Object>)m).keyIterator();
//                else
//                    return (Iterator<E>)(((java.util.TreeMap.NavigableSubMap)m).keyIterator());
//            }
//
//            public Iterator<E> descendingIterator() {
//                if (m instanceof java.util.TreeMap)
//                    return ((java.util.TreeMap<E,Object>)m).descendingKeyIterator();
//                else
//                    return (Iterator<E>)(((java.util.TreeMap.NavigableSubMap)m).descendingKeyIterator());
//            }
//
//            public int size() { return m.size(); }
//            public boolean isEmpty() { return m.isEmpty(); }
//            public boolean contains(Object o) { return m.containsKey(o); }
//            public void clear() { m.clear(); }
//            public E lower(E e) { return m.lowerKey(e); }
//            public E floor(E e) { return m.floorKey(e); }
//            public E ceiling(E e) { return m.ceilingKey(e); }
//            public E higher(E e) { return m.higherKey(e); }
//            public E first() { return m.firstKey(); }
//            public E last() { return m.lastKey(); }
//            public Comparator<? super E> comparator() { return m.comparator(); }
//            public E pollFirst() {
//                Map.Entry<E,Object> e = m.pollFirstEntry();
//                return (e == null) ? null : e.getKey();
//            }
//            public E pollLast() {
//                Map.Entry<E,Object> e = m.pollLastEntry();
//                return (e == null) ? null : e.getKey();
//            }
//            public boolean remove(Object o) {
//                int oldSize = size();
//                m.remove(o);
//                return size() != oldSize;
//            }
//            public NavigableSet<E> subSet(E fromElement, boolean fromInclusive,
//                                          E toElement,   boolean toInclusive) {
//                return new java.util.TreeMap.KeySet<>(m.subMap(fromElement, fromInclusive,
//                        toElement,   toInclusive));
//            }
//            public NavigableSet<E> headSet(E toElement, boolean inclusive) {
//                return new java.util.TreeMap.KeySet<>(m.headMap(toElement, inclusive));
//            }
//            public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
//                return new java.util.TreeMap.KeySet<>(m.tailMap(fromElement, inclusive));
//            }
//            public SortedSet<E> subSet(E fromElement, E toElement) {
//                return subSet(fromElement, true, toElement, false);
//            }
//            public SortedSet<E> headSet(E toElement) {
//                return headSet(toElement, false);
//            }
//            public SortedSet<E> tailSet(E fromElement) {
//                return tailSet(fromElement, true);
//            }
//            public NavigableSet<E> descendingSet() {
//                return new java.util.TreeMap.KeySet(m.descendingMap());
//            }
//        }
//
//        /**
//         * Base class for TreeMap Iterators
//         */
//        abstract class PrivateEntryIterator<T> implements Iterator<T> {
//            java.util.TreeMap.Entry<K,V> next;
//            java.util.TreeMap.Entry<K,V> lastReturned;
//            int expectedModCount;
//
//            PrivateEntryIterator(java.util.TreeMap.Entry<K,V> first) {
//                expectedModCount = modCount;
//                lastReturned = null;
//                next = first;
//            }
//
//            public final boolean hasNext() {
//                return next != null;
//            }
//
//            final java.util.TreeMap.Entry<K,V> nextEntry() {
//                java.util.TreeMap.Entry<K,V> e = next;
//                if (e == null)
//                    throw new NoSuchElementException();
//                if (modCount != expectedModCount)
//                    throw new ConcurrentModificationException();
//                next = successor(e);
//                lastReturned = e;
//                return e;
//            }
//
//            final java.util.TreeMap.Entry<K,V> prevEntry() {
//                java.util.TreeMap.Entry<K,V> e = next;
//                if (e == null)
//                    throw new NoSuchElementException();
//                if (modCount != expectedModCount)
//                    throw new ConcurrentModificationException();
//                next = predecessor(e);
//                lastReturned = e;
//                return e;
//            }
//
//            public void remove() {
//                if (lastReturned == null)
//                    throw new IllegalStateException();
//                if (modCount != expectedModCount)
//                    throw new ConcurrentModificationException();
//                // deleted entries are replaced by their successors
//                if (lastReturned.left != null && lastReturned.right != null)
//                    next = lastReturned;
//                deleteEntry(lastReturned);
//                expectedModCount = modCount;
//                lastReturned = null;
//            }
//        }
//
//        final class EntryIterator extends java.util.TreeMap.PrivateEntryIterator<Map.Entry<K,V>> {
//            EntryIterator(java.util.TreeMap.Entry<K,V> first) {
//                super(first);
//            }
//            public Map.Entry<K,V> next() {
//                return nextEntry();
//            }
//        }
//
//        final class ValueIterator extends java.util.TreeMap.PrivateEntryIterator<V> {
//            ValueIterator(java.util.TreeMap.Entry<K,V> first) {
//                super(first);
//            }
//            public V next() {
//                return nextEntry().value;
//            }
//        }
//
//        final class KeyIterator extends java.util.TreeMap.PrivateEntryIterator<K> {
//            KeyIterator(java.util.TreeMap.Entry<K,V> first) {
//                super(first);
//            }
//            public K next() {
//                return nextEntry().key;
//            }
//        }
//
//        final class DescendingKeyIterator extends java.util.TreeMap.PrivateEntryIterator<K> {
//            DescendingKeyIterator(java.util.TreeMap.Entry<K,V> first) {
//                super(first);
//            }
//            public K next() {
//                return prevEntry().key;
//            }
//        }
//
//        //对插入的元素比较：如果自定义比较为null，则调用调用默认自然顺序比较
//        //如果自定义比较器不为null，则用自定义比较器对元素进行比较；
//        final int compare(Object k1, Object k2) {
//            return comparator==null ? ((Comparable<? super K>)k1).compareTo((K)k2)
//                    : comparator.compare((K)k1, (K)k2);
//        }
//
//        /**
//         * Test two values for equality.  Differs from o1.equals(o2) only in
//         * that it copes with {@code null} o1 properly.
//         */
//        static final boolean valEquals(Object o1, Object o2) {
//            return (o1==null ? o2==null : o1.equals(o2));
//        }
//
//        /**
//         * Return SimpleImmutableEntry for entry, or null if null
//         */
//        static <K,V> Map.Entry<K,V> exportEntry(java.util.TreeMap.Entry<K,V> e) {
//            return (e == null) ? null :
//                    new AbstractMap.SimpleImmutableEntry<>(e);
//        }
//
//        /**
//         * Return key for entry, or null if null
//         */
//        static <K,V> K keyOrNull(java.util.TreeMap.Entry<K,V> e) {
//            return (e == null) ? null : e.key;
//        }
//
//        /**
//         * Returns the key corresponding to the specified Entry.
//         * @throws NoSuchElementException if the Entry is null
//         */
//        static <K> K key(java.util.TreeMap.Entry<K,?> e) {
//            if (e==null)
//                throw new NoSuchElementException();
//            return e.key;
//        }
//
//
//        // SubMaps
//
//        /**
//         * Dummy value serving as unmatchable fence key for unbounded
//         * SubMapIterators
//         */
//        private static final Object UNBOUNDED = new Object();
//
//        /**
//         * @serial include
//         */
//        abstract static class NavigableSubMap<K,V> extends AbstractMap<K,V>
//                implements NavigableMap<K,V>, java.io.Serializable {
//            /**
//             * The backing map.
//             */
//            final java.util.TreeMap<K,V> m;
//
//            /**
//             * Endpoints are represented as triples (fromStart, lo,
//             * loInclusive) and (toEnd, hi, hiInclusive). If fromStart is
//             * true, then the low (absolute) bound is the start of the
//             * backing map, and the other values are ignored. Otherwise,
//             * if loInclusive is true, lo is the inclusive bound, else lo
//             * is the exclusive bound. Similarly for the upper bound.
//             */
//            final K lo, hi;
//            final boolean fromStart, toEnd;
//            final boolean loInclusive, hiInclusive;
//
//            NavigableSubMap(java.util.TreeMap<K,V> m,
//                            boolean fromStart, K lo, boolean loInclusive,
//                            boolean toEnd, K hi, boolean hiInclusive) {
//                if (!fromStart && !toEnd) {
//                    if (m.compare(lo, hi) > 0)
//                        throw new IllegalArgumentException("fromKey > toKey");
//                } else {
//                    if (!fromStart) // type check
//                        m.compare(lo, lo);
//                    if (!toEnd)
//                        m.compare(hi, hi);
//                }
//
//                this.m = m;
//                this.fromStart = fromStart;
//                this.lo = lo;
//                this.loInclusive = loInclusive;
//                this.toEnd = toEnd;
//                this.hi = hi;
//                this.hiInclusive = hiInclusive;
//            }
//
//            // internal utilities
//
//            final boolean tooLow(Object key) {
//                if (!fromStart) {
//                    int c = m.compare(key, lo);
//                    if (c < 0 || (c == 0 && !loInclusive))
//                        return true;
//                }
//                return false;
//            }
//
//            final boolean tooHigh(Object key) {
//                if (!toEnd) {
//                    int c = m.compare(key, hi);
//                    if (c > 0 || (c == 0 && !hiInclusive))
//                        return true;
//                }
//                return false;
//            }
//
//            final boolean inRange(Object key) {
//                return !tooLow(key) && !tooHigh(key);
//            }
//
//            final boolean inClosedRange(Object key) {
//                return (fromStart || m.compare(key, lo) >= 0)
//                        && (toEnd || m.compare(hi, key) >= 0);
//            }
//
//            final boolean inRange(Object key, boolean inclusive) {
//                return inclusive ? inRange(key) : inClosedRange(key);
//            }
//
//        /*
//         * Absolute versions of relation operations.
//         * Subclasses map to these using like-named "sub"
//         * versions that invert senses for descending maps
//         */
//
//            final java.util.TreeMap.Entry<K,V> absLowest() {
//                java.util.TreeMap.Entry<K,V> e =
//                        (fromStart ?  m.getFirstEntry() :
//                                (loInclusive ? m.getCeilingEntry(lo) :
//                                        m.getHigherEntry(lo)));
//                return (e == null || tooHigh(e.key)) ? null : e;
//            }
//
//            final java.util.TreeMap.Entry<K,V> absHighest() {
//                java.util.TreeMap.Entry<K,V> e =
//                        (toEnd ?  m.getLastEntry() :
//                                (hiInclusive ?  m.getFloorEntry(hi) :
//                                        m.getLowerEntry(hi)));
//                return (e == null || tooLow(e.key)) ? null : e;
//            }
//
//            final java.util.TreeMap.Entry<K,V> absCeiling(K key) {
//                if (tooLow(key))
//                    return absLowest();
//                java.util.TreeMap.Entry<K,V> e = m.getCeilingEntry(key);
//                return (e == null || tooHigh(e.key)) ? null : e;
//            }
//
//            final java.util.TreeMap.Entry<K,V> absHigher(K key) {
//                if (tooLow(key))
//                    return absLowest();
//                java.util.TreeMap.Entry<K,V> e = m.getHigherEntry(key);
//                return (e == null || tooHigh(e.key)) ? null : e;
//            }
//
//            final java.util.TreeMap.Entry<K,V> absFloor(K key) {
//                if (tooHigh(key))
//                    return absHighest();
//                java.util.TreeMap.Entry<K,V> e = m.getFloorEntry(key);
//                return (e == null || tooLow(e.key)) ? null : e;
//            }
//
//            final java.util.TreeMap.Entry<K,V> absLower(K key) {
//                if (tooHigh(key))
//                    return absHighest();
//                java.util.TreeMap.Entry<K,V> e = m.getLowerEntry(key);
//                return (e == null || tooLow(e.key)) ? null : e;
//            }
//
//            /** Returns the absolute high fence for ascending traversal */
//            final java.util.TreeMap.Entry<K,V> absHighFence() {
//                return (toEnd ? null : (hiInclusive ?
//                        m.getHigherEntry(hi) :
//                        m.getCeilingEntry(hi)));
//            }
//
//            /** Return the absolute low fence for descending traversal  */
//            final java.util.TreeMap.Entry<K,V> absLowFence() {
//                return (fromStart ? null : (loInclusive ?
//                        m.getLowerEntry(lo) :
//                        m.getFloorEntry(lo)));
//            }
//
//            // Abstract methods defined in ascending vs descending classes
//            // These relay to the appropriate absolute versions
//
//            abstract java.util.TreeMap.Entry<K,V> subLowest();
//            abstract java.util.TreeMap.Entry<K,V> subHighest();
//            abstract java.util.TreeMap.Entry<K,V> subCeiling(K key);
//            abstract java.util.TreeMap.Entry<K,V> subHigher(K key);
//            abstract java.util.TreeMap.Entry<K,V> subFloor(K key);
//            abstract java.util.TreeMap.Entry<K,V> subLower(K key);
//
//            /** Returns ascending iterator from the perspective of this submap */
//            abstract Iterator<K> keyIterator();
//
//            /** Returns descending iterator from the perspective of this submap */
//            abstract Iterator<K> descendingKeyIterator();
//
//            // public methods
//
//            public boolean isEmpty() {
//                return (fromStart && toEnd) ? m.isEmpty() : entrySet().isEmpty();
//            }
//
//            public int size() {
//                return (fromStart && toEnd) ? m.size() : entrySet().size();
//            }
//
//            public final boolean containsKey(Object key) {
//                return inRange(key) && m.containsKey(key);
//            }
//
//            public final V put(K key, V value) {
//                if (!inRange(key))
//                    throw new IllegalArgumentException("key out of range");
//                return m.put(key, value);
//            }
//
//            public final V get(Object key) {
//                return !inRange(key) ? null :  m.get(key);
//            }
//
//            public final V remove(Object key) {
//                return !inRange(key) ? null : m.remove(key);
//            }
//
//            public final Map.Entry<K,V> ceilingEntry(K key) {
//                return exportEntry(subCeiling(key));
//            }
//
//            public final K ceilingKey(K key) {
//                return keyOrNull(subCeiling(key));
//            }
//
//            public final Map.Entry<K,V> higherEntry(K key) {
//                return exportEntry(subHigher(key));
//            }
//
//            public final K higherKey(K key) {
//                return keyOrNull(subHigher(key));
//            }
//
//            public final Map.Entry<K,V> floorEntry(K key) {
//                return exportEntry(subFloor(key));
//            }
//
//            public final K floorKey(K key) {
//                return keyOrNull(subFloor(key));
//            }
//
//            public final Map.Entry<K,V> lowerEntry(K key) {
//                return exportEntry(subLower(key));
//            }
//
//            public final K lowerKey(K key) {
//                return keyOrNull(subLower(key));
//            }
//
//            public final K firstKey() {
//                return key(subLowest());
//            }
//
//            public final K lastKey() {
//                return key(subHighest());
//            }
//
//            public final Map.Entry<K,V> firstEntry() {
//                return exportEntry(subLowest());
//            }
//
//            public final Map.Entry<K,V> lastEntry() {
//                return exportEntry(subHighest());
//            }
//
//            public final Map.Entry<K,V> pollFirstEntry() {
//                java.util.TreeMap.Entry<K,V> e = subLowest();
//                Map.Entry<K,V> result = exportEntry(e);
//                if (e != null)
//                    m.deleteEntry(e);
//                return result;
//            }
//
//            public final Map.Entry<K,V> pollLastEntry() {
//                java.util.TreeMap.Entry<K,V> e = subHighest();
//                Map.Entry<K,V> result = exportEntry(e);
//                if (e != null)
//                    m.deleteEntry(e);
//                return result;
//            }
//
//            // Views
//            transient NavigableMap<K,V> descendingMapView = null;
//            transient java.util.TreeMap.NavigableSubMap.EntrySetView entrySetView = null;
//            transient java.util.TreeMap.KeySet<K> navigableKeySetView = null;
//
//            public final NavigableSet<K> navigableKeySet() {
//                java.util.TreeMap.KeySet<K> nksv = navigableKeySetView;
//                return (nksv != null) ? nksv :
//                        (navigableKeySetView = new java.util.TreeMap.KeySet(this));
//            }
//
//            public final Set<K> keySet() {
//                return navigableKeySet();
//            }
//
//            public NavigableSet<K> descendingKeySet() {
//                return descendingMap().navigableKeySet();
//            }
//
//            public final SortedMap<K,V> subMap(K fromKey, K toKey) {
//                return subMap(fromKey, true, toKey, false);
//            }
//
//            public final SortedMap<K,V> headMap(K toKey) {
//                return headMap(toKey, false);
//            }
//
//            public final SortedMap<K,V> tailMap(K fromKey) {
//                return tailMap(fromKey, true);
//            }
//
//            // View classes
//
//            abstract class EntrySetView extends AbstractSet<Map.Entry<K,V>> {
//                private transient int size = -1, sizeModCount;
//
//                public int size() {
//                    if (fromStart && toEnd)
//                        return m.size();
//                    if (size == -1 || sizeModCount != m.modCount) {
//                        sizeModCount = m.modCount;
//                        size = 0;
//                        Iterator i = iterator();
//                        while (i.hasNext()) {
//                            size++;
//                            i.next();
//                        }
//                    }
//                    return size;
//                }
//
//                public boolean isEmpty() {
//                    java.util.TreeMap.Entry<K,V> n = absLowest();
//                    return n == null || tooHigh(n.key);
//                }
//
//                public boolean contains(Object o) {
//                    if (!(o instanceof Map.Entry))
//                        return false;
//                    Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
//                    K key = entry.getKey();
//                    if (!inRange(key))
//                        return false;
//                    java.util.TreeMap.Entry node = m.getEntry(key);
//                    return node != null &&
//                            valEquals(node.getValue(), entry.getValue());
//                }
//
//                public boolean remove(Object o) {
//                    if (!(o instanceof Map.Entry))
//                        return false;
//                    Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
//                    K key = entry.getKey();
//                    if (!inRange(key))
//                        return false;
//                    java.util.TreeMap.Entry<K,V> node = m.getEntry(key);
//                    if (node!=null && valEquals(node.getValue(),
//                            entry.getValue())) {
//                        m.deleteEntry(node);
//                        return true;
//                    }
//                    return false;
//                }
//            }
//
//            /**
//             * Iterators for SubMaps
//             */
//            abstract class SubMapIterator<T> implements Iterator<T> {
//                java.util.TreeMap.Entry<K,V> lastReturned;
//                java.util.TreeMap.Entry<K,V> next;
//                final Object fenceKey;
//                int expectedModCount;
//
//                SubMapIterator(java.util.TreeMap.Entry<K,V> first,
//                               java.util.TreeMap.Entry<K,V> fence) {
//                    expectedModCount = m.modCount;
//                    lastReturned = null;
//                    next = first;
//                    fenceKey = fence == null ? UNBOUNDED : fence.key;
//                }
//
//                public final boolean hasNext() {
//                    return next != null && next.key != fenceKey;
//                }
//
//                final java.util.TreeMap.Entry<K,V> nextEntry() {
//                    java.util.TreeMap.Entry<K,V> e = next;
//                    if (e == null || e.key == fenceKey)
//                        throw new NoSuchElementException();
//                    if (m.modCount != expectedModCount)
//                        throw new ConcurrentModificationException();
//                    next = successor(e);
//                    lastReturned = e;
//                    return e;
//                }
//
//                final java.util.TreeMap.Entry<K,V> prevEntry() {
//                    java.util.TreeMap.Entry<K,V> e = next;
//                    if (e == null || e.key == fenceKey)
//                        throw new NoSuchElementException();
//                    if (m.modCount != expectedModCount)
//                        throw new ConcurrentModificationException();
//                    next = predecessor(e);
//                    lastReturned = e;
//                    return e;
//                }
//
//                final void removeAscending() {
//                    if (lastReturned == null)
//                        throw new IllegalStateException();
//                    if (m.modCount != expectedModCount)
//                        throw new ConcurrentModificationException();
//                    // deleted entries are replaced by their successors
//                    if (lastReturned.left != null && lastReturned.right != null)
//                        next = lastReturned;
//                    m.deleteEntry(lastReturned);
//                    lastReturned = null;
//                    expectedModCount = m.modCount;
//                }
//
//                final void removeDescending() {
//                    if (lastReturned == null)
//                        throw new IllegalStateException();
//                    if (m.modCount != expectedModCount)
//                        throw new ConcurrentModificationException();
//                    m.deleteEntry(lastReturned);
//                    lastReturned = null;
//                    expectedModCount = m.modCount;
//                }
//
//            }
//
//            final class SubMapEntryIterator extends java.util.TreeMap.NavigableSubMap.SubMapIterator<Entry<K,V>> {
//                SubMapEntryIterator(java.util.TreeMap.Entry<K,V> first,
//                                    java.util.TreeMap.Entry<K,V> fence) {
//                    super(first, fence);
//                }
//                public Map.Entry<K,V> next() {
//                    return nextEntry();
//                }
//                public void remove() {
//                    removeAscending();
//                }
//            }
//
//            final class SubMapKeyIterator extends java.util.TreeMap.NavigableSubMap.SubMapIterator<K> {
//                SubMapKeyIterator(java.util.TreeMap.Entry<K,V> first,
//                                  java.util.TreeMap.Entry<K,V> fence) {
//                    super(first, fence);
//                }
//                public K next() {
//                    return nextEntry().key;
//                }
//                public void remove() {
//                    removeAscending();
//                }
//            }
//
//            final class DescendingSubMapEntryIterator extends java.util.TreeMap.NavigableSubMap.SubMapIterator<Entry<K,V>> {
//                DescendingSubMapEntryIterator(java.util.TreeMap.Entry<K,V> last,
//                                              java.util.TreeMap.Entry<K,V> fence) {
//                    super(last, fence);
//                }
//
//                public Map.Entry<K,V> next() {
//                    return prevEntry();
//                }
//                public void remove() {
//                    removeDescending();
//                }
//            }
//
//            final class DescendingSubMapKeyIterator extends java.util.TreeMap.NavigableSubMap.SubMapIterator<K> {
//                DescendingSubMapKeyIterator(java.util.TreeMap.Entry<K,V> last,
//                                            java.util.TreeMap.Entry<K,V> fence) {
//                    super(last, fence);
//                }
//                public K next() {
//                    return prevEntry().key;
//                }
//                public void remove() {
//                    removeDescending();
//                }
//            }
//        }
//
//        /**
//         * @serial include
//         */
//        static final class AscendingSubMap<K,V> extends java.util.TreeMap.NavigableSubMap<K,V> {
//            private static final long serialVersionUID = 912986545866124060L;
//
//            AscendingSubMap(java.util.TreeMap<K,V> m,
//                            boolean fromStart, K lo, boolean loInclusive,
//                            boolean toEnd, K hi, boolean hiInclusive) {
//                super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
//            }
//
//            public Comparator<? super K> comparator() {
//                return m.comparator();
//            }
//
//            public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
//                                            K toKey,   boolean toInclusive) {
//                if (!inRange(fromKey, fromInclusive))
//                    throw new IllegalArgumentException("fromKey out of range");
//                if (!inRange(toKey, toInclusive))
//                    throw new IllegalArgumentException("toKey out of range");
//                return new java.util.TreeMap.AscendingSubMap(m,
//                        false, fromKey, fromInclusive,
//                        false, toKey,   toInclusive);
//            }
//
//            public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
//                if (!inRange(toKey, inclusive))
//                    throw new IllegalArgumentException("toKey out of range");
//                return new java.util.TreeMap.AscendingSubMap(m,
//                        fromStart, lo,    loInclusive,
//                        false,     toKey, inclusive);
//            }
//
//            public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
//                if (!inRange(fromKey, inclusive))
//                    throw new IllegalArgumentException("fromKey out of range");
//                return new java.util.TreeMap.AscendingSubMap(m,
//                        false, fromKey, inclusive,
//                        toEnd, hi,      hiInclusive);
//            }
//
//            public NavigableMap<K,V> descendingMap() {
//                NavigableMap<K,V> mv = descendingMapView;
//                return (mv != null) ? mv :
//                        (descendingMapView =
//                                new java.util.TreeMap.DescendingSubMap(m,
//                                        fromStart, lo, loInclusive,
//                                        toEnd,     hi, hiInclusive));
//            }
//
//            Iterator<K> keyIterator() {
//                return new SubMapKeyIterator(absLowest(), absHighFence());
//            }
//
//            Iterator<K> descendingKeyIterator() {
//                return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
//            }
//
//            final class AscendingEntrySetView extends EntrySetView {
//                public Iterator<Map.Entry<K,V>> iterator() {
//                    return new SubMapEntryIterator(absLowest(), absHighFence());
//                }
//            }
//
//            public Set<Map.Entry<K,V>> entrySet() {
//                EntrySetView es = entrySetView;
//                return (es != null) ? es : new java.util.TreeMap.AscendingSubMap.AscendingEntrySetView();
//            }
//
//            java.util.TreeMap.Entry<K,V> subLowest()       { return absLowest(); }
//            java.util.TreeMap.Entry<K,V> subHighest()      { return absHighest(); }
//            java.util.TreeMap.Entry<K,V> subCeiling(K key) { return absCeiling(key); }
//            java.util.TreeMap.Entry<K,V> subHigher(K key)  { return absHigher(key); }
//            java.util.TreeMap.Entry<K,V> subFloor(K key)   { return absFloor(key); }
//            java.util.TreeMap.Entry<K,V> subLower(K key)   { return absLower(key); }
//        }
//
//        /**
//         * @serial include
//         */
//        static final class DescendingSubMap<K,V>  extends java.util.TreeMap.NavigableSubMap<K,V> {
//            private static final long serialVersionUID = 912986545866120460L;
//            DescendingSubMap(java.util.TreeMap<K,V> m,
//                             boolean fromStart, K lo, boolean loInclusive,
//                             boolean toEnd, K hi, boolean hiInclusive) {
//                super(m, fromStart, lo, loInclusive, toEnd, hi, hiInclusive);
//            }
//
//            private final Comparator<? super K> reverseComparator =
//                    Collections.reverseOrder(m.comparator);
//
//            public Comparator<? super K> comparator() {
//                return reverseComparator;
//            }
//
//            public NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive,
//                                            K toKey,   boolean toInclusive) {
//                if (!inRange(fromKey, fromInclusive))
//                    throw new IllegalArgumentException("fromKey out of range");
//                if (!inRange(toKey, toInclusive))
//                    throw new IllegalArgumentException("toKey out of range");
//                return new java.util.TreeMap.DescendingSubMap(m,
//                        false, toKey,   toInclusive,
//                        false, fromKey, fromInclusive);
//            }
//
//            public NavigableMap<K,V> headMap(K toKey, boolean inclusive) {
//                if (!inRange(toKey, inclusive))
//                    throw new IllegalArgumentException("toKey out of range");
//                return new java.util.TreeMap.DescendingSubMap(m,
//                        false, toKey, inclusive,
//                        toEnd, hi,    hiInclusive);
//            }
//
//            public NavigableMap<K,V> tailMap(K fromKey, boolean inclusive) {
//                if (!inRange(fromKey, inclusive))
//                    throw new IllegalArgumentException("fromKey out of range");
//                return new java.util.TreeMap.DescendingSubMap(m,
//                        fromStart, lo, loInclusive,
//                        false, fromKey, inclusive);
//            }
//
//            public NavigableMap<K,V> descendingMap() {
//                NavigableMap<K,V> mv = descendingMapView;
//                return (mv != null) ? mv :
//                        (descendingMapView =
//                                new java.util.TreeMap.AscendingSubMap(m,
//                                        fromStart, lo, loInclusive,
//                                        toEnd,     hi, hiInclusive));
//            }
//
//            Iterator<K> keyIterator() {
//                return new DescendingSubMapKeyIterator(absHighest(), absLowFence());
//            }
//
//            Iterator<K> descendingKeyIterator() {
//                return new SubMapKeyIterator(absLowest(), absHighFence());
//            }
//
//            final class DescendingEntrySetView extends EntrySetView {
//                public Iterator<Map.Entry<K,V>> iterator() {
//                    return new DescendingSubMapEntryIterator(absHighest(), absLowFence());
//                }
//            }
//
//            public Set<Map.Entry<K,V>> entrySet() {
//                EntrySetView es = entrySetView;
//                return (es != null) ? es : new java.util.TreeMap.DescendingSubMap.DescendingEntrySetView();
//            }
//
//            java.util.TreeMap.Entry<K,V> subLowest()       { return absHighest(); }
//            java.util.TreeMap.Entry<K,V> subHighest()      { return absLowest(); }
//            java.util.TreeMap.Entry<K,V> subCeiling(K key) { return absFloor(key); }
//            java.util.TreeMap.Entry<K,V> subHigher(K key)  { return absLower(key); }
//            java.util.TreeMap.Entry<K,V> subFloor(K key)   { return absCeiling(key); }
//            java.util.TreeMap.Entry<K,V> subLower(K key)   { return absHigher(key); }
//        }
//
//        /**
//         * This class exists solely for the sake of serialization
//         * compatibility with previous releases of TreeMap that did not
//         * support NavigableMap.  It translates an old-version SubMap into
//         * a new-version AscendingSubMap. This class is never otherwise
//         * used.
//         *
//         * @serial include
//         */
//        private class SubMap extends AbstractMap<K,V>
//                implements SortedMap<K,V>, java.io.Serializable {
//            private static final long serialVersionUID = -6520786458950516097L;
//            private boolean fromStart = false, toEnd = false;
//            private K fromKey, toKey;
//            private Object readResolve() {
//                return new java.util.TreeMap.AscendingSubMap(java.util.TreeMap.this,
//                        fromStart, fromKey, true,
//                        toEnd, toKey, false);
//            }
//            public Set<Map.Entry<K,V>> entrySet() { throw new InternalError(); }
//            public K lastKey() { throw new InternalError(); }
//            public K firstKey() { throw new InternalError(); }
//            public SortedMap<K,V> subMap(K fromKey, K toKey) { throw new InternalError(); }
//            public SortedMap<K,V> headMap(K toKey) { throw new InternalError(); }
//            public SortedMap<K,V> tailMap(K fromKey) { throw new InternalError(); }
//            public Comparator<? super K> comparator() { throw new InternalError(); }
//        }
//
//
//        // Red-black mechanics
//
//        private static final boolean RED   = false;
//        private static final boolean BLACK = true;
//
//        /**
//         * Node in the Tree.  Doubles as a means to pass key-value pairs back to
//         * user (see Map.Entry).
//         */
//
//        static final class Entry<K,V> implements Map.Entry<K,V> {
//            K key;
//            V value;
//            java.util.TreeMap.Entry<K,V> left = null;
//            java.util.TreeMap.Entry<K,V> right = null;
//            java.util.TreeMap.Entry<K,V> parent;
//            boolean color = BLACK;
//
//            /**
//             * Make a new cell with given key, value, and parent, and with
//             * {@code null} child links, and BLACK color.
//             */
//            Entry(K key, V value, java.util.TreeMap.Entry<K,V> parent) {
//                this.key = key;
//                this.value = value;
//                this.parent = parent;
//            }
//
//            /**
//             * Returns the key.
//             *
//             * @return the key
//             */
//            public K getKey() {
//                return key;
//            }
//
//            /**
//             * Returns the value associated with the key.
//             *
//             * @return the value associated with the key
//             */
//            public V getValue() {
//                return value;
//            }
//
//            /**
//             * Replaces the value currently associated with the key with the given
//             * value.
//             *
//             * @return the value associated with the key before this method was
//             *         called
//             */
//            public V setValue(V value) {
//                V oldValue = this.value;
//                this.value = value;
//                return oldValue;
//            }
//
//            public boolean equals(Object o) {
//                if (!(o instanceof Map.Entry))
//                    return false;
//                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
//
//                return valEquals(key,e.getKey()) && valEquals(value,e.getValue());
//            }
//
//            public int hashCode() {
//                int keyHash = (key==null ? 0 : key.hashCode());
//                int valueHash = (value==null ? 0 : value.hashCode());
//                return keyHash ^ valueHash;
//            }
//
//            public String toString() {
//                return key + "=" + value;
//            }
//        }
//
//        /**
//         * Returns the first Entry in the TreeMap (according to the TreeMap's
//         * key-sort function).  Returns null if the TreeMap is empty.
//         */
//        final java.util.TreeMap.Entry<K,V> getFirstEntry() {
//            java.util.TreeMap.Entry<K,V> p = root;
//            if (p != null)
//                while (p.left != null)
//                    p = p.left;
//            return p;
//        }
//
//        /**
//         * Returns the last Entry in the TreeMap (according to the TreeMap's
//         * key-sort function).  Returns null if the TreeMap is empty.
//         */
//        final java.util.TreeMap.Entry<K,V> getLastEntry() {
//            java.util.TreeMap.Entry<K,V> p = root;
//            if (p != null)
//                while (p.right != null)
//                    p = p.right;
//            return p;
//        }
//
//        /**
//         * Returns the successor of the specified Entry, or null if no such.
//         */
//        static <K,V> java.util.TreeMap.Entry<K,V> successor(java.util.TreeMap.Entry<K,V> t) {
//            if (t == null)
//                return null;
//            else if (t.right != null) {
//                java.util.TreeMap.Entry<K,V> p = t.right;
//                while (p.left != null)
//                    p = p.left;
//                return p;
//            } else {
//                java.util.TreeMap.Entry<K,V> p = t.parent;
//                java.util.TreeMap.Entry<K,V> ch = t;
//                while (p != null && ch == p.right) {
//                    ch = p;
//                    p = p.parent;
//                }
//                return p;
//            }
//        }
//
//        /**
//         * Returns the predecessor of the specified Entry, or null if no such.
//         */
//        static <K,V> java.util.TreeMap.Entry<K,V> predecessor(java.util.TreeMap.Entry<K,V> t) {
//            if (t == null)
//                return null;
//            else if (t.left != null) {
//                java.util.TreeMap.Entry<K,V> p = t.left;
//                while (p.right != null)
//                    p = p.right;
//                return p;
//            } else {
//                java.util.TreeMap.Entry<K,V> p = t.parent;
//                java.util.TreeMap.Entry<K,V> ch = t;
//                while (p != null && ch == p.left) {
//                    ch = p;
//                    p = p.parent;
//                }
//                return p;
//            }
//        }
//
//        private static <K,V> boolean colorOf(java.util.TreeMap.Entry<K,V> p) {
//            return (p == null ? BLACK : p.color);
//        }
//
//        private static <K,V> java.util.TreeMap.Entry<K,V> parentOf(java.util.TreeMap.Entry<K,V> p) {
//            return (p == null ? null: p.parent);
//        }
//
//        private static <K,V> void setColor(java.util.TreeMap.Entry<K,V> p, boolean c) {
//            if (p != null)
//                p.color = c;
//        }
//
//        private static <K,V> java.util.TreeMap.Entry<K,V> leftOf(java.util.TreeMap.Entry<K,V> p) {
//            return (p == null) ? null: p.left;
//        }
//
//        private static <K,V> java.util.TreeMap.Entry<K,V> rightOf(java.util.TreeMap.Entry<K,V> p) {
//            return (p == null) ? null: p.right;
//        }
//
//        /** From CLR */
//        private void rotateLeft(java.util.TreeMap.Entry<K,V> p) {
//            if (p != null) {
//                java.util.TreeMap.Entry<K,V> r = p.right;
//                p.right = r.left;
//                if (r.left != null)
//                    r.left.parent = p;
//                r.parent = p.parent;
//                if (p.parent == null)
//                    root = r;
//                else if (p.parent.left == p)
//                    p.parent.left = r;
//                else
//                    p.parent.right = r;
//                r.left = p;
//                p.parent = r;
//            }
//        }
//
//        private void rotateRight(java.util.TreeMap.Entry<K,V> p) {
//            if (p != null) {
//                java.util.TreeMap.Entry<K,V> l = p.left;
//                p.left = l.right;
//                if (l.right != null) l.right.parent = p;
//                l.parent = p.parent;
//                if (p.parent == null)
//                    root = l;
//                else if (p.parent.right == p)
//                    p.parent.right = l;
//                else p.parent.left = l;
//                l.right = p;
//                p.parent = l;
//            }
//        }
//
//        //核心方法：插入新的节点后，为了保持红黑树平衡，对红黑树进行调整
//        private void fixAfterInsertion(java.util.TreeMap.Entry<K,V> x) {
//            // 将新插入节点的颜色设置为红色
//            x.color = RED;
//            // while循环，保证新插入节点x不是根节点或者新插入节点x的父节点不是红色（这两种情况不需要调整）
//            while (x != null && x != root && x.parent.color == RED) {
//                //如果新插入节点x的父节点 是 x父节点的父节点的左孩子
//                if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
//                    //获取x父节点的父节点的右孩子：叔叔节点
//                    java.util.TreeMap.Entry<K,V> y = rightOf(parentOf(parentOf(x)));
//                    //如果y为红色：
//                    if (colorOf(y) == RED) {
//                        //将x的父节点设置黑色：
//                        setColor(parentOf(x), BLACK);  10
//                        //将y设置成黑色：
//                        setColor(y, BLACK);27
//                        //将x的父节点的父节点设置成红色：
//                        setColor(parentOf(parentOf(x)), RED);23
//                        // 将x指向x的父节点的父节点，判断是否需要下次循环---如果x的祖父节点的父节点是红色，按照上面的步奏继续循环
//                        x = parentOf(parentOf(x));
//                    } else {
//                        //x为其父节点的右孩子：
//                        if (x == rightOf(parentOf(x))) {
//                            x = parentOf(x);
//                            rotateLeft(x);
//                        }
//                        setColor(parentOf(x), BLACK);
//                        setColor(parentOf(parentOf(x)), RED);
//                        rotateRight(parentOf(parentOf(x)));
//                    }
//                } else {//如果新插入节点x的父节点 是 x父节点的父节点的右孩子
//
//                    //获取x父节点的父节点：
//                    java.util.TreeMap.Entry<K,V> y = leftOf(parentOf(parentOf(x)));
//                    if (colorOf(y) == RED) {
//                        setColor(parentOf(x), BLACK);12
//                        setColor(y, BLACK);5
//                        setColor(parentOf(parentOf(x)), RED);10
//                        x = parentOf(parentOf(x));
//                    } else {
//                        if (x == leftOf(parentOf(x))) {
//                            x = parentOf(x);
//                            rotateRight(x);
//                        }
//                        setColor(parentOf(x), BLACK);
//                        setColor(parentOf(parentOf(x)), RED);
//                        rotateLeft(parentOf(parentOf(x)));
//                    }
//                }
//            }
//            root.color = BLACK;
//        }
//
//
//        private void deleteEntry(java.util.TreeMap.Entry<K,V> p) {
//            modCount++;
//            size--;
//
//             if (p.left != null && p.right != null) {
//                java.util.TreeMap.Entry<K,V> s = successor(p);
//                p.key = s.key;
//                p.value = s.value;
//                p = s;
//            }
//
//            java.util.TreeMap.Entry<K,V> replacement = (p.left != null ? p.left : p.right);
//
//            if (replacement != null) {
//                // Link replacement to parent
//                replacement.parent = p.parent;
//                if (p.parent == null)
//                    root = replacement;
//                else if (p == p.parent.left)
//                    p.parent.left  = replacement;
//                else
//                    p.parent.right = replacement;
//
//                // Null out links so they are OK to use by fixAfterDeletion.
//                p.left = p.right = p.parent = null;
//
//                // Fix replacement
//                if (p.color == BLACK)
//                    fixAfterDeletion(replacement);
//            } else if (p.parent == null) { // return if we are the only node.
//                root = null;
//            } else { //  No children. Use self as phantom replacement and unlink.
//                if (p.color == BLACK)
//                    fixAfterDeletion(p);
//
//                if (p.parent != null) {
//                    if (p == p.parent.left)
//                        p.parent.left = null;
//                    else if (p == p.parent.right)
//                        p.parent.right = null;
//                    p.parent = null;
//                }
//            }
//        }
//
//        private void fixAfterDeletion(java.util.TreeMap.Entry<K,V> x) {
//            while (x != root && colorOf(x) == BLACK) {
//                if (x == leftOf(parentOf(x))) {
//                    java.util.TreeMap.Entry<K,V> sib = rightOf(parentOf(x));
//
//                    if (colorOf(sib) == RED) {
//                        setColor(sib, BLACK);
//                        setColor(parentOf(x), RED);
//                        rotateLeft(parentOf(x));
//                        sib = rightOf(parentOf(x));
//                    }
//
//                    if (colorOf(leftOf(sib))  == BLACK &&
//                            colorOf(rightOf(sib)) == BLACK) {
//                        setColor(sib, RED);
//                        x = parentOf(x);
//                    } else {
//                        if (colorOf(rightOf(sib)) == BLACK) {
//                            setColor(leftOf(sib), BLACK);
//                            setColor(sib, RED);
//                            rotateRight(sib);
//                            sib = rightOf(parentOf(x));
//                        }
//                        setColor(sib, colorOf(parentOf(x)));
//                        setColor(parentOf(x), BLACK);
//                        setColor(rightOf(sib), BLACK);
//                        rotateLeft(parentOf(x));
//                        x = root;
//                    }
//                } else { // symmetric
//                    java.util.TreeMap.Entry<K,V> sib = leftOf(parentOf(x));
//
//                    if (colorOf(sib) == RED) {
//                        setColor(sib, BLACK);
//                        setColor(parentOf(x), RED);
//                        rotateRight(parentOf(x));
//                        sib = leftOf(parentOf(x));
//                    }
//
//                    if (colorOf(rightOf(sib)) == BLACK &&
//                            colorOf(leftOf(sib)) == BLACK) {
//                        setColor(sib, RED);
//                        x = parentOf(x);
//                    } else {
//                        if (colorOf(leftOf(sib)) == BLACK) {
//                            setColor(rightOf(sib), BLACK);
//                            setColor(sib, RED);
//                            rotateLeft(sib);
//                            sib = leftOf(parentOf(x));
//                        }
//                        setColor(sib, colorOf(parentOf(x)));
//                        setColor(parentOf(x), BLACK);
//                        setColor(leftOf(sib), BLACK);
//                        rotateRight(parentOf(x));
//                        x = root;
//                    }
//                }
//            }
//
//            setColor(x, BLACK);
//        }
//
//        private static final long serialVersionUID = 919286545866124006L;
//
//        /**
//         * Save the state of the {@code TreeMap} instance to a stream (i.e.,
//         * serialize it).
//         *
//         * @serialData The <em>size</em> of the TreeMap (the number of key-value
//         *             mappings) is emitted (int), followed by the key (Object)
//         *             and value (Object) for each key-value mapping represented
//         *             by the TreeMap. The key-value mappings are emitted in
//         *             key-order (as determined by the TreeMap's Comparator,
//         *             or by the keys' natural ordering if the TreeMap has no
//         *             Comparator).
//         */
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws java.io.IOException {
//            // Write out the Comparator and any hidden stuff
//            s.defaultWriteObject();
//
//            // Write out size (number of Mappings)
//            s.writeInt(size);
//
//            // Write out keys and values (alternating)
//            for (Iterator<Map.Entry<K,V>> i = entrySet().iterator(); i.hasNext(); ) {
//                Map.Entry<K,V> e = i.next();
//                s.writeObject(e.getKey());
//                s.writeObject(e.getValue());
//            }
//        }
//
//        /**
//         * Reconstitute the {@code TreeMap} instance from a stream (i.e.,
//         * deserialize it).
//         */
//        private void readObject(final java.io.ObjectInputStream s)
//                throws java.io.IOException, ClassNotFoundException {
//            // Read in the Comparator and any hidden stuff
//            s.defaultReadObject();
//
//            // Read in size
//            int size = s.readInt();
//
//            buildFromSorted(size, null, s, null);
//        }
//
//        /** Intended to be called only from TreeSet.readObject */
//        void readTreeSet(int size, java.io.ObjectInputStream s, V defaultVal)
//                throws java.io.IOException, ClassNotFoundException {
//            buildFromSorted(size, null, s, defaultVal);
//        }
//
//        /** Intended to be called only from TreeSet.addAll */
//        void addAllForTreeSet(SortedSet<? extends K> set, V defaultVal) {
//            try {
//                buildFromSorted(set.size(), set.iterator(), null, defaultVal);
//            } catch (java.io.IOException cannotHappen) {
//            } catch (ClassNotFoundException cannotHappen) {
//            }
//        }
//
//
//        /**
//         * Linear time tree building algorithm from sorted data.  Can accept keys
//         * and/or values from iterator or stream. This leads to too many
//         * parameters, but seems better than alternatives.  The four formats
//         * that this method accepts are:
//         *
//         *    1) An iterator of Map.Entries.  (it != null, defaultVal == null).
//         *    2) An iterator of keys.         (it != null, defaultVal != null).
//         *    3) A stream of alternating serialized keys and values.
//         *                                   (it == null, defaultVal == null).
//         *    4) A stream of serialized keys. (it == null, defaultVal != null).
//         *
//         * It is assumed that the comparator of the TreeMap is already set prior
//         * to calling this method.
//         *
//         * @param size the number of keys (or key-value pairs) to be read from
//         *        the iterator or stream
//         * @param it If non-null, new entries are created from entries
//         *        or keys read from this iterator.
//         * @param str If non-null, new entries are created from keys and
//         *        possibly values read from this stream in serialized form.
//         *        Exactly one of it and str should be non-null.
//         * @param defaultVal if non-null, this default value is used for
//         *        each value in the map.  If null, each value is read from
//         *        iterator or stream, as described above.
//         * @throws IOException propagated from stream reads. This cannot
//         *         occur if str is null.
//         * @throws ClassNotFoundException propagated from readObject.
//         *         This cannot occur if str is null.
//         */
//        private void buildFromSorted(int size, Iterator it,
//                                     java.io.ObjectInputStream str,
//                                     V defaultVal)
//                throws  java.io.IOException, ClassNotFoundException {
//            this.size = size;
//            root = buildFromSorted(0, 0, size-1, computeRedLevel(size),
//                    it, str, defaultVal);
//        }
//
//        /**
//         * Recursive "helper method" that does the real work of the
//         * previous method.  Identically named parameters have
//         * identical definitions.  Additional parameters are documented below.
//         * It is assumed that the comparator and size fields of the TreeMap are
//         * already set prior to calling this method.  (It ignores both fields.)
//         *
//         * @param level the current level of tree. Initial call should be 0.
//         * @param lo the first element index of this subtree. Initial should be 0.
//         * @param hi the last element index of this subtree.  Initial should be
//         *        size-1.
//         * @param redLevel the level at which nodes should be red.
//         *        Must be equal to computeRedLevel for tree of this size.
//         */
//        private final java.util.TreeMap.Entry<K,V> buildFromSorted(int level, int lo, int hi,
//                                                                   int redLevel,
//                                                                   Iterator it,
//                                                                   java.io.ObjectInputStream str,
//                                                                   V defaultVal)
//                throws  java.io.IOException, ClassNotFoundException {
//        /*
//         * Strategy: The root is the middlemost element. To get to it, we
//         * have to first recursively construct the entire left subtree,
//         * so as to grab all of its elements. We can then proceed with right
//         * subtree.
//         *
//         * The lo and hi arguments are the minimum and maximum
//         * indices to pull out of the iterator or stream for current subtree.
//         * They are not actually indexed, we just proceed sequentially,
//         * ensuring that items are extracted in corresponding order.
//         */
//
//            if (hi < lo) return null;
//
//            int mid = (lo + hi) >>> 1;
//
//            java.util.TreeMap.Entry<K,V> left  = null;
//            if (lo < mid)
//                left = buildFromSorted(level+1, lo, mid - 1, redLevel,
//                        it, str, defaultVal);
//
//            // extract key and/or value from iterator or stream
//            K key;
//            V value;
//            if (it != null) {
//                if (defaultVal==null) {
//                    Map.Entry<K,V> entry = (Map.Entry<K,V>)it.next();
//                    key = entry.getKey();
//                    value = entry.getValue();
//                } else {
//                    key = (K)it.next();
//                    value = defaultVal;
//                }
//            } else { // use stream
//                key = (K) str.readObject();
//                value = (defaultVal != null ? defaultVal : (V) str.readObject());
//            }
//
//            java.util.TreeMap.Entry<K,V> middle =  new java.util.TreeMap.Entry<>(key, value, null);
//
//            // color nodes in non-full bottommost level red
//            if (level == redLevel)
//                middle.color = RED;
//
//            if (left != null) {
//                middle.left = left;
//                left.parent = middle;
//            }
//
//            if (mid < hi) {
//                java.util.TreeMap.Entry<K,V> right = buildFromSorted(level+1, mid+1, hi, redLevel,
//                        it, str, defaultVal);
//                middle.right = right;
//                right.parent = middle;
//            }
//
//            return middle;
//        }
//
//        /**
//         * Find the level down to which to assign all nodes BLACK.  This is the
//         * last `full' level of the complete binary tree produced by
//         * buildTree. The remaining nodes are colored RED. (This makes a `nice'
//         * set of color assignments wrt future insertions.) This level number is
//         * computed by finding the number of splits needed to reach the zeroeth
//         * node.  (The answer is ~lg(N), but in any case must be computed by same
//         * quick O(lg(N)) loop.)
//         */
//        private static int computeRedLevel(int sz) {
//            int level = 0;
//            for (int m = sz - 1; m >= 0; m = m / 2 - 1)
//                level++;
//            return level;
//        }
//    }
//
//
//}
