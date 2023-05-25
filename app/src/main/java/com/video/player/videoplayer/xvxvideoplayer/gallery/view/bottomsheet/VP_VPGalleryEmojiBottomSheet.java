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
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryEmojiAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryEmojiListener;


public class VP_VPGalleryEmojiBottomSheet extends BottomSheetDialogFragment implements VP_GalleryEmojiListener {
    private RecyclerView vp_recyclerViewEmojis;
    private final VP_GalleryEmojiListener VP_galleryEmojiListener;

    public VP_VPGalleryEmojiBottomSheet(VP_GalleryEmojiListener VP_galleryEmojiListener) {
        this.VP_galleryEmojiListener = VP_galleryEmojiListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vp_gallery_bottom_sheet_emoji, container, false);
        vp_init(view);
        vp_setEmojiAdapter();
        vp_showDialogFullScreen();
        return view;
    }

    private void vp_setEmojiAdapter() {
        this.vp_recyclerViewEmojis.setAdapter(new VP_GalleryEmojiAdapter(this, getContext()));
    }

    private void vp_init(View view) {
        this.vp_recyclerViewEmojis = view.findViewById(R.id.vp_recyclerViewEmoji);
    }

    @Override
    public void onEmojiClicked(String emoji) {
        //notify emoji clicked and close bottom sheet
        this.VP_galleryEmojiListener.onEmojiClicked(emoji);
        dismiss();
    }

    /**
     * change dialog parameters and show full screen
     */
    private void vp_showDialogFullScreen() {
        Dialog vp_dialogE = getDialog();
        if (vp_dialogE != null) {
            vp_dialogE.setOnShowListener(dialog -> {
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