package com.example.splitit.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splitit.R;
import com.example.splitit.model.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<Member> memberList;

    public MemberAdapter(List<Member> memberList) {
        this.memberList = memberList;
    }

    @NonNull
    @Override
    public MemberAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.tvMemberName.setText(member.getName());
        if(member.getImageUri() != null && !member.getImageUri().isEmpty()) {
            holder.ivMemberImage.setImageURI(Uri.parse(member.getImageUri()));
        } else {
            holder.ivMemberImage.setImageResource(android.R.drawable.ic_menu_camera); // default
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivMemberImage;
        TextView tvMemberName;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMemberName = itemView.findViewById(R.id.tvMemberName);
            ivMemberImage = itemView.findViewById(R.id.ivMemberImage);
        }
    }
}

