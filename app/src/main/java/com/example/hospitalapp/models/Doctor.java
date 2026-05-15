package com.example.hospitalapp.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "doctors",
        foreignKeys = @ForeignKey(
                entity = Schedule.class,
                parentColumns = "id",
                childColumns = "scheduleId",
                onDelete = ForeignKey.SET_NULL
        )
)
public class Doctor {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String fullName;
    private String specialization;
    private int experience;
    private String phoneNumber;
    private String cabinetNumber;
    private Integer scheduleId;

    public Doctor() { }
    public Doctor(String fullName, String specialization, int experience, String phoneNumber, String cabinetNumber, Integer scheduleId) {
        this.fullName = fullName;
        this.specialization = specialization;
        this.experience = experience;
        this.phoneNumber = phoneNumber;
        this.cabinetNumber = cabinetNumber;
        this.scheduleId = scheduleId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCabinetNumber() { return cabinetNumber; }
    public void setCabinetNumber(String cabinetNumber) { this.cabinetNumber = cabinetNumber; }

    public Integer getScheduleId() { return scheduleId; }
    public void setScheduleId(Integer scheduleId) { this.scheduleId = scheduleId; }
}