package com.sanron.sunweather.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.sanron.sunweather.R;
import com.sanron.sunweather.common.CityProvider;
import com.sanron.sunweather.engine.AppConfig;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            Log.i("versionCode", "current versioncode " + packageInfo.versionCode);
            Log.i("versionName", "current versionname " + packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_splash);
        initData();
    }

    private void initData() {
        final long start = SystemClock.uptimeMillis();
        new Thread() {
            public void run() {
                CityProvider.init(SplashActivity.this);
                AppConfig.getInstance(SplashActivity.this);

                if ((SystemClock.uptimeMillis() - start) < 500) {
                    SystemClock.sleep(500 - (SystemClock.uptimeMillis() - start));
                }
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            ;
        }.start();
    }
}
