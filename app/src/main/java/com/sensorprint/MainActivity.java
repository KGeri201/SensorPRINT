package com.sensorprint;

import com.androguard.PatchCalibration;

import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static SensorManager sensorManager;
    private EditText interval;
    private EditText duration;
    private Button record;
    private CheckBox apply_patch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PatchCalibration.run(this);

        SetupFragment();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Utils.setFilesDir(Objects.requireNonNull(getExternalFilesDir(null)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int sensor : Utils.SENSORS)
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(sensor),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void SetupFragment() {
        interval = findViewById(R.id.interval_stg);
        interval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) { saveSettings(); }
        });

        duration = findViewById(R.id.duration_stg);
        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) { saveSettings(); }
        });

        record = findViewById(R.id.record_btn);
        record.setOnClickListener(v -> onTimerStart());

        apply_patch = findViewById(R.id.apply_patch_chk);
        apply_patch.setOnClickListener(v -> saveSettings());

        saveSettings();
    }

    private void saveSettings() {
        Utils.setInterval(interval.getText().toString());
        Utils.setDuration(duration.getText().toString());
        Utils.setPatchApplication(apply_patch.isChecked());
    }

    private void enableFields(boolean state) {
        interval.setEnabled(state);
        duration.setEnabled(state);
        record.setEnabled(state);
        apply_patch.setEnabled(state);
    }

    public void onTimerStart() {
        enableFields(false);

        Utils.cleanCSVs();

        new CountDownTimer(Utils.getDuration(), Utils.getInterval()) {
            public void onTick(long millisUntilFinished) {
                Utils.writeCSVs();
                record.setText(Utils.TIMER.format(new Date(millisUntilFinished)));
            }

            public void onFinish() {
                Utils.zipFiles();
                record.setText(getString(R.string.record_btn));
                enableFields(true);
            }
        }.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Utils.saveValues(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}