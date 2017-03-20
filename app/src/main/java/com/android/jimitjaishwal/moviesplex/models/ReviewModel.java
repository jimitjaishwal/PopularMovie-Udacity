package com.android.jimitjaishwal.moviesplex.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by JIMIT JAISHWAL on 11-03-2017.
 */

public class ReviewModel {

    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class ReviewResponse {

        @SerializedName("id")
        private Long mId;

        @SerializedName("page")
        private int page;

        @SerializedName("results")
        private ArrayList<ReviewModel> reviewsList;

        public ArrayList<ReviewModel> getReviewsList() {
            return reviewsList;
        }

        public void setReviewsList(ArrayList<ReviewModel> reviewsList) {
            this.reviewsList = reviewsList;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public Long getmId() {
            return mId;
        }

        public void setmId(Long mId) {
            this.mId = mId;
        }
    }
}
