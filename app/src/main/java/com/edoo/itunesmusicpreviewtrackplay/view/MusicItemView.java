package com.edoo.itunesmusicpreviewtrackplay.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edoo.itunesmusicpreviewtrackplay.R;
import com.squareup.picasso.Picasso;


public class MusicItemView extends RecyclerView.ViewHolder {

    private ImageView mIcon;
    private TextView mTrackName;
    private TextView mCollectionArtistName;

    public MusicItemView(@NonNull View itemView) {
        super(itemView);
        mIcon = itemView.findViewById(R.id.icon);
        mTrackName = itemView.findViewById(R.id.track_name);
        mCollectionArtistName = itemView.findViewById(R.id.collection_artist_name);

    }

    public void setIcon(String url) {
        Picasso.get().load(url).into(mIcon);
    }

    public void setTrackName(String name) {
        mTrackName.setText(name);
    }

    public void setCollectionArtistName(String name) {
        mCollectionArtistName.setText(name);
    }

}
