package com.demo.hookactivity.plugin;

import android.content.Context;

/**
 * Created by 花歹 on 2017/8/21.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class PluginManager {
    public static final void install(Context context, String apkPath) {
        try {
            FixDexManager fixDexManager = new FixDexManager(context);
            fixDexManager.addFixDex(apkPath);
            fixDexManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
