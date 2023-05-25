package com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GallerySquareImageView;

public class VP_StickerViewHolder extends RecyclerView.ViewHolder {
    public VP_GallerySquareImageView vp_imgSticker;

    public VP_StickerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.vp_imgSticker = itemView.findViewById(R.id.vp_imgSticker);
    }

}
