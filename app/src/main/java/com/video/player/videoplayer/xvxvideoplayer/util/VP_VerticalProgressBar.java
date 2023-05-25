package com.video.player.videoplayer.xvxvideoplayer.util;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ProgressBar;

public class VP_VerticalProgressBar extends ProgressBar {
    private int vp_w;
    private int vp_x;
    private int vp_y;
    private int vp_z;

    public void drawableStateChanged() {
        super.drawableStateChanged();
    }

    public VP_VerticalProgressBar(Context context) {
        super(context);
    }

    public VP_VerticalProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public VP_VerticalProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i2, i, i4, i3);
        this.vp_x = i;
        this.vp_y = i2;
        this.vp_z = i3;
        this.vp_w = i4;
    }

    public synchronized void onMeasure(int i, int i2) {
        super.onMeasure(i2, i);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    public void onDraw(Canvas canvas) {
        canvas.rotate(-90.0f);
        canvas.translate((float) (-getHeight()), 0.0f);
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            setSelected(true);
            setPressed(true);
        } else if (action == 1) {
            setSelected(false);
            setPressed(false);
        } else if (action == 2) {
            setProgress(getMax() - ((int) ((((float) getMax()) * motionEvent.getY()) / ((float) getHeight()))));
            onSizeChanged(getWidth(), getHeight(), 0, 0);
        }
        return true;
    }

    public synchronized void setProgress(int i) {
        if (i >= 0) {
            super.setProgress(i);
        } else {
            super.setProgress(0);
        }
        onSizeChanged(this.vp_x, this.vp_y, this.vp_z, this.vp_w);
    }
}
