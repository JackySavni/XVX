package com.video.player.videoplayer.xvxvideoplayer.gallery.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryPhotoController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorUtil;
import com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder.VP_ColorViewHolder;

import java.util.ArrayList;


public class VP_GalleryColorsAdapter extends RecyclerView.Adapter<VP_ColorViewHolder> {
    private final VP_GalleryColorListener VP_galleryColorListener;
    private final ArrayList<Integer> vp_colors;
    private int vp_positionFilter = 6;
    private VP_ColorViewHolder vp_lastHolderClicked;

    public VP_GalleryColorsAdapter(VP_GalleryColorListener VP_galleryColorListener) {
        this.vp_colors = VP_GalleryPhotoController.vp_getColors();
        this.VP_galleryColorListener = VP_galleryColorListener;
    }

    @NonNull
    @Override
    public VP_ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_gallery_card_color, parent, false);
        return new VP_ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VP_ColorViewHolder holder, int position) {
        //get color from list
        int color = vp_colors.get(holder.getAdapterPosition());
        //get all views from holder
        ImageView vp_img = holder.vp_img, imgFilterUsed = holder.vp_imgFilterUsed;

        if (vp_img != null && imgFilterUsed != null) {
            //set rainbow gradient for 1º item and color for the rest
            if (position == 0) {
                vp_img.setBackground(VP_GalleryColorUtil.vp_getRainbowGradient());
            } else {
                vp_img.setBackgroundColor(color);
            }

            //show/hide check image
            if (vp_positionFilter == holder.getAdapterPosition()) {
                imgFilterUsed.setVisibility(View.VISIBLE);
                vp_lastHolderClicked = holder;
            } else {
                imgFilterUsed.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> {
                //hide check image from last item clicked
                if (vp_lastHolderClicked != null) {
                    vp_lastHolderClicked.vp_imgFilterUsed.setVisibility(View.GONE);
                }
                //show check from item clicked
                imgFilterUsed.setVisibility(View.VISIBLE);
                //set position from item clicked
                vp_positionFilter = holder.getAdapterPosition();
                //set holder
                vp_lastHolderClicked = holder;
                //notify color and position
                VP_galleryColorListener.onColorClicked(color, position);
            });

        }

    }

    /**
     * @return number of colors
     */
    @Override
    public int getItemCount() {
        return vp_colors.size();
    }

}
