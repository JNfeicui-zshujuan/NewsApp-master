package com.example.zhengshujuan.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.zhengshujuan.newsapp.adapter.NewsAdapter;
import com.example.zhengshujuan.newsapp.adapter.NewsTypeAdapter;
import com.example.zhengshujuan.newsapp.biz.NewsDBManager;
import com.example.zhengshujuan.newsapp.biz.NewsManager;
import com.example.zhengshujuan.newsapp.biz.ParserNews;
import com.example.zhengshujuan.newsapp.biz.SystemUtils;
import com.example.zhengshujuan.newsapp.entity.News;
import com.example.zhengshujuan.newsapp.entity.SubType;
import com.example.zhengshujuan.newsapp.entity.XListView;
import com.example.zhengshujuan.newsapp.ui.LoginActivity;
import com.example.zhengshujuan.newsapp.ui.MyBaseActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
    private ImageView ima_login;

    //初始化主界面布局控件
    ImageView im_set;
    ImageView im_share;
    TextView tv_title;
    RelativeLayout layoutContent;
    //新闻主界面
    //  private ListView listView;
    private XListView listView;
    private View view;
    private NewsAdapter adapter;
    private News news;
    private NewsDBManager dbManager ;
    private NewsTypeAdapter typeAdapter;
    // private HorizontalListView horrizontalListView;
    private HorizontalScrollView slType;
    public static String link;

    //每页显示10行
    private int limit = 10;
    //第几页
    private int offset;
    //解析数据jason
    private ParserNews newsParser;
    private Handler handler;
    private StringBuffer mStringBuffer;
    private int mode;
    private News data=new News();
    private ImageView iv_share;
    private ImageView iv_home;

    // @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        view = inflater.inflate(R.layout.main1_activity, container, false);
        slType = (HorizontalScrollView) view.findViewById(R.id.sl_type);
        return view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1_activity);

        view = View.inflate(this, R.layout.mainlist_detail, null);
        listView = (XListView) findViewById(R.id.lv_main);
        iv_share = (ImageView) findViewById(R.id.im_user);
        iv_home = (ImageView) findViewById(R.id.im_set);


        dbManager =new NewsDBManager(this);
        typeAdapter=new NewsTypeAdapter(this);
     //  loadNewsType();
        if (listView != null) {
            adapter = new NewsAdapter(this, listView);
            listView.setAdapter(adapter);
            listView.setPullRefreshEnable(true);
            listView.setPullLoadEnable(true);
            //加载监听
            listView.setXListViewListener(listViewListener);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    for (int i = 0; i < 20; i++) {
                        if (i ==position-1 ) {
                            link = adapter.getAdapterData().get(i).getLink();
//                             News news=new News();
//                             String link = news.getLink();
                            Log.d(TAG, "onItemClick: " + link);
                            Intent intent = new Intent(ActivityMain.this, AcitivityShow.class);
                            startActivity(intent);
                        }
                    }
                }
            });

        }
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
        menu.setSecondaryMenu(R.layout.sliding_menu_right);
        ima_login= (ImageView) findViewById(R.id.imageView1);

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
        iv_share.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        ima_login.setOnClickListener(this);
        loadData();
        handler = new Handler() {
            public void handleMessage(Message msg) {

                super.handleMessage(msg);
                if (msg.what == 100) {
                    //存在数据库中
                    listView.setAdapter(adapter);
                    //  LoadDataFromDB(limit, offset);
                } else if (msg.what == 200) {
                    showToast("网络连接错误");
                }
//                dialog.dismiss();
            }
        };

    }

    private XListView.IXListViewListener listViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            //数据的刷新
            loadNextNews(false);
            //加载完毕
            listView.stopRefresh();

            // listView.setRefreshTime(CommonUtil.getSystemtime());
        }

        @Override
        public void onLoadMore() {
            LoadPreNews();
            listView.stopLoadMore();
            listView.stopRefresh();

        }
    };
    //加载先前的新闻数据

    protected void LoadPreNews() {
        if (listView.getCount() - 2 <= 0) {
            return;
//            int nId=adapter.getAdapterData().get(listView.getLastVisiblePosition()-2).getNid();


        }
//    getItem(listView.getLastVisiblePosition()-2)

        mode = NewsManager.MODE_PREVIOUS;
        if (SystemUtils.getInstance(getApplicationContext()).isNetConn()) {

        }
    }

    private int subId = 1;

    //加载新的数据
    protected void loadNextNews(boolean isNewType) {
        //第一条新闻的id
        int nId = 1;
        if (!isNewType) {
            if (adapter.getAdapterData().size() > 0) {
                nId = adapter.getAdapterData().get(0).getNid();

            }
        }
        mode = NewsManager.MODE_NEXT;
        if (SystemUtils.getInstance(getApplicationContext()).isNetConn()) {
            NewsManager.loadNewsFromServer(getApplicationContext(), mode, subId, nId, new
                    VolleyResponseHandler(), new VolleyErrorHandler());
        }

    }


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
            case R.id.im_user:
                menu.toggle();
                menu.showSecondaryMenu();
                break;
            case R.id.im_set:
                menu.toggle();
                break;
            case R.id.imageView1:
                openActivity(LoginActivity.class);
                break;

        }
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
                    ArrayList<News> str = newsParser.parser(mStringBuffer.toString());
//                    SubType subType=new SubType(0,"");
                    adapter = new NewsAdapter(ActivityMain.this, listView);
                    adapter.addenData(str, false);
//                    adapter.addSubType(subType);
//                    Object item = adpter.getItem(0);
                    Log.d(TAG, "run: " + str.get(0).getSummary());
//                    Object item1 = adpter.getItem(1);
//                    Log.d(TAG, "run: 111111111111111"+item1);
//                  listView.setAdapter(adpter);

//                    Log.d(TAG, "run: "+str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "run: 000000000000" + mStringBuffer);

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

    //Volley 成功新闻列表回调接口实现类
    private class VolleyResponseHandler implements Response.Listener<String> {
        @Override
        public void onResponse(String response) {
            List<SubType> types = ParserNews.parserTypeList(response);
            boolean isClear = mode == NewsManager.MODE_NEXT ? true : false;
                adapter.appendData(data,isClear);
//              ActivityMain.cancelDialog();
            adapter.update();
//            dbManager.saveNewsType(types);
        }
    }

    public  class VolleyErrorHandler implements Response.ErrorListener {


        @Override
        public void onErrorResponse(VolleyError error) {
            // ActivityMain.cancelDialog();
            Log.d(TAG, "onErrorResponse: 服务器连接异常.......");
        }
    }

    //新闻单项点击事件
//    private AdapterView.OnItemClickListener newsItemListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //打开显示当前的新闻
//            News news = (News) parent.getItemAtPosition(position);
//            Intent intent = new Intent(getApplicationContext(), AcitivityShow.class);
//            intent.putExtra("newsitem", news);
//            getApplicationContext().startActivity(intent);
//        }
//    };

    // 加载新闻类型
//    private void loadNewsType() {
//        if (dbManager.queryNewsType().size() == 0) {
//            if (SystemUtils.getInstance(getApplicationContext()).isNetConn()) {
//                Log.d(TAG, "loadNewsType: 1111111");
//                NewsManager.loadNewsType(this,
//                        new VolleyResponseHandler(),  new VolleyErrorHandler());
//            }
//
//        } else {
//            List<SubType> types = dbManager.queryNewsType();
//            typeAdapter.appendData((News) types, true);
//            typeAdapter.update();
//        }
//
//    }
}
