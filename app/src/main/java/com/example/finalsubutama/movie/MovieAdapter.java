package com.example.finalsubutama.movie;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.finalsubutama.DetailActivity;
import com.example.finalsubutama.R;
import com.example.finalsubutama.Utils;
import com.example.finalsubutama.movie.model_movie.ResultsShow;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {


    private List<ResultsShow> movieList = new ArrayList<>();
    private final Context context;
    private static final String EXTRA_KEY = "KEY";
    private static final String EXTRA_TYPE= "TYPE";
    private static final String EXTRA_MOVIE= "MOVIE";
    MovieAdapter(Context context) {
        this.context = context;
    }

    void setMovieList(List<ResultsShow> list){
        movieList.clear();
        movieList.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycleview_movie, viewGroup, false);
        return new MovieHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MovieAdapter.MovieHolder movieHolder, int i) {
        movieHolder.bindView(movieList.get(i));

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvRelease;
        private ImageView ivMain;
        String fotoUrl;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_movie_name);
            tvRelease = itemView.findViewById(R.id.tv_item_release);
            ivMain = itemView.findViewById(R.id.img_item_movie);

        }

        private void bindView(final ResultsShow item){
            tvTitle.setText(item.getOriginalTitle());
            tvRelease.setText(item.getReleaseDate());
            fotoUrl = Utils.BASE_IMG_URL +item.getPosterPath();
            Glide.with(context).load(fotoUrl).into(ivMain);

         itemView.setOnClickListener(v -> {
             Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
             intent.putExtra(EXTRA_KEY, item);
             intent.putExtra(EXTRA_TYPE, EXTRA_MOVIE);
             context.startActivity(intent);
         });

        }


    }
}