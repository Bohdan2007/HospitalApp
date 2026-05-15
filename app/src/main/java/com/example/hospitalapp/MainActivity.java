package com.example.hospitalapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hospitalapp.adapters.DoctorAdapter;
import com.example.hospitalapp.database.AppDatabase;
import com.example.hospitalapp.models.Doctor;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);

        recyclerView = findViewById(R.id.recyclerViewDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnAdd = findViewById(R.id.btnAddDoctor);
        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddDoctorActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctors();
    }

    private void loadDoctors() {
        List<Doctor> doctors = db.doctorDao().getAllDoctors();

        adapter = new DoctorAdapter(doctors);
        recyclerView.setAdapter(adapter);
    }
}