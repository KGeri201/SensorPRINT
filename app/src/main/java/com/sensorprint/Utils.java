package com.sensorprint;

import android.annotation.SuppressLint;

import android.app.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils extends ViewModel  {
    public static final int[] SENSORS = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};

    public static HashMap<Integer, SensorEvent> sensor_values = new HashMap<>();

    public static Map<Integer, MutableLiveData<Float>> lambda_offsets = Map.of(
            Sensor.TYPE_ACCELEROMETER, new MutableLiveData<>(0.5f),
            Sensor.TYPE_GYROSCOPE, new MutableLiveData<>(0.1f)
    );

    public static Map<Integer, MutableLiveData<Float>> lambda_gains = Map.of(
            Sensor.TYPE_ACCELEROMETER, new MutableLiveData<>(0.5f),
            Sensor.TYPE_GYROSCOPE, new MutableLiveData<>(0.5f)
    );

    public static final String[] TAB_NAMES = {"Home", "Settings"};

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat timer = new SimpleDateFormat("mm:ss");

    public static MutableLiveData<Long> duration = new MutableLiveData<>(60000L);
    public static MutableLiveData<Long> interval = new MutableLiveData<>(250L);

    public static MutableLiveData<Boolean> recording_in_progress = new MutableLiveData<>(false);

    public static void saveSensorValues(Activity activity, boolean patch) {
        for (int sensor : SENSORS) {
            SensorEvent event = Objects.requireNonNull(sensor_values.get(sensor));
            Recorder.recordValues(activity, event, "before");
            if (patch) {
                Patch.manipulateValues(event);
                Recorder.recordValues(activity, event, "after");
            }
        }
    }
}
