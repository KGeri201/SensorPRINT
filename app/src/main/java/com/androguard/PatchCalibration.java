package com.androguard;

import com.sensorprint.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class PatchCalibration implements SensorEventListener  {
    private static final HashMap<Integer, List<float[]>> values = new HashMap<>();
    private static final PatchCalibration INSTANCE = new PatchCalibration();
    private static SensorManager sensorManager;
    private Activity activity;

    private PatchCalibration() { }

    public static PatchCalibration getInstance() {
        return INSTANCE;
    }

    public static void run(Activity activity) {
        if(Patch.config.exists()) return;

        final PatchCalibration instance = getInstance();
        instance.setActivity(activity);
        instance.showDialog();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.androguard_msg);
        builder.setCancelable(true);
        builder.setPositiveButton("Start", (dialog, which) -> approve());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void approve() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.start_calibration);
        builder.setCancelable(true);
        builder.setPositiveButton("Continue", (dialog, which) -> gatherValues());
        builder.show();
    }

    private void gatherValues() {
        final int interval = 10000;
        final String[] progress = {".", "..", "..."};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.calibration_in_progress);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);

        new CountDownTimer(interval, 500) {
            int i = 0;
            public void onTick(long millisUntilFinished) {
                dialog.setMessage(activity.getString(R.string.calibration_in_progress) + progress[++i % progress.length]);
            }

            public void onFinish() {
                sensorManager.unregisterListener(getInstance());
                try {
                    save();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                restart();
            }
        }.start();
    }

    private void restart() {
        PackageManager packageManager = activity.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(activity.getPackageName());
        Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
        mainIntent.setPackage(activity.getPackageName());
        activity.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    private void save() throws IOException, JSONException {
        JSONObject corrections = new JSONObject();

        for(int sensor : values.keySet())
            corrections.put(String.valueOf(sensor), new JSONArray(computeCorrections(sensor)));

        FileWriter file = new FileWriter(Patch.config);
        file.write(corrections.toString());
        file.close();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!values.containsKey(event.sensor.getType()))
            values.put(event.sensor.getType(), new ArrayList<>());
        Objects.requireNonNull(values.get(event.sensor.getType())).add(event.values.clone());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float[] computeCorrections(final int sensor) {
        switch (sensor) {
            case Sensor.TYPE_GYROSCOPE:
                return computeMean(Objects.requireNonNull(values.get(sensor)));
            case Sensor.TYPE_ACCELEROMETER:
                float[] mean = computeMean(Objects.requireNonNull(values.get(sensor)));
                int gravity_axis = 0;
                for (int i = 1; i < mean.length; ++i)
                    if (mean[i] > mean[gravity_axis])
                        gravity_axis = i;

                mean[gravity_axis] -= 9.81f;
                return mean;
        }

        return new float[]{0f, 0f, 0f};
    }

    private float[] computeMean(List<float[]> data) {
        float[] tmp = new float[data.get(0).length];

        for (float[] values : data)
            for (int i = 0; i < values.length; ++i)
                tmp[i] += values[i];

        for (int i = 0; i < tmp.length; ++i)
            tmp[i] /= data.size();

        return tmp;
    }
}