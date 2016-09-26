package com.fuzho.nimingban.post;

import android.util.Log;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.main.MainView;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.pojo.Menu;

import java.util.ArrayList;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by fuzho on 2016/9/7.
 */
public class PostPersenter extends BasePresenter implements IPostPersenter {
    private static final String TAG = "PostPersenter";
    PostModel mPostModel;
    PostView mPostView;

    public PostPersenter(PostView postView) {
        mPostModel = new PostModel(this);
        mPostView  = postView;
    }
    @Override
    public void getArticles(int id) {
        mPostModel.getArticles(id);
    }

    @Override
    public void getMenuList() {

    }

    @Override
    public void getArticlesCallBack(ArrayList<Article> articles) {
        mPostView.setData(articles);
    }

    @Override
    public void onErrorCallBack(String msg) {

    }

    public void LoadMore() {
        ((PostView)getView()).showLoadingMore(true);
        mPostModel.LoadMore();
    }
}
