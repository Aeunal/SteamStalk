package com.project.agu.steamstalk.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class SteamDbHelper extends SQLiteOpenHelper {

    private static final int    DATABASE_VERSION = 12;
    public  static final String DATABASE_NAME    = "Steam.db";

    public SteamDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude

        final String SQL_CREATE_GAMES_TABLE = "CREATE TABLE " + SteamContract.GameEntry.TABLE_NAME + " (" +
                SteamContract.GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SteamContract.GameEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, " +
                SteamContract.GameEntry.COLUMN_GAME_ID + " TEXT NOT NULL, " +
                SteamContract.GameEntry.COLUMN_GAME_HEADER + " TEXT  NOT NULL, " +
                SteamContract.GameEntry.COLUMN_GAME_DESCRIPTION + " TEXT NOT NULL, " +
                SteamContract.GameEntry.COLUMN_GAME_GENRES + " TEXT NOT NULL, " +
                SteamContract.GameEntry.COLUMN_GAME_DEVELOPERS + " TEXT NOT NULL, " +
                SteamContract.GameEntry.COLUMN_GAME_PUBLISHERS + " TEXT NOT NULL, " +
                SteamContract.GameEntry.COLUMN_GAME_FINAL_PRICE + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + SteamContract.UserEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                SteamContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with th
                // is weather data
                SteamContract.UserEntry.COLUMN_USER_AVATAR + " TEXT NOT NULL, " +
                SteamContract.UserEntry.COLUMN_USER_NAME + " TEXT NOT NULL, " +
                SteamContract.UserEntry.COLUMN_USER_ID + " TEXT NOT NULL, " +
                SteamContract.UserEntry.COLUMN_USER_STATUS + " TEXT NOT NULL, " +
                SteamContract.UserEntry.COLUMN_USER_LEVEL + " TEXT NOT NULL," +

                SteamContract.UserEntry.COLUMN_USER_OWNED + " TEXT NOT NULL, " +
                SteamContract.UserEntry.COLUMN_USER_RECENT + " TEXT NOT NULL, " +
                SteamContract.UserEntry.COLUMN_USER_YEARS + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_GAMES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SteamContract.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SteamContract.GameEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }















}
