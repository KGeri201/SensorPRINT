package com.sensorprint;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.Date;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private Button record;
    private CheckBox patch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patch = view.findViewById(R.id.apply_chk);

        record = view.findViewById(R.id.record_btn);
        record.setOnClickListener(v -> onTimerStart());

        Utils.recording_in_progress.observe(requireActivity(), item -> {
            patch.setEnabled(!item);
            record.setEnabled(!item);
        });
    }

    public void onTimerStart() {
        Utils.recording_in_progress.setValue(true);

        Recorder.clean(requireContext(), "before");
        Recorder.clean(requireContext(), "after");

        new CountDownTimer(Objects.requireNonNull(Utils.duration.getValue()), Objects.requireNonNull(Utils.interval.getValue())) {
            public void onTick(long millisUntilFinished) {
                Utils.writeCSV((Activity) getContext(), patch.isChecked());
                record.setText(Utils.TIMER.format(new Date(millisUntilFinished)));
            }

            public void onFinish() {
                Utils.recording_in_progress.setValue(false);
                record.setText(getString(R.string.record_btn));
            }
        }.start();
    }
}
