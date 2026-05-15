package com.example.hospitalapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.hospitalapp.dao.DoctorDao;
import com.example.hospitalapp.dao.ScheduleDao;
import com.example.hospitalapp.models.Doctor;
import com.example.hospitalapp.models.Schedule;

@Database(entities = {Doctor.class, Schedule.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DoctorDao doctorDao();
    public abstract ScheduleDao scheduleDao();
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "hospital_db").allowMainThreadQueries().addCallback(roomCallback).build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new Thread(() -> {
                ScheduleDao dao = instance.scheduleDao();
                dao.save(new Schedule("Ранкова зміна", "08:00", "14:00"));
                dao.save(new Schedule("Денна зміна", "12:00", "18:00"));
                dao.save(new Schedule("Вечірня зміна", "14:00", "20:00"));
            }).start();
        }
    };
}