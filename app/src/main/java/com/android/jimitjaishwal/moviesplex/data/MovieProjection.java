package com.android.jimitjaishwal.moviesplex.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.android.jimitjaishwal.moviesplex.MovieUtility.Utility;
import com.android.jimitjaishwal.moviesplex.models.MovieDetail;

/**
 * Created by JIMIT JAISHWAL on 07-03-2017.
 */

public class MovieProjection {
    public static final int MOVIE_ID = 0;
    public static final int BACKDROP_PATH = 1;
    public static final int POSTER_PATH = 2;
    public static final int MOVIE_NAME = 3;
    public static final int MOVIE_GENRE = 4;
    public static final int MOVIE_OVERVIEW = 5;
    public static final int MOVIE_AVERAGE = 6;
    public static final int MOVIE_RELEASE_DATE = 7;
    public static final int VOTE_COUNT = 8;
    public static final int MOVIE_RUNTIME = 9;

    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_BACKDROP_PATH = 1;
    public static final int INDEX_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_NAME = 3;
    public static final int INDEX_MOVIE_GENRE = 4;
    public static final int INDEX_MOVIE_OVERVIEW = 5;
    public static final int INDEX_MOVIE_AVERAGE = 6;
    public static final int INDEX_MOVIE_RELEASE_DATE = 7;
    public static final int INDEX_VOTE_COUNT = 8;
    public static final int INDEX_MOVIE_RUNTIME = 9;

