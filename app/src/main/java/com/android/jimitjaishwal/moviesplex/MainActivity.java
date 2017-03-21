package com.android.jimitjaishwal.moviesplex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.jimitjaishwal.moviesplex.MovieUtility.TypefaceSpan;
import com.android.jimitjaishwal.moviesplex.data.MovieContract;

public class MainActivity extends AppCompatActivity {

    private TextView appName;
    private String sort_by;

    // final App Ropo fo Nanodegree
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appName = (TextView) findViewById(R.id.app_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        SpannableString s = new SpannableString("Movie Plex");
        s.setSpan(new TypefaceSpan(this, "Pacifico.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        appName.setText(s);
    }

    @Override
    public void finish() {
        super.finish();
        this.getContentResolver().delete(MovieContract.PopularMovieEntry.BASE_URI, null, null);
        this.getContentResolver().delete(MovieContract.MovieEntry.BASE_URI, null, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sortBy = prefs.getString(
                this.getString(R.string.pref_movie_sort_by_key),
                this.getString(R.string.pref_movie_sort_by_popular_key));

        if (sortBy != null && !sortBy.equals(sort_by)) {
            MovieFragment fragment = (MovieFragment) getSupportFragmentManager().findFragmentById(R.id.movie_fragment);

            if (fragment != null) {
                fragment.onSortByChange(sortBy);
            }
            sort_by = sortBy;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_fragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, Settings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
