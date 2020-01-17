package com.example.finalsubutama.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.finalsubutama.db.FavoriteHelper;

import java.sql.SQLException;
import java.util.Objects;

import static com.example.finalsubutama.db.DatabaseContract.AUTHORITY;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.finalsubutama.db.DatabaseContract.TABLE_FAVORITE;

public class MovieProvider extends ContentProvider {
    private static final int FAVOURITE = 1;
    private static final int FAVOURITE_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteHelper favoriteHelper;

    static {

        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE, FAVOURITE);

        sUriMatcher.addURI(AUTHORITY, TABLE_FAVORITE + "/#", FAVOURITE_ID);
    }

    @Override
    public boolean onCreate() {
        favoriteHelper = FavoriteHelper.getInstance(getContext());
        return false;
    }

    @Nullable

    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        try {
            favoriteHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITE:
                cursor = favoriteHelper.queryProvider();
                break;
            case FAVOURITE_ID:
                cursor = favoriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        try {
            favoriteHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long added;
        if (sUriMatcher.match(uri) == FAVOURITE) {
            added = favoriteHelper.insertProvider(values);
        } else {
            added = 0;
        }

        if (added > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        }

        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            favoriteHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int deleted;
        if (sUriMatcher.match(uri) == FAVOURITE_ID) {
            deleted = favoriteHelper.deleteProvider(uri.getLastPathSegment());
        } else {
            deleted = 0;
        }

        if (deleted > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        try {
            favoriteHelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int updated;
        if (sUriMatcher.match(uri) == FAVOURITE) {
            updated = favoriteHelper.updateProvider(uri.getLastPathSegment(), values);
        } else {
            updated = 0;
        }

        if (updated > 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(CONTENT_URI, null);
        }

        return updated;
    }
}
