package com.example.finalsubutama.movie;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.finalsubutama.movie.model_movie.ResponseMovie;
import com.example.finalsubutama.movie.model_movie.ResultsShow;
import com.example.finalsubutama.service.BaseApiService;
import com.example.finalsubutama.service.MovieClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MovieViewModel extends ViewModel {
    private MutableLiveData<List<ResultsShow>> listData = new MutableLiveData<>();
    private BaseApiService apiService = MovieClient.getApiService();
    private BaseApiService apiSearchService = MovieClient.getSearchService();

    void setMoivieData(){
        Call<ResponseMovie> getData = apiService.getMovie();
        getData.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    listData.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
    void searchMovieData(String textCari){
        Call<ResponseMovie> getSearch = apiSearchService.searchMovie(textCari);
        getSearch.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(@NonNull Call<ResponseMovie> call, @NonNull Response<ResponseMovie> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    listData.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseMovie> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    MutableLiveData<List<ResultsShow>> getMovie(){
        return listData;
    }

}
