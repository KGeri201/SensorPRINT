package com.sensorprint;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private CheckBox manipulate;
    private Button start;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat timer_format = new SimpleDateFormat("mm:ss");

    private final CountDownTimer timer = new CountDownTimer(Utils.duration, Utils.interval) {
        public void onTick(long millisUntilFinished) {
            for (int sensor : Utils.SENSORS) {
                Utils.recordValues(requireActivity(),
                        Objects.requireNonNull(Utils.original_sensors.get(sensor)), "before");
                if (manipulate.isChecked())
                    Utils.recordValues(requireActivity(),
                            Objects.requireNonNull(Utils.manipulated_sensors.get(sensor)), "after");
            }

            start.setText(timer_format.format(new Date(millisUntilFinished)));
        }

        public void onFinish() {
            manipulate.setEnabled(true);
            start.setEnabled(true);
            start.setText(getString(R.string.button_name));
        }
    };

    public void onTimerStart() {
        manipulate.setEnabled(false);
        start.setEnabled(false);
        timer.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        manipulate = view.findViewById(R.id.apply_patch);

        start = view.findViewById(R.id.record);
        start.setText(getString(R.string.button_name));
        start.setOnClickListener(v -> onTimerStart());

        return view;
    }
}
