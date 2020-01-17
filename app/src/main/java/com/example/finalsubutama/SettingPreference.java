package com.example.finalsubutama;

import android.content.Context;
import android.content.SharedPreferences;


class SettingPreference {
   private SharedPreferences sharedPreferences;
   private SharedPreferences.Editor editor;

    SettingPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(Utils.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    void setReminderReleaseTime(String time) {
        editor.putString(Utils.KEY_RELEASE_DAILY,time);
        editor.commit();
    }

    void setReminderReleaseMessage(String message) {
        editor.putString(Utils.KEY_REMINDER_MESSAGE_Release,message);
    }


    void setAlarmDailyTime(String time) {
        editor.putString(Utils.KEY_REMINDER_DAILY,time);
        editor.commit();
    }

    void setAlarmDailyMessage(String message) {
        editor.putString(Utils.KEY_REMINDER_MESSAGE_DAILY,message);
    }
}
