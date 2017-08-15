package com.demo.hookactivity;

import android.app.Application;

/**
 * Created by 花歹 on 2017/8/14.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HookStartActivityUtil hookStartActivityUtil = new HookStartActivityUtil(getApplicationContext(),
                ProxyActivity.class);
        try {
            hookStartActivityUtil.hookStartActivity();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
