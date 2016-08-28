package com.fuzho.nimingban;

import java.lang.ref.WeakReference;

/**
 * Created by fuzhongqing on 16/8/27.
 * MVP 架构中 Presenter 的基础类
 */
public abstract class BasePresenter<V extends MVPBaseActivity,M>{
    protected WeakReference<V> mView;
    public void attachView(V view) {
        mView = new WeakReference<>(view);
    }

    protected V getView() {
        return mView.get();
    }

    public boolean isAttached() {
        return (mView != null) && (mView.get() != null);
    }

    public void dettachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }
}
