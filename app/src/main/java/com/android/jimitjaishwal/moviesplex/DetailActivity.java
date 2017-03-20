package com.android.jimitjaishwal.moviesplex;

import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.android.jimitjaishwal.moviesplex.data.MovieContract;
import com.android.jimitjaishwal.moviesplex.data.MovieProjection;

import static com.android.jimitjaishwal.moviesplex.data.MovieProjection.MOVIE_ID;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private MyPagerAdapter pagerAdapter;
    private Cursor mCursor;
    private String mSelectedItemId;
    private String mStartId;
    private Uri mUri;
    private String[] mProjection;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportLoaderManager().initLoader(0, null, this);
        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        viewPager.setPageMargin((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
        int colorPrimary = getResources().getColor(R.color.colorPrimaryDark);
        viewPager.setPageMarginDrawable(new ColorDrawable(colorPrimary));


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mCursor != null) {
                    mCursor.moveToPosition(position);
                    mSelectedItemId = mCursor.getString(MovieProjection.INDEX_MOVIE_ID);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (savedInstanceState == null) {
            if (getIntent() != null && getIntent().getData() != null) {
                mStartId = MovieContract.MovieEntry.getMovieIdFromUri(getIntent().getData());
                mSelectedItemId = mStartId;
            }
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sort_by = getIntent().getExtras().getString("SORT_BY");
        if (sort_by != null)
            switch (sort_by) {
                case "favourite":
                    mUri = MovieContract.FavouriteMovieEntry.BASE_URI;
                    mProjection = MovieProjection.Projection.FAVOURITE_MOVIE_LIST_PROJECTION;
                    break;

                case "popularity.desc":
                    mUri = MovieContract.PopularMovieEntry.BASE_URI;
                    mProjection = MovieProjection.Projection.MOVIE_LIST_PROJECTION;
                    break;

                case "vote_average.desc":
                    mUri = MovieContract.MovieEntry.BASE_URI;
                    mProjection = MovieProjection.Projection.POPULAR_MOVIE_LIST_PROJECTION;
                    break;
            }


        return new CursorLoader(
                this,
                mUri,
                mProjection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        pagerAdapter.notifyDataSetChanged();
        if (mStartId == null) {
            mStartId = "0";
        }
        int id = Integer.parseInt(mStartId);
        if (id > 0) {
            mCursor.moveToFirst();

            while (!mCursor.isAfterLast()) {
                if (mCursor.getString(MovieProjection.INDEX_MOVIE_ID).equals(mStartId)) {
                    final int position = mCursor.getPosition();
                    viewPager.setCurrentItem(position, false);
                    break;
                }
                mCursor.moveToNext();
            }

            mStartId = "0";
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
        pagerAdapter.notifyDataSetChanged();
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            mCursor.moveToPosition(position);
            return DetailsFragment.getInstance(mCursor.getString(MOVIE_ID));
        }

        @Override
        public int getCount() {
            return (mCursor != null) ? mCursor.getCount() : 0;
        }
    }

}
