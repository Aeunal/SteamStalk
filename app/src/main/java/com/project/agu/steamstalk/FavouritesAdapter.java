package com.project.agu.steamstalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 *  add to build.gradle
 *     implementation 'com.android.support:recyclerview-v7:28.0.0'
 *     implementation 'com.github.bumptech.glide:glide:4.8.0'
 *     annotationProcessor 'com.github.bumptech.glide:compiler:4.8.0'
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private ArrayList<String> appids;
    private Context context;
    //private ListItemClickListener mOnClickListener;

    public FavouritesAdapter(Context context, ArrayList<String> appids){//, ListItemClickListener listener) {
        this.appids = appids;
        this.context = context;
        //mOnClickListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
        Context        context                         = parent.getContext();
        int            layoutIdForListItem             = R.layout.vertical_list_item;
        LayoutInflater inflater                        = LayoutInflater.from(context);
        boolean        shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);//, mOnClickListener);
        return viewHolder;
        */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_list_item, parent ,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.bind(items[position]);
        //Glide.with(context).asBitmap().load(takeFromHTML("appImage",appids.get(position))).into(holder.img);
        //holder.txt.setText(takeFromHTML("appName",appids.get(position)));
        //holder.price.setText("XX.XX TRY");
        Glide.with(context).asBitmap().load(takeFromHTML("appImage",appids.get(position))).into(holder.img);
        holder.txt.setText(appids.get(position));
        holder.price.setText("XX.XX TRY");
    }

    String takeFromHTML(String lookFor, String input) {
        switch(lookFor) {
            case "appName":
                return templateHTML(
                        "https://store.steampowered.com/app/" + input,
                        "apphub_AppName", "", "</div>");
            case "appImage":
            default:
                return "https://steamcdn-a.akamaihd.net/steam/apps/"+input+"/header.jpg";
        }
    }

    public String templateHTML(String URL, String key, String beginIndex, String endIndex) {
        String ret = null;
        try {
            java.net.URL url = new URL(URL);
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
    public int getItemCount() {
        return appids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {// implements View.OnClickListener {
        //private  ListItemClickListener mListener;
        public TextView txt;
        public ImageView img;
        public TextView price;

        public ViewHolder(View itemView){//, ListItemClickListener listener) {
            super(itemView);
            txt = itemView.findViewById(R.id.appName);
            img = itemView.findViewById(R.id.appImage);
            price = itemView.findViewById(R.id.price);
            //mListener            = listener;
            //itemView.setOnClickListener(this);
        }
        //void bind(String s) { listItemForecastView.setText(s); }
        //@Override
        //public void onClick(View view) { int clickedPosition = getAdapterPosition();
        //mListener.onListItemClick(clickedPosition);}
    }


}