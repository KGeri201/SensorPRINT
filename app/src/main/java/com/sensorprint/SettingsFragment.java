package com.sensorprint;

import android.hardware.Sensor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {
    private final String name;
    private final AutoRecord.TestObject values;
    private final AdaptedPatch patch;

    private EditText filename;
    private EditText lo_acc;
    private EditText lo_gyro;
    private EditText lg_acc;
    private EditText lg_gyro;
    private Button save;

    public SettingsFragment(@NonNull final String name, @Nullable final AutoRecord.TestObject values) {
        this.name = name;
        this.values = values;
        this.patch = new AdaptedPatch();

        if (values != null) {
            this.patch.setFilename(values.filename);
            this.patch.setOffset(Sensor.TYPE_ACCELEROMETER, values.lo_acc);
            this.patch.setOffset(Sensor.TYPE_GYROSCOPE, values.lo_gyro);
            this.patch.setGain(Sensor.TYPE_ACCELEROMETER, values.lg_acc);
            this.patch.setGain(Sensor.TYPE_GYROSCOPE, values.lg_gyro);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filename = view.findViewById(R.id.filename_stg);
        filename.setText(values != null ? values.filename : filename.getText().toString());
        filename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                patch.setFilename(s.toString());
            }
        });

        lo_acc = view.findViewById(R.id.lo_acc_stg);
        lo_acc.setText(values != null ? values.lo_acc : lo_acc.getText().toString());
        lo_acc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                patch.setOffset(Sensor.TYPE_ACCELEROMETER, lo_acc.getText().toString());
            }
        });

        lo_gyro = view.findViewById(R.id.lo_gyro_stg);
        lo_gyro.setText(values != null ? values.lo_gyro : lo_gyro.getText().toString());
        lo_gyro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                patch.setOffset(Sensor.TYPE_GYROSCOPE, lo_gyro.getText().toString());
            }
        });

        lg_acc = view.findViewById(R.id.lg_acc_stg);
        lg_acc.setText(values != null ? values.lg_acc : lg_acc.getText().toString());
        lg_acc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                patch.setGain(Sensor.TYPE_ACCELEROMETER, lg_acc.getText().toString());
            }
        });

        lg_gyro = view.findViewById(R.id.lg_gyro_stg);
        lg_gyro.setText(values != null ? values.lg_gyro : lg_gyro.getText().toString());
        lg_gyro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                patch.setGain(Sensor.TYPE_GYROSCOPE, lg_gyro.getText().toString());
            }
        });

        save = view.findViewById(R.id.save_btn);
        save.setOnClickListener(v -> saveSettings());

        new ViewModelProvider(requireActivity()).get(Utils.class).recording_in_progress.observe(requireActivity(), item -> {
            lo_acc.setEnabled(!item);
            lo_gyro.setEnabled(!item);
            lg_acc.setEnabled(!item);
            lg_gyro.setEnabled(!item);
            save.setEnabled(!item);
        });

        saveSettings();
    }

    @NonNull
    public String toString() {
        return this.name;
    }

    private void saveSettings() {
        patch.setFilename(filename.getText().toString());
        patch.setOffset(Sensor.TYPE_ACCELEROMETER, lo_acc.getText().toString());
        patch.setOffset(Sensor.TYPE_GYROSCOPE, lo_gyro.getText().toString());
        patch.setGain(Sensor.TYPE_ACCELEROMETER, lg_acc.getText().toString());
        patch.setGain(Sensor.TYPE_GYROSCOPE, lg_gyro.getText().toString());
    }
}