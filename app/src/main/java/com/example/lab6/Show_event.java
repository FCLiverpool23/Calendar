package com.example.lab6;

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
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Show_event extends AppCompatActivity {

    public static String Time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        TextView text_name = (TextView)findViewById(R.id.show_event_name);
        TextView text_time = (TextView)findViewById(R.id.show_event_time);
        TextView text_date = (TextView)findViewById(R.id.show_event_date);
        Button btn_remove = (Button) findViewById(R.id.removeEvent);
        Button btn_edit = (Button)findViewById(R.id.editEvent);
        DataBase dataBase = new DataBase(this);

        Event event = getIntent().getSerializableExtra("Event", Event.class);

        Time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeStart()) + " - " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeEnd());
        text_name.setText(event.getName());
        text_time.setText(Time);
        text_date.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(event.getDate()));

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if (event.getActivity()){
                        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getApplicationContext(), Notification_Receiver.class);
                        intent.putExtra("Event", event);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), event.getId(), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                    }
                    dataBase.remove_event(text_name.getText().toString(),
                            new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(text_date.getText().toString()),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(text_time.getText().toString()).getTime()));
                    Toast.makeText(Show_event.this,"Напоминание удалено", Toast.LENGTH_LONG).show();
                    finish();
                }catch (Exception ex){
                    Log.e("Error_remove_event", ex.getMessage());
                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Edit_Event.class);
                intent.putExtra("Event", event);
                startActivity(intent);
                finish();
            }
        });
    }
    public void Back(View view) { finish(); }
}