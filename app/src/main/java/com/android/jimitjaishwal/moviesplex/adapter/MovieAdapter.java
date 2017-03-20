package com.android.jimitjaishwal.moviesplex.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jimitjaishwal.moviesplex.R;
import com.android.jimitjaishwal.moviesplex.data.MovieContract;
import com.android.jimitjaishwal.moviesplex.data.MovieProjection;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIMIT JAISHWAL on 24-02-2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Context mContext;
    private OnItemSelected onItemSelected;
    private Cursor cursor;

    public void swapCursor(final Cursor cursor) {
        // isLoadingAdded = true;
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void loadMore(Cursor mCursor, RecyclerView view){
        this.cursor = mCursor;
        view.post(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeInserted(getItemCount(),cursor.getCount()-1);
            }
        });
    }

    public MovieAdapter(Context mContext, OnItemSelected onItemSelected) {
        this.mContext = mContext;
        this.onItemSelected = onItemSelected;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_grid, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieHolder holder, final int position) {
        cursor.moveToPosition(position);

        String url = cursor.getString(MovieProjection.INDEX_POSTER_PATH);
        String imageUrl = "http://image.tmdb.org/t/p/w342" + url;

        Glide.with(mContext)
                .load(imageUrl)
                .dontAnimate()
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
                        Palette palette = Palette.generate(bitmap);
                        int defaultColor = 0xFF333333;
                        int color = palette.getDarkMutedColor(defaultColor);
                        holder.contentBackGround.setBackgroundColor(color);
                        return false;
                    }
                })
                .into(holder.moviePoster);

        String title = cursor.getString(MovieProjection.INDEX_MOVIE_NAME);
        Log.e("Fragment", "onLoadFinished: " + title);

        final String originalTitle = String.format(mContext.getResources().getString(R.string.movie_title), title);
        holder.movieTitle.setText(originalTitle);

        String genre = cursor.getString(MovieProjection.INDEX_MOVIE_GENRE);
        String genres = String.format(mContext.getResources().getString(R.string.genres), genre);
        holder.movieCategory.setText(genres);

        final String movieId = cursor.getString(MovieProjection.INDEX_MOVIE_ID);

        if (isMovieFavourite(movieId)) {
            holder.favouriteMovie.setImageResource(R.drawable.ic_favorite_white_24dp);
        } else {
            holder.favouriteMovie.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }

        holder.favouriteMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMovieFavourite(movieId)) {
                    Toast.makeText(mContext, originalTitle + " remove from Favourite!", Toast.LENGTH_SHORT).show();
                    deleteMovies(movieId, holder);
                } else {
                    Toast.makeText(mContext, originalTitle + " add Favourite!", Toast.LENGTH_SHORT).show();
                    insertData(cursor, position);
                    holder.favouriteMovie.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
            }
        });
    }

    private void insertData(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        mContext.getContentResolver().insert(MovieContract.FavouriteMovieEntry.BASE_URI, MovieProjection.getContentValue(cursor));
    }

    private void deleteMovies(String movieId, MovieHolder holder) {
        String where = MovieContract.FavouriteMovieEntry.COLUMN_MOVIE_ID + " = ?";
        int deletedRows = mContext.getContentResolver().delete(
                MovieContract.FavouriteMovieEntry.BASE_URI, where, new String[]{movieId});

        if (deletedRows > 0) {
            holder.favouriteMovie.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
        notifyItemRemoved(holder.getPosition());
    }

    private boolean isMovieFavourite(String movieId) {
        Uri mUri = MovieContract.FavouriteMovieEntry.buildFavouriteMovieDetailUri(movieId);
        Cursor queryCursor = mContext.getContentResolver().query(mUri, null, null, null, null);
        if (queryCursor != null && queryCursor.moveToFirst()) {
            queryCursor.close();
            return true;
        }
        return false;
    }


    public interface OnItemSelected {

        void onMovieSelected(String movieId, int position, View imageView);
    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        return cursor.getCount();
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.favourite_movie)
        ImageView favouriteMovie;
        @BindView(R.id.movie_category)
        TextView movieCategory;
        @BindView(R.id.movie_name)
        TextView movieTitle;
        @BindView(R.id.movie_poster)
        ImageView moviePoster;
        @BindView(R.id.content_backGround)
        LinearLayout contentBackGround;
        @BindView(R.id.card_view)
        CardView cardView;

        MovieHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            cursor.moveToPosition(position);
            String movieId = cursor.getString(MovieProjection.INDEX_MOVIE_ID);
            onItemSelected.onMovieSelected(movieId, position, v);
        }
    }
}
