package com.example.finalsubutama.release;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseRelease {

    @SerializedName("results")
    private List<ReleaseShow> results;


    public ResponseRelease(List<ReleaseShow> movies) {
        this.results = movies;
    }

    List<ReleaseShow> getResults(){
        return results;
    }

}
