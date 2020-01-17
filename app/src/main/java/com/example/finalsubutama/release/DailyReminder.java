package com.example.finalsubutama.release;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.example.finalsubutama.MainActivity;
import com.example.finalsubutama.R;
import com.example.finalsubutama.Utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class DailyReminder extends BroadcastReceiver {

    private final static int NOTIF_ID_DAILY = 101;
    public static String CHANNEL_ID = "channel_01";
    public static CharSequence CHANNEL_NAME = "Final Submission channel";
    public DailyReminder() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "Yuk Buka Aplikasi lagi, banyak film terbaru menunggumu lho";
        String title = "Daily Reminder";

        showAlarmNotification(context, title, message);

    }

    private void showAlarmNotification(Context context, String title, String message) {

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(DailyReminder.NOTIF_ID_DAILY, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,message)
                .setSmallIcon(R.drawable.ic_favorite)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
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
            notificationManagerCompat.notify(DailyReminder.NOTIF_ID_DAILY, notification);
        }

    }



    public void setReminder(Context context, String type, String time, String message) {
        if (isDateInvalid(time)) return;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        intent.putExtra(Utils.EXTRA_MESSAGE_PREF,message);
        intent.putExtra(Utils.EXTRA_TYPE_PREF,type);
        String[] timeArray = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND,0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_DAILY,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(context, "Anda Akan di ingatkan untuk membuka aplikasi setiap jam 7", Toast.LENGTH_SHORT).show();
    }

    public void cancelReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_DAILY,intent,0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context,"dinonaktifkan", Toast.LENGTH_SHORT).show();
    }

    private final static String TIME_FORMAT = "HH:mm";

    private boolean isDateInvalid(String date) {
        try {
            DateFormat df = new SimpleDateFormat(DailyReminder.TIME_FORMAT, Locale.getDefault());
            df.setLenient(false);
            df.parse(date);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }
}