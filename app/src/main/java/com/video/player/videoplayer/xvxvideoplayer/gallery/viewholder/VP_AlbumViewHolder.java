package com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;


public class VP_AlbumViewHolder extends RecyclerView.ViewHolder {

    public ImageView vp_img;
    public TextView vp_txtName, vp_txtNrPhotos;

    public VP_AlbumViewHolder(@NonNull View itemView) {
        super(itemView);
        this.vp_img = itemView.findViewById(R.id.vp_imgAlbum);
        this.vp_txtName = itemView.findViewById(R.id.vp_txtAlbumName);
        this.vp_txtNrPhotos = itemView.findViewById(R.id.vp_txtNrPhotos);
    }

}
