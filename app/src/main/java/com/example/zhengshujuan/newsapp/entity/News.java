package com.example.zhengshujuan.newsapp.entity;

import java.io.Serializable;

/**
 * Created by zhengshujuan on 2016/6/1.
 */
public class News implements Serializable {
    //新闻id
    private int nid;
    //标题
    private String title="";
    //摘要
    private String summary="";
    //图标
    private String icon="";
    //网页链接
    private String link="";
    //新闻类型
    private int type;
    //时间戳
    private String stamp;

    public News(int type, int nid, String stamp, String icon, String title, String summary, String link) {
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public String getLink() {
        return link;
    }

    public int getType() {
        return type;
    }

    public News(int nid, String title, String summary, String list_image,
                String url, int type){
        this.nid=nid;
        this.title=title;
        this.summary=summary;
        this.icon=list_image;
        this.link=url;
        this.type=type;
        this.stamp=stamp;
    }

    @Override
    public String toString() {
        return "News[nid="+nid+",title="+title+",summary=" + summary+"," +
                " icon=" + icon +",url=" + link + ", type=" + type+"]";
    }

    public int getNid() {
        return nid;
    }

    public String  getStamp() {
        return stamp;
    }
}
