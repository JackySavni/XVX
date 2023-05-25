package com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;

public class VP_ColorViewHolder extends RecyclerView.ViewHolder {

    public ImageView vp_img, vp_imgFilterUsed;

    public VP_ColorViewHolder(@NonNull View itemView) {
        super(itemView);
        this.vp_img = itemView.findViewById(R.id.vp_img);
        this.vp_imgFilterUsed = itemView.findViewById(R.id.vp_imgFilterUsed);
    }

}
