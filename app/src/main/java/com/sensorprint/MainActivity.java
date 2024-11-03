package com.sensorprint;

import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TabLayout tablayout = findViewById(R.id.tab_layout);
        ViewPager2 viewpager2 = findViewById(R.id.pager);

        viewpager2.setAdapter(new Adapter(this));

        new TabLayoutMediator(tablayout, viewpager2, (tab, position) -> tab.setText(Utils.TAB_NAMES[position])).attach();

        for (int sensor : Utils.SENSORS) {
            Utils.original_sensors.put(sensor, null);
            Utils.manipulated_sensors.put(sensor, null);
        }

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Utils.getPermissions(this);

        Utils.clean(this, "before");
        Utils.clean(this, "after");
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
        Utils.original_sensors.replace(event.sensor.getType(), event);
        Patch.manipulateValues(event);
        Utils.manipulated_sensors.replace(event.sensor.getType(), event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}