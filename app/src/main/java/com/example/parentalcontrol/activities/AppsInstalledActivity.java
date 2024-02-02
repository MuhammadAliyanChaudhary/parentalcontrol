package com.example.parentalcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.adapters.AppsListAdapter;
import com.example.parentalcontrol.databinding.ActivityAppsInstalledBinding;
import com.example.parentalcontrol.model.AppModel;
import com.example.parentalcontrol.utils.AppsInstalledUtils;
import com.example.parentalcontrol.utils.LoadingDialogUtils;

import java.util.List;

public class AppsInstalledActivity extends AppCompatActivity {

    private ActivityAppsInstalledBinding binding;
    private AppsListAdapter appsListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppsInstalledBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

      getInstalledAppsList();

    }

    private void getInstalledAppsList(){
        LoadingDialogUtils.showLoadingDialog(this);
        AppsInstalledUtils.getAllInstalledAppsAsync(this, new AppsInstalledUtils.OnAppsFetchedListener() {
            @Override
            public void onAppsFetched(List<AppModel> appList) {
                setRecyclerView(appList);

            }
        });
    }

   private void setRecyclerView(List<AppModel> appList){
        binding.appListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appsListAdapter = new AppsListAdapter(this, appList);
        binding.appListRecyclerView.setAdapter(appsListAdapter);
        appsListAdapter.notifyDataSetChanged();
       LoadingDialogUtils.dismissLoadingDialog();


   }






}