package com.fuzho.nimingban.tools;

import android.graphics.Bitmap;
import android.nfc.Tag;
import android.text.LoginFilter;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by fuzhongqing on 16/8/29.
 */
public class BitMapCache implements ImageLoader.ImageCache {

    static final String TAG = "BitMapCache";
    private LruCache<String,Bitmap> mCache;

    public BitMapCache() {
        Log.d(TAG,"create cache system");
        int maxSize = 10 * 1024 * 1024;
        mCache = new LruCache<String,Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }
    @Override
    public Bitmap getBitmap(String url) {
        Log.d(TAG,"get url : " + url + " size : " + mCache.size() * 1.0 / (1024 * 1024) + "M");
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Log.d(TAG,"put url : " + url);
        mCache.put(url, bitmap);
    }
}
