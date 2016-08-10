package com.sanron.sunweather.activities;

import android.app.Application;

import com.sanron.sunweather.engine.CrashHandler;

/**
 * Created by Administrator on 2015/12/1.
 */
public class SunApplication extends Application {

    private CrashHandler crashHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        crashHandler = CrashHandler.getmInstance(this);
    }
}
