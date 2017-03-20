package com.android.jimitjaishwal.moviesplex.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by JIMIT JAISHWAL on 05-03-2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.android.jimitjaishwal.moviesplex";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String MOVIE_PATH = "movies";

    public static final String FAVOURITE_MOVIE_PATH = "favouriteMovie";

    public static final String POPULAR_MOVIE_PATH = "popularmovie";

    public static class MovieEntry implements BaseColumns {

        public static final Uri BASE_URI = MovieContract.BASE_URI.buildUpon()
                .appendPath(MOVIE_PATH).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + MOVIE_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/ " + AUTHORITY + "/" + MOVIE_PATH;

        public static final Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(BASE_URI, id);
        }

        public static final Uri buildMovieDetailUri(String movieId) {
            return BASE_URI.buildUpon().appendPath(movieId).build();
        }

        public static final String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static final String COLUMN_TABLE_NAME = "Movies";

        public static final String COLUMN_MOVIE_RUNTIME = "movie_runtime";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_MOVIE_NAME = "movie_name";

        public static final String COLUMN_MOVIE_GENRE = "movie_genre";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_VOTE_COUNT = "vote_count";
    }

    public static class FavouriteMovieEntry implements BaseColumns {

        public static final Uri BASE_URI = MovieContract.BASE_URI.buildUpon()
                .appendPath(FAVOURITE_MOVIE_PATH).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + FAVOURITE_MOVIE_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/ " + AUTHORITY + "/" + FAVOURITE_MOVIE_PATH;

        public static final Uri buildFavouriteMovieUri(long id) {
            return ContentUris.withAppendedId(BASE_URI, id);
        }

        public static final Uri buildFavouriteMovieDetailUri(String movieId) {
            return BASE_URI.buildUpon().appendPath(movieId).build();
        }

        public static final String getFavouriteMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static final String COLUMN_MOVIE_RUNTIME = "movie_runtime";

        public static final String COLUMN_TABLE_NAME = "FavouriteMovie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_POSTER_PTH = "poster_path";

        public static final String COLUMN_MOVIE_NAME = "movie_name";

        public static final String COLUMN_MOVIE_GENRE = "movie_genre";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_VOTE_COUNT = "vote_count";
    }

    public static class PopularMovieEntry implements BaseColumns {

        public static final Uri BASE_URI = MovieContract.BASE_URI.buildUpon()
                .appendPath(POPULAR_MOVIE_PATH).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + POPULAR_MOVIE_PATH;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/ " + AUTHORITY + "/" + POPULAR_MOVIE_PATH;

        public static final Uri buildPopularMovieUri(long id) {
            return ContentUris.withAppendedId(BASE_URI, id);
        }

        public static final Uri buildPopularMovieDetailUri(String movieId) {
            return BASE_URI.buildUpon().appendPath(movieId).build();
        }

        public static final String getPopulatMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static final String COLUMN_MOVIE_RUNTIME = "movie_runtime";

        public static final String COLUMN_TABLE_NAME = "PopularMovie";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String COLUMN_POSTER_PTH = "poster_path";

        public static final String COLUMN_MOVIE_NAME = "movie_name";

        public static final String COLUMN_MOVIE_GENRE = "movie_genre";

        public static final String COLUMN_MOVIE_OVERVIEW = "overview";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_VOTE_COUNT = "vote_count";
    }
}