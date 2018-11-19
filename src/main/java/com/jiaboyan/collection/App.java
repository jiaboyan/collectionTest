package com.jiaboyan.collection;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Hello world!
 *
 */
public class App implements Comparable<App>{

    private String name;

    private Integer age;

    private volatile App next;

    public App() {}

    public App(String name,Integer age) {
        this.name = name;
        this.age = age;
    }

    public App(String name,Integer age,App next){
        this.name = name;
        this.age = age;
        this.next = next;
    }

    public App getNext() {
        return next;
    }

    public void setNext(App next) {
        this.next = next;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }


    public boolean casNext(App cmp, App val) {
        return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
    }

    // Unsafe mechanics
    private static sun.misc.Unsafe UNSAFE;
    static{
        Field field = null;
        try {
            field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE =  (sun.misc.Unsafe)field.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static final long nextOffset =
            objectFieldOffset(UNSAFE, "next", App.class);


    static long objectFieldOffset(sun.misc.Unsafe UNSAFE,
                                  String field, Class<?> klazz) {
        try {
            return UNSAFE.objectFieldOffset(klazz.getDeclaredField(field));
        } catch (NoSuchFieldException e) {
            // Convert Exception to corresponding Error
            NoSuchFieldError error = new NoSuchFieldError(field);
            error.initCause(e);
            throw error;
        }
    }

    public static void main(String[] args ) {
            String k = "erwerwerwererwerwe";
            int h = 0;
            h ^= k.hashCode();
            h += (h <<  15) ^ 0xffffcd7d;
            h ^= (h >>> 10);
            h += (h <<   3);
            h ^= (h >>>  6);
            h += (h <<   2) + (h << 14);
            int x= h ^ (h >>> 16);
        String xx=Integer.toBinaryString(x);


        int head = -1 & (8 - 1);
        int numElements = 8;
        int initialCapacity = 8;
        if (numElements >= initialCapacity) {
            initialCapacity = numElements;
            initialCapacity |= (initialCapacity >>>  1);
            initialCapacity |= (initialCapacity >>>  2);
            initialCapacity |= (initialCapacity >>>  4);
            initialCapacity |= (initialCapacity >>>  8);
            initialCapacity |= (initialCapacity >>> 16);
            initialCapacity++;

            if (initialCapacity < 0)   // Too many elements, must back off
                initialCapacity >>>= 1;// Good luck allocating 2 ^ 30 elements
        }


        ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>(8);
        boolean flag = arrayDeque.offerFirst(null);
        arrayDeque.push(1);
        arrayDeque.push(2);
        arrayDeque.push(3);

        arrayDeque.pop();

        System.out.println(  -1&7 );
    }

    //自定义比较：先比较name的长度，在比较age的大小；
    public int compareTo(App app) {
        int num = this.name.length() - app.name.length();
        return num == 0 ? this.age - app.age : num;
    }

    @Override
    public String toString() {
        return "App{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
