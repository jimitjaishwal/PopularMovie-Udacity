
package com.android.jimitjaishwal.moviesplex.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class MovieResponse {

    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private ArrayList<MovieModel> mMovieModels = new ArrayList<>();
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public Long getPage() {
        return mPage;
    }

    public void setPage(Long page) {
        mPage = page;
    }

    public ArrayList<MovieModel> getResults() {
        return mMovieModels;
    }

    public void setResults(ArrayList<MovieModel> movieModels) {
        mMovieModels = movieModels;
    }

    public Long getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Long totalPages) {
        mTotalPages = totalPages;
    }

    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

}
