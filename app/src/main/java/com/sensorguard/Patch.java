package com.sensorguard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import android.hardware.SensorEvent;
import android.hardware.Sensor;
import android.os.Environment;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class containing the noise generation to be applied to values of the Android
 * Sensor class in order to obscure the builtin error
 * @author  Gergö Kranz
 * @version 1.0
 * @since
 */
public class Patch {
    public static final int AXIS_X = 0;
    public static final int AXIS_Y = 1;
    public static final int AXIS_Z = 2;
    public static final File config = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "androguard.json");

    /**
     * ...
     */
    private static JSONObject corrections = new JSONObject();

    /**
     * ...
     */
    static {
        try {
            String jsonData = new String(Files.readAllBytes(config.toPath()));
            corrections = new JSONObject(jsonData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the lambda offset value for the specified sensor.
     * If the key is not found returns 0.
     *
     * @param type sensor type as key.
     * @return ...
     */
    private JSONArray getCorrection(final int type) throws JSONException {
        if(corrections.has(String.valueOf(type)))
            return corrections.getJSONArray(String.valueOf(type));

        return new JSONArray() {{
            put(0.0f);
            put(0.0f);
            put(0.0f);
        }};
    }

    /**
     * Selects offset and gain for the appropriate sensor and
     * applies noise to the value if the right sensor is read.
     * @param event SensorEvent.
     * @return SensorEvent the modified event.
     * @see SensorEvent
     */
    @SuppressWarnings("UnusedReturnValue")
    public SensorEvent manipulateValues(@NonNull SensorEvent event)  {
        try {
            final int type = event.sensor.getType();

            final JSONArray correction = getCorrection(type);

            switch (type) {
                case Sensor.TYPE_ACCELEROMETER:
                case Sensor.TYPE_GYROSCOPE:
                    event.values[AXIS_Z] -= (float) correction.getDouble(AXIS_Z);
                    event.values[AXIS_Y] -= (float) correction.getDouble(AXIS_Y);
                    event.values[AXIS_X] -= (float) correction.getDouble(AXIS_X);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return event;
    }
}
