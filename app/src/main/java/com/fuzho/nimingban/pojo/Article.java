package com.fuzho.nimingban.pojo;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by fuzhongqing on 16/8/27.
 *
 */
public class Article {
    public enum TYPE {
        MAIN,
        REPLY
    }
    private int id;
    private String uid;
    private String content;
    private String imgurl;
    private boolean isadmin;
    private int replys;
    private TYPE type;
    private String time;
    private boolean isSege;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public boolean isadmin() {
        return isadmin;
    }

    public void setIsadmin(boolean isadmin) {
        this.isadmin = isadmin;
    }

    public int getReplys() {
        return replys;
    }

    public void setReplys(int replys) {
        this.replys = replys;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public boolean isSege() {
        return isSege;
    }

    public void setSege(boolean sege) {
        isSege = sege;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Article(JSONObject object) throws JSONException {
        this.id = object.getInt("id");
        this.content = object.getString("content");
        this.imgurl = object.getString("img") + object.getString("ext");
        if ("".equals(object.getString("img"))) this.imgurl = "";
        this.isadmin = object.getInt("admin") != 0;
        this.isSege = object.getInt("sage") != 0;
        this.isSege = object.getInt("sage") != 0;
        this.time = object.getString("now");
        this.uid = object.getString("userid");
        this.type = object.has("replyCount") ? TYPE.MAIN : TYPE.REPLY;
        if (type == TYPE.MAIN) {
            this.replys = object.getInt("replyCount");
        }
    }

    Article() {
        this.content = "EMPTY";
    }

    public Article(String str) {
        this.content = str;
    }

    public static final Article emptyArticle = new Article();

}
