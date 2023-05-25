package com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;


public class VP_PhotoViewHolder extends RecyclerView.ViewHolder {
    public TextView vp_txtNameSize;
    public ImageView vp_img, vp_imgSelected;

    public VP_PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.vp_img = itemView.findViewById(R.id.vp_imgPhoto);
        this.vp_imgSelected = itemView.findViewById(R.id.vp_imgSelected);
        this.vp_txtNameSize = itemView.findViewById(R.id.vp_txtNameSize);
    }

}
