package com.edoo.itunesmusicpreviewtrackplay.apiHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;

public class ApiHandler {
    private static final String TAG = ApiHandler.class.getSimpleName();
    public static final String KEYWORDS = "keywords";

    private final int MSG_GET_MUSIC_LIST = 100;

    private static ApiHandler sInstance;

    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private ArrayList<ITunesMusicListListener> mGetITunesMusicListListener = new ArrayList<>();

    public static ApiHandler getInstance() {
        if (sInstance == null) {
            sInstance = new ApiHandler();
        }

        return sInstance;
    }

    private ApiHandler() {
        mBackgroundThread = new HandlerThread("apiBGHandler");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_GET_MUSIC_LIST:
                        String keywords = msg.getData().getString(KEYWORDS);
                        post(new getITunesMusicListRunnable(mGetITunesMusicListListener, keywords));
                        break;
                    default:

                        break;
                }
            }
        };
    }

    public synchronized void getITunesMusicList(String keywords) {
        if (!mBackgroundHandler.hasMessages(MSG_GET_MUSIC_LIST)) {
            Message msg = new Message();
            msg.what = MSG_GET_MUSIC_LIST;
            Bundle bundle = new Bundle();
            bundle.putString(KEYWORDS, keywords);
            msg.setData(bundle);
            mBackgroundHandler.sendMessage(msg);
        } else {
            Log.d(TAG,"Query music list is queuing ! ");
        }
    }

    public synchronized void addGetITunesMusicListListener(ITunesMusicListListener listener) {
        if (!mGetITunesMusicListListener.contains(listener)) {
            mGetITunesMusicListListener.add(listener);
        }
    }

    public synchronized void removeGetITunesMusicListListener(ITunesMusicListListener listener) {
        if (mGetITunesMusicListListener.contains(listener)) {
            mGetITunesMusicListListener.remove(listener);
        }
    }
}
