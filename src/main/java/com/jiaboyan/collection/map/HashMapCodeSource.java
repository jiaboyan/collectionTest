//package com.jiaboyan.collection.map;
//
//import java.io.IOException;
//import java.io.InvalidObjectException;
//import java.io.Serializable;
//import java.util.*;
//
///**
// * Created by jiaboyan on 2017/8/20.
// */
//public class HashMapCodeSource {
//
//    public class HashMap<K,V> extends AbstractMap<K,V>
//            implements Map<K,V>, Cloneable, Serializable {
//
//        //hashMap中的数组初始化大小：1 << 4=2^4=16
//        static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
//
////        1<<30 表示1左移30位，每左移一位乘以2，所以就是1*2^30=1073741824。
//        static final int MAXIMUM_CAPACITY = 1 << 30;
//
//        //默认装载因子：
//        static final float DEFAULT_LOAD_FACTOR = 0.75f;
//
//        //HashMap默认初始化的空数组：
//        static final java.util.HashMap.Entry<?,?>[] EMPTY_TABLE = {};
//
//        //HashMap中底层保存数据的数组：HashMap其实就是一个Entry数组
//        transient java.util.HashMap.Entry<K,V>[] table = (java.util.HashMap.Entry<K,V>[]) EMPTY_TABLE;
//
//        //Hashmap中元素的个数：
//        transient int size;
//
//        //threshold：等于capacity * loadFactory，决定了HashMap能够放进去的数据量。
//        int threshold;
//
//        //loadFactor：装载因子，默认值为0.75，它决定了bucket填充程度；
//        final float loadFactor;
//
//        transient int modCount;
//
//        static final int ALTERNATIVE_HASHING_THRESHOLD_DEFAULT = Integer.MAX_VALUE;
//
//        //主要对 ALTERNATIVE_HASHING_THRESHOLD 进行初始化，
//        // 此属性是 hashSeed 属性是否初始化的重要条件：
//        private static class Holder {
//            static final int ALTERNATIVE_HASHING_THRESHOLD;
//            static {
//                String altThreshold = java.security.AccessController.
//                        doPrivileged(new sun.security.action.GetPropertyAction("jdk.map.althashing.threshold"));
//                int threshold;
//                try {
//                    threshold = (null != altThreshold)? Integer.parseInt(altThreshold) : ALTERNATIVE_HASHING_THRESHOLD_DEFAULT;
//                    if (threshold == -1) {
//                        threshold = Integer.MAX_VALUE;
//                    }
//                    if (threshold < 0) {
//                        throw new IllegalArgumentException("value must be positive integer.");
//                    }
//                } catch(IllegalArgumentException failed) {
//                    throw new Error("Illegal value for 'jdk.map.althashing.threshold'", failed);
//                }
//                ALTERNATIVE_HASHING_THRESHOLD = threshold;
//            }
//        }
//
//        //hashSeed也是一个非常重要的角色，可以把它看成一个开关，如果开关打开，
//        //并且key的类型是String时可以采取sun.misc.Hashing.stringHash32方法获取其hash值。
//        transient int hashSeed = 0;
//
//        //构造方法：初始化容量  指定装载因子
//        public HashMap(int initialCapacity, float loadFactor) {
//            if (initialCapacity < 0)
//                throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
//            //指定初始化容量大于 HashMap规定最大容量的话，就将其设置为最大容量；
//            if (initialCapacity > MAXIMUM_CAPACITY)
//                initialCapacity = MAXIMUM_CAPACITY;
//            //不能小于0，判断参数float的值是否是数字
//            if (loadFactor <= 0 || Float.isNaN(loadFactor))
//                throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
//            //装载因子赋值：
//            this.loadFactor = loadFactor;
//            //初始容量赋值给 临界值属性
//            threshold = initialCapacity;
//            init();
//        }
//
//        //构造方法：初始化容量
//        public HashMap(int initialCapacity) {
//            this(initialCapacity, DEFAULT_LOAD_FACTOR);
//        }
//
//        //无参构造：默认初始化容量16、默认装载因子0.75
//        public HashMap() {
//            this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
//        }
//
//        public HashMap(Map<? extends K, ? extends V> m) {
//            this(Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1,
//                    DEFAULT_INITIAL_CAPACITY), DEFAULT_LOAD_FACTOR);
//            inflateTable(threshold);
//
//            putAllForCreate(m);
//        }
//
//        //用来计算大于等于number的2的幂数，也就是2的几次方；
//        private static int roundUpToPowerOf2(int number) {
////            首先number与MAXIMUM_CAPACITYB比较(绝对多数情况下不会出现大于等于的可能)
////            其次，(number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1，
////            number大于1，在计算Integer.highestOneBit((number - 1) << 1)，
//// Integer.highestOneBit()含义是只保留二进制的最高位的1，其余全为0；
////            15<<1，为30，再取最高位1，则为16
//            return number >= MAXIMUM_CAPACITY ?
//                    MAXIMUM_CAPACITY : (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
//        }
////        接下来，我们分析下为什么哈希表的容量一定要是2的整数次幂。
////        首先，length为2的整数次幂的话，h&(length-1)就相当于对length取模，这样便保证了散列的均匀，
////        同时也提升了效率；其次，length为2的整数次幂的话，为偶数，
//// 这样length-1为奇数，奇数的最后一位是1，
////        这样便保证了h&(length-1)的最后一位可能为0，也可能为1（这取决于h的值），
//// 即与后的结果可能为偶数，
////        也可能为奇数，这样便可以保证散列的均匀性，
//// 而如果length为奇数的话，很明显length-1为偶数，它的最后一位是0，
////        这样h&(length-1)的最后一位肯定为0，即只能为偶数，
//// 这样任何hash值都只会被散列到数组的偶数下标位置上，
////        这便浪费了近一半的空间，
//// 因此，length取2的整数次幂，是为了使不同hash值发生碰撞的概率较小，
//
//
//        //初始化 HashMap的table属性，也就是Entry数组。默认是16个大小
//        private void inflateTable(int toSize) {
//            //计算容量大小：
//            int capacity = roundUpToPowerOf2(toSize);
//
//            //对HashMap的阀值计算：绝大部分情况下为 容量大小 * loadFactor
//            threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
//            //给table赋值，创建数组：
//            table = new java.util.HashMap.Entry[capacity];
//            //判断是否需要初始化hashSeed属性：
//            initHashSeedAsNeeded(capacity);
//        }
//
//        void init() {
//        }
//
//        //判断hashSeed是否需要初始化：
//        // 如果初始化了hashSeed，那么当HashMap的key为string类型时，
//        // 会调用sun.misc.Hashing.stringHash32方法获取其hash值
//        final boolean initHashSeedAsNeeded(int capacity) {
//            //判断hashSeed是否等于0：
//            boolean currentAltHashing = hashSeed != 0;
//            boolean useAltHashing = sun.misc.VM.isBooted() &&
//                    (capacity >= java.util.HashMap.Holder.ALTERNATIVE_HASHING_THRESHOLD);
//            boolean switching = currentAltHashing ^ useAltHashing;
//            //判断是否需要对hashSeed赋值：
//            if (switching) {
//                hashSeed = useAltHashing? sun.misc.Hashing.randomHashSeed(this): 0;
//            }
//            return switching;
//        }
//
//        //hashMap 提供的 hash值算法
//        final int hash(Object k) {
//            int h = hashSeed;
//            //如果为string类型，并且hashSeed不等于0，也就是对hashSeed初始化了（具体参考initHashSeedAsNeeded）
//            //调用了sun.misc.Hashing.stringHash32方法
//            if (0 != h && k instanceof String) {
//                return sun.misc.Hashing.stringHash32((String) k);
//            }
//            //调用key的hashcode方法，对hashcode再进行一系列处理；
//            h ^= k.hashCode();
//            h ^= (h >>> 20) ^ (h >>> 12);
//            return h ^ (h >>> 7) ^ (h >>> 4);
//        }
//
//        //计算元素所处于数组的位置，进行与运算
////      一般使用hash值对length取模（即除法散列法）
//        static int indexFor(int h, int length) {
//            return h & (length-1);
//        }
//
//        //获取hashMap中的元素大小：
//        public int size() {
//            return size;
//        }
//
//        //判断hashMap是否为空：
//        public boolean isEmpty() {
//            return size == 0;
//        }
//
//        //通过key 获取对应的value:
//        public V get(Object key) {
//            if (key == null)
//                //获取key==null的value：
//                return getForNullKey();
//
//            //获取key不为null的value值：
//            java.util.HashMap.Entry<K,V> entry = getEntry(key);
//
//            //返回对应的value：
//            return null == entry ? null : entry.getValue();
//        }
//
//        //获取hashMap中key为 null的value值：
//        private V getForNullKey() {
//            //hashmap中没有元素，则返回null：
//            if (size == 0) {
//                return null;
//            }
//            //获取Entry数组中，角标为0的Entry对象（put的时候如果有null的key，就存放到角标为0的位置）
//            //获取角标为0的Entry对象，遍历整个链表，看是否有key为null的key：返回对应的value：
//            for (java.util.HashMap.Entry<K,V> e = table[0]; e != null; e = e.next) {
//                if (e.key == null)
//                    return e.value;
//            }
//            //如果都不存在，则返回null：
//            return null;
//        }
//
//        public boolean containsKey(Object key) {
//            return getEntry(key) != null;
//        }
//
//        //通过对应的key获取 Entry对象：
//        final java.util.HashMap.Entry<K,V> getEntry(Object key) {
//            //hashmap长度为0 ，则返回null：
//            if (size == 0) {
//                return null;
//            }
//            //获取key对应的hash值：若key为null,则hash值为0；
//            int hash = (key == null) ? 0 : hash(key);
//            //计算hash值对应数组的角标，获取数组中角标下的Entry对象：对该元素所属的链表进行遍历；
//            for (java.util.HashMap.Entry<K,V> e = table[indexFor(hash, table.length)]; e != null; e = e.next) {
//                Object k;
//                //判断key的hash值，再调用equlas方法进行判断：hash值可能会相同，equals进一步验证；
//                if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k))))
//                    return e;
//            }
//            //没找到返回null:
//            return null;
//        }
//
//        //添加元素：key -- value
//        public V put(K key, V value) {
//            //如果调用put方法时，还是空数组，则进行初始化数组
//            if (table == EMPTY_TABLE) {
//                //进行初始化HashMap的 table属性，也就是Entry数组
//                inflateTable(threshold);
//            }
//            //如果key为null：将null添加到HashMap中来，
//            if (key == null)
////                向HashMap添加为null的key--value
//                return putForNullKey(value);
//            //key不为null的情况下：计算元素hash值。
//            int hash = hash(key);
//            //计算元素所处于数组的位置，
//            int i = indexFor(hash, table.length);
//            //看hashMap中相同角标下是否存在该key：
//            for (java.util.HashMap.Entry<K,V> e = table[i]; e != null; e = e.next) {
//                Object k;
//                //Entry数组中i角标下，存在元素了，判断key是否相同：判断hash值，在equals判断：
//                if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
//                    //key相同，则替换原有key下的value：
//                    V oldValue = e.value;
//                    e.value = value;
//                    e.recordAccess(this);
//                    //返回被替换的值：
//                    return oldValue;
//                }
//            }
//            modCount++;
//            //向HashMap中增加元素：
//            addEntry(hash, key, value, i);
//            return null;
//        }
//
//        //向HashMap添加为null的key -value
//        private V putForNullKey(V value) {
//            //遍历集合数组看现有元素是否包含 key为null的情况：
//            for (java.util.HashMap.Entry<K,V> e = table[0]; e != null; e = e.next) {
//                //如果现有元素存在key为null 的情况，则将原有key对应的value进行替换
//                if (e.key == null) {
//                    V oldValue = e.value;
//                    e.value = value;
//                    e.recordAccess(this);
//                    //将key为null的value返回：
//                    return oldValue;
//                }
//            }
//            //如果不包含null，则进行添加操作：
//            modCount++;//操作数+1
//            //向Entry数组中添加，hash值为0，数组角标为0（数组被形容为一个桶）
//            addEntry(0, null, value, 0);
//            return null;
//        }
//
//        private void putForCreate(K key, V value) {
//            int hash = null == key ? 0 : hash(key);
//            int i = indexFor(hash, table.length);
//
//            for (java.util.HashMap.Entry<K,V> e = table[i]; e != null; e = e.next) {
//                Object k;
//                if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k)))) {
//                    e.value = value;
//                    return;
//                }
//            }
//            createEntry(hash, key, value, i);
//        }
//
//        private void putAllForCreate(Map<? extends K, ? extends V> m) {
//            for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
//                putForCreate(e.getKey(), e.getValue());
//        }
//
//        //将HashMap进行扩容：
//        void resize(int newCapacity) {
//            //将Entry数组赋值给oldTable：
//            java.util.HashMap.Entry[] oldTable = table;
//            //获取现阶段Entry中有多少个元素：
//            int oldCapacity = oldTable.length;
//            //判断现阶段Entry中元素个数，是否达到了Hashmap所规定最大的容量值；
//            if (oldCapacity == MAXIMUM_CAPACITY) {
//                //将扩容阈值设置为Integer的最大值，并返回。没扩容
//                threshold = Integer.MAX_VALUE;
//                return;
//            }
//
//            //创建一个新的Entry数组：容量就是原有容量的2倍大小。
//            java.util.HashMap.Entry[] newTable = new java.util.HashMap.Entry[newCapacity];
//            //将Entry数组中的元素，添加到新的数组中：
//            transfer(newTable, initHashSeedAsNeeded(newCapacity));
//            //新数组赋值给table属性：
//            table = newTable;
//            //重新计算扩容阈值：新的容量大小*装载因子  与   最小容量+1 比大小；
//            threshold = (int)Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
//        }
//
//        //扩容的实现：
//        void transfer(java.util.HashMap.Entry[] newTable, boolean rehash) {
//            //新数组大小
//            int newCapacity = newTable.length;
//            //遍历要扩容的数组：
//            for (java.util.HashMap.Entry<K,V> e : table) {
//                //元素不为null就一直循环：主要循环数组元素后面的链表
//                while(null != e) {
//                    java.util.HashMap.Entry<K,V> next = e.next;
//                    //此处判断hashSeed是否初始化，如果初始化则重新计算hashCode：
//                    if (rehash) {
//                        //hash()方法调用sun.misc.Hashing.stringHash32
//                        e.hash = null == e.key ? 0 : hash(e.key);
//                    }
//                    //计算扩容后元素所处于数组的位置：
//                    int i = indexFor(e.hash, newCapacity);
//                    e.next = newTable[i];
//                    newTable[i] = e;
//                    e = next;
//                }
//            }
//        }
//
//        public void putAll(Map<? extends K, ? extends V> m) {
//            int numKeysToBeAdded = m.size();
//            if (numKeysToBeAdded == 0)
//                return;
//
//            if (table == EMPTY_TABLE) {
//                inflateTable((int) Math.max(numKeysToBeAdded * loadFactor, threshold));
//            }
//            if (numKeysToBeAdded > threshold) {
//                int targetCapacity = (int)(numKeysToBeAdded / loadFactor + 1);
//                if (targetCapacity > MAXIMUM_CAPACITY)
//                    targetCapacity = MAXIMUM_CAPACITY;
//                int newCapacity = table.length;
//                while (newCapacity < targetCapacity)
//                    newCapacity <<= 1;
//                if (newCapacity > table.length)
//                    resize(newCapacity);
//            }
//
//            for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
//                put(e.getKey(), e.getValue());
//        }
//
//        //移除hashMap中的元素，通过key：
//        public V remove(Object key) {
//            //移除HashMap数组中的Entry对象：
//            java.util.HashMap.Entry<K,V> e = removeEntryForKey(key);
//            return (e == null ? null : e.value);
//        }
//        //通过key移除数组中Entry：
//        final java.util.HashMap.Entry<K,V> removeEntryForKey(Object key) {
//            //如果Hashmap集合为0，则返回null：
//            if (size == 0) {
//                return null;
//            }
//            //计算key的hash值：
//            int hash = (key == null) ? 0 : hash(key);
//            //计算hash值对应的数组角标：
//            int i = indexFor(hash, table.length);
//
//            //获取key对应的Entry对象：
//            java.util.HashMap.Entry<K,V> prev = table[i];
//            //将此对象赋值给e：
//            java.util.HashMap.Entry<K,V> e = prev;
//
//            //单向链表的遍历：
//            while (e != null) {
//                //获取当前元素的下一个元素：
//                java.util.HashMap.Entry<K,V> next = e.next;
//
//                Object k;
//                //判断元素的hash值、equals方法，是否和传入的key相同：
//                if (e.hash == hash &&
//                        ((k = e.key) == key || (key != null && key.equals(k)))) {
//                    //增加操作数：
//                    modCount++;
//
//                    //减少元素数量：
//                    size--;
//                    //当为链表的第一个元素时，直接将下一个元素顶到链表头部：
//                    if (prev == e)
//                        table[i] = next;
//                    else
//                        //当前元素的下下一个元素
//                        prev.next = next;
//                    //删除当前遍历到的元素：空方法，
//                    // 将被删除的元素不再与map有关联，没有置为null之类的操作；
//                    e.recordRemoval(this);
//                    return e;
//                }
//                //不相同的话，就把
//                prev = e;
//                e = next;
//            }
//            return e;
//        }
//
//        final java.util.HashMap.Entry<K,V> removeMapping(Object o) {
//            if (size == 0 || !(o instanceof Map.Entry))
//                return null;
//
//            Map.Entry<K,V> entry = (Map.Entry<K,V>) o;
//            Object key = entry.getKey();
//            int hash = (key == null) ? 0 : hash(key);
//            int i = indexFor(hash, table.length);
//            java.util.HashMap.Entry<K,V> prev = table[i];
//            java.util.HashMap.Entry<K,V> e = prev;
//
//            while (e != null) {
//                java.util.HashMap.Entry<K,V> next = e.next;
//                if (e.hash == hash && e.equals(entry)) {
//                    modCount++;
//                    size--;
//                    if (prev == e)
//                        table[i] = next;
//                    else
//                        prev.next = next;
//                    e.recordRemoval(this);
//                    return e;
//                }
//                prev = e;
//                e = next;
//            }
//
//            return e;
//        }
//
//        //将HashMap元素清楚：
//        public void clear() {
//            //操作数+1
//            modCount++;
//            //将Entry数组中的元素置为null:遍历数组，将数组的每一个元素置为null；
//            Arrays.fill(table, null);
//            //长度属性置为0：
//            size = 0;
//        }
//
//        public boolean containsValue(Object value) {
//            if (value == null)
//                return containsNullValue();
//
//            java.util.HashMap.Entry[] tab = table;
//            for (int i = 0; i < tab.length ; i++)
//                for (java.util.HashMap.Entry e = tab[i]; e != null ; e = e.next)
//                    if (value.equals(e.value))
//                        return true;
//            return false;
//        }
//
//        private boolean containsNullValue() {
//            java.util.HashMap.Entry[] tab = table;
//            for (int i = 0; i < tab.length ; i++)
//                for (java.util.HashMap.Entry e = tab[i]; e != null ; e = e.next)
//                    if (e.value == null)
//                        return true;
//            return false;
//        }
//
//        public Object clone() {
//            java.util.HashMap<K,V> result = null;
//            try {
//                result = (java.util.HashMap<K,V>)super.clone();
//            } catch (CloneNotSupportedException e) {
//                // assert false;
//            }
//            if (result.table != EMPTY_TABLE) {
//                result.inflateTable(Math.min(
//                        (int) Math.min(
//                                size * Math.min(1 / loadFactor, 4.0f),
//                                // we have limits...
//                                java.util.HashMap.MAXIMUM_CAPACITY),
//                        table.length));
//            }
//            result.entrySet = null;
//            result.modCount = 0;
//            result.size = 0;
//            result.init();
//            result.putAllForCreate(this);
//            return result;
//        }
//
//        //Entry形成一个单向链表结构，是HashMap中的内部类。实现Map.Entry接口
//        //Entry对象中包含了键和值，其中next也是一个Entry对象,用来处理hash冲突，形成链表
//        static class Entry<K,V> implements Map.Entry<K,V> {
//            //Entry属性-也就是HashMap的key
//            final K key;
//
//            //Entry属性-也就是HashMap的value
//            V value;
//
//            //指向下一个节点的引用：
//            java.util.HashMap.Entry<K,V> next;
//
//            //此Entry的hash值：也就是key的hash值
//            int hash;
//
//            // 构造函数：
//            Entry(int h, K k, V v, java.util.HashMap.Entry<K,V> n) {
//                value = v;
//                next = n;
//                key = k;
//                hash = h;
//            }
//
//            public final K getKey() {
//                return key;
//            }
//
//            public final V getValue() {
//                return value;
//            }
//
//            public final V setValue(V newValue) {
//                V oldValue = value;
//                value = newValue;
//                return oldValue;
//            }
//
//            // 判断两个Entry是否相等 ：
//            // 若两个Entry的“key”和“value”都相等，则返回true。否则，返回false
//            public final boolean equals(Object o) {
//                //首选判断其是否属于Map.Entry：
//                if (!(o instanceof Map.Entry))
//                    return false;
//                Map.Entry e = (Map.Entry)o;
//                Object k1 = getKey();
//                Object k2 = e.getKey();
//                //判断 两个Entry对象的 key是否相同：
//                if (k1 == k2 || (k1 != null && k1.equals(k2))) {
//                    Object v1 = getValue();
//                    Object v2 = e.getValue();
//                    //key相同的话，在判断value是否相同：
//                    if (v1 == v2 || (v1 != null && v1.equals(v2)))
//                        return true;
//                }
//                return false;
//            }
//
//            //计算Entry的 hashCode的值：利用异或运算；
//            public final int hashCode() {
//                return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
//            }
//
//            public final String toString() {
//                return getKey() + "=" + getValue();
//            }
//
//            void recordAccess(java.util.HashMap<K,V> m) {
//            }
//
//            void recordRemoval(java.util.HashMap<K,V> m) {
//            }
//        }
//
//        //当发生哈希冲突并且size大于阈值的时候，需要进行数组扩容，扩容时，需要新建一个长度为之前数组2倍的新的数组，
//        // 然后将当前的Entry数组中的元素全部传输过去，扩容后的新数组长度为之前的2倍。
//        void addEntry(int hash, K key, V value, int bucketIndex) {
//            //当size超过临界值时 并且 插入的角标所处数组的元素不为null（也就是出现了哈希冲突了）
//            if ((size >= threshold) && (null != table[bucketIndex])) {
//                //变成原来大小的2倍：
//                resize(2 * table.length);
//                hash = (null != key) ? hash(key) : 0;
//                bucketIndex = indexFor(hash, table.length);
//            }
////            创建Entry数组中 entry对象：
//            createEntry(hash, key, value, bucketIndex);
//        }
//
//        //此方法中，如果有重复角标的元素，则将新来的元素放在最前面，之前的元素依次向后形成链表
//        void createEntry(int hash, K key, V value, int bucketIndex) {
//            //获此现阶段此数组角标下的元素：
//            java.util.HashMap.Entry<K,V> e = table[bucketIndex];
//            //创建Entry对象，将原有角标上的元素与传入的元素组成链表结构。
////            Entry构造很关键，最后一个e，在Entry中指的是next属性
//            table[bucketIndex] = new java.util.HashMap.Entry<>(hash, key, value, e);
////            Map集合长度+1
//            size++;
//        }
//
//        //hashMap 迭代器：Entry
//        private abstract class HashIterator<E> implements Iterator<E> {
//            java.util.HashMap.Entry<K,V> next;        // next entry to return
//            int expectedModCount;   // For fast-fail
//            int index;              // current slot
//            java.util.HashMap.Entry<K,V> current;     // current entry
//
//            HashIterator() {
//                expectedModCount = modCount;
//                if (size > 0) { // advance to first entry
//                    java.util.HashMap.Entry[] t = table;
//                    while (index < t.length && (next = t[index++]) == null)
//                        ;
//                }
//            }
//
//            public final boolean hasNext() {
//                return next != null;
//            }
//
//            final java.util.HashMap.Entry<K,V> nextEntry() {
//                if (modCount != expectedModCount)
//                    throw new ConcurrentModificationException();
//                java.util.HashMap.Entry<K,V> e = next;
//                if (e == null)
//                    throw new NoSuchElementException();
//
//                if ((next = e.next) == null) {
//                    java.util.HashMap.Entry[] t = table;
//                    while (index < t.length && (next = t[index++]) == null)
//                        ;
//                }
//                current = e;
//                return e;
//            }
//
//            public void remove() {
//                if (current == null)
//                    throw new IllegalStateException();
//                if (modCount != expectedModCount)
//                    throw new ConcurrentModificationException();
//                Object k = current.key;
//                current = null;
//                java.util.HashMap.this.removeEntryForKey(k);
//                expectedModCount = modCount;
//            }
//        }
//
//        //HashMap value的迭代器：
//        private final class ValueIterator extends java.util.HashMap.HashIterator<V> {
//            public V next() {
//                return nextEntry().value;
//            }
//        }
//        //HashMap key的迭代器：
//        private final class KeyIterator extends java.util.HashMap.HashIterator<K> {
//            public K next() {
//                return nextEntry().getKey();
//            }
//        }
//
//        //HashMap entry的迭代器：继承HashIterator
//        private final class EntryIterator extends java.util.HashMap.HashIterator<Map.Entry<K,V>> {
//            public Map.Entry<K,V> next() {
//                return nextEntry();
//            }
//        }
//
//        Iterator<K> newKeyIterator()   {
//            return new java.util.HashMap.KeyIterator();
//        }
//        Iterator<V> newValueIterator()   {
//            return new java.util.HashMap.ValueIterator();
//        }
//        Iterator<Map.Entry<K,V>> newEntryIterator()   {
//            return new java.util.HashMap.EntryIterator();
//        }
//
//        private transient Set<Map.Entry<K,V>> entrySet = null;
//
//        //将HashMap的key专成Set集合：
////        transient volatile Set<K>        keySet = null;父类中的属性
////        transient volatile Collection<V> values = null;父类中的属性
//        public Set<K> keySet() {
//            Set<K> ks = keySet;
//            return (ks != null ? ks : (keySet = new java.util.HashMap.KeySet()));
//        }
//
//        private final class KeySet extends AbstractSet<K> {
//            public Iterator<K> iterator() {
//                return newKeyIterator();
//            }
//            public int size() {
//                return size;
//            }
//            public boolean contains(Object o) {
//                return containsKey(o);
//            }
//            public boolean remove(Object o) {
//                return java.util.HashMap.this.removeEntryForKey(o) != null;
//            }
//            public void clear() {
//                java.util.HashMap.this.clear();
//            }
//        }
//
//        //hashMap中的 value转成集合：
//        public Collection<V> values() {
//            Collection<V> vs = values;
//            return (vs != null ? vs : (values = new java.util.HashMap.Values()));
//        }
//
//        private final class Values extends AbstractCollection<V> {
//            public Iterator<V> iterator() {
//                return newValueIterator();
//            }
//            public int size() {
//                return size;
//            }
//            public boolean contains(Object o) {
//                return containsValue(o);
//            }
//            public void clear() {
//                java.util.HashMap.this.clear();
//            }
//        }
//
//        //将Map中的数组，填充到Set集合中去：
//        public Set<Map.Entry<K,V>> entrySet() {
//            return entrySet0();
//        }
//
//        private Set<Map.Entry<K,V>> entrySet0() {
//            Set<Map.Entry<K,V>> es = entrySet;
//            return es != null ? es : (entrySet = new java.util.HashMap.EntrySet());
//        }
//
//        private final class EntrySet extends AbstractSet<Map.Entry<K,V>> {
//            public Iterator<Map.Entry<K,V>> iterator() {
//                return newEntryIterator();
//            }
//            public boolean contains(Object o) {
//                if (!(o instanceof Map.Entry))
//                    return false;
//                Map.Entry<K,V> e = (Map.Entry<K,V>) o;
//                java.util.HashMap.Entry<K,V> candidate = getEntry(e.getKey());
//                return candidate != null && candidate.equals(e);
//            }
//            public boolean remove(Object o) {
//                return removeMapping(o) != null;
//            }
//            public int size() {
//                return size;
//            }
//            public void clear() {
//                java.util.HashMap.this.clear();
//            }
//        }
//
//
//
//        //序列化方法：
//        private void writeObject(java.io.ObjectOutputStream s)
//                throws IOException {
//            s.defaultWriteObject();
//
//            if (table==EMPTY_TABLE) {
//                s.writeInt(roundUpToPowerOf2(threshold));
//            } else {
//                s.writeInt(table.length);
//            }
//
//            s.writeInt(size);
//
//            if (size > 0) {
//                for(Map.Entry<K,V> e : entrySet0()) {
//                    s.writeObject(e.getKey());
//                    s.writeObject(e.getValue());
//                }
//            }
//        }
//
//        private static final long serialVersionUID = 362498820763181265L;
//
//        private void readObject(java.io.ObjectInputStream s)
//                throws IOException, ClassNotFoundException
//        {
//            s.defaultReadObject();
//            if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
//                throw new InvalidObjectException("Illegal load factor: " +
//                        loadFactor);
//            }
//
//            table = (java.util.HashMap.Entry<K,V>[]) EMPTY_TABLE;
//
//            s.readInt(); // ignored.
//
//            int mappings = s.readInt();
//            if (mappings < 0)
//                throw new InvalidObjectException("Illegal mappings count: " +
//                        mappings);
//
//            int capacity = (int) Math.min(
//                    mappings * Math.min(1 / loadFactor, 4.0f),
//                    java.util.HashMap.MAXIMUM_CAPACITY);
//
//            if (mappings > 0) {
//                inflateTable(capacity);
//            } else {
//                threshold = capacity;
//            }
//
//            init();  // Give subclass a chance to do its thing.
//
//            for (int i = 0; i < mappings; i++) {
//                K key = (K) s.readObject();
//                V value = (V) s.readObject();
//                putForCreate(key, value);
//            }
//        }
//
//        int   capacity()     { return table.length; }
//        float loadFactor()   { return loadFactor;   }
//    }
//}
