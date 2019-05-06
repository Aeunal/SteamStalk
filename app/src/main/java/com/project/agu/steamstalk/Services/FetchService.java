package com.project.agu.steamstalk.Services;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.project.agu.steamstalk.Data.SteamContract;
import com.project.agu.steamstalk.GameActivity;
import com.project.agu.steamstalk.HistoryAdapter;
import com.project.agu.steamstalk.Search;
import com.project.agu.steamstalk.UserActivity;

import java.util.Map;

public class FetchService extends IntentService {

    public static GameActivity gameActivity;
    public static UserActivity userActivity;
    public static HistoryAdapter historyAdapter;
    public static Search search;

    public static Context staticContext;

    public FetchService() {
        super("FetchService");
    }

    public static void startUserFetching(UserActivity activity, Context context, String api) {
        staticContext = context;
        userActivity = activity;
        Intent intent = new Intent(context, FetchService.class);
        intent.setAction(FetchTasks.ACTION_FETCH_USER_API);
        intent.putExtra(FetchTasks.PARAMETER_APPID_KEY, api);
        context.startService(intent);
    }

    public static void startHistoryFetching(HistoryAdapter adapter, Context context, String api) {
        staticContext = context;
        historyAdapter = adapter;
        Intent intent = new Intent(context, FetchService.class);
        intent.setAction(FetchTasks.ACTION_FETCH_HISTORY_API);
        intent.putExtra(FetchTasks.PARAMETER_APPID_KEY, api);
        context.startService(intent);
    }

    public static void startGameFetching(GameActivity activity, Context context, String api) {
        staticContext = context;
        gameActivity = activity;
        Intent intent = new Intent(context, FetchService.class);
        intent.setAction(FetchTasks.ACTION_FETCH_GAME_API);
        intent.putExtra(FetchTasks.PARAMETER_APPID_KEY, api);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (FetchTasks.ACTION_FETCH_GAME_API.equals(action)) {
                final String appid = intent.getStringExtra(FetchTasks.PARAMETER_APPID_KEY);
                handleFetchGame(appid);
            } else if (FetchTasks.ACTION_FETCH_USER_API.equals(action)) {
                final String appid = intent.getStringExtra(FetchTasks.PARAMETER_APPID_KEY);
                handleFetchUser(appid);
            } else if (FetchTasks.ACTION_FETCH_HISTORY_API.equals(action)) {
                final String appid = intent.getStringExtra(FetchTasks.PARAMETER_APPID_KEY);
                handleFetchHistory(appid);
            }
        }
    }

    private void handleFetchGame(String appid) {
        FetchTasks.executeTask(this, FetchTasks.ACTION_FETCH_GAME_API, appid);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleFetchUser(String appid) {
        FetchTasks.executeTask(this, FetchTasks.ACTION_FETCH_USER_API, appid);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleFetchHistory(String appid) {
        FetchTasks.executeTask(this, FetchTasks.ACTION_FETCH_HISTORY_API, appid);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void refreshData(String state, boolean isPrivate, Cursor cursor, String appid) {
        Log.i("GameData","trying to refresh some data");



        if (state.equals(FetchTasks.ACTION_FETCH_GAME_API)) {
            Log.i("GameData","trying to refresh gameData");
            Intent GameActivityIntent = new Intent(FetchService.staticContext, GameActivity.class);
            GameActivityIntent
                    .putExtra(Intent.EXTRA_TEXT, appid)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            GameActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            staticContext.startActivity(GameActivityIntent);
            gameActivity.overridePendingTransition(0,0);
            //gameActivity.refreshData(cursor);
        }
        Log.i("isPrivate", isPrivate+"");
        if (state.equals(FetchTasks.ACTION_FETCH_USER_API)) {
            Intent UserActivityIntent = new Intent(FetchService.staticContext, UserActivity.class);
            UserActivityIntent
                    .putExtra(Intent.EXTRA_TEXT, appid)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            UserActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            staticContext.startActivity(UserActivityIntent);
            userActivity.overridePendingTransition(0,0);
            if (!isPrivate) {
                //userActivity.mAdapterGames.refreshData("owned", cursor);
                //userActivity.mAdapterAchievements.refreshData("recent", cursor);
            }
            //userActivity.refreshData(isPrivate, cursor);
        }

        //Intent intent = new Intent();
        //intent.setAction("com.project.agu.steamstalk.FETCH_INTENT");
        //sendBroadcast(intent);
    }
}
