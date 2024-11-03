package com.sensorprint;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Adapter extends FragmentStateAdapter {
    private static final int ITEM_COUNT = 2;

    private static final int MAIN = 0;
    private static final int SETTINGS = 1;

    public Adapter(FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
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
        return ITEM_COUNT;
    }
}
