package com.example.parentalcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.adapters.CallLogAdapter;
import com.example.parentalcontrol.databinding.ActivityCallLogBinding;
import com.example.parentalcontrol.utils.CallLogUtils;

public class CallLogActivity extends AppCompatActivity {

    private ActivityCallLogBinding binding;
    private CallLogAdapter callLogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setRecyclerView();

    }

    private void setRecyclerView(){
        binding.callLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        callLogAdapter = new CallLogAdapter(this, CallLogUtils.getAllCallLogs(this));
        binding.callLogRecyclerView.setAdapter(callLogAdapter);
        callLogAdapter.notifyDataSetChanged();
    }

}