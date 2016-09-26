package com.fuzho.nimingban.imageloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by fuzho on 2016/9/20.
 *
 */
public class Loader implements Cloneable{

    private static final String TAG = "ImageLoader";
    private int preLoadRes = -1;
    private int failedRes  = -1;
    private Cache mCache;
    private Resizer mResizer;
    private NetWork mNetWork;
    private String mUrl;
    private Context mContext;

    private Loader(Context ctx) {
        mContext = ctx;
    }

    /**
     * 一个Loader只会拥有至多一个Cache,旧的Cache(如果存在)将会被新的Cache取代
     */
    public Loader CacheWith(Cache cache) {
        mCache = cache;
        return this;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            throw new CloneNotSupportedException();
        }
        Loader l = new Loader(mContext);
        l.preLoadRes = preLoadRes;
        l.failedRes  = failedRes;
        l.mCache     = mCache;
        l.mResizer   = mResizer;
        l.mNetWork   = mNetWork;
        l.mUrl       = mUrl;
        return l;
    }

    public Loader resizeWith(Resizer resizer) {
        this.mResizer = resizer;
        return this;
    }

    public Loader LoadPicWith(int resId) {
        this.preLoadRes = resId;
        return this;
    }

    public Loader FailedPicWith(int resId) {
        this.failedRes = resId;
        return this;
    }

    public Loader networkWith(NetWork netWork) {
        this.mNetWork = netWork;
        return this;
    }

    public Loader from(String url) {
        this.mUrl = url;
        return this;
    }

    private Bitmap loadFromResId(int id) {
        return BitmapFactory.decodeResource(mContext.getResources(), id);
    }

    public Loader to(ImageView view) {
        return to(view ,0 ,0);
    }
    public Loader to(final ImageView view,final int maxWidth,final int maxHeight) {
        if (mUrl != null) {
            boolean hit = (mCache!=null && (mCache.get(mUrl) != null));
            if (hit) {
                //同步更新
                view.setImageBitmap((Bitmap) mCache.get(mUrl));
            } else {
                //网络异步
                if (mNetWork != null) {
                    Bitmap bm;
                    if ((bm = loadFromResId(preLoadRes)) != null) {
                        attach(mContext.getResources(),preLoadRes,maxWidth,maxHeight,view);
                    }
                    mNetWork.setmListener(new NetWork.Listener() {
                        @Override
                        public void onSucess(Bitmap bitmap) {
                            if (mCache != null) {
                                mCache.set(mUrl, bitmap);
                            }
                            view.setImageBitmap(bitmap);
                        }
                        @Override
                        public void onError(Object String) {
                            if (loadFromResId(failedRes) != null) {
                                attach(mContext.getResources(),failedRes,maxWidth,maxHeight,view);
                            }
                        }
                    });
                    mNetWork.doRequest(mUrl ,maxWidth, maxHeight);
                }
            }
        } else {
            Log.w(TAG , "httpClient is null");
        }
        return this;
    }

    public void attach(Resources res, int resId, int maxw, int maxh , ImageView iv) {
        if (res == null) return;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, maxw, maxh);
        options.inJustDecodeBounds = false;
        iv.setImageBitmap(BitmapFactory.decodeResource(res, resId, options));
    }

    public static Loader create(Context ctx) {
        if (ctx == null) {
            throw new NullPointerException("Context can't be null");
        }
        return new Loader(ctx);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqW,int reqH) {
        final int height = options.outHeight;
        final int width  = options.outWidth;
        int inSampleSize = 1;
        if (reqH == 0 || reqW == 0) return inSampleSize;

        if (height > reqH || width > reqW) {
            final int halfHeight = height / 2;
            final int halfWeight = width  / 2;

            while ((halfHeight / inSampleSize) >= reqH &&
                    (halfWeight / inSampleSize) >= reqW) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
