package com.sensorprint;

import androidx.annotation.NonNull;

public class AutoRecord {
    static class TestObject {
        public String filename;
        public String lo_acc;
        public String lo_gyro;
        public String lg_acc;
        public String lg_gyro;

        public TestObject(final String filename,
                        final String lo_acc,
                        final String lo_gyro,
                        final String lg_acc,
                        final String lg_gyro) {
            this.filename = filename;
            this.lo_acc = lo_acc;
            this.lo_gyro = lo_gyro;
            this.lg_acc = lg_acc;
            this.lg_gyro = lg_gyro;
        }
    }

    private static AutoRecord INSTANCE;

    private final TestObject[] objects = {
            new TestObject("before", "0", "0", "0", "0"),
            new TestObject("after", "0.5", "0.1", "0.5", "0.5"),
            new TestObject("after_ho", "1", "0.2", "0.5", "0.5"),
            new TestObject("after_hg", "0.5", "0.1", "1", "1"),
            new TestObject("after_hb", "1", "0.2", "1", "1"),
            new TestObject("after_lo", "0.25", "0.05", "0.5", "0.5"),
            new TestObject("after_lg", "0.5", "0.1", "0.25", "0.25"),
            new TestObject("after_lb", "0.25", "0.05", "0.25", "0.25")
    };

    private boolean used = false;

    private PagerAdapter adapter;

    private AutoRecord() { }

    public static AutoRecord getInstance() {
        if(INSTANCE == null) INSTANCE = new AutoRecord();

        return INSTANCE;
    }

    public void setAdapter(@NonNull final PagerAdapter adapter) {
        this.adapter = adapter;
    }

    public void setup() {
        if (used)
            return;

        for (TestObject values : objects)
            adapter.addFragment(new SettingsFragment("Patch " + adapter.getItemCount(), values));

        used = true;
    }

    public void run() {
        setup();
        ((HomeFragment) adapter.getFragment(0)).onTimerStart();
    }
}
