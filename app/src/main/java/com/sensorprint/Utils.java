package com.sensorprint;

import com.sensorguard.Patch;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils  {
    private static final String ZIPFOLDER = "device_name.zip";
    public static final int[] SENSORS = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE};

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIMER = new SimpleDateFormat("mm:ss");

    private static final Map<String, HashMap<Integer, float[]>> values = new HashMap<String, HashMap<Integer, float[]>>() {{
        put("before", new HashMap<>());
        put("after", new HashMap<>());
    }};

    private static final MutableLiveData<Long> interval = new MutableLiveData<>();
    private static final MutableLiveData<Long> duration = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> apply_patch = new MutableLiveData<>();

    private static final Patch patch = new Patch();

    private static File dir;

    public static void setFilesDir(@NonNull final File filesdir) {
        dir = filesdir;
    }

    public static void writeCSVs() {
        values.forEach((key, entry) ->
            entry.forEach((sensor, value) ->  record(sensor, value, dir.getAbsolutePath() + File.separator + key)));
    }

    public static void cleanCSVs() {
        values.forEach((key, value) -> clean(new File(dir.getAbsolutePath(), key)));
    }

    public static void setInterval(final String s) {
        interval.setValue(Long.parseLong(s.isEmpty() ? "0" : s));
    }

    public static void setDuration(final String s) {
        duration.setValue(Long.parseLong(s.isEmpty() ? "0" : s));
    }

    public static void setPatchApplication(final Boolean b) {
        apply_patch.setValue(b);
    }

    public static long getInterval() {
        return Objects.requireNonNull(interval.getValue());
    }

    public static long getDuration() {
        return Objects.requireNonNull(duration.getValue());
    }

    public static void saveValues(@NonNull SensorEvent event) {
        Objects.requireNonNull(values.get("before")).put(event.sensor.getType(), event.values.clone());
        if (Boolean.TRUE.equals(apply_patch.getValue())) {
            patch.manipulateValues(event);
            Objects.requireNonNull(values.get("after")).put(event.sensor.getType(), event.values.clone());
        }
    }

    public static void zipFiles() {
        zipFiles(dir, dir.getAbsolutePath() + File.separator + ZIPFOLDER);
    }

    public static void record(final int sensor, final float[] values, final String path) {
        String name = "";
        String header = "t_unix";
        String content = "" + (System.currentTimeMillis()/1000);

        try {
            switch(sensor) {
                case Sensor.TYPE_ACCELEROMETER:
                    name += "ACG";
                    header += ";x;y;z";
                    content += ";" + values[Patch.AXIS_X];
                    content += ";" + values[Patch.AXIS_Y];
                    content += ";" + values[Patch.AXIS_Z];
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    name += "GYRO";
                    header += ";x;y;z";
                    content += ";" + values[Patch.AXIS_X];
                    content += ";" + values[Patch.AXIS_Y];
                    content += ";" + values[Patch.AXIS_Z];
                    break;
                default: return;
            }

            File file = new File(path, name + ".csv");

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

    public static void clean(@NonNull final File dir) {
        deleteRecursive(dir);
    }

    private static void deleteRecursive(@NonNull final File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private static void write(@NonNull final File file, final String content) throws IOException {
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content + "\n");
        bw.close();
    }

    private static List<String> getFiles(File dir) throws IOException {
        List<String> filesListInDir = new ArrayList<>();

        File[] files = dir.listFiles();
        assert files != null;
        for(File file : files){
            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else filesListInDir.addAll(getFiles(file));
        }

        return filesListInDir;
    }

    public static void zipFiles(File fileOrDirectory, String zipDirName) {
        try {
            List<String> filesListInDir = getFiles(fileOrDirectory);
            FileOutputStream fos = new FileOutputStream(zipDirName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for(String filePath : filesListInDir){
                zos.putNextEntry(new ZipEntry(filePath.substring(fileOrDirectory.getAbsolutePath().length()+1)));
                FileInputStream fis = new FileInputStream(filePath);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
