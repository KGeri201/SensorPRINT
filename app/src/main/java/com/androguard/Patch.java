package com.androguard;

import java.util.HashMap;
import java.util.Objects;
import java.util.Map;

import android.hardware.SensorEvent;
import android.hardware.Sensor;

import androidx.annotation.NonNull;

/**
 * Class containing the noise generation to be applied to values of the Android
 * Sensor class in order to obscure the builtin error
 * @author  Gergö Kranz
 * @version 1.0
 * @since   17-11-2024
 */
public class Patch {
    static final int AXIS_X = 0;
    static final int AXIS_Y = 1;
    static final int AXIS_Z = 2;

    /**
     * ...
     */
    final Map<Integer, float[]> errors = new HashMap<Integer, float[]>() {{
        put(Sensor.TYPE_ACCELEROMETER, new float[]{0.0f, 0.0f, 0.0f});
        put(Sensor.TYPE_GYROSCOPE, new float[]{0.0f, 0.0f, 0.0f});
    }};

    /**
     * Applies noise to the original sensor value.
     * @param original Original sensor value.
     * @param error ...
     * @return float corrected sensor value.
     */
    float applyCorrection(final float original, final float error) {
        return original - error;
    }

    /**
     * Gets the lambda offset value for the specified sensor.
     * If the key is not found returns 0.
     * @param type sensor type as key.
     * @return ...
     */
    float[] getError(final int type) {
        return Objects.requireNonNull(errors.getOrDefault(type, new float[]{0.0f, 0.0f, 0.0f}));
    }

    /**
     * Selects offset and gain for the appropriate sensor and
     * applies noise to the value if the right sensor is read.
     * @param event SensorEvent.
     * @return SensorEvent the modified event.
     * @see SensorEvent
     */
    public SensorEvent manipulateValues(@NonNull SensorEvent event) {
        final float[] error = getError(event.sensor.getType());

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_GYROSCOPE:
                event.values[AXIS_X] = applyCorrection(event.values[AXIS_X], error[AXIS_X]);    // X
                event.values[AXIS_Y] = applyCorrection(event.values[AXIS_Y], error[AXIS_Y]);    // Y
                event.values[AXIS_Z] = applyCorrection(event.values[AXIS_Z], error[AXIS_Z]);    // Z
                break;
            default:
                break;
        }

        return event;
    }
}
