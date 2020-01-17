package com.example.favoriteapplication.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.favoriteapplication.db.DatabaseContract;

import java.io.Serializable;

import static com.example.favoriteapplication.db.DatabaseContract.getColumnInt;
import static com.example.favoriteapplication.db.DatabaseContract.getColumnString;


public class FavoriteData implements Serializable, Parcelable{

    private
    int id;


    private
    String judul;


    private
    String deskripsi;

    private
    String release;


    private
    String bahasa;


    private
    String path;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    private
    String type;
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getJudul() {
        return judul;
    }

    public void setJudul( String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBahasa() {
        return bahasa;
    }

    public void setBahasa(String bahasa) {
        this.bahasa = bahasa;
    }




    @Override
    public int describeContents() {
        return 0;
    }
    public FavoriteData() {
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.judul);
        dest.writeString(this.deskripsi);
        dest.writeString(this.release);
        dest.writeString(this.bahasa);
        dest.writeString(this.path);
        dest.writeString(this.type);
    }
    private FavoriteData(Parcel in) {
        this.id = in.readInt();
        this.judul = in.readString();
        this.deskripsi = in.readString();
        this.release = in.readString();
        this.bahasa = in.readString();
        this.path = in.readString();
        this.type = in.readString();
    }

    public static final Creator<FavoriteData> CREATOR = new Creator<FavoriteData>() {
        @Override
        public FavoriteData createFromParcel(Parcel source) {
            return new FavoriteData(source);
        }

        @Override
        public FavoriteData[] newArray(int size) {
            return new FavoriteData[size];
        }
    };
    public FavoriteData(int id, String title, String overview, String releaseDate, String bahasa, String posterPath, String type) {
        this.id = id;
        this.judul = title;
        this.deskripsi = overview;
        this.release = releaseDate;
        this.bahasa = bahasa;
        this.path = posterPath;
        this.type = type;
    }

    public FavoriteData(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.MovieColumns._ID);
        this.judul = getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.release = getColumnString(cursor, DatabaseContract.MovieColumns.DATE);
        this.deskripsi= getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.path = getColumnString(cursor, DatabaseContract.MovieColumns.POSTER);
        this.bahasa = getColumnString(cursor, DatabaseContract.MovieColumns.LANGUAGE);
        this.type = getColumnString(cursor, DatabaseContract.MovieColumns.TYPE);
    }
}
