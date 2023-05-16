package com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;

public class VP_FilterViewHolder extends RecyclerView.ViewHolder {
    public ImageView vp_imgFilter, vp_imgFilterUsed;
    public TextView vp_txtFilterName;

    public VP_FilterViewHolder(@NonNull View itemView) {
        super(itemView);
        this.vp_imgFilter = itemView.findViewById(R.id.vp_imgFilter);
        this.vp_imgFilterUsed = itemView.findViewById(R.id.vp_imgFilterUsed);
        this.vp_txtFilterName = itemView.findViewById(R.id.vp_txtFilterName);
    }

}
