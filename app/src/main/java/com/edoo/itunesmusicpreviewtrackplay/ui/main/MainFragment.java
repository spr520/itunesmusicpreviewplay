package com.edoo.itunesmusicpreviewtrackplay.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.edoo.itunesmusicpreviewtrackplay.R;
import com.edoo.itunesmusicpreviewtrackplay.data.ITunesMusic;
import com.edoo.itunesmusicpreviewtrackplay.view.MusicItemAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    RecyclerView mRecyclerView;
    MusicItemAdapter mAdapter;
    MediaPlayer mMediaplayer;

    ImageView mCtrlBarIcon;
    TextView mCtrlBarTrackName;
    TextView mCtrlBarArtName;
    ImageButton mControlBtn;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMediaplayer = new MediaPlayer();
        mAdapter = new MusicItemAdapter();

        mAdapter.setOnItemClickListener(new MusicItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                ITunesMusic music = (ITunesMusic)view.getTag();
                try {
                    if(mMediaplayer.isPlaying()) {
                        mMediaplayer.stop();
                    }
                    // sluggish
                    mMediaplayer.reset();
                    mMediaplayer.setDataSource(music.previewUrl);
                    mMediaplayer.prepare();
                    mMediaplayer.start();

                    Picasso.get().load(music.artworkUrl100).into(mCtrlBarIcon);
                    mCtrlBarTrackName.setText(music.trackName);
                    mCtrlBarArtName.setText(music.collectionName + " " + music.artistName);
                    mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pause, null));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        findViews();

        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaplayer.isPlaying()) {
                    mMediaplayer.pause();
                    mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play_arrow, null));
                } else {
                    mMediaplayer.start();
                    mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pause, null));
                }
            }
        });
    }

    private void findViews() {
        mRecyclerView = getActivity().findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation()));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAlpha(0);

        mCtrlBarIcon = getActivity().findViewById(R.id.ctrl_bar_icon);
        mCtrlBarTrackName = getActivity().findViewById(R.id.ctrl_bar_track_name);
        mCtrlBarArtName = getActivity().findViewById(R.id.ctrl_bar_collection_artist_name);
        mControlBtn = getActivity().findViewById(R.id.control_btn);
    }

    public void updateMusicList(final ITunesMusic[] ITunesMusics) {
        mAdapter.clear();
        if (ITunesMusics.length > 0) {
            mRecyclerView.setAlpha(1);
        } else {
            mRecyclerView.setAlpha(0);
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ITunesMusic item : ITunesMusics) {
                    mAdapter.addItem(item);
                }
                mAdapter.notifyDataSetChanged();
            }
        });

    }

}
