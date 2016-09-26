package com.fuzho.nimingban.post;

import com.fuzho.nimingban.pojo.Article;

import java.util.ArrayList;

/**
 * Created by fuzho on 2016/9/6.
 */
public interface IPostView {
    void showProcessBar(boolean b);
    void showToast(String msg);
    void showLoadingMore(boolean b);
    void setData(ArrayList<Article> s);
}
