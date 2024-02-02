package com.example.parentalcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.adapters.SmsAdapter;
import com.example.parentalcontrol.databinding.ActivitySmsBinding;
import com.example.parentalcontrol.utils.SmsUtils;

public class SmsActivity extends AppCompatActivity {

    private ActivitySmsBinding binding;
    private SmsAdapter smsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySmsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setRecyclerView();


    }

    private void setRecyclerView(){
        binding.smsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        smsAdapter = new SmsAdapter(this, SmsUtils.getAllSms(this));
        binding.smsRecyclerView.setAdapter(smsAdapter);
        smsAdapter.notifyDataSetChanged();
    }
}