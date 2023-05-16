package com.video.player.videoplayer.xvxvideoplayer.gallery.view.viewer;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.slider.Slider;
import com.squareup.picasso.Picasso;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryColorsAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryFiltersAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryMediaController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryPhotoController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryFilter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryBitmapUtils;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryBrushListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryColorUtil;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryCropUtils;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryEmojiListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryFilterListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryMediaListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryMediaTypeAction;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GallerySetWallpaperListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryStickerListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryStorageUtils;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryTextListener;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryTypeScreen;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.tasks.VP_GallerySetWallpaperTask;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet.VP_GalleryMediaBottomSheet;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet.VP_GallerySetWallpaperBottomSheet;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet.VP_VPGalleryBrushBottomSheet;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet.VP_VPGalleryEmojiBottomSheet;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.bottomsheet.VP_VPGalleryStickerBottomSheet;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.dialog.VP_GalleryDeleteMediaDialogFragment;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.dialog.VP_VPGalleryTextDialogFragment;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.viven.imagezoom.ImageZoomHelper;
import com.yalantis.ucrop.UCrop;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import yuku.ambilwarna.AmbilWarnaDialog;

public class VP_GalleryPhotoActivity extends AppCompatActivity implements VP_GalleryMediaListener, VP_GalleryFilterListener,
        VP_GalleryColorListener, AmbilWarnaDialog.OnAmbilWarnaListener, VP_GalleryEmojiListener, VP_GalleryBrushListener,
        VP_GalleryTextListener, VP_GalleryStickerListener, VP_GallerySetWallpaperListener {

    public static final int VP_IMG_CROP_CODE = 333;
    //
    private VP_GalleryPhoto VPGalleryPhotoRaw;
    private int vp_photoPosition;

    private String vp_baseActivity;
    //
    private PhotoEditorView vp_photoEditorView;
    private PhotoEditor vp_photoEditor;
    private ImageView vp_img, vp_imgArrow;
    private ImageZoomHelper vp_imageZoomHelper;
    private ImageButton vp_btnWallpaper, vp_btnMore, vp_btnEdit, vp_btnText, vp_btnCrop, vp_btnSticker, vp_btnEmoji, vp_btnBrush, vp_btnEraser;
    private View vp_layoutViewPhoto, vp_layoutButtons, vp_layoutButtonsTop;
    private RecyclerView vp_recyclerViewFilters, vp_recyclerViewColors;
    private BottomSheetBehavior<LinearLayout> vp_bottomSheetFilters, vp_bottomSheetDetails;
    private Slider vp_sliderFilter;
    private VP_GalleryFilter VPGalleryFilterSelected = null;
    private VP_GalleryFiltersAdapter vp_filtersAdapter;
    private boolean vp_brushEnabled = false;
    private LottieAnimationView vp_animSettingWallpaper, vp_animDoneWallpaper;

    private TextView vp_dateTimeTv, vp_pathTv, vp_pixelTv, vp_resolutionTv, vp_sizeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_gallery_activity_photo);
        vp_getExtras();
        vp_init();
        vp_setListeners();
        vp_setAdapterFilters();
        vp_setAdapterColors();
        VP_GalleryPhotoController.vp_moveLayoutTop(vp_layoutButtonsTop);
        //show default photo

        vp_loadImage();

        //hide bottom sheet filters
        vp_bottomSheetFilters.setState(BottomSheetBehavior.STATE_HIDDEN);
        vp_bottomSheetDetails.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void vp_loadImage() {
        Glide.with(this).load(VPGalleryPhotoRaw.getVp_uri())
                .apply(new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (!VP_GalleryPhotoActivity.this.isDestroyed() && !VP_GalleryPhotoActivity.this.isFinishing()) {
                    Picasso picasso = Picasso.get();
                    picasso.setIndicatorsEnabled(true);
                    picasso.load(VPGalleryPhotoRaw.getVp_uri())
                            .rotate(getRightAngleImage(VPGalleryPhotoRaw.getVp_path()))
                            .into(vp_img);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable vp_resource, Object vp_model, Target<Drawable> vp_target, DataSource vp_dataSource, boolean vp_isFirstResource) {
                return false;
            }
        }).into(vp_img);
    }

    public int getRightAngleImage(String vp_imageFilePath) {
        int vp_rotate = 0;
        try {
            ExifInterface vp_exif;

            vp_exif = new ExifInterface(vp_imageFilePath);
            String exifOrientation = vp_exif
                    .getAttribute(ExifInterface.TAG_ORIENTATION);
            int vp_orientation = vp_exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (vp_orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    vp_rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    vp_rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    vp_rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vp_rotate;
    }

    private void vp_init() {
        this.vp_photoEditorView = findViewById(R.id.vp_photoEditorView);
        this.vp_img = this.vp_photoEditorView.getSource();
        this.vp_imageZoomHelper = new ImageZoomHelper(this);
        ImageZoomHelper.setViewZoomable(vp_photoEditorView);
        this.vp_photoEditor = new PhotoEditor.Builder(this, vp_photoEditorView).setPinchTextScalable(true).build();
        this.vp_btnWallpaper = findViewById(R.id.vp_btnWallpaper);
        this.vp_btnMore = findViewById(R.id.vp_btnMore);
        this.vp_layoutViewPhoto = findViewById(R.id.vp_layoutViewPhoto);
        this.vp_btnEdit = findViewById(R.id.vp_btnEdit);
        this.vp_btnText = findViewById(R.id.vp_btnText);
        this.vp_btnCrop = findViewById(R.id.vp_btnCrop);
        this.vp_btnSticker = findViewById(R.id.vp_btnSticker);
        this.vp_btnEmoji = findViewById(R.id.vp_btnEmoji);
        this.vp_btnBrush = findViewById(R.id.vp_btnBrush);
        this.vp_btnEraser = findViewById(R.id.vp_btnEraser);
        LinearLayout layoutFilters = findViewById(R.id.vp_bottomSheetFilters);
        LinearLayout layoutDetails = findViewById(R.id.bottomSheetPhotoDetails);
        this.vp_bottomSheetFilters = BottomSheetBehavior.from(layoutFilters);
        this.vp_bottomSheetDetails = BottomSheetBehavior.from(layoutDetails);
        this.vp_recyclerViewFilters = findViewById(R.id.vp_recyclerViewFilters);
        this.vp_recyclerViewColors = findViewById(R.id.vp_recyclerViewColors);
        this.vp_imgArrow = findViewById(R.id.vp_imgArrow);
        this.vp_layoutButtons = findViewById(R.id.vp_layoutButtons);
        this.vp_sliderFilter = findViewById(R.id.vp_sliderFilter);
        this.vp_filtersAdapter = new VP_GalleryFiltersAdapter(this, VPGalleryPhotoRaw.getVp_path(), this);
        this.vp_layoutButtonsTop = findViewById(R.id.vp_layoutButtonsTop);
        this.vp_animSettingWallpaper = findViewById(R.id.vp_animSettingWallpaper);
        this.vp_animDoneWallpaper = findViewById(R.id.vp_animDoneWallpaper);
        this.vp_dateTimeTv = findViewById(R.id.vp_date_time_tv);
        this.vp_pathTv = findViewById(R.id.vp_path_tv);
        this.vp_pixelTv = findViewById(R.id.vp_pixel_tv);
        this.vp_resolutionTv = findViewById(R.id.vp_resolution_tv);
        this.vp_sizeTv = findViewById(R.id.vp_size_tv);
    }

    /**
     * get photo and position (photo adapter) from bundle
     */
    private void vp_getExtras() {
        try {
            Bundle vp_bundle = getIntent().getExtras();
            this.VPGalleryPhotoRaw = vp_bundle.getParcelable(VP_GalleryPhoto.VD_PHOTO_KEY);
            this.vp_photoPosition = vp_bundle.getInt(VP_GalleryPhoto.VD_PHOTO_POSITION_KEY, -1);
            this.vp_baseActivity = vp_bundle.getString("BaseActivity");
        } catch (NullPointerException e) {
            e.getMessage();
        }
    }

    @Override
    protected void onActivityResult(int vp_requestCode, int vp_resultCode, @Nullable Intent vp_data) {
        super.onActivityResult(vp_requestCode, vp_resultCode, vp_data);
        switch (vp_requestCode) {
            case VP_GalleryMediaController.REQ_CODE_DELETE:
                if (vp_resultCode == RESULT_OK) {
                    if (vp_baseActivity.equals("GalleryAlbumActivity")) {
                        int i = ((VP_MyApplication) getApplicationContext()).vp_deleteAlbActPhotoPos;
                        if (i != -1) {
                            ((VP_MyApplication) getApplicationContext()).VPGalleryAlbum.getPhotos().remove(i);
                        }
                    }
                    finish();
                }
                break;
            case VP_GalleryPhotoActivity.VP_IMG_CROP_CODE:
                if (vp_resultCode == RESULT_OK && vp_data != null) {
                    try {
                        //get uri from img cropped
                        Uri vp_imgCropped = UCrop.getOutput(vp_data);
                        if (vp_imgCropped != null) {
                            //show the new image skipping cache
                            Glide.with(this).load(vp_imgCropped)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(vp_img);
                            //set path from new image
                            VPGalleryPhotoRaw.setVp_path(vp_imgCropped.getPath());
                        }
                    } catch (NullPointerException e) {
                        e.getMessage();
                    }
                }
                break;
        }
    }

    private void vp_setListeners() {
        this.vp_btnWallpaper.setOnClickListener(v -> {
            VP_GallerySetWallpaperBottomSheet VPGallerySetWallpaperBottomSheet = new VP_GallerySetWallpaperBottomSheet(this);
            VPGallerySetWallpaperBottomSheet.show(getSupportFragmentManager(), VP_GallerySetWallpaperBottomSheet.class.getName());
        });

        this.vp_btnMore.setOnClickListener(view -> {
            VP_GalleryMediaBottomSheet VPGalleryMediaBottomSheet = new VP_GalleryMediaBottomSheet(VP_GalleryPhotoActivity.this);
            VPGalleryMediaBottomSheet.show(getSupportFragmentManager(), VP_GalleryMediaBottomSheet.class.getName());
        });

        this.vp_bottomSheetFilters.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //enable-disable image zoom depending of the state of bottom sheet filters
                ImageZoomHelper.setZoom(vp_photoEditorView, newState != BottomSheetBehavior.STATE_COLLAPSED
                        && newState != BottomSheetBehavior.STATE_EXPANDED);

                //enable-disable buttons from bottom sheet filters
                boolean enable = newState != BottomSheetBehavior.STATE_EXPANDED;
                vp_btnText.setEnabled(enable);
                vp_btnCrop.setEnabled(enable);
                vp_btnSticker.setEnabled(enable);
                vp_btnEmoji.setEnabled(enable);
                vp_btnBrush.setEnabled(enable);
                vp_btnEraser.setEnabled(enable);

                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if (VPGalleryFilterSelected != null) {
                            //show-hide slider | recycler view colors depending of filter type
                            VP_GalleryPhotoController.vp_setSliderVisibility(vp_sliderFilter, VPGalleryFilterSelected.getFilterType());
                            VP_GalleryPhotoController.ex_setRViewColorsVisibility(vp_recyclerViewColors, VPGalleryFilterSelected.getFilterType());
                        }
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        //disable brush and change color from button brush
                        vp_btnBrush.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                        vp_photoEditor.setBrushDrawingMode(false);
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View vp_bottomSheet, float vp_slideOffset) {

                if (vp_slideOffset > 0) {
                    //rotate from 0 to 180
                    vp_imgArrow.setRotation(vp_slideOffset * 180);

                    //change scale of photo editor
                    vp_photoEditorView.setScaleX(1f - (0.25f * vp_slideOffset));
                    vp_photoEditorView.setScaleY(1f - (0.25f * vp_slideOffset));
                    //change translation Y of photo editor
                    vp_photoEditorView.setTranslationY(-(250 * vp_slideOffset));

                    //change alpha of layout that contains all buttons in bottom sheet filters
                    vp_layoutButtons.setAlpha(1 - vp_slideOffset);
                }

                if (vp_slideOffset < 0) {
                    //show-hide layout with main buttons : set as wallpaper, edit and menu
                    vp_layoutViewPhoto.setTranslationY((1 + vp_slideOffset) * vp_layoutViewPhoto.getHeight());
                    //show-hide layout with undo, redo and save buttons
                    vp_layoutButtonsTop.setTranslationY(vp_slideOffset * vp_layoutButtonsTop.getHeight());
                }
                //hide slider and recycler view if is edit mode
                vp_sliderFilter.setVisibility(View.GONE);
                vp_recyclerViewColors.setVisibility(View.GONE);
            }
        });

        this.vp_bottomSheetDetails.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if (slideOffset > 0) {
                    //change translation Y of photo editor
                    vp_photoEditorView.setTranslationY(-(250 * slideOffset));

                }
            }
        });

        this.vp_btnEdit.setOnClickListener(v -> vp_bottomSheetFilters.setState(BottomSheetBehavior.STATE_COLLAPSED));

        this.vp_sliderFilter.addOnChangeListener((slider, value, fromUser) -> {
            //apply new filter if slider change value, filter selected and draw mode disabled
            if (fromUser && VPGalleryFilterSelected != null && !vp_photoEditor.getBrushDrawableMode()) {
                VP_GalleryPhotoController.vp_applyNewFilterValues(value, VPGalleryFilterSelected.getFilterType(), VPGalleryPhotoRaw.getVp_path(), vp_img, this);
            }
        });

        this.vp_btnCrop.setOnClickListener(v -> VP_GalleryCropUtils.vp_cropImage(VPGalleryPhotoRaw.getVp_uri(),
                this, VP_GalleryPhotoActivity.VP_IMG_CROP_CODE));

        this.vp_btnSticker.setOnClickListener(v -> {
            VP_VPGalleryStickerBottomSheet stickerBottomSheet = new VP_VPGalleryStickerBottomSheet(this);
            stickerBottomSheet.show(getSupportFragmentManager(), stickerBottomSheet.getTag());
        });

        this.vp_btnEmoji.setOnClickListener(v -> {
            VP_VPGalleryEmojiBottomSheet emojiBottomSheet = new VP_VPGalleryEmojiBottomSheet(this);
            emojiBottomSheet.show(getSupportFragmentManager(), emojiBottomSheet.getTag());
        });

        this.vp_btnBrush.setOnClickListener(v -> {
            vp_toggleBrushMode();
        });

        this.vp_btnEraser.setOnClickListener(v -> vp_photoEditor.brushEraser());

        this.vp_btnText.setOnClickListener(v -> {
            VP_VPGalleryTextDialogFragment vp_GalleryTextDialogFragment = new VP_VPGalleryTextDialogFragment(this);
            vp_GalleryTextDialogFragment.show(getSupportFragmentManager(), VP_VPGalleryTextDialogFragment.class.getName());
        });

        findViewById(R.id.vp_btnUndo).setOnClickListener(v -> vp_photoEditor.undo());

        findViewById(R.id.vp_btnRedo).setOnClickListener(v -> vp_photoEditor.redo());

        findViewById(R.id.vp_btnSave).setOnClickListener(v -> vp_saveImage());
    }

    /**
     * show bottom sheet edit brush
     * enable-disable brush mode
     * change button color
     */
    private void vp_toggleBrushMode() {
        if (!this.vp_brushEnabled) {
            this.vp_filtersAdapter.setVp_brushModeEnabled(true);
            VP_VPGalleryBrushBottomSheet exGalleryBrushBottomSheet = new VP_VPGalleryBrushBottomSheet(this);
            exGalleryBrushBottomSheet.show(getSupportFragmentManager(), exGalleryBrushBottomSheet.getTag());
        }
        this.vp_btnBrush.setImageTintList(ColorStateList.valueOf(this.vp_brushEnabled
                ? Color.WHITE : ResourcesCompat.getColor(getResources(), R.color.purple_500, null)));
        this.vp_brushEnabled = !this.vp_brushEnabled;
        this.vp_photoEditor.setBrushDrawingMode(this.vp_brushEnabled);
    }

    /**
     * save image if write external storage is enabled
     * set path of edited image
     * notify new image
     */

    private void vp_saveImage() {
        if (permission() == PackageManager.PERMISSION_GRANTED) {
            vp_photoEditor.saveAsFile(VP_GalleryStorageUtils.getImageFile().getPath(), new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String imagePath) {
                    VPGalleryPhotoRaw.setVp_path(imagePath);
                    VP_GalleryStorageUtils.notifyNewImage(imagePath, getBaseContext());
                    Toast.makeText(VP_GalleryPhotoActivity.this, "Image Saved Successfully...", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> finish(), 1000);
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }
    }

    private int permission() {
        int permission = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkSelfPermission(READ_MEDIA_IMAGES);
        } else {
            checkSelfPermission(WRITE_EXTERNAL_STORAGE);
        }
        return permission;
    }

