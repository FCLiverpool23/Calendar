package com.example.lab6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static ListView listViewEvent;
    public static ListView listViewAlarm;
    ViewSwitcher switcher;
    Button all_alarm;
    Button all_notification;
    private boolean flag = false;
    private DataBase dataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendar = (CalendarView)findViewById(R.id.calendar);
        listViewEvent = (ListView)findViewById(R.id.listEvent);
        listViewAlarm = (ListView)findViewById(R.id.listAlarm);
        switcher = (ViewSwitcher)findViewById(R.id.switcher);
        all_alarm = (Button) findViewById(R.id.alarm);
        all_notification = (Button) findViewById(R.id.notification);

        switcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));

        dataBase = new DataBase(this);

        listViewEvent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                try{
                    Intent intent = new Intent(getApplicationContext(), Show_event.class);

                    Event event = dataBase.getEvent(((TextView)v.findViewById(R.id.Name)).getText().toString(),
                            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(((TextView)v.findViewById(R.id.Date)).getText().toString()),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(((TextView)v.findViewById(R.id.TimeStart)).getText().toString()).getTime()));

                    intent.putExtra("Event", event);
                    startActivity(intent);
                }catch (Exception ex){
                    Log.e("Events_Day", ex.getMessage());
                }
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = String.valueOf(dayOfMonth) + "." + String.valueOf(month+1) + "." + String.valueOf(year);
                List<Event> events_list = dataBase.getEvents(date);
                Event[] events = events_list.toArray(new Event[0]);
                listViewEvent.setAdapter(new MyArrayAdapterEvent(MainActivity.this, events));
            }
        });
    }

    public void New_event(View view) {
        Intent intent;
        if (flag){
            intent = new Intent(MainActivity.this, New_Alarm.class);
        }else {
            intent = new Intent(MainActivity.this, Activity_new_event.class);
        }
        startActivity(intent);
    }
    public void All_event(View view) {
        if (flag){
            switcher.showPrevious();
            all_notification.setBackgroundColor(Color.rgb(37, 37, 37));
            all_alarm.setBackgroundColor(Color.rgb(0, 0, 0));
            flag = false;
        }
        List<Event> events_list = dataBase.getEvents();
        Event[] events = events_list.toArray(new Event[0]);
        listViewEvent.setAdapter(new MyArrayAdapterEvent(this, events));
    }
    public void All_alarm(View view) {
        if (!flag){
            switcher.showNext();
            all_alarm.setBackgroundColor(Color.rgb(37, 37, 37));
            all_notification.setBackgroundColor(Color.rgb(0, 0, 0));
            flag = true;
        }
        List<Alarm> alarms_list = dataBase.getAlarms();
        Alarm[] alarms = alarms_list.toArray(new Alarm[0]);
        listViewAlarm.setAdapter(new MyArrayAdapterAlarm(this, alarms));
    }
}