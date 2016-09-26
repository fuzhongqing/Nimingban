package com.fuzho.nimingban.post;

import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.pojo.Menu;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by fuzho on 2016/9/6.
 */
public interface IPostPersenter {
    //面向View
    void getArticles(int id);
    void getMenuList();
    //面向Model
    void getArticlesCallBack(ArrayList<Article> articles);
    void onErrorCallBack(String msg);
}
