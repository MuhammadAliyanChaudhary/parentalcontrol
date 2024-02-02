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
import com.example.parentalcontrol.model.DocumentModel;
import com.example.parentalcontrol.model.ImageModel;

import java.util.List;

public class DocumentsAdapter extends RecyclerView.Adapter<DocumentsAdapter.ViewHolder> {

    private Context context;
    private List<DocumentModel> documentModelList;

    public DocumentsAdapter(Context context, List<DocumentModel> documentModelList) {
        this.context = context;
        this.documentModelList = documentModelList;
    }

    @NonNull
    @Override
    public DocumentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentsAdapter.ViewHolder holder, int position) {
        DocumentModel documentModel = documentModelList.get(position);
        holder.mediaTitle.setText(documentModel.getTitle());
        Glide.with(context).load(documentModel.getPath()).placeholder(R.drawable.ic_file).into(holder.mediaImage);

    }

    @Override
    public int getItemCount() {
        return documentModelList.size();
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
