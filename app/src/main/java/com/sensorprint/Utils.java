package com.sensorprint;

import android.annotation.SuppressLint;

import android.app.Activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Objects;

public class Utils extends ViewModel  {
    public static final int[] SENSORS = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIMER = new SimpleDateFormat("mm:ss");

    public static final HashMap<AdaptedPatch, HashMap<Integer, SensorValues>> values = new HashMap<>();

    public final MutableLiveData<Boolean> recording_in_progress = new MutableLiveData<>(false);

    public final MutableLiveData<Long> interval = new MutableLiveData<>();
    public final MutableLiveData<Long> duration = new MutableLiveData<>();

    public void writeCSVs(final Activity activity) {
        values.forEach((key, value) -> {
            for (int sensor : SENSORS)
                Recorder.record(activity, Objects.requireNonNull(value.get(sensor)), key.getFilename());
        });
    }

    public void cleanCSVs(final Activity activity) {
        values.forEach((key, value) -> Recorder.clean(activity, key.getFilename()));
    }

    public void setInterval(final String s) {
        interval.setValue(Long.parseLong(s.isEmpty() ? "0" : s));
    }

    public void setDuration(final String s) {
        duration.setValue(Long.parseLong(s.isEmpty() ? "0" : s));
    }

    public long getInterval() {
        return Objects.requireNonNull(interval.getValue());
    }

    public long getDuration() {
        return Objects.requireNonNull(duration.getValue());
    }

    public void saveValues(@NonNull SensorEvent event) {
        values.forEach((key, value) -> {
            key.manipulateValues(event);
            value.put(event.sensor.getType(), new SensorValues(event));
        });
    }

    public static void initArray(@NonNull final AdaptedPatch patch) {
        values.put(patch, new HashMap<Integer, SensorValues>() {{
            for (int sensor : SENSORS) {
                put(sensor, null);
            }
        }});
    }
}
