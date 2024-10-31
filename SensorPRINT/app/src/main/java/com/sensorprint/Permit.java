package com.sensorprint;

import android.Manifest;
import android.app.Activity;
//import android.app.Instrumentation;
import android.content.Context;
//import android.content.Intent;
import android.content.pm.PackageManager;

//import android.net.Uri;
import android.os.Build;
import android.os.Environment;
//import android.provider.Settings;

//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permit {
    private static final int STORAGE_PERMISSION_CODE = 23;

//    private static ActivityResultLauncher<Intent> launcher = new ActivityResultLauncher();
//    registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>(){
//
//        @Override
//        public void onActivityResult(ActivityResult o) {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//                //Android is 11 (R) or above
//                if(Environment.isExternalStorageManager()){
//                    //Manage External Storage Permissions Granted
//                    Log.d(TAG, "onActivityResult: Manage External Storage Permissions Granted");
//                }else{
//                    Toast.makeText(MainActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                //Below android 11
//
//            }
//        }
//    });

    public static void getPermissions(Context context) {
        if(!checkPermissions(context))
            requestForStoragePermissions((Activity) context);
    }

    private static boolean checkPermissions(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager();
        }else {
            //Below android 11
            final int write = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int read = ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    private static void requestForStoragePermissions(Activity activity) {
        //Android is 11 (R) or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            try {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//                intent.addCategory("android.intent.category.DEFAULT");
//                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
//                launcher.launch(intent);
//            } catch (Exception e) {
//                launcher.launch(new Intent().setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
//            }
        }else {
            //Below android 11
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
