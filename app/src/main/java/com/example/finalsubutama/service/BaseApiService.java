package com.example.finalsubutama.service;

import com.example.finalsubutama.Utils;
import com.example.finalsubutama.movie.model_movie.ResponseMovie;
import com.example.finalsubutama.release.ResponseRelease;
import com.example.finalsubutama.tvshow.model_tvshow.ResponseShow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface BaseApiService {

    @GET("movie?api_key="+ Utils.API_KEY +"&language=en-US")
    Call<ResponseMovie> getMovie();

    @GET("tv?api_key="+ Utils.API_KEY +"&language=en-US")
    Call<ResponseShow> getShow();


    @GET("movie?api_key="+ Utils.API_KEY +"&language=en-US")
    Call<ResponseMovie> searchMovie(@Query("query") String search);

    @GET("movie?api_key="+ Utils.API_KEY)
    Call<ResponseRelease> releaseMovie(@Query("primary_release_date.gte") String date,@Query("primary_release_date.lte") String date2);

    @GET("tv?api_key="+ Utils.API_KEY +"&language=en-US")
    Call<ResponseShow> getSearchShow(@Query("query") String strings);
}
