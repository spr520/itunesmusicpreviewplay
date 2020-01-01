package com.edoo.itunesmusicpreviewtrackplay.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edoo.itunesmusicpreviewtrackplay.R;
import com.edoo.itunesmusicpreviewtrackplay.data.ITunesMusic;
import java.util.ArrayList;

public class MusicItemAdapter extends RecyclerView.Adapter implements View.OnClickListener{
    private OnItemClickListener mOnItemClickListener = null;


    public interface OnItemClickListener {
        void onItemClick(View view);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    protected ArrayList<Integer> mLabelIndex;
    protected ArrayList<ITunesMusic> mItemList;


    public MusicItemAdapter() {
        mLabelIndex = new ArrayList<>();
        mItemList = new ArrayList<>();
    }

    public void addItem(ITunesMusic item) {
        mItemList.add(item);
    }

    public void clear() {
        mItemList.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.music_itemview, null);
        view.setOnClickListener(this);
        return new MusicItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ITunesMusic music = mItemList.get(i);
        MusicItemView vh = (MusicItemView) viewHolder;

        vh.setTrackName(music.trackName);
        String label = music.collectionName + " " + music.artistName;
        vh.setCollectionArtistName(label);
        vh.setIcon(music.artworkUrl100);

        vh.itemView.setTag(music);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }
}
