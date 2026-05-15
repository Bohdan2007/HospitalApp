package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hospitalapp.adapters.ScheduleAdapter;
import com.example.hospitalapp.database.AppDatabase;
import com.example.hospitalapp.models.Schedule;
import java.util.ArrayList;
import java.util.List;

public class ScheduleListActivity extends AppCompatActivity {
    public static final String EXTRA_SPECIFIC_SCHEDULE_ID = "SPECIFIC_SCHEDULE_ID";
    private static final int DEFAULT_SCHEDULE_ID = -1;
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private AppDatabase db;
    private int specificScheduleId = DEFAULT_SCHEDULE_ID;
    private boolean showAllSchedules = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewSchedules);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getIntent().hasExtra(EXTRA_SPECIFIC_SCHEDULE_ID)) {
            specificScheduleId = getIntent().getIntExtra(EXTRA_SPECIFIC_SCHEDULE_ID, DEFAULT_SCHEDULE_ID);
        }

        Button btnAllSchedules = findViewById(R.id.btnAllSchedules);
        Button btnAddNewSchedule = findViewById(R.id.btnAddNewSchedule);
        Button btnBack = findViewById(R.id.btnBack);

        btnAllSchedules.setOnClickListener(v -> {
            showAllSchedules = true;
            loadSchedules();
        });

        btnAddNewSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(ScheduleListActivity.this, AddScheduleActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSchedules();
    }

    private void loadSchedules() {
        List<Schedule> schedulesToDisplay = new ArrayList<>();

        if (specificScheduleId != DEFAULT_SCHEDULE_ID && !showAllSchedules) {
            Schedule schedule = db.scheduleDao().findById(specificScheduleId);
            if (schedule != null) {
                schedulesToDisplay.add(schedule);
            } else {
                Toast.makeText(this, "Графік лікаря не знайдено!", Toast.LENGTH_SHORT).show();
            }
        } else {
            schedulesToDisplay = db.scheduleDao().findAll();
        }

        adapter = new ScheduleAdapter(schedulesToDisplay);
        recyclerView.setAdapter(adapter);
    }
}