package com.sensorprint;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
//import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Recorder {
    private static final int AXIS_X = 0;
    private static final int AXIS_Y = 1;
    private static final int AXIS_Z = 2;

    public static void recordValues(final Context context, final SensorEvent event, final String path) {
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

//            File file = new File(Environment.getExternalStoragePublicDirectory(null), name +".csv");
            File file = new File(context.getExternalFilesDir(null), path+File.separator+name +".csv");

            if (!file.exists()) {
                new File(context.getExternalFilesDir(null), path).mkdirs();
                file.createNewFile();

                write(file, header);
            }

            write(file, content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write(File file, String content) throws IOException {
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content + "\n");
        bw.close();
    }
}
