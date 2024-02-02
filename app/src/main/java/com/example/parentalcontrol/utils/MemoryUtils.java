package com.example.parentalcontrol.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

public class MemoryUtils {

    public static String formatBytes(long bytes) {
        DecimalFormat df = new DecimalFormat("0.00");

        double kilobytes = bytes / 1024.0;
        double megabytes = kilobytes / 1024.0;
        double gigabytes = megabytes / 1024.0;

        if (gigabytes >= 1) {
            return df.format(gigabytes) + " GB";
        } else if (megabytes >= 1) {
            return df.format(megabytes) + " MB";
        } else {
            return df.format(kilobytes) + " KB";
        }
    }

    public static String getTotalRAM(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long totalRAM = memoryInfo.totalMem;

        return formatBytes(totalRAM);
    }

    public static String getAvailableRAM(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);

        long availableRAM = memoryInfo.availMem;

        return formatBytes(availableRAM);
    }

    public static String getTotalStorage() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long totalStorage = (long) statFs.getBlockSizeLong() * statFs.getBlockCountLong();

        return formatBytes(totalStorage);
    }

    public static String getAvailableStorage() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long availableStorage = (long) statFs.getBlockSizeLong() * statFs.getAvailableBlocksLong();

        return formatBytes(availableStorage);
    }
}
