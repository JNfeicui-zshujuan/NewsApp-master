package com.example.zhengshujuan.newsapp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.example.zhengshujuan.newsapp.entity.News;
import com.example.zhengshujuan.newsapp.ui.MyBaseActivity;

/**
 * Created by l on 2016/6/5.
 */
public class AcitivityShow extends MyBaseActivity {
    //加载网页的进度条
    private ProgressBar progressBar;
    //网页
    private WebView webView;
    //当前的新闻
    private News news;
    //返回按钮
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
         webView = (WebView) findViewById(R.id.wedView1);
        WebSettings webSettings=webView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        //设置webView的属性缓存模式离线
        this.webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        this.webView.getSettings().setUseWideViewPort(true);

        //设置加载全部后的监听
        this.webView.setWebViewClient(viewClient);
        //设置当加载时的监听
        this.webView.loadUrl(ActivityMain.link);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        this.webView = (WebView) findViewById(R.id.wedView1);
        imageButton = (ImageButton) findViewById(R.id.ib_title_bar);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        news = (News) getIntent().getSerializableExtra("news");
    }

    private WebViewClient viewClient = new WebViewClient() {
        //在点击请求的是链接是才会调用，重写此方法返回 true  表明点击网页里
        //的 面的链接还是在当前的 webview  里跳转，不跳到浏览器那边。
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }
    };
    //进度条的使用,根据进度
    private WebChromeClient chromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (progressBar.getProgress() == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }
    };
}
