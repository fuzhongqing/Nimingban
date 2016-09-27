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
public class Lru {

    static final String TAG = "LruCache";
    private LruCache<String,String> mCache;

    public Lru() {

        int maxSize = 10 * 1024 * 1024;
        mCache = new LruCache<String,String>(maxSize) {
            @Override
            protected int sizeOf(String key, String value) {
                return value.length();
            }
        };
    }

    public String getString(String id) {
        return mCache.get(id);
    }

    public void putString(String id, String content) {
        mCache.put(id, content);
    }
}
