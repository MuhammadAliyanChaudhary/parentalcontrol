package com.example.parentalcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.parentalcontrol.R;
import com.example.parentalcontrol.model.ImageModel;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {
    private Context context;
    private List<ImageModel> imageModels;

    public MediaAdapter(Context context, List<ImageModel> imageModels) {
        this.context = context;
        this.imageModels = imageModels;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageModel imageModel = imageModels.get(position);
        holder.mediaTitle.setText(imageModel.getTitle());
        Glide.with(context).load(imageModel.getUri()).placeholder(R.drawable.ic_launcher_foreground).into(holder.mediaImage);

    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mediaImage;
        TextView mediaTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            mediaImage = itemView.findViewById(R.id.mediaImage);
            mediaTitle = itemView.findViewById(R.id.mediaTitle);
        }
    }
}
