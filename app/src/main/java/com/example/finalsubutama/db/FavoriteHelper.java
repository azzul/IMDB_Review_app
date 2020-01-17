package com.example.finalsubutama.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finalsubutama.entity.FavoriteData;

import java.sql.SQLException;
import java.util.ArrayList;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.DATE;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.TITLE;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.TYPE;
import static com.example.finalsubutama.db.DatabaseContract.TABLE_FAVORITE;

public class FavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_FAVORITE;
    private static DatabaseHelper dataBaseHelper;
    private static FavoriteHelper INSTANCE;
    private static SQLiteDatabase database;

    private FavoriteHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<FavoriteData> getAllFavorite() {
        ArrayList<FavoriteData> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                _ID + " ASC",
                null);
        cursor.moveToFirst();
        FavoriteData favoriteData;
        if (cursor.getCount() > 0) {
            do {
                favoriteData = new FavoriteData();
                favoriteData.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                favoriteData.setJudul(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                favoriteData.setDeskripsi(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                favoriteData.setRelease(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                favoriteData.setPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTER)));
                favoriteData.setType(cursor.getString(cursor.getColumnIndexOrThrow(TYPE)));
                arrayList.add(favoriteData);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public FavoriteData getMovieById(int id) {
        Cursor cursor = database.query(
                DATABASE_TABLE,
                new String[]{_ID, TITLE, OVERVIEW, DATE, POSTER},
                _ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);

        FavoriteData favoriteItem = new FavoriteData();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            favoriteItem.setId(cursor.getColumnIndex(_ID));
            favoriteItem.setJudul(cursor.getString(cursor.getColumnIndex(TITLE)));
            favoriteItem.setRelease(cursor.getString(cursor.getColumnIndex(DATE)));
            favoriteItem.setPath(cursor.getString(cursor.getColumnIndex(POSTER)));

            cursor.close();
            return favoriteItem;
        }

        return null;
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, _ID + "=?", new String[]{id});
    }

}
