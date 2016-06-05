package com.example.zhengshujuan.newsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.zhengshujuan.newsapp.entity.News;
import com.example.zhengshujuan.newsapp.ui.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengshujuan on 2016/6/1.
 */
public abstract class MyBaseAdapter<N> extends BaseAdapter {
    protected LayoutInflater inflater;
    protected Context context;
    protected List<News> myList=new ArrayList<>();
    protected ArrayList<News> list=new ArrayList<>();
    //定义构造方法,并初始化context,inflater

    public MyBaseAdapter(Context context){
            this.context=context;
        inflater=LayoutInflater.from(context);
    }
    //清除所有数据
    public void clear(){
        myList.clear();
    }
    //查找所有数据
    public List<News> getAdapterData(){
        return list;
    }

    /**
     * 封装添加一条记录的方法
     */
    //news一套数据的对象,isClearOld是否清除原数据
    public void appendData(News news,boolean isCleadOld){
        if (news==null){
            return;
        }
        if(isCleadOld){
            list.clear();
        }//添加一条新数据到最后
        list.add(news);
    }
    /*
    * 添加多条记录*/
    public void addenData(ArrayList<News> alist,boolean isClearOld){
        if (alist==null){
            return;
        }
        if (isClearOld){
            list.clear();
        }
        list.addAll(alist);
    }
    /**
     *  添加一条记录到第一条
     * @param t
     * @param isClearOld
     */
    public void appendDataTop(News t,boolean isClearOld) {
        if (t == null) { // 非空验证
            return;
        }
        if (isClearOld) {// 如果 true  清空原数据
            list.clear();
        }
    }
    /**
     *  添加多条记录到顶部
     * @param alist  数据集合
     * @param isClearOld  是否清空原数据
     */
    public void addendDataTop(ArrayList<News> alist,boolean isClearOld) {
        if (alist == null) {
            return;
        }
        if (isClearOld) {
            list.clear();
        }
        list.addAll(0, alist);
    }
            /*
            * 刷新适配器
            * */
    public void update(){
        //刷新适配器
        this.notifyDataSetChanged();

    }
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (list==null){
            return null;
        }
        //没有数据返回空
        if (position>list.size()-1){
            return null;
        }
        else {
        return list.get(position);}
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getMyView(position,convertView,parent);
    }
    // 作为预留方法，定义为抽象方法，要求子类继承该基础类时，必须重写该方法
    public abstract View getMyView(int position, View convertView, ViewGroup
            parent);{

}

}
