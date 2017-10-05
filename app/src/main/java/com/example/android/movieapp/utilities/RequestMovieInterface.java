package com.example.android.movieapp.utilities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by medo on 03-Sep-17.
 */

public interface RequestMovieInterface {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apikey);

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apikey);

    @GET("movie/{id}")
    Call<MovieShape> getMovieById(@Path("id") int id, @Query("api_key") String apikey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerResponse> getMovieVideo(@Path("id") int id, @Query("api_key") String apikey);

    @GET("movie/{id}/images")
    Call<MovieImagesResponse> getMovieImages(@Path("id") int id, @Query("api_key") String apikey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apikey);


}
