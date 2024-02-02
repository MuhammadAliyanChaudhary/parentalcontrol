package com.example.parentalcontrol.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.adapters.ContactAdapter;
import com.example.parentalcontrol.databinding.ActivityContactsBinding;
import com.example.parentalcontrol.model.ContactModel;
import com.example.parentalcontrol.utils.ContactUtils;
import com.example.parentalcontrol.utils.LoadingDialogUtils;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private ActivityContactsBinding binding;
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getContactsList();






    }

    private void getContactsList(){

        LoadingDialogUtils.showLoadingDialog(this);
        ContactUtils.getAllContactsListAsync(this, new ContactUtils.OnContactFetchedListener() {
            @Override
            public void onContactsFetched(List<ContactModel> contactsList) {
                setRecyclerView(contactsList);
                LoadingDialogUtils.dismissLoadingDialog();
            }
        });
    }

    private void setRecyclerView(List<ContactModel> contactsList){
        binding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactAdapter(this,contactsList);
        binding.contactRecyclerView.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();


    }



}