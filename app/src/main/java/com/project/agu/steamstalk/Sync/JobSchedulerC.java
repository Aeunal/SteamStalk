package com.project.agu.steamstalk.Sync;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

public class JobSchedulerC {

    private static       boolean sInitialized = false;

    synchronized public static void scheduleChargingReminder(@NonNull final Context context) {

        if(!sInitialized)
        {
            ComponentName componentName = new ComponentName(context, SteamStalkJobService.class);

            JobInfo info = new JobInfo.Builder(123,componentName)
                    .setRequiresCharging(true)
                    .setPersisted(true)
                    .setPeriodic(15 * 60 * 1000)
                    .build();

            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            int result = scheduler.schedule(info);
            if(result == JobScheduler.RESULT_SUCCESS)
            {
                Log.d("job" , "yep");
            }
            sInitialized = true;
        }



    }



}
