package com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GallerySetWallpaperListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryTypeScreen;

;

public class VP_GallerySetWallpaperBottomSheet extends BottomSheetDialogFragment {
    private final VP_GallerySetWallpaperListener VP_gallerySetWallpaperListener;

    public VP_GallerySetWallpaperBottomSheet(VP_GallerySetWallpaperListener VP_gallerySetWallpaperListener) {
        this.VP_gallerySetWallpaperListener = VP_gallerySetWallpaperListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_gallery_bottom_sheet_set_wallpaper, container, false);

        //notify what type of action want to do
        view.findViewById(R.id.vp_btnHome).setOnClickListener(v -> {
            VP_gallerySetWallpaperListener.VP_onButtonSetWallpaperClicked(VP_GalleryTypeScreen.VP_HOME);
            dismiss();
        });

        view.findViewById(R.id.vp_btnLock).setOnClickListener(v -> {
            VP_gallerySetWallpaperListener.VP_onButtonSetWallpaperClicked(VP_GalleryTypeScreen.VP_LOCK);
            dismiss();
        });

        view.findViewById(R.id.vp_btnHomeLock).setOnClickListener(v -> {
            VP_gallerySetWallpaperListener.VP_onButtonSetWallpaperClicked(VP_GalleryTypeScreen.VP_HOME_LOCK);
            dismiss();
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set style
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            //set background transparent
            ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);
        } catch (NullPointerException e) {
            e.getMessage();
            e.getStackTrace();
        }
    }

}
