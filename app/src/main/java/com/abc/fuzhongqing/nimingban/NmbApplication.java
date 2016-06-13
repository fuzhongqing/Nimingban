package com.abc.fuzhongqing.nimingban;

import android.app.Application;

import com.abc.fuzhongqing.nimingban.resources.Util;

import org.json.JSONArray;

/**
 * Created by fuzhongqing on 16/6/10.
 *
 * Application
 */
public class NmbApplication extends Application {
    public String currForm;
    public String currFormName;
    public JSONArray menu;
    @Override
    public void onCreate() {
        super.onCreate();
        currForm = Constants.defaultFid;
        currFormName = Constants.defaultFname;
        menu = null;

    }
}
