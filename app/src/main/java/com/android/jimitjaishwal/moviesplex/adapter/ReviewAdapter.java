package com.android.jimitjaishwal.moviesplex.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.jimitjaishwal.moviesplex.MovieUtility.ExpandableTextView;
import com.android.jimitjaishwal.moviesplex.R;
import com.android.jimitjaishwal.moviesplex.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by JIMIT JAISHWAL on 11-03-2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private List<ReviewModel> reviewList;
    private Context mContext;

    public ReviewAdapter(Context mContext) {
        this.mContext = mContext;
        reviewList = new ArrayList<>();
    }

    public void addAll(ArrayList<ReviewModel> reviews) {
        for (ReviewModel review : reviews) {
            reviewList.add(review);
        }
        notifyDataSetChanged();
    }

    public void add(ReviewModel model) {
        reviewList.add(model);
        notifyDataSetChanged();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.rewiews_row_item, parent, false);
        return new ReviewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        ReviewModel review = reviewList.get(position);
        holder.anchorText.setText(review.getAuthor());
        holder.anchorContent.setText(Html.fromHtml(review.getContent()));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_anchor)
        TextView anchorText;

        @BindView(R.id.anchor_content)
        ExpandableTextView anchorContent;

        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
