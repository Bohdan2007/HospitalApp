package com.example.hospitalapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hospitalapp.database.AppDatabase;
import com.example.hospitalapp.models.Doctor;
import com.example.hospitalapp.models.Schedule;
import java.util.List;

public class AddDoctorActivity extends AppCompatActivity {
    public static final String EXTRA_DOCTOR_ID = "DOCTOR_ID";
    private static final int DEFAULT_DOCTOR_ID = -1;
    private static final int REQUIRED_PHONE_LENGTH = 10;
    private static final int MIN_EXPERIENCE = 0;
    private static final int MAX_EXPERIENCE = 100;
    private static final int MIN_CABINET = 1;
    private static final int MAX_CABINET = 250;
    private EditText etFullName, etSpecialization, etExperience, etPhoneNumber, etCabinetNumber;
    private Spinner spinnerSchedule;
    private AppDatabase db;
    private Button btnSaveDoctor, btnBack;
    private int doctorIdToEdit = DEFAULT_DOCTOR_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);

        db = AppDatabase.getInstance(this);

        etFullName = findViewById(R.id.etFullName);
        etSpecialization = findViewById(R.id.etSpecialization);
        etExperience = findViewById(R.id.etExperience);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etCabinetNumber = findViewById(R.id.etCabinetNumber);
        spinnerSchedule = findViewById(R.id.spinnerSchedule);
        btnSaveDoctor = findViewById(R.id.btnSaveDoctor);

        btnBack = findViewById(R.id.btnBack);
        loadSchedulesIntoSpinner();

        if (getIntent().hasExtra(EXTRA_DOCTOR_ID)) {
            doctorIdToEdit = getIntent().getIntExtra(EXTRA_DOCTOR_ID, DEFAULT_DOCTOR_ID);
            if (doctorIdToEdit != DEFAULT_DOCTOR_ID) {
                btnSaveDoctor.setText("Оновити дані");
                loadDoctorData(doctorIdToEdit);
            }
        }

        btnSaveDoctor.setOnClickListener(v -> saveDoctorToDatabase());

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadSchedulesIntoSpinner() {
        List<Schedule> schedules = db.scheduleDao().findAll();

        if (schedules.isEmpty()) {
            Toast.makeText(this, "Спочатку додайте графіки роботи!", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayAdapter<Schedule> adapter = new ArrayAdapter<>(this, R.layout.spinner_text, schedules);
        adapter.setDropDownViewResource(R.layout.spinner_text);

        spinnerSchedule.setAdapter(adapter);
    }

    private void loadDoctorData(int id) {
        Doctor doctor = db.doctorDao().findById(id);
        if (doctor != null) {
            etFullName.setText(doctor.getFullName());
            etSpecialization.setText(doctor.getSpecialization());
            etExperience.setText(String.valueOf(doctor.getExperience()));
            etPhoneNumber.setText(doctor.getPhoneNumber());
            etCabinetNumber.setText(doctor.getCabinetNumber());

            ArrayAdapter<Schedule> adapter = (ArrayAdapter<Schedule>) spinnerSchedule.getAdapter();
            if (adapter != null) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    Schedule schedule = adapter.getItem(i);
                    if (schedule != null && schedule.getId() == doctor.getScheduleId()) {
                        spinnerSchedule.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    private void saveDoctorToDatabase() {
        String name = etFullName.getText().toString().trim();
        String spec = etSpecialization.getText().toString().trim();
        String expStr = etExperience.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String cabStr = etCabinetNumber.getText().toString().trim();

        if (name.isEmpty() || spec.isEmpty() || expStr.isEmpty() || phone.isEmpty() || cabStr.isEmpty()) {
            Toast.makeText(this, "Будь ласка, заповніть всі поля!", Toast.LENGTH_SHORT).show();
            return;
        }

        phone = phone.replaceAll("[^0-9]", "");
        if (phone.length() != REQUIRED_PHONE_LENGTH) {
            Toast.makeText(this, "Номер телефону має містити рівно " + REQUIRED_PHONE_LENGTH + " цифр!", Toast.LENGTH_SHORT).show();
            return;
        }

        int experience = 0;
        try {
            experience = Integer.parseInt(expStr);
            if (experience < MIN_EXPERIENCE || experience > MAX_EXPERIENCE) {
                Toast.makeText(this, "Стаж має бути від " + MIN_EXPERIENCE + " до " + MAX_EXPERIENCE + " років!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Стаж має бути числом!", Toast.LENGTH_SHORT).show();
            return;
        }

        int cabinetNumber = 0;
        try {
            cabinetNumber = Integer.parseInt(cabStr);
            if (cabinetNumber < MIN_CABINET || cabinetNumber > MAX_CABINET) {
                Toast.makeText(this, "Номер кабінету має бути від " + MIN_CABINET + " до " + MAX_CABINET + "!", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Номер кабінету має бути числом!", Toast.LENGTH_SHORT).show();
            return;
        }

        Schedule selectedSchedule = (Schedule) spinnerSchedule.getSelectedItem();
        if (selectedSchedule == null) {
            Toast.makeText(this, "Оберіть графік роботи!", Toast.LENGTH_SHORT).show();
            return;
        }
        int scheduleId = selectedSchedule.getId();

        Doctor newDoctor = new Doctor(name, spec, experience, phone, String.valueOf(cabinetNumber), scheduleId);

        if (doctorIdToEdit != DEFAULT_DOCTOR_ID) {
            newDoctor.setId(doctorIdToEdit);
            db.doctorDao().update(newDoctor);
            Toast.makeText(this, "Дані лікаря оновлено!", Toast.LENGTH_SHORT).show();
        } else {
            db.doctorDao().save(newDoctor);
            Toast.makeText(this, "Лікаря успішно додано!", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}