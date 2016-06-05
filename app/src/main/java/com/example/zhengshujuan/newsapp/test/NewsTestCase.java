package com.example.zhengshujuan.newsapp.test;

import android.test.AndroidTestCase;

import com.android.volley.toolbox.HttpClientStack;
import com.example.zhengshujuan.newsapp.biz.ParserNews;
import com.example.zhengshujuan.newsapp.entity.News;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhengshujuan on 2016/6/1.
 */
public class NewsTestCase extends AndroidTestCase {
    //测试parseNews方法
    public void testParserNews(){
        String path="http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20160601&cnt=20";
   //发送请求得到返回数据

        ParserNews parser=new ParserNews(getContext());

     //   List<News> mList=parser.parser(json);
    }
}
