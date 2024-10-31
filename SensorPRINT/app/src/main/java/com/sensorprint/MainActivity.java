package com.sensorprint;

import android.os.Bundle;
import android.os.CountDownTimer;

import android.widget.Button;
import android.widget.CheckBox;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.annotation.SuppressLint;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;
import java.util.HashMap;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private final int[] sensors = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};

    private final long interval = 250;      // in ms
    private final long duration = 60000;    // in ms

    private final HashMap<Integer, SensorEvent> original_sensors = new HashMap<>();
    private final HashMap<Integer, SensorEvent> manipulated_sensors = new HashMap<>();

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat timer_format = new SimpleDateFormat("mm:ss");

    private SensorManager sensorManager;
    private CheckBox manipulate;
    private Button start;

    private final CountDownTimer timer = new CountDownTimer(duration, interval) {
        public void onTick(long millisUntilFinished) {
            for (int sensor : sensors) {
                Recorder.recordValues(getApplicationContext(),
                        Objects.requireNonNull(original_sensors.get(sensor)), "before");
                if (manipulate.isChecked())
                    Recorder.recordValues(getApplicationContext(),
                            Objects.requireNonNull(manipulated_sensors.get(sensor)), "after");
            }

            start.setText(timer_format.format(new Date(millisUntilFinished)));
        }

        public void onFinish() {
            manipulate.setEnabled(true);
            start.setEnabled(true);
            start.setText(getString(R.string.button_name));
        }
    };

    public void onTimerStart() {
        manipulate.setEnabled(false);
        start.setEnabled(false);
        timer.start();
    }

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

        for (int sensor : sensors) {
            original_sensors.put(sensor, null);
            manipulated_sensors.put(sensor, null);
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        manipulate = findViewById(R.id.checkBox);

        start = findViewById(R.id.button);
        start.setText(getString(R.string.button_name));
        start.setOnClickListener(v -> onTimerStart());

        Permit.getPermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int sensor : sensors)
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(sensor),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        original_sensors.replace(event.sensor.getType(), event);
        Patch.manipulateValues(event);
        manipulated_sensors.replace(event.sensor.getType(), event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}