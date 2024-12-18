package com.example.splitit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Group;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    private List<Group> groupList;
    private OnGroupClickListener onGroupClickListener;

    public interface OnGroupClickListener {
        void onGroupClick(Group group);
        void onDeleteGroupClick(Group group);
    }

    public GroupAdapter(List<Group> groupList, OnGroupClickListener listener) {
        this.groupList = groupList;
        this.onGroupClickListener = listener;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        Group group = groupList.get(position);
        holder.tvGroupName.setText(group.getName());
        holder.itemView.setOnClickListener(v -> {
            if (onGroupClickListener != null) {
                onGroupClickListener.onGroupClick(group);
            }
        });

        holder.ivDeleteGroup.setOnClickListener(v -> {
            if(onGroupClickListener != null) {
                onGroupClickListener.onDeleteGroupClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivDeleteGroup;
        TextView tvGroupName;
        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            ivDeleteGroup = itemView.findViewById(R.id.ivDeleteGroup);
        }
    }
}