    public static ContentValues getContentValue(Cursor cursor) {
        long movieId = cursor.getLong(MovieProjection.INDEX_MOVIE_ID);
        String backDropPath = cursor.getString(MovieProjection.INDEX_BACKDROP_PATH);
        String posterPath = cursor.getString(MovieProjection.INDEX_POSTER_PATH);
        String originalTitle = cursor.getString(MovieProjection.INDEX_MOVIE_NAME);
        String movieGenre = cursor.getString(MovieProjection.INDEX_MOVIE_GENRE);
        String overview = cursor.getString(MovieProjection.INDEX_MOVIE_OVERVIEW);
        double voteAverage = cursor.getDouble(MovieProjection.INDEX_MOVIE_AVERAGE);
        String releaseDate = cursor.getString(MovieProjection.INDEX_MOVIE_RELEASE_DATE);
        int voteCount = cursor.getInt(MovieProjection.INDEX_VOTE_COUNT);
        String movieRuntime = cursor.getString(MovieProjection.INDEX_MOVIE_RUNTIME);

        ContentValues values = new ContentValues();
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_POSTER_PTH, posterPath);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_BACKDROP_PATH, backDropPath);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_NAME, originalTitle);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_GENRE, movieGenre);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_VOTE_COUNT, voteCount);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_OVERVIEW, overview);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID, movieId);
        values.put(MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RUNTIME, movieRuntime);
        return values;
    }

    public static ContentValues putContentValueFromVoteAverageMovie(MovieDetail movie) {
        String poster = movie.getPosterPath();
        ContentValues values = new ContentValues();
        if (poster != null){
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, poster);
        }else {
            poster = "https://i1.wp.com/vanillicon.com/872a0fb8f9b8c0ca768199c004fe8833_200.png?ssl=1";
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, poster);
        }
        String backDropPath = movie.getBackdropPath();
        if (backDropPath != null){
            values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, backDropPath);
        }else {
            poster = "https://i1.wp.com/vanillicon.com/872a0fb8f9b8c0ca768199c004fe8833_200.png?ssl=1";
            values.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, backDropPath);
        }
        String title = movie.getTitle();
        if (title != null){
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, title);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, "N/A");
        }

        String genres = Utility.getGenres(movie);
        if (genres != null){
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENRE, genres);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_GENRE, "N/A");
        }

        String releaseDate = movie.getReleaseDate();
        if (releaseDate != null){
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "N/A");
        }

        double voteAverage = movie.getVoteAverage();
        if (voteAverage != 0){
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, "N/A");
        }

        long voteCount = movie.getVoteCount();
        if (voteCount != 0){
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, voteCount);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, "N/A");
        }

        String overView = movie.getOverview();
        if (overView != null){
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, overView);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, "N/A");
        }

        long movieId = movie.getId();
        if (movieId != 0){
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "N/A");
        }

        long runtime = movie.getRuntime();
        if (runtime != 0){
            String movieRuntime = String.valueOf(runtime) + " Min";
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME, movieRuntime);
        }else {
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME, "N/A");
        }
        return values;
    }

    public static ContentValues putContentValueFromPopularMovie(MovieDetail movie) {
        String poster = movie.getPosterPath();
        ContentValues values = new ContentValues();
        if (poster != null){
            values.put(MovieContract.PopularMovieEntry.COLUMN_POSTER_PTH, poster);
        }else {
            poster = "https://i1.wp.com/vanillicon.com/872a0fb8f9b8c0ca768199c004fe8833_200.png?ssl=1";
            values.put(MovieContract.PopularMovieEntry.COLUMN_POSTER_PTH, poster);
        }
        String backDropPath = movie.getBackdropPath();
        if (backDropPath != null){
            values.put(MovieContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, backDropPath);
        }else {
            poster = "https://i1.wp.com/vanillicon.com/872a0fb8f9b8c0ca768199c004fe8833_200.png?ssl=1";
            values.put(MovieContract.PopularMovieEntry.COLUMN_BACKDROP_PATH, backDropPath);
        }
        String title = movie.getTitle();
        if (title != null){
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_NAME, title);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_NAME, "N/A");
        }

        String genres = Utility.getGenres(movie);
        if (genres != null){
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_GENRE, genres);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_GENRE, "N/A");
        }

        String releaseDate = movie.getReleaseDate();
        if (releaseDate != null){
            values.put(MovieContract.PopularMovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_RELEASE_DATE, "N/A");
        }

        double voteAverage = movie.getVoteAverage();
        if (voteAverage != 0){
            values.put(MovieContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE, "N/A");
        }

        long voteCount = movie.getVoteCount();
        if (voteCount != 0){
            values.put(MovieContract.PopularMovieEntry.COLUMN_VOTE_COUNT, voteCount);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_VOTE_COUNT, "N/A");
        }

        String overView = movie.getOverview();
        if (overView != null){
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_OVERVIEW, overView);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_OVERVIEW, "N/A");
        }

        long movieId = movie.getId();
        if (movieId != 0){
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_ID, movieId);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_ID, "N/A");
        }

        long runtime = movie.getRuntime();
        String movieRuntime = String.valueOf(runtime) + " Min";
        if (movieRuntime != null){
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_RUNTIME, movieRuntime);
        }else {
            values.put(MovieContract.PopularMovieEntry.COLUMN_MOVIE_RUNTIME, "N/A");
        }
        return values;
    }

    public interface Projection {

        String[] MOVIE_DETAILS_PROJECTION = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
                MovieContract.MovieEntry.COLUMN_MOVIE_GENRE,
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME
        };

        String[] MOVIE_LIST_PROJECTION = {
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.MovieEntry.COLUMN_POSTER_PATH,
                MovieContract.MovieEntry.COLUMN_MOVIE_NAME,
                MovieContract.MovieEntry.COLUMN_MOVIE_GENRE,
                MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME
        };

        String[] FAVOURITE_MOVIE_DETAILS_PROJECTION = {
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID,
                MovieContract.FavouriteMovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.FavouriteMovieEntry.COLUMN_POSTER_PTH,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_NAME,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_GENRE,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.FavouriteMovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RUNTIME
        };

        String[] FAVOURITE_MOVIE_LIST_PROJECTION = {
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID,
                MovieContract.FavouriteMovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.FavouriteMovieEntry.COLUMN_POSTER_PTH,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_NAME,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_GENRE,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.FavouriteMovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RUNTIME
        };

         String[] POPULAR_MOVIE_LIST_PROJECTION = {
                 MovieContract.PopularMovieEntry.COLUMN_MOVIE_ID,
                 MovieContract.PopularMovieEntry.COLUMN_BACKDROP_PATH,
                 MovieContract.PopularMovieEntry.COLUMN_POSTER_PTH,
                 MovieContract.PopularMovieEntry.COLUMN_MOVIE_NAME,
                 MovieContract.PopularMovieEntry.COLUMN_MOVIE_GENRE,
                 MovieContract.PopularMovieEntry.COLUMN_MOVIE_OVERVIEW,
                 MovieContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE,
                 MovieContract.PopularMovieEntry.COLUMN_RELEASE_DATE,
                 MovieContract.PopularMovieEntry.COLUMN_VOTE_COUNT,
                 MovieContract.PopularMovieEntry.COLUMN_MOVIE_RUNTIME
        };

        String [] POPULAR_MOVIE_DETAIL_PROJECTION = {
                MovieContract.PopularMovieEntry.COLUMN_MOVIE_ID,
                MovieContract.PopularMovieEntry.COLUMN_BACKDROP_PATH,
                MovieContract.PopularMovieEntry.COLUMN_POSTER_PTH,
                MovieContract.PopularMovieEntry.COLUMN_MOVIE_NAME,
                MovieContract.PopularMovieEntry.COLUMN_MOVIE_GENRE,
                MovieContract.PopularMovieEntry.COLUMN_MOVIE_OVERVIEW,
                MovieContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE,
                MovieContract.PopularMovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.PopularMovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.PopularMovieEntry.COLUMN_MOVIE_RUNTIME
        };

    }
}
