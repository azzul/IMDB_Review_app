package com.example.finalsubutama.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieClient {
    private static Retrofit getMovieClient(){
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/discover/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static BaseApiService getApiService(){
        return getMovieClient().create(BaseApiService.class);
    }

    private static Retrofit getMovieSearch(){
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static BaseApiService getSearchService(){
        return getMovieSearch().create(BaseApiService.class);
    }
    private static Retrofit getReleaseMovie(){
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/discover/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static BaseApiService getReleaseToday(){
        return getReleaseMovie().create(BaseApiService.class);
    }

    private static Retrofit getShowSearch(){
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
    public static BaseApiService getSearchServiceShow() {
        return getShowSearch().create(BaseApiService.class);
    }
}
