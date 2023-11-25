package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class New_Alarm extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.YEAR, 0);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

    public void Back(View view) {
        finish();
    }

    public void Add(View view) {
        TextView textName = (TextView) findViewById(R.id.edit_name_alarm);
        TextView textDescription = (TextView) findViewById(R.id.edit_description_alarm);
        String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));

        DataBase db = new DataBase(this);
        try{
            Alarm alarm = new Alarm(MainActivity.listViewEvent.getChildCount() + MainActivity.listViewAlarm.getChildCount(),
                    textName.getText().toString(), textDescription.getText().toString(),
                    new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time).getTime()), true);
            db.addAlarm(alarm);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()){
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            }
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getAlarmActionPendingIntent(alarm));
            finish();
        }catch (Exception ex){
            Log.e("Error_add_alarm", ex.getMessage());
        }
    }

    private PendingIntent getAlarmActionPendingIntent(Alarm alarm){
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("Alarm", alarm);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getBroadcast(this, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}