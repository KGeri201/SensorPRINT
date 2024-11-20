package com.sensorprint;

import android.hardware.Sensor;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Recorder {
    final int AXIS_X = 0;
    final int AXIS_Y = 1;
    final int AXIS_Z = 2;

    private static Recorder INSTANCE;

    private Recorder() { }

    public static Recorder getInstance() {
        if(INSTANCE == null) INSTANCE = new Recorder();

        return INSTANCE;
    }

    public void record(final SensorValues event, final String path) {
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
                    header += ";x;y;z";
                    content += ";" + event.values[AXIS_X];
                    content += ";" + event.values[AXIS_Y];
                    content += ";" + event.values[AXIS_Z];
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

    public void clean(@NonNull final File dir) {
        deleteRecursive(dir);
    }

    private void deleteRecursive(@NonNull final File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private void write(@NonNull final File file, final String content) throws IOException {
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content + "\n");
        bw.close();
    }

    private List<String> getFiles(File dir) throws IOException {
        List<String> filesListInDir = new ArrayList<>();

        File[] files = dir.listFiles();
        assert files != null;
        for(File file : files){
            if(file.isFile()) filesListInDir.add(file.getAbsolutePath());
            else filesListInDir.addAll(getFiles(file));
        }

        return filesListInDir;
    }

    public void zipFiles(File fileOrDirectory, String zipDirName) {
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