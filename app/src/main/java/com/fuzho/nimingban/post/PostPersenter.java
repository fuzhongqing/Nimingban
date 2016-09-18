package com.fuzho.nimingban.post;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.pojo.Menu;

import java.util.ArrayList;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fuzho on 2016/9/7.
 */
public class PostPersenter extends BasePresenter implements IPostPersenter {
    @Override
    public void getArticles(int id) {

    }

    @Override
    public void getMenuList() {

    }

    @Override
    public void getArticlesCallBack(ArrayList<Article> articles) {

    }

    @Override
    public void onErrorCallBack(String msg) {

    }
}
