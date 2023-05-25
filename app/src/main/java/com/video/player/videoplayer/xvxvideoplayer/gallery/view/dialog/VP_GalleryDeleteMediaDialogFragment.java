package com.video.player.videoplayer.xvxvideoplayer.gallery.view.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryMediaListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryMediaTypeAction;

public class VP_GalleryDeleteMediaDialogFragment extends DialogFragment {
    private final VP_GalleryMediaListener VP_galleryMediaListener;
    private final String vp_mediaName;

    public VP_GalleryDeleteMediaDialogFragment(String vp_mediaName, VP_GalleryMediaListener VP_galleryMediaListener) {
        this.vp_mediaName = vp_mediaName;
        this.VP_galleryMediaListener = VP_galleryMediaListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder vp_builder = new AlertDialog.Builder(getActivity());
        LayoutInflater vp_inflater = getActivity().getLayoutInflater();
        View view = vp_inflater.inflate(R.layout.vp_gallery_dialog_delete_media, null);
        vp_builder.setView(view);
        final AlertDialog vp_dialog = vp_builder.create();
        try {
            //set background transparent
            vp_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (NullPointerException e) {
            e.getMessage();
            e.getStackTrace();
        }
        //get textview from
        TextView vp_txtNameVideo = view.findViewById(R.id.vp_txtNameVideo);

        //hide textview or set name
        if (vp_mediaName == null) {
            vp_txtNameVideo.setVisibility(View.GONE);
        } else {
            vp_txtNameVideo.setText(vp_mediaName);
        }

        Button vp_btnDelete = view.findViewById(R.id.vp_btnDeleteVideo);
        vp_btnDelete.setOnClickListener(v -> {
            //notify delete action and dismiss dialog
            VP_galleryMediaListener.VP_onMenuClickListener(VP_GalleryMediaTypeAction.VP_DELETE);
            vp_dialog.dismiss();
        });

        //dismiss dialog
        Button vp_btnCancel = view.findViewById(R.id.vp_btnCancel);
        vp_btnCancel.setOnClickListener(v -> vp_dialog.dismiss());
        return vp_dialog;
    }


}
