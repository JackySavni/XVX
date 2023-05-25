package com.video.player.videoplayer.xvxvideoplayer.gallery.util;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * custom square image view
 */
public class VP_GallerySquareImageView extends AppCompatImageView {

    public VP_GallerySquareImageView(Context context) {
        super(context);
    }

    public VP_GallerySquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VP_GallerySquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

}