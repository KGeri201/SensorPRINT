package com.sensorprint;

import android.annotation.SuppressLint;

import android.app.Activity;

import android.hardware.Sensor;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils extends ViewModel  {
    public static final int[] SENSORS = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};

    public static Map<Integer, MutableLiveData<Float>> lambda_offsets = Map.of(
            Sensor.TYPE_ACCELEROMETER, new MutableLiveData<>(0.0f),
            Sensor.TYPE_GYROSCOPE, new MutableLiveData<>(0.0f)
    );

    public static Map<Integer, MutableLiveData<Float>> lambda_gains = Map.of(
            Sensor.TYPE_ACCELEROMETER, new MutableLiveData<>(0.0f),
            Sensor.TYPE_GYROSCOPE, new MutableLiveData<>(0.0f)
    );

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIMER = new SimpleDateFormat("mm:ss");

    public static HashMap<Integer, SensorValues> original_values = new HashMap<>();
    public static HashMap<Integer, SensorValues> patched_values = new HashMap<>();

    public static MutableLiveData<Long> duration = new MutableLiveData<>(0L);
    public static MutableLiveData<Long> interval = new MutableLiveData<>(0L);

    public static MutableLiveData<Boolean> recording_in_progress = new MutableLiveData<>(false);

    public static void writeCSV(Activity activity, boolean patch) {
        for (int sensor : SENSORS) {
            Recorder.recordValues(activity, Objects.requireNonNull(original_values.get(sensor)), "before");
            if (patch)
                Recorder.recordValues(activity, Objects.requireNonNull(patched_values.get(sensor)), "after");
        }
    }

    public static void saveSettings(CostomTextWatcher.TextFields field, String s) {
        if (s.isEmpty()) s = "0";

        switch(field) {
            case INTERVAL: interval.setValue(Long.parseLong(s));
                break;
            case DURATION: duration.setValue(Long.parseLong(s));
                break;
            case LO_ACC: Objects.requireNonNull(lambda_offsets.get(Sensor.TYPE_ACCELEROMETER)).setValue(Float.parseFloat(s));
                break;
            case LO_GYRO: Objects.requireNonNull(lambda_offsets.get(Sensor.TYPE_GYROSCOPE)).setValue(Float.parseFloat(s));
                break;
            case LG_ACC: Objects.requireNonNull(lambda_gains.get(Sensor.TYPE_ACCELEROMETER)).setValue(Float.parseFloat(s));
                break;
            case LG_GYRO: Objects.requireNonNull(lambda_gains.get(Sensor.TYPE_GYROSCOPE)).setValue(Float.parseFloat(s));
                break;
            default: break;
        }
    }

    public static float getOffset(int type) {
        return Objects.requireNonNull(Objects.requireNonNull(lambda_offsets.getOrDefault(type, new MutableLiveData<>(0.0f))).getValue());
    }

    public static float getGain(int type) {
        return Objects.requireNonNull(Objects.requireNonNull(lambda_gains.getOrDefault(type, new MutableLiveData<>(0.0f))).getValue());
    }
}
