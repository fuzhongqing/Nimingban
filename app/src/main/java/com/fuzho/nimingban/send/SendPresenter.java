package com.fuzho.nimingban.send;

import com.fuzho.nimingban.BasePresenter;

/**
 * Created by fuzho on 2016/9/19.
 */
public class SendPresenter extends BasePresenter implements ISendPresenter {

    private ISendModel mSendModel;

    public SendPresenter() {
        mSendModel = new SendModel();
    }
    @Override
    public void updateTitle(String title) {
        mSendModel.getMessage().setTitle(title);
    }

    @Override
    public void updateEmail(String email) {
        mSendModel.getMessage().setEmail(email);
    }

    @Override
    public void updateName(String name) {
        mSendModel.getMessage().setName(name);
    }

    @Override
    public void send() {
        mSendModel.send();
    }

    @Override
    public void callback(String msg) {

    }

    @Override
    public void onFailed(String msg) {

    }
}
