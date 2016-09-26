package com.fuzho.nimingban.send;

/**
 * Created by fuzho on 2016/9/20.
 */
public interface ISendPresenter {
    //for SendView
    void updateTitle(String title);
    void updateEmail(String email);
    void updateName(String name);
    void send();
    //for Model
    void callback(String msg);
    void onFailed(String msg);
}
