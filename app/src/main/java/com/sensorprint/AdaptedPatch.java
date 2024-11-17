package com.sensorprint;

import android.hardware.Sensor;
import androidx.annotation.NonNull;

public class AdaptedPatch extends Patch {
    private String filename;

    public AdaptedPatch() {
        for (int sensor : Utils.SENSORS) {
            lambda_offsets.put(sensor, 0.0F);
            lambda_gains.put(sensor, 0.0F);
        }
    }

    public SensorValues manipulateValues(@NonNull SensorValues event) {
        final float offset = super.getOffset(event.sensor.getType());
        final float gain = super.getGain(event.sensor.getType());

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_GYROSCOPE:
                event.values[AXIS_X] = super.applyNoise(event.values[AXIS_X], offset, gain);
                event.values[AXIS_Y] = super.applyNoise(event.values[AXIS_Y], offset, gain);
                event.values[AXIS_Z] = super.applyNoise(event.values[AXIS_Z], offset, gain);
                break;
            default:
                break;
        }

        return event;
    }

    public void setOffset(final int type, String s) {
       s = s.isEmpty() ? "0" : s;
       lambda_offsets.put(type, Float.parseFloat(s));
    }

    public void setGain(final int type, String s) {
        s = s.isEmpty() ? "0" : s;
        lambda_gains.put(type, Float.parseFloat(s));
    }

    public void setFilename(final String filename) {
       this.filename = filename;
   }

    public String getFilename() {
       return filename;
   }
}
