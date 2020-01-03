package com.edoo.itunesmusicpreviewtrackplay.apiHelper;

import android.util.Log;

import com.edoo.itunesmusicpreviewtrackplay.data.ITunesMusic;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class getITunesMusicListRunnable extends DataRequestRunnable {

    private final String TAG = getITunesMusicListRunnable.class.getSimpleName();

    private final String DOMAIN_URL = "http://itunes.apple.com/search?";
    private final String DATA_URL = "term=%s&media=music";

    private ArrayList<ITunesMusicListListener> mListeners;
    private String mKeywords;

    public getITunesMusicListRunnable(ArrayList<ITunesMusicListListener> listeners, String keywords) {
        mListeners = listeners;
        mKeywords = keywords;
    }

    @Override
    public void run() {
        String parameter = String.format(DATA_URL, mKeywords);

        String url = DOMAIN_URL + parameter;

        try {
            InputStream in = getData(url);
            if (in != null) {
                parse(in);
            } else { // fail
                Log.d(TAG, "Get iTunes music list failed ! url = " + url);
            }
        } catch (IOException e) {
            // TODO
            Log.d(TAG,"" +e);
            for(ITunesMusicListListener listener : mListeners) {
                listener.onFail("exception");
            }
        }


    }

    private void parse(InputStream in) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");

            Gson gson = new Gson();
            Result result = gson.fromJson(inputStreamReader, Result.class);
            for(ITunesMusicListListener listener : mListeners) {
                listener.onSuccess(result.results);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG,"" + e);
        }
    }

    class Result {
        String resultCount;
        ITunesMusic[] results;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Result : {\n");
            sb.append(", resultCount : " + resultCount + "\n");
            for(ITunesMusic product : results) {
                sb.append(product.toString() + "\n");
            }
            sb.append(" }");

            return sb.toString();
        }


    }
}
