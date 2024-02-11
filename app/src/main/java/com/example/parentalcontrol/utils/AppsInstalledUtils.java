package com.example.parentalcontrol.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.example.parentalcontrol.model.AppModel;

import java.util.ArrayList;
import java.util.List;

public class AppsInstalledUtils {

    public interface OnAppsFetchedListener {
        void onAppsFetched(List<AppModel> appList);
    }

    public static void getAllInstalledAppsAsync(Context context, OnAppsFetchedListener listener) {
        new FetchInstalledAppsTask(context, listener).execute();
    }

    private static class FetchInstalledAppsTask extends AsyncTask<Void, Void, List<AppModel>> {

        private final Context context;
        private final OnAppsFetchedListener listener;

        public FetchInstalledAppsTask(Context context, OnAppsFetchedListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected List<AppModel> doInBackground(Void... voids) {
            List<AppModel> appList = new ArrayList<>();

            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ApplicationInfo> applications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo applicationInfo : applications) {
                // Exclude system apps if needed
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    String appName = (String) packageManager.getApplicationLabel(applicationInfo);
                    String packageName = applicationInfo.packageName;
                    Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);

                    Log.d("appinstalledpackages", ""+packageName);
                    AppModel appModel = new AppModel(appName, appIcon, packageName);
                    appList.add(appModel);
                }
            }

            return appList;
        }

        @Override
        protected void onPostExecute(List<AppModel> appList) {
            super.onPostExecute(appList);
            if (listener != null) {
                listener.onAppsFetched(appList);
            }
        }
    }
}
