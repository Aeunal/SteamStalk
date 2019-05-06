package com.project.agu.steamstalk;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.agu.steamstalk.Data.SteamContract;
import com.project.agu.steamstalk.Services.FetchService;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements NewsAdapter.ListItemClickListener, Observer {

    private final Context context = GameActivity.this;
    private FloatingActionButton fab;
    private ImageView headerImageView;
    private CardView cardView;
    private FrameLayout cardFrame;
    private boolean descOpen;
    private boolean favorited;
    private TextView gameDescription;
    private RecyclerView newsList;

    public NewsAdapter newsAdapter;

    private TextView developer;
    private TextView publisher;
    private TextView genres;
    private TextView price;

    HttpURLConnection urlConnection;
    private String appID;
    String appURL;
    String appName;
    String title;
    boolean executeDone;

    public static final String[] MAIN_GAME_PROJECTION = {
            SteamContract.GameEntry.COLUMN_GAME_PUBLISHERS,
            SteamContract.GameEntry.COLUMN_GAME_DEVELOPERS,
            SteamContract.GameEntry.COLUMN_GAME_FINAL_PRICE,
            SteamContract.GameEntry.COLUMN_GAME_NAME,
            SteamContract.GameEntry.COLUMN_GAME_GENRES,
            SteamContract.GameEntry.COLUMN_GAME_HEADER,
            SteamContract.GameEntry.COLUMN_GAME_DESCRIPTION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout colToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        setSupportActionBar(toolbar);

        headerImageView = (ImageView) findViewById(R.id.headerImage);
        gameDescription = (TextView) findViewById(R.id.gameDescription);
        developer = (TextView) findViewById(R.id.developer);
        publisher = (TextView) findViewById(R.id.publisher);
        genres = (TextView) findViewById(R.id.genres);
        price = (TextView) findViewById(R.id.price);

        /*
        newsList = (RecyclerView) findViewById(R.id.news_list);
        //LinearLayoutManager layoutManagerNews = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManagerNews = new LinearLayoutManager(this);
        newsList.setLayoutManager(layoutManagerNews);
        newsList.setHasFixedSize(true); // false
        newsAdapter = new NewsAdapter(this, this);
        newsList.setAdapter(newsAdapter);
        newsList.setHorizontalScrollBarEnabled(true);
        //*/

        descOpen = false;
        cardView = (CardView) findViewById(R.id.cardGameDesc);
        cardFrame =(FrameLayout) findViewById(R.id.frameLayout);
        cardView.setOnClickListener(
                new CardView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if(descOpen) v.animate().scaleY(-100f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000);
                        //else v.animate().scaleY(100f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(1000);
                        /*ValueAnimator anim = ValueAnimator.ofInt(v.getMeasuredHeight(), -100);
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                int val = (Integer) valueAnimator.getAnimatedValue();
                                ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                                layoutParams.height = val;
                                cardView.setLayoutParams(layoutParams);
                            }
                        });
                        anim.setDuration(2);
                        anim.start();*/
                        ///*
                        FrameLayout.LayoutParams lp;
                        if(descOpen)
                            lp = new FrameLayout.LayoutParams(v.getWidth(), 300);
                        else
                            lp = new FrameLayout.LayoutParams(v.getWidth(), FrameLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(5,5,5,5);

                        v.setLayoutParams(lp);//*/
                        //v.getResources().getDisplayMetrics().density = 1; //  use to scale it
                        descOpen = !descOpen;
                    }
                }
        );


        favorited = false;
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(favorited){
                    Snackbar.make(view, "Removed from Favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    favorited = false;
                    //fab.setImageResource(R.btn_star_big_off);
                } else {
                    Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    favorited = true;
                    //fab.setImageResource(R.drawable.btn_star_big_on);
                }
            }
        });

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            appID = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        //appID = "333600";//"326480";

        appURL = "https://store.steampowered.com/api/appdetails/" +
                "?appids="  + appID;

        loadHeader();
        title = ("Game Loading...");
        titleSet();

        gameDescription = (TextView) findViewById(R.id.gameDescription);

        executeDone = false;
        Log.i("APPID", appID);

        ContentResolver mResolver = context.getContentResolver();
        String[] projectedColumns = {SteamContract.GameEntry.COLUMN_GAME_ID};
        String   selectedString   = SteamContract.GameEntry.COLUMN_GAME_ID + "=?";
        String[] selectionArgs    = {appID};
        Cursor   cursorToCheck;
        long locationId;

        cursorToCheck = mResolver.query(SteamContract.GameEntry.CONTENT_URI,
                MAIN_GAME_PROJECTION,
                        selectedString,
                        selectionArgs,
                null);

        if (cursorToCheck.moveToFirst())
        {

            refreshData(cursorToCheck);
        }
        else
        {
            FetchService.startGameFetching(this, this, appID);
            //FetchGameTask gameTask = new FetchGameTask(this, this);
            //gameTask.execute(appID+"");
        }

    }

    public void titleSet() {
        setTitle(title);
    }

    private void loadHeader() {
        headerImageView = (ImageView) findViewById(R.id.headerImage);
        String internetUrl = "https://steamcdn-a.akamaihd.net/steam/apps/"+appID+"/header.jpg";
        Glide
            .with(context)
            .load(internetUrl)
            .into(headerImageView);
    }

    private void refreshData() {
        Log.i("GameData", "refreshedData");
    }

    public void refreshData(Cursor cursor)
    {

        boolean checkDB = cursor.moveToFirst();

        Log.i("GameData", "refreshing data:"+checkDB);

        if(checkDB) {
               title = cursor.getString(cursor.getColumnIndex("Name"));
           Log.i("APPTITLE", "X:"+title);
           executeDone = true;
           setTitle(title);

           String description = cursor.getString(cursor.getColumnIndex("Description"));

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               gameDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
           } else {
               gameDescription.setText(Html.fromHtml(description));
           }

           developer.setText(cursor.getString(cursor.getColumnIndex("Developer")));
           publisher.setText(cursor.getString(cursor.getColumnIndex("Publisher")));
           genres.setText(cursor.getString(cursor.getColumnIndex("Genres")));
           price.setText(cursor.getString(cursor.getColumnIndex("Price")));

           /*
           title = data.get(FetchGameTask.GAME_NAME);
           Log.i("APPTITLE", "X:"+title);
           executeDone = true;
           setTitle(title);

           String description = data.get(FetchGameTask.GAME_DESCRIPTION);
           Log.i("Descrip", "des:"+description);
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
               gameDescription.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
           } else {
               gameDescription.setText(Html.fromHtml(description));
           }
           developer.setText(data.get(FetchGameTask.GAME_DEVELOPERS));
           publisher.setText(data.get(FetchGameTask.GAME_PUBLISHERS));
           genres.setText(data.get(FetchGameTask.GAME_GENRES));
           price.setText(data.get(FetchGameTask.GAME_FINAL_PRICE));
           */
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
