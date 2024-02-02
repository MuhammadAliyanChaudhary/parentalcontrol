package com.example.parentalcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.model.SmsModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {

    private List<SmsModel> smsList;
    private LayoutInflater inflater;

    public SmsAdapter(Context context, List<SmsModel> smsList) {
        this.inflater = LayoutInflater.from(context);
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public SmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_sms, parent, false);
        return new SmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {
        SmsModel sms = smsList.get(position);

        holder.textViewAddress.setText(sms.getAddress());
        holder.textViewBody.setText(sms.getBody());
        
        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.textViewDate.setText(dateFormat.format(Long.parseLong(sms.getDate())));
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    static class SmsViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAddress;
        TextView textViewBody;
        TextView textViewDate;

        public SmsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            textViewBody = itemView.findViewById(R.id.textViewBody);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}
