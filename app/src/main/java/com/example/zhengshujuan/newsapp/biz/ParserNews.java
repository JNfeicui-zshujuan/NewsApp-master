package com.example.zhengshujuan.newsapp.biz;

/**
 * Created by zhengshujuan on 2016/6/1.
 */

import android.content.Context;
import android.util.Log;

import com.example.zhengshujuan.newsapp.entity.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 解析从网络获取的数据,json解析
 */

public class ParserNews {
    public static final String TAG="ActivityMain";
    private Context context;
    public ParserNews(Context context){
        this.context=context;
    }
    //解析新闻数据
    public ArrayList<News> parser(String jsaon) throws JSONException {
        ArrayList<News> newsList=new ArrayList<>();
        //根据数据块名称获取一个数组
        JSONObject jsonObject=new JSONObject(jsaon);
        JSONArray jsonArray=jsonObject.getJSONArray("data");
        //循环遍历数组
        for (int i=0;i<jsonArray.length();i++){
            JSONObject object=jsonArray.getJSONObject(i);
            int nid=object.getInt("nid");
            String title=object.getString("title");
            String summary=object.getString("summary");
            String icon=object.getString("icon");
            String link=object.getString("link");
            int type=object.getInt("type");
            News news=new News(nid,title,summary,icon,link,type);
            newsList.add(news);
//            Log.d(TAG, "parser: 5555555555555"+newsList);
        }
        return newsList;
    }
}
