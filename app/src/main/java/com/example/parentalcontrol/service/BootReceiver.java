package com.example.parentalcontrol.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("RestartPhone", "onReceive: ");
        if (intent.getAction() != null) {
            if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

                Log.d("RestartPhone", "Action Boot Compeleted: ");
                Intent serviceIntent = new Intent(context, ForegroundServiceWithNotification.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ContextCompat.startForegroundService(context,serviceIntent);
                } else {
                    context.startService(serviceIntent);
                }
            }
        }

    }



}
