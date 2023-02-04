package com.shunyank.shunyankvideoplayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shunyank.shunyankvideoplayer.callbacks.ItemClickListener;
import com.shunyank.shunyankvideoplayer.network.VideoModel;

import java.util.ArrayList;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.ViewHolder> {

    ArrayList<VideoModel> data;

    ItemClickListener listener;
    boolean forCompletedVideos = false;

    public void setData(ArrayList<VideoModel> data) {
        this.data = data;
    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int pos = position;
        holder.fileName.setText(data.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(data.get(pos));
            }


        });
        if (forCompletedVideos) {
            holder.markAsComplete.setText("Remove as completed");
        }else {
            holder.markAsComplete.setText("Mark as complete");

        }
        holder.markAsComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forCompletedVideos) {
                    listener.onRemoveAsComplete(data.get(pos),pos);
                } else {
                    listener.onMarkAsComplete(data.get(pos), pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {

        return data.size();
    }

    public void setAdapterForCompleted(boolean b) {
        forCompletedVideos = b;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView markAsComplete, fileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.file_name);
            markAsComplete = itemView.findViewById(R.id.mark_as_complete);

        }
    }
}
