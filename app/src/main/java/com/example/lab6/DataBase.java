package com.example.lab6;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBase extends SQLiteOpenHelper {
    public DataBase(Context context) {
        super(context, "CalendarDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTable = "CREATE TABLE Event ( " +
                "ID INTEGER PRIMARY KEY," +
                "Name TEXT," +
                "Place TEXT," +
                "Date TEXT," +
                "TimeStart TEXT," +
                "TimeEnd TEXT," +
                "Activity TEXT);";
        db.execSQL(CreateTable);
        CreateTable = "CREATE TABLE Alarm ( " +
                "ID INTEGER PRIMARY KEY," +
                "Name TEXT," +
                "Description TEXT," +
                "Time TEXT," +
                "Activity TEXT)";
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Event");
        db.execSQL("DROP TABLE IF EXISTS Alarm");
        onCreate(db);
    }

    public void addEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", event.getId());
        contentValues.put("Name", event.getName());
        contentValues.put("Place", event.getPlace());
        contentValues.put("Date", df.format(event.getDate()));
        contentValues.put("TimeStart", new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeStart()));
        contentValues.put("TimeEnd", new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeEnd()));
        contentValues.put("Activity", String.valueOf(event.getActivity()));
        db.insert("Event", null, contentValues);
        db.close();
    }
    public void addAlarm(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", alarm.getId());
        contentValues.put("Name", alarm.getName());
        contentValues.put("Description", alarm.getDescription());
        contentValues.put("Time", new SimpleDateFormat("HH:mm", Locale.getDefault()).format(alarm.getTime()));
        contentValues.put("Activity", String.valueOf(alarm.getActivity()));
        db.insert("Alarm", null, contentValues);
        db.close();
    }
    public Event getEvent(String name, Date date, Time timeStart){
            Event event;
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            SQLiteDatabase db = this.getReadableDatabase();
            String searchQuery = "SELECT * FROM Event WHERE Name = '" + name + "' AND Date = '"  + df.format(date)
                    + "' AND TimeStart = '" + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeStart) + "'";

            Cursor row  = db.rawQuery(searchQuery, null);
        try{
            if (row.moveToFirst()){
                event = new Event(row.getInt(0), row.getString(1), row.getString(2),
                        new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(row.getString(3)),
                        new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(row.getString(4)).getTime()),
                        new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(row.getString(5)).getTime()),
                        Boolean.parseBoolean(row.getString(6)));
                db.close();
                return event;
            }
        }catch (Exception ex){
            Log.i("Error_search_event", ex.getMessage());
        }
        db.close();
        return null;
    }
    public Alarm getAlarm(String name, Time time){
        Alarm alarm;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM Alarm WHERE Name = '" + name + "' AND Time = '"
                + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(time) + "'";
        Cursor row  = db.rawQuery(searchQuery, null);
        try{
            if (row.moveToFirst()){
                alarm = new Alarm(row.getInt(0), row.getString(1), row.getString(2),
                        new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(row.getString(3)).getTime()),
                        Boolean.parseBoolean(row.getString(4)));
                db.close();
                return alarm;
            }
        }catch (Exception ex){
            Log.i("Error_search_alarm", ex.getMessage());
        }
        db.close();
        return null;
    }
    public List<Event> getEvents(){
        List<Event> events =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM Event", null);
        try{
            if (allRows.moveToFirst()) {
                do {
                    Event event = new Event(allRows.getInt(0), allRows.getString(1),
                            allRows.getString(2), new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(allRows.getString(3)),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(allRows.getString(4)).getTime()),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(allRows.getString(5)).getTime()),
                            Boolean.parseBoolean(allRows.getString(6)));
                    events.add(event);
                } while (allRows.moveToNext());
            }
        }catch (Exception ex){
            Log.e("Error_all_Events", ex.toString());
        }
        db.close();
        return events;
    }
    public List<Alarm> getAlarms(){
        List<Alarm> alarms =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor allRows  = db.rawQuery("SELECT * FROM Alarm", null);
        try{
            if (allRows.moveToFirst()) {
                do {
                    Alarm alarm = new Alarm(allRows.getInt(0), allRows.getString(1), allRows.getString(2),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(allRows.getString(3)).getTime()),
                            Boolean.parseBoolean(allRows.getString(4)));
                    alarms.add(alarm);
                } while (allRows.moveToNext());
            }
        }catch (Exception ex){
            Log.e("Error_all_Alarms", ex.toString());
        }
        db.close();
        return alarms;
    }
    public List<Event> getEvents(String date){
        List<Event> events =new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM Event WHERE Date = '" + date + "'";
        Cursor allRows  = db.rawQuery(searchQuery, null);
        try{
            if (allRows.moveToFirst()) {
                do {
                    Event event = new Event(allRows.getInt(0), allRows.getString(1),
                            allRows.getString(2), new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(allRows.getString(3)),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(allRows.getString(4)).getTime()),
                            new Time(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(allRows.getString(5)).getTime()),
                            Boolean.parseBoolean(allRows.getString(6)));
                    events.add(event);
                } while (allRows.moveToNext());
            }
        }catch (Exception ex){
            Log.e("Error_all_Events", ex.toString());
        }
        db.close();
        return events;
    }
    public void remove_event(String Name, Date date, Time timeStart){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SQLiteDatabase db = this.getWritableDatabase();
        String removeQuery = "DELETE FROM Event WHERE Name = '" + Name + "' AND Date = '"  + df.format(date)
                + "' AND TimeStart = '" + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeStart) + "'";
        db.execSQL(removeQuery);
        db.close();
    }
    public void remove_alarm(String Name, Time time){
        SQLiteDatabase db = this.getWritableDatabase();
        String removeQuery = "DELETE FROM Alarm WHERE Name = '" + Name + "' AND Time = '" +
                new SimpleDateFormat("HH:mm", Locale.getDefault()).format(time) + "'";
        db.execSQL(removeQuery);
        db.close();
    }
    public void edit_event(int Id, Event event){
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", event.getId());
        contentValues.put("Name", event.getName());
        contentValues.put("Place", event.getPlace());
        contentValues.put("Date", df.format(event.getDate()));
        contentValues.put("TimeStart", new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeStart()));
        contentValues.put("TimeEnd", new SimpleDateFormat("HH:mm", Locale.getDefault()).format(event.getTimeEnd()));
        contentValues.put("Activity", String.valueOf(event.getActivity()));
        db.update("Event", contentValues, "ID = ?", new String[] {String.valueOf(Id)});
        db.close();
    }
    public void edit_alarm(int Id, Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", alarm.getId());
        contentValues.put("Name", alarm.getName());
        contentValues.put("Description", alarm.getDescription());
        contentValues.put("Time", new SimpleDateFormat("HH:mm", Locale.getDefault()).format(alarm.getTime()));
        contentValues.put("Activity", String.valueOf(alarm.getActivity()));
        db.update("Alarm", contentValues, "ID = ?", new String[] {String.valueOf(Id)});
        db.close();
    }
    public void edit_event_activity(int ID, boolean activity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Activity", String.valueOf(activity));
        db.update("Event", contentValues, "ID = ?", new String[] {String.valueOf(ID)});
        db.close();
    }

    public void edit_alarm_activity(int ID, boolean activity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Activity", String.valueOf(activity));
        db.update("Alarm", contentValues, "ID = ?", new String[] {String.valueOf(ID)});
        db.close();
    }
}
