package com.r42914lg.tutu.utils;

import static com.r42914lg.tutu.Constants.LOG;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.r42914lg.tutu.model.TuTuViewModel;
import java.util.Map;

/**
 * Auxiliary class to check permissions, notifies ViewModel of failure via callback
 * onPermissionsCheckFailed() only if permissions check not passed
 */

public class PermissionsHelper {
    public static final String TAG = "LG> PermissionsHelper";

    private final AppCompatActivity appCompatActivity;

    private final String [] permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE
    };

    private final ActivityResultLauncher<String[]> requestPermissionLauncher;

    public PermissionsHelper(AppCompatActivity appCompatActivity, TuTuViewModel tuTuViewModel) {
        this.appCompatActivity = appCompatActivity;

        requestPermissionLauncher =  appCompatActivity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> result) {
                        if (result.containsValue(false)) {
                            tuTuViewModel.onPermissionsCheckFailed();
                            if (LOG) {
                                Log.d(TAG, ".onActivityResult --> NOT ALL permissions granted");
                            }
                        } else {
                            if (LOG) {
                                Log.d(TAG, ".onActivityResult --> ALL permissions granted");
                            }
                        }
                    }
                });

        if (LOG) {
            Log.d(TAG, "  instance created");
        }

        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(appCompatActivity, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(appCompatActivity, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(appCompatActivity, Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {

            if (LOG) {
                Log.d(TAG, ".checkPermissions PASSED, calling onPermissionsCheckPassed() on VM");
            }

        } else {
            if (LOG) {
                Log.d(TAG, ".checkPermissions --> requesting permissions via activity");
            }
            requestPermissionLauncher.launch(permissions);
        }
    }
}
