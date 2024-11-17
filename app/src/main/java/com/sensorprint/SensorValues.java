package com.sensorprint;

import android.hardware.SensorEvent;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class SensorValues {
    public static class Sensor {
        private final int type;

        public Sensor(@NonNull final android.hardware.Sensor sensor) {
            this.type = sensor.getType();
        }

        public int getType() {
            return type;
        }
    }

    public final float[] values;
    public final Sensor sensor;

    public SensorValues(@NonNull final SensorEvent event) {
        this.sensor = new Sensor(event.sensor);
        this.values = Arrays.copyOf(event.values, event.values.length);
    }


}
