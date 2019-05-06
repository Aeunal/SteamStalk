package com.project.agu.steamstalk;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.project.agu.steamstalk.Data.SteamContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchGameTask extends AsyncTask<String, Void, Map<String, String>> {

    Cursor gameCursor;
    Cursor userCursor;
    public static final String[] MAIN_GAME_PROJECTION = {
            SteamContract.GameEntry.COLUMN_GAME_PUBLISHERS,
            SteamContract.GameEntry.COLUMN_GAME_DEVELOPERS,
            SteamContract.GameEntry.COLUMN_GAME_FINAL_PRICE,
            SteamContract.GameEntry.COLUMN_GAME_NAME,
            SteamContract.GameEntry.COLUMN_GAME_GENRES,
            SteamContract.GameEntry.COLUMN_GAME_HEADER,
            SteamContract.GameEntry.COLUMN_GAME_DESCRIPTION,
    };

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

    public static final String[] MAIN_PRIVATE_USER_PROJECTION = {
            SteamContract.UserEntry.COLUMN_USER_AVATAR,
            SteamContract.UserEntry.COLUMN_USER_NAME,
    };

    final static String NEWS_TITLE = "title";
    final static String NEWS_CONTENT = "contents";
    final static String NEWS_LABEL = "feedlabel";
    final static String NEWS_DATE = "date";

    final static String GAME_DATA = "data";
    final static String GAME_NAME = "name";
    final static String GAME_DESCRIPTION = "detailed_description";
    final static String GAME_HEADER = "header_image";
    final static String GAME_DEVELOPERS = "developers";
    final static String GAME_PUBLISHERS = "publishers";
    final static String GAME_PRICE = "price_overview";
    final static String GAME_FINAL_PRICE = "final_formatted";
    final static String GAME_GENRES = "genres";
    final static String GAME_CONTENT = "content_descriptors";
    final static String GAME_NOTES = "notes";

    final static String USER_NAME = "name";
    final static String USER_AVATAR = "avatar";
    final static String USER_STATUS = "status";
    final static String USER_HOURS = "hours";
    final static String USER_MONEY = "money";
    final static String USER_YEARS = "years";
    final static String USER_GAME_NAME = "gameName";
    final static String USER_GAME_PRICE = "gamePrice";
    final static String USER_GAME_IMAGE = "gameImage";
    final static String USER_GAME_HOURS = "gameHours";
    final static String USER_SUMMARY = "user";
    final static String USER_LEVEL = "level";
    final static String USER_OWNED = "owned";
    final static String USER_RECENT = "recent";

    private final String KEY = "D18A2EC7DA3B1EF77388D67C3CAFFD99";
    private final int GAME_STATE = 0;
    private final int USER_STATE = 1;
    private final int FETCH_STATE = 2;
    private final int HISTORY_STATE = 3;
    private int state;
    ContentResolver mResolver;
    Map<String, String> result;
    GameActivity gameActivity;
    UserActivity userActivity;
    HistoryAdapter historyAdapter;
    Search search;

    private int recentGameLimit;
    private int newsLimit;

    private String appid; // take from doInBg
    private boolean success = true;
    private boolean isPrivate = false;
    private Context context;

    public FetchGameTask(GameActivity gameActivity, Context context) {
        this.gameActivity = gameActivity;
        this.context = context;
        state = GAME_STATE;
        mResolver       = context.getContentResolver();
    }

    public FetchGameTask(UserActivity userActivity, Context context) {
        this.userActivity = userActivity;
        this.context = context;
        state = USER_STATE;
        mResolver       = context.getContentResolver();
    }

    public FetchGameTask(Search search, Context context) {
        this.search = search;
        this.context = context;
        state = FETCH_STATE;
        mResolver       = context.getContentResolver();
    }

    public FetchGameTask(HistoryAdapter history, Context context) {
        this.historyAdapter = history;
        this.context = context;
        state = HISTORY_STATE;
        mResolver       = context.getContentResolver();
    }

    public void addGame(String gameID, String name,String header, String description,String developers,String publishers, String genres,String price)
    {
        /*
        String[] projectedColumns = {SteamContract.GameEntry._ID};
        String   selectedString   = SteamContract.GameEntry.COLUMN_GAME_ID + "=?";
        String[] selectionArgs    = {gameID};
        Cursor gameCursor;
        long gameId;

        gameCursor = mResolver.query(SteamContract.GameEntry.CONTENT_URI,
                projectedColumns,
                selectedString,
                selectionArgs,
                null);
        */


            Uri insertedUri;
            ContentValues gameValues = new ContentValues();

            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_NAME, name);
            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_ID, gameID);
            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_HEADER, header);
            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_DESCRIPTION, description);
            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_DEVELOPERS, developers);
            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_PUBLISHERS, publishers);
            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_GENRES, genres);
            gameValues.put(SteamContract.GameEntry.COLUMN_GAME_FINAL_PRICE, price);

            insertedUri = mResolver.insert(SteamContract.GameEntry.CONTENT_URI, gameValues);





    }

    public void addUser(String userID, String avatar,String name, String status,String level,String ownedGames,String recentGames, String years)
    {/*
        String[] projectedColumns = {SteamContract.UserEntry._ID};
        String   selectedString   = SteamContract.UserEntry.COLUMN_USER_ID + "=?";
        String[] selectionArgs    = {userID};
        Cursor gameCursor;
        long userId;

        gameCursor = mResolver.query(SteamContract.UserEntry.CONTENT_URI,
                projectedColumns,
                selectedString,
                selectionArgs,
                null);


        if (gameCursor.moveToFirst())
        {
            int userIdIndex = gameCursor.getColumnIndex(SteamContract.UserEntry._ID);
            userId          = gameCursor.getLong(userIdIndex);
        }

*/

            Uri insertedUri;
            ContentValues userValues = new ContentValues();

            userValues.put(SteamContract.UserEntry.COLUMN_USER_NAME, name);
            userValues.put(SteamContract.UserEntry.COLUMN_USER_ID, userID);
            userValues.put(SteamContract.UserEntry.COLUMN_USER_AVATAR, avatar);
            userValues.put(SteamContract.UserEntry.COLUMN_USER_LEVEL, level);
            userValues.put(SteamContract.UserEntry.COLUMN_USER_OWNED, ownedGames);
            userValues.put(SteamContract.UserEntry.COLUMN_USER_RECENT, recentGames);
            userValues.put(SteamContract.UserEntry.COLUMN_USER_STATUS, status);
            userValues.put(SteamContract.UserEntry.COLUMN_USER_YEARS, years);
            insertedUri = mResolver.insert(SteamContract.UserEntry.CONTENT_URI, userValues);

    }





    @Override
    protected Map<String, String> doInBackground(String... urlStrings) {
        result = new HashMap<>();

        OkHttpClient client = new OkHttpClient();
        try {

            if (state == USER_STATE) {

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            if(recentGameLimit<16)
            {
                recentGameLimit = Integer.parseInt(prefs.getString("gameCount", ""));
            }
            else{
                recentGameLimit=15;
            }


                Response getUserSummaries = null;   // community-name-persona-url-timeCreated
                Response getBadges = null;          // level
                Response getOwnedGames = null;      // appids
                Response getRecentGames = null;     // appids
                Response getOwnedAppDetail = null;  // trouble

                Request userSummaries = new Request.Builder()
                        .url("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + KEY + "&steamids=" + urlStrings[0])
                        .get().build();
                getUserSummaries = client.newCall(userSummaries).execute();
                String json1 = getUserSummaries.body().string();
                getUserSummaries.close();
                String userSummariesReturn = fetchUserSummaries(json1);
                result.put(USER_SUMMARY, userSummariesReturn);
                Log.i("API_User", userSummariesReturn);

                if(!isPrivate) {
                    Request badges = new Request.Builder()
                            .url("https://api.steampowered.com/IPlayerService/GetBadges/v1?key=" + KEY + "&steamid=" + urlStrings[0])
                            .get().build();
                    getBadges = client.newCall(badges).execute();
                    String json2 = getBadges.body().string();
                    getBadges.close();
                    String badgesReturn = fetchBadges(json2);

                    Request ownedGames = new Request.Builder()
                            .url("http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=" + KEY + "&steamid=" + urlStrings[0]+"&include_played_free_games=-1&include_appinfo=1")
                            .get().build();
                    getOwnedGames = client.newCall(ownedGames).execute();
                    String json3 = getOwnedGames.body().string();
                    getOwnedGames.close();
                    String ownedGamesReturn = fetchOwnedGames(json3);

                    Request recentGames = new Request.Builder()
                            .url("http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v0001/?key=" + KEY + "&steamid=" + urlStrings[0]+"&count="+recentGameLimit)
                            .get().build();
                    getRecentGames = client.newCall(recentGames).execute();
                    String json4 = getRecentGames.body().string();
                    getRecentGames.close();
                    String recentGameReturn = fetchRecentGames(json4);
                    Log.i("API_Level", badgesReturn);
                    Log.i("API_Games", ownedGamesReturn);
                    Log.i("API_Recent", recentGameReturn);
                    result.put(USER_LEVEL, badgesReturn);
                    result.put(USER_OWNED, ownedGamesReturn);
                    result.put(USER_RECENT, recentGameReturn);
                    addUser(urlStrings[0],
                            result.get(USER_AVATAR),
                            result.get(USER_NAME),
                            result.get(USER_STATUS),
                            result.get(USER_LEVEL),
                            result.get(USER_OWNED),
                            result.get(USER_RECENT),
                            result.get(USER_YEARS));

                    //addUser(urlStrings[0],USER_AVATAR,USER_NAME,null,null,null,null,null);

                    String   selectedString   = SteamContract.UserEntry.COLUMN_USER_ID + "=?";
                    String[] selectionArgs    = {urlStrings[0]};

                    userCursor = mResolver.query (
                            SteamContract.UserEntry.CONTENT_URI,
                            MAIN_USER_PROJECTION,
                            selectedString,
                            selectionArgs,
                            null
                    );


                } else {
                    //String defaultImageURL = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/fe/fef49e7fa7e1997310d705b2a6158ff8dc1cdfeb_full.jpg";

                    addUser(urlStrings[0],result.get(USER_AVATAR),result.get(USER_NAME),"","","","","");

                    String   selectedString   = SteamContract.UserEntry.COLUMN_USER_ID + "=?";
                    String[] selectionArgs    = {urlStrings[0]};

                    userCursor = mResolver.query (
                            SteamContract.UserEntry.CONTENT_URI,
                            MAIN_PRIVATE_USER_PROJECTION,
                            selectedString,
                            selectionArgs,
                            null
                    );
                }
                //Log.i("userRePro", badgesReturn.length()+" "+ownedGamesReturn.length()+" "+ recentGameReturn.length()+" "+userSummariesReturn.length());

            } else if (state == GAME_STATE) {

                //TODO
                newsLimit = -1;

                appid = urlStrings[0];
                Request gameData = new Request.Builder()
                        .url("https://store.steampowered.com/api/appdetails/?appids=" + appid)
                        .get().build();
                Response getGameData = client.newCall(gameData).execute();
                String json = getGameData.body().string();
                getGameDataFromJson(json);
                Request request = new Request.Builder()
                        .url("https://api.steampowered.com/ISteamNews/GetNewsForApp/v2?appid="+appid+"&maxlength=0&enddate=-1&count=20")
                        .get().build();
                Response response = client.newCall(request).execute();
                String jsonNews = response.body().string();
                fetchNewsFromJson(jsonNews);
                Log.i("ApiCall", "Details: "+json);
                Log.i("ApiCall", "News: "+jsonNews);
                return result;
                //return getGameDataFromJson(json);
            } else if(state == FETCH_STATE) {

                /*
                Response getUserSummaries = null;
                Request userSummaries = new Request.Builder()
                        .url("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + KEY + "&steamids=" + urlStrings[0])
                        .get().build();
                getUserSummaries = client.newCall(userSummaries).execute();
                String json1 = getUserSummaries.body().string();
                getUserSummaries.close();
                String userSummariesReturn = fetchUserSummaries(json1);
                result.put(USER_SUMMARY, userSummariesReturn);
                Log.i("API_User", userSummariesReturn);

                return result;*/
            } else if(state == HISTORY_STATE) {
                String urls = "";
                for(int i = 0; i < urlStrings.length; i++) {
                    Response getUserSummaries = null;
                    Request userSummaries = new Request.Builder()
                            .url("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + KEY + "&steamids=" + urlStrings[i])
                            .get().build();
                    getUserSummaries = client.newCall(userSummaries).execute();
                    String json = getUserSummaries.body().string();
                    getUserSummaries.close();
                    fetchUserInfo(json);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(Map<String, String> gameData)
    {
        super.onPostExecute(gameData);
        Log.i("onPostExecute", "DONE");
        if(state == FETCH_STATE) search.syncAvatars(gameData);
        if (state == GAME_STATE) gameActivity.refreshData(gameCursor);
        //if (state == GAME_STATE) gameActivity.newsAdapter.refreshData(gameData);
        if(state == HISTORY_STATE) historyAdapter.refreshImages(gameData);

        Log.i("isPrivate", isPrivate+"");
        if(!isPrivate)
        {
            if (state == USER_STATE) userActivity.mAdapterGames.refreshData("owned", userCursor);
            if (state == USER_STATE) userActivity.mAdapterAchievements.refreshData("recent", userCursor);
        }
        if (state == USER_STATE) userActivity.refreshData(isPrivate, userCursor);
        state = -1;
    }

    /// FETCH METHODS


    private Map<String, String> fetchNewsFromJson(String string) throws JSONException
    {
        JSONObject response = new JSONObject(string).getJSONObject("appnews");
        JSONArray games = response.getJSONArray("newsitems");
        int entries = games.length();
        String titles = "", contents = "", labels = "", dates = "";
        for (int i = 0; i < entries; i++) {
            JSONObject game = games.getJSONObject(i);
            String title = game.getString(NEWS_TITLE);
            String content = game.getString(NEWS_CONTENT);
            String label = game.getString(NEWS_LABEL);
            String date = game.getString(NEWS_DATE);
            titles += title + (i<entries-1?"₺":"");
            contents += content + (i<entries-1?"₺":"");
            labels += label + (i<entries-1?"₺":"");
            dates += date + (i<entries-1?"₺":"");
        }
        Log.i("News", "T "+titles);
        Log.i("News", "C "+contents);
        Log.i("News", "L "+labels);
        Log.i("News", "D "+dates);
        result.put(NEWS_TITLE, titles);
        result.put(NEWS_CONTENT, contents);
        result.put(NEWS_LABEL, labels);
        result.put(NEWS_DATE, dates);
        return result;
    }

    private Map<String, String> getGameDataFromJson(String gameJsonStr)
            throws JSONException {
        final String GAME_ID = appid;
        final int CENT_TO_DOLLAR = 100;
        JSONObject gameJson = new JSONObject(gameJsonStr).getJSONObject(GAME_ID);
        JSONObject gameData = gameJson.getJSONObject(GAME_DATA);
        success = gameJson.getBoolean("success");
        if (success) {
            result.put(GAME_NAME, gameData.getString(GAME_NAME));
            result.put(GAME_HEADER, gameData.getString(GAME_HEADER));
            result.put(GAME_DEVELOPERS, gameData.getJSONArray(GAME_DEVELOPERS).getString(0));
            result.put(GAME_PUBLISHERS, gameData.getJSONArray(GAME_PUBLISHERS).getString(0));
            result.put(GAME_DESCRIPTION, formatDesc(gameData.getString(GAME_DESCRIPTION)));
            result.put(GAME_NOTES, gameData.getJSONObject(GAME_CONTENT).getString(GAME_NOTES));
            JSONObject priceOverview = gameData.getJSONObject(GAME_PRICE);
            int price = priceOverview.getInt("initial") / CENT_TO_DOLLAR;
            int discount = priceOverview.getInt("discount_percent");
            result.put(GAME_PRICE, price + "");
            result.put("discount_percent", discount + "");
            String currency = priceOverview.getString("currency");
            result.put("currency", currency);
            String finalPrice = priceOverview.getString(GAME_FINAL_PRICE);
            result.put(GAME_FINAL_PRICE, finalPrice);
            JSONArray genres = gameData.getJSONArray(GAME_GENRES);
            String genreArray = "";
            for (int i = 0; i < genres.length(); i++)
                genreArray += genres.getJSONObject(i).getString("description") + (i < genres.length() - 1 ? ", " : "");
            result.put(GAME_GENRES, genreArray);

            addGame(appid, gameData.getString(GAME_NAME),gameData.getString(GAME_HEADER), formatDesc(gameData.getString(GAME_DESCRIPTION))
                    ,gameData.getJSONArray(GAME_DEVELOPERS).getString(0),gameData.getJSONArray(GAME_PUBLISHERS).getString(0),genreArray, finalPrice);


            String   selectedString   = SteamContract.GameEntry.COLUMN_GAME_ID + "=?";
            String[] selectionArgs    = {appid};

            gameCursor = mResolver.query(SteamContract.GameEntry.CONTENT_URI,
                    MAIN_GAME_PROJECTION,
                    selectedString,
                    selectionArgs,
                    null);



            return result;
        } else return result;
    }

    private String formatDesc(String rawDesc) {
        //Document desc = Jsoup.parse(rawDesc);
        String lineBreak = "<br \\/>";
        rawDesc.indexOf(lineBreak);
        return rawDesc;
    }

    private String getReadableDateString(long time) {
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }

    private String fetchRecentGames(String jsonString) throws JSONException {
        if (isPrivate)
            return null;
        String result = "";
        JSONObject firstObject = new JSONObject(jsonString);
        JSONObject secondObject = firstObject.getJSONObject("response");
        int gameCount = Math.min(secondObject.getInt("total_count"),10);
        JSONArray games = secondObject.getJSONArray("games");
        gameCount = games.length();
        for (int i = 0; i < gameCount; i++) {
            JSONObject gameObject = games.getJSONObject(i);
            int appid = gameObject.getInt("appid");
            int playtime = gameObject.getInt("playtime_2weeks");
            String info = playtime + "#" + fetchAppDetails(appid)+(i<gameCount-1?";":"");
            //Log.i("jaxa", i+" : "+info);
            result += info;
        }
        return result;
    }

    private String fetchOwnedGames(String jsonString) throws JSONException {
        if (isPrivate)
            return null;
        String result = "";
        JSONObject firstObject = new JSONObject(jsonString);
        JSONObject secondObject = firstObject.getJSONObject("response");
        int gameCount = Math.min(secondObject.getInt("game_count"), 25);
        JSONArray games = secondObject.getJSONArray("games");

        for (int i = 0; i < gameCount; i++) {
            JSONObject gameObject = games.getJSONObject(i);
            int appid = gameObject.getInt("appid");
            int playtime = gameObject.getInt("playtime_forever");
            String info = playtime + "#" + fetchAppDetails(appid)+(i<gameCount-1?";":"");
            //Log.i("jaxa", i+" : "+info);
            result += info;
        }
        return result;
    }

    private String fetchBadges(String jsonString) throws JSONException {
        if (isPrivate) return null;
        int level = 0;
        JSONObject firstObject = new JSONObject(jsonString);
        JSONObject secondObject = firstObject.getJSONObject("response");
        level = secondObject.getInt("player_level");
        //result.put(USER_LEVEL, level+"");
        return level + "";
    }

    private String fetchUserSummaries(String jsonString) throws JSONException {
        String nick = "";
        String avatarURL = "";
        int personastate = 0;
        long timecreated = 0;
        int communityvisiblestate = 0;

        JSONObject firstObject = new JSONObject(jsonString);
        JSONObject secondObject = firstObject.getJSONObject("response");

        JSONObject info = secondObject.getJSONArray("players").getJSONObject(0);

        communityvisiblestate = info.getInt("communityvisibilitystate");
        Log.i("isPPP","s: "+communityvisiblestate);
        if (communityvisiblestate != 3) {
            isPrivate = true;
            nick = info.getString("personaname");
            result.put(USER_NAME, nick);
            //avatarURL = info.getString("profileurl");
            avatarURL = info.getString("avatarfull");
            result.put(USER_AVATAR, avatarURL);
            return nick + "-" + avatarURL;
        }
        nick = info.getString("personaname");
        //avatarURL = info.getString("profileurl");
        avatarURL = info.getString("avatarfull");
        personastate = info.getInt("personastate");
        timecreated = info.getLong("timecreated");
        result.put(USER_NAME, nick);
        result.put(USER_AVATAR, avatarURL);
        result.put(USER_STATUS, personastate+"");
        result.put(USER_YEARS, timecreated+"");
        String userStatus = convertUserStatus(USER_STATUS);


        return nick + "-" + avatarURL + "-" + personastate + "-" + timecreated;
    }

    private String fetchUserInfo(String jsonString) throws JSONException {
        String nick = "";
        String avatarURL = "";
        int personastate = 0;
        int communityvisiblestate = 0;

        JSONObject firstObject = new JSONObject(jsonString);
        JSONObject secondObject = firstObject.getJSONObject("response");

        JSONObject info = secondObject.getJSONArray("players").getJSONObject(0);

        communityvisiblestate = info.getInt("communityvisibilitystate");
        Log.i("isPPP","s: "+communityvisiblestate);
        if (communityvisiblestate != 3) {
            isPrivate = true;
            nick = info.getString("personaname");
            if(result.get(USER_NAME) != null)
                result.put(USER_NAME, result.get(USER_NAME)+nick+"₺");
            else
                result.put(USER_NAME, nick+"₺");
            //avatarURL = info.getString("profileurl");
            avatarURL = info.getString("avatarfull");
            if(result.get(USER_AVATAR) != null)
                result.put(USER_AVATAR, result.get(USER_AVATAR)+avatarURL+"₺");
            else
                result.put(USER_AVATAR, avatarURL+"₺");
            if(result.get(USER_STATUS) != null)
                result.put(USER_STATUS, result.get(USER_STATUS)+"4"+"₺");
            else
                result.put(USER_STATUS, "4"+"₺");
            return nick + "-" + avatarURL;
        }
        nick = info.getString("personaname");
        //avatarURL = info.getString("profileurl");
        avatarURL = info.getString("avatarfull");
        personastate = info.getInt("personastate");

        if(result.get(USER_NAME) != null)
            result.put(USER_NAME, result.get(USER_NAME)+nick+"₺");
        else
            result.put(USER_NAME, nick+"₺");

        if(result.get(USER_AVATAR) != null)
            result.put(USER_AVATAR, result.get(USER_AVATAR)+avatarURL+"₺");
        else
            result.put(USER_AVATAR, avatarURL+"₺");

        if(result.get(USER_STATUS) != null)
            result.put(USER_STATUS, result.get(USER_STATUS)+personastate+"₺");
        else
            result.put(USER_STATUS, personastate+"₺");
        String userStatus = convertUserStatus(personastate+"");
        return nick + "-" + avatarURL + "-" + personastate;
    }

    private String fetchAppDetails(int appid) throws JSONException {
        Response getAppDetails = null;
        OkHttpClient client = new OkHttpClient();
        Request appDetails = new Request.Builder()
                .url("https://store.steampowered.com/api/appdetails/?appids=" + appid)
                .get().build();
        try {
            getAppDetails = client.newCall(appDetails).execute();
            String json = getAppDetails.body().string();
            getAppDetails.close();
            //Log.i("jaxaRes", json);
            JSONObject firstObject = new JSONObject(json);
            JSONObject secondObject = firstObject.getJSONObject(appid + "");
            if(!secondObject.getBoolean("success")) {
                return "Error" + "#" + "https://steamdb.info/static/img/default.jpg";
            }
            JSONObject info = secondObject.getJSONObject("data");
            String name = info.getString("name");
            int appId = info.getInt("steam_appid");
            Log.i("API_SearchForGame", appId+ " : "+name);
            String price = "Free";
            if(info.has("price_overview")) {
                JSONObject priceObject = info.getJSONObject("price_overview");
                price = priceObject.getString("final_formatted");
            }
            String imageURL = info.getString("header_image");
            return price + "#" + imageURL;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertUserStatus(String status)
    {
        String statue = "";
        switch (status)
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
        return statue;
    }


}
