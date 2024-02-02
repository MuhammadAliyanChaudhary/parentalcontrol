package com.example.parentalcontrol.service;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import androidx.core.content.ContextCompat;

public class PhoneCallReceiver extends PhoneStateListener {

    Context context;

    public PhoneCallReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        super.onCallStateChanged(state, phoneNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                // Call ended, stop recording
                stopRecordingService();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                // Call answered, start recording
                startRecordingService();
                break;
            // Handle other states as needed
        }
    }

    private void startRecordingService() {
        Intent serviceIntent = new Intent(context, CallRecordingService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }

    private void stopRecordingService() {
        Intent serviceIntent = new Intent(context, CallRecordingService.class);
        context.stopService(serviceIntent);
    }



}
