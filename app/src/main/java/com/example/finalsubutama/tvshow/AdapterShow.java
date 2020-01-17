package com.example.finalsubutama.tvshow;

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
import com.example.finalsubutama.tvshow.model_tvshow.ResultsItem;

import java.util.ArrayList;
import java.util.List;

public class AdapterShow extends RecyclerView.Adapter<AdapterShow.MovieHolder> {

    private List<ResultsItem> showItem = new ArrayList<>();
    private Context mContex;
    private static final String EXTRA_KEY = "KEY";
    private static final String EXTRA_TYPE= "TYPE";
    private static final String EXTRA_SHOW = "SHOW";
    AdapterShow(Context mContex) {
        this.mContex = mContex;
    }

    void setShowItem(List<ResultsItem> items) {
        showItem.clear();
        showItem.addAll(items);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AdapterShow.MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycleview_tvshow, viewGroup, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShow.MovieHolder movieHolder, int i) {
        movieHolder.bindView(showItem.get(i));

    }

    @Override
    public int getItemCount() {
        return showItem.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvReleaseShow;
        private ImageView ivMain;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_tvshow_name);
            tvReleaseShow = itemView.findViewById(R.id.tv_item_release_show);
            ivMain = itemView.findViewById(R.id.img_item_tvshow);
        }

        private void bindView(final ResultsItem items) {
            tvTitle.setText(items.getOriginalName());
            tvReleaseShow.setText(items.getFirstAirDate());
            String fotoUrl = Utils.BASE_IMG_URL + items.getPosterPath();
            Glide.with(itemView.getContext()).load(fotoUrl).into(ivMain);
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                intent.putExtra(EXTRA_KEY, items);
                intent.putExtra(EXTRA_TYPE, EXTRA_SHOW);
                mContex.startActivity(intent);
            });
        }
    }
}
