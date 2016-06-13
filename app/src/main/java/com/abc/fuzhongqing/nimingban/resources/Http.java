package com.abc.fuzhongqing.nimingban.resources;

import android.content.Context;

import com.litesuits.http.HttpConfig;
import com.litesuits.http.LiteHttp;

/**
 * Created by fuzhongqing on 16/6/10.
 *
 *
 */
public class Http {
    public static LiteHttp getHttp(Context mContext) {

        HttpConfig config = new HttpConfig(mContext)
                .setDebugged(true)
                .setDetectNetwork(true)
                .setDoStatistics(true)
                .setTimeOut(20000,20000);
        return LiteHttp.newApacheHttpClient(config);
    }
}
