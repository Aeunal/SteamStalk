package com.project.agu.steamstalk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.agu.steamstalk.Services.FetchService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<String> id;
    private String[] imageUrls;
    private String[] userNicks;
    private String[] userStatuses;
    private Context context;
    private boolean app;
    private ListItemClickListener mOnClickListener;
    private Map<String, String> data;
    private boolean dataSynchronized;

    public HistoryAdapter(Context context, ArrayList<String> id, boolean app, ListItemClickListener listener) {
        this.imageUrls = new String[0];
        this.userNicks = new String[0];
        this.userStatuses = new String[0];
        this.id = id;
        this.context = context;
        this.app = app;
        this.dataSynchronized = false;
        mOnClickListener = listener;
        int idCount = id.size();
        String idArraySplit = "";
        //String[] idArray = new String[idCount];
        for(int i = 0; i < idCount; i++) {
            idArraySplit = id.get(i) + (i < idCount-1 ?";":"");
            //idArray[i] = id.get(i);
        }
        if(!app) {
            //FetchService.startHistoryFetching(this, context, idArraySplit);
            //FetchGameTask gameTask = new FetchGameTask(this, context);
            //gameTask.execute(idArray);
        }
    }


    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*
        Context        context                         = parent.getContext();
        int            layoutIdForListItem             = R.layout.vertical_list_item;
        LayoutInflater inflater                        = LayoutInflater.from(context);
        boolean        shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view);//, mOnClickListener);
        return viewHolder;
        */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, parent ,false);
        HistoryAdapter.ViewHolder holder = new HistoryAdapter.ViewHolder(view, app, mOnClickListener);
        return holder;
    }

    public void refreshData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.bind(items[position]);
        //Glide.with(context).asBitmap().load(takeFromHTML("appImage",appids.get(position))).into(holder.img);
        //holder.txt.setText(takeFromHTML("appName",appids.get(position)));
        //holder.price.setText("XX.XX TRY");
        String defaultImageURL = "https://steamcdn-a.akamaihd.net/steamcommunity/public/images/avatars/fe/fef49e7fa7e1997310d705b2a6158ff8dc1cdfeb_full.jpg";
        if(app) {
            Glide.with(context).asBitmap().load(takeFromHTML("appImage",id.get(position))).into(holder.img);
            holder.txt.setText("Game ID: "+id.get(position)); //maybe we dont need
        } else {
            if(dataSynchronized
                    && userStatuses.length > 0 && userNicks.length > 0 && imageUrls.length > 0
                    && userStatuses.length > position && userNicks.length > position && imageUrls.length > position) {
                String statusIcon = "";
                if(userStatuses[position] == "0") { // offline
                    int unicode = 0x26AA;
                    statusIcon = getEmojiByUnicode(unicode);
                } else if(userStatuses[position] == "1") { // online
                    int unicode = 0x1F535;
                    statusIcon = getEmojiByUnicode(unicode);
                } else if(userStatuses[position] == "2") { // busy
                    int unicode = 0x1F534;
                    statusIcon = getEmojiByUnicode(unicode);
                } else {
                    int unicode = 0x26AB;
                    statusIcon = getEmojiByUnicode(unicode);
                }
                Glide.with(context).asBitmap().load(imageUrls[position]).into(holder.img);
                holder.txt.setText(statusIcon+" "+userNicks[position]);
            } else {
                Glide.with(context).asBitmap().load(defaultImageURL).into(holder.img);
                holder.txt.setText("ID:"+id.get(position));
            }
        }
    }

    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public void refreshImages(Map<String, String> data) {
        this.data = data;
        Log.i("refresh_url", data.get(FetchGameTask.USER_AVATAR));
        Log.i("refresh_nick", data.get(FetchGameTask.USER_NAME));
        Log.i("refresh_stat", data.get(FetchGameTask.USER_STATUS));
        String[] urls = data.get(FetchGameTask.USER_AVATAR).split("₺");
        String[] nicks = data.get(FetchGameTask.USER_NAME).split("₺");
        String[] statuses = data.get(FetchGameTask.USER_STATUS).split("₺");
        int size = urls.length ;
        this.imageUrls = new String[size];
        this.userNicks = new String[size];
        this.userStatuses = new String[size];
        for (int i = 0; i < size; i++) {
            this.imageUrls[i] = urls[i];
        }
        for (int i = 0; i < size; i++) {
            this.userNicks[i] = nicks[i];
        }
        for (int i = 0; i < size; i++) {
            this.userStatuses[i] = statuses[i];
        }
        dataSynchronized = true;
        notifyDataSetChanged();
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
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private  ListItemClickListener mListener;
        public TextView txt;
        public ImageView img;
        boolean app;

        public ViewHolder(View itemView, boolean app, ListItemClickListener listener) {
            super(itemView);
            this.app = app;
            txt = itemView.findViewById(R.id.historyText);
            img = itemView.findViewById(R.id.historyImage);
            mListener            = listener;
            itemView.setOnClickListener(this);
        }
        void bind(String s) {
            txt.setText(s);
        }
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(clickedPosition, app);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, boolean app);
    }

}
