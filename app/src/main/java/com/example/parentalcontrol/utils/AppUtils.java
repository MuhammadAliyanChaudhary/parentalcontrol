package com.example.parentalcontrol.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class AppUtils {

    private static final String PREF_NAME = "MyPrefs";

    public static void saveRecordingState(Context context, boolean isRecordingStarted) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isRecordingStarted", isRecordingStarted);
        editor.apply();
    }

    public static boolean getRecordingState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("isRecordingStarted", false);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
