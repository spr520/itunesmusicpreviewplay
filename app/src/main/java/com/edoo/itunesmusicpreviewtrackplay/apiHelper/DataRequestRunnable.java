package com.edoo.itunesmusicpreviewtrackplay.apiHelper;

import android.util.Log;


import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.edoo.itunesmusicpreviewtrackplay.BuildConfig;

abstract class DataRequestRunnable implements Runnable {

    private static final String TAG = DataRequestRunnable.class.getSimpleName();

    private OkHttpClient mClient;

    DataRequestRunnable() {
        mClient = new OkHttpClient();
    }

    InputStream getData(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        if (BuildConfig.isDebug) Log.d(TAG, "url > " + url);

        Response response = mClient.newCall(request).execute();

        if (BuildConfig.isDebug) Log.d(TAG, "response > " + response.code());

        if (response.code() == 200) {
            return response.body().byteStream();
        } else {
            return null;
        }
    }
}