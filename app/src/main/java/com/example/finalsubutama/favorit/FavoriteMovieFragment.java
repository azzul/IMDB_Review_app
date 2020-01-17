package com.example.finalsubutama.favorit;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.example.finalsubutama.DetailActivity;
import com.example.finalsubutama.R;
import com.example.finalsubutama.db.FavoriteHelper;
import com.example.finalsubutama.entity.FavoriteData;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment implements LoadFavoriteCallback {
    private ProgressBar pgFavMovie;
    private FavoriteHelper movieHelper;
    private FavoriteMovieAdapter favoriteMovieAdapter;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static final String EXTRA_KEY = "KEY";
    private static final String EXTRA_TYPE = "TYPE";
    private static final String EXTRA_FAVORITE = "FAVORITE";

    public FavoriteMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return getView() != null ? getView() :
                inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvFavMovie = view.findViewById(R.id.rv_favoritedua);
        rvFavMovie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFavMovie.setHasFixedSize(true);
        pgFavMovie = view.findViewById(R.id.pg_fav_movie);

        movieHelper = FavoriteHelper.getInstance(getActivity());
        try {
            movieHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        favoriteMovieAdapter = new FavoriteMovieAdapter(getActivity());
        rvFavMovie.setAdapter(favoriteMovieAdapter);

        favoriteMovieAdapter.setOnItemClickCallback(data -> showSelectedMovie(data));

        if (savedInstanceState == null) {
            Log.d("favoritemovie", "onViewCreated: saved instance kosong");
            new LoadFavoriteAsync(movieHelper, this).execute();
        } else {
            Log.d("favoritemovie", "onViewCreated: saved instance ada");
            ArrayList<FavoriteData> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                favoriteMovieAdapter.setListMovies(list);
            }
        }
    }

    private void showSelectedMovie(FavoriteData movie) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(EXTRA_KEY, (Parcelable) movie);
        intent.putExtra(EXTRA_TYPE, EXTRA_FAVORITE);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, favoriteMovieAdapter.getListMovies());
    }

    @Override
    public void preExecute() {
        new Runnable() {
            @Override
            public void run() {
                pgFavMovie.setVisibility(View.VISIBLE);
            }
        };
        Log.d("favoritemovie", "preExecute: masuk");
    }

    @Override
    public void postExecute(ArrayList<FavoriteData> movieItems) {
        pgFavMovie.setVisibility(View.INVISIBLE);
        favoriteMovieAdapter.setListMovies(movieItems);
        Log.d("favoritemovie", "postExecute: " + movieItems.toString());
    }

    private class LoadFavoriteAsync extends AsyncTask<Void, Void, ArrayList<FavoriteData>> {
        private final WeakReference<FavoriteHelper> movieHelperWeakReference;
        private final WeakReference<LoadFavoriteCallback> moviesCallbackWeakReference;

        LoadFavoriteAsync(FavoriteHelper movieHelper, FavoriteMovieFragment loadMoviesCallback) {
            movieHelperWeakReference = new WeakReference<>(movieHelper);
            moviesCallbackWeakReference = new WeakReference<>(loadMoviesCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            moviesCallbackWeakReference.get().preExecute();
        }

        @Override
        protected ArrayList<FavoriteData> doInBackground(Void... voids) {
            return movieHelperWeakReference.get().getAllFavorite();
        }

        @Override
        protected void onPostExecute(ArrayList<FavoriteData> movieItems) {
            super.onPostExecute(movieItems);

            moviesCallbackWeakReference.get().postExecute(movieItems);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        movieHelper.close();
    }
}

