package com.fuzho.nimingban;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by fuzhongqing on 16/8/27.
 * 自定义 Application 类
 */
public class Application extends android.app.Application{
    private static RequestQueue mRequestQueue;

    //由于requestQueue的创建是需要 Context 的 所以
    //在这个方法被调用的时候 mRequestQueue 一定是非空的
    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(this);
    }


}
