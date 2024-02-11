package com.example.parentalcontrol.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.model.AppModel;
import com.example.parentalcontrol.utils.AppsInstalledUtils;

import java.util.List;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.AppViewHolder> {
    private static final String PREF_FILE_NAME = "AppLockerPrefs";
    private static final String LOCKED_APPS_PREF_KEY = "LockedApps";

    private SharedPreferences prefs;


    private List<AppModel> appList;
    private LayoutInflater inflater;

    public AppsListAdapter(Context context, List<AppModel> appList) {
        this.inflater = LayoutInflater.from(context);
        this.appList = appList;
        this.prefs = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_app_list, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        AppModel app = appList.get(position);

        holder.imageViewIcon.setImageDrawable(app.getAppIcon());
        holder.textViewAppName.setText(app.getAppName());


        // Check if the app is locked or unlocked
        if (isAppLocked(app.getPackageName())) {
            holder.lockIcon.setImageResource(R.drawable.ic_lock);
        } else {
            holder.lockIcon.setImageResource(R.drawable.ic_lock_open); // Set unlock icon
        }

        holder.lockIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleAppLockStatus(app.getPackageName());
                Toast.makeText(inflater.getContext(), "Package :"+app.getPackageName(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged(); // Notify RecyclerView to update UI
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon, lockIcon;
        TextView textViewAppName;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            lockIcon = itemView.findViewById(R.id.lockIcon);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewAppName = itemView.findViewById(R.id.textViewAppName);
        }
    }

    private boolean isAppLocked(String packageName) {
        String lockedApps = prefs.getString(LOCKED_APPS_PREF_KEY, "");
        if (packageName != null) {
            return lockedApps.contains(packageName);
        }
        return false; // Return false if packageName is null
    }


    private void toggleAppLockStatus(String packageName) {
        if (packageName != null) {
            String lockedApps = prefs.getString(LOCKED_APPS_PREF_KEY, "");
            if (lockedApps.contains(packageName)) {
                // App is locked, unlock it
                lockedApps = lockedApps.replace(packageName + ",", "");
            } else {
                // App is unlocked, lock it
                lockedApps += packageName + ",";
            }
            prefs.edit().putString(LOCKED_APPS_PREF_KEY, lockedApps).apply();
        }
    }



}
