package com.sensorprint;

import android.hardware.SensorEvent;

import java.util.Arrays;

public class SensorValues {
    public final float[] values;
    private final int type;

    public SensorValues(SensorEvent event) {
        this.type = event.sensor.getType();
        this.values = Arrays.copyOf(event.values, event.values.length);
    }

    public int getType() {
        return type;
    }
}
