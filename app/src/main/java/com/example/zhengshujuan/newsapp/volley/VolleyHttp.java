package com.example.zhengshujuan.newsapp.volley;

import android.content.Context;
import android.net.sip.SipSession;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.zhengshujuan.newsapp.R;


import java.io.File;

/**
 * Created by zhengshujuan on 2016/6/1.
 */
public class VolleyHttp {
    public static RequestQueue mQueue;
    Context context;
    public VolleyHttp(Context context){
        //没有网络请求的时候,创建一个网络请求对象?
        if (mQueue==null){
            mQueue= Volley.newRequestQueue(context);
        }
        this.context=context;
    }
    public void getJSONObject(String url, Response.Listener<String> listener,
                              Response.ErrorListener errorListener){
        StringRequest request=new StringRequest(url,listener,errorListener);
        mQueue.add(request);
    }
    public void addImage(String url, ImageLoader.ImageCache imageCache, ImageView iv){
        ImageLoader mImageLoader = new ImageLoader(mQueue, imageCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(iv,
                R.drawable.defaultpic, android.R.drawable.ic_delete);
        mImageLoader.get(url, listener);
    }
    public void upLoadImage(String url, File file, Response.Listener<String> listener
    , Response.ErrorListener errorListener){
      //  MultiPosttRequest request=new MultiPosttRequest(url,listener,errorListener);
    }

}
