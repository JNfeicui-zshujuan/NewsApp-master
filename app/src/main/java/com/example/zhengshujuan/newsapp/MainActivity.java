package com.example.zhengshujuan.newsapp;

import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.zhengshujuan.newsapp.adapter.LeadImgAdapter;
import com.example.zhengshujuan.newsapp.ui.MyBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyBaseActivity {
    public static final String TAG = "MainActivity";
    public static final String IS_FIRST_RUN = "isFirstRun";
    private ViewPager viewPager;
    private ImageView[] points = new ImageView[4];
    private LeadImgAdapter adapter;
    //  private boolean isFrist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //设置每一个具体界面的样式\
        SharedPreferences pre = getSharedPreferences("runconfig", MODE_PRIVATE);
        Log.d(TAG, "onCreate: 有没有保存,有没有拿到" + pre);
        boolean isFirst = pre.getBoolean(IS_FIRST_RUN, true);
        Log.d(TAG, "onCreate: 真还是假" + isFirst);
        //如果不是第一次打开，则直接跳转到Logo界面
        if (!isFirst) {
            Log.d(TAG, "onCreate: " + isFirst);
            openActivity(ActivityLogo.class);
            finish();
            return;
        }
        points[0] = (ImageView) findViewById(R.id.iv_p1);
        points[1] = (ImageView) findViewById(R.id.iv_p2);
        points[2] = (ImageView) findViewById(R.id.iv_p3);
        points[3] = (ImageView) findViewById(R.id.iv_p4);
        setPoint(0);
        List<View> viewList = new ArrayList<>();
        viewList.add(getLayoutInflater().inflate(R.layout.lead1, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead2, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead3, null));
        viewList.add(getLayoutInflater().inflate(R.layout.lead4, null));
        adapter = new LeadImgAdapter(viewList);
        //设置适配器
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(listner);
        Log.d(TAG, "onCreate: " + viewList);

    }


    private void setPoint(int index) {
        for (int i = 0; i < points.length; i++) {
            if (i == index) {
                points[i].setAlpha(255);
            } else {
                points[i].setAlpha(100);
            }
            Log.d(TAG, "setPoint: " + points);
        }
    }
    private ViewPager.OnPageChangeListener listner = new ViewPager.OnPageChangeListener() {


        //当界面切换后调用
        @Override
        public void onPageSelected(int position) {
            setPoint(position);
            if (position >= points.length -1) {
                openActivity(ActivityLogo.class);
                SharedPreferences pre = getSharedPreferences("runconfig", MODE_PRIVATE);
                SharedPreferences.Editor editor = pre.edit();
                editor.putBoolean(IS_FIRST_RUN, false);
                editor.commit();
                Log.d(TAG, "onPageSelected: " + editor);
                Log.d(TAG, "onPageSelected: 我在这里");
                Log.d(TAG, "onPageSelected: 有没有保存" + pre);
                finish();
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        //滑动状态变化时调用
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
