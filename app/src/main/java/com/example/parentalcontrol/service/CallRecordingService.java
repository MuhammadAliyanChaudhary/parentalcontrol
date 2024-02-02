package com.example.parentalcontrol.service;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CallRecordingService extends AccessibilityService {




    private MediaRecorder mediaRecorder;

    public static final String ACTION_START_RECORDING = "com.example.parentalcontrol.START_RECORDING";
    public static final String ACTION_STOP_RECORDING = "com.example.parentalcontrol.STOP_RECORDING";

    private boolean isRecording = false;




    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_START_RECORDING));
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_STOP_RECORDING));

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    Log.d("BackgroundTask", "\nBackground task running... " + i);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        //startRecording();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Handle accessibility events if needed
    }

    @Override
    public void onInterrupt() {
       // stopRecording();
    }

    @Override
    public void onDestroy() {
        // Unregister the broadcast receiver when the service is destroyed
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void startRecording() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Set the output file path
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/your_directory/";
        File directory = new File(filePath);
        directory.mkdirs();

        String fileName = "your_output_file.3gp";
        String fullPath = filePath + fileName;

        // Create a MediaRecorder instance
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(fullPath);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            showToast("Recording Started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                switch (intent.getAction()) {
                    case ACTION_START_RECORDING:
                        startRecording();
                        break;
                    case ACTION_STOP_RECORDING:
                        stopRecording();
                        break;
                }
            }
        }
    };

    private void stopRecording() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            showToast("Recording Stopped");
        }
    }

    // Method to show a toast message
    private void showToast(String message) {
        Context applicationContext = getApplicationContext();
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show();
    }




}
