package com.sensorprint;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class HomeFragment extends Fragment {
    private final String name;

    private Utils viewModel;

    private EditText interval;
    private EditText duration;
    private Button record;
    private Button add_patch;

    public HomeFragment(@NonNull final String name) {
        this.name = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(Utils.class);

        view.findViewById(R.id.app_name).setOnClickListener(v -> AutoRecord.getInstance().setup());

        interval = view.findViewById(R.id.interval_stg);
        interval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setInterval(interval.getText().toString());
            }
        });

        duration = view.findViewById(R.id.duration_stg);
        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setDuration(duration.getText().toString());
            }
        });

        record = view.findViewById(R.id.record_btn);
        record.setOnClickListener(v -> onTimerStart());

        add_patch = view.findViewById(R.id.add_patch_btn);
        add_patch.setOnClickListener(v -> addNewPatch());

        viewModel.recording_in_progress.observe(requireActivity(), item -> {
            interval.setEnabled(!item);
            duration.setEnabled(!item);
            record.setEnabled(!item);
            add_patch.setEnabled(!item);
        });

        saveSettings();
    }

    private void saveSettings() {
        viewModel.setInterval(interval.getText().toString());
        viewModel.setDuration(duration.getText().toString());
    }

    public void onTimerStart() {
        viewModel.recording_in_progress.setValue(true);

        viewModel.cleanCSVs((Activity) getContext());

        new CountDownTimer(viewModel.getDuration(), viewModel.getInterval()) {
            public void onTick(long millisUntilFinished) {
                viewModel.writeCSVs((Activity) getContext());
                record.setText(Utils.TIMER.format(new Date(millisUntilFinished)));
            }

            public void onFinish() {
                viewModel.recording_in_progress.setValue(false);
                record.setText(getString(R.string.record_btn));
            }
        }.start();
    }

    public void addNewPatch() {
        ((MainActivity) requireActivity()).addNewTab();
    }

    @NonNull
    public String toString() {
        return this.name;
    }
}
