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
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryMediaListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryMediaTypeAction;

public class VP_GalleryMediaBottomSheet extends BottomSheetDialogFragment {

    private final VP_GalleryMediaListener VP_galleryMediaListener;

    public VP_GalleryMediaBottomSheet(VP_GalleryMediaListener VP_galleryMediaListener) {
        this.VP_galleryMediaListener = VP_galleryMediaListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_gallery_bottom_sheet, container, false);

        //notify what type of action want to do
        view.findViewById(R.id.vp_btnShareVideo).setOnClickListener(v -> {
            VP_galleryMediaListener.VP_onMenuClickListener(VP_GalleryMediaTypeAction.VP_SHARE);
            dismiss();
        });

        view.findViewById(R.id.vp_btnDeleteVideo).setOnClickListener(v -> {
            VP_galleryMediaListener.VP_onMenuClickListener(VP_GalleryMediaTypeAction.VP_SHOW_DIALOG_DELETE);
            dismiss();
        });

        view.findViewById(R.id.vp_btnDetails).setOnClickListener(v -> {
            VP_galleryMediaListener.VP_onMenuClickListener(VP_GalleryMediaTypeAction.VP_DETAILS);
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
