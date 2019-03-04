package com.example.mat3amk.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mat3amk.R;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public TextView userTextView , commentTextView;
    public RatingBar ratingBar;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        userTextView = itemView.findViewById(R.id.username);
        commentTextView = itemView.findViewById(R.id.comment_tv);
        ratingBar = itemView.findViewById(R.id.ratingBar);
    }
}
