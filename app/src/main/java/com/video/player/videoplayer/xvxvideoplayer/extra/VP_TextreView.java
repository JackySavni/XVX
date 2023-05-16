package com.video.player.videoplayer.xvxvideoplayer.extra;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

public class VP_TextreView extends android.view.TextureView {
    protected static final String TAG = "JZResizeTextureView";
    public int vp_currentVideoHeight;
    public int vp_currentVideoWidth;

    public VP_TextreView(Context context) {
        super(context);
        this.vp_currentVideoWidth = 0;
        this.vp_currentVideoHeight = 0;
        this.vp_currentVideoWidth = 0;
        this.vp_currentVideoHeight = 0;
    }

    public VP_TextreView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.vp_currentVideoWidth = 0;
        this.vp_currentVideoHeight = 0;
        this.vp_currentVideoWidth = 0;
        this.vp_currentVideoHeight = 0;
    }

    public void setVideoSize(int i, int i2) {
        if (this.vp_currentVideoWidth != i || this.vp_currentVideoHeight != i2) {
            this.vp_currentVideoWidth = i;
            this.vp_currentVideoHeight = i2;
            requestLayout();
        }
    }

    public void setRotation(float f) {
        if (f != getRotation()) {
            super.setRotation(f);
            requestLayout();
        }
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("SixeCheck", "" + vp_currentVideoHeight);
//        int width = getDefaultSize(currentVideoWidth, videoWidth);
//        int height = getDefaultSize(currentVideoHeight, videoHeight);
//        setMeasuredDimension(width, height);
        double mAspectRatio = (double) vp_currentVideoHeight / vp_currentVideoWidth;
        if (mAspectRatio < 0) {
            setMeasuredDimension((int) (mAspectRatio * widthMeasureSpec), heightMeasureSpec);
        } else {
            setMeasuredDimension(widthMeasureSpec, (int) (mAspectRatio * heightMeasureSpec));
        }
//        TextureView textureView = Jzvd.currentVd.textureView;
//        int viewWidth = textureView.getWidth();
//        int viewHeight = textureView.getHeight();
//        double aspectRatio = (double) videoHeight / videoWidth;
//        int newWidth, newHeight;
//        if (viewHeight > (int) (viewWidth * aspectRatio)) {
//            // limited by narrow width; restrict height
//            newWidth = viewWidth;
//            newHeight = (int) (viewWidth * aspectRatio);
//        } else {
//            // limited by short height; restrict width
//            newWidth = (int) (viewHeight / aspectRatio);
//            newHeight = viewHeight;
//        }
//        int xoff = (viewWidth - newWidth) / 2;
//        int yoff = (viewHeight - newHeight) / 2;
//        Matrix txform = new Matrix();
//        textureView.getTransform(txform);
//        txform.setScale((float) newWidth / viewWidth, (float) newHeight / viewHeight);
//        txform.postTranslate(xoff, yoff);
//        textureView.setTransform(txform);

//        int width;
//        int height;
//        int wantedWidth,mWidth = 0;
//        int wantedHeight,mHeight = 0;
//        int mOrientation = (int) getRotation();
//        double mAspectRatio = (double) heightMeasureSpec / widthMeasureSpec;
//        if(mWidth == 0 && mHeight == 0 ){
//            mWidth = MeasureSpec.getSize(widthMeasureSpec);
//            mHeight =MeasureSpec.getSize(heightMeasureSpec);
//        }
//
//        width = mWidth;
//        height = mHeight;
//
//        int mMargin = 5;
//        if (mOrientation == 0 || mOrientation == 180) {
//
//            wantedWidth = width  - (int)(mMargin * 2);
//
////            mVideo.measure(MeasureSpec.makeMeasureSpec(wantedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((int) (wantedWidth * mVideoAspectRatio), MeasureSpec.EXACTLY));
//            wantedHeight = (((View) getParent()).getLayoutParams().height) * 2 + (int) (wantedWidth * mAspectRatio);
//
//        } else {
//            Log.e(TAG, "Real Width = " + width + " real Height = " + height);
//
//            wantedHeight = width - 2 * ((View) getParent()).getLayoutParams().height - (int)(mMargin * 2);
//
////            mVideo.measure(MeasureSpec.makeMeasureSpec(wantedHeight, MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec((int) (wantedHeight * mAspectRatio), MeasureSpec.EXACTLY));
////
//            wantedWidth =(int) (wantedHeight * mAspectRatio) ;
//            wantedHeight =  width - (int)(mMargin * 2);
//        }
//
//        Log.e(TAG, "onMeasure: " + wantedWidth + "x" + wantedHeight);
//        setMeasuredDimension(MeasureSpec.makeMeasureSpec(wantedWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(wantedHeight, MeasureSpec.EXACTLY));
//

//        int i3;
//        int i4;
//        Log.i(TAG, "onMeasure  [" + hashCode() + "] ");
//        int rotation = (int) getRotation();
//        int i5 = this.currentVideoWidth;
//        int i6 = this.currentVideoHeight;
//        int measuredHeight = ((View) getParent()).getMeasuredHeight();
//        int measuredWidth = ((View) getParent()).getMeasuredWidth();
//        if (!(measuredWidth == 0 || measuredHeight == 0 || i5 == 0 || i6 == 0 || Jzvd.VIDEO_IMAGE_DISPLAY_TYPE != 1)) {
//            if (rotation == 90 || rotation == 270) {
//                measuredWidth = measuredHeight;
//            }
//            i6 = (i5 * measuredHeight) / measuredWidth;
//        }
//        if (rotation == 90 || rotation == 270) {
//            i3 = i;
//            i4 = i2;
//        } else {
//            i4 = i;
//            i3 = i2;
//        }
//        int defaultSize = getDefaultSize(i5, i4);
//        int defaultSize2 = getDefaultSize(i6, i3);
//        if (i5 > 0 && i6 > 0) {
//            int mode = MeasureSpec.getMode(i4);
//            int size = MeasureSpec.getSize(i4);
//            int mode2 = MeasureSpec.getMode(i3);
//            int size2 = MeasureSpec.getSize(i3);
//            Log.i(TAG, "widthMeasureSpec  [" + MeasureSpec.toString(i4) + "]");
//            Log.i(TAG, "heightMeasureSpec [" + MeasureSpec.toString(i3) + "]");
//            int i7 = 0;
//            if (mode == 1073741824 && mode2 == 1073741824) {
//                int i8 = size2 * i5;
//                int i9 = size * i6;
//                if (i8 < i9) {
//                    int i10 = i8 / i6;
//                } else if (i8 > i9) {
//                    i7 = i9 / i5;
//                }
//            } else if (mode == 1073741824) {
//                int i11 = (size * i6) / i5;
//                if (mode2 == Integer.MIN_VALUE && i11 > size2) {
//                    int i12 = (size2 * i5) / i6;
//                }
//                i7 = i11;
//            } else if (mode2 == 1073741824) {
//                int i13 = (size2 * i5) / i6;
//                if (mode == Integer.MIN_VALUE && i13 > size) {
//                    i7 = (size * i6) / i5;
//                }
//            } else {
//                int i14 = (mode2 != Integer.MIN_VALUE || i6 <= size2) ? i5 : (size2 * i5) / i6;
//                if (mode == Integer.MIN_VALUE && i14 > size) {
//                    int i15 = (size * i6) / i5;
//                }
//            }
//            defaultSize = size;
//            defaultSize2 = i7;
//        }
//        if (!(measuredWidth == 0 || measuredHeight == 0 || i5 == 0 || i6 == 0)) {
//            if (Jzvd.VIDEO_IMAGE_DISPLAY_TYPE != 3 && Jzvd.VIDEO_IMAGE_DISPLAY_TYPE == 2) {
//                if (rotation == 90 || rotation == 270) {
//                    measuredWidth = measuredHeight;
//                }
//                double d = (double) i6;
//                double d2 = (double) i5;
//                Double.isNaN(d);
//                Double.isNaN(d2);
//                Double.isNaN(d);
//                Double.isNaN(d2);
//                double d3 = d / d2;
//                double d4 = (double) measuredHeight;
//                double d5 = (double) measuredWidth;
//                Double.isNaN(d4);
//                Double.isNaN(d5);
//                Double.isNaN(d4);
//                Double.isNaN(d5);
//                double d6 = d4 / d5;
//                if (d3 > d6) {
//                    double d7 = (double) defaultSize;
//                    Double.isNaN(d5);
//                    Double.isNaN(d7);
//                    Double.isNaN(d5);
//                    Double.isNaN(d7);
//                    double d8 = d5 / d7;
//                    double d9 = (double) defaultSize2;
//                    Double.isNaN(d9);
//                    Double.isNaN(d9);
//                    measuredHeight = (int) (d8 * d9);
//                    i5 = measuredWidth;
//                } else if (d3 < d6) {
//                    double d10 = (double) defaultSize2;
//                    Double.isNaN(d4);
//                    Double.isNaN(d10);
//                    Double.isNaN(d4);
//                    Double.isNaN(d10);
//                    double d11 = d4 / d10;
//                    double d12 = (double) defaultSize;
//                    Double.isNaN(d12);
//                    Double.isNaN(d12);
//                    i5 = (int) (d11 * d12);
//                }
//                setMeasuredDimension(i5, measuredHeight);
//            }
//            measuredHeight = i6;
//            setMeasuredDimension(i5, measuredHeight);
//        }
//        setMeasuredDimension(videoWidth, videoHeight);
    }
}
