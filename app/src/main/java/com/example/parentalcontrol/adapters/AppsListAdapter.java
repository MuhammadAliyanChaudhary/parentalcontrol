package com.example.parentalcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parentalcontrol.R;
import com.example.parentalcontrol.model.AppModel;

import java.util.List;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.AppViewHolder> {

    private List<AppModel> appList;
    private LayoutInflater inflater;

    public AppsListAdapter(Context context, List<AppModel> appList) {
        this.inflater = LayoutInflater.from(context);
        this.appList = appList;
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
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewAppName;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewAppName = itemView.findViewById(R.id.textViewAppName);
        }
    }
}
