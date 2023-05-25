package com.video.player.videoplayer.xvxvideoplayer.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryPhotoController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GallerySquareImageView;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryStickerListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder.VP_StickerViewHolder;

import java.util.ArrayList;

public class VP_GalleryStickerAdapter extends RecyclerView.Adapter<VP_StickerViewHolder> {
    private final ArrayList<Integer> vp_stickers;
    private final Context vp_context;
    private final VP_GalleryStickerListener VP_galleryStickerListener;

    public VP_GalleryStickerAdapter(VP_GalleryStickerListener VP_galleryStickerListener, Context vp_context) {
        this.VP_galleryStickerListener = VP_galleryStickerListener;
        this.vp_context = vp_context;
        this.vp_stickers = VP_GalleryPhotoController.vp_getStickers();
    }

    @NonNull
    @Override
    public VP_StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_gallery_card_sticker, parent, false);
        return new VP_StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VP_StickerViewHolder holder, int position) {
        //get sticker
        int vp_sticker = vp_stickers.get(position);
        //get view from holder
        VP_GallerySquareImageView vp_imgSticker = holder.vp_imgSticker;
        if (vp_imgSticker != null) {
            //show sticker
            Glide.with(vp_context).load(vp_sticker).into(vp_imgSticker);

            holder.itemView.setOnClickListener(v -> {
                //notify sticker clicked
                if (VP_galleryStickerListener != null) {
                    VP_galleryStickerListener.vp_onStickerClicked(vp_sticker);
                }
            });
        }
    }

    /**
     * @return number of stickers
     */
    @Override
    public int getItemCount() {
        return vp_stickers.size();
    }

}
