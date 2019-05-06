package com.project.agu.steamstalk.Sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class SteamStalkIntentService extends IntentService {


    public SteamStalkIntentService() {super("SteamStalkIntentService");}

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        Log.d("debug" , "task executed");
        String action = intent.getAction();
        SteamStalkTasks.executeTask(this, action);
    }
}
