package com.video.player.videoplayer.xvxvideoplayer.gallery.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryPhotoController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryFilter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryFilterListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder.VP_FilterViewHolder;

import java.util.ArrayList;


public class VP_GalleryFiltersAdapter extends RecyclerView.Adapter<VP_FilterViewHolder> {
    private final ArrayList<VP_GalleryFilter> VPGalleryFilters;
    private final Activity vp_activity;
    private final VP_GalleryFilterListener VP_galleryFilterListener;
    private final String vp_imgPath;
    private int vp_positionFilter = 0;
    private VP_FilterViewHolder vp_lastHolderClicked;
    private boolean vp_brushModeEnabled = false;

    public VP_GalleryFiltersAdapter(Activity vp_activity, String vp_imgPath, VP_GalleryFilterListener VP_galleryFilterListener) {
        this.vp_activity = vp_activity;
        this.VPGalleryFilters = VP_GalleryPhotoController.ex_getFilters();
        this.vp_imgPath = vp_imgPath;
        this.VP_galleryFilterListener = VP_galleryFilterListener;
    }

    @NonNull
    @Override
    public VP_FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vp_gallery_card_filter, parent, false);
        return new VP_FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VP_FilterViewHolder holder, int position) {
        //get galleryFilter
        VP_GalleryFilter VPGalleryFilter = VPGalleryFilters.get(holder.getAdapterPosition());
        //get all views from holder
        TextView vp_txtFilterName = holder.vp_txtFilterName;
        ImageView vp_imgFilter = holder.vp_imgFilter, vp_imgFilterUsed = holder.vp_imgFilterUsed;

        if (VPGalleryFilter != null && vp_txtFilterName != null && vp_imgFilter != null && vp_imgFilterUsed != null) {
            //set title
            vp_txtFilterName.setText(VPGalleryFilter.getVp_title());
            //apply default image or galleryFilter, depending if transformation == null
            //override image new width-height
            //add position as signature for cache purpose
            if (!vp_activity.isFinishing()) {
                if (VPGalleryFilter.getVp_transformation() == null) {
                    Glide.with(vp_activity).load(vp_imgPath)
                            .signature(new ObjectKey(position))
                            .override(100, 150)
                            .into(vp_imgFilter);
                } else {
                    Glide.with(vp_activity).load(vp_imgPath)
                            .apply(RequestOptions.bitmapTransform(VPGalleryFilter.getVp_transformation()))
                            .signature(new ObjectKey(position))
                            .override(100, 150)
                            .into(vp_imgFilter);
                }
            }
            //show/hide check image
            if (vp_positionFilter == holder.getAdapterPosition()) {
                vp_imgFilterUsed.setVisibility(View.VISIBLE);
                vp_lastHolderClicked = holder;
            } else {
                vp_imgFilterUsed.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> {
                if (vp_brushModeEnabled) {
                    Toast.makeText(vp_activity, "Cannot apply new galleryFilter after painting", Toast.LENGTH_SHORT).show();
                } else {
                    //hide check image from last item clicked
                    if (vp_lastHolderClicked != null) {
                        vp_lastHolderClicked.vp_imgFilterUsed.setVisibility(View.GONE);
                    }
                    //show check from item clicked
                    vp_imgFilterUsed.setVisibility(View.VISIBLE);
                    //set position from item clicked
                    vp_positionFilter = holder.getAdapterPosition();
                    //set holder
                    vp_lastHolderClicked = holder;
                    //notify galleryFilter
                    VP_galleryFilterListener.vp_onFilterClicked(VPGalleryFilter);
                }
            });
        }

    }

    /**
     * @return number of galleryFilters
     */
    @Override
    public int getItemCount() {
        return VPGalleryFilters.size();
    }

    /**
     * disable apply new galleryFilters after painting
     *
     * @param vp_brushModeEnabled
     */
    public void setVp_brushModeEnabled(boolean vp_brushModeEnabled) {
        this.vp_brushModeEnabled = vp_brushModeEnabled;
    }

}
