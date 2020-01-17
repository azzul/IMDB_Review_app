package com.example.favoriteapplication;

import android.database.Cursor;


import com.example.favoriteapplication.entity.FavoriteData;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

import static com.example.favoriteapplication.db.DatabaseContract.MovieColumns.DATE;
import static com.example.favoriteapplication.db.DatabaseContract.MovieColumns.LANGUAGE;
import static com.example.favoriteapplication.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.favoriteapplication.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.favoriteapplication.db.DatabaseContract.MovieColumns.TITLE;
import static com.example.favoriteapplication.db.DatabaseContract.MovieColumns.TYPE;

class MappingHelper {
    static ArrayList<FavoriteData> mapCursorToArrayList(Cursor favoriteCursor) {
        ArrayList<FavoriteData> movieItems = new ArrayList<>();

        while (favoriteCursor.moveToNext()) {
            int id = favoriteCursor.getInt(favoriteCursor.getColumnIndexOrThrow(_ID));
            String title = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(TITLE));
            String overview = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(OVERVIEW));
            String date = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(DATE));
            String language = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(LANGUAGE));
            String poster = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(POSTER));
            String type = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow(TYPE));
            movieItems.add(new FavoriteData(id, title, overview, date,language, poster, type));
        }

        return movieItems;
    }
}
