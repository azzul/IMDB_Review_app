package com.example.finalsubutama.tvshow;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.finalsubutama.R;
import com.example.finalsubutama.tvshow.model_tvshow.ResultsItem;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowsFragment extends Fragment {
    Context context;
    AdapterShow adapter;
    RecyclerView rvShow;
    SwipeRefreshLayout swipeShow;
    LinearLayout layError;
    ShowViewModel showViewModel;

    public ShowsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_shows, container, false);
        context = getActivity();

        showViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ShowViewModel.class);
        showViewModel.getTVShow().observe(this, getShow);

        adapter = new AdapterShow(context);
        adapter.notifyDataSetChanged();

        rvShow = view.findViewById(R.id.rv_shows);
        rvShow.setLayoutManager(new LinearLayoutManager(getContext()));
        rvShow.setAdapter(adapter);

        showViewModel.setShowData();

        swipeShow = view.findViewById(R.id.srShow);
        layError = view.findViewById(R.id.layError);
        swipeShow.setRefreshing(true);
        //getShow();
        swipeShow.setOnRefreshListener(() -> {
            swipeShow.setRefreshing(true);
            showViewModel.setShowData();
        });

        return view;
    }

    private Observer<List<ResultsItem>> getShow = new Observer<List<ResultsItem>>() {
        @Override
        public void onChanged(List<ResultsItem> lisShow) {
            if (lisShow != null) {
                adapter.setShowItem(lisShow);
                swipeShow.setRefreshing(false);
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
                swipeShow.setRefreshing(true);
                showViewModel.setShowData();
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
                showViewModel.getSearchShow(s);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

}