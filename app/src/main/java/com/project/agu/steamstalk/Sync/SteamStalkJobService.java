package com.project.agu.steamstalk.Sync;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class SteamStalkJobService extends JobService {
    private AsyncTask mBackgroundTask;




    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        Log.d("jobTask", "started");
        mBackgroundTask = new AsyncTask()
        {

            @Override
            protected Object doInBackground(Object[] params)
            {
                Context context = SteamStalkJobService.this;
                SteamStalkTasks.executeTask(context, SteamStalkTasks.ACTION_CHARGE_NOTIFICATION_USER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(jobParameters, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("jobTask", "stopped");
        if (mBackgroundTask != null)
        {
            mBackgroundTask.cancel(true);
        }
        return true;
    }
}
