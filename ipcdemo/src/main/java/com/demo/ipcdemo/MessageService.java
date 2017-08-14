package com.demo.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by 花歹 on 2017/6/19.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class MessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //绑定
        return mBinder;
    }

    private final UserAidl.Stub mBinder = new UserAidl.Stub() {

        @Override
        public String getUserName() throws RemoteException {
            return "Darren@163.com";
        }

        @Override
        public String getUserPwd() throws RemoteException {
            return "19940223";
        }
    };
}
