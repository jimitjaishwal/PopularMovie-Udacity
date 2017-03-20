package com.android.jimitjaishwal.moviesplex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by JIMIT JAISHWAL on 05-03-2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_MOVIE_TABLE = "CREATE TABLE "
                + MovieContract.MovieEntry.COLUMN_TABLE_NAME + " ("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_GENRE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL , "
                + MovieContract.MovieEntry.COLUMN_MOVIE_RUNTIME + " TEXT NOT NULL, "
                + "UNIQUE ( " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String CREATE_FAVOURITE_MOVIE = " CREATE TABLE "
                + MovieContract.FavouriteMovieEntry.COLUMN_TABLE_NAME + " ( "
                + MovieContract.FavouriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_POSTER_PTH + " TEXT NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_GENRE + " TEXT NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
                + MovieContract.FavouriteMovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL , "
                + MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_RUNTIME + " TEXT NOT NULL, "
                + "UNIQUE ( " + MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String CREATE_POPULAR_MOVIE = " CREATE TABLE "
                + MovieContract.PopularMovieEntry.COLUMN_TABLE_NAME + " ( "
                + MovieContract.PopularMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_POSTER_PTH + " TEXT NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_MOVIE_GENRE + " TEXT NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, "
                + MovieContract.PopularMovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL , "
                + MovieContract.PopularMovieEntry.COLUMN_MOVIE_RUNTIME + " TEXT NOT NULL, "
                + "UNIQUE ( " + MovieContract.PopularMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(CREATE_MOVIE_TABLE);
        db.execSQL(CREATE_FAVOURITE_MOVIE);
        db.execSQL(CREATE_POPULAR_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + MovieContract.MovieEntry.COLUMN_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + MovieContract.FavouriteMovieEntry.COLUMN_TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + MovieContract.PopularMovieEntry.COLUMN_TABLE_NAME);
        onCreate(db);
    }
}