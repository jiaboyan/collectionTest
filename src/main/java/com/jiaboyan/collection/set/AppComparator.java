package com.jiaboyan.collection.set;

import com.jiaboyan.collection.App;

import java.util.Comparator;

/**
 * Created by jiaboyan on 2017/8/13.
 */
//自定义比较器：
public class AppComparator implements Comparator<App> {

    //比较方法：
    public int compare(App app1, App app2) {
        int num = app1.getAge() - app2.getAge();
        return num == 0 ? app1.getName().length() - app2.getName().length() : num;
    }
}
