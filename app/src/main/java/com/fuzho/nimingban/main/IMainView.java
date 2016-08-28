package com.fuzho.nimingban.main;

import com.fuzho.nimingban.pojo.Article;

import java.util.ArrayList;

/**
 * Created by fuzhongqing on 16/8/27.
 * 主界面 的接口
 */
public interface IMainView {
    void showProcessBar(boolean b);
    void showToast(String msg);
    void showLoadingMore(boolean b);
    void setData(ArrayList<Article> s);
}
