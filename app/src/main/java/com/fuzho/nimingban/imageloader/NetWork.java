package com.fuzho.nimingban.imageloader;

import android.graphics.Bitmap;

/**
 * Created by fuzho on 2016/9/20.
 */
public interface NetWork {
    interface Listener {
        void onSucess(Bitmap bitmap);
        void onError(Object String);
    }
    void doRequest(String url ,int maxh, int maxw);
    void setmListener(Listener mListener);
}
