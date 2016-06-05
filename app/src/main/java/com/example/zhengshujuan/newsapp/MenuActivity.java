package com.example.zhengshujuan.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zhengshujuan.newsapp.ui.MyBaseActivity;

/**
 * Created by zhengshujuan on 2016/5/31.
 */
public class MenuActivity extends MyBaseActivity implements View.OnClickListener {
    public static final String TAG = "menu";
    RelativeLayout viewNews;
    RelativeLayout viewLocal;
    RelativeLayout viewComment;
    RelativeLayout viewRead;
    RelativeLayout viewPic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_menu);
        viewNews = (RelativeLayout) findViewById(R.id.menu_news);
        viewRead = (RelativeLayout) findViewById(R.id.menu_read);
        viewLocal = (RelativeLayout) findViewById(R.id.menu_local);
        viewComment = (RelativeLayout) findViewById(R.id.meun_comment);
        viewPic = (RelativeLayout) findViewById(R.id.menu_pic);
        Log.d(TAG, "onCreate:................... ");
        viewNews.setOnClickListener(this);
        viewLocal.setOnClickListener(this);
        viewComment.setOnClickListener(this);
        viewRead.setOnClickListener(this);
        viewPic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.menu_news:
                Toast.makeText(MenuActivity.this, "你点击了news", Toast.LENGTH_SHORT).show();
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
}
