package com.sensorprint;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permit {
    private static final int STORAGE_PERMISSION_CODE = 23;

    public static void getPermissions(Context context) {
        if(!checkPermissions(context))
            requestForStoragePermissions((Activity) context);
    }

    private static boolean checkPermissions(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }else {
            final boolean write = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
            final boolean read = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            return read && write;
        }
    }

    private static void requestForStoragePermissions(Activity activity) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
    }
}