package com.example.finalsubutama.favorit;


import com.example.finalsubutama.entity.FavoriteData;

import java.util.ArrayList;

public interface LoadFavoriteCallback {
    void preExecute();

    void postExecute(ArrayList<FavoriteData> movieItems);
}
