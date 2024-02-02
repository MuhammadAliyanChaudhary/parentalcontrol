package com.example.parentalcontrol;

import static android.provider.Telephony.Carriers.PORT;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.example.parentalcontrol.activities.AppsInstalledActivity;
import com.example.parentalcontrol.activities.BaseActivity;
import com.example.parentalcontrol.activities.CallLogActivity;
import com.example.parentalcontrol.activities.ContactsActivity;
import com.example.parentalcontrol.activities.SmsActivity;
import com.example.parentalcontrol.databinding.ActivityMainBinding;
import com.example.parentalcontrol.service.CallRecordingService;
import com.example.parentalcontrol.service.ForegroundServiceWithNotification;
import com.example.parentalcontrol.service.LocationService;
import com.example.parentalcontrol.service.MyForegroundService;
import com.example.parentalcontrol.service.NoNotificationForegroundService;
import com.example.parentalcontrol.service.PhoneCallReceiver;
import com.example.parentalcontrol.utils.AppUtils;
import com.example.parentalcontrol.utils.DeviceUtils;
import com.example.parentalcontrol.utils.MemoryUtils;
import com.example.parentalcontrol.utils.PermissionManager;
import com.google.firebase.FirebaseApp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends BaseActivity {


    private static final String SERVER_IP = "172.16.10.235";
    private static final int SERVER_PORT = 12345;

    private ActivityMainBinding binding;
    private static String TAG = "Attribute";
    private PermissionManager permissionManager;
    private int REQUEST_CODE = 101;

    boolean isRecordingEnabled = false;

    private PhoneCallReceiver phoneCallReceiver;
    Bundle bundle;


    private MediaProjectionManager projectionManager;
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;
    private MediaRecorder mediaRecorder;
    private int screenWidth;
    private int screenHeight;
    private int screenDensity;
    private ServerSocket serverSocket;
    private static final int PORT = 8080; // Choose a port number


    String[] permissions = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO
            // Add more permissions here
    };

    String[] permissionsAndroid10 = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.RECORD_AUDIO
            // Add more permissions here
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeViews();

        ignoreBatteryOptimizations();
        //startClientThread();


        //startLocationServices();

        if(!foregroundServiceRunning()){
            startForegroundServiceWithNotification();
        }
        startClientThread();
        checkAndroidVersions();
        //startNoNotificationBackgroundService();


        /*if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){

            if(isAccessibilityServiceEnabled(this, CallRecordingService.class)){

            }else{
                showEnableAccessibilityServiceDialog();
            }

        }else{
            phoneCallReceiver = new PhoneCallReceiver(this);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(phoneCallReceiver, PhoneStateListener.LISTEN_CALL_STATE);
        }*/




        //startForegroundLocationService();

        binding.shareScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqScreenCapturePermission();
            }
        });


        binding.startAndStopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtils.getRecordingState(getApplicationContext())){
                    Intent stopRecordingIntent = new Intent(CallRecordingService.ACTION_STOP_RECORDING);
                    sendBroadcast(stopRecordingIntent);
                    AppUtils.saveRecordingState(getApplicationContext(), false);
                }else{
                    Intent startRecordingIntent = new Intent(CallRecordingService.ACTION_START_RECORDING);
                    sendBroadcast(startRecordingIntent);
                    AppUtils.saveRecordingState(getApplicationContext(),true);
                }
            }
        });

        binding.imagesAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("media", "Images");
                openActivity(GallaryDataActivity.class, bundle);
            }
        });

        binding.videosAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("media", "Videos");
                openActivity(GallaryDataActivity.class, bundle);
            }
        });

        binding.documentsAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("media", "Documents");
                openActivity(GallaryDataActivity.class, bundle);
            }
        });

        binding.contactsAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ContactsActivity.class, null);
            }
        });

        binding.smsAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SmsActivity.class, null);
            }
        });

        binding.callLogsAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CallLogActivity.class, null);
            }
        });


        binding.appsAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AppsInstalledActivity.class, null);
            }
        });



    }

    private void reqScreenCapturePermission() {
        Intent projectionIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            projectionIntent = projectionManager.createScreenCaptureIntent();
        }
        startActivityForResult(projectionIntent, REQUEST_CODE_SCREEN_CAPTURE);
    }

    private void initializeViews() {
        FirebaseApp.initializeApp(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        permissionManager = new PermissionManager(this, REQUEST_CODE);
    }

    private void ignoreBatteryOptimizations() {
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            if (!((PowerManager) getSystemService(Context.POWER_SERVICE)).isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
    }
    private void startLocationServices() {
        // Start the location service
        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    private void showEnableAccessibilityServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Accessibility Service");
        builder.setMessage("Please enable the accessibility service in device settings.");
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAccessibilitySettings();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
        String service = context.getPackageName() + "/" + accessibilityService.getName();

        String enabledServices = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        );

        return enabledServices != null && enabledServices.contains(service);
    }


    private void checkDeviceStats() {
        binding.androidVersionText.setText(DeviceUtils.getAndroidVersion());
        binding.deviceModelText.setText(DeviceUtils.getDeviceModel());
        binding.imeiText.setText(DeviceUtils.getIMEI(this));
        binding.ramText.setText(MemoryUtils.getTotalRAM(this));
        binding.storageText.setText(MemoryUtils.getTotalStorage());
        binding.availableStorageText.setText(MemoryUtils.getAvailableStorage());
        binding.batteryText.setText(DeviceUtils.getBatteryLevel(this)+"%");

        Log.d(TAG, "androidVersion: "+ DeviceUtils.getAndroidVersion());
        Log.d(TAG, "deviceModel: "+DeviceUtils.getDeviceModel());
        Log.d(TAG, "batteryLevel: "+DeviceUtils.getBatteryLevel(this)+"%");
        Log.d(TAG, "imei: "+DeviceUtils.getIMEI(this));
        Log.d(TAG, "totalRAM: "+MemoryUtils.getTotalRAM(this));
        Log.d(TAG, "totalStorage: "+MemoryUtils.getTotalStorage());
    }

    private void openAccessibilitySettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, 0);
    }


    private void startForegroundLocationService(){
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        }
        startService(serviceIntent);
    }


    private void startNoNotificationBackgroundService(){
        Intent serviceIntent = new Intent(this, NoNotificationForegroundService.class);
        startService(serviceIntent);
    }


    private void startForegroundServiceWithNotification(){
        Intent serviceIntent = new Intent(this, ForegroundServiceWithNotification.class);
        serviceIntent.putExtra("inputExtra", "Service Running for Notification");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, serviceIntent);
        }
        startService(serviceIntent);
    }

    public boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(ForegroundServiceWithNotification.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




    private void checkAndroidVersions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            checkPermissions(permissionsAndroid10);
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                checkPermissions(permissionsAndroid10);
            }else{
                checkPermissions(permissions);
            }

        }
    }


    private void checkPermissions(String[] permissions){
        if(permissionManager.checkPermissions(permissions)){
            checkDeviceStats();
        }else{
            permissionManager.requestPermissionsWithRationale(permissions, getString(R.string.missing_permission_explanation), false);
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
                checkDeviceStats();
            } else {
                permissionManager.requestPermissionsWithRationale(permissions, getString(R.string.missing_permission_explanation), true);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {
            if (resultCode == RESULT_OK) {

                startSocket(resultCode, data);
            } else {
                Toast.makeText(this, "Screen Capture permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSocket(int resultCode, Intent data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create a socket and connect to the server
                    Socket clientSocket = new Socket(SERVER_IP, PORT);

                    // Start screen sharing when a connection is established
                    if (clientSocket != null) {
                        MediaProjection mediaProjection = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            mediaProjection = projectionManager.getMediaProjection(resultCode, data);
                        }
                        startScreenSharing(mediaProjection, clientSocket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void startScreenSharing(MediaProjection mediaProjection, Socket clientSocket) {
        ParcelFileDescriptor pfd = ParcelFileDescriptor.fromSocket(clientSocket);
        Surface surface = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            surface = mediaRecorder.getSurface();
        }
        mediaRecorder.setOutputFile(pfd.getFileDescriptor());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjection.createVirtualDisplay(
                    "ScreenSharingDemo",
                    screenWidth,
                    screenHeight,
                    screenDensity,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    surface,
                    null,
                    null
            );
        }
        mediaRecorder.start();
    }




    private void startClientThread(){
        Thread thread = new Thread(new ClientThread());
        thread.start();
    }








    private class ClientThread implements Runnable {
        @Override
        public void run() {
            try {
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                InputStream inputStream = socket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    String message = new String(buffer, 0, bytesRead);
                    Log.d(TAG, "Received message from server: " + message);
                }

                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "run: "+e.getMessage());
            }
        }
    }

}

