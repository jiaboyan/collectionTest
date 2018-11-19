package com.jiaboyan.collection.concurrent;

import java.util.Map;

/**
 * Created by jiaboyan on 2017/10/29.
 */
public class ConcurrentMapCodeSource {

    public interface ConcurrentMap<K, V> extends Map<K, V> {

        //插入元素(与原有put方法不同的是，putIfAbsent方法中如果插入的key相同，则不替换原有的value值)：
        V putIfAbsent(K key, V value);

        //移除元素(与原有remove方法不同的是，新remove方法中增加了对value的判断，如果key和value不能与Map中的对应上，则不会删除该元素)：
        boolean remove(Object key, Object value);

        //替换元素(增加了对value值的判断，如果key--oldValue能与Map中的key--value对应才进行替换操作)：
        boolean replace(K key, V oldValue, V newValue);

        //替换元素(与上面不同的是，此replace不会对Map中的value进行判断):
        V replace(K key, V value);
    }
}
