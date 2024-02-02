package com.example.parentalcontrol.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

        @Override
        protected void onCreate(@Nullable Bundle saveInstanceState){
            super.onCreate(saveInstanceState);

        }

    public void openActivity(Class<?> calledActivity, @Nullable Bundle bundle) {
        Intent myIntent = new Intent(this, calledActivity);
        if (bundle != null) {
            myIntent.putExtras(bundle);
            if (bundle.getBoolean("clear")) {
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }
        this.startActivity(myIntent);
    }

}
