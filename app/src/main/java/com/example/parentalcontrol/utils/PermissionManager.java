package com.example.parentalcontrol.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {

    private Activity activity;
    private int requestCode;


    public PermissionManager(Activity activity, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
    }

    public boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void requestPermissionsWithRationale(String[] permissions, String rationale, Boolean showRationale) {

        if (showRationale) {
            // Show a rationale for why the permissions are needed.
            showRationaleDialog(permissions, rationale);
        } else {
            // No rationale needed; request the permissions.
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    private void showRationaleDialog(final String[] permissions, String rationale) {
        // Show a dialog explaining why the permissions are needed.
        // You can customize the dialog's appearance and behavior.
        // Use a AlertDialog, Snackbar, or any other UI element as needed.
        // Example: AlertDialog.Builder
        new AlertDialog.Builder(activity)
                .setMessage(rationale)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                })
                .create()
                .show();
    }




}

