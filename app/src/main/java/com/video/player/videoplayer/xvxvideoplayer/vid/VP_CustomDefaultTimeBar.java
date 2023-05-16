package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ui.DefaultTimeBar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VP_CustomDefaultTimeBar extends DefaultTimeBar {
    Rect vp_scrubberBar;
    private boolean vp_scrubbing;
    private int vp_scrubbingStartX;

    public VP_CustomDefaultTimeBar(Context context) {
        this(context, null);
    }

    public VP_CustomDefaultTimeBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VP_CustomDefaultTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, attrs);
    }

    public VP_CustomDefaultTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @Nullable AttributeSet timebarAttrs) {
        this(context, attrs, defStyleAttr, timebarAttrs, 0);
    }

    public VP_CustomDefaultTimeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, @Nullable AttributeSet timebarAttrs, int defStyleRes) {
        super(context, attrs, defStyleAttr, timebarAttrs, defStyleRes);
        try {
            Field field = DefaultTimeBar.class.getDeclaredField("scrubberBar");
            field.setAccessible(true);
            vp_scrubberBar = (Rect) field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && vp_scrubberBar != null) {
            vp_scrubbing = false;
            vp_scrubbingStartX = (int)event.getX();
            final int distanceFromScrubber = Math.abs(vp_scrubberBar.right - vp_scrubbingStartX);
            if (distanceFromScrubber > VP_Utils.vp_dpToPx(24))
                return true;
            else
                vp_scrubbing = true;
        }
        if (!vp_scrubbing && event.getAction() == MotionEvent.ACTION_MOVE && vp_scrubberBar != null) {
            final int distanceFromStart = Math.abs(((int)event.getX()) - vp_scrubbingStartX);
            if (distanceFromStart > VP_Utils.vp_dpToPx(6)) {
                vp_scrubbing = true;
                try {
                    final Method method = DefaultTimeBar.class.getDeclaredMethod("startScrubbing", long.class);
                    method.setAccessible(true);
                    method.invoke(this, (long) 0);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                return true;
            }
        }
        return super.onTouchEvent(event);
    }
}
