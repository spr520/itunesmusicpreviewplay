package com.edoo.itunesmusicpreviewtrackplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import com.edoo.itunesmusicpreviewtrackplay.apiHelper.ApiHandler;
import com.edoo.itunesmusicpreviewtrackplay.apiHelper.ITunesMusicListListener;
import com.edoo.itunesmusicpreviewtrackplay.data.ITunesMusic;
import com.edoo.itunesmusicpreviewtrackplay.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity implements ITunesMusicListListener {
    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        getSupportActionBar().setTitle(R.string.action_bar_title);

        ApiHandler.getInstance().addGetITunesMusicListListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchbar, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String text) {
                    ApiHandler.getInstance().getITunesMusicList(text);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    return true;
                }

            });

        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApiHandler.getInstance().removeGetITunesMusicListListener(this);
    }

    @Override
    public void onSuccess(ITunesMusic[] ITunesMusics) {
        MainFragment fragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if(fragment != null)
            fragment.updateMusicList(ITunesMusics);
    }

    @Override
    public void onFail(String message) {

    }
}
