package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Edit_Alarm extends AppCompatActivity {
    TextView name;
    TextView description;
    TimePicker timePicker;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        Button btn_remove = (Button)findViewById(R.id.alarm_remove);
        Button btn_back = (Button)findViewById(R.id.alarmBack);
        Button btn_edit = (Button)findViewById(R.id.alarmEdit);
        name = (TextView)findViewById(R.id.name_alarm_edit);
        description = (TextView)findViewById(R.id.description_alarm_edit);
        timePicker = (TimePicker) findViewById(R.id.edit_time_picker);
        timePicker.setIs24HourView(true);

        Alarm alarm = getIntent().getSerializableExtra("Alarm", Alarm.class);

        name.setText(alarm.getName());
        description.setText(alarm.getDescription());
        String[] alarmTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(alarm.getTime()).split(":");
        timePicker.setHour(Integer.parseInt(alarmTime[0]));
        timePicker.setMinute(Integer.parseInt(alarmTime[1]));

        DataBase dataBase = new DataBase(this);

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBase.remove_alarm(alarm.getName(), alarm.getTime());
                if (alarm.getActivity()) {
                    Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                    intent.putExtra("Alarm", alarm);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarm.getId(),
                                intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                }
                Toast.makeText(Edit_Alarm.this,"Будильник удален", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
                try{
                    dataBase.edit_alarm(alarm.getId(),
                            new Alarm(alarm.getId(), name.getText().toString(), description.getText().toString(),
                                    new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(time).getTime()),false));
                    Toast.makeText(Edit_Alarm.this,"Будильник изменен", Toast.LENGTH_LONG).show();
                    finish();
                }catch (Exception ex){
                    Log.e("Error_Edit_Alarm", ex.getMessage());
                }
            }
        });

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
}