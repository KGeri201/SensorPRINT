package com.sensorprint;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {
    private EditText interval;
    private EditText duration;
    private EditText lo_acc;
    private EditText lo_gyro;
    private EditText lg_acc;
    private EditText lg_gyro;
    private Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        interval = view.findViewById(R.id.interval_stg);
        interval.addTextChangedListener(new CostomTextWatcher(CostomTextWatcher.TextFields.INTERVAL));

        duration = view.findViewById(R.id.duration_stg);
        duration.addTextChangedListener(new CostomTextWatcher(CostomTextWatcher.TextFields.DURATION));

        lo_acc = view.findViewById(R.id.lo_acc_stg);
        lo_acc.addTextChangedListener(new CostomTextWatcher(CostomTextWatcher.TextFields.LO_ACC));

        lo_gyro = view.findViewById(R.id.lo_gyro_stg);
        lo_gyro.addTextChangedListener(new CostomTextWatcher(CostomTextWatcher.TextFields.LO_GYRO));

        lg_acc = view.findViewById(R.id.lg_acc_stg);
        lg_acc.addTextChangedListener(new CostomTextWatcher(CostomTextWatcher.TextFields.LG_ACC));

        lg_gyro = view.findViewById(R.id.lg_gyro_stg);
        lg_gyro.addTextChangedListener(new CostomTextWatcher(CostomTextWatcher.TextFields.LG_GYRO));

        save = view.findViewById(R.id.save_btn);
        save.setOnClickListener(v -> {
            Utils.saveSettings(CostomTextWatcher.TextFields.INTERVAL, interval.getText());
            Utils.saveSettings(CostomTextWatcher.TextFields.DURATION, duration.getText());
            Utils.saveSettings(CostomTextWatcher.TextFields.LO_ACC, lo_acc.getText());
            Utils.saveSettings(CostomTextWatcher.TextFields.LO_GYRO, lo_gyro.getText());
            Utils.saveSettings(CostomTextWatcher.TextFields.LG_ACC, lg_acc.getText());
            Utils.saveSettings(CostomTextWatcher.TextFields.LG_GYRO, lg_gyro.getText());
        });

        Utils.recording_in_progress.observe(requireActivity(), item -> {
            interval.setEnabled(!item);
            duration.setEnabled(!item);
            lo_acc.setEnabled(!item);
            lo_gyro.setEnabled(!item);
            lg_acc.setEnabled(!item);
            lg_gyro.setEnabled(!item);
            save.setEnabled(!item);
        });
    }
}