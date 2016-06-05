package com.example.zhengshujuan.newsapp.entity;

/**
 * Created by l on 2016/6/5.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.example.zhengshujuan.newsapp.biz.ImageLoadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取图片的公共类
 * 根据图片的 uri  存放两个方法
 * 1. 存文件 2. 存内存
 * 2. 取得文件 2.1  从网络获取 2.2  从文件获取 2.3  从内存获取
 * 3. 最终调用的方法得到图片先判断是不是内存 -- 文件 -- 网络
 */
public class LoadImage {
    private Context context;
    private ImageLoadListener listener;

    public LoadImage(Context context, ImageLoadListener listener) {
        this.context = context;
        this.listener = listener;
    }
    /*
    * 自定义回调接口传递图片和图片路径
    * */

    public interface ImageLoadListener {
        void imageLoadOk(Bitmap bitmap, String url);
    }

    /**
     * 异步加载类
     * 1.Url  处理的网络路径
     * 2. 无    Void 当加载一条时传递的类型
     * 3. 返回的加载后的数据：Bitmap
     */
    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        //执行之前的线程

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //后台运行新线程的代码不能修改ui
        @Override
        protected Bitmap doInBackground(String... params) {
            //单条加载更新ui
            publishProgress();
            url = params[0];
            Bitmap bitmap = null;
            try {
                //建立网络连接
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //得到输入字符流
                InputStream inputStream = conn.getInputStream();
                //得到图片
                bitmap = BitmapFactory.decodeStream(inputStream);
                //存入软引用中
                saveSoftReference(params[0], bitmap);
                //存在缓存文件中
                saveCachFile(params[0], bitmap);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        //doInBackground  执行 return
// 后马上执行 ui  线程并把结果传递给此方法 execute(url)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (listener != null) {
                //线程结束后返回图片和路径
                listener.imageLoadOk(bitmap, url);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private void getBitmapAsync(String url) {
        //自定义的异步加载类
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask();
        //执行加载
        imageAsyncTask.execute(url);
    }
/*
* 定义图片缓存机制
* 最终调用的方法: 先查看内存中有没有，再看缓存文件中有没有，最后只能向网络
请求图片
*
* */
    public Bitmap getBitmap(String url){
        Bitmap bitmap=null;
        if (url==null||url.length()<=0){
            return bitmap;
        }
        //内存中
        bitmap=getBitmapSoftReferences(url);
        if (bitmap!=null){
            return bitmap;
        }
        //缓存文件中
        bitmap=getBitmapFromCache(url);
        if (bitmap!=null){
            return bitmap;
        }
        //网络的
        getBitmapAsync(url);
        return bitmap;
    }
    private void publishProgress(String[] params) {
    }

    //软引用存内存只要不关闭该程序一直存放????
    private static Map<String, SoftReference<Bitmap>> softReferences = new HashMap<String, SoftReference<Bitmap>>();

    //定义内存缓存存储和获取的方法
    public void saveSoftReference(String url, Bitmap bitmap) {
        //存在内存中的一个图片
        SoftReference<Bitmap> softBitmap = new SoftReference<Bitmap>(bitmap);
        //存在集合中
        softReferences.put(url, softBitmap);
    }

    //得到内存的图片软引用
    public Bitmap getBitmapSoftReferences(String url) {
        Bitmap bitmap = null;
        //如果内存集合中根据key得到values
        if (softReferences.containsKey(url)) {
            //得到软引用中的图片
            bitmap = softReferences.get(url).get();
        }
        return bitmap;
    }

    //本地缓存存储和获取的方法
    public void saveCachFile(String url, Bitmap bitmap) {
        //获取文件名
        String name = url.substring(url.lastIndexOf("/") + 1);
        //返回的路径目录应用程序文件  得到缓存目录 如果不存在 创建
        File cacheDir = context.getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        //建立输出流
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(cacheDir, name));
            //存图片到文件
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从缓存文件中获取图片
     */
    private Bitmap getBitmapFromCache(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        //获取当前包下的缓存文件路径
        File cacheDri = context.getCacheDir();
        //得到该文件夹下的所有文件
        File[] files = cacheDri.listFiles();
        if (files == null) {
            return null;
        }
        File bitFile = null;
        //如有名字和传入的文件名一致的则找到图片
        for (File file : files) {
            if (file.getName().equals(name)) {
                bitFile = file;
                break;
            }
            //如果没有找到则返回空
            if (bitFile == null) {
                return null;
            }
        }
        //将找到的文件转化为bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(bitFile.getAbsolutePath());
        return bitmap;
    }

}







