package com.example.parentalcontrol.model;

import android.graphics.drawable.Drawable;

public class AppModel {

    private String appName;
    private String packageName;
    private Drawable appIcon;

    public AppModel(String appName, Drawable appIcon, String packageName) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }
}
