package com.fuzho.nimingban.main;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fuzho.nimingban.Application;
import com.fuzho.nimingban.pojo.Article;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


/**
 * Created by fuzhongqing on 16/8/27.
 *
 */
public class MainModel implements IMainModel{
    static final String TAG = "MainModel";
    private MainPresenter mPresenter;
    private RequestQueue mRequestQueue;
    private String tid;
    private ArrayList<Article> mArticles;
    public MainModel(MainPresenter m) {
        mRequestQueue = Application.getRequestQueue();
        mPresenter = m;
        //默认的tid
        tid = "4";
        mArticles = new ArrayList<>();
    }

    @Override
    public void getArticles() {
        // TODO: 16/8/27 添加缓存机制
        mRequestQueue.add(new StringRequest("https://h.nimingban.com/Api/showf/id/" + tid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    mArticles.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mArticles.add(new Article(jsonArray.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mPresenter.onErrorCallBack(e.getLocalizedMessage());
                }
                mPresenter.getArticlesCallBack(mArticles);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.getMessage();
                mPresenter.onErrorCallBack(error.getMessage());
            }
        }));
    }


    @Override
    public void LoadMore() {

    }
}
