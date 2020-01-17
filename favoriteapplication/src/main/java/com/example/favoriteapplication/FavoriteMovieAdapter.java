package com.example.favoriteapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.favoriteapplication.entity.FavoriteData;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.Holder> {

    private ArrayList<FavoriteData> listData = new ArrayList<>();
    private Context context;
    private OnItemClickCallback onItemClickCallback;

    public ArrayList<FavoriteData> getListMovies() {
        return listData;
    }

    FavoriteMovieAdapter(Context context) {
        this.context= context;
    }

    void setListMovies(ArrayList<FavoriteData> listMovie) {

        if (listMovie.size() > 0) {
            this.listData.clear();
        }
        this.listData.addAll(listMovie);

        notifyDataSetChanged();
    }


    void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycleview_favorite, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        holder.titleFavMovie.setText(listData.get(i).getJudul());
        holder.yearFavMovie.setText(listData.get(i).getRelease());
        holder.type.setText(listData.get(i).getType());
        String baseUrlImage = "https://image.tmdb.org/t/p/original";
        Glide.with(context).load(baseUrlImage + listData.get(i).getPath())
                .into(holder.posterFavMovie);

        holder.itemView.setOnClickListener(v ->
                onItemClickCallback.onItemClicked(listData.get(holder.getAdapterPosition())));


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView titleFavMovie, yearFavMovie, type;
        ImageView posterFavMovie;
        Holder(@NonNull View itemView) {
            super(itemView);

            titleFavMovie = itemView.findViewById(R.id.tv_favorite_name);
            yearFavMovie = itemView.findViewById(R.id.tv_item_release);
            type = itemView.findViewById(R.id.tv_favorite_type);
            posterFavMovie = itemView.findViewById(R.id.img_item_favorite);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(FavoriteData data);
    }
}
