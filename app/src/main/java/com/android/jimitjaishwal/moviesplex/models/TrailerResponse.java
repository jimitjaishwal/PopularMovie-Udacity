
package com.android.jimitjaishwal.moviesplex.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class TrailerResponse {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private ArrayList<TrailerModel> mTrailerModels;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public ArrayList<TrailerModel> getResults() {
        return mTrailerModels;
    }

    public void setResults(ArrayList<TrailerModel> trailerModels) {
        mTrailerModels = trailerModels;
    }

}
