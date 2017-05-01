package com.demo.base.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setIcon(R.mipmap.ic_launcher)
//                .setTitle("AlertDialog")
//                .setMessage("这是App的AlertDialog")
//                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        builder.create().show();
    }

    public void onClick(View view) {
        BaseDialog.Builder builder = new BaseDialog.Builder(this);
        builder.setContentView(R.layout.layout_dialog)
                .setOnClickListener(R.id.tv_left, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "left click", Toast.LENGTH_LONG).show();
                    }
                })
                .setOnClickListener(R.id.tv_right, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "right click", Toast.LENGTH_LONG).show();
                    }
                }).fromBottom(true).fullWidth().show();
    }
}
