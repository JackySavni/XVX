package com.video.player.videoplayer.xvxvideoplayer.gallery.adapter;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.showInterAd2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showInterAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryAlbum;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.viewer.VP_GalleryAlbumActivity;
import com.video.player.videoplayer.xvxvideoplayer.gallery.viewholder.VP_AlbumViewHolder;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import java.util.ArrayList;

public class VP_GalleryAlbumsAdapter extends RecyclerView.Adapter<VP_AlbumViewHolder> {
    private final ArrayList<VP_GalleryAlbum> VPGalleryAlbums;
    private final Activity vp_activity;
    private boolean vp_showLinearManager = false;
    public static final int VP_TYPE_GRID = 0;
    public static final int VP_TYPE_LINEAR = 1;

    public VP_GalleryAlbumsAdapter(Activity vp_activity) {
        this.vp_activity = vp_activity;
        this.VPGalleryAlbums = new ArrayList<>();
    }

    @NonNull
    @Override
    public VP_AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //return different design depending of the viewType
        switch (viewType) {
            case VP_TYPE_GRID:
                View vp_grid = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vp_gallery_card_album_type_grid, parent, false);
                return new VP_AlbumViewHolder(vp_grid);
            case VP_TYPE_LINEAR:
                View vp_linear = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vp_gallery_card_album_type_linear, parent, false);
                return new VP_AlbumViewHolder(vp_linear);
            default:
                throw new IllegalArgumentException("Invalid view type | GalleryAlbumsAdapter");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VP_AlbumViewHolder holder, int position) {
        //get galleryAlbum
        VP_GalleryAlbum VPGalleryAlbum = VPGalleryAlbums.get(position);

        //get all views from holder
        ImageView vp_img = holder.vp_img;
        TextView vp_txtName = holder.vp_txtName, txtNrPhotos = holder.vp_txtNrPhotos;

        if (VPGalleryAlbum != null && vp_img != null && vp_txtName != null && txtNrPhotos != null) {
            vp_txtName.setText(VPGalleryAlbum.getVp_name());

            //set number of galleryPhotos or number of galleryPhotos and galleryAlbum size
            if (getItemViewType(position) == VP_TYPE_GRID) {
                txtNrPhotos.setText("("+String.valueOf(VPGalleryAlbum.getVp_nrPhotos())+")");
            } else {
                int vp_nrPhotos = VPGalleryAlbum.getVp_nrPhotos();
                String photo = vp_nrPhotos == 1 ? " photo" : " galleryPhotos";
                txtNrPhotos.setText(vp_nrPhotos + photo + " ∙ " + Formatter.formatFileSize(vp_activity, VPGalleryAlbum.getVp_size()));
            }

            //show 1º image of galleryAlbum
            Glide.with(vp_activity).load(VPGalleryAlbum.getPhotos().get(0).getVp_path())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(vp_img);

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(vp_activity, VP_GalleryAlbumActivity.class);
                VP_MyApplication vp_videoPlayerManager = (VP_MyApplication) vp_activity.getApplicationContext();
                vp_videoPlayerManager.VPGalleryAlbum = VPGalleryAlbum;
                if (vp_GInter1(vp_activity).equals(vp_GInter2(vp_activity))) {
                    showInterAd2(vp_activity, intent);
                } else {
                    vp_showInterAd(vp_activity, intent);
                }
            });
        }
    }

    /**
     * @param position
     * @return viewType depending of showLinearManager
     */
    @Override
    public int getItemViewType(int position) {
        return vp_showLinearManager ? VP_TYPE_LINEAR : VP_TYPE_GRID;
    }

    /**
     * @return number of galleryAlbums
     */
    @Override
    public int getItemCount() {
        return VPGalleryAlbums.size();
    }

    /**
     * set galleryAlbum
     *
     * @param VPGalleryAlbum
     */
    public void setAlbum(VP_GalleryAlbum VPGalleryAlbum) {
        this.VPGalleryAlbums.add(VPGalleryAlbum);
        vp_activity.runOnUiThread(this::notifyDataSetChanged);
    }

    public void clearAlbum() {
        this.VPGalleryAlbums.clear();
    }

    /**
     * to show linear or grid
     *
     * @param showLinearManager
     */
    public void showLinearManager(boolean showLinearManager) {
        this.vp_showLinearManager = showLinearManager;
    }

}
