package com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;

public class VP_EmojiViewHolder extends RecyclerView.ViewHolder {
    public TextView vp_txtEmoji;

    public VP_EmojiViewHolder(View itemView) {
        super(itemView);
        this.vp_txtEmoji = itemView.findViewById(R.id.vp_txtEmoji);
    }

}
