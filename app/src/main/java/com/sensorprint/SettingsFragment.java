package com.sensorprint;

import android.hardware.Sensor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private EditText interval;
    private EditText duration;
    private EditText lo_acc;
    private EditText lo_gyro;
    private EditText lg_acc;
    private EditText lg_gyro;
    private Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        interval = view.findViewById(R.id.interval_stg);
        interval.setOnClickListener(v -> Utils.interval.setValue(Long.parseLong(interval.getText().toString())));

        duration = view.findViewById(R.id.duration_stg);
        duration.setOnClickListener(v -> Utils.duration.setValue(Long.parseLong(duration.getText().toString())));

        lo_acc = view.findViewById(R.id.lo_acc_stg);
        lo_acc.setOnClickListener(v -> Objects.requireNonNull(Utils.lambda_offsets.get(Sensor.TYPE_ACCELEROMETER)).setValue(Float.parseFloat(lo_acc.getText().toString())));

        lo_gyro = view.findViewById(R.id.lo_gyro_stg);
        lo_gyro.setOnClickListener(v -> Objects.requireNonNull(Utils.lambda_offsets.get(Sensor.TYPE_GYROSCOPE)).setValue(Float.parseFloat(lo_gyro.getText().toString())));

        lg_acc = view.findViewById(R.id.lg_acc_stg);
        lg_acc.setOnClickListener(v -> Objects.requireNonNull(Utils.lambda_offsets.get(Sensor.TYPE_ACCELEROMETER)).setValue(Float.parseFloat(lg_acc.getText().toString())));

        lg_gyro = view.findViewById(R.id.lg_gyro_stg);
        lg_gyro.setOnClickListener(v -> Objects.requireNonNull(Utils.lambda_offsets.get(Sensor.TYPE_GYROSCOPE)).setValue(Float.parseFloat(lg_gyro.getText().toString())));

        save = view.findViewById(R.id.save_btn);
//        save.setOnClickListener(v -> {});

        Utils.recording_in_progress.observe(requireActivity(), item -> {
            interval.setEnabled(!item);
            duration.setEnabled(!item);
            lo_acc.setEnabled(!item);
            lo_gyro.setEnabled(!item);
            lg_acc.setEnabled(!item);
            lg_gyro.setEnabled(!item);
            save.setEnabled(!item);
        });
    }
}