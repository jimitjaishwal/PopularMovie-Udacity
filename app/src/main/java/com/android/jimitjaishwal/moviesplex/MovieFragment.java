package com.android.jimitjaishwal.moviesplex;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.jimitjaishwal.moviesplex.MovieUtility.Utility;
import com.android.jimitjaishwal.moviesplex.adapter.EndlessRecyclerViewScrollListener;
import com.android.jimitjaishwal.moviesplex.adapter.MovieAdapter;
import com.android.jimitjaishwal.moviesplex.data.MovieContract;
import com.android.jimitjaishwal.moviesplex.data.MovieProjection;
import com.android.jimitjaishwal.moviesplex.networkservice.FetchMovieTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIMIT JAISHWAL on 23-02-2017.
 */

public class MovieFragment extends Fragment implements MovieAdapter.OnItemSelected, LoaderManager.LoaderCallbacks<Cursor> {

    private MovieAdapter mMovieAdapter;
    private StaggeredGridLayoutManager layoutManager;

    private Cursor mCursor;
    private String selectedItemPosition = "itemPosition";

    @BindView(R.id.movie_list_recycler)
    RecyclerView mRecyclerView;

    private Uri mUri;
    private String[] mProjection;

    private static final int LOADER_ID = 1;
    private boolean isMovieFavouriteSelected = false;
    private Parcelable mGridState;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout mSwipeToRefresh;

    private String sort_by;
    private EndlessRecyclerViewScrollListener scrollListener;

    private boolean isLoadFirstTime = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGridState = layoutManager.onSaveInstanceState();
        outState.putParcelable(selectedItemPosition, mGridState);
    }

    public void loadMore(String page) {
// 2. Notify the adapter of the update
// 3. Reset endless scroll listener when performing a new search
        // mMovieAdapter.removeFooter();
        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        movieTask.execute(sort_by, page);
        //getLoaderManager().restartLoader(LOADER_ID, null, this).forceLoad();
        // Handler handler = new Handler();
        // mMovieAdapter.addFooter();
      /*  final Runnable r = new Runnable() {
            public void run() {
                mMovieAdapter.notifyItemInserted(mCursor.getCount() - 1);
            }
        };

        handler.post(r);*/
    }

    public void refresh() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort_by = prefs.getString(
                getActivity().getString(R.string.pref_movie_sort_by_key),
                getActivity().getString(R.string.pref_movie_sort_by_popular_key));

        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        movieTask.execute(sort_by, "1");

        switch (sort_by) {
            case "favourite":
                mUri = MovieContract.FavouriteMovieEntry.BASE_URI;
                mProjection = MovieProjection.Projection.FAVOURITE_MOVIE_LIST_PROJECTION;
                isMovieFavouriteSelected = true;
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                break;

            case "popularity.desc":
                mUri = MovieContract.PopularMovieEntry.BASE_URI;
                mProjection = MovieProjection.Projection.MOVIE_LIST_PROJECTION;
                isMovieFavouriteSelected = false;
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                break;

            case "vote_average.desc":
                mUri = MovieContract.MovieEntry.BASE_URI;
                mProjection = MovieProjection.Projection.POPULAR_MOVIE_LIST_PROJECTION;
                getLoaderManager().restartLoader(LOADER_ID, null, this);
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_fragment_base, container, false);
        ButterKnife.bind(this, rootView);
        refresh();

        if (savedInstanceState != null) {
            mGridState = savedInstanceState.getParcelable(selectedItemPosition);
        }


        mSwipeToRefresh.setColorScheme(Utility.colors);

        int columnCount = Utility.calculateNoOfColumns(getActivity());

        layoutManager =
                new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setHasFixedSize(true);


        isLoadFirstTime = true;
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
                isLoadFirstTime = false;
                mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //refresh();
                        mSwipeToRefresh.setRefreshing(true);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadNextPage(page);
                                mSwipeToRefresh.setRefreshing(false);
                            }
                        }, 3000);
                    }
                });
            }
        };

        mRecyclerView.addOnScrollListener(scrollListener);
        // scrollListener.resetState();
        return rootView;
    }

    public void loadNextPage(int page) {
        // mMovieAdapter.removeFooter();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sort_by = prefs.getString(
                getActivity().getString(R.string.pref_movie_sort_by_key),
                getActivity().getString(R.string.pref_movie_sort_by_popular_key));

        final FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        movieTask.execute(sort_by, String.valueOf(page));

        switch (sort_by) {
            case "favourite":
                mUri = MovieContract.FavouriteMovieEntry.BASE_URI;
                mProjection = MovieProjection.Projection.FAVOURITE_MOVIE_LIST_PROJECTION;
                isMovieFavouriteSelected = true;
                //  mMovieAdapter.removeFooter();
                break;

            case "popularity.desc":
                mUri = MovieContract.PopularMovieEntry.BASE_URI;
                mProjection = MovieProjection.Projection.MOVIE_LIST_PROJECTION;
                isMovieFavouriteSelected = false;
                break;

            case "vote_average.desc":
                mUri = MovieContract.MovieEntry.BASE_URI;
                mProjection = MovieProjection.Projection.POPULAR_MOVIE_LIST_PROJECTION;
                break;
        }
        getLoaderManager().restartLoader(LOADER_ID, null, this);
        // mMovieAdapter.addFooter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        scrollListener.resetState();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onMovieSelected(String movieId, int position, View imageView) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Uri detailUri;
        if (sort_by != null) {
            switch (sort_by) {
                case "favourite":
                    detailUri = MovieContract.FavouriteMovieEntry.buildFavouriteMovieDetailUri(movieId);
                    intent.setData(detailUri);
                    break;
                case "popularity.desc":
                    detailUri = MovieContract.PopularMovieEntry.buildPopularMovieDetailUri(movieId);
                    intent.setData(detailUri);
                    break;

                case "vote_average.desc":
                    detailUri = MovieContract.MovieEntry.buildMovieDetailUri(movieId);
                    intent.setData(detailUri);
                    break;
            }
        } else {
            sort_by = "popularity.desc";
            detailUri = MovieContract.PopularMovieEntry.buildPopularMovieDetailUri(movieId);
            intent.setData(detailUri);
        }
        intent.putExtra("SORT_BY", sort_by);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(
                getActivity(),
                mUri,
                mProjection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            mCursor = data;
            Log.e("MovieDataFragment ", "Cursor data Count" + data.getCount());
            if (isLoadFirstTime) {
                mMovieAdapter.swapCursor(mCursor);
                if (mGridState != null) {
                    layoutManager.onRestoreInstanceState(mGridState);
                }
            } else {
                mMovieAdapter.loadMore(mCursor, mRecyclerView);
                if (mGridState != null) {
                    layoutManager.onRestoreInstanceState(mGridState);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }
}
