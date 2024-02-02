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
import com.example.parentalcontrol.model.VideoModel;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    private Context context;
    private List<VideoModel> videoModels;

    public VideoAdapter(Context context, List<VideoModel> videoModels) {
        this.context = context;
        this.videoModels = videoModels;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, int position) {
        VideoModel videoModel = videoModels.get(position);
        holder.mediaTitle.setText(videoModel.getTitle());
        Glide.with(context).load(videoModel.getUri()).placeholder(R.drawable.ic_launcher_foreground).into(holder.mediaImage);

    }

    @Override
    public int getItemCount() {
        return videoModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mediaImage;
        TextView mediaTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaImage = itemView.findViewById(R.id.mediaImage);
            mediaTitle = itemView.findViewById(R.id.mediaTitle);
        }
    }
}
