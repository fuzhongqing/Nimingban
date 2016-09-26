package com.fuzho.nimingban.imageloader;

import android.graphics.Bitmap;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.fuzho.nimingban.Application;

/**
 * Created by fuzho on 2016/9/20.
 */
public class VolleyBasedNetWork implements NetWork {

    Listener mListener;

    @Override
    public void doRequest(String url, int maxw, int maxh) {
        Application.getRequestQueue().add(new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if (mListener != null) mListener.onSucess(response);
            }
        }, maxw, maxh, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mListener != null) mListener.onError(error);
            }
        }));
    }

    @Override
    public void setmListener(Listener mListener) {
        this.mListener = mListener;
    }
}
