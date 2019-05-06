package com.project.agu.steamstalk;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<userViewHolder> {

    private int mNumberItems;
    private String[] mValues;
    private String[] mImageRes;
    private Context context;

    public UserAdapter(int numberOfItems, String[] imageRes, String[] values, Context context){
        mNumberItems = numberOfItems;
        mValues = values;
        mImageRes = imageRes;
        this.context = context;
    }

    @Override
    public userViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.user_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        userViewHolder viewHolder = new userViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(userViewHolder holder, int position) {
        holder.bind(mImageRes[position], mValues[position], context);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public void refreshData(String adapterType, Cursor cursor)
    {
        cursor.moveToFirst();
        String[] games = null;
        //Log.i("userREP", data.get(FetchGameTask.USER_OWNED));
        if(adapterType.equals("owned"))
            games = cursor.getString(cursor.getColumnIndex("OwnedGames")).split(";");
        if(adapterType.equals("recent"))
            games = cursor.getString(cursor.getColumnIndex("RecentGames")).split(";");


        mNumberItems = games.length;
        String[] hours = new String[mNumberItems];
        String[] prices = new String[mNumberItems];
        String[] url = new String[mNumberItems];
        for (int i = 0; i < mNumberItems; i++) {
            String[] txt = games[i].split("#");
            int infoCount = txt.length;
            Log.i("GlideBefore",i+" --- "+games[i]);
            hours[i] = Integer.parseInt(txt[0])/60+" Hour / Last 2 Weeks";
            prices[i] = txt[1];
            url[i] = "https://steamdb.info/static/img/default.jpg";
            if(infoCount > 2) {
                url[i] = txt[2];
            }
            Log.i("GlideZZZ", i+" : "+url[i]);
        }
        if(adapterType.equals("owned"))
            mValues = prices;
        if(adapterType.equals("recent"))
            mValues = hours;

        mImageRes = url;
        notifyDataSetChanged();


/*

        String[] games = null;
        //Log.i("userREP", data.get(FetchGameTask.USER_OWNED));
        if(adapterType.equals(FetchGameTask.USER_OWNED))
            games = data.get(FetchGameTask.USER_OWNED).split(";");
        if(adapterType.equals(FetchGameTask.USER_RECENT))
            games = data.get(FetchGameTask.USER_RECENT).split(";");
        mNumberItems = games.length;
        String[] hours = new String[mNumberItems];
        String[] prices = new String[mNumberItems];
        String[] url = new String[mNumberItems];
        for (int i = 0; i < mNumberItems; i++) {
            String[] txt = games[i].split("#");
            int infoCount = txt.length;
            Log.i("GlideBefore",i+" --- "+games[i]);
            hours[i] = Integer.parseInt(txt[0])/60+" Hour / Last 2 Weeks";
            prices[i] = txt[1];
            url[i] = "https://steamdb.info/static/img/default.jpg";
            if(infoCount > 2) {
                url[i] = txt[2];
            }
            Log.i("GlideZZZ", i+" : "+url[i]);
        }
        if(adapterType.equals(FetchGameTask.USER_OWNED))
            mValues = prices;
        if(adapterType.equals(FetchGameTask.USER_RECENT))
            mValues = hours;

        mImageRes = url;
        notifyDataSetChanged();*/
    }

}
