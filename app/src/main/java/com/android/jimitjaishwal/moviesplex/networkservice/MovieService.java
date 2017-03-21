package com.android.jimitjaishwal.moviesplex.networkservice;

import com.android.jimitjaishwal.moviesplex.models.MovieDetail;
import com.android.jimitjaishwal.moviesplex.models.MovieResponse;
import com.android.jimitjaishwal.moviesplex.models.ReviewModel;
import com.android.jimitjaishwal.moviesplex.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by JIMIT JAISHWAL on 24-02-2017.
 */

public interface MovieService {

    @GET("movie/{sort_by}?")
    Call<MovieResponse> getMovieList(@Path("sort_by") String sort_by, @Query("api_key") String api_key, @Query("page") String page);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id")String movie_id, @Query("api_key") String api_key);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("movie_id") String movie_id, @Query("api_key") String api_key);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewModel.ReviewResponse> getMovieReviews(@Path("movie_id") String movieId, @Query("api_key") String api_ley);
}
