package com.example.finalsubutama;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalsubutama.db.DatabaseContract;
import com.example.finalsubutama.db.FavoriteHelper;
import com.example.finalsubutama.entity.FavoriteData;
import com.example.finalsubutama.movie.model_movie.ResultsShow;
import com.example.finalsubutama.release.ReleaseShow;
import com.example.finalsubutama.tvshow.model_tvshow.ResultsItem;
import com.example.finalsubutama.widget.FavoriteWidget;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.provider.BaseColumns._ID;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.CONTENT_URI;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.DATE;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.LANGUAGE;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.OVERVIEW;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.POSTER;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.TITLE;
import static com.example.finalsubutama.db.DatabaseContract.MovieColumns.TYPE;

public class DetailActivity extends AppCompatActivity {


    private static final String EXTRA_KEY = "KEY";
    private static final String EXTRA_TYPE= "TYPE";
    private static final String EXTRA_SHOW = "SHOW";
    private static final String EXTRA_RELEASE= "RELEASE";
    private static final String EXTRA_FAVORITE = "FAVORITE";
    private int idShow, idFav, idMovie;
    private Boolean isFavorite = false;
    private FavoriteHelper favoriteHelper;
    private Button btnfav, btnDel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDate = findViewById(R.id.tvDate);
        TextView tvDesc = findViewById(R.id.tvDesc);
        TextView tvOriginalLanguage = findViewById(R.id.tvLanguage);
        btnfav = findViewById(R.id.btnFav);
        btnDel = findViewById(R.id.btnDelete);
        ImageView ivBack = findViewById(R.id.ivCover);
        ImageView ivPoster = findViewById(R.id.ivPoster);




        String type = getIntent().getStringExtra(EXTRA_TYPE);
        switch (type) {
            case EXTRA_SHOW:
                favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
                try {
                    favoriteHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                new ResultsItem();
                ResultsItem resultsItem;
                resultsItem = getIntent().getParcelableExtra(EXTRA_KEY);
                Objects.requireNonNull(getSupportActionBar()).setTitle(resultsItem.getOriginalName());
                idShow = resultsItem.getId();
                tvTitle.setText(resultsItem.getOriginalName());
                tvDate.setText(resultsItem.getFirstAirDate());
                tvDesc.setText(resultsItem.getOverview());
                tvOriginalLanguage.setText(resultsItem.getOriginalLanguage());
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + resultsItem.getPosterPath()).into(ivPoster);
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + resultsItem.getPosterPath()).into(ivBack);

                FavoriteData favoriteShow = favoriteHelper.getMovieById(idShow);

