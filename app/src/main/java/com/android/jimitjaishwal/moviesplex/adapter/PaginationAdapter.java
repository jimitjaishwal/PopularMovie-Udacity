package com.android.jimitjaishwal.moviesplex.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
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
 * Created by JIMIT JAISHWAL on 19-03-2017.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private MovieAdapter.OnItemSelected onItemSelected;
    private Cursor cursor;

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private boolean isLoadingAdded = false;

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

    public PaginationAdapter(Context mContext, MovieAdapter.OnItemSelected onItemSelected) {
        this.mContext = mContext;
        this.onItemSelected = onItemSelected;
    }

    public void removeFooter() {
        isLoadingAdded = false;
        // swapCursor(null);
    }

    public void addFooter() {
        isLoadingAdded = true;
       /* Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                notifyItemInserted(cursor.getCount() - 1);
            }
        };

        handler.post(r);*/
    }

    public void clearAdapter() {
        swapCursor(null);
        notifyDataSetChanged();
    }

   /* @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item_grid, parent, false);
        return new PaginationAdapter.PaginationHolder(view);
    }*/


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.movie_item_grid, parent, false);
        viewHolder = new PaginationHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holders, final int position) {
        cursor.moveToPosition(position);

        switch (getItemViewType(position)) {

            case ITEM:
                final PaginationHolder holder = (PaginationHolder) holders;
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
                break;

            case LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == cursor.getCount() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

   /* @Override
    public void onBindViewHolder(final MovieAdapter.MovieHolder holder, final int position) {
        cursor.moveToPosition(position);


    }*/

    private void insertData(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        mContext.getContentResolver().insert(MovieContract.FavouriteMovieEntry.BASE_URI, MovieProjection.getContentValue(cursor));
    }

    private void deleteMovies(String movieId, PaginationHolder holder) {
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

   /* @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;
        return cursor.getCount();
    }*/

    class PaginationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

        PaginationHolder(View view) {
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

    class LoadingVH extends RecyclerView.ViewHolder {


        public LoadingVH(View itemView) {
            super(itemView);
        }
    }
}