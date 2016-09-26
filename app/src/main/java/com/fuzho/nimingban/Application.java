package com.fuzho.nimingban;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.fuzho.nimingban.imageloader.Loader;
import com.fuzho.nimingban.imageloader.SampleLruImageCache;
import com.fuzho.nimingban.imageloader.VolleyBasedNetWork;

/**
 * Created by fuzhongqing on 16/8/27.
 * 自定义 Application 类
 */
public class Application extends android.app.Application{
    private static RequestQueue mRequestQueue;
    private static Loader mLoader;

    public static RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
    public static Loader getLoader() {
        return mLoader;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(this);
        if (mLoader == null) {
            mLoader= Loader.create(this)
                        .CacheWith(new SampleLruImageCache())
                        .networkWith(new VolleyBasedNetWork())
                        .LoadPicWith(R.drawable.img_loading)
                        .FailedPicWith(R.drawable.img_loading);
        }
    }
}
