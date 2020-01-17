package com.example.finalsubutama;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.Switch;

import com.example.finalsubutama.release.DailyReminder;
import com.example.finalsubutama.release.GetRelease;

public class SettingActivity extends AppCompatActivity {
    Switch releaseSw, dailySw;
    GetRelease getRelease;
    SettingPreference settingPreference;
    public DailyReminder reminderDaily;
    public SharedPreferences releaseReminderShr, dailyReminderShr;
    public SharedPreferences.Editor editorReleaseReminder, editorDailyReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        releaseSw = findViewById(R.id.switchRelease);
        dailySw = findViewById(R.id.switchDaily);

        getRelease = new GetRelease();
        reminderDaily =new DailyReminder();
        settingPreference = new SettingPreference(this);
        setPreference();
        releaseSw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editorReleaseReminder = releaseReminderShr.edit();
            if (isChecked) {
                editorReleaseReminder.putBoolean(Utils.KEY_FIELD_UPCOMING_REMINDER, true);
                editorReleaseReminder.apply();
                releaseReminderOn();
            } else {
                editorReleaseReminder.putBoolean(Utils.KEY_FIELD_UPCOMING_REMINDER, false);
                editorReleaseReminder.apply();
                releaseReminderOff();
            }
        });
        dailySw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editorDailyReminder = dailyReminderShr.edit();
            if (isChecked) {
                editorDailyReminder.putBoolean(Utils.KEY_FIELD_REMINDER_DAILY, true);
                editorDailyReminder.apply();
                dailyOn();
            } else {
                editorDailyReminder.putBoolean(Utils.KEY_FIELD_REMINDER_DAILY, false);
                editorDailyReminder.commit();
                dailyOff();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dailyOff() {
        reminderDaily.cancelReminder(SettingActivity.this);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dailyOn() {
        String time = "07:00";
        String message = "Daily Movie Missing You, please wait come back soon";
        settingPreference.setAlarmDailyTime(time);
        settingPreference.setAlarmDailyMessage(message);
        reminderDaily.setReminder(SettingActivity.this, Utils.TYPE_REMINDER_DAILY, time, message);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void releaseReminderOff() {
        getRelease.cancelRelease(SettingActivity.this);
    }




    @RequiresApi(api = Build.VERSION_CODES.N)
    private void releaseReminderOn() {
        String time = "08:00";
        String message = "Release Movie, please wait come back soon";
        settingPreference.setReminderReleaseTime(time);
        settingPreference.setReminderReleaseMessage(message);
        getRelease.setRelease(SettingActivity.this, Utils.TYPE_REMINDER_PREF, time, message);

    }

    private void setPreference() {
        releaseReminderShr = getSharedPreferences(Utils.KEY_HEADER_UPCOMING_REMINDER, MODE_PRIVATE);
        dailyReminderShr = getSharedPreferences(Utils.KEY_HEADER_DAILY_REMINDER, MODE_PRIVATE);
        boolean checkSwReleaseReminder = releaseReminderShr.getBoolean(Utils.KEY_FIELD_UPCOMING_REMINDER, false);
        releaseSw.setChecked(checkSwReleaseReminder);
        boolean checkSwDaily = dailyReminderShr.getBoolean(Utils.KEY_FIELD_REMINDER_DAILY, false);
        dailySw.setChecked(checkSwDaily);
    }

}
