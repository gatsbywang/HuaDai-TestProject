package com.demo.hookactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.hookactivity.plugin.PluginManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private final String mPluginPath = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "xxx.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View view) {

        Intent intent = new Intent();
        intent.setClassName(getPackageName(), "com.demo.hookactivity.TestActivity");
        startActivity(intent);
    }

    public void install(View view) {
        //这里只是把类替换了，还需要进行资源加载
        PluginManager.install(this, mPluginPath);
    }


}
