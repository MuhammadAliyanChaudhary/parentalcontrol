package com.example.parentalcontrol.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.example.parentalcontrol.model.SmsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsUtils {

    public static List<SmsModel> getAllSms(Context context) {
        List<SmsModel> smsList = new ArrayList<>();
        Map<String, List<SmsModel>> smsMap = new HashMap<>();

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(
                Uri.parse("content://sms"),
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(Telephony.Sms._ID));
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                @SuppressLint("Range") String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));

                boolean isSent = type == Telephony.Sms.MESSAGE_TYPE_SENT;

                SmsModel sms = new SmsModel(id,address, body, date, isSent);
                if (!smsMap.containsKey(address)) {
                    smsMap.put(address, new ArrayList<>());
                }
                smsMap.get(address).add(sms);
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Flatten the map into a single list
        for (List<SmsModel> messages : smsMap.values()) {
            smsList.addAll(messages);
        }

        return smsList;
    }
}
