package com.sensorprint;

import android.Manifest;

import android.app.Activity;

import android.content.Context;
import android.content.pm.PackageManager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Utils {
    public static final int[] SENSORS = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};

    public static Map<Integer, Float> lambda_offsets = Map.of(
            Sensor.TYPE_ACCELEROMETER, 0.5f,
            Sensor.TYPE_GYROSCOPE, 0.1f
    );

    public static Map<Integer, Float> lambda_gains = Map.of(
            Sensor.TYPE_ACCELEROMETER, 0.05f,
            Sensor.TYPE_GYROSCOPE, 0.05f
    );

    public static long interval = 250;      // in ms
    public static long duration = 60000;

    public static final String[] TAB_NAMES = {"Home", "Settings"};// in ms

    public static final HashMap<Integer, SensorEvent> original_sensors = new HashMap<>();
    public static final HashMap<Integer, SensorEvent> manipulated_sensors = new HashMap<>();

    private static final int AXIS_X = 0;
    private static final int AXIS_Y = 1;
    private static final int AXIS_Z = 2;

    private static final int STORAGE_PERMISSION_CODE = 23;

    public static void recordValues(@NonNull final Context context, final SensorEvent event, final String path) {
        String name = "";
        String header = "t_unix";
        String content = "" + (System.currentTimeMillis()/1000);

        try {
            switch(event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    name += "ACG";
                    header += ";x;y;z";
                    content += ";" + event.values[AXIS_X];
                    content += ";" + event.values[AXIS_Y];
                    content += ";" + event.values[AXIS_Z];
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    name += "GYRO";
                    header = ";x;y;z";
                    content += ";" + event.values[AXIS_X];
                    content += ";" + event.values[AXIS_Y];
                    content += ";" + event.values[AXIS_Z];
                    break;
                default: return;
            }

            File file = new File(context.getExternalFilesDir(null), path+File.separator+name +".csv");

            if (!file.exists()) {
                if (!path.isEmpty())
                    Objects.requireNonNull(file.getParentFile()).mkdirs();
                file.createNewFile();

                write(file, header);
            }

            write(file, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clean(@NonNull final Context context, final String path) {
        deleteRecursive(new File(context.getExternalFilesDir(null), path));
    }

    private static void deleteRecursive(final File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private static void write(final File file, final String content) throws IOException {
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content + "\n");
        bw.close();
    }

    public static void getPermissions(@NonNull final Context context) {
        if(!checkPermissions(context))
            requestForStoragePermissions((Activity) context);
    }

    private static boolean checkPermissions(@NonNull final Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else {
            final boolean write = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            final boolean read = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            return read && write;
        }
    }

    private static void requestForStoragePermissions(@NonNull final Activity activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
    }
}
