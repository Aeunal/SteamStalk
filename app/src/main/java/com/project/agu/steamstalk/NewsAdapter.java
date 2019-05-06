package com.project.agu.steamstalk;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private ListItemClickListener mOnClickListener;
    private int itemCount;
    private Map<String, String> data;

    private String[] contents;
    private String[] labels;
    private String[] dates;
    private String[] titles;

    public NewsAdapter(Context context, ListItemClickListener listener) {
        this.context = context;
        mOnClickListener = listener;
        itemCount=0;

        contents= null;
        labels=null;
        dates= null;
        titles=null;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, parent ,false);
        ViewHolder holder = new ViewHolder(view, mOnClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(titles[position], contents[position], dates[position], labels[position]);
    }

    public void refreshData(Map<String, String> data) {
        this.data = data;

        contents = data.get(FetchGameTask.NEWS_CONTENT).split("₺");
        labels = data.get(FetchGameTask.NEWS_LABEL).split("₺");
        dates = data.get(FetchGameTask.NEWS_DATE).split("₺");
        titles = data.get(FetchGameTask.NEWS_TITLE).split("₺");
        itemCount = titles.length;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ListItemClickListener mListener;
        public TextView title;
        public TextView date;
        public TextView content;
        public TextView label;

        public ViewHolder(View itemView, ListItemClickListener listener) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            date = (TextView) itemView.findViewById(R.id.item_date);
            content = (TextView) itemView.findViewById(R.id.item_content);
            label = (TextView) itemView.findViewById(R.id.item_label);
            mListener            = listener;
            itemView.setOnClickListener(this);
        }
        void bind(String title, String content, String date, String label) {
            Log.i( "NewsRes","T: "+title);
            this.title.setText(title);
            /*this.label.setText(label);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.content.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT));
            } else {
                this.content.setText(Html.fromHtml(content));
            }
            this.date.setText(getDate(Long.parseLong(date)*1000, "dd/MM/yyyy"));//*///"dd/MM/yyyy hh:mm:ss.SSS"));
        }

        public String getDate(long milliSeconds, String dateFormat)
        {
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onListItemClick(clickedPosition);
        }
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


}

