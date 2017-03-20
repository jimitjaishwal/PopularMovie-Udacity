package com.android.jimitjaishwal.moviesplex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.jimitjaishwal.moviesplex.R;
import com.android.jimitjaishwal.moviesplex.models.TrailerModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIMIT JAISHWAL on 03-03-2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private Context mContext;
    private List<TrailerModel> trailerList;
    private OnMovieSelected onItemSelected;

    public TrailerAdapter(Context mContext, OnMovieSelected onItemSelected) {
        this.mContext = mContext;
        this.trailerList = new ArrayList<>();
        this.onItemSelected = onItemSelected;
    }

    public void addAll(List<TrailerModel> trailers) {
        for (TrailerModel trailer : trailers) {
            add(trailer);
        }
        notifyDataSetChanged();
    }

    public void add(TrailerModel trailer) {
        trailerList.add(trailer);
        notifyDataSetChanged();
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_item_rows, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        TrailerModel movieTrailer = trailerList.get(position);
        String key = movieTrailer.getKey();
        String thumbnailImage = "https://i1.ytimg.com/vi/" + key + "/mqdefault.jpg";
        Glide.with(mContext).load(thumbnailImage).into(holder.trailerThumbnail);
        Log.e("Trailer Adapter", "Trailer Name: " + movieTrailer.getName());
    }

    public interface OnMovieSelected {

        void onVideoSelected(String videoUrl);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailer_thumbnail)
         ImageView trailerThumbnail;

        public TrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String videoUrl = trailerList.get(position).getKey();
            onItemSelected.onVideoSelected(videoUrl);
        }
    }
}
