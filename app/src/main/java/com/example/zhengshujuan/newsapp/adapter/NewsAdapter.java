package com.example.zhengshujuan.newsapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhengshujuan.newsapp.R;
import com.example.zhengshujuan.newsapp.entity.LoadImage;
import com.example.zhengshujuan.newsapp.entity.News;

import news.common.CommonUtil;
import news.common.LogUtil;

/**
 * Created by zhengshujuan on 2016/6/1.
 */
public class NewsAdapter extends MyBaseAdapter<News> {
    public static final String TAG = "ActivityMain";
    //加载图片之前默认图片
    private Bitmap defaultBitmap;
    private ListView listView;
    private LoadImage loadImage;


    public NewsAdapter(Context context, ListView lv) {
        super(context);
        defaultBitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.defaultpic);
        listView = lv;
        loadImage = new LoadImage(context, listener);
    }

    private LoadImage.ImageLoadListener listener = new LoadImage.ImageLoadListener() {

        @Override
        public void imageLoadOk(Bitmap bitmap, String url) {
            //类似于findViewById得到每一个listview的图片通过异步加载显示图片
            ImageView iv = (ImageView) listView.findViewWithTag(url);
            Log.d(TAG, "imageLoadOk: " + url);
            LogUtil.d(url);
            if (iv != null) {
                LogUtil.d("异步加载得到图片的url=" + url);
                Log.d(TAG, "imageLoadOk: 图片图片图片图片");
                iv.setImageBitmap(bitmap);
            }
        }
    };

    //返回每一个字条目的视图
    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {

        HoldView holdView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mainlist_detail, null);
            holdView = new HoldView(convertView);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }
        News news = list.get(position);
        //得到图片的地址
        String urlImage =news.getIcon();
        //给每个图片存入编号
        holdView.iv_main.setTag(CommonUtil.NETPATH+urlImage);
        //// 获取图片 1.  先从网络 2. 如果已经下载过了存在本地文件中下次启动程
       // 序从文件读取 3.. 当程序不关闭再次进入该界面从内存中读取
        Bitmap bitmap=loadImage.getBitmap(urlImage);
        //如果不是网络则马上加载可以是文件或内存
        if (bitmap!=null){
            holdView.iv_main.setImageBitmap(bitmap);
        }
        holdView.tv_main_title.setText(news.getTitle());
        holdView.tv_main_text.setText(news.getSummary());
     //   holdView.iv_main.setImageBitmap(defaultBitmap);//默认图片
        return convertView;


    }

    private class HoldView {
        public ImageView iv_main;
        public TextView tv_main_title;
        public TextView tv_main_text;

        public HoldView(View convertView) {
            this.iv_main = (ImageView) convertView.findViewById(R.id.imageView1);
            this.tv_main_title = (TextView) convertView.findViewById(R.id.textView1);
            this.tv_main_text = (TextView) convertView.findViewById(R.id.textView2);
        }
    }
}
