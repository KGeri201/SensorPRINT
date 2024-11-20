package com.sensorprint;

import android.annotation.SuppressLint;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Objects;

public class Utils extends ViewModel  {
    public static final int[] SENSORS = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIMER = new SimpleDateFormat("mm:ss");

    public final MutableLiveData<Boolean> recording_in_progress = new MutableLiveData<>(false);

    private static final Recorder recorder = Recorder.getInstance();

    private static final HashMap<AdaptedPatch, HashMap<Integer, SensorValues>> values = new HashMap<>();

    private final MutableLiveData<Long> interval = new MutableLiveData<>();
    private final MutableLiveData<Long> duration = new MutableLiveData<>();

    private final String ZIPFOLDER = "device_name.zip";

    public void writeCSVs(@NonNull final Context context) {
        values.forEach((key, value) -> {
            for (int sensor : SENSORS)
                recorder.record(Objects.requireNonNull(value.get(sensor)), context.getExternalFilesDir(null) + File.separator + key.getFilename());
        });
    }

    public void cleanCSVs(@NonNull final Context context) {
        values.forEach((key, value) -> recorder.clean(new File(context.getExternalFilesDir(null), key.getFilename())));
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
        values.forEach((key, value) -> value.put(event.sensor.getType(), key.manipulateValues(new SensorValues(event))));
    }

    public static void addNewPatch(@NonNull final AdaptedPatch patch) {
        values.put(patch, new HashMap<Integer, SensorValues>() {{
            for (int sensor : SENSORS)
                put(sensor, null);
        }});
    }

    public void zipFiles(@NonNull final Context context) {
        recorder.zipFiles(context.getExternalFilesDir(null), context.getExternalFilesDir(null).getAbsolutePath() + File.separator + ZIPFOLDER);
    }
}
