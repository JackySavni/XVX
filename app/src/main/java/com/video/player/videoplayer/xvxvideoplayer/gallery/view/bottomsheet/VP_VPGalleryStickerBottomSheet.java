package com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryStickerAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryStickerListener;

public class VP_VPGalleryStickerBottomSheet extends BottomSheetDialogFragment implements VP_GalleryStickerListener {
    private RecyclerView vp_recyclerViewSticker;
    private final VP_GalleryStickerListener VP_galleryStickerListener;

    public VP_VPGalleryStickerBottomSheet(VP_GalleryStickerListener VP_galleryStickerListener) {
        this.VP_galleryStickerListener = VP_galleryStickerListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_gallery_bottom_sheet_sticker, container, false);
        init(view);
        setEmojiAdapter();
        showDialogFullScreen();
        return view;
    }

    private void setEmojiAdapter() {
        this.vp_recyclerViewSticker.setAdapter(new VP_GalleryStickerAdapter(this, getContext()));
    }

    private void init(View view) {
        this.vp_recyclerViewSticker = view.findViewById(R.id.vp_recyclerViewSticker);
    }

    @Override
    public void vp_onStickerClicked(int imgRes) {
        //notify sticker clicked and close bottom sheet
        this.VP_galleryStickerListener.vp_onStickerClicked(imgRes);
        dismiss();
    }

    /**
     * change dialog parameters and show full screen
     */
    private void showDialogFullScreen() {
        Dialog vp_dialogS = getDialog();
        if (vp_dialogS != null) {
            vp_dialogS.setOnShowListener(dialog -> {
                BottomSheetDialog vp_bottomSheetDialog = (BottomSheetDialog) dialog;
                FrameLayout vp_bottomSheet = vp_bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                CoordinatorLayout vp_coordinatorLayout = (CoordinatorLayout) vp_bottomSheet.getParent();
                BottomSheetBehavior<FrameLayout> vp_bottomSheetBehavior = BottomSheetBehavior.from(vp_bottomSheet);
                vp_bottomSheetBehavior.setPeekHeight(vp_bottomSheet.getHeight());
                vp_coordinatorLayout.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                vp_coordinatorLayout.getParent().requestLayout();
            });
        }
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