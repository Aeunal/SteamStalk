package com.project.agu.steamstalk;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.ToggleButton;

import com.project.agu.steamstalk.Data.SteamContract;
import com.project.agu.steamstalk.Services.FetchService;
import com.project.agu.steamstalk.Utilities.NotificationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class Search extends AppCompatActivity implements  HistoryAdapter.ListItemClickListener {

    ArrayList<String> playerIdHistory = new ArrayList<>();
    ArrayList<String> playerAvatarHistory = new ArrayList<>();
    ArrayList<String> appIdHistory = new ArrayList<String>();
    ArrayList<String> favouriteId = new ArrayList<String>();
    private boolean appSearch;

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
        setContentView(R.layout.activity_search);
        //getActionBar().hide();

        /*boolean edited = false;
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra("isEdited")) {
            edited = intentThatStartedThisActivity.getBooleanExtra("isEdited");
        }

        if(edited) {

        }*/

        playerIdHistory.add("76561197972710279"); //Wayfaerer
        playerIdHistory.add("76561198223105864"); //Fastlock
        playerIdHistory.add("76561198096674104"); //Tayami
        playerIdHistory.add("76561198312476051"); //Trixma
        playerIdHistory.add("76561198097355936"); //Germakoci
        playerIdHistory.add("76561198198663550"); //ErdalBaggal
        playerIdHistory.add("76561197960287930"); //GABEN

        appIdHistory.add("221910"); //TheStanleyParable
        appIdHistory.add("20920"); //TheWitcher2
        appIdHistory.add("4700"); //Medieval2
        appIdHistory.add("48700"); //MountBlade
        //appIdHistory.add("730"); //CounterStrike
        appIdHistory.add("359550"); //RainbowSix

        favouriteId.add("485510"); //Nioh
        favouriteId.add("221380"); //AgeOfEmpires2
        favouriteId.add("242760"); //TheForest
        favouriteId.add("236850"); //EuropaUniversalisIV
        favouriteId.add("374320"); //DarkSoulsIII
        favouriteId.add("48700"); //MountBlade

        initHistoryRecyclerView(true);
        initHistoryRecyclerView(false);
        initFavouriteRecyclerView();

        final SearchView search = (SearchView) findViewById(R.id.searchView);
        //search.add

        ToggleButton searchMode = (ToggleButton) findViewById(R.id.searchMode);
        //searchMode.getText();
        searchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) appSearch = true;
                else appSearch = false;
            }
        });

        search.setOnClickListener(
                new SearchView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search.setIconified(false);
                        //InputMethodManager im = ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE));
                        //im.showSoftInput(search, 0);
                    }
                }
        );

        search.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(appSearch){
                            if(!contains(appIdHistory, query)) appIdHistory.add(query);
                            Intent GameActivityIntent = new Intent(Search.this, GameActivity.class);
                            GameActivityIntent.putExtra(Intent.EXTRA_TEXT, query);
                            startActivity(GameActivityIntent);
                        } else {
                            if(!contains(playerIdHistory, query)) playerIdHistory.add(query);
                            Intent UserActivityIntent = new Intent(Search.this, UserActivity.class);
                            UserActivityIntent.putExtra(Intent.EXTRA_TEXT, query);
                            startActivity(UserActivityIntent);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        //FetchGameTask apiTask = new FetchGameTask(this, this);
        //apiTask.execute();

    }
/*
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        return new CursorLoader(this,
                SteamContract.GameEntry.CONTENT_URI,
                MAIN_GAME_PROJECTION,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }*/

    public void syncAvatars(Map<String, String> data) {

    }

    private boolean contains(ArrayList<String> list, String e) {
        boolean contains = false;
        for (String item: list) {
            if(item.equals(e)) contains = true;
        }
        return contains;
    }

    private void initFavouriteRecyclerView() {
        RecyclerView favouritesList = findViewById(R.id.favourites);
        FavouritesAdapter adapter = new FavouritesAdapter(this,favouriteId);
        favouritesList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL); //  LinearLayoutManager.VERTICAL);
        favouritesList.setLayoutManager(layoutManager);
        /*
        favouritesList.setHasFixedSize(false);
        DividerItemDecoration itemDecoration;
        itemDecoration = new DividerItemDecoration(favouritesList.getContext(), layoutManager.getOrientation());
        favouritesList.addItemDecoration(itemDecoration);
        */
    }

    private void initHistoryRecyclerView(boolean app) {
        RecyclerView historyList;
        HistoryAdapter adapter;
        if(app) {
            historyList = findViewById(R.id.gameHistory);
            adapter = new HistoryAdapter(this,appIdHistory, app, this);
        } else {
            historyList = findViewById(R.id.playerHistory);
            adapter = new HistoryAdapter(this,playerIdHistory, app, this);
        }
        historyList.setAdapter(adapter);
        historyList.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        historyList.setLayoutManager(layoutManager);
    }

    String takeFromHTML(String lookFor, String input) {
        switch(lookFor) {
            case "userName":
                return templateHTML(
                        "https://steamcommunity.com/profiles/" + input,
                        "actual_persona_name", "","</span>");
            case "userImage":
                return templateHTML(
                        "https://steamcommunity.com/profiles/" + input,
                        "playerAvatarAutoSizeInner", "=\"", "\"></div>");
            case "appName":
                return templateHTML(
                        "https://store.steampowered.com/app/" + input,
                        "apphub_AppName", "", "</div>");
            case "appImage":
                return "https://steamcdn-a.akamaihd.net/steam/apps/"+input+"/header.jpg";
            case "ID":
            default:
                return templateHTML(
                        "https://steamid64.net/lookup/" + input,
                        "value", "", "'></div>");
        }
    }

    public String templateHTML(String URL, String key, String beginIndex, String endIndex) {
        String ret = null;
        try {
            URL url = new URL(URL);
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String html = "";
            String line = null;
            while ((line = br.readLine()) != null)
                html+=line;
            String[] results = html.split(key);
            int indexBegin = results[1].indexOf(beginIndex);
            int indexEnd = results[1].indexOf(endIndex);
            ret = results[1].substring(indexBegin+2, indexEnd);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.err.println("Error");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error");
        }
        return ret;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //MenuItem searchItem = menu.findItem(R.id.searchView);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_profile) {
            Intent GameActivityIntent = new Intent(Search.this, UserActivity.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String steamid = prefs.getString("profile", "");
            GameActivityIntent.putExtra(Intent.EXTRA_TEXT, steamid);
            startActivity(GameActivityIntent);
            return true;
        }

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(Search.this, SettingsActivity.class);
            settingsIntent.putExtra(Intent.EXTRA_TEXT, favouriteId.size()+"-"+playerIdHistory.size()+"-"+appIdHistory.size());
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex, boolean app) {
        if(app){
            Intent GameActivityIntent = new Intent(Search.this, GameActivity.class);
            GameActivityIntent.putExtra(Intent.EXTRA_TEXT, appIdHistory.get(clickedItemIndex));
            startActivity(GameActivityIntent);
        } else {
            Intent UserActivityIntent = new Intent(Search.this, UserActivity.class);
            UserActivityIntent.putExtra(Intent.EXTRA_TEXT, playerIdHistory.get(clickedItemIndex));
            startActivity(UserActivityIntent);
        }
    }
    public void testNotification(View view)
    {
        NotificationUtils.tauntUserWithRandomGame(this);
    }

    public void testNotification2(View view)
    {
        NotificationUtils.tauntUserWithRandomUser(this);
    }
}
