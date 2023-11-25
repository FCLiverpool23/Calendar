package com.example.lab6;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Alarm implements Serializable {
    private final int id;
    private final String name;
    private final String description;
    private final Time time;
    private boolean activity;

    public Alarm(int id, String name, String description, Time time, boolean activity){
        this.id = id;
        this.name = name;
        this.description = description;
        this.time = time;
        this.activity = activity;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Time getTime() { return time; }
    public boolean getActivity() { return activity; }
    public void setActivity(boolean activity) {this.activity = activity;}
}
