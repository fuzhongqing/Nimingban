package com.fuzho.nimingban.send;

import com.fuzho.nimingban.pojo.SendMessage;

/**
 * Created by fuzho on 2016/9/20.
 */
public interface ISendModel {
    void send();
    void updateMessage();
    SendMessage getMessage();
}
