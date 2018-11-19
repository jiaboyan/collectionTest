//package com.jiaboyan.collection.concurrent;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.Serializable;
//import java.util.*;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.locks.ReentrantLock;
//
///**
// * Created by jiaboyan on 2017/10/29.
// */
//public class ConcurrentHashMapCodeSource {
//
//    public class ConcurrentHashMap<K, V> extends AbstractMap<K, V>
//            implements ConcurrentMap<K, V>, Serializable {
//
//        private static final long serialVersionUID = 7249069246763182397L;
//
//        //HashEntry数组初始容量：决定了HashEntry数组的初始容量和初始阀值大小
//        static final int DEFAULT_INITIAL_CAPACITY = 16;
//
//        //HashEntry数组的初始加载因子：
//        static final float DEFAULT_LOAD_FACTOR = 0.75f;
//
//        //Segment的初始并发等级：决定了Segment数组的长度
//        static final int DEFAULT_CONCURRENCY_LEVEL = 16;
//
//        //HashEntry数组最大容量：
//        static final int MAXIMUM_CAPACITY = 1 << 30;
//
//        //最小segment数组容量：
//        static final int MIN_SEGMENT_TABLE_CAPACITY = 2;
//
//        //最大segement数组容量
//        static final int MAX_SEGMENTS = 1 << 16; // slightly conservative
//
//        static final int RETRIES_BEFORE_LOCK = 2;
//
//        private static class Holder {
//            static final boolean ALTERNATIVE_HASHING;
//            static {
//                String altThreshold = java.security.AccessController.doPrivileged(
//                        new sun.security.action.GetPropertyAction(
//                                "jdk.map.althashing.threshold"));
//                int threshold;
//                try {
//                    threshold = (null != altThreshold)
//                            ? Integer.parseInt(altThreshold)
//                            : Integer.MAX_VALUE;
//                    if (threshold == -1) {
//                        threshold = Integer.MAX_VALUE;
//                    }
//                    if (threshold < 0) {
//                        throw new IllegalArgumentException("value must be positive integer.");
//                    }
//                } catch(IllegalArgumentException failed) {
//                    throw new Error("Illegal value for 'jdk.map.althashing.threshold'", failed);
//                }
//                ALTERNATIVE_HASHING = threshold <= MAXIMUM_CAPACITY;
//            }
//        }
//
//        private transient final int hashSeed = randomHashSeed(this);
//
//        private static int randomHashSeed(instance) {
//            if (sun.misc.VM.isBooted() && Holder.ALTERNATIVE_HASHING) {
//                return sun.misc.Hashing.randomHashSeed(instance);
//            }
//            return 0;
//        }
//
//        //用于根据给定的key的hash值定位到一个Segment
//        final int segmentMask;
//        //用于根据给定的key的hash值定位到一个Segment
//        final int segmentShift;
//
//        //ConcurrentHashMap的 分段数组，每一个Segment对象，
//        //底层保存了多个HashEntry对象，而每一个HashEntry对象都保存了key、value
//        final Segment<K,V>[] segments;
//
//        transient Set<K> keySet;
//        transient Set<Map.Entry<K,V>> entrySet;
//        transient Collection<V> values;
//
//        //最底层保存数据的HashEntry对象，对标与HashMap的Entry对象；
//        static final class HashEntry<K,V> {
//            final int hash;
//            final K key;
//            volatile V value;
//            volatile HashEntry<K,V> next;
//
//            HashEntry(int hash, K key, V value, HashEntry<K,V> next) {
//                this.hash = hash;
//                this.key = key;
//                this.value = value;
//                this.next = next;
//            }
//
//            final void setNext(HashEntry<K,V> n) {
//                UNSAFE.putOrderedObject(this, nextOffset, n);
//            }
//
//            static final sun.misc.Unsafe UNSAFE;
//            static final long nextOffset;
//            static {
//                try {
//                    UNSAFE = sun.misc.Unsafe.getUnsafe();
//                    Class k = HashEntry.class;
//                    nextOffset = UNSAFE.objectFieldOffset
//                            (k.getDeclaredField("next"));
//                } catch (Exception e) {
//                    throw new Error(e);
//                }
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        static final <K,V> HashEntry<K,V> entryAt(HashEntry<K,V>[] tab, int i) {
//            return (tab == null) ? null :
//                    (HashEntry<K,V>) UNSAFE.getObjectVolatile(tab, ((long)i << TSHIFT) + TBASE);
//        }
//
//        static final <K,V> void setEntryAt(HashEntry<K,V>[] tab, int i,
//                                           HashEntry<K,V> e) {
//            UNSAFE.putOrderedObject(tab, ((long)i << TSHIFT) + TBASE, e);
//        }
//
//        private int hash(Object k) {
//            int h = hashSeed;
//            if ((0 != h) && (k instanceof String)) {
//                return sun.misc.Hashing.stringHash32((String) k);
//            }
//            h ^= k.hashCode();
//            h += (h <<  15) ^ 0xffffcd7d;
//            h ^= (h >>> 10);
//            h += (h <<   3);
//            h ^= (h >>>  6);
//            h += (h <<   2) + (h << 14);
//            return h ^ (h >>> 16);
//        }
//
//        //ConcurrentHashMap中的分段数组，并发时候分段加锁保证数据安全
//        static final class Segment<K,V> extends ReentrantLock implements Serializable {
//
//            private static final long serialVersionUID = 2249069246763182397L;
//
//            static final int MAX_SCAN_RETRIES =
//                    Runtime.getRuntime().availableProcessors() > 1 ? 64 : 1;
//
//            transient volatile HashEntry<K,V>[] table;
//
//            transient int count;
//
//            transient int modCount;
//
//            transient int threshold;
//
//            final float loadFactor;
//
//            Segment(float lf, int threshold, HashEntry<K,V>[] tab) {
//                this.loadFactor = lf;
//                this.threshold = threshold;
//                this.table = tab;
//            }
//
//            //Segment中的put()方法：
//            final V put(K key, int hash, V value, boolean onlyIfAbsent) {
//                //获取锁：
//                HashEntry<K,V> node = tryLock() ? null : scanAndLockForPut(key, hash, value);
//                V oldValue;
//                try {
//                    //获取Segment对象中的 HashEntry[]：
//                    HashEntry<K,V>[] tab = table;
//
//                    //计算key的hash值在HashEntry[]中的角标：
//                    int index = (tab.length - 1) & hash;
//
//                    //根据index角标获取HashEntry对象：
//                    HashEntry<K,V> first = entryAt(tab, index);
//                    //遍历此HashEntry对象(链表结构)：
//                    for (HashEntry<K,V> e = first;;) {
//                        //判断逻辑与HashMap大体相似：
//                        if (e != null) {
//                            K k;
//                            if ((k = e.key) == key || (e.hash == hash && key.equals(k))) {
//                                oldValue = e.value;
//                                if (!onlyIfAbsent) {
//                                    e.value = value;
//                                    ++modCount;
//                                }
//                                break;
//                            }
//                            e = e.next;
//                        } else {
//                            if (node != null)
//                                node.setNext(first);
//                            else
//                                node = new HashEntry<K,V>(hash, key, value, first);
//                            int c = count + 1;
//                            if (c > threshold && tab.length < MAXIMUM_CAPACITY)
//                                //超过了Segment中HashEntry[]的阀值，对HashEntry[]进行扩容;
//                                rehash(node);
//                            else
//                                setEntryAt(tab, index, node);
//                            ++modCount;
//                            count = c;
//                            oldValue = null;
//                            break;
//                        }
//                    }
//                } finally {
//                    unlock();
//                }
//                return oldValue;
//            }
//
//            @SuppressWarnings("unchecked")
//            private void rehash(HashEntry<K,V> node) {
//                HashEntry<K,V>[] oldTable = table;
//                int oldCapacity = oldTable.length;
//                int newCapacity = oldCapacity << 1;
//                threshold = (int)(newCapacity * loadFactor);
//                HashEntry<K,V>[] newTable = (HashEntry<K,V>[]) new HashEntry[newCapacity];
//                int sizeMask = newCapacity - 1;
//                for (int i = 0; i < oldCapacity ; i++) {
//                    HashEntry<K,V> e = oldTable[i];
//                    if (e != null) {
//                        HashEntry<K,V> next = e.next;
//                        int idx = e.hash & sizeMask;
//                        if (next == null)   //  Single node on list
//                            newTable[idx] = e;
//                        else { // Reuse consecutive sequence at same slot
//                            HashEntry<K,V> lastRun = e;
//                            int lastIdx = idx;
//                            for (HashEntry<K,V> last = next;
//                                 last != null;
//                                 last = last.next) {
//                                int k = last.hash & sizeMask;
//                                if (k != lastIdx) {
//                                    lastIdx = k;
//                                    lastRun = last;
//                                }
//                            }
//                            newTable[lastIdx] = lastRun;
//                            // Clone remaining nodes
//                            for (HashEntry<K,V> p = e; p != lastRun; p = p.next) {
//                                V v = p.value;
//                                int h = p.hash;
//                                int k = h & sizeMask;
//                                HashEntry<K,V> n = newTable[k];
//                                newTable[k] = new HashEntry<K,V>(h, p.key, v, n);
//                            }
//                        }
//                    }
//                }
//                int nodeIndex = node.hash & sizeMask; // add the new node
//                node.setNext(newTable[nodeIndex]);
//                newTable[nodeIndex] = node;
//                table = newTable;
//            }
//
//            private HashEntry<K,V> scanAndLockForPut(K key, int hash, V value) {
//                HashEntry<K,V> first = entryForHash(this, hash);
//                HashEntry<K,V> e = first;
//                HashEntry<K,V> node = null;
//                int retries = -1; // negative while locating node
//                while (!tryLock()) {
//                    HashEntry<K,V> f; // to recheck first below
//                    if (retries < 0) {
//                        if (e == null) {
//                            if (node == null) // speculatively create node
//                                node = new HashEntry<K,V>(hash, key, value, null);
//                            retries = 0;
//                        }
//                        else if (key.equals(e.key))
//                            retries = 0;
//                        else
//                            e = e.next;
//                    }
//                    else if (++retries > MAX_SCAN_RETRIES) {
//                        lock();
//                        break;
//                    }
//                    else if ((retries & 1) == 0 &&
//                            (f = entryForHash(this, hash)) != first) {
//                        e = first = f; // re-traverse if entry changed
//                        retries = -1;
//                    }
//                }
//                return node;
//            }
//
//            private void scanAndLock(Object key, int hash) {
//                HashEntry<K,V> first = entryForHash(this, hash);
//                HashEntry<K,V> e = first;
//                int retries = -1;
//                while (!tryLock()) {
//                    HashEntry<K,V> f;
//                    if (retries < 0) {
//                        if (e == null || key.equals(e.key))
//                            retries = 0;
//                        else
//                            e = e.next;
//                    }
//                    else if (++retries > MAX_SCAN_RETRIES) {
//                        lock();
//                        break;
//                    }
//                    else if ((retries & 1) == 0 &&
//                            (f = entryForHash(this, hash)) != first) {
//                        e = first = f;
//                        retries = -1;
//                    }
//                }
//            }
//
//            final V remove(Object key, int hash, Object value) {
//                if (!tryLock())
//                    scanAndLock(key, hash);
//                V oldValue = null;
//                try {
//                    HashEntry<K,V>[] tab = table;
//                    int index = (tab.length - 1) & hash;
//                    HashEntry<K,V> e = entryAt(tab, index);
//                    HashEntry<K,V> pred = null;
//                    while (e != null) {
//                        K k;
//                        HashEntry<K,V> next = e.next;
//                        if ((k = e.key) == key ||
//                                (e.hash == hash && key.equals(k))) {
//                            V v = e.value;
//                            if (value == null || value == v || value.equals(v)) {
//                                if (pred == null)
//                                    setEntryAt(tab, index, next);
//                                else
//                                    pred.setNext(next);
//                                ++modCount;
//                                --count;
//                                oldValue = v;
//                            }
//                            break;
//                        }
//                        pred = e;
//                        e = next;
//                    }
//                } finally {
//                    unlock();
//                }
//                return oldValue;
//            }
//
//            final boolean replace(K key, int hash, V oldValue, V newValue) {
//                if (!tryLock())
//                    scanAndLock(key, hash);
//                boolean replaced = false;
//                try {
//                    HashEntry<K,V> e;
//                    for (e = entryForHash(this, hash); e != null; e = e.next) {
//                        K k;
//                        if ((k = e.key) == key ||
//                                (e.hash == hash && key.equals(k))) {
//                            if (oldValue.equals(e.value)) {
//                                e.value = newValue;
//                                ++modCount;
//                                replaced = true;
//                            }
//                            break;
//                        }
//                    }
//                } finally {
//                    unlock();
//                }
//                return replaced;
//            }
//
//            final V replace(K key, int hash, V value) {
//                if (!tryLock())
//                    scanAndLock(key, hash);
//                V oldValue = null;
//                try {
//                    HashEntry<K,V> e;
//                    for (e = entryForHash(this, hash); e != null; e = e.next) {
//                        K k;
//                        if ((k = e.key) == key ||
//                                (e.hash == hash && key.equals(k))) {
//                            oldValue = e.value;
//                            e.value = value;
//                            ++modCount;
//                            break;
//                        }
//                    }
//                } finally {
//                    unlock();
//                }
//                return oldValue;
//            }
//
//            final void clear() {
//                lock();
//                try {
//                    HashEntry<K,V>[] tab = table;
//                    for (int i = 0; i < tab.length ; i++)
//                        setEntryAt(tab, i, null);
//                    ++modCount;
//                    count = 0;
//                } finally {
//                    unlock();
//                }
//            }
//        }
//
//        @SuppressWarnings("unchecked")
//        static final <K,V> Segment<K,V> segmentAt(Segment<K,V>[] ss, int j) {
//            long u = (j << SSHIFT) + SBASE;
//            return ss == null ? null :
//                    (Segment<K,V>) UNSAFE.getObjectVolatile(ss, u);
//        }
//
//        @SuppressWarnings("unchecked")
//        private Segment<K,V> ensureSegment(int k) {
//            final Segment<K,V>[] ss = this.segments;
//            long u = (k << SSHIFT) + SBASE; // raw offset
//            Segment<K,V> seg;
//            if ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u)) == null) {
//                Segment<K,V> proto = ss[0]; // use segment 0 as prototype
//                int cap = proto.table.length;
//                float lf = proto.loadFactor;
//                int threshold = (int)(cap * lf);
//                HashEntry<K,V>[] tab = (HashEntry<K,V>[])new HashEntry[cap];
//                if ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u))
//                        == null) { // recheck
//                    Segment<K,V> s = new Segment<K,V>(lf, threshold, tab);
//                    while ((seg = (Segment<K,V>)UNSAFE.getObjectVolatile(ss, u))
//                            == null) {
//                        if (UNSAFE.compareAndSwapObject(ss, u, null, seg = s))
//                            break;
//                    }
//                }
//            }
//            return seg;
//        }
//
//        @SuppressWarnings("unchecked")
//        private Segment<K,V> segmentForHash(int h) {
//            long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
//            return (Segment<K,V>) UNSAFE.getObjectVolatile(segments, u);
//        }
//
//        @SuppressWarnings("unchecked")
//        static final <K,V> HashEntry<K,V> entryForHash(Segment<K,V> seg, int h) {
//            HashEntry<K,V>[] tab;
//            return (seg == null || (tab = seg.table) == null) ? null :
//                    (HashEntry<K,V>) UNSAFE.getObjectVolatile
//                            (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
//        }
//
//        //指定容量(影响Segment下HashEntry[]的大小)
//        //加载因子(影响Segment下HashEntry[]的扩容阀值)
//        //并发等级(影响ConcurrentHashMap下Segment[]的大小)
//        public ConcurrentHashMap(int initialCapacity,
//                                 float loadFactor, int concurrencyLevel) {
//            //对容量、加载因子、并发等级做限制，不能小于(等于0)
//            if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0)
//                throw new IllegalArgumentException();
//
//            //传入的并发等级不能大于最大等级
//            if (concurrencyLevel > MAX_SEGMENTS)
//                concurrencyLevel = MAX_SEGMENTS;
//
//            //通过并发等级来确定segment数组的大小
//            int sshift = 0; //sshift用来记录向左按位移动的次数
//
//            //ssize用来记录segment数组的大小
//            int ssize = 1;
//
//            while (ssize < concurrencyLevel) {
//                ++sshift;
//                //ssize = ssize<<1（左移1位，结果为ssize的2次方）
//                //14<<2,14的二进制的00001110向左移两位等于56的二进制00111000
//                //1<<i是把1左移i位，每次左移一位就是乘以2，所以1<<i的结果是1乘以2的i次方
//                ssize <<= 1;
//            }
//            this.segmentShift = 32 - sshift;
//            this.segmentMask = ssize - 1;
//
//            //传入初始化的值大于最大容量值，则默认为最大容量值
//            if (initialCapacity > MAXIMUM_CAPACITY)
//                initialCapacity = MAXIMUM_CAPACITY;
//
//            //c影响了每个Segment[]上要放置多少个HashEntry;
//            int c = initialCapacity / ssize;
//            if (c * ssize < initialCapacity)
//                ++c;
//            int cap = MIN_SEGMENT_TABLE_CAPACITY;
//            while (cap < c)
//                cap <<= 1;
//
//            //创建第一个segment对象，并创建该对象下HashEntry[]
//            Segment<K,V> s0 = new Segment<K,V>(loadFactor, (int)(cap * loadFactor), (HashEntry<K,V>[])new HashEntry[cap]);
//
//            //创建Segment[]，指定segment数组的长度：
//            Segment<K,V>[] ss = (Segment<K,V>[])new Segment[ssize];
//
//            //使用CAS方式，将上面创建的segment对象放入segment[]数组中;
//            UNSAFE.putOrderedObject(ss, SBASE, s0);
//
//            //对ConcurrentHashMap中的segment数组赋值：
//            this.segments = ss;
//        }
//
//        public ConcurrentHashMap(int initialCapacity, float loadFactor) {
//            this(initialCapacity, loadFactor, DEFAULT_CONCURRENCY_LEVEL);
//        }
//
//        public ConcurrentHashMap(int initialCapacity) {
//            this(initialCapacity, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
//        }
//
//        public ConcurrentHashMap() {
//            this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
//        }
//
//        public ConcurrentHashMap(Map<? extends K, ? extends V> m) {
//            this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
//                    DEFAULT_INITIAL_CAPACITY),
//                    DEFAULT_LOAD_FACTOR, DEFAULT_CONCURRENCY_LEVEL);
//            putAll(m);
//        }
//
//        public boolean isEmpty() {
//            long sum = 0L;
//            final Segment<K,V>[] segments = this.segments;
//            for (int j = 0; j < segments.length; ++j) {
//                Segment<K,V> seg = segmentAt(segments, j);
//                if (seg != null) {
//                    if (seg.count != 0)
//                        return false;
//                    sum += seg.modCount;
//                }
//            }
//            if (sum != 0L) { // recheck unless no modifications
//                for (int j = 0; j < segments.length; ++j) {
//                    Segment<K,V> seg = segmentAt(segments, j);
//                    if (seg != null) {
//                        if (seg.count != 0)
//                            return false;
//                        sum -= seg.modCount;
//                    }
//                }
//                if (sum != 0L)
//                    return false;
//            }
//            return true;
//        }
//
//        //锁住所有Segment对象，但需要最少遍历2次后，才能发现是否需要加锁
//        public int size() {
//            //得到所有的Segment[]：
//            final Segment<K,V>[] segments = this.segments;
//            int size;
//            boolean overflow; // true if size overflows 32 bits
//            long sum;         // sum of modCounts
//            long last = 0L;   // previous sum
//            int retries = -1; // first iteration isn't retry
//            try {
//                for (;;) {
//                    //先比较在++，所以说能进到此逻辑中来，肯定retries大于2了
//                    if (retries++ == RETRIES_BEFORE_LOCK) {
////                        -1比较，变0
////                        0比较，变1
////                        1比较，变2
////                        2比较，变3
//                        for (int j = 0; j < segments.length; ++j)
//                            ensureSegment(j).lock(); // force creation
//                    }
//                    sum = 0L;
//                    size = 0;
//                    overflow = false;
//                    for (int j = 0; j < segments.length; ++j) {
//                        //遍历Segment[]，获取其中的Segment对象：
//                        Segment<K,V> seg = segmentAt(segments, j);
//                        if (seg != null) {
//                            //Segment对象被操作的次数：
//                            sum += seg.modCount;
//                            //Segment对象内元素的个数：也就是HashEntry对象的个数；
//                            int c = seg.count;
//                            //size每遍历一次增加一次：
//                            if (c < 0 || (size += c) < 0)
//                                overflow = true;
//                        }
//                    }
//                    if (sum == last)
//                        break;
//                    last = sum;
//                }
//            } finally {
//                //释放锁：retries只有大于2的情况下，才会加锁；
//                if (retries > RETRIES_BEFORE_LOCK) {
//                    for (int j = 0; j < segments.length; ++j)
//                        segmentAt(segments, j).unlock();
//                }
//            }
//            return overflow ? Integer.MAX_VALUE : size;
//        }
//
//        //获取元素方法：
//        public V get(Object key) {
//            Segment<K,V> s;
//            HashEntry<K,V>[] tab;
//
//            //计算key的hash值：
//            int h = hash(key);
//
//            //计算该hash值所属的Segment[]的角标：
//            long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
//
//            //获取Segment[]中u角标下的Segment对象：不存在直接返回
//            if ((s = (Segment<K,V>)UNSAFE.getObjectVolatile(segments, u)) != null && (tab = s.table) != null) {
//                //再根据hash值，从Segment对象中的HashEntry[]获取HashEntry对象：并进行链表遍历
//                for (HashEntry<K,V> e = (HashEntry<K,V>) UNSAFE.getObjectVolatile(tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
//                     e != null; e = e.next) {
//                    K k;
//                    //在链表中找到对应元素，便返回；
//                    if ((k = e.key) == key || (e.hash == h && key.equals(k)))
//                        return e.value;
//                }
//            }
//            return null;
//        }
//
//        @SuppressWarnings("unchecked")
//        public boolean containsKey(Object key) {
//            Segment<K,V> s; // same as get() except no need for volatile value read
//            HashEntry<K,V>[] tab;
//            int h = hash(key);
//            long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
//            if ((s = (Segment<K,V>)UNSAFE.getObjectVolatile(segments, u)) != null &&
//                    (tab = s.table) != null) {
//                for (HashEntry<K,V> e = (HashEntry<K,V>) UNSAFE.getObjectVolatile
//                        (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
//                     e != null; e = e.next) {
//                    K k;
//                    if ((k = e.key) == key || (e.hash == h && key.equals(k)))
//                        return true;
//                }
//            }
//            return false;
//        }
//
//        public boolean containsValue(Object value) {
//            // Same idea as size()
//            if (value == null)
//                throw new NullPointerException();
//            final Segment<K,V>[] segments = this.segments;
//            boolean found = false;
//            long last = 0;
//            int retries = -1;
//            try {
//                outer: for (;;) {
//                    if (retries++ == RETRIES_BEFORE_LOCK) {
//                        for (int j = 0; j < segments.length; ++j)
//                            ensureSegment(j).lock(); // force creation
//                    }
//                    long hashSum = 0L;
//                    int sum = 0;
//                    for (int j = 0; j < segments.length; ++j) {
//                        HashEntry<K,V>[] tab;
//                        Segment<K,V> seg = segmentAt(segments, j);
//                        if (seg != null && (tab = seg.table) != null) {
//                            for (int i = 0 ; i < tab.length; i++) {
//                                HashEntry<K,V> e;
//                                for (e = entryAt(tab, i); e != null; e = e.next) {
//                                    V v = e.value;
//                                    if (v != null && value.equals(v)) {
//                                        found = true;
//                                        break outer;
//                                    }
//                                }
//                            }
//                            sum += seg.modCount;
//                        }
//                    }
//                    if (retries > 0 && sum == last)
//                        break;
//                    last = sum;
//                }
//            } finally {
//                if (retries > RETRIES_BEFORE_LOCK) {
//                    for (int j = 0; j < segments.length; ++j)
//                        segmentAt(segments, j).unlock();
//                }
//            }
//            return found;
//        }
//
//        public boolean contains(Object value) {
//            return containsValue(value);
//        }
//
//        @SuppressWarnings("unchecked")
//        public V put(K key, V value) {
//            Segment<K,V> s;
//
//            //传入的value不能为null
//            if (value == null)
//                throw new NullPointerException();
//
//            //计算key的hash值：
//            int hash = hash(key);
//
//            //通过key的hash值，定位ConcurrentHashMap中Segment[]的角标
//            int j = (hash >>> segmentShift) & segmentMask;
//
//            //使用CAS方式，从Segment[]中获取j角标下的Segment对象,并判断是否存在：
//            if ((s = (Segment<K,V>)UNSAFE.getObject(segments, (j << SSHIFT) + SBASE)) == null)
//                //如果在Segment[]中的j角标处没有元素，则在j角标处新建元素---Segment对象；
//                s = ensureSegment(j);
//
//            //底层使用Segment对象的put方法：
//            return s.put(key, hash, value, false);
//        }
//
//        @SuppressWarnings("unchecked")
//        public V putIfAbsent(K key, V value) {
//            Segment<K,V> s;
//            if (value == null)
//                throw new NullPointerException();
//            int hash = hash(key);
//            int j = (hash >>> segmentShift) & segmentMask;
//            if ((s = (Segment<K,V>)UNSAFE.getObject
//                    (segments, (j << SSHIFT) + SBASE)) == null)
//                s = ensureSegment(j);
//            return s.put(key, hash, value, true);
//        }
//
//        public void putAll(Map<? extends K, ? extends V> m) {
//            for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
//                put(e.getKey(), e.getValue());
//        }
//
//        public V remove(Object key) {
//            int hash = hash(key);
//            Segment<K,V> s = segmentForHash(hash);
//            return s == null ? null : s.remove(key, hash, null);
//        }
//
//        public boolean remove(Object key, Object value) {
//            int hash = hash(key);
//            Segment<K,V> s;
//            return value != null && (s = segmentForHash(hash)) != null &&
//                    s.remove(key, hash, value) != null;
//        }
//
//        public boolean replace(K key, V oldValue, V newValue) {
//            int hash = hash(key);
//            if (oldValue == null || newValue == null)
//                throw new NullPointerException();
//            Segment<K,V> s = segmentForHash(hash);
//            return s != null && s.replace(key, hash, oldValue, newValue);
//        }
//
//        public V replace(K key, V value) {
//            int hash = hash(key);
//            if (value == null)
//                throw new NullPointerException();
//            Segment<K,V> s = segmentForHash(hash);
//            return s == null ? null : s.replace(key, hash, value);
//        }
//
//        public void clear() {
//            final Segment<K,V>[] segments = this.segments;
//            for (int j = 0; j < segments.length; ++j) {
//                Segment<K,V> s = segmentAt(segments, j);
//                if (s != null)
//                    s.clear();
//            }
//        }
//
//        public Set<K> keySet() {
//            Set<K> ks = keySet;
//            return (ks != null) ? ks : (keySet = new KeySet());
//        }
//
//        public Collection<V> values() {
//            Collection<V> vs = values;
//            return (vs != null) ? vs : (values = new Values());
//        }
//
//        public Set<Map.Entry<K,V>> entrySet() {
//            Set<Map.Entry<K,V>> es = entrySet;
//            return (es != null) ? es : (entrySet = new EntrySet());
//        }
//
//        public Enumeration<K> keys() {
//            return new KeyIterator();
//        }
//
//        public Enumeration<V> elements() {
//            return new ValueIterator();
//        }
//
//    /* ---------------- Iterator Support -------------- */
//
//        abstract class HashIterator {
//            int nextSegmentIndex;
//            int nextTableIndex;
//            HashEntry<K,V>[] currentTable;
//            HashEntry<K, V> nextEntry;
//            HashEntry<K, V> lastReturned;
//
//            HashIterator() {
//                nextSegmentIndex = segments.length - 1;
//                nextTableIndex = -1;
//                advance();
//            }
//
//            /**
//             * Set nextEntry to first node of next non-empty table
//             * (in backwards order, to simplify checks).
//             */
//            final void advance() {
//                for (;;) {
//                    if (nextTableIndex >= 0) {
//                        if ((nextEntry = entryAt(currentTable,
//                                nextTableIndex--)) != null)
//                            break;
//                    }
//                    else if (nextSegmentIndex >= 0) {
//                        Segment<K,V> seg = segmentAt(segments, nextSegmentIndex--);
//                        if (seg != null && (currentTable = seg.table) != null)
//                            nextTableIndex = currentTable.length - 1;
//                    }
//                    else
//                        break;
//                }
//            }
//
//            final HashEntry<K,V> nextEntry() {
//                HashEntry<K,V> e = nextEntry;
//                if (e == null)
//                    throw new NoSuchElementException();
//                lastReturned = e; // cannot assign until after null check
//                if ((nextEntry = e.next) == null)
//                    advance();
//                return e;
//            }
//
//            public final boolean hasNext() { return nextEntry != null; }
//            public final boolean hasMoreElements() { return nextEntry != null; }
//
//            public final void remove() {
//                if (lastReturned == null)
//                    throw new IllegalStateException();
//                this.remove(lastReturned.key);
//                lastReturned = null;
//            }
//        }
//
//        final class KeyIterator
//                extends HashIterator
//                implements Iterator<K>, Enumeration<K>
//        {
//            public final K next()        { return super.nextEntry().key; }
//            public final K nextElement() { return super.nextEntry().key; }
//        }
//
//        final class ValueIterator
//                extends HashIterator
//                implements Iterator<V>, Enumeration<V>
//        {
//            public final V next()        { return super.nextEntry().value; }
//            public final V nextElement() { return super.nextEntry().value; }
//        }
//
//        /**
//         * Custom Entry class used by EntryIterator.next(), that relays
//         * setValue changes to the underlying map.
//         */
//        final class WriteThroughEntry
//                extends AbstractMap.SimpleEntry<K,V>
//        {
//            WriteThroughEntry(K k, V v) {
//                super(k,v);
//            }
//
//            /**
//             * Set our entry's value and write through to the map. The
//             * value to return is somewhat arbitrary here. Since a
//             * WriteThroughEntry does not necessarily track asynchronous
//             * changes, the most recent "previous" value could be
//             * different from what we return (or could even have been
//             * removed in which case the put will re-establish). We do not
//             * and cannot guarantee more.
//             */
//            public V setValue(V value) {
//                if (value == null) throw new NullPointerException();
//                V v = super.setValue(value);
//                this.put(getKey(), value);
//                return v;
//            }
//        }
//
//        final class EntryIterator
//                extends HashIterator
//                implements Iterator<Entry<K,V>>
//        {
//            public Map.Entry<K,V> next() {
//                HashEntry<K,V> e = super.nextEntry();
//                return new WriteThroughEntry(e.key, e.value);
//            }
//        }
//
//        final class KeySet extends AbstractSet<K> {
//            public Iterator<K> iterator() {
//                return new KeyIterator();
//            }
//            public int size() {
//                return this.size();
//            }
//            public boolean isEmpty() {
//                return this.isEmpty();
//            }
//            public boolean contains(Object o) {
//                return this.containsKey(o);
//            }
//            public boolean remove(Object o) {
//                return this.remove(o) != null;
//            }
//            public void clear() {
//                this.clear();
//            }
//        }
//
//        final class Values extends AbstractCollection<V> {
//            public Iterator<V> iterator() {
//                return new ValueIterator();
//            }
//            public int size() {
//                return this.size();
//            }
//            public boolean isEmpty() {
//                return this.isEmpty();
//            }
//            public boolean contains(Object o) {
//                return this.containsValue(o);
//            }
//            public void clear() {
//                this.clear();
//            }
//        }
//
//        final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
//            public Iterator<Map.Entry<K,V>> iterator() {
//                return new EntryIterator();
//            }
//            public boolean contains(Object o) {
//                if (!(o instanceof Map.Entry))
//                    return false;
//                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
//                V v = this.get(e.getKey());
//                return v != null && v.equals(e.getValue());
//            }
//            public boolean remove(Object o) {
//                if (!(o instanceof Map.Entry))
//                    return false;
//                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
//                return this.remove(e.getKey(), e.getValue());
//            }
//            public int size() {
//                return this.size();
//            }
//            public boolean isEmpty() {
//                return this.isEmpty();
//            }
//            public void clear() {
//                this.clear();
//            }
//        }
//
//    /* ---------------- Serialization Support -------------- */
//
//        /**
//         * Save the state of the <tt>ConcurrentHashMap</tt> instance to a
//         * stream (i.e., serialize it).
//         * @param s the stream
//         * @serialData
//         * the key (Object) and value (Object)
//         * for each key-value mapping, followed by a null pair.
//         * The key-value mappings are emitted in no particular order.
//         */
//        private void writeObject(java.io.ObjectOutputStream s) throws IOException {
//            // force all segments for serialization compatibility
//            for (int k = 0; k < segments.length; ++k)
//                ensureSegment(k);
//            s.defaultWriteObject();
//
//            final Segment<K,V>[] segments = this.segments;
//            for (int k = 0; k < segments.length; ++k) {
//                Segment<K,V> seg = segmentAt(segments, k);
//                seg.lock();
//                try {
//                    HashEntry<K,V>[] tab = seg.table;
//                    for (int i = 0; i < tab.length; ++i) {
//                        HashEntry<K,V> e;
//                        for (e = entryAt(tab, i); e != null; e = e.next) {
//                            s.writeObject(e.key);
//                            s.writeObject(e.value);
//                        }
//                    }
//                } finally {
//                    seg.unlock();
//                }
//            }
//            s.writeObject(null);
//            s.writeObject(null);
//        }
//
//        /**
//         * Reconstitute the <tt>ConcurrentHashMap</tt> instance from a
//         * stream (i.e., deserialize it).
//         * @param s the stream
//         */
//        @SuppressWarnings("unchecked")
//        private void readObject(java.io.ObjectInputStream s)
//                throws IOException, ClassNotFoundException {
//            // Don't call defaultReadObject()
//            ObjectInputStream.GetField oisFields = s.readFields();
//            final Segment<K,V>[] oisSegments = (Segment<K,V>[])oisFields.get("segments", null);
//
//            final int ssize = oisSegments.length;
//            if (ssize < 1 || ssize > MAX_SEGMENTS
//                    || (ssize & (ssize-1)) != 0 )  // ssize not power of two
//                throw new java.io.InvalidObjectException("Bad number of segments:"
//                        + ssize);
//            int sshift = 0, ssizeTmp = ssize;
//            while (ssizeTmp > 1) {
//                ++sshift;
//                ssizeTmp >>>= 1;
//            }
//            UNSAFE.putIntVolatile(this, SEGSHIFT_OFFSET, 32 - sshift);
//            UNSAFE.putIntVolatile(this, SEGMASK_OFFSET, ssize - 1);
//            UNSAFE.putObjectVolatile(this, SEGMENTS_OFFSET, oisSegments);
//
//            // set hashMask
//            UNSAFE.putIntVolatile(this, HASHSEED_OFFSET, randomHashSeed(this));
//
//            // Re-initialize segments to be minimally sized, and let grow.
//            int cap = MIN_SEGMENT_TABLE_CAPACITY;
//            final Segment<K,V>[] segments = this.segments;
//            for (int k = 0; k < segments.length; ++k) {
//                Segment<K,V> seg = segments[k];
//                if (seg != null) {
//                    seg.threshold = (int)(cap * seg.loadFactor);
//                    seg.table = (HashEntry<K,V>[]) new HashEntry[cap];
//                }
//            }
//
//            // Read the keys and values, and put the mappings in the table
//            for (;;) {
//                K key = (K) s.readObject();
//                V value = (V) s.readObject();
//                if (key == null)
//                    break;
//                put(key, value);
//
//            }
//        }
//
//        // Unsafe mechanics
//        private static final sun.misc.Unsafe UNSAFE;
//        private static final long SBASE;
//        private static final int SSHIFT;
//        private static final long TBASE;
//        private static final int TSHIFT;
//        private static final long HASHSEED_OFFSET;
//        private static final long SEGSHIFT_OFFSET;
//        private static final long SEGMASK_OFFSET;
//        private static final long SEGMENTS_OFFSET;
//
//        static {
//            int ss, ts;
//            try {
//                UNSAFE = sun.misc.Unsafe.getUnsafe();
//                Class tc = HashEntry[].class;
//                Class sc = Segment[].class;
//                TBASE = UNSAFE.arrayBaseOffset(tc);
//                SBASE = UNSAFE.arrayBaseOffset(sc);
//                ts = UNSAFE.arrayIndexScale(tc);
//                ss = UNSAFE.arrayIndexScale(sc);
//                HASHSEED_OFFSET = UNSAFE.objectFieldOffset(
//                        class.getDeclaredField("hashSeed"));
//                SEGSHIFT_OFFSET = UNSAFE.objectFieldOffset(
//                        class.getDeclaredField("segmentShift"));
//                SEGMASK_OFFSET = UNSAFE.objectFieldOffset(
//                        class.getDeclaredField("segmentMask"));
//                SEGMENTS_OFFSET = UNSAFE.objectFieldOffset(
//                        class.getDeclaredField("segments"));
//            } catch (Exception e) {
//                throw new Error(e);
//            }
//            if ((ss & (ss-1)) != 0 || (ts & (ts-1)) != 0)
//                throw new Error("data type scale not a power of two");
//            SSHIFT = 31 - Integer.numberOfLeadingZeros(ss);
//            TSHIFT = 31 - Integer.numberOfLeadingZeros(ts);
//        }
//
//    }
//}
