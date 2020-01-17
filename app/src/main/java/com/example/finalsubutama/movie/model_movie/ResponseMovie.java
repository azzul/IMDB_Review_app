package com.example.finalsubutama.movie.model_movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMovie{

	@SerializedName("results")
	private List<ResultsShow> results;



	public void setResults(List<ResultsShow> results){
		this.results = results;
	}

	public List<ResultsShow> getResults(){
		return results;
	}

}