package com.fuzho.nimingban.main;

import android.content.Intent;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.fuzho.nimingban.BasePresenter;
import com.fuzho.nimingban.pojo.Article;
import com.fuzho.nimingban.pojo.Menu;

import java.util.ArrayList;


/**
 * Created by fuzhongqing on 16/8/27.
 *
 */
public class MainPresenter extends BasePresenter implements IMainPresenter{
    static final String TAG = "MainPresenter";
    private MainModel model;
    public MainPresenter() {
        model = new MainModel(this);
    }
    @Override
    public void getArticles() {
        Log.d(TAG , "getArticles");
        ((MainView)getView()).mSnackbar.dismiss();
        ((MainView)getView()).showProcessBar(true);
        model.getArticles();
    }

    @Override
    public void LoadMore() {
        ((MainView)getView()).showLoadingMore(true);
        model.LoadMore();
    }

    @Override
    public void getMenuList() {
        model.getMenu();
    }

    @Override
    public void getArticlesCallBack(ArrayList<Article> articles) {
        Log.d(TAG , "getArticles [Success] callback");
        ((MainView)getView()).showProcessBar(false);
        ((MainView)getView()).showLoadingMore(false);
        ((MainView)getView()).setData(articles);
    }

    @Override
    public void onErrorCallBack(String msg) {
        Log.d(TAG , "getArticles [Bad] callback");
        ((MainView)getView()).mSnackbar.setText("遇到一点问题,请下拉重新试一下").show();
        ((MainView)getView()).showProcessBar(false);
        if (msg != null) {
            ((MainView)getView()).showToast(msg);
        }
    }
    public void setTid(String id) {
        model.setTid(id);
    }
    @Override
    public void setMenuList(ArrayList<Menu> menus) {
        android.view.Menu mMenu = ((MainView)getView()).mMenu;
        for (int i = 0;i < menus.size();++i) {
            Menu e = menus.get(i);
            mMenu.addSubMenu(e.getName());
            for (int j = 0; j < e.getsMenu().size(); ++j) {
                Menu f = e.getsMenu().get(j);

                mMenu.getItem(i).getSubMenu().add(f.getName());
                MenuItem m = mMenu.getItem(i).getSubMenu().getItem(j);
                m.setIntent(new Intent().putExtra("id",f.getId()));
            }
        }
    }
}
