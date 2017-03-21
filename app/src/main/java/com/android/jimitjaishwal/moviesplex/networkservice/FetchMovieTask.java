package com.android.jimitjaishwal.moviesplex.networkservice;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;

import com.android.jimitjaishwal.moviesplex.BuildConfig;
import com.android.jimitjaishwal.moviesplex.models.MovieDetail;
import com.android.jimitjaishwal.moviesplex.models.MovieModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by JIMIT JAISHWAL on 06-03-2017.
 */

public class FetchMovieTask extends AsyncTask<String, Integer, Void> {
    private Context mContext;
    private static final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    public FetchMovieTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
    }

    @Override
    protected Void doInBackground(String... params) {
        Vector<ContentValues> contentValues = new Vector<>();
        ArrayList<MovieModel> movieList = new ArrayList<>();
        ArrayList<MovieDetail> movieDetailList = new ArrayList<>();
        final String sort_by = params[0];
        final String page = params[1];

        try {
            if (sort_by.equals("popular")) {
                movieList.addAll(NetworkService.getMovieList(sort_by, BuildConfig.THE_MOVIE_DB_API_TOKEN, page));

                for (MovieModel movie : movieList) {
                    movieDetailList.add(NetworkService.getMovieDetails(Long.toString(movie.getId()), BuildConfig.THE_MOVIE_DB_API_TOKEN));
                }
                NetworkUtils.insertPopularMovies(movieDetailList, contentValues, mContext, LOG_TAG);

            } else if (sort_by.equals("top_rated")) {

                movieList.addAll(NetworkService.getMovieList(sort_by, BuildConfig.THE_MOVIE_DB_API_TOKEN, page));

                for (MovieModel movie : movieList) {
                    movieDetailList.add(NetworkService.getMovieDetails(Long.toString(movie.getId()), BuildConfig.THE_MOVIE_DB_API_TOKEN));
                }

                NetworkUtils.insertVoteAverageMovies(movieDetailList, contentValues, mContext, LOG_TAG);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
