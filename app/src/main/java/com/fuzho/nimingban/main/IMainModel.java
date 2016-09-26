package com.fuzho.nimingban.main;

import com.android.volley.Response;

import org.json.JSONObject;


/**
 * Created by fuzhongqing on 16/8/27.
 */
public interface IMainModel {
    void getArticles();
    void LoadMore();
    void setTid(String tid);
    void getMenu();
}
