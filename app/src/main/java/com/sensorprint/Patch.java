package com.sensorprint;

import java.util.Objects;
import java.util.Random;
import java.util.Map;

import android.hardware.SensorEvent;
import android.hardware.Sensor;

/**
 * Class containing the noise generation to be applied to values of the Android
 * Sensor class in order to obscure the builtin error
 * @author  Gergö Kranz
 * @version 1.1
 * @since   03-08-2024
 */
public class Patch {
    private static final Random rnd = new Random();

    private static final int AXIS_X = 0;
    private static final int AXIS_Y = 1;
    private static final int AXIS_Z = 2;

    /**
     * List of 0 +/- offsets for the different sensors.
     */
    private static final Map<Integer, Float> lambda_offsets = Map.of(
            Sensor.TYPE_ACCELEROMETER, 0.5f,
            Sensor.TYPE_GYROSCOPE, 0.1f
    );

    /**
     * List of 1 +/- gains for the different sensors.
     */
    private static final Map<Integer, Float> lambda_gains = Map.of(
            Sensor.TYPE_ACCELEROMETER, 0.05f,
            Sensor.TYPE_GYROSCOPE, 0.05f
    );

    /**
     * Gets a random value between the min and max range, inclusive the edges.
     * @param min Lower bound of the range.
     * @param max Upper bound of the range.
     * @return float random value between min and max.
     */
    private static float generateRandomValue(final float min, final float max) {
        float median = (max / 2) + (min / 2);
        float radius = (max / 2) - (min / 2);
        int invert = rnd.nextBoolean() ? 1 : -1;
        return median + invert * rnd.nextFloat() * radius;
    }

    /**
     * Applies noise to the original sensor value.
     * @param original Original sensor value.
     * @param lambda_offset +/- offset to be applied to the original value.
     * @param lambda_gain 1 +/- gain to be applied to the original value.
     * @return float obscured sensor value.
     */
    private static float applyNoise(final float original, final float lambda_offset, final float lambda_gain) {
        float offset = generateRandomValue(0 - Math.abs(lambda_offset), 0 + Math.abs(lambda_offset));
        float gain = generateRandomValue(1 - Math.abs(lambda_gain), 1 + Math.abs(lambda_gain));

        return (original - offset) / gain;
    }

    /**
     * Gets the lambda offset value for the specified sensor.
     * If the key is not found returns 0.
     * @param type sensor type as key.
     * @return float +/- value offset.
     */
    private static float getOffset(int type) {
        return Objects.requireNonNull(lambda_offsets.getOrDefault(type, 0.0f));
    }

    /**
     * Gets the lambda gain value for the specified sensor.
     * If the key is not found returns 0.
     * @param type sensor type as key.
     * @return float +/- value gain.
     */
    private static float getGain(int type) {
        return Objects.requireNonNull(lambda_gains.getOrDefault(type, 0.0f));
    }

    /**
     * Selects offset and gain for the appropriate sensor and
     * applies noise to the value if the right sensor is read.
     * @param event SensorEvent.
     * @see SensorEvent
     */
    public static void manipulateValues(SensorEvent event) {
        float offset = Utils.getOffset(event.sensor.getType());
        float gain = Utils.getGain(event.sensor.getType());

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
            case Sensor.TYPE_GYROSCOPE:
                event.values[AXIS_X] = applyNoise(event.values[AXIS_X], offset, gain);    // X
                event.values[AXIS_Y] = applyNoise(event.values[AXIS_Y], offset, gain);    // Y
                event.values[AXIS_Z] = applyNoise(event.values[AXIS_Z], offset, gain);    // Z
                break;
            default:
                break;
        }
    }
}