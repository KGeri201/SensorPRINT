package com.sensorprint;

import java.util.HashMap;
import java.util.Map;

public class AdaptedPatch extends Patch {
    private String filename;

    private Map<Integer, Float> lambda_offsets = new HashMap<>();
    private Map<Integer, Float> lambda_gains = new HashMap<>();

    public AdaptedPatch() {
        for (int sensor : Utils.SENSORS) {
            lambda_offsets.put(sensor, 0.0F);
            lambda_gains.put(sensor, 0.0F);
        }
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
