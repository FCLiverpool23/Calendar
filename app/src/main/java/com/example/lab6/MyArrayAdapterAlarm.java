package com.example.lab6;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyArrayAdapterAlarm extends ArrayAdapter<Alarm> {
    private final Activity context;
    private final Alarm[] alarms;
    public MyArrayAdapterAlarm(Activity context, Alarm[] alarms) {
        super(context, R.layout.list_view_alarm, alarms);
        this.context = context;
        this.alarms = alarms;
    }
    static class ViewHolder {
        public TextView time;
        public TextView name;
        public LinearLayout layout;
        public SwitchCompat switchCompat;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_view_alarm, null, true);
            holder = new ViewHolder();
            holder.time = (TextView) rowView.findViewById(R.id.alarmTime);
            holder.name = (TextView) rowView.findViewById(R.id.alarmName);
            holder.switchCompat = (SwitchCompat) rowView.findViewById(R.id.alarmToggleSwitch);
            holder.layout = (LinearLayout) rowView.findViewById(R.id.layout_alarm);
            rowView.setTag(holder);
        }else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.time.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(alarms[position].getTime()));
        holder.name.setText(alarms[position].getName());
        holder.switchCompat.setChecked(alarms[position].getActivity());
        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                String[] time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(alarms[position].getTime()).split(":");
                Calendar calendar = Calendar.getInstance();
                DataBase dataBase = new DataBase(context);
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                if (isChecked) {
                    if (Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()){
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                    }
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), getAlarmActionPendingIntent(alarms[position]));
                }else{
                    alarmManager.cancel(getAlarmActionPendingIntent(alarms[position]));
                }
                dataBase.edit_alarm_activity(alarms[position].getId(), isChecked);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(context, Edit_Alarm.class);
                    intent.putExtra("Alarm", alarms[position]);
                    context.startActivity(intent);
                }catch (Exception ex){
                    Log.e("Alarms_Day", ex.getMessage());
                }
            }
        });
        return rowView;
    }
    private PendingIntent getAlarmActionPendingIntent(Alarm alarm){
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("Alarm", alarm);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getBroadcast(context, alarm.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}