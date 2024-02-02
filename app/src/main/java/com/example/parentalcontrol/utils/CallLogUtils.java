package com.example.parentalcontrol.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import com.example.parentalcontrol.model.CallLogModel;

import java.util.ArrayList;
import java.util.List;

public class CallLogUtils {

    public static List<CallLogModel> getAllCallLogs(Context context) {
        List<CallLogModel> callLogs = new ArrayList<>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                CallLog.Calls.CONTENT_URI,
                null,
                null,
                null,
                CallLog.Calls.DATE + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));

                CallLogModel callLog = new CallLogModel(id,number, name, date, type);
                callLogs.add(callLog);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return callLogs;
    }
}
