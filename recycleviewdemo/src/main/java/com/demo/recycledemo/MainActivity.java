package com.demo.recycledemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.demo.recycledemo.BaseUse.BaseUseActivity;
import com.demo.recycledemo.wrap.HeaderAndFooterAdapterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 基本使用
     *
     * @param view
     */
    public void baseUseClick(View view) {
        Intent intent = BaseUseActivity.buildStartIntent(this);
        startActivity(intent);
    }

    /**
     * 基本使用
     *
     * @param view
     */
    public void headerAndFooterClick(View view) {
        Intent intent = HeaderAndFooterAdapterActivity.buildStartIntent(this);
        startActivity(intent);
    }
}
