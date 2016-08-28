package com.fuzho.nimingban.main;

import android.util.Log;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.pojo.Article;

import java.util.ArrayList;


/**
 * Created by fuzhongqing on 16/8/27.
 *
 */
public class MainPresenter extends BasePresenter implements IMainPresenter{
    static final String TAG = "MainPresenter";
    private IMainModel model;
    public MainPresenter() {
        model = new MainModel(this);
    }
    @Override
    public void getArticles() {
        Log.d(TAG,"加载中....");
        ((MainView)getView()).showProcessBar(true);
        model.getArticles();
    }

    @Override
    public void LoadMore() {

    }

    @Override
    public void getArticlesCallBack(ArrayList<Article> articles) {
        Log.d(TAG,"加载完成...");
        ((MainView)getView()).showProcessBar(false);
        ((MainView)getView()).setData(articles);
    }

    @Override
    public void onErrorCallBack(String msg) {
        ((MainView)getView()).showProcessBar(false);
    }

}
