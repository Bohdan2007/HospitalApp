package com.example.hospitalapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.hospitalapp.models.Doctor;
import java.util.List;

@Dao
public interface DoctorDao {
    @Insert
    void save(Doctor doctor);
    @Update
    void update(Doctor doctor);
    @Delete
    void delete(Doctor doctor);
    @Query("SELECT * FROM doctors")
    List<Doctor> getAllDoctors();
    @Query("SELECT * FROM doctors WHERE id = :id LIMIT 1")
    Doctor findById(int id);
}