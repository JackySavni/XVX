package com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.youtube.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.video.player.videoplayer.xvxvideoplayer.R;


/**
 * View class
 *
 * Draws a arc shape and provides a circle scaling animation.
 * Used by [VP_YouTubeOverlay][com.github.vkay94.dtpv.youtube.VP_YouTubeOverlay].
 */
public final class VP_CircleClipTapView extends View {

    private Paint vp_backgroundPaint;
    private Paint vp_circlePaint;

    private int vp_widthPx;
    private int vp_heightPx;

    // Background

    private Path vp_shapePath;
    private boolean vp_isLeft;

    // Circle

    private float vp_cX;
    private float vp_cY;

    private float vp_currentRadius;
    private int vp_minRadius;
    private int vp_maxRadius;

    // Animation

    private ValueAnimator vp_valueAnimator;
    private boolean vp_forceReset;

    private float vp_arcSize;

    Runnable vp_performAtEnd;

    public VP_CircleClipTapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        vp_backgroundPaint = new Paint();
        vp_circlePaint = new Paint();

        vp_widthPx = 0;
        vp_heightPx = 0;

        // Background

        vp_shapePath = new Path();
        vp_isLeft = true;

        vp_cX = 0f;
        vp_cY = 0f;

        vp_currentRadius = 0f;
        vp_minRadius = 0;
        vp_maxRadius = 0;

        vp_valueAnimator = null;
        vp_forceReset = false;

        vp_backgroundPaint.setStyle(Paint.Style.FILL);
        vp_backgroundPaint.setAntiAlias(true);
        vp_backgroundPaint.setColor(ContextCompat.getColor(context, R.color.dtpv_yt_background_circle_color));

        vp_circlePaint.setStyle(Paint.Style.FILL);
        vp_circlePaint.setAntiAlias(true);
        vp_circlePaint.setColor(ContextCompat.getColor(context, R.color.dtpv_yt_tap_circle_color));

        // Pre-configuations depending on device display metrics
        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        vp_widthPx = dm.widthPixels;
        vp_heightPx = dm.heightPixels;

        vp_minRadius = (int)(30f * dm.density);
        vp_maxRadius = (int)(400f * dm.density);

        vp_updatePathShape();

        vp_valueAnimator = vp_getCircleAnimator();

        vp_arcSize = 80f;

        vp_performAtEnd = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    /*
        Getter and setter
     */

    public final Runnable getVp_performAtEnd() {
        return vp_performAtEnd;
    }

    public final void setVp_performAtEnd(Runnable value) {
        vp_performAtEnd = value;
    }

    public final float getVp_arcSize() {
        return vp_arcSize;
    }

    public final void setVp_arcSize(float value) {
        vp_arcSize = value;
        vp_updatePathShape();
    }

    public final int vp_getCircleBackgroundColor() {
        return vp_backgroundPaint.getColor();
    }

    public final void vp_setCircleBackgroundColor(int value) {
        vp_backgroundPaint.setColor(value);
    }

    public final int vp_getCircleColor() {
        return vp_circlePaint.getColor();
    }

    public final void vp_setCircleColor(int value) {
        vp_circlePaint.setColor(value);
    }

    public final long vp_getAnimationDuration() {
        return vp_valueAnimator != null ? vp_valueAnimator.getDuration() : 650L;
    }

    public final void vp_setAnimationDuration(long value) {
        vp_getCircleAnimator().setDuration(value);
    }

    /*
       Methods
    */

    /*
        Circle
     */

    public final void vp_updatePosition(float x, float y) {
        vp_cX = x;
        vp_cY = y;

        boolean newIsLeft = x <= (float)(getResources().getDisplayMetrics().widthPixels / 2);
        if (vp_isLeft != newIsLeft) {
            vp_isLeft = newIsLeft;
            vp_updatePathShape();
        }
    }

    private final void vp_invalidateWithCurrentRadius(float factor) {
        vp_currentRadius = (float) vp_minRadius + (float)(vp_maxRadius - vp_minRadius) * factor;
        invalidate();
    }

    /*
        Background
     */

    private final void vp_updatePathShape() {
        float halfWidth = (float) vp_widthPx * 0.5f;

        vp_shapePath.reset();

        float w = vp_isLeft ? 0.0f : (float) vp_widthPx;
        int f = vp_isLeft ? 1 : -1;

        vp_shapePath.moveTo(w, 0.0F);
        vp_shapePath.lineTo((float)f * (halfWidth - vp_arcSize) + w, 0.0F);
        vp_shapePath.quadTo(
                (float)f * (halfWidth + vp_arcSize) + w,
                (float) vp_heightPx / (float)2,
                (float)f * (halfWidth - vp_arcSize) + w,
                (float) vp_heightPx
        );
        vp_shapePath.lineTo(w, (float) vp_heightPx);
        vp_shapePath.close();
        invalidate();
    }

    /*
        Animation
     */

    private final ValueAnimator vp_getCircleAnimator() {
        if (vp_valueAnimator == null) {
            vp_valueAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
            vp_valueAnimator.setDuration(vp_getAnimationDuration());

            vp_valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    vp_invalidateWithCurrentRadius((float)animation.getAnimatedValue());
                }
            });

            vp_valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!vp_forceReset)
                        vp_performAtEnd.run();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        return vp_valueAnimator;
    }

    public final void vp_resetAnimation(Runnable body) {
        vp_forceReset = true;
        vp_getCircleAnimator().end();
        body.run();
        vp_forceReset = false;
        vp_getCircleAnimator().start();
    }

    public final void endAnimation() {
        vp_getCircleAnimator().end();
    }

    /*
        Others: Drawing and Measurements
     */

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vp_widthPx = w;
        vp_heightPx = h;
        vp_updatePathShape();
    }

    protected void onDraw(Canvas vp_canvas) {
        super.onDraw(vp_canvas);

        // Background
        if (vp_canvas != null) {
            vp_canvas.clipPath(this.vp_shapePath);
        }
        if (vp_canvas != null) {
            vp_canvas.drawPath(this.vp_shapePath, this.vp_backgroundPaint);
        }

        // Circle
        if (vp_canvas != null) {
            vp_canvas.drawCircle(this.vp_cX, this.vp_cY, this.vp_currentRadius, this.vp_circlePaint);
        }
    }
}
