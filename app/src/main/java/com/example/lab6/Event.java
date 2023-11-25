package com.example.lab6;

import android.content.Context;

import java.io.Serializable;
import java.security.PublicKey;
import java.sql.Time;
import java.util.Date;

public class Event implements Serializable {
    private final int id;
    private final String name;
    private final String place;
    private final Date date;
    private final Time timeStart;
    private final Time timeEnd;
    private boolean activity;
    public Event(int id, String name, String place, Date date, Time timeStart, Time timeEnd, boolean activity){
        this.id = id;
        this.name = name;
        this.place = place;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.activity = activity;
    }
    public int getId() {return id;}

    public String getName() { return name; }
    public String getPlace() { return place; }
    public Date getDate() { return date; }
    public Time getTimeStart() { return timeStart; }
    public Time getTimeEnd() { return timeEnd; }
    public boolean getActivity() { return activity; }
    public void setActivity(boolean activity) {this.activity = activity;}

}
