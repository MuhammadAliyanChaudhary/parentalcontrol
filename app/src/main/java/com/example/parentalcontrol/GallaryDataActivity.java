package com.example.parentalcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;

import android.os.Bundle;
import android.util.Log;

import com.example.parentalcontrol.adapters.DocumentsAdapter;
import com.example.parentalcontrol.adapters.MediaAdapter;
import com.example.parentalcontrol.adapters.VideoAdapter;
import com.example.parentalcontrol.utils.MediaStoreHelper;
import com.example.parentalcontrol.utils.PermissionManager;

public class GallaryDataActivity extends AppCompatActivity {

    String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
            // Add more permissions here
    };

    String[] permissionsAndroid10 = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
            // Add more permissions here
    };
    private PermissionManager permissionManager;
    private int REQUEST_CODE = 101;
    private RecyclerView mediaRecyclerView;
    private MediaAdapter mediaAdapter;
    private VideoAdapter videoAdapter;
    private DocumentsAdapter documentsAdapter;


    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallary_data);

        mediaRecyclerView = findViewById(R.id.mediaRecyclerView);
        mediaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        permissionManager = new PermissionManager(this, REQUEST_CODE);


        bundle = getIntent().getExtras();
        if (bundle != null) {
            String mediaType = bundle.getString("media");
            Log.d("bundle", "onCreate: " + mediaType);
            setRecyclerView(mediaType);
        } else {
            Log.d("bundle", "onCreate: Bundle is null");
        }








    }


    private void setRecyclerView(String mediaType){

        switch (mediaType) {
            case "Images":
                mediaAdapter = new MediaAdapter(this, MediaStoreHelper.getAllImages(this));
                mediaRecyclerView.setAdapter(mediaAdapter);
                mediaAdapter.notifyDataSetChanged();
                break;
            case "Videos":
                videoAdapter = new VideoAdapter(this, MediaStoreHelper.getAllVideos(this));
                mediaRecyclerView.setAdapter(videoAdapter);
                videoAdapter.notifyDataSetChanged();
                break;
            case "Documents":
                documentsAdapter = new DocumentsAdapter(this, MediaStoreHelper.getAllDocuments(this));
                mediaRecyclerView.setAdapter(documentsAdapter);
                documentsAdapter.notifyDataSetChanged();
                break;
        }
    }








}