package com.demo.skin;

import android.app.Application;

import com.demo.skin.skin.SkinManager;

/**
 * Created by 花歹 on 2017/5/23.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SkinManager.getInstance().init(this);
    }
}
