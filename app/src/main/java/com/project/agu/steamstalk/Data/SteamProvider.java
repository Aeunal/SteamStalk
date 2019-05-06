package com.project.agu.steamstalk.Data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;





public class SteamProvider extends ContentProvider {

    public static final int GAME = 100;
    public static final int USER = 101;
    public static final int USER_WITH_ID_AND_NAME = 102;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private              SteamDbHelper mOpenHelper;

    public SteamProvider() {
    }


    public static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = SteamContract.CONTENT_AUTHORITY;

        matcher.addURI(authority,SteamContract.PATH_GAME, GAME);
        matcher.addURI(authority,SteamContract.PATH_USER, USER);
        matcher.addURI(authority,SteamContract.PATH_USER + "/*/#", USER_WITH_ID_AND_NAME);
        return matcher;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri)
    {
        final int match = sUriMatcher.match(uri);

        switch (match)
        {
            case GAME: return SteamContract.GameEntry.CONTENT_TYPE;
            case USER : return SteamContract.UserEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch(match){
            case GAME: {
                long _id = db.insert(SteamContract.GameEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = SteamContract.GameEntry.buildGameUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;

            }
            case USER: {
                long _id = db.insert(SteamContract.UserEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = SteamContract.UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into" + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SteamDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder)
    {

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch(match){
            case GAME: {
                retCursor = db.query(SteamContract.GameEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            } break;
            case USER: {
                retCursor = db.query(SteamContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            } break;

            case USER_WITH_ID_AND_NAME:{

                String userID = uri.getPathSegments().get(1);
                String name         = uri.getPathSegments().get(2);

                retCursor = db.query(SteamContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

            } break;

            default: {
                throw new UnsupportedOperationException("Unknown uri:" + uri);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;


    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
