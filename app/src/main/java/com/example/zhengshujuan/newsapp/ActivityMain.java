package com.example.zhengshujuan.newsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhengshujuan.newsapp.adapter.NewsAdapter;
import com.example.zhengshujuan.newsapp.biz.NewsDBManager;
import com.example.zhengshujuan.newsapp.biz.ParserNews;
import com.example.zhengshujuan.newsapp.entity.News;
import com.example.zhengshujuan.newsapp.ui.MyBaseActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cz.jalasoft.net.http.HttpClient;

/**
 * Created by zhengshujuan on 2016/5/31.
 */
public class ActivityMain extends MyBaseActivity implements View.OnClickListener {

    public static final String TAG = "ActivityMain";
    WebView mWebView;
    //初始化侧拉菜单界面控件
    RelativeLayout viewNews;
    RelativeLayout viewLocal;
    RelativeLayout viewComment;
    RelativeLayout viewRead;
    RelativeLayout viewPic;
    SlidingMenu menu;
    //初始化主界面布局控件
    ImageView im_set;
    ImageView im_share;
    TextView tv_title;
    RelativeLayout layoutContent;
    NewsAdapter adpter;
    //新闻主界面
    private ListView listView;
    private NewsAdapter adapter;
    private NewsDBManager dbManager;
    //每页显示10行
    private int limit = 10;
    //第几页
    private int offset;
    //解析数据jason
    private ParserNews newsParser;
    private Handler handler;
    private StringBuffer mStringBuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1_activity);

        listView = (ListView) findViewById(R.id.lv_main);
        adapter = new NewsAdapter(this, listView);
        //listView.setAdapter(adapter);

        Log.d(TAG, "onCreate: 主线程里");
        //主界面的控件
//        getUrl();
        menu = new SlidingMenu(this);
        //设置侧滑菜单在左边
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        //menu.setMode(SlidingMenu.RIGHT);
        //是否可以打开滑动手势
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置可拉的阴影面积
        //  menu.setShadowDrawable(R.drawable.shadow);
        //滑动菜单背后宽度
        menu.setBehindWidthRes(R.dimen.show_width);
        //设置滑动菜单试图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        //设置渐入渐出效果值
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_menu);
        menu.setSecondaryMenu(R.layout.lead2);

        //侧拉菜单
        viewNews = (RelativeLayout) findViewById(R.id.menu_news);
        viewRead = (RelativeLayout) findViewById(R.id.menu_read);
        viewLocal = (RelativeLayout) findViewById(R.id.menu_local);
        viewComment = (RelativeLayout) findViewById(R.id.meun_comment);
        viewPic = (RelativeLayout) findViewById(R.id.menu_pic);
        viewNews.setOnClickListener(this);
        viewLocal.setOnClickListener(this);
        viewComment.setOnClickListener(this);
        viewRead.setOnClickListener(this);
        viewPic.setOnClickListener(this);
      loadData();
        handler = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                if (msg.what == 100) {
                    //存在数据库中
                   listView.setAdapter(adpter);
                 //  LoadDataFromDB(limit, offset);
                } else if (msg.what == 200) {
                    showToast("网络连接错误");
                }
//                dialog.dismiss();
            }
        };

    }

    //加载网页
//    public void getUrl() {
//        String url = "http://www.baidu.com";
//        mWebView = new WebView(this);
//        mWebView.getSettings().getJavaScriptEnabled();
//        mWebView.loadUrl(url);
//        mWebView.setWebViewClient(new WebViewClient());
//        setContentView(mWebView);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_news:
                Toast.makeText(ActivityMain.this, "你点击了news", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_read:
                break;
            case R.id.menu_local:
                break;
            case R.id.meun_comment:
                break;
            case R.id.menu_pic:
                break;
        }
    }

    /*
    * 重写handleMessage方法接收加载处理结果
    * */


    //limit2每页多少行 offset2便宜多少行

    /**
     * 什么便宜了
     */
    private void LoadDataFromDB(int limit2, int offset2) {
        //第一次是0页10行
        ArrayList<News> data = dbManager.queryNews(limit2, offset2);
        adapter.addenData(data, false);
        adapter.update();
        Log.d(TAG, "LoadDataFromDB: 222222222");
        this.offset = offset + data.size();
    }

    //异步加载数据
    private void loadData() {
       // dialog = ProgressDialog.show(this, null, "加载中,请稍后...");

        //启动新线程加载数据
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                String path = "http://118.244.212.82:9092/newsClient/news_list?ver=1&subid=1&dir=1&nid=1&stamp=20160603&cnt=2";
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(path).build();
                try {
                    com.squareup.okhttp.Response response = okHttpClient.newCall(request).execute();
                    //获取网络流
                    InputStream inputStream = response.body().byteStream();
                    //创建缓冲区
                    mStringBuffer = new StringBuffer();
                    //写入流
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    Log.d(TAG, "run: ssssssssss" + br);
                    String s;
                    while ((s = br.readLine()) != null) {
                        mStringBuffer.append(s);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                newsParser = new ParserNews(ActivityMain.this);
                try {
                   ArrayList<News> str= newsParser.parser(mStringBuffer.toString());
                    adpter=new NewsAdapter(ActivityMain.this,listView);
                    adpter.addenData(str,false);
//                    Object item = adpter.getItem(0);
                    Log.d(TAG, "run: "+str.get(0).getSummary());
//                    Object item1 = adpter.getItem(1);
//                    Log.d(TAG, "run: 111111111111111"+item1);
//                  listView.setAdapter(adpter);

//                    Log.d(TAG, "run: "+str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "run: 000000000000"+mStringBuffer);

                //发送请求，得到返回数据
//                dialog.dismiss();
               handler.sendEmptyMessage(100);
//                handler.sendEmptyMessage(200);
            }
        }).start();
    }

    //初始化标题栏文本
    private void Title(String title) {
        tv_title = (TextView) findViewById(R.id.tv_main_title);
        tv_title.setText(title);
    }

    @Override
    public void onBackPressed() {
//        if (mWebView.canGoBack()) {
//            mWebView.goBack();
//        }
        //如果当前菜单栏显示.则返回主界面
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            //按两次退出
            exitTwice();
        }
        // super.onBackPressed();
    }

    private boolean isFristExit = true;

    //按两次退出
    private void exitTwice() {
        if (isFristExit) {
            Toast.makeText(ActivityMain.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            isFristExit = false;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        isFristExit = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        } else {
            finish();
        }
    }


}
