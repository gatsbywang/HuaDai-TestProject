package com.demo.ipcdemo;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 客户端一定要获取aild的实例
    private UserAidl mUserAidl;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //已连接
            mUserAidl = UserAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, MessageService.class));

        //隐式意图
        Intent intent = new Intent(this, MessageService.class);

        bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    public void getUserName(View view){
        try {
            Toast.makeText(this, mUserAidl.getUserName(), Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getPWD(View view){
        try {
            Toast.makeText(this, mUserAidl.getUserPwd(), Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bind(){}

    public void unbind(){}

}
