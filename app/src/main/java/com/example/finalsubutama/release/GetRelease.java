package com.example.finalsubutama.release;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.finalsubutama.DetailActivity;
import com.example.finalsubutama.R;
import com.example.finalsubutama.Utils;
import com.example.finalsubutama.service.BaseApiService;
import com.example.finalsubutama.service.MovieClient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GetRelease extends BroadcastReceiver {
    private static final String EXTRA_KEY = "KEY";
    private static final String EXTRA_TYPE= "TYPE";
    private static final String EXTRA_RELEASE= "RELEASE";
    private int NOTIFICATION_ID = 21;
    String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "Final Submission channel";




    public GetRelease() {

    }
    public List<ReleaseShow> listData = new ArrayList();

    @Override
    public void onReceive(Context context, Intent intent) {

        BaseApiService apiService = MovieClient.getReleaseToday();
        Call<ResponseRelease> getRelease = apiService.releaseMovie(currentDate, currentDate);

        getRelease.enqueue(new Callback<ResponseRelease>() {
            @Override
            public void onResponse(@NonNull Call<ResponseRelease> call, @NonNull Response<ResponseRelease> response) {

                if (response.body() != null) {
                    listData = response.body().getResults();

                List<ReleaseShow> items = response.body().getResults();
                Calendar calander = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                String timeku = simpleDateFormat.format(calander.getTime());
                Log.d("jajal", "getTime: "+timeku);

                    for (int i = 1; i <items.size(); i++) {

                        ReleaseShow item = items.get(i);
                        String path = items.get(i).getPosterPath();
                        String overview = items.get(i).getOverview()+"...";
                        String title = items.get(i).getOriginalTitle();

                    showNotification(context, title, overview, i, path, item);
                        Log.d("jajal", "rand: "+i);
                }
                }else {
                    Toast.makeText(context, "Gagal ambil data mohon hubngi developer aplikasi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRelease> call, Throwable t) {
                Log.d("getRelease", "onFailure: " + t.toString());
            }
        });


    }


    private void showNotification(Context context, String title, String message, int index,String path, ReleaseShow item) {

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_KEY, item);
        intent.putExtra(EXTRA_TYPE, EXTRA_RELEASE);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String fotoUrl = Utils.BASE_IMG_URL + path;
        Log.d("foto", "showNotification: " + fotoUrl);
        Glide.with(context).asBitmap().load(fotoUrl) .apply(new RequestOptions().override(380, 300)).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, message)
                        .setContentTitle(title)
                        .setSmallIcon(R.drawable.ic_favorite)
                        .setContentText(message)
                        .setLargeIcon(resource)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(resource)
                                .bigLargeIcon(null))
                        .setContentIntent(pendingIntent)
                        .setColor(ContextCompat.getColor(context, android.R.color.black))
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(alarmSound);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                    builder.setChannelId(CHANNEL_ID);
                    if (notificationManagerCompat != null) {
                        notificationManagerCompat.createNotificationChannel(channel);
                    }
                }

                Notification notification = builder.build();

                if (notificationManagerCompat != null) {
                    notificationManagerCompat.notify(index, notification);
                }
            }


        });

        }



    public void setRelease(Context context, String type, String time, String message) {
        if (isDateInvalid(time, TIME_FORMAT)) return;
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, GetRelease.class);
            intent.putExtra(Utils.EXTRA_MESSAGE_RECIEVE, message);
            intent.putExtra(Utils.EXTRA_TYPE_RECIEVE, type);
            String[] timeArray = time.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
            calendar.set(Calendar.SECOND, 0);

            int requestCode = NOTIFICATION_ID;
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
            Toast.makeText(context, "Anda akan mendapatkan notifikasi update ketika jam 08.00", Toast.LENGTH_SHORT).show();

    }

    public void cancelRelease(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, GetRelease.class);
        int requestCode = NOTIFICATION_ID;
        if (alarmManager != null) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context,"Anda tidak akan mendapatkan notif film yang rilis setiap hari", Toast.LENGTH_SHORT).show();
    }

    private final static String TIME_FORMAT = "HH:mm";

    private boolean isDateInvalid(String date, String format) {
        try {
            DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}


