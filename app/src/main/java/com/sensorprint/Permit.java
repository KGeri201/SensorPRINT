package com.sensorprint;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permit {
    private static Permit INSTANCE;

    private final int STORAGE_PERMISSION_CODE = 23;

    private Context context = null;

    private Permit() { }

    public static Permit getInstance() {
        if(INSTANCE == null) INSTANCE = new Permit();

        return INSTANCE;
    }

    public void getPermissions(@NonNull final Context context) {
        this.context = context;

        if(!checkPermissions())
            requestForStoragePermissions();
    }

    private boolean checkPermissions(){
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

    private void requestForStoragePermissions() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    new String[]{
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
    }
}