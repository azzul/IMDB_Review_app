package com.example.finalsubutama.movie;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.finalsubutama.R;
import com.example.finalsubutama.movie.model_movie.ResponseMovie;
import com.example.finalsubutama.movie.model_movie.ResultsShow;
import com.example.finalsubutama.service.BaseApiService;
import com.example.finalsubutama.service.MovieClient;

import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    Context context;
    RecyclerView rvMain;
    SwipeRefreshLayout swipeMovie;
    LinearLayout layError;
    MovieAdapter adapter;
    MovieViewModel movieViewModel;

    private BaseApiService apiService = MovieClient.getApiService();

    public MoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        context = getActivity();




        movieViewModel =new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
        movieViewModel.getMovie().observe(this, getMovie);


        adapter = new MovieAdapter(context);
        rvMain = view.findViewById(R.id.rv_movies);
        rvMain.setAdapter(adapter);
        movieViewModel.setMoivieData();

        swipeMovie = view.findViewById(R.id.srMain);
        swipeMovie.setRefreshing(true);
        layError = view.findViewById(R.id.layError);
        rvMain.setLayoutManager(new LinearLayoutManager(getContext()));

        swipeMovie.setOnRefreshListener(() -> {
            swipeMovie.setRefreshing(true);
            movieViewModel.setMoivieData();
        });

        return view;
    }

    private Observer<List<ResultsShow>> getMovie = new Observer<List<ResultsShow>>() {
        @Override
        public void onChanged(@Nullable List<ResultsShow> resultsItems) {
            if (resultsItems != null) {
                adapter.setMovieList(resultsItems);
                swipeMovie.setRefreshing(false);
            }
        }
    };



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                                           @Override
                                           public boolean onMenuItemActionExpand(MenuItem item) {
                                               return true;
                                           }

                                           @Override
                                           public boolean onMenuItemActionCollapse(MenuItem item) {
                                               swipeMovie.setRefreshing(true);
                                               movieViewModel.setMoivieData();
                                               return true;
                                           }
                                       });
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        movieViewModel.searchMovieData(s);
                        return true;
                    }
                });
        super.onCreateOptionsMenu(menu,inflater);
    }

}
