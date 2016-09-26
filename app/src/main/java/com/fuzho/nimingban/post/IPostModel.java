package com.fuzho.nimingban.post;

import rx.Observable;

/**
 * Created by fuzho on 2016/9/6.
 */
public interface IPostModel {
    void getArticles(int id);
    void LoadMore();
    void setTid(String tid);
    void getMenu();
}
