package com.sensorprint;

import android.text.Editable;
import android.text.TextWatcher;

public class CostomTextWatcher implements TextWatcher {
    public enum TextFields {
        INTERVAL,
        DURATION,
        LO_ACC,
        LO_GYRO,
        LG_ACC,
        LG_GYRO
    }

    private final TextFields field;

    public CostomTextWatcher(TextFields field) {
        this.field = field;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Utils.saveSettings(field, s.toString());
    }
}
