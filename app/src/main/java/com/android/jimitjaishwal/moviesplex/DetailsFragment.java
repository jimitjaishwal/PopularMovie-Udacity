package com.android.jimitjaishwal.moviesplex;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jimitjaishwal.moviesplex.MovieUtility.Utility;
import com.android.jimitjaishwal.moviesplex.adapter.ReviewAdapter;
import com.android.jimitjaishwal.moviesplex.adapter.TrailerAdapter;
import com.android.jimitjaishwal.moviesplex.data.MovieContract;
import com.android.jimitjaishwal.moviesplex.data.MovieProjection;
import com.android.jimitjaishwal.moviesplex.models.ReviewModel;
import com.android.jimitjaishwal.moviesplex.models.TrailerModel;
import com.android.jimitjaishwal.moviesplex.networkservice.NetworkService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.jimitjaishwal.moviesplex.R.id.overView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment implements TrailerAdapter.OnMovieSelected, LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(overView)
    TextView movieOverView;

    @BindView(R.id.movie_runtime)
    TextView movieRuntime;

    @BindView(R.id.release_date)
    TextView releaseDate;

    @BindView(R.id.vote_average)
    TextView voteAverage;

    @BindView(R.id.movie_poster)
    ImageView detailPoster;

    @BindView(R.id.movie_category)
    TextView movieType;

    @BindView(R.id.movie_title)
    TextView movieTitle;

    @BindView(R.id.movie_title_bg)
    LinearLayout movieTitleBg;

    @BindView(R.id.backdrop_path)
    ImageView backDropPath;

    @BindView(R.id.toolbar)
    Toolbar toolBar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.movie_trailer_recycler)
    RecyclerView movieTrailerList;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.movie_review_recycler)
    RecyclerView mReviewRecycler;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.nested_scrollview)
    NestedScrollView nsv;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private static final int DETAIL_LOADER_ID = 0;
    public static final String ARG_MOVIE_ID = "movie_id";

    private String mItemId;
    private Uri detailUri;

    private String[] mProjection;
    private int darkMutedColor;
    private int lightColor;

    public DetailsFragment() {
    }

    public static DetailsFragment getInstance(String movieId) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_MOVIE_ID, movieId);
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTrailerAdapter = new TrailerAdapter(getActivity(), this);
        if (getArguments().containsKey(ARG_MOVIE_ID)) {
            mItemId = getArguments().getString(ARG_MOVIE_ID);

            FetchVideoTrailer videoTrailer = new FetchVideoTrailer();
            videoTrailer.execute(mItemId);

            FetchReviews reviews = new FetchReviews();
            reviews.execute(mItemId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail_layout, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        movieTrailerList.setLayoutManager(layoutManager);
        movieTrailerList.setAdapter(mTrailerAdapter);
        movieTrailerList.setNestedScrollingEnabled(false);
        movieTrailerList.setHasFixedSize(false);

        int reviewColumnCount = getResources().getInteger(R.integer.review_column_count);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(reviewColumnCount
                , StaggeredGridLayoutManager.VERTICAL);
        mReviewRecycler.setLayoutManager(gridLayoutManager);
        mReviewAdapter = new ReviewAdapter(getActivity());
        mReviewRecycler.setAdapter(mReviewAdapter);
        mReviewRecycler.setNestedScrollingEnabled(false);
        mReviewRecycler.setHasFixedSize(false);
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
    }

    @Override
    public void onVideoSelected(String videoUrl) {
        String url = "http://www.youtube.com/watch?v=" + videoUrl;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String sort_by = getActivity().getIntent().getExtras().getString("SORT_BY");
        if (sort_by != null)
            switch (sort_by) {
                case "favourite":
                    detailUri = MovieContract.FavouriteMovieEntry.buildFavouriteMovieDetailUri(mItemId);
                    mProjection = MovieProjection.Projection.FAVOURITE_MOVIE_DETAILS_PROJECTION;
                    break;
                case "popularity.desc":
                    detailUri = MovieContract.PopularMovieEntry.buildPopularMovieDetailUri(mItemId);
                    mProjection = MovieProjection.Projection.POPULAR_MOVIE_DETAIL_PROJECTION;
                    break;

                case "vote_average.desc":
                    detailUri = MovieContract.MovieEntry.buildMovieDetailUri(mItemId);
                    mProjection = MovieProjection.Projection.MOVIE_DETAILS_PROJECTION;
                    break;
            }

        return new CursorLoader(
                getActivity(),
                detailUri,
                mProjection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            String sort_by = getActivity().getIntent().getExtras().getString("SORT_BY");

            String movieName = cursor.getString(MovieProjection.MOVIE_NAME);
            final String originalTitle = String.format(getString(R.string.movie_title), movieName);
            movieTitle.setText(originalTitle);
            collapsingToolbar.setTitle(originalTitle);

            String genre = cursor.getString(MovieProjection.MOVIE_GENRE);
            String movieGenre = String.format(getString(R.string.genres), genre);
            movieType.setText(movieGenre);

            String posterPath = "http://image.tmdb.org/t/p/w342" + cursor.getString(MovieProjection.POSTER_PATH);
            Log.e("DetailFragment: ", "MOVIEID==>" + mItemId);
            Glide.with(getActivity())
                    .load(posterPath)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            detailPoster.setImageBitmap(bitmap);
                        }
                    });

            toolBar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });

            String backdropPath = "http://image.tmdb.org/t/p/w780" + cursor.getString(MovieProjection.BACKDROP_PATH);
            Glide.with(this)
                    .load(backdropPath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();

                            Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                                public void onGenerated(Palette palette) {
                                    int defaultColor = 0xFF333333;
                                    darkMutedColor = palette.getDarkMutedColor(defaultColor);
                                    int vibrantColor = palette.getDominantColor(defaultColor);
                                    mCoordinatorLayout.setBackgroundColor(vibrantColor);
                                    lightColor = palette.getVibrantColor(defaultColor);

                                    int lightVibrantColor = palette.getLightVibrantColor(defaultColor);
                                    fab.setRippleColor(lightVibrantColor);
                                    fab.setBackgroundTintList(ColorStateList.valueOf(lightColor));
                                    movieTitleBg.setBackgroundColor(darkMutedColor);
                                    if (collapsingToolbar != null) {
                                        collapsingToolbar.setContentScrimColor(darkMutedColor);
                                        collapsingToolbar.setStatusBarScrimColor(darkMutedColor);
                                    }
                                }
                            });

                            return false;
                        }
                    })
                    .into(backDropPath);

            String overView = cursor.getString(MovieProjection.MOVIE_OVERVIEW);
            String mOverView = String.format(getString(R.string.overview), overView);
            movieOverView.setText(mOverView);

            double mVoteAverage = cursor.getShort(MovieProjection.MOVIE_AVERAGE);
            String movieAvg = String.format(getString(R.string.vote_average), String.valueOf(mVoteAverage));
            voteAverage.setText(movieAvg);

            String mReleaseDate = cursor.getString(MovieProjection.MOVIE_RELEASE_DATE);
            String date = String.format(getString(R.string.Release_Date), Utility.humanReadableDate(mReleaseDate));
            releaseDate.setText(date);

            String time = cursor.getString(MovieProjection.MOVIE_RUNTIME);
            String runtime = String.format(getString(R.string.run_time), time);
            movieRuntime.setText(runtime);

            if (isMovieFavourite(mItemId)) {
                fab.setImageResource(R.drawable.ic_favorite_white_24dp);
            } else {
                fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            }

            if (sort_by != null) {
                if (sort_by.equals("favourite")) {
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isMovieFavourite(mItemId)) {
                                Toast.makeText(getActivity(), originalTitle + " remove from Favourite!", Toast.LENGTH_SHORT).show();
                                deleteMovies(mItemId);
                                getActivity().onBackPressed();
                            }
                        }
                    });
                } else {
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isMovieFavourite(mItemId)) {
                                Toast.makeText(getActivity(), originalTitle + " remove from Favourite!", Toast.LENGTH_SHORT).show();
                                deleteMovies(mItemId);
                            } else {
                                Toast.makeText(getActivity(), originalTitle + " add Favourite!", Toast.LENGTH_SHORT).show();
                                insertData(cursor);
                                fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                            }
                        }
                    });
                }
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void insertData(Cursor cursor) {
        getActivity().getContentResolver().insert(MovieContract.FavouriteMovieEntry.BASE_URI, MovieProjection.getContentValue(cursor));
    }

    private void deleteMovies(String movieId) {
        String where = MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " = ?";
        int deletedRows = getActivity().getContentResolver().delete(
                MovieContract.FavouriteMovieEntry.BASE_URI, where, new String[]{movieId});

        if (deletedRows > 0) {
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
    }

    private boolean isMovieFavourite(String movieId) {
        Uri mUri = MovieContract.FavouriteMovieEntry.buildFavouriteMovieDetailUri(movieId);
        Cursor queryCursor = getActivity().getContentResolver().query(mUri, null, null, null, null);
        if (queryCursor != null && queryCursor.moveToFirst()) {
            queryCursor.close();
            return true;
        }
        return false;
    }

    public class FetchVideoTrailer extends AsyncTask<String, Void, ArrayList<TrailerModel>> {
        private ArrayList<TrailerModel> trailerList = new ArrayList<>();

        @Override
        protected ArrayList<TrailerModel> doInBackground(String... params) {

            try {
                trailerList = NetworkService.getTrailerList(params[0], BuildConfig.THE_MOVIE_DB_API_TOKEN);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return trailerList;
        }

        @Override
        protected void onPostExecute(ArrayList<TrailerModel> trailerModels) {
            if (trailerModels != null) {
                for (TrailerModel trailer : trailerModels) {
                    mTrailerAdapter.add(trailer);
                }
                mTrailerAdapter.notifyDataSetChanged();
            }
        }
    }

    public class FetchReviews extends AsyncTask<String, Void, ArrayList<ReviewModel>> {

        private ArrayList<ReviewModel> reviewList = new ArrayList<>();

        @Override
        protected ArrayList<ReviewModel> doInBackground(String... params) {
            try {
                reviewList = NetworkService.getMovieReviews(params[0], BuildConfig.THE_MOVIE_DB_API_TOKEN);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return reviewList;
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewModel> reviewList) {
            if (reviewList != null) {
                for (ReviewModel review : reviewList) {
                    mReviewAdapter.add(review);
                }
            }
        }
    }
}
