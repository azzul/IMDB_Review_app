package com.example.finalsubutama.tvshow;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.finalsubutama.service.BaseApiService;
import com.example.finalsubutama.service.MovieClient;
import com.example.finalsubutama.tvshow.model_tvshow.ResponseShow;
import com.example.finalsubutama.tvshow.model_tvshow.ResultsItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class ShowViewModel extends ViewModel {
    private MutableLiveData<List<ResultsItem>> listData = new MutableLiveData<>();
    private final BaseApiService apiService = MovieClient.getApiService();
    private final BaseApiService showService = MovieClient.getSearchServiceShow();

    void setShowData() {
        final Call<ResponseShow> responseShowCall = apiService.getShow();
        responseShowCall.enqueue(new Callback<ResponseShow>() {
            @Override
            public void onResponse(Call<ResponseShow> call, Response<ResponseShow> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    listData.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<ResponseShow> call, Throwable t) {
            }
        });

    }


    void getSearchShow(String strings){
        final Call<ResponseShow> responseShowCall = showService.getSearchShow(strings);
        responseShowCall.enqueue(new Callback<ResponseShow>() {
            @Override
            public void onResponse(@NonNull Call<ResponseShow> call, @NonNull Response<ResponseShow> response) {
                if (response.isSuccessful()){
                    assert response.body() != null;
                    listData.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseShow> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }



    MutableLiveData<List<ResultsItem>> getTVShow() {
        return listData;
    }

}
