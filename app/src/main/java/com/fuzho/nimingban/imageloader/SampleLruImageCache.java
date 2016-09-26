package com.fuzho.nimingban.imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by fuzho on 2016/9/20.
 * 内存缓存
 */
public class SampleLruImageCache implements Cache<String,Bitmap> {

    static final String TAG = "SampleLruImageCache";
    private LruCache<String,Bitmap> mCache;
    public SampleLruImageCache() {
        int maxSize = 10 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getHeight() * value.getWidth();
            }
        };
    }
    @Override
    public void set(String key, Bitmap object) {
        mCache.put(key,object);
    }

    @Override
    public Bitmap get(String key) {
        return mCache.get(key);
    }
}
