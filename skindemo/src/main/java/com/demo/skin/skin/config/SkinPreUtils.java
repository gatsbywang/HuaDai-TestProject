package com.demo.skin.skin.config;

import android.content.Context;

/**
 * Created by 花歹 on 2017/6/6.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class SkinPreUtils {
    private static SkinPreUtils mInstance;
    private Context mContext;

    private SkinPreUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static SkinPreUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SkinPreUtils.class) {
                if (mInstance == null) {
                    mInstance = new SkinPreUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存当前皮肤路径
     *
     * @param skinPath
     */
    public void saveSkinPath(String skinPath) {
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE).edit()
                .putString(SkinConfig.SKIN_PATH_NAME, skinPath).commit();

    }

    /**
     * 获取皮肤的路径
     *
     * @return
     */
    public String getSkinPath() {
        return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME, Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_PATH_NAME, "");

    }

    /**
     * 清空皮肤路径
     */
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
