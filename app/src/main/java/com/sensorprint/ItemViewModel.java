package com.sensorprint;

import android.hardware.Sensor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

public class ItemViewModel extends ViewModel {
    public static Map<Integer, MutableLiveData<Float>> lambda_offsets = Map.of(
            Sensor.TYPE_ACCELEROMETER, new MutableLiveData<>(0.5f),
            Sensor.TYPE_GYROSCOPE, new MutableLiveData<>(0.1f)
    );

    public static Map<Integer, MutableLiveData<Float>> lambda_gains = Map.of(
            Sensor.TYPE_ACCELEROMETER, new MutableLiveData<>(0.5f),
            Sensor.TYPE_GYROSCOPE, new MutableLiveData<>(0.5f)
    );
}