//    private void vp_saveImage() {
//        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
//            vp_photoEditor.saveAsFile(VP_GalleryStorageUtils.getImageFile().getPath(), new PhotoEditor.OnSaveListener() {
//                @Override
//                public void onSuccess(@NonNull String imagePath) {
//                    VPGalleryPhotoRaw.setVp_path(imagePath);
//                    VP_GalleryStorageUtils.notifyNewImage(imagePath, getBaseContext());
//                    Toast.makeText(VP_GalleryPhotoActivity.this, "onSuccess", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    Toast.makeText(VP_GalleryPhotoActivity.this, "onFailure", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//        }
//    }

    /**
     * set adapter filters
     */
    private void vp_setAdapterFilters() {
        this.vp_recyclerViewFilters.setAdapter(this.vp_filtersAdapter);
    }

    /**
     * set adapter colors
     */
    private void vp_setAdapterColors() {
        this.vp_recyclerViewColors.setAdapter(new VP_GalleryColorsAdapter(this));
    }

    /**
     * pass all touch events to the imageZoomHelper
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return vp_imageZoomHelper.onDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        //hide filters
        if (vp_bottomSheetFilters.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            vp_bottomSheetFilters.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (vp_bottomSheetFilters.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            vp_bottomSheetFilters.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (vp_bottomSheetDetails.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            vp_bottomSheetDetails.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (vp_bottomSheetDetails.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            vp_bottomSheetDetails.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void VP_onMenuClickListener(VP_GalleryMediaTypeAction vp_typeAction) {
        switch (vp_typeAction) {
            case VP_SHARE:
                //share photo
                VP_GalleryMediaController.ex_shareMedia(VPGalleryPhotoRaw.getVp_uri(), "Share image", "image/jpeg", this);
                break;
            case VP_SHOW_DIALOG_DELETE:
                //show dialog to before delete photo
                VP_GalleryDeleteMediaDialogFragment videoDialogFragment = new VP_GalleryDeleteMediaDialogFragment(VPGalleryPhotoRaw.getVp_name(), VP_GalleryPhotoActivity.this);
                videoDialogFragment.show(getSupportFragmentManager(), videoDialogFragment.getClass().getSimpleName());
                break;
            case VP_DELETE:
                //delete photo and close this activity
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    VP_GalleryMediaController.ex_deleteMedia(new ArrayList<>(Collections.singletonList(VPGalleryPhotoRaw.getVp_uri())), new ArrayList<>(Collections.singletonList(VPGalleryPhotoRaw)), vp_photoPosition, this);
                } else {
                    VP_GalleryMediaController.vp_deleteMediaFile(new ArrayList<>(Collections.singletonList(VPGalleryPhotoRaw.getVp_path())), new ArrayList<>(Collections.singletonList(vp_photoPosition)), true, this);
                    finish();
                }
                break;
            case VP_DETAILS:
                vp_bottomSheetDetails.setState(BottomSheetBehavior.STATE_EXPANDED);
                vp_dateTimeTv.setText(getDate(Long.parseLong(VPGalleryPhotoRaw.getVp_date())));
                vp_pathTv.setText(VPGalleryPhotoRaw.getVp_path());
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(VPGalleryPhotoRaw.getVp_uri()),
                            null,
                            options);
                    int imageWidth = options.outWidth;
                    int imageHeight = options.outHeight;
                    float pixelInt = (float) (imageWidth * imageHeight) / 1000000;
                    vp_pixelTv.setText(String.format(Locale.US, "%.1f", pixelInt) + "MP");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!VPGalleryPhotoRaw.getVp_resolution().equals("resolution")){
                    vp_resolutionTv.setText(VPGalleryPhotoRaw.getVp_resolution());
                }else {
                    ExifInterface exif = null;
                    int width = 0,height=0;
                    try {
                        exif = new ExifInterface(VPGalleryPhotoRaw.getVp_path());
                        width = exif.getAttributeInt( ExifInterface.TAG_IMAGE_WIDTH, 0 );
                        height = exif.getAttributeInt( ExifInterface.TAG_IMAGE_LENGTH, 0 );

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String resolution = width+"x"+height;
                    vp_resolutionTv.setText(resolution);
                }
                vp_sizeTv.setText(Formatter.formatFileSize(VP_GalleryPhotoActivity.this, VPGalleryPhotoRaw.getVp_size()));
                break;
        }
    }

    private String getDate(long timeStamp) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    @Override
    public void onColorClicked(int color, int positionClicked) {
        if (positionClicked == 0) {
            //show dialog to select color
            AmbilWarnaDialog vp_dialog = new AmbilWarnaDialog(this, VP_GalleryColorUtil.getRandomColor(), this);
            vp_dialog.show();
        } else {
            //apply color filter
            VP_GalleryPhotoController.vp_applyFilter(VPGalleryPhotoRaw.getVp_path(),
                    new ColorFilterTransformation(ColorUtils.setAlphaComponent(color, 126)),
                    vp_img, this);
        }
    }

    @Override
    public void onCancel(AmbilWarnaDialog dialog) {

    }

    @Override
    public void onOk(AmbilWarnaDialog dialog, int color) {
        //apply color filter
        VP_GalleryPhotoController.vp_applyFilter(VPGalleryPhotoRaw.getVp_path(),
                new ColorFilterTransformation(ColorUtils.setAlphaComponent(color, 126)),
                vp_img, this);
    }

    @Override
    public void onEmojiClicked(String emoji) {
        //add new emoji
        this.vp_photoEditor.addEmoji(emoji);
    }

    @Override
    public void onColorChanged(int colorCode) {
        //set new brush color
        this.vp_photoEditor.setBrushColor(colorCode);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        //set new brush opacity
        this.vp_photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        //set new brush size
        this.vp_photoEditor.setBrushSize(brushSize);
    }

    @Override
    public void onTextChanged(String text, int color) {
        //add text with color
        TextStyleBuilder styleBuilder = new TextStyleBuilder();
        styleBuilder.withTextColor(color);
        this.vp_photoEditor.addText(text, styleBuilder);
    }

    @Override
    public void vp_onStickerClicked(int imgRes) {
        //add sticker
        vp_photoEditor.addImage(VP_GalleryBitmapUtils.vp_getBitmap(imgRes, getResources()));
    }

    @Override
    public void vp_onFilterClicked(VP_GalleryFilter VPGalleryFilter) {
        //set galleryFilter selected
        this.VPGalleryFilterSelected = VPGalleryFilter;
        //apply new galleryFilter
        VP_GalleryPhotoController.vp_applyFilter(VPGalleryPhotoRaw.getVp_path(), VPGalleryFilter.getVp_transformation(), vp_img, this);
        //set new values to slider
        VP_GalleryPhotoController.vp_configSlider(vp_sliderFilter, VPGalleryFilter.getFilterType());
        //show-hide slider
        VP_GalleryPhotoController.vp_setSliderVisibility(vp_sliderFilter, VPGalleryFilter.getFilterType());
        //show-hide recycler view colors
        VP_GalleryPhotoController.ex_setRViewColorsVisibility(vp_recyclerViewColors, VPGalleryFilter.getFilterType());
    }

    @Override
    public void VP_onButtonSetWallpaperClicked(VP_GalleryTypeScreen VPGalleryTypeScreen) {
        //get bitmap from path
        Bitmap vp_bitmap = VP_GalleryBitmapUtils.vp_getBitmap(VPGalleryPhotoRaw.getVp_path());
        switch (VPGalleryTypeScreen) {
            case VP_HOME:
                //set wallpaper to home screen
                new VP_GallerySetWallpaperTask(this, vp_animDoneWallpaper, vp_animSettingWallpaper).execute(vp_bitmap, 1);
                break;
            case VP_LOCK:
                //set wallpaper to lock screen
                new VP_GallerySetWallpaperTask(this, vp_animDoneWallpaper, vp_animSettingWallpaper).execute(vp_bitmap, 2);
                break;
            case VP_HOME_LOCK:
                //set wallpaper to both screens
                new VP_GallerySetWallpaperTask(this, vp_animDoneWallpaper, vp_animSettingWallpaper).execute(vp_bitmap, 3);
                break;
        }
    }

}