package com.demo.droidplugin;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.morgoo.droidplugin.pm.PluginManager;

public class MainActivity extends AppCompatActivity {

    private String apkPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void install() {
        try {
            int result = PluginManager.getInstance().installPackage(apkPath, 0);
            Log.e("TAG", "result = " + result);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startPlugin() {
        // 一定要这样
        PackageManager pm = getPackageManager();
        // 有了apk路径是可以获取apk的包名
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        String packageName = info.packageName;
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
