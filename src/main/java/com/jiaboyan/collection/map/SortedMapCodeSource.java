package com.jiaboyan.collection.map;

import java.util.*;

/**
 * Created by jiaboyan on 2017/8/29.
 */
public class SortedMapCodeSource {

    public interface SortedMap<K,V> extends Map<K,V> {

        Comparator<? super K> comparator();

        java.util.SortedMap<K,V> subMap(K fromKey, K toKey);

        java.util.SortedMap<K,V> headMap(K toKey);

        java.util.SortedMap<K,V> tailMap(K fromKey);

        K firstKey();

        K lastKey();

        Set<K> keySet();

        Collection<V> values();

        Set<Map.Entry<K, V>> entrySet();
    }


}
