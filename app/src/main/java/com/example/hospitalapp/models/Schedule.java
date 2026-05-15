package com.example.hospitalapp.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedules")
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String startTime;
    private String endTime;

    public Schedule() { }
    public Schedule(String title, String startTime, String endTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        return title + " (" + startTime + " — " + endTime + ")";
    }
}