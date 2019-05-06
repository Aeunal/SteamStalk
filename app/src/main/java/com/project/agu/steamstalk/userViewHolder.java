package com.project.agu.steamstalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class userViewHolder extends RecyclerView.ViewHolder  {

    ImageView gameImage;
    TextView value;

    public userViewHolder(View itemView) {
        super(itemView);
        value = (TextView) itemView.findViewById(R.id.value);
        gameImage = (ImageView) itemView.findViewById(R.id.gameImage);
    }
    void bind(String imageRes, String s, Context context){
        value.setText(s);
        Log.i("GlideURL",s+" : "+ imageRes);
        Glide.with(context).load(imageRes).into(gameImage);
        //gameImage.setImageResource(imageRes);
    }
}
