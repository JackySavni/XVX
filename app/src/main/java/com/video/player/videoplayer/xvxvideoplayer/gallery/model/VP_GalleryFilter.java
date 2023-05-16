package com.video.player.videoplayer.xvxvideoplayer.gallery.model;

import android.graphics.Bitmap;

import com.bumptech.glide.load.Transformation;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryFilterType;

public class VP_GalleryFilter {

    private final Transformation<Bitmap> vp_transformation;
    private final String vp_title;
    private final VP_GalleryFilterType VPGalleryFilterType;

    public VP_GalleryFilter(Transformation<Bitmap> vp_transformation, String vp_title, VP_GalleryFilterType VPGalleryFilterType) {
        this.vp_transformation = vp_transformation;
        this.vp_title = vp_title;
        this.VPGalleryFilterType = VPGalleryFilterType;
    }

    public Transformation<Bitmap> getVp_transformation() {
        return vp_transformation;
    }

    public String getVp_title() {
        return vp_title;
    }

    public VP_GalleryFilterType getFilterType() {
        return VPGalleryFilterType;
    }

}
