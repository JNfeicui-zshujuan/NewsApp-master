package com.example.zhengshujuan.newsapp;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.zhengshujuan.newsapp.ui.MyBaseActivity;

/**
 * Created by zhengshujuan on 2016/5/30.
 */
public class ActivityLogo extends MyBaseActivity {
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        imageView = (ImageView) findViewById(R.id.im_logo);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        //当FillAfter设置为true是.动画会一直进行?
        //不设置默认为false
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            //动画启动时调用
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画结束时调用
            @Override
            public void onAnimationEnd(Animation animation) {
                openActivity(ActivityMain.class);
                ActivityLogo.this.finish();

            }

            //动画重复时调用
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.setAnimation(animation);

    }
}
