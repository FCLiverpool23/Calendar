package com.example.lab6;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DataBase dataBase = new DataBase(context);
        Alarm alarm = intent.getSerializableExtra("Alarm", Alarm.class);
        dataBase.edit_alarm_activity(alarm.getId(), false);
        Intent alarmIntent = new Intent(context, Alarm_Active.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra("Alarm", alarm);
        context.startActivity(alarmIntent);
    }
}