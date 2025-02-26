package com.sensorprint;

import android.app.Activity;
import android.app.AlertDialog;

public class PatchCalibration {
    private static final int datapoints = 100;
    private static boolean test = false;

    public static void showDialog(Activity activity) {
        if(test) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Alert !");
        builder.setMessage("Do you want to exit ?");

        builder.setCancelable(true);

        builder.setPositiveButton("Start", (dialog, which) -> {
            calibrate();
            activity.finish();
            activity.startActivity(activity.getIntent());
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    private static void calibrate() {
        test = true;
    }
}
