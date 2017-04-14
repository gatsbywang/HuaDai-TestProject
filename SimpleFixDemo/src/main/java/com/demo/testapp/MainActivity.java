package com.demo.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btn_test);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
        downLoadFixDexFiles();
    }
//    private void fixDexBug() {
//        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
//        if (fixFile.exists()) {
//            FixDexManager fixDexManager = new FixDexManager(this);
//            try {
//                fixDexManager.fixDex(fixFile.getAbsolutePath());
//                Toast.makeText(this, "修复成功", Toast.LENGTH_LONG).show();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(this, "修复失败", Toast.LENGTH_LONG).show();
//            }
//        }
//    }

    private void downLoadFixDexFiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    inputStream = getApplicationContext().getAssets().open("fix.dex");
                    fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/fix.dex");
                    byte[] buffer = new byte[1024 * 8];
                    int len = -1;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }

                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
                if (fixFile.exists()) {
                    FixDexManager fixDexManager = new FixDexManager(getApplicationContext());
                    try {
                        fixDexManager.addFixDex(fixFile.getAbsolutePath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
