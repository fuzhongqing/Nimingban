package com.fuzho.nimingban.send;

/**
 * Created by fuzho on 2016/9/20.
 */
public interface ISendView {
    void onCallback(String msg);
    void onfailed(String errorMsg);
}
