package com.fuzho.nimingban.post;

import android.app.Application;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.fuzho.nimingban.pojo.Article;
import com.zzhoujay.richtext.RichText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fuzho on 2016/9/7.
 */
public class PostModel implements IPostModel {

    ArrayList<Article> mArticles;
    PostPersenter mPostPersenter;
    RequestQueue mRequestQueue;
    int page;
    int id;
    public PostModel(PostPersenter postPersenter) {
        mArticles = new ArrayList<>();
        mPostPersenter = postPersenter;
        mRequestQueue = com.fuzho.nimingban.Application.getRequestQueue();
        page = 0;
    }
    @Override
    public void getArticles(int id) {
        if (id == -1) {
            id = this.id;
            page = 1;
        } else {
            page = 1;
            this.id = id;
        }
        com.fuzho.nimingban.Application.getRequestQueue().add(new JsonObjectRequest("https://h.nimingban.com/Api/thread/id/" + id + "/page/1",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("model",response.toString());
                        mArticles.clear();
                        try {
                            Article mainArticle = new Article(response);
                            mainArticle.setType(Article.TYPE.MAIN);
                            mArticles.add(mainArticle);
                            JSONArray jsonArray = response.getJSONArray("replys");
                            for (int i = 0;i < jsonArray.length(); ++i) {
                                mArticles.add(new Article(jsonArray.getJSONObject(i)));
                            }
                            mPostPersenter.getArticlesCallBack(mArticles);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
    }

    @Override
    public void LoadMore() {
        page++;
        mRequestQueue.add(new StringRequest("https://h.nimingban.com/Api/thread/id/" + id + "/page/" + page, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONObject(response).getJSONArray("replys");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mArticles.add(new Article(jsonArray.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                    mPostPersenter.onErrorCallBack(e.getLocalizedMessage());
                }
                mPostPersenter.getArticlesCallBack(mArticles);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getLocalizedMessage());
                mPostPersenter.onErrorCallBack(error.getLocalizedMessage());
            }
        }));
    }

    @Override
    public void setTid(String tid) {
    }

    @Override
    public void getMenu() {

    }
}
