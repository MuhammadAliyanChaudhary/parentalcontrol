package com.example.parentalcontrol.adapters;

import android.content.Context;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.model.ContactModel;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    Context context;
    List<ContactModel> contactModels;

    public ContactAdapter(Context context, List<ContactModel> contactModels) {
        this.context = context;
        this.contactModels = contactModels;
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        ContactModel contactModel = contactModels.get(position);

        holder.name.setText(contactModel.getDisplayName());
      //  holder.number.setText(contactModel.getPhoneNumbers().get(position));

        if (!contactModel.getPhoneNumbers().isEmpty()) {
            holder.number.setText(contactModel.getPhoneNumbers().get(0));
        } else {
            // Handle the case where there are no phone numbers
            holder.number.setText("No phone number");
        }
    }

    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contactName);
            number = itemView.findViewById(R.id.contact_number);

        }
    }
}
