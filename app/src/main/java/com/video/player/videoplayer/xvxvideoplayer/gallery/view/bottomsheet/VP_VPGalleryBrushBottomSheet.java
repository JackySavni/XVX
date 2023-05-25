package com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.slider.Slider;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryColorsAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryBrushListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorUtil;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GallerySharedPrefsSettings;

import yuku.ambilwarna.AmbilWarnaDialog;

public class VP_VPGalleryBrushBottomSheet extends BottomSheetDialogFragment implements VP_GalleryColorListener,
        AmbilWarnaDialog.OnAmbilWarnaListener {

    private Slider vp_sliderBrushSize, vp_sliderOpacity;
    private RecyclerView vp_recyclerViewColors;
    private ImageView vp_imgBrushPreview;
    private final VP_GalleryBrushListener VP_galleryBrushListener;
    private int vp_initColor;

    public VP_VPGalleryBrushBottomSheet(VP_GalleryBrushListener VP_galleryBrushListener) {
        this.VP_galleryBrushListener = VP_galleryBrushListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ex_gallery_bottom_sheet_brush, container, false);
        vp_init(view);
        vp_setListeners();
        vp_setColorsAdapter();
        vp_setBrushSize();
        vp_setOpacity();
        //notify init color
        VP_galleryBrushListener.onColorChanged(vp_initColor);
        vp_setImgBrushPreviewColor(vp_initColor);

        return view;
    }

    /**
     * set last opacity
     */
    private void vp_setOpacity() {
        Context vp_context = getContext();
        if (vp_context != null) {
            int opacity = VP_GallerySharedPrefsSettings.getBrushOpacity(vp_context);
            vp_sliderOpacity.setValue(opacity);
            VP_galleryBrushListener.onOpacityChanged(opacity);
            vp_setImgBrushPreviewAlpha(opacity);
        }
    }

    /**
     * set last brush size
     */
    private void vp_setBrushSize() {
        Context vp_context = getContext();
        if (vp_context != null) {
            int brushSize = VP_GallerySharedPrefsSettings.getBrushSize(vp_context);
            vp_sliderBrushSize.setValue(brushSize);
            VP_galleryBrushListener.onBrushSizeChanged(brushSize);
            setImgBrushPreviewSize(brushSize);
        }
    }

    private void vp_init(View view) {
        this.vp_initColor = 0xFFee534f;
        this.vp_recyclerViewColors = view.findViewById(R.id.vp_recyclerViewColors);
        this.vp_sliderBrushSize = view.findViewById(R.id.vp_sliderBrushSize);
        this.vp_sliderOpacity = view.findViewById(R.id.vp_sliderOpacity);
        this.vp_imgBrushPreview = view.findViewById(R.id.vp_imgBrushPreview);
    }

    private void vp_setColorsAdapter() {
        this.vp_recyclerViewColors.setAdapter(new VP_GalleryColorsAdapter(this));
    }

    private void vp_setListeners() {
        this.vp_sliderBrushSize.addOnChangeListener((slider, value, vp_fromUser) -> {
            if (vp_fromUser && this.VP_galleryBrushListener != null) {
                //show label with text and value
                slider.setLabelFormatter(value1 -> "Brush size " + (int) value1);
                //notify brush size
                this.VP_galleryBrushListener.onBrushSizeChanged((int) value);
                setImgBrushPreviewSize((int) value);
                Context vp_context = getContext();
                if (vp_context != null) {
                    //save brush size
                    VP_GallerySharedPrefsSettings.vp_setBrushSize((int) value, vp_context);
                }
            }
        });

        this.vp_sliderOpacity.addOnChangeListener((vp_slider, value, vp_fromUser) -> {
            if (vp_fromUser && this.VP_galleryBrushListener != null) {
                //show label with text and value
                vp_slider.setLabelFormatter(value1 -> "Opacity " + (int) value1);
                //notify opacity
                this.VP_galleryBrushListener.onOpacityChanged((int) value);
                vp_setImgBrushPreviewAlpha(value);
                Context vp_context = getContext();
                if (vp_context != null) {
                    //save opacity
                    VP_GallerySharedPrefsSettings.setBrushOpacity((int) value, vp_context);
                }
            }
        });
    }

    @Override
    public void onCancel(AmbilWarnaDialog dialog) {

    }

    @Override
    public void onOk(AmbilWarnaDialog vp_dialog, int color) {
        if (VP_galleryBrushListener != null) {
            //notify color
            VP_galleryBrushListener.onColorChanged(color);
            vp_setImgBrushPreviewColor(color);
        }
    }

    @Override
    public void onColorClicked(int color, int vp_positionClicked) {
        //show dialog to select color or notify color selected from recycler view
        if (vp_positionClicked == 0) {
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(getContext(), VP_GalleryColorUtil.getRandomColor(), this);
            dialog.show();
        } else {
            if (VP_galleryBrushListener != null) {
                VP_galleryBrushListener.onColorChanged(color);
                vp_setImgBrushPreviewColor(color);
            }
        }
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

    /**
     * set image size
     *
     * @param size
     */
    private void setImgBrushPreviewSize(int size) {
        Context vp_context = getContext();
        if (vp_context != null) {
            ViewGroup.LayoutParams params = vp_imgBrushPreview.getLayoutParams();
            params.width = size;
            params.height = size;
            vp_imgBrushPreview.setLayoutParams(params);
            vp_imgBrushPreview.requestLayout();
        }
    }

    /**
     * set image color
     *
     * @param color
     */
    private void vp_setImgBrushPreviewColor(int color) {
        vp_imgBrushPreview.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * set image alpha
     *
     * @param alpha
     */
    private void vp_setImgBrushPreviewAlpha(float alpha) {
        vp_imgBrushPreview.setAlpha(alpha / 100);
    }

}