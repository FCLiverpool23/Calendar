package com.example.lab6;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Alarm_Active extends AppCompatActivity {

    private Alarm alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_active);
        alarm = getIntent().getSerializableExtra("Alarm", Alarm.class);

        TextView textName = (TextView) findViewById(R.id.alarm_name_in_active);
        Button btn = (Button)findViewById(R.id.alarm_off);

        textName.setText(alarm.getName());

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
        ringtone.play();

        btn.setOnClickListener(v->{
            if (ringtone.isPlaying()){
                ringtone.stop();
            }
            finish();
        });
    }
}