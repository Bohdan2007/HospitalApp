package com.example.hospitalapp.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hospitalapp.AddDoctorActivity;
import com.example.hospitalapp.R;
import com.example.hospitalapp.ScheduleListActivity;
import com.example.hospitalapp.database.AppDatabase;
import com.example.hospitalapp.models.Doctor;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private static final int NO_SCHEDULE_ID = -1;
    private List<Doctor> doctorList;

    public DoctorAdapter(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);

        holder.tvName.setText(doctor.getFullName());
        holder.tvSpec.setText(doctor.getSpecialization());
        holder.tvExp.setText("Стаж: " + doctor.getExperience() + " р.");
        holder.tvCab.setText("каб. " + doctor.getCabinetNumber());

        holder.btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ScheduleListActivity.class);

            int schedId = 0;
            if(doctor.getScheduleId() != null){
                schedId = doctor.getScheduleId();
            }else{
                schedId = -1;
            }

            intent.putExtra(ScheduleListActivity.EXTRA_SPECIFIC_SCHEDULE_ID, schedId);
            v.getContext().startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            int currentPos = holder.getAdapterPosition();
            if (currentPos != RecyclerView.NO_POSITION) {
                AppDatabase db = AppDatabase.getInstance(v.getContext());
                db.doctorDao().delete(doctor);
                doctorList.remove(currentPos);
                notifyItemRemoved(currentPos);
                Toast.makeText(v.getContext(), "Лікаря видалено!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), AddDoctorActivity.class);
            intent.putExtra(AddDoctorActivity.EXTRA_DOCTOR_ID, doctor.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpec, tvExp, tvCab;
        Button btnSchedule;
        ImageButton btnEdit, btnDelete;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvDoctorName);
            tvSpec = itemView.findViewById(R.id.tvSpecialization);
            tvExp = itemView.findViewById(R.id.tvExperience);
            tvCab = itemView.findViewById(R.id.tvCabinetNumber);
            btnSchedule = itemView.findViewById(R.id.btnViewSchedule);
            btnEdit = itemView.findViewById(R.id.btnEditDoctor);
            btnDelete = itemView.findViewById(R.id.btnDeleteDoctor);
        }
    }
}