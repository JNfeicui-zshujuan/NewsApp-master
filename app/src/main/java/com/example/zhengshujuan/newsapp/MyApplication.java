package com.example.zhengshujuan.newsapp;

/**
 * Created by zhengshujuan on 2016/5/30.
 */

import android.app.Application;
import android.content.res.Configuration;

import java.util.HashMap;

import news.common.LogUtil;

/**
 *每个程序中仅创建一个Application类的对象
 * 全局存储容器
 * 整个应用程序唯一实例
 * 用来存储系统的一些信息
 * 生命周期等于这个程序的生命周期
 * 进行一些数据传递,数据共享,数据缓存
 */

public class MyApplication extends Application {
    //用来保存整个应用程序的数据
    private HashMap<String ,Object> allData=new HashMap<>();
    //存数据
    public void addAllData(String key,Object value){
        allData.put(key,value);
    }
    //取数据
    public Object getAllData(String key){
        if (allData.containsKey(key)){
            return  allData.get(key);
        }
        return  null;
    }
    //删除一条数据
    public void delAllDAtaBykey(String key){
        if (allData.containsKey(key)){
            allData.remove(key);
        }
    }
    //删除所有数据
    public void delAllData(){
        allData.clear();
    }
    //单利模式
    private static MyApplication application;
    public static MyApplication getInstance(){
        LogUtil.d(LogUtil.TAG,"MyApplication onCreate");
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        LogUtil.d(LogUtil.TAG,"MyApplication onCreate");
    }
    //内存不足的时候


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.d(LogUtil.TAG,"MyApplication onLowMemory");
    }
    //结束的时候

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtil.d(LogUtil.TAG,"MyApplication onTerminate");
    }
    //配置改变的时候

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtil.d(LogUtil.TAG,"MyApplication onConfigurationChanged");
    }
}
