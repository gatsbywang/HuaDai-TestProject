package com.demo.servicekeppalive;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.List;

/**
 * Created by 花歹 on 2017/7/5.
 * Email:   gatsbywang@126.com
 * Description:
 * Thought:
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService {

    private final int jobWakeUpId = 1;

    //开启一个轮询
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        JobInfo.Builder jobBuilder = new JobInfo.Builder(jobWakeUpId, new ComponentName(this, JobWakeUpService.class));
        jobBuilder.setPeriodic(2000);

        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobBuilder.build());
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //开启定时任务，定时轮询，看MessageService有没有被杀死
        //如果杀死了启动 轮询onStartJob
        boolean isServiceAlive = isServiceAlive(MessageService.class.getName());
        if (!isServiceAlive) {
            startService(new Intent(JobWakeUpService.this, MessageService.class));
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private boolean isServiceAlive(String serviceName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        if (runningServices.size() <= 0) {
            return false;
        }

        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            String name = runningService.service.getClassName().toString();
            if (serviceName.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
