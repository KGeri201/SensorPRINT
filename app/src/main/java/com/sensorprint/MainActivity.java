package com.sensorprint;

import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static SensorManager sensorManager;

    private PagerAdapter pagerAdapter;
    private Utils viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(Utils.class);

        TabLayout tablayout = findViewById(R.id.tab_layout);
        ViewPager2 viewpager2 = findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(this);
        viewpager2.setAdapter(pagerAdapter);

        new TabLayoutMediator(tablayout, viewpager2, (tab, position) ->
                tab.setText(pagerAdapter.getFragment(position).toString())
        ).attach();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Permit.getInstance().getPermissions(this);

        AutoRecord.getInstance().setAdapter(pagerAdapter);
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
        viewModel.saveValues(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void addNewTab() {
        pagerAdapter.addFragment(new SettingsFragment("Patch " + pagerAdapter.getItemCount(), null));
    }
}