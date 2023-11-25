package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Edit_Event extends AppCompatActivity {
    TextView text_timeStart;
    TextView text_timeEnd;
    TextView text_date;
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.newAllDay);
        text_timeStart = (TextView) findViewById(R.id.newTimeStartEvent);
        text_timeEnd = (TextView) findViewById(R.id.newTimeEndEvent);
        text_date = (TextView) findViewById(R.id.newDateEvent);
        TextView text_name = (TextView) findViewById(R.id.newNameEvent);
        TextView text_location = (TextView) findViewById(R.id.newLocationEvent);
        ImageView btn_timeStart = (ImageView) findViewById(R.id.newButtonTimeStartEvent);
        ImageView btn_timeEnd = (ImageView) findViewById(R.id.newButtonTimeEndEvent);
        ImageView btn_date = (ImageView) findViewById(R.id.newButtonDateEvent);
        Button btn_add = (Button)findViewById(R.id.addEvent);

        DataBase db = new DataBase(this);
        Event event = getIntent().getSerializableExtra("Event", Event.class);
        String[] date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(event.getDate()).replace(".", "-").split("-");
        String[] timeStart = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeStart()).split(":");
        String[] timeEnd = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeEnd()).split(":");

        text_name.setText(event.getName());
        text_location.setText(event.getPlace());
        text_date.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(event.getDate()));
        text_timeStart.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeStart()));
        text_timeEnd.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeEnd()));

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked) {
                    btn_timeStart.setEnabled(false);
                    btn_timeEnd.setEnabled(false);
                    text_timeStart.setText("00:00");
                    text_timeEnd.setText("23:59");
                }
                else {
                    btn_timeStart.setEnabled(true);
                    btn_timeEnd.setEnabled(true);
                    text_timeStart.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeStart()));
                    text_timeEnd.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeEnd()));
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Event new_event = new Event(event.getId(), text_name.getText().toString(), text_location.getText().toString(),
                            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(text_date.getText().toString()),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(text_timeStart.getText().toString()).getTime()),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(text_timeEnd.getText().toString()).getTime()), true);

                    db.edit_event(event.getId(), new_event);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getAlarmActionPendingIntent(event));

                    Toast.makeText(Edit_Event.this,"Добавлено напоминание.", Toast.LENGTH_LONG).show();
                    finish();
                }catch(Exception ex){
                    Log.e("Error_new_event", ex.getMessage());
                }
            }
        });

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Edit_Event.this, d, Integer.parseInt(date[2]), Integer.parseInt(date[1]) -1, Integer.parseInt(date[0])).show();
            }
        });

        btn_timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(Edit_Event.this, tS, Integer.parseInt(timeStart[0]), Integer.parseInt(timeStart[1]), true).show();
            }
        });

        btn_timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(Edit_Event.this, tE, Integer.parseInt(timeEnd[0]), Integer.parseInt(timeEnd[1]), true).show();
            }
        });
    }
    private PendingIntent getAlarmActionPendingIntent(Event event){
        Intent intent = new Intent(this, Notification_Receiver.class);
        intent.putExtra("Event", event);
        return PendingIntent.getBroadcast(this.getApplicationContext(), event.getId(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    }
    public void Back(View view) { finish(); }
    TimePickerDialog.OnTimeSetListener tS = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            text_timeStart.setText(time);
        }
    };
    TimePickerDialog.OnTimeSetListener tE = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            text_timeEnd.setText(time);
        }
    };
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String time = String.valueOf(dayOfMonth) + "." + String.valueOf(monthOfYear) + "." + String.valueOf(year);
            text_date.setText(time);
        }
    };
}