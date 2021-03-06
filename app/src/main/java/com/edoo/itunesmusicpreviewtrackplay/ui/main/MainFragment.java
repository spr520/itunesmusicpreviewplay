package com.edoo.itunesmusicpreviewtrackplay.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.edoo.itunesmusicpreviewtrackplay.R;
import com.edoo.itunesmusicpreviewtrackplay.data.ITunesMusic;
import com.edoo.itunesmusicpreviewtrackplay.view.MusicItemAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    RecyclerView mRecyclerView;
    MusicItemAdapter mAdapter;
    MediaPlayer mMediaPlayer;

    ImageView mCtrlBarIcon;
    TextView mCtrlBarTrackName;
    TextView mCtrlBarArtName;
    ImageButton mControlBtn;
    LottieAnimationView mLoadingAnim;
    LottieAnimationView mNoDataAnim;
    LottieAnimationView mNoNetworkAnim;
    CopyOnWriteArrayList<ITunesMusic> mITunesMusics = new CopyOnWriteArrayList<>();
    boolean mHasNetwork = true;
    boolean mIsPause = false;

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

        mMediaPlayer = new MediaPlayer();
        mAdapter = new MusicItemAdapter();

        mAdapter.setOnItemClickListener(new MusicItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                ITunesMusic music = (ITunesMusic)view.getTag();
                try {
                    if(mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(music.previewUrl);
                    mMediaPlayer.prepareAsync();

                    Picasso.get().load(music.artworkUrl100).into(mCtrlBarIcon);
                    mCtrlBarTrackName.setText(music.trackName);
                    mCtrlBarArtName.setText(music.collectionName + ", " + music.artistName);
                    mControlBtn.setVisibility(View.VISIBLE);
                    mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play_arrow, null));
                    mControlBtn.setEnabled(false);
                    mControlBtn.setAlpha(0.5f);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        findViews();

        mControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play_arrow, null));
                } else {
                    mMediaPlayer.start();
                    mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pause, null));
                }
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play_arrow, null));
            }
        });

        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (!mIsPause) {
                    mMediaPlayer.start();
                    mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.pause, null));
                }
                mControlBtn.setEnabled(true);
                mControlBtn.setAlpha(1f);
            }
        });

        mLoadingAnim.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                clearRecyclerView();
                mRecyclerView.setAlpha(0);
                mNoDataAnim.setVisibility(View.INVISIBLE);
                mNoNetworkAnim.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLoadingAnim.setVisibility(View.INVISIBLE);
                mAdapter.clear();
                if(!mHasNetwork) {
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAlpha(0);
                    showNoNetworkAnimation();
                    return;
                }

                if (mITunesMusics.size() > 0) {
                    mRecyclerView.setAlpha(1);
                    mAdapter.clear();
                    for (ITunesMusic item : mITunesMusics) {
                        mAdapter.addItem(item);
                    }
                } else {
                    mRecyclerView.setAlpha(0);
                    showNoDataAnimation();
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsPause = true;
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        if (mControlBtn != null)
            mControlBtn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.play_arrow, null));
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

        mLoadingAnim = getActivity().findViewById(R.id.loading_anim_view);
        mNoDataAnim = getActivity().findViewById(R.id.no_data_anim_view);
        mNoNetworkAnim = getActivity().findViewById(R.id.no_network_anim_view);
    }

    public void startLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingAnim.setVisibility(View.VISIBLE);
                mLoadingAnim.setRepeatCount(LottieDrawable.INFINITE);
                mLoadingAnim.playAnimation();
            }
        });
    }

    public void stopLoading() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoadingAnim.setRepeatCount(0);
            }
        });
    }

    private void showNoDataAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNoDataAnim.setVisibility(View.VISIBLE);
                mNoDataAnim.setRepeatCount(0);
                mNoDataAnim.playAnimation();
            }
        });
    }

    private void showNoNetworkAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNoNetworkAnim.setVisibility(View.VISIBLE);
                mNoNetworkAnim.setRepeatCount(0);
                mNoNetworkAnim.playAnimation();
            }
        });
    }


    public void clearRecyclerView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void updateMusicList(final ITunesMusic[] ITunesMusics) {
        mITunesMusics.clear();
        for(ITunesMusic item :ITunesMusics) {
            mITunesMusics.add(item);
        }
        stopLoading();
    }

    public void notifyNetworkStatus(boolean hasNetWork) {
        mHasNetwork = hasNetWork;
    }

}
