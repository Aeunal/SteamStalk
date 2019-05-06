package com.project.agu.steamstalk.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class SteamContract {

    public static final String CONTENT_AUTHORITY = "com.project.agu.steamstalk";
    public static final Uri BASE_CONTENT_URI  = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_GAME      = "Games";
    public static final String PATH_USER     = "Users";



    public static final class GameEntry implements BaseColumns
    {
        public static final Uri    CONTENT_URI       = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_GAME)
                .build();
        public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;

        public static final String TABLE_NAME = "Games";
        public static final String COLUMN_GAME_NAME = "Name";
        public static final String COLUMN_GAME_ID = "id";
        public static final String COLUMN_GAME_HEADER = "Header";
        public static final String COLUMN_GAME_DESCRIPTION = "Description";
        public static final String COLUMN_GAME_DEVELOPERS = "Developer";
        public static final String COLUMN_GAME_PUBLISHERS = "Publisher";
        public static final String COLUMN_GAME_GENRES = "Genres";
        public static final String COLUMN_GAME_FINAL_PRICE = "Price";

        public static Uri buildGameUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }



    public static final class UserEntry implements BaseColumns
    {
        public static final Uri    CONTENT_URI       = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_USER)
                .build();
        public static final String CONTENT_TYPE      = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_USER_AVATAR = "AvatarURL";
        public static final String COLUMN_USER_NAME = "Name";
        public static final String COLUMN_USER_ID = "ID";
        public static final String COLUMN_USER_STATUS = "Status";
        public static final String COLUMN_USER_LEVEL = "Level";
        public static final String COLUMN_USER_OWNED = "OwnedGames";
        public static final String COLUMN_USER_RECENT = "RecentGames";
        public static final String COLUMN_USER_YEARS = "Years";


        public static Uri buildUserUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildUserIDWithName(String userID, String name)
        {
            return CONTENT_URI.buildUpon()
                    .appendPath(userID)
                    .appendPath(name)
                    .build();
        }

    }








}
