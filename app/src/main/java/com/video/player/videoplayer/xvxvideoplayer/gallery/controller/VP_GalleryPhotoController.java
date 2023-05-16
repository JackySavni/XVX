package com.video.player.videoplayer.xvxvideoplayer.gallery.controller;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.slider.Slider;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryPhotoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryFilter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;
import com.video.player.videoplayer.xvxvideoplayer.gallery.util.VP_GalleryFilterType;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageCrosshatchFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFalseColorFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHalftoneFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageKuwaharaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceThresholdFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageWhiteBalanceFilter;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ContrastFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.GPUFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.PixelationFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SwirlFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.VignetteFilterTransformation;

public class VP_GalleryPhotoController {

    /**
     * move view out of the screen
     *
     * @param view
     */
    public static void vp_moveLayoutTop(View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                view.setTranslationY(-view.getHeight());
            }
        });
    }

    /**
     * apply default image or filter
     *
     * @param vp_imgPath
     * @param vp_filter
     * @param vp_img
     * @param vp_context
     */
    public static void vp_applyFilter(String vp_imgPath, Transformation<Bitmap> vp_filter,
                                      ImageView vp_img, Context vp_context) {
        if (vp_filter == null) {
            Glide.with(vp_context)
                    .load(vp_imgPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(vp_img);
        } else {
            Glide.with(vp_context)
                    .load(vp_imgPath)
                    .apply(RequestOptions.bitmapTransform(vp_filter))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(vp_img);
        }
    }

    /**
     * get all stickers
     *
     * @return
     */
    public static ArrayList<Integer> vp_getStickers() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.vp_sticker_0);
        arrayList.add(R.drawable.vp_sticker_1);
        arrayList.add(R.drawable.vp_sticker_2);
        arrayList.add(R.drawable.vp_sticker_3);
        arrayList.add(R.drawable.vp_sticker_4);
        arrayList.add(R.drawable.vp_sticker_5);
        arrayList.add(R.drawable.vp_sticker_6);
        arrayList.add(R.drawable.vp_sticker_7);
        arrayList.add(R.drawable.vp_sticker_8);
        arrayList.add(R.drawable.vp_sticker_9);
        arrayList.add(R.drawable.vp_sticker_10);
        arrayList.add(R.drawable.vp_sticker_11);
        arrayList.add(R.drawable.vp_sticker_12);
        arrayList.add(R.drawable.vp_sticker_13);
        arrayList.add(R.drawable.vp_sticker_14);
        arrayList.add(R.drawable.vp_sticker_15);
        arrayList.add(R.drawable.vp_sticker_16);
        arrayList.add(R.drawable.vp_sticker_17);
        arrayList.add(R.drawable.vp_sticker_18);
        arrayList.add(R.drawable.vp_sticker_19);
        arrayList.add(R.drawable.vp_sticker_20);
        arrayList.add(R.drawable.vp_sticker_21);
        arrayList.add(R.drawable.vp_sticker_22);
        arrayList.add(R.drawable.vp_sticker_23);
        arrayList.add(R.drawable.vp_sticker_24);
        arrayList.add(R.drawable.vp_sticker_25);
        arrayList.add(R.drawable.vp_sticker_26);
        arrayList.add(R.drawable.vp_sticker_27);
        arrayList.add(R.drawable.vp_sticker_28);
        arrayList.add(R.drawable.vp_sticker_29);
        arrayList.add(R.drawable.vp_sticker_30);
        arrayList.add(R.drawable.vp_sticker_31);
        arrayList.add(R.drawable.vp_sticker_32);
        arrayList.add(R.drawable.vp_sticker_33);

        return arrayList;
    }

    /**
     * get all filters with init values
     *
     * @return
     */
    public static ArrayList<VP_GalleryFilter> ex_getFilters() {
        ArrayList<VP_GalleryFilter> vp_arrayList = new ArrayList<>();
        vp_arrayList.add(new VP_GalleryFilter(null, "None", VP_GalleryFilterType.VP_NONE));
        vp_arrayList.add(new VP_GalleryFilter(new BrightnessFilterTransformation(0.30f), "Brightness", VP_GalleryFilterType.VP_BRIGHTNESS));
        vp_arrayList.add(new VP_GalleryFilter(new ContrastFilterTransformation(2.0f), "Contrast", VP_GalleryFilterType.VP_CONTRAST));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageSaturationFilter(2.0f)), "Saturation", VP_GalleryFilterType.VP_SATURATION));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageHueFilter(55.0f)), "Hue", VP_GalleryFilterType.VP_HUE));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageWhiteBalanceFilter(2500f, 0f)), "White balance", VP_GalleryFilterType.VP_WHITE_BALANCE));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageGammaFilter(0.30f)), "Gamma", VP_GalleryFilterType.VP_GAMMA));
        vp_arrayList.add(new VP_GalleryFilter(new BlurTransformation(10, 3), "Blur", VP_GalleryFilterType.VP_BLUR));
        vp_arrayList.add(new VP_GalleryFilter(new GrayscaleTransformation(), "Gray", VP_GalleryFilterType.VP_GRAY));
        vp_arrayList.add(new VP_GalleryFilter(new ColorFilterTransformation(0x50FF0000), "Color", VP_GalleryFilterType.VP_COLOR));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageFalseColorFilter()), "False color", VP_GalleryFilterType.VP_FALSE_COLOR));
        vp_arrayList.add(new VP_GalleryFilter(new VignetteFilterTransformation(), "Vignette", VP_GalleryFilterType.VP_VIGNETTE));
        vp_arrayList.add(new VP_GalleryFilter(new InvertFilterTransformation(), "Invert", VP_GalleryFilterType.VP_INVERT));
        vp_arrayList.add(new VP_GalleryFilter(new PixelationFilterTransformation(15), "Pixel", VP_GalleryFilterType.VP_PIXELATION));
        vp_arrayList.add(new VP_GalleryFilter(new SepiaFilterTransformation(2.5f), "Sepia", VP_GalleryFilterType.VP_SEPIA));
        vp_arrayList.add(new VP_GalleryFilter(new SketchFilterTransformation(), "Sketch", VP_GalleryFilterType.VP_SKETCH));
        vp_arrayList.add(new VP_GalleryFilter(new ToonFilterTransformation(0.2f, 15.0f), "Toon", VP_GalleryFilterType.VP_TOON));
        vp_arrayList.add(new VP_GalleryFilter(new SwirlFilterTransformation(0.35f, 0.35f * 0.5f, new PointF(0.5f, 0.5f)), "Swirl", VP_GalleryFilterType.VP_SWIRL));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageLuminanceThresholdFilter(0.40f)), "Luminance", VP_GalleryFilterType.VP_LUMINANCE));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageCrosshatchFilter(0.02f, 0.003f)), "Crosshatch", VP_GalleryFilterType.VP_CROSSHATCH));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageDirectionalSobelEdgeDetectionFilter()), "Sobe", VP_GalleryFilterType.VP_SOBE));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageHalftoneFilter(0.03f)), "Halftone", VP_GalleryFilterType.VP_HALFTONE));
        vp_arrayList.add(new VP_GalleryFilter(new GPUFilterTransformation(new GPUImageKuwaharaFilter(3)), "Kuwahara", VP_GalleryFilterType.VP_KUWAHARA));
        return vp_arrayList;
    }

    /**
     * show or hide slider depending if filter has the option to change his values
     *
     * @param sliderFilter
     * @param VPGalleryFilterType
     */
    public static void vp_setSliderVisibility(Slider sliderFilter, VP_GalleryFilterType VPGalleryFilterType) {
        boolean hide = VPGalleryFilterType.equals(VP_GalleryFilterType.VP_NONE)
                || VPGalleryFilterType.equals(VP_GalleryFilterType.VP_COLOR)
                || VPGalleryFilterType.equals(VP_GalleryFilterType.VP_GRAY)
                || VPGalleryFilterType.equals(VP_GalleryFilterType.VP_INVERT)
                || VPGalleryFilterType.equals(VP_GalleryFilterType.VP_SKETCH)
                || VPGalleryFilterType.equals(VP_GalleryFilterType.VP_SOBE);

        sliderFilter.setVisibility(hide
                ? View.GONE : View.VISIBLE);
    }

    /**
     * get default colors
     * index 0 = -1 because user will see rainbow gradient
     *
     * @return
     */
    public static ArrayList<Integer> vp_getColors() {
        ArrayList<Integer> vp_arrayList = new ArrayList<>();
        vp_arrayList.add(0, -1);
        vp_arrayList.add(0xFFFFFFFF);
        vp_arrayList.add(0xFF000000);
        vp_arrayList.add(0xFF36474F);
        vp_arrayList.add(0xFF546f7a);
        vp_arrayList.add(0xFF90a4ad);
        vp_arrayList.add(0xFFee534f);
        vp_arrayList.add(0xFFff7143);
        vp_arrayList.add(0xFFffa827);
        vp_arrayList.add(0xFFffc928);
        vp_arrayList.add(0xFFffee58);
        vp_arrayList.add(0xFFd4e056);
        vp_arrayList.add(0xFF9ccc66);
        vp_arrayList.add(0xFF66bb6a);
        vp_arrayList.add(0xFF26a59a);
        vp_arrayList.add(0xFF25c6da);
        vp_arrayList.add(0xFF28b6f6);
        vp_arrayList.add(0xFF42a5f6);
        vp_arrayList.add(0xFF1d89e4);
        vp_arrayList.add(0xFF5c6bc0);
        vp_arrayList.add(0xFF7e57c2);
        vp_arrayList.add(0xFFaa47bc);
        vp_arrayList.add(0xFFec407a);

        return vp_arrayList;
    }

    /**
     * get all galleryPhotos
     *
     * @param contextWeakReference
     * @return
     */
    @SuppressLint({"InlinedApi", "SetTextI18n"})
    public static ArrayList<VP_GalleryPhoto> vp_getPhotos(WeakReference<Context> contextWeakReference) {

        ArrayList<VP_GalleryPhoto> VPGalleryPhotos = new ArrayList<>();
        Context vp_context = contextWeakReference.get();

        if (vp_context != null) {
            Uri vp_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] vp_projection;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                vp_projection = new String[]{MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DATE_MODIFIED,
                        MediaStore.Images.ImageColumns.RESOLUTION,
                        MediaStore.Images.ImageColumns.SIZE,
                        MediaStore.Images.ImageColumns.DISPLAY_NAME,
                        MediaStore.Images.ImageColumns._ID};
            }else {
               vp_projection = new String[]{MediaStore.Images.ImageColumns.DATA,
                       MediaStore.Images.ImageColumns.DATE_MODIFIED,
                       MediaStore.Images.ImageColumns.SIZE,
                       MediaStore.Images.ImageColumns.DISPLAY_NAME,
                       MediaStore.Images.ImageColumns._ID};
            }

            String vp_orderBy = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";
            Cursor vp_cursor = vp_context.getContentResolver().query(vp_uri, vp_projection, null, null, vp_orderBy);

            int vp_indexData = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
            int vp_indexDate = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED);
            int vp_indexSize = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE);
            int vp_indexDisplayName = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
            int vp_indexID = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);

            while (vp_cursor.moveToNext()) {
                String vp_path = vp_cursor.getString(vp_indexData);
                String vp_date = vp_cursor.getString(vp_indexDate);

                Long vp_size = vp_cursor.getLong(vp_indexSize);
                String vp_name = vp_cursor.getString(vp_indexDisplayName);
                Uri vp_uriPhoto = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, vp_cursor.getLong(vp_indexID));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    VPGalleryPhotos.add(new VP_GalleryPhoto(vp_uriPhoto, vp_name, vp_path, vp_date,
                            vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.RESOLUTION)),
                            vp_size));
                }else {
                    VPGalleryPhotos.add(new VP_GalleryPhoto(vp_uriPhoto, vp_name, vp_path, vp_date, "resolution", vp_size));
                }
            }

            vp_cursor.close();
        }
        return VPGalleryPhotos;
    }

    /**
     * show r.view when filter is type COLOR
     *
     * @param recyclerView
     * @param VPGalleryFilterType
     */
    public static void ex_setRViewColorsVisibility(RecyclerView recyclerView, VP_GalleryFilterType VPGalleryFilterType) {
        recyclerView.setVisibility(VPGalleryFilterType.equals(VP_GalleryFilterType.VP_COLOR) ? View.VISIBLE : View.GONE);
    }

    public static ScaleGestureDetector.SimpleOnScaleGestureListener
    getScaleGestureDetector(RecyclerView vp_recyclerView, VP_GalleryPhotoAdapter vp_adapter, ConstraintLayout vp_layoutPhotos) {
        return new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                vp_recyclerView.stopScroll();
                return super.onScaleBegin(detector);
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {

                //get layout manager
                GridLayoutManager vp_gridLayoutManager = (GridLayoutManager) vp_recyclerView.getLayoutManager();
                //get span count
                int vp_spanCount = vp_gridLayoutManager.getSpanCount();
                //detect actual span and set new span
                if (detector.getCurrentSpan() > 200 && detector.getTimeDelta() > 200) {
                    if (detector.getCurrentSpan() - detector.getPreviousSpan() < -1) {
                        if (vp_spanCount == 6) {
                            vp_setNewSpan(5, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        } else if (vp_spanCount == 5) {
                            vp_setNewSpan(4, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        } else if (vp_spanCount == 4) {
                            vp_setNewSpan(3, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        } else if (vp_spanCount == 3) {
                            vp_setNewSpan(2, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        }

                    } else if (detector.getCurrentSpan() - detector.getPreviousSpan() > 1) {

                        if (vp_spanCount == 2) {
                            vp_setNewSpan(3, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        } else if (vp_spanCount == 3) {
                            vp_setNewSpan(4, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        } else if (vp_spanCount == 4) {
                            vp_setNewSpan(5, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        } else if (vp_spanCount == 5) {
                            vp_setNewSpan(6, vp_recyclerView, vp_adapter, vp_layoutPhotos);
                            return true;
                        }

                    }

                }
                return false;
            }

        };

    }

    /**
     * change values from slider depending of filter type
     * this values mean min-max you can apply to filters
     *
     * @param vp_sliderFilter
     * @param VPGalleryFilterType
     */
    public static void vp_configSlider(Slider vp_sliderFilter, VP_GalleryFilterType VPGalleryFilterType) {
        switch (VPGalleryFilterType) {
            case VP_CONTRAST:
                vp_sliderFilter.setStepSize(0.25f);
                vp_sliderFilter.setValue(2.0f);
                vp_sliderFilter.setValueFrom(0.25f);
                vp_sliderFilter.setValueTo(4.0f);
                break;
            case VP_BRIGHTNESS:
                vp_sliderFilter.setStepSize(0.05f);
                vp_sliderFilter.setValue(0.30f);
                vp_sliderFilter.setValueFrom(-0.50f);
                vp_sliderFilter.setValueTo(0.50f);
                break;
            case VP_BLUR:
                vp_sliderFilter.setStepSize(1f);
                vp_sliderFilter.setValue(10.0f);
                vp_sliderFilter.setValueFrom(5.0f);
                vp_sliderFilter.setValueTo(25.0f);
                break;
            case VP_VIGNETTE:
                vp_sliderFilter.setStepSize(0.05f);
                vp_sliderFilter.setValue(0.75f);
                vp_sliderFilter.setValueFrom(0.5f);
                vp_sliderFilter.setValueTo(1.0f);
                break;
            case VP_PIXELATION:
                vp_sliderFilter.setStepSize(1f);
                vp_sliderFilter.setValue(15.0f);
                vp_sliderFilter.setValueFrom(1.0f);
                vp_sliderFilter.setValueTo(30.0f);
                break;
            case VP_SEPIA:
                vp_sliderFilter.setStepSize(0.25f);
                vp_sliderFilter.setValue(2.5f);
                vp_sliderFilter.setValueFrom(0.5f);
                vp_sliderFilter.setValueTo(5.0f);
                break;
            case VP_TOON:
                vp_sliderFilter.setStepSize(1f);
                vp_sliderFilter.setValue(15.0f);
                vp_sliderFilter.setValueFrom(1f);
                vp_sliderFilter.setValueTo(30.0f);
                break;
            case VP_SWIRL:
                vp_sliderFilter.setStepSize(0.05f);
                vp_sliderFilter.setValue(0.35f);
                vp_sliderFilter.setValueFrom(0.10f);
                vp_sliderFilter.setValueTo(1.0f);
                break;
            case VP_GAMMA:
                vp_sliderFilter.setStepSize(0.10f);
                vp_sliderFilter.setValue(0.30f);
                vp_sliderFilter.setValueFrom(0.10f);
                vp_sliderFilter.setValueTo(3.0f);
                break;
            case VP_SATURATION:
                vp_sliderFilter.setStepSize(0.10f);
                vp_sliderFilter.setValue(2.0f);
                vp_sliderFilter.setValueFrom(0.0f);
                vp_sliderFilter.setValueTo(2.0f);
                break;
            case VP_LUMINANCE:
                vp_sliderFilter.setStepSize(0.05f);
                vp_sliderFilter.setValue(0.40f);
                vp_sliderFilter.setValueFrom(0.20f);
                vp_sliderFilter.setValueTo(0.6f);
                break;
            case VP_HUE:
                vp_sliderFilter.setStepSize(5.0f);
                vp_sliderFilter.setValue(55.0f);
                vp_sliderFilter.setValueFrom(10.0f);
                vp_sliderFilter.setValueTo(100.0f);
                break;
            case VP_CROSSHATCH:
                vp_sliderFilter.setStepSize(0.001f);
                vp_sliderFilter.setValue(0.02f);
                vp_sliderFilter.setValueFrom(0.01f);
                vp_sliderFilter.setValueTo(0.03f);
                break;
            case VP_FALSE_COLOR:
                vp_sliderFilter.setStepSize(0.25f);
                vp_sliderFilter.setValue(1.0f);
                vp_sliderFilter.setValueFrom(1f);
                vp_sliderFilter.setValueTo(5.0f);
                break;
            case VP_HALFTONE:
                vp_sliderFilter.setStepSize(0.001f);
                vp_sliderFilter.setValue(0.03f);
                vp_sliderFilter.setValueFrom(0.01f);
                vp_sliderFilter.setValueTo(0.03f);
                break;
            case VP_KUWAHARA:
                vp_sliderFilter.setStepSize(1.0f);
                vp_sliderFilter.setValue(3.0f);
                vp_sliderFilter.setValueFrom(1.0f);
                vp_sliderFilter.setValueTo(15.0f);
                break;
            case VP_WHITE_BALANCE:
                vp_sliderFilter.setStepSize(500.0f);
                vp_sliderFilter.setValue(2500.0f);
                vp_sliderFilter.setValueFrom(1000.0f);
                vp_sliderFilter.setValueTo(15000.0f);
                break;
        }
    }

    /**
     * set new span count
     * start transition
     * notify adapter
     *
     * @param vp_spanCount
     * @param vp_recyclerView
     * @param vp_adapter
     * @param vp_layoutPhotos
     */
    private static void vp_setNewSpan(int vp_spanCount, RecyclerView vp_recyclerView,
                                      VP_GalleryPhotoAdapter vp_adapter, ConstraintLayout vp_layoutPhotos) {
        vp_recyclerView.post(() -> {
            try {
                TransitionManager.beginDelayedTransition(vp_layoutPhotos);
                GridLayoutManager vp_manager = (GridLayoutManager) vp_recyclerView.getLayoutManager();
                vp_manager.setSpanCount(vp_spanCount);
                vp_adapter.setVp_spanCount(vp_spanCount);
                vp_adapter.notifyDataSetChanged();

            } catch (NullPointerException e) {
                e.getMessage();
            }
        });
    }

    /**
     * apply new filter or new values to image
     *
     * @param vp_value
     * @param VPGalleryFilterType
     * @param vp_imagePath
     * @param vp_img
     * @param vp_context
     */
    public static void vp_applyNewFilterValues(float vp_value, VP_GalleryFilterType VPGalleryFilterType, String vp_imagePath, ImageView vp_img, Context vp_context) {
        switch (VPGalleryFilterType) {
            case VP_CONTRAST:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new ContrastFilterTransformation(vp_value), vp_img, vp_context);
                break;
            case VP_BRIGHTNESS:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new BrightnessFilterTransformation(vp_value), vp_img, vp_context);
                break;
            case VP_BLUR:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new BlurTransformation((int) vp_value), vp_img, vp_context);
                break;
            case VP_VIGNETTE:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new VignetteFilterTransformation(new PointF(0.5f, 0.5f), new float[]{0.0f, 0.0f, 0.0f}, vp_value - 0.75f, vp_value), vp_img, vp_context);
                break;
            case VP_PIXELATION:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new PixelationFilterTransformation(vp_value), vp_img, vp_context);
                break;
            case VP_SEPIA:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new SepiaFilterTransformation(vp_value), vp_img, vp_context);
                break;
            case VP_TOON:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new ToonFilterTransformation(0.2f, vp_value), vp_img, vp_context);
                break;
            case VP_SWIRL:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new SwirlFilterTransformation(vp_value, vp_value * 0.5f, new PointF(0.5f, 0.5f)), vp_img, vp_context);
                break;
            case VP_GAMMA:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageGammaFilter(vp_value)), vp_img, vp_context);
                break;
            case VP_SATURATION:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageSaturationFilter(vp_value)), vp_img, vp_context);
                break;
            case VP_LUMINANCE:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageLuminanceThresholdFilter(vp_value)), vp_img, vp_context);
                break;
            case VP_HUE:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageHueFilter(vp_value)), vp_img, vp_context);
                break;
            case VP_CROSSHATCH:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageCrosshatchFilter(vp_value, 0.003f)), vp_img, vp_context);
                break;
            case VP_FALSE_COLOR:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageFalseColorFilter(0f, 0f, vp_value / 2, vp_value, 0f, 0f)), vp_img, vp_context);
                break;
            case VP_HALFTONE:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageHalftoneFilter(vp_value)), vp_img, vp_context);
                break;
            case VP_KUWAHARA:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageKuwaharaFilter((int) vp_value)), vp_img, vp_context);
                break;
            case VP_WHITE_BALANCE:
                VP_GalleryPhotoController.vp_applyFilter(vp_imagePath, new GPUFilterTransformation(new GPUImageWhiteBalanceFilter(vp_value, 0f)), vp_img, vp_context);
                break;
        }
    }

}
