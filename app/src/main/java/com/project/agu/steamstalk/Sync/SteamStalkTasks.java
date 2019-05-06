package com.project.agu.steamstalk.Sync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.project.agu.steamstalk.GameActivity;
import com.project.agu.steamstalk.UserActivity;
import com.project.agu.steamstalk.Utilities.NotificationUtils;

import java.util.Random;


public class SteamStalkTasks  {

    private static final String[] GAME_ID = {"221910" , "20920" , "4700" , "48700" , "359550" , "485510" , "221380" , "236850" , "242760" , "374320" , "48700"};
    private static final String[] USER_ID = {"76561198223105864" , "76561198096674104" , "76561198312476051" , "76561198097355936" , "76561198198663550" , "76561197960287930"};

    public static final String ACTION_SHOW_GAME = "show-game";
    public static final String ACTION_SHOW_USER = "show-user";
    public static final String ACTION_DISMISS_NOTIFICATION  = "dismiss-notification";
    public static final String ACTION_CHARGE_NOTIFICATION_USER  = "user-notification";


    public static void executeTask(Context context, String action)
    {
        Log.d("debug" , "in execute task");
        if (ACTION_SHOW_GAME.equals(action))
        {
            Log.d("debug" , "show game");
            Random rand = new Random();
            Intent gameIntent = new Intent(context, GameActivity.class);
            gameIntent.putExtra(Intent.EXTRA_TEXT, GAME_ID[rand.nextInt(GAME_ID.length)]);
            context.startActivity(gameIntent);
        }
        else if (ACTION_SHOW_USER.equals(action))
        {
            Log.d("debug" , "show game");
            Random rand = new Random();
            Intent userIntent = new Intent(context, UserActivity.class);
            userIntent.putExtra(Intent.EXTRA_TEXT, USER_ID[rand.nextInt(USER_ID.length)]);
            context.startActivity(userIntent);
        }
        else if (ACTION_DISMISS_NOTIFICATION.equals(action))
        {
            NotificationUtils.clearAllNotifications(context);
        }
        else if (ACTION_CHARGE_NOTIFICATION_USER.equals(action))
        {
            NotificationUtils.tauntUserWithRandomUser(context);
        }
    }



















}
