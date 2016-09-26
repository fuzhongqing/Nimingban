package com.fuzho.nimingban.send;

import android.util.Log;

import com.fuzho.nimingban.pojo.SendMessage;

/**
 * Created by fuzho on 2016/9/20.
 */
public class SendModel implements ISendModel {

    private static final String TAG = "SendModel";
    private SendMessage mSendMessage;

    public SendModel() {
        mSendMessage = new SendMessage();
    }

    @Override
    public void send() {
        Log.d(TAG, "send --->" + mSendMessage.toString());
        Log.d(TAG, "send --->" + mSendMessage.getEmail());
    }

    @Override
    public void updateMessage() {

    }

    @Override
    public SendMessage getMessage() {
        return this.mSendMessage;
    }
}
