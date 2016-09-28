package com.fuzho.nimingban;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by fuzhongqing on 16/8/27.
 * MVP 框架中 View 的基础类
 */
public abstract class MVPBaseActivity<T extends BasePresenter> extends AppCompatActivity{

    protected T mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter.isAttached()) mPresenter.detachView();
    }

    protected abstract T getPresenter();

}
