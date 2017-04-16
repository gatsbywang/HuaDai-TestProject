package com.demo.testapp;

import android.app.Application;
import android.util.Log;

/**
 * Created by wang on 2017/4/14.
 */

public class BaseApplication extends Application {

    private final String TAG = BaseApplication.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        //每次进来都要加载新的包
        try {
            FixDexManager fixDexManager = new FixDexManager(this);
            fixDexManager.loadFixDex();
            Log.i(TAG, "修复成功");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "修复失败");
        }

    }
}
