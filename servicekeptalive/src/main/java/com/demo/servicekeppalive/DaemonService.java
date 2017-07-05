package com.demo.servicekeppalive;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by 花歹 on 2017/6/28.
 * Email:   gatsbywang@126.com
 * Description: 守护进程,双进程通信
 * Thought:
 */

public class DaemonService extends Service {

    private final int DaemonId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(DaemonId, new Notification());
        bindService(new Intent(this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {
        };
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(DaemonService.this, "建立链接", Toast.LENGTH_SHORT);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开链接，重新启动，重新绑定
            startService(new Intent(DaemonService.this, MessageService.class));
            bindService(new Intent(DaemonService.this, MessageService.class), mServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
