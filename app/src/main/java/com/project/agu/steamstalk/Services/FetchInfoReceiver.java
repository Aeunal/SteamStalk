package com.project.agu.steamstalk.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class FetchInfoReceiver extends BroadcastReceiver {
    public FetchInfoReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ObservableObject.getInstance().updateValue(intent);
    }
}