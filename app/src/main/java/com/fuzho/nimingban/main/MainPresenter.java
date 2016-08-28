package com.fuzho.nimingban.main;

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
        ((MainView)getView()).showProcessBar(true);
        model.getArticles();
    }

    @Override
    public void LoadMore() {

    }

    @Override
    public void getArticlesCallBack(ArrayList<Article> articles) {
        ((MainView)getView()).showProcessBar(false);
        ((MainView)getView()).setData(articles);
    }

    @Override
    public void onErrorCallBack(String msg) {

    }

}
