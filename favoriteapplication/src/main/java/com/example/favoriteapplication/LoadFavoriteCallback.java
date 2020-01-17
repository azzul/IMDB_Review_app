package com.example.favoriteapplication;

import android.database.Cursor;

public interface LoadFavoriteCallback {
    void postExecute(Cursor movies);
}
