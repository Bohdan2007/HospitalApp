package com.example.hospitalapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hospitalapp.database.AppDatabase;
import com.example.hospitalapp.models.Schedule;
import java.util.ArrayList;
import java.util.List;

public class AddScheduleActivity extends AppCompatActivity {
    private static final int HOURS_IN_DAY = 24;
    private static final int DEFAULT_START_TIME_INDEX = 8;
    private static final int DEFAULT_END_TIME_INDEX = 17;
    private EditText etScheduleTitle;
    private Spinner spinnerStartTime, spinnerEndTime;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        db = AppDatabase.getInstance(this);

        etScheduleTitle = findViewById(R.id.etScheduleTitle);
        spinnerStartTime = findViewById(R.id.spinnerStartTime);
        spinnerEndTime = findViewById(R.id.spinnerEndTime);
        Button btnSaveSchedule = findViewById(R.id.btnSaveSchedule);

        setupTimeSpinners();

        btnSaveSchedule.setOnClickListener(v -> saveScheduleToDatabase());
    }

    private void setupTimeSpinners() {
        List<String> times = new ArrayList<>();
        for (int i = 0; i < HOURS_IN_DAY; i++) {
            times.add(String.format("%02d:00", i));
        }

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, R.layout.spinner_text, times);
        timeAdapter.setDropDownViewResource(R.layout.spinner_text);

        spinnerStartTime.setAdapter(timeAdapter);
        spinnerEndTime.setAdapter(timeAdapter);

        spinnerStartTime.setSelection(DEFAULT_START_TIME_INDEX);
        spinnerEndTime.setSelection(DEFAULT_END_TIME_INDEX);
    }

    private void saveScheduleToDatabase() {
        String title = etScheduleTitle.getText().toString().trim();
        String startTime = spinnerStartTime.getSelectedItem().toString();
        String endTime = spinnerEndTime.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Будь ласка, введіть назву графіка!", Toast.LENGTH_SHORT).show();
            return;
        }

        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int endHour = Integer.parseInt(endTime.split(":")[0]);

        if (startHour >= endHour) {
            Toast.makeText(this, "Помилка: Час кінця роботи має бути пізніше за час початку!", Toast.LENGTH_LONG).show();
            return;
        }

        Schedule newSchedule = new Schedule(title, startTime, endTime);
        db.scheduleDao().save(newSchedule);

        Toast.makeText(this, "Графік успішно збережено!", Toast.LENGTH_SHORT).show();
        finish();
    }
}