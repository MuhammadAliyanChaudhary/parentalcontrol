package com.example.parentalcontrol.adapters;

import android.content.Context;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.model.CallLogModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CallLogAdapter extends RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder> {

    private List<CallLogModel> callLogList;
    private LayoutInflater inflater;

    public CallLogAdapter(Context context, List<CallLogModel> callLogList) {
        this.inflater = LayoutInflater.from(context);
        this.callLogList = callLogList;
    }

    @NonNull
    @Override
    public CallLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_call_log, parent, false);
        return new CallLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallLogViewHolder holder, int position) {
        CallLogModel callLog = callLogList.get(position);

        holder.textViewNumber.setText(callLog.getNumber());
        holder.textViewName.setText(callLog.getName());

        // Format the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        holder.textViewDate.setText(dateFormat.format(Long.parseLong(callLog.getDate())));

        holder.textViewType.setText(getCallType(callLog.getType()));
    }

    @Override
    public int getItemCount() {
        return callLogList.size();
    }

    static class CallLogViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber;
        TextView textViewName;
        TextView textViewDate;
        TextView textViewType;

        public CallLogViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewType = itemView.findViewById(R.id.textViewType);
        }
    }

    private String getCallType(int callType) {
        switch (callType) {
            case CallLog.Calls.INCOMING_TYPE:
                return "Incoming";
            case CallLog.Calls.OUTGOING_TYPE:
                return "Outgoing";
            case CallLog.Calls.MISSED_TYPE:
                return "Missed";
            case CallLog.Calls.REJECTED_TYPE:
                return "Rejected";
            case CallLog.Calls.BLOCKED_TYPE:
                return "Blocked";
            default:
                return "Unknown";
        }
    }
}
