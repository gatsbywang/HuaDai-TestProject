package com.demo.hookactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 花歹 on 2017/8/14.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

public class HookStartActivityUtil {

    private final String TAG = HookStartActivityUtil.class.getName();
    private Context mContext;

    public Class<?> mProxyClass;
    private final String EXTRA_ORIGIN_INTENT = "key";

    public HookStartActivityUtil(Context context, Class<?> proxyClass) {
        this.mContext = context.getApplicationContext();
        this.mProxyClass = proxyClass;
    }

    private void hookLaunchActivity() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        //1、获取ActivityThread的实例
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field sCurrentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        sCurrentActivityThreadField.setAccessible(true);
        Object activityThread = sCurrentActivityThreadField.get(null);
        //获取ActivityThread中的mH
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Handler mHHandler = (Handler) mHField.get(activityThread);
        //hook handlerLaunchActivity
        //给Handler设置CallBack回调，也要通过反射
        Class<?> handlerClass = mHHandler.getClass();
        Field mCallbackField = handlerClass.getDeclaredField("mCallback");
        mCallbackField.setAccessible(true);
        mCallbackField.set(mHHandler, new HandlerCallBack());
    }

    private class HandlerCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            Log.e(TAG, "handleMessage");

            //每发一个消息，都会走一次这个callBack方法

            if (100 == msg.what) {
                handleLaunchActivity(msg);
            }
            return false;
        }

        /**
         * 开始启动创建Activity拦截
         *
         * @param msg
         */
        private void handleLaunchActivity(Message msg) {
            try {
                Object record = msg.obj;
                //从ActivityClientRecord中获取过安检的Intent
                Field intentField = record.getClass().getDeclaredField("intent");
                Intent sageIntent = (Intent) intentField.get(record);
                //从safeIntent中获取原来的originIntent
                Intent originIntent = sageIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
                //将原来的originIntent设置回去
                if (originIntent != null) {
                    intentField.set(record, originIntent);
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }
    }


    public void hookStartActivity() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        //获取ActvitiyManagerNative里面的gDefault
        Class<?> amnClass = Class.forName("android.app.ActvitiyManagerNative");
        //获取属性
        Field gDefaultField = amnClass.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);
        //gDefault为static,所以传null
        Object gDefault = gDefaultField.get(null);

        //获取gDefault中的mInstance属性
        Class<?> singleTonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singleTonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object iamInstance = mInstanceField.get(gDefault);

        Class<?> iamClass = Class.forName("android.app.IActivityManager");
        Object proxyIamInstance = Proxy.newProxyInstance(HookStartActivityUtil.class.getClassLoader(), new Class[]{iamClass},
                new startActivityInvocationHandler(iamInstance));

        //重新指定
        mInstanceField.set(gDefault, proxyIamInstance);

    }

    private class startActivityInvocationHandler implements InvocationHandler {

        //方法执行
        private Object mObject;

        public startActivityInvocationHandler(Object object) {
            this.mObject = object;
        }


        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Log.i(TAG, "hookactivity:invoke");
            //替换Intent,过AndroidManifest检测
            if ("startActivity".endsWith(method.getName())) {
                //1、首先获取原来的Intent
                Intent originIntent = (Intent) args[2];
                //2、创建一个安全的
                Intent safeIntent = new Intent(mContext, mProxyClass);
                args[2] = safeIntent;
                //3、绑定原来的Intent
                safeIntent.putExtra(EXTRA_ORIGIN_INTENT, originIntent);
            }
            return method.invoke(mObject, args);
        }
    }
}
