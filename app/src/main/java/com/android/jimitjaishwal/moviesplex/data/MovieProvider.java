package com.android.jimitjaishwal.moviesplex.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by JIMIT JAISHWAL on 06-03-2017.
 */

public class MovieProvider extends ContentProvider {

    private MovieDbHelper mOpenHelper;
    private UriMatcher sUriMatcher = buildUriMatcher();
    private static final int MOVIES = 100;
    public static final int MOVIES_WITH_ID = 101;
    public static final int FAVOURITE_MOVIE = 200;
    public static final int FAVOURITE_MOVIE_WITH_ID = 201;
    public static final int POPULAR_MOVIE = 300;
    public static final int POPULAR_MOVIE_WITH_ID = 301;

    private UriMatcher buildUriMatcher() {
        String AUTHORITY = MovieContract.AUTHORITY;
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, MovieContract.MOVIE_PATH, MOVIES);
        uriMatcher.addURI(AUTHORITY, MovieContract.MOVIE_PATH + "/*", MOVIES_WITH_ID);
        uriMatcher.addURI(AUTHORITY, MovieContract.FAVOURITE_MOVIE_PATH, FAVOURITE_MOVIE);
        uriMatcher.addURI(AUTHORITY, MovieContract.FAVOURITE_MOVIE_PATH + "/*", FAVOURITE_MOVIE_WITH_ID);
        uriMatcher.addURI(AUTHORITY, MovieContract.POPULAR_MOVIE_PATH, POPULAR_MOVIE);
        uriMatcher.addURI(AUTHORITY, MovieContract.POPULAR_MOVIE_PATH + "/*", POPULAR_MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    public Cursor getMovieDetailsFromCursor(Uri uri, String[] projection, String sortOrder) {
        String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.COLUMN_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public Cursor getFavouriteMovieDetailsFromCursor(Uri uri, String[] projection, String sortOrder) {
        String selection = MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.FavouriteMovieEntry.COLUMN_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    public Cursor getPopularMovieDetailsFromCursor(Uri uri, String[] projection, String sortOrder) {

        String selection = MovieContract.PopularMovieEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.PopularMovieEntry.COLUMN_TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int matcher = sUriMatcher.match(uri);
        Cursor reCursor;

        switch (matcher) {

            case MOVIES: {
                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.MovieEntry.COLUMN_TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }

            case MOVIES_WITH_ID: {

                reCursor = getMovieDetailsFromCursor(uri, projection, sortOrder);
                break;
            }

            case FAVOURITE_MOVIE: {
                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.FavouriteMovieEntry.COLUMN_TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }

            case FAVOURITE_MOVIE_WITH_ID: {
                reCursor = getFavouriteMovieDetailsFromCursor(uri, projection, sortOrder);
                break;
            }

            case POPULAR_MOVIE: {
                reCursor = mOpenHelper.getReadableDatabase()
                        .query(MovieContract.PopularMovieEntry.COLUMN_TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            }

            case POPULAR_MOVIE_WITH_ID: {
                reCursor = getPopularMovieDetailsFromCursor(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        reCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return reCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;

            case MOVIES_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            case FAVOURITE_MOVIE:
                return MovieContract.FavouriteMovieEntry.CONTENT_DIR_TYPE;

            case FAVOURITE_MOVIE_WITH_ID:
                return MovieContract.FavouriteMovieEntry.CONTENT_ITEM_TYPE;

            case POPULAR_MOVIE:
                return MovieContract.PopularMovieEntry.CONTENT_DIR_TYPE;

            case POPULAR_MOVIE_WITH_ID:
                return MovieContract.PopularMovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int matcher = sUriMatcher.match(uri);
        Uri returnUri;

        switch (matcher) {

            case MOVIES: {
                long movieId = db.insert(MovieContract.MovieEntry.COLUMN_TABLE_NAME, null, values);
                if (movieId > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(movieId);
                else
                    throw new android.database.SQLException("Failed To insert Rows " + uri);
                break;
            }

            case FAVOURITE_MOVIE: {
                long movieId = db.insert(MovieContract.FavouriteMovieEntry.COLUMN_TABLE_NAME, null, values);
                if (movieId > 0)
                    returnUri = MovieContract.FavouriteMovieEntry.buildFavouriteMovieUri(movieId);
                else
                    throw new android.database.SQLException("Failed To insert Rows " + uri);
                break;
            }

            case POPULAR_MOVIE: {
                long movieId = db.insert(MovieContract.PopularMovieEntry.COLUMN_TABLE_NAME, null, values);
                if (movieId > 0)
                    returnUri = MovieContract.PopularMovieEntry.buildPopularMovieUri(movieId);
                else
                    throw new android.database.SQLException("Failed To insert Rows " + uri);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int deletedRows;
        int matcher = sUriMatcher.match(uri);

        if (selection == null)
            selection = "1";

        switch (matcher) {

            case MOVIES: {
                deletedRows = db.delete(MovieContract.MovieEntry.COLUMN_TABLE_NAME, selection, selectionArgs);
                break;
            }

            case FAVOURITE_MOVIE: {
                deletedRows = db.delete(MovieContract.FavouriteMovieEntry.COLUMN_TABLE_NAME, selection, selectionArgs);
                break;
            }

            case POPULAR_MOVIE: {
                deletedRows = db.delete(MovieContract.PopularMovieEntry.COLUMN_TABLE_NAME, selection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        if (deletedRows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int matcher = sUriMatcher.match(uri);

        switch (matcher) {

            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;

                try {
                    for (ContentValues value : values) {
                        long id = db.insert(MovieContract.MovieEntry.COLUMN_TABLE_NAME, null, value);
                        if (id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
        mOpenHelper.close();
    }
}
