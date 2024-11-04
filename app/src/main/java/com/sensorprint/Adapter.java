package com.sensorprint;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Adapter extends FragmentStateAdapter {
    public enum Pages {
        MAIN("Home"),
        SETTINGS("Settings");

        private final String name;
        Pages(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public Adapter(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(Pages.values()[position]) {
            case MAIN:
                return new HomeFragment();
            case SETTINGS:
                return new SettingsFragment();
            default:
                throw new IllegalStateException("Unexpected position " + position);
        }
    }

    @Override
    public int getItemCount() {
        return Pages.values().length;
    }
}
