package com.project.agu.steamstalk.Utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.project.agu.steamstalk.R;
import com.project.agu.steamstalk.Search;
import com.project.agu.steamstalk.Sync.SteamStalkIntentService;
import com.project.agu.steamstalk.Sync.SteamStalkTasks;

public class NotificationUtils {

    private static final int    GAME_TAUNT_NOTIFICATION_ID   = 1010;
    private static final int    GAME_TAUNT_PENDING_INTENT_ID = 1001;
    private static final String GAME_TAUNT_CHANNEL_ID        = "game_taunt_notification_channel";

    private static final int    USER_TAUNT_NOTIFICATION_ID   = 1100;
    private static final int    USER_TAUNT_PENDING_INTENT_ID = 1011;
    private static final String USER_TAUNT_CHANNEL_ID        = "user_taunt_notification_channel";

    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    public static void tauntUserWithRandomGame(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Log.d("debug" , "taunt user");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    GAME_TAUNT_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,GAME_TAUNT_CHANNEL_ID);
        notificationBuilder
                .setColor(ContextCompat.getColor(context, android.support.v4.R.color.notification_icon_bg_color))
                .setSmallIcon(R.drawable.notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Hey stalker!")
                .setContentText("Have you checked this game?")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        "Have you checked this game?"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(gameContentIntent(context))
                .setAutoCancel(true);
        notificationBuilder
                .addAction(showGame(context))
                .addAction(ignoreGame(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(GAME_TAUNT_NOTIFICATION_ID, notificationBuilder.build());

    }

    private static final int    ACTION_SHOW_GAME_PENDING_INTENT_ID         = 1;

    private static Action showGame(Context context)
    {
        Log.d("debug" , "show executed");
        Intent showGameIntent = new Intent(context, SteamStalkIntentService.class);
        showGameIntent.setAction(SteamStalkTasks.ACTION_SHOW_GAME);

        PendingIntent showGamePendingIntent = PendingIntent.getService(
                context,
                ACTION_SHOW_GAME_PENDING_INTENT_ID,
                showGameIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Action showGameAction = new Action(R.drawable.question_mark,
                "Show me the damn game already!",
                showGamePendingIntent);

        return showGameAction;
    }

    private static final int    ACTION_IGNORE_PENDING_INTENT_ID        = 14;

    private static Action ignoreGame(Context context)
    {
        Log.d("debug" , "ignore executed");
        Intent ignoreGameIntent = new Intent(context, SteamStalkIntentService.class);
        ignoreGameIntent.setAction(SteamStalkTasks.ACTION_DISMISS_NOTIFICATION);

        PendingIntent ignoreGamePendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreGameIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Action ignoreGameAction = new Action(R.drawable.ic_cancel_black_24px,
                "No, thanks.",
                ignoreGamePendingIntent);

        return ignoreGameAction;
    }

    public static void tauntUserWithRandomUser(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel mChannel = new NotificationChannel(
                    USER_TAUNT_CHANNEL_ID,
                    "Primary",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,USER_TAUNT_CHANNEL_ID);
        notificationBuilder
                .setColor(ContextCompat.getColor(context, android.support.v4.R.color.notification_icon_bg_color))
                .setSmallIcon(R.drawable.notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Hey stalker!")
                .setContentText("Have you stalked this user yet?")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(
                        "Have you stalked this user yet?"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(userContentIntent(context))
                .setAutoCancel(true);
        notificationBuilder
                .addAction(showUser(context))
                .addAction(ignoreGame(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(USER_TAUNT_NOTIFICATION_ID, notificationBuilder.build());

    }

    private static final int    ACTION_SHOW_USER_PENDING_INTENT_ID         = 2;

    private static Action showUser(Context context)
    {
        Log.d("debug" , "show executed");
        Intent showUserIntent = new Intent(context, SteamStalkIntentService.class);
        showUserIntent.setAction(SteamStalkTasks.ACTION_SHOW_USER);

        PendingIntent showUserPendingIntent = PendingIntent.getService(
                context,
                ACTION_SHOW_USER_PENDING_INTENT_ID,
                showUserIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Action showGameAction = new Action(R.drawable.unknown_person,
                "Show me the user",
                showUserPendingIntent);

        return showGameAction;
    }






    private static PendingIntent gameContentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, Search.class);
        return PendingIntent.getActivity(
                context,
                GAME_TAUNT_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent userContentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, Search.class);
        return PendingIntent.getActivity(
                context,
                USER_TAUNT_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }



    private static Bitmap largeIcon(Context context)
    {
        Resources res       = context.getResources();
        Bitmap    largeIcon = BitmapFactory.decodeResource(res, R.drawable.notification);
        return largeIcon;
    }



}
