package com.project.agu.steamstalk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.constraint.solver.widgets.ConstraintHorizontalLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.agu.steamstalk.Data.SteamContract;
import com.project.agu.steamstalk.Services.FetchService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class UserActivity extends AppCompatActivity {

    private static final String uShallNotPass = "https://www.wannart.com/wp-content/uploads/2018/02/you-shall-not-pass.jpg";
    private static final String[] images ={ uShallNotPass, uShallNotPass, uShallNotPass};
    private static final String[] values ={"XX$","XX$", "Hi!"};

    public static final String[] MAIN_USER_PROJECTION = {
            SteamContract.UserEntry.COLUMN_USER_AVATAR,
            SteamContract.UserEntry.COLUMN_USER_NAME,
            SteamContract.UserEntry.COLUMN_USER_ID,
            SteamContract.UserEntry.COLUMN_USER_STATUS,
            SteamContract.UserEntry.COLUMN_USER_LEVEL,
            SteamContract.UserEntry.COLUMN_USER_OWNED,
            SteamContract.UserEntry.COLUMN_USER_RECENT,
            SteamContract.UserEntry.COLUMN_USER_YEARS,

    };
    Cursor userCursor;
    private final Context context = UserActivity.this;

    private ImageView pp;
    private TextView nick;
    private TextView level;
    private TextView status;
    private TextView totalHours;
    private TextView totalValue;
    private TextView yearsOnSteam;

    public UserAdapter mAdapterGames;
    public UserAdapter mAdapterAchievements;
    private RecyclerView mGames;
    private RecyclerView mAchievements;

    private FetchGameTask userTask;

    private String steamId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mGames = (RecyclerView) findViewById(R.id.games);
        mAchievements = (RecyclerView) findViewById(R.id.achievements);
        LinearLayoutManager layoutManagergames = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerAchievements = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);

        String gifUrl = "https://i.gifer.com/1amw.gif";

        pp = (ImageView) (findViewById(R.id.pp));
        Glide.with(this).load(gifUrl).into(pp);
        nick = (TextView) (findViewById(R.id.nick));
        status = (TextView) (findViewById(R.id.isOnline));
        level = (TextView) (findViewById(R.id.level));
        totalHours = (TextView) (findViewById(R.id.accHours));
        totalValue = (TextView) (findViewById(R.id.accValue));
        yearsOnSteam = (TextView) (findViewById(R.id.years));

        mGames.setLayoutManager(layoutManagergames);

        mGames.setHasFixedSize(false);
        mAdapterGames = new UserAdapter(images.length, images, values, this);
        mGames.setAdapter(mAdapterGames);
        mGames.setHorizontalScrollBarEnabled(true);

        mAchievements.setLayoutManager(layoutManagerAchievements);
        mAchievements.setHasFixedSize(false);
        mAdapterAchievements = new UserAdapter(images.length, images, values, this);
        mAchievements.setAdapter(mAdapterAchievements);
        mAchievements.setHorizontalScrollBarEnabled(true);

        Intent userIntent = getIntent();
        if (userIntent.hasExtra(Intent.EXTRA_TEXT))
        {
            steamId = userIntent.getStringExtra(
                    Intent.EXTRA_TEXT);
        }


        String   selectedString   = SteamContract.UserEntry.COLUMN_USER_ID + "=?";
        String[] selectionArgs    = {steamId};
        ContentResolver mResolver = context.getContentResolver();

        userCursor = mResolver.query(SteamContract.UserEntry.CONTENT_URI,
                MAIN_USER_PROJECTION,
                selectedString,
                selectionArgs,
                null);


        if(userCursor.moveToFirst())
        {
            if(userCursor.getString(userCursor.getColumnIndex("Level")).equals("")
            ||userCursor.getString(userCursor.getColumnIndex("Level")).length() <= 0)
            {
                refreshData(true,userCursor);
            }
            else
            {
                refreshData(false,userCursor);
                mAdapterGames.refreshData("owned",userCursor);
                mAdapterAchievements.refreshData("recent",userCursor);
            }


        }
        else
        {
            FetchService.startUserFetching(this, this, steamId);
            //userTask = new FetchGameTask(this, this);
            //userTask.execute(steamId);
        }



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(userTask != null)
            userTask.cancel(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(userTask != null)
            userTask.cancel(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(userTask != null)
            userTask.cancel(true);

    }

    public void refreshData(boolean isPrivate, Cursor cursor) {

        cursor.moveToFirst();
        Log.i("Avatar", cursor.getString(cursor.getColumnIndex("AvatarURL")));
        Glide.with(this).load(cursor.getString(cursor.getColumnIndex("AvatarURL"))).into(pp);
        nick.setText(cursor.getString(cursor.getColumnIndex("Name")));
        status.setText("Status: Private");
        if(!isPrivate) {
            level.setText("Level: " + cursor.getString(cursor.getColumnIndex("Level")));

            String statue = "Status: ";
            switch (cursor.getString(cursor.getColumnIndex("Status")))
            {
                case "0":
                    statue += ("Offline");
                    break; // offline or private
                case "1":
                    statue += ("Online");
                    break; // online
                case "2":
                    statue += "Busy";
                    break; // busy
                case "3":
                    statue += "Away";
                    break; // Away
                case "4":
                    statue += "Snooze";
                    break; // Snooze
                case "5":
                    //break; // looking to trade
                case "6":
                    //break; // looking to play
                default:
                    statue += ("Looking around");
                    break;
            }
            status.setText(statue);

            String owned = cursor.getString(cursor.getColumnIndex("OwnedGames"));
            //Log.i("Split",owned);
            String[] ownedGames = owned.split(";");
            int ownedCount = ownedGames.length;
            String[] hours = new String[ownedCount];
            String[] prices = new String[ownedCount];
            String[] url = new String[ownedCount];
            int totalHours = 0;
            double totalMoney = 0;
            for (int i = 0; i < ownedCount; i++) {
                //Log.i("Split", ownedGames[i]);
                String[] txt = ownedGames[i].split("#");
                hours[i] = txt[0];
                totalHours += Integer.parseInt(hours[i]);
                prices[i] = txt[1];
                String money = prices[i];
                if (!(money.equals("Free") || money.equals("Error"))) {
                    //Log.i("Money", money);
                    String commaToP = money.split(",")[0]; //+ "." + money.split(",")[1].substring(0,2);
                    totalMoney += Double.parseDouble(commaToP);
                }
                url[i] = txt[2];

            }
            Double realTotalHours = Math.floor(((double) totalHours / 60) * 100) / 100;
            this.totalHours.setText("Hours Wasted: " + realTotalHours);
            this.totalValue.setText("Total Money: " + totalMoney);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex("Years"))) * 1000);
            //String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(data.get(FetchGameTask.USER_YEARS)));
            this.yearsOnSteam.setText("Years On Steam: " + c.get(Calendar.YEAR));




            /*

        Log.i("Avatar", data.get(FetchGameTask.USER_AVATAR));
        Glide.with(this).load(data.get(FetchGameTask.USER_AVATAR)).into(pp);
        nick.setText(data.get(FetchGameTask.USER_NAME));
        status.setText("Status: Private");
        if(!isPrivate) {
            level.setText("Level: " + data.get(FetchGameTask.USER_LEVEL));

            String statue = "Status: ";
            switch (data.get(FetchGameTask.USER_STATUS))
            {
                case "0":
                    statue += ("Offline");
                    break; // offline or private
                case "1":
                    statue += ("Online");
                    break; // online
                case "2":
                    statue += "Busy";
                    break; // busy
                case "3":
                    statue += "Away";
                    break; // Away
                case "4":
                    statue += "Snooze";
                    break; // Snooze
                case "5":
                    //break; // looking to trade
                case "6":
                    //break; // looking to play
                default:
                    statue += ("Looking around");
                    break;
            }
            status.setText(statue);

            String owned = data.get(FetchGameTask.USER_OWNED);
            //Log.i("Split",owned);
            String[] ownedGames = owned.split(";");
            int ownedCount = ownedGames.length;
            String[] hours = new String[ownedCount];
            String[] prices = new String[ownedCount];
            String[] url = new String[ownedCount];
            int totalHours = 0;
            double totalMoney = 0;
            for (int i = 0; i < ownedCount; i++) {
                //Log.i("Split", ownedGames[i]);
                String[] txt = ownedGames[i].split("#");
                hours[i] = txt[0];
                totalHours += Integer.parseInt(hours[i]);
                prices[i] = txt[1];
                String money = prices[i];
                if (!(money.equals("Free") || money.equals("Error"))) {
                    //Log.i("Money", money);
                    String commaToP = money.split(",")[0]; //+ "." + money.split(",")[1].substring(0,2);
                    totalMoney += Double.parseDouble(commaToP);
                }
                url[i] = txt[2];

            }
            Double realTotalHours = Math.floor(((double) totalHours / 60) * 100) / 100;
            this.totalHours.setText("Hours Wasted: " + realTotalHours);
            this.totalValue.setText("Total Money: " + totalMoney);

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(Long.parseLong(data.get(FetchGameTask.USER_YEARS)) * 1000);
            //String dateString = new SimpleDateFormat("MM/dd/yyyy").format(new Date(data.get(FetchGameTask.USER_YEARS)));
            this.yearsOnSteam.setText("Years On Steam: " + c.get(Calendar.YEAR));
              */
        }
    }

}
