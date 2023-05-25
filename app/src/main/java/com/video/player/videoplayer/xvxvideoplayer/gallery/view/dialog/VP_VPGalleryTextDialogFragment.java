package com.video.player.videoplayer.xvxvideoplayer.gallery.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryColorsAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorUtil;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryTextListener;

import yuku.ambilwarna.AmbilWarnaDialog;

public class VP_VPGalleryTextDialogFragment extends DialogFragment implements VP_GalleryColorListener,
        AmbilWarnaDialog.OnAmbilWarnaListener {
    private TextInputEditText vp_txtPhoto;
    private MaterialButton vp_btnAddText;
    private RecyclerView vp_recyclerViewColors;
    private InputMethodManager vp_inputMethodManager;
    private final VP_GalleryTextListener VP_galleryTextListener;
    private int vp_color;

    public VP_VPGalleryTextDialogFragment(VP_GalleryTextListener VP_galleryTextListener) {
        this.VP_galleryTextListener = VP_galleryTextListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vp_gallery_dialog_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        vp_setColorsAdapter();
        vp_setTextColor();
        vp_showKeyboard();
        vp_setListeners();
    }

    private void init(View view) {
        this.vp_color = 0xFFFF0000;
        this.vp_txtPhoto = view.findViewById(R.id.vp_txtPhoto);
        Activity vp_activity = getActivity();
        if (vp_activity != null) {
            this.vp_inputMethodManager = (InputMethodManager) vp_activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        this.vp_btnAddText = view.findViewById(R.id.vp_btnAddText);
        this.vp_recyclerViewColors = view.findViewById(R.id.vp_recyclerViewColors);
    }

    @Override
    public void onStart() {
        super.onStart();
        setFullScreenTransparent();
    }

    /**
     * dialog full screen with transparent background
     */
    private void setFullScreenTransparent() {
        Dialog vp_dialog = getDialog();
        if (vp_dialog != null) {
            int vp_width = ViewGroup.LayoutParams.MATCH_PARENT;
            int vp_height = ViewGroup.LayoutParams.MATCH_PARENT;
            vp_dialog.getWindow().setLayout(vp_width, vp_height);
            vp_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    /**
     * focus edit text
     * show keyboard
     */
    private void vp_showKeyboard() {
        this.vp_txtPhoto.requestFocus();
        this.vp_inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * hide keyboard
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        this.vp_inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * set text color
     */
    private void vp_setTextColor() {
        this.vp_txtPhoto.setTextColor(vp_color);
    }

    /**
     * set new color
     *
     * @param vp_color
     */
    public void setVp_color(int vp_color) {
        this.vp_color = vp_color;
    }

    private void vp_setColorsAdapter() {
        this.vp_recyclerViewColors.setAdapter(new VP_GalleryColorsAdapter(this));
    }

    private void vp_setListeners() {
        this.vp_btnAddText.setOnClickListener(view -> {
            hideKeyboard(view);
            vp_addText();
            dismiss();
        });
    }

    /**
     * get text and notify
     */
    private void vp_addText() {
        String vp_text = this.vp_txtPhoto.getText().toString();
        if (!TextUtils.isEmpty(vp_text) && this.VP_galleryTextListener != null) {
            this.VP_galleryTextListener.onTextChanged(vp_text, this.vp_color);
        }
    }

    @Override
    public void onColorClicked(int color, int positionClicked) {
        //show dialog to select color or notify color selected from recycler view
        if (positionClicked == 0) {
            AmbilWarnaDialog vp_dialog = new AmbilWarnaDialog(getContext(), VP_GalleryColorUtil.getRandomColor(), this);
            vp_dialog.show();
        } else {
            setVp_color(color);
            vp_setTextColor();
        }
    }

    @Override
    public void onCancel(AmbilWarnaDialog dialog) {

    }

    @Override
    public void onOk(AmbilWarnaDialog dialog, int color) {
        setVp_color(color);
        vp_setTextColor();
    }

}
