package com.sensorprint;

import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tablayout = findViewById(R.id.tab_layout);
        ViewPager2 viewpager2 = findViewById(R.id.pager);
        viewpager2.setAdapter(new Adapter(this));

        new TabLayoutMediator(tablayout, viewpager2, (tab, position) ->
                tab.setText(Utils.TAB_NAMES[position])
        ).attach();

        for (int sensor : Utils.SENSORS)
            Utils.sensor_values.put(sensor, null);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Permit.getPermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int sensor : Utils.SENSORS)
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(sensor),
                    SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Utils.sensor_values.replace(event.sensor.getType(), event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}