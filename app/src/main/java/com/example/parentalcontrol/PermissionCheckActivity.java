package com.example.parentalcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.parentalcontrol.utils.PermissionManager;

public class PermissionCheckActivity extends AppCompatActivity{


    private PermissionManager permissionManager;
    private int REQUEST_CODE = 101;
    String[] permissions = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_check);

        permissionManager = new PermissionManager(this, REQUEST_CODE);

        if(!permissionManager.checkPermissions(permissions)){
            permissionManager.requestPermissionsWithRationale(permissions, getString(R.string.missing_permission_explanation), false);
        }else{
            Toast.makeText(this, "Permission are granted", Toast.LENGTH_SHORT).show();
        }




    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                Toast.makeText(this, "Permissions are granted", Toast.LENGTH_SHORT).show();
            } else {
                permissionManager.requestPermissionsWithRationale(permissions, getString(R.string.missing_permission_explanation), true);
            }
        }
    }
}