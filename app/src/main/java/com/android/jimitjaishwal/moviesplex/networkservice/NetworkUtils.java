package com.android.jimitjaishwal.moviesplex.networkservice;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.android.jimitjaishwal.moviesplex.data.MovieContract;
import com.android.jimitjaishwal.moviesplex.data.MovieProjection;
import com.android.jimitjaishwal.moviesplex.models.MovieDetail;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by JIMIT JAISHWAL on 08-03-2017.
 */

public class NetworkUtils {

    public static void insertVoteAverageMovies(ArrayList<MovieDetail> movieDetailList
            , Vector<ContentValues> contentValues, Context mContext, String LOG_TAG) {
        for (MovieDetail movie : movieDetailList) {
            if (movie != null) {
                ContentValues values = MovieProjection.putContentValueFromVoteAverageMovie(movie);
                contentValues.add(values);
            }
        }

        int inserted = 0;
        if (contentValues.size() > 0) {
            ContentValues[] contentValuesArray = new ContentValues[contentValues.size()];
            contentValues.toArray(contentValuesArray);
            inserted = mContext.getContentResolver().bulkInsert(
                    MovieContract.MovieEntry.BASE_URI,
                    contentValuesArray
            );
            Log.e(LOG_TAG, +inserted + " Rows Inserted Into Vote Average Movie Database");
        }

    }

    public static void insertPopularMovies(ArrayList<MovieDetail> movieDetailList,
                                           Vector<ContentValues> contentValues, Context mContext, String LOG_TAG) {
        for (MovieDetail movie : movieDetailList) {
            if (movie != null) {
                ContentValues values = MovieProjection.putContentValueFromPopularMovie(movie);
                contentValues.add(values);
            }
        }

        int inserted = 0;
        if (contentValues.size() > 0) {
            ContentValues[] contentValuesArray = new ContentValues[contentValues.size()];
            contentValues.toArray(contentValuesArray);
            inserted = mContext.getContentResolver().bulkInsert(
                    MovieContract.PopularMovieEntry.BASE_URI,
                    contentValuesArray
            );
            Log.e(LOG_TAG, +inserted + " Rows Inserted Into Popular Movie Database");
        }

    }
}
