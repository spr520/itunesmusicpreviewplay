package com.edoo.itunesmusicpreviewtrackplay.apiHelper;


import com.edoo.itunesmusicpreviewtrackplay.data.ITunesMusic;

public interface ITunesMusicListListener {
    void onSuccess(ITunesMusic[] ITunesMusics);
    void onFail(String message);
}
