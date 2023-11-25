package com.example.lab6;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MyArrayAdapterEvent extends ArrayAdapter<Event> {
    private final Activity context;
    private final Event[] events;
    public MyArrayAdapterEvent(Activity context, Event[] events) {
        super(context, R.layout.list_view_event, events);
        this.context = context;
        this.events = events;
    }

    static class ViewHolder {
        public TextView timeStart;
        public TextView timeEnd;
        public TextView name;
        public TextView date;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View rowView = convertView;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_view_event, null, true);
            holder = new ViewHolder();
            holder.timeStart = (TextView) rowView.findViewById(R.id.TimeStart);
            holder.timeEnd = (TextView) rowView.findViewById(R.id.TimeEnd);
            holder.name = (TextView) rowView.findViewById(R.id.Name);
            holder.date = (TextView) rowView.findViewById(R.id.Date);
            rowView.setTag(holder);
        }else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.timeStart.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(events[position].getTimeStart()));
        holder.timeEnd.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(events[position].getTimeEnd()));
        holder.name.setText(events[position].getName());
        holder.date.setText(df.format(events[position].getDate()));

        return rowView;
    }
}
