package com.fuzho.nimingban.main;

import com.fuzho.nimingban.pojo.Article;

import java.util.ArrayList;

/**
 * Created by fuzhongqing on 16/8/27.
 */
public interface IMainPresenter {
    //面向View
    void getArticles();
    void LoadMore();
    //面向Model
    void getArticlesCallBack(ArrayList<Article> articles);
    void onErrorCallBack(String msg);
}
