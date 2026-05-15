package com.example.hospitalapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.hospitalapp.models.Schedule;
import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert
    void save(Schedule schedule);
    @Query("SELECT * FROM schedules")
    List<Schedule> findAll();
    @Query("SELECT * FROM schedules WHERE id = :id LIMIT 1")
    Schedule findById(int id);
}