package com.fuzho.nimingban.main;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.fuzho.nimingban.Application;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.pojo.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;


/**
 * Created by fuzhongqing on 16/8/27.
 *
 */
public class MainModel implements IMainModel{
    static final String TAG = "MainModel";
    private MainPresenter mPresenter;
    private RequestQueue mRequestQueue;
    private String tid;
    private int page;
    private ArrayList<Article> mArticles;
    public MainModel(MainPresenter m) {
        mRequestQueue = Application.getRequestQueue();
        mPresenter = m;
        //默认的tid
        tid = "4";
        page = 0;
        mArticles = new ArrayList<>();
    }
    @Override
    public void getArticles() {
        // TODO: 16/8/27 添加缓存功能
        mRequestQueue.add(new StringRequest("https://h.nimingban.com/Api/showf/id/" + tid + "/page/" + page, new Response.Listener<String>() {
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
        page++;
        mRequestQueue.add(new StringRequest("https://h.nimingban.com/Api/showf/id/" + tid + "/page/" + page, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mArticles.add(new Article(jsonArray.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                    mPresenter.onErrorCallBack(e.getLocalizedMessage());
                }
                mPresenter.getArticlesCallBack(mArticles);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getLocalizedMessage());
                mPresenter.onErrorCallBack(error.getLocalizedMessage());
            }
        }));
    }

    @Override
    public void setTid(String tid) {
        page = 0;
        this.tid = tid;
    }

    public String getTid() {
        return this.tid;
    }
    @Override
    public void getMenu() {
        mRequestQueue.add(new StringRequest("https://h.nimingban.com/Api/getForumList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Menu> menuGroup = new ArrayList<Menu>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        Menu sMenu = new Menu(jsonArray.getJSONObject(i));
                        menuGroup.add(sMenu);
                    }
                    mPresenter.setMenuList(menuGroup);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Nothing to do
            }
        }));
    }
}