                if (favoriteShow != null) {
                    Log.d("detailActivity", "favoriteState: data favorite ditemukan");
                    Log.d("detailActivity", "favoriteState: " + favoriteShow);

                    List<FavoriteData> movieItemList = new ArrayList<>();
                    movieItemList.add(0, favoriteShow);

                    if (movieItemList.isEmpty()) {
                        isFavorite = false;
                        btnfav.setVisibility(View.VISIBLE);
                        btnDel.setVisibility(View.INVISIBLE);

                        Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                    } else {
                        isFavorite = true;
                        btnDel.setVisibility(View.VISIBLE);
                        btnfav.setVisibility(View.INVISIBLE);
                    }
                } else {
                    isFavorite = false;
                    Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                    btnfav.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.INVISIBLE);
                }
                btnfav.setOnClickListener(v -> {
                    new ResultsItem();
                    ResultsItem resultsItem1;
                    resultsItem1 = getIntent().getParcelableExtra(EXTRA_KEY);
                    ContentValues values = new ContentValues();

                    values.put(_ID, resultsItem1.getId());
                    values.put(TITLE, resultsItem1.getOriginalName());
                    values.put(DATE, resultsItem1.getFirstAirDate());
                    values.put(OVERVIEW, resultsItem1.getOverview());
                    values.put(LANGUAGE, resultsItem1.getOriginalLanguage());
                    values.put(POSTER, resultsItem1.getPosterPath());
                    values.put(TYPE, "Tv Show");
                    Log.d("detailActivity", "Judul: " + resultsItem1.getId());
                    getContentResolver().insert(DatabaseContract.MovieColumns.CONTENT_URI, values);
                    btnDel.setVisibility(View.VISIBLE);
                    btnfav.setVisibility(View.INVISIBLE);
                    updateFavoriteWidget(this);

                    Toast.makeText(DetailActivity.this, R.string.msg_success_Favorite, Toast.LENGTH_SHORT).show();

                });
                btnDel.setOnClickListener(v -> {
                    int result = getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + idShow),
                            null,
                            null);

                    if (!(result > 0)) {
                        Toast.makeText(DetailActivity.this, R.string.failed_delete_favorite, Toast.LENGTH_SHORT).show();
                    } else {
                        updateFavoriteWidget(this);
                        Intent intentku = new Intent(this, MainActivity.class);
                        startActivity(intentku);
                        Toast.makeText(DetailActivity.this, R.string.success_delete_favorite, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case EXTRA_FAVORITE: {
                favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
                try {
                    favoriteHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                new FavoriteData();
                FavoriteData dataFavorite;
                dataFavorite = getIntent().getParcelableExtra(EXTRA_KEY);
                Objects.requireNonNull(getSupportActionBar()).setTitle(dataFavorite.getJudul());
                idFav = dataFavorite.getId();
                tvTitle.setText(dataFavorite.getJudul());
                tvDate.setText(dataFavorite.getRelease());
                tvDesc.setText(dataFavorite.getDeskripsi());
                tvOriginalLanguage.setText(dataFavorite.getBahasa());
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + dataFavorite.getPath()).into(ivPoster);
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + dataFavorite.getPath()).into(ivBack);

                FavoriteData favoriteSql = favoriteHelper.getMovieById(idFav);

                if (favoriteSql != null) {
                    Log.d("detailActivity", "favoriteState: data favorite ditemukan");
                    Log.d("detailActivity", "favoriteState: " + favoriteSql);

                    List<FavoriteData> movieItemList = new ArrayList<>();
                    movieItemList.add(0, favoriteSql);

                    if (movieItemList.isEmpty()) {
                        isFavorite = false;
                        btnfav.setVisibility(View.VISIBLE);
                        btnDel.setVisibility(View.INVISIBLE);

                        Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                    } else {
                        isFavorite = true;
                        btnDel.setVisibility(View.VISIBLE);
                        btnfav.setVisibility(View.INVISIBLE);
                    }
                } else {
                    isFavorite = false;
                    btnfav.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.INVISIBLE);
                    Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                }
                btnDel.setOnClickListener(v -> {
                    int result = getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + idFav),
                            null,
                            null);

                    if (!(result > 0)) {
                        Toast.makeText(DetailActivity.this, R.string.failed_delete_favorite, Toast.LENGTH_SHORT).show();
                    } else {
                        updateFavoriteWidget(this);
                        Intent intentku = new Intent(this, MainActivity.class);
                        startActivity(intentku);
                        Toast.makeText(DetailActivity.this, R.string.success_delete_favorite, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case EXTRA_RELEASE: {
                favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
                try {
                    favoriteHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                new ReleaseShow();
                ReleaseShow releaseItem;
                releaseItem = getIntent().getParcelableExtra(EXTRA_KEY);
                Objects.requireNonNull(getSupportActionBar()).setTitle(releaseItem.getOriginalTitle());
                idFav = releaseItem.getId();
                tvTitle.setText(releaseItem.getOriginalTitle());
                tvDate.setText(releaseItem.getReleaseDate());
                tvDesc.setText(releaseItem.getOverview());
                tvOriginalLanguage.setText(releaseItem.getOriginalLanguage());
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + releaseItem.getPosterPath()).into(ivPoster);
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + releaseItem.getPosterPath()).into(ivBack);

                FavoriteData favoriteSql = favoriteHelper.getMovieById(idFav);

                if (favoriteSql != null) {
                    Log.d("detailActivity", "favoriteState: data favorite ditemukan");
                    Log.d("detailActivity", "favoriteState: " + favoriteSql);

                    List<FavoriteData> movieItemList = new ArrayList<>();
                    movieItemList.add(0, favoriteSql);

                    if (movieItemList.isEmpty()) {
                        isFavorite = false;
                        btnfav.setVisibility(View.VISIBLE);
                        btnDel.setVisibility(View.INVISIBLE);

                        Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                    } else {
                        isFavorite = true;
                        btnDel.setVisibility(View.VISIBLE);
                        btnfav.setVisibility(View.INVISIBLE);
                    }
                } else {
                    isFavorite = false;
                    btnfav.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.INVISIBLE);
                    Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                }
                btnfav.setOnClickListener(v -> {
                    new ReleaseShow();
                    ReleaseShow releaseShow1;
                    releaseShow1 = getIntent().getParcelableExtra(EXTRA_KEY);
                    ContentValues values = new ContentValues();

                    values.put(_ID, releaseShow1.getId());
                    values.put(TITLE, releaseShow1.getOriginalTitle());
                    values.put(DATE, releaseShow1.getReleaseDate());
                    values.put(OVERVIEW, releaseShow1.getOverview());
                    values.put(LANGUAGE, releaseShow1.getOriginalLanguage());
                    values.put(POSTER, releaseShow1.getPosterPath());
                    values.put(TYPE, "Release Favorite");
                    Log.d("DrtailActivitas", "Judul: " + releaseShow1.getId());
                    getContentResolver().insert(DatabaseContract.MovieColumns.CONTENT_URI, values);
                    btnDel.setVisibility(View.VISIBLE);
                    btnfav.setVisibility(View.INVISIBLE);
                    updateFavoriteWidget(this);

                    Toast.makeText(DetailActivity.this, R.string.msg_success_Favorite, Toast.LENGTH_SHORT).show();

                });
                btnDel.setOnClickListener(v -> {
                    int result = getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + idFav),
                            null,
                            null);

                    if (!(result > 0)) {
                        Toast.makeText(DetailActivity.this, R.string.failed_delete_favorite, Toast.LENGTH_SHORT).show();
                    } else {
                        updateFavoriteWidget(this);
                        Intent intentku = new Intent(this, MainActivity.class);
                        startActivity(intentku);
                        Toast.makeText(DetailActivity.this, R.string.success_delete_favorite, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            default:
                favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
                try {
                    favoriteHelper.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                new ResultsShow();
                ResultsShow resultsShow;
                resultsShow = getIntent().getParcelableExtra(EXTRA_KEY);
                Objects.requireNonNull(getSupportActionBar()).setTitle(resultsShow.getOriginalTitle());
                idMovie = resultsShow.getId();
                tvTitle.setText(resultsShow.getOriginalTitle());
                tvDate.setText(resultsShow.getReleaseDate());
                tvDesc.setText(resultsShow.getOverview());
                tvOriginalLanguage.setText(resultsShow.getOriginalLanguage());
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + resultsShow.getPosterPath()).into(ivPoster);
                Glide.with(getApplicationContext()).load(Utils.BASE_IMG_URL + resultsShow.getPosterPath()).into(ivBack);
                FavoriteData favoriteMovie = favoriteHelper.getMovieById(idMovie);

                if (favoriteMovie != null) {
                    Log.d("detailActivity", "favoriteState: data favorite ditemukan");
                    Log.d("detailActivity", "favoriteState: " + favoriteMovie);

                    List<FavoriteData> movieItemList = new ArrayList<>();
                    movieItemList.add(0, favoriteMovie);

                    if (movieItemList.isEmpty()) {
                        isFavorite = false;
                        btnfav.setVisibility(View.VISIBLE);
                        btnDel.setVisibility(View.INVISIBLE);

                        Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                    } else {
                        isFavorite = true;
                        btnDel.setVisibility(View.VISIBLE);
                        btnfav.setVisibility(View.INVISIBLE);
                    }
                } else {
                    isFavorite = false;
                    btnfav.setVisibility(View.VISIBLE);
                    btnDel.setVisibility(View.INVISIBLE);
                    Log.d("detailActivity", "favoriteState: data favorite tidak ditemukan");
                }
                btnfav.setOnClickListener(v -> {
                    new ResultsShow();
                    ResultsShow resultsShow1;
                    resultsShow1 = getIntent().getParcelableExtra(EXTRA_KEY);
                    ContentValues values = new ContentValues();

                    values.put(_ID, resultsShow1.getId());
                    values.put(TITLE, resultsShow1.getOriginalTitle());
                    values.put(DATE, resultsShow1.getReleaseDate());
                    values.put(OVERVIEW, resultsShow1.getOverview());
                    values.put(LANGUAGE, resultsShow1.getOriginalLanguage());
                    values.put(POSTER, resultsShow1.getPosterPath());
                    values.put(TYPE, "Movies");
                    Log.d("detailActivity", "Judul: " + resultsShow1.getId());
                    getContentResolver().insert(DatabaseContract.MovieColumns.CONTENT_URI, values);
                    btnDel.setVisibility(View.VISIBLE);
                    btnfav.setVisibility(View.INVISIBLE);
                    updateFavoriteWidget(this);

                    Toast.makeText(DetailActivity.this, R.string.msg_success_Favorite, Toast.LENGTH_SHORT).show();

                });

                btnDel.setOnClickListener(v -> {
                    int result = getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + idMovie),
                            null,
                            null);

                    if (!(result > 0)) {
                        Toast.makeText(DetailActivity.this, R.string.failed_delete_favorite, Toast.LENGTH_SHORT).show();
                    } else {
                        updateFavoriteWidget(this);
                        Intent intentku = new Intent(this, MainActivity.class);
                        startActivity(intentku);
                        Toast.makeText(DetailActivity.this, R.string.success_delete_favorite, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }

    private void updateFavoriteWidget(Context context) {
        Intent intent = new Intent(context, FavoriteWidget.class);
        intent.setAction(FavoriteWidget.UPDATE_WIDGET);
        context.sendBroadcast(intent);
    }


}
