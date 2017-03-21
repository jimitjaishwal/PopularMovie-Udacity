package com.android.jimitjaishwal.moviesplex.networkservice;

import android.util.Log;

import com.android.jimitjaishwal.moviesplex.models.MovieDetail;
import com.android.jimitjaishwal.moviesplex.models.MovieModel;
import com.android.jimitjaishwal.moviesplex.models.MovieResponse;
import com.android.jimitjaishwal.moviesplex.models.ReviewModel;
import com.android.jimitjaishwal.moviesplex.models.TrailerModel;
import com.android.jimitjaishwal.moviesplex.models.TrailerResponse;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by JIMIT JAISHWAL on 25-02-2017.
 */

public class NetworkService {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    private static MovieService mMovieService;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mMovieService = retrofit.create(MovieService.class);
    }

    public static ArrayList<MovieModel> getMovieList(String sortBy, String api_key, String page) throws IOException {
        Call<MovieResponse> call = mMovieService.getMovieList("popular", api_key, page);
        Log.e(TAG, "Log Url " + call.request().url());
        Response<MovieResponse> response = call.execute();
        MovieResponse movieResponse = response.body();
        return movieResponse.getResults();
    }

    public static ArrayList<TrailerModel> getTrailerList(String movieId, String api_key) throws IOException {
        Call<TrailerResponse> call = mMovieService.getMovieTrailers(movieId, api_key);
        Response<TrailerResponse> response = call.execute();
        TrailerResponse trailerResponse = response.body();
        return trailerResponse.getResults();
    }

    public static MovieDetail getMovieDetails(String movie_id, String api_key) throws IOException {
        Call<MovieDetail> call = mMovieService.getMovieDetail(movie_id, api_key);
        Response<MovieDetail> response = call.execute();
        return response.body();
    }

    public static ArrayList<ReviewModel> getMovieReviews(String movieId, String api_key) throws IOException {
        Call<ReviewModel.ReviewResponse> call = mMovieService.getMovieReviews(movieId, api_key);
        Response<ReviewModel.ReviewResponse> response = call.execute();
        return response.body().getReviewsList();
    }
}
