package com.video.player.videoplayer.xvxvideoplayer.vid.dtpv;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.vid.VP_CustomStyledPlayerView;


/**
 * Custom player class for Double-Tapping listening
 */
public class VP_DoubleTapPlayerViewVP extends VP_CustomStyledPlayerView {
    private final GestureDetectorCompat vp_gestureDetector;
    private final vp_DoubleTapGestureListener vp_gestureListener;

    private VP_PlayerDoubleTapListener vp_controller;

    private final VP_PlayerDoubleTapListener getVp_controller() {
        return vp_gestureListener.getVp_controls();
    }

    private final void setVp_controller(VP_PlayerDoubleTapListener value) {
        vp_gestureListener.setVp_controls(value);
        vp_controller = value;
    }

    private int vp_controllerRef;

    public VP_DoubleTapPlayerViewVP(Context context) {
        this(context, null);
    }

    public VP_DoubleTapPlayerViewVP(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VP_DoubleTapPlayerViewVP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        vp_controllerRef = -1;

        vp_gestureListener = new vp_DoubleTapGestureListener(this);
        vp_gestureDetector = new GestureDetectorCompat(context, vp_gestureListener);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DoubleTapPlayerView, 0, 0);
            vp_controllerRef = a != null ? a.getResourceId(R.styleable.DoubleTapPlayerView_dtpv_controller, -1) : -1;
            if (a != null) {
                a.recycle();
            }
        }

        vp_isDoubleTapEnabled = true;
        vp_doubleTapDelay = 700L;
    }

    /**
     * If this field is set to `true` this view will handle double tapping, otherwise it will
     * handle touches the same way as the original [PlayerView][com.google.android.exoplayer2.ui.PlayerView] does
     */
    private boolean vp_isDoubleTapEnabled;

    public final boolean isVp_isDoubleTapEnabled() {
        return vp_isDoubleTapEnabled;
    }

    public final void setVp_isDoubleTapEnabled(boolean var1) {
        vp_isDoubleTapEnabled = var1;
    }

    /**
     * Time window a double tap is active, so a followed tap is calling a gesture detector
     * method instead of normal tap (see [PlayerView.onTouchEvent])
     */
    private long vp_doubleTapDelay;

    public final long getVp_doubleTapDelay() {
        return vp_gestureListener.getVp_doubleTapDelay();
    }

    public final void setVp_doubleTapDelay(long value) {
        vp_gestureListener.setVp_doubleTapDelay(value);
        vp_doubleTapDelay = value;
    }

    /**
     * Sets the [VP_PlayerDoubleTapListener] which handles the gesture callbacks.
     *
     * Primarily used for [VP_YouTubeOverlay][com.github.vkay94.dtpv.youtube.VP_YouTubeOverlay]
     */
    public final VP_DoubleTapPlayerViewVP controller(VP_PlayerDoubleTapListener controller) {
        setVp_controller(controller);
        return this;
    }

    /**
     * Returns the current state of double tapping.
     */
    public final boolean isInDoubleTapMode() {
        return vp_gestureListener.isVp_isDoubleTapping();
    }

    /**
     * Resets the timeout to keep in double tap mode.
     *
     * Called once in [VP_PlayerDoubleTapListener.onDoubleTapStarted]. Needs to be called
     * from outside if the double tap is customized / overridden to detect ongoing taps
     */
    public final void keepInDoubleTapMode() {
        vp_gestureListener.vp_keepInDoubleTapMode();
    }

    /**
     * Cancels double tap mode instantly by calling [VP_PlayerDoubleTapListener.onDoubleTapFinished]
     */
    public final void cancelInDoubleTapMode() {
        vp_gestureListener.vp_cancelInDoubleTapMode();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (vp_isDoubleTapEnabled) {
            boolean vp_consumed = vp_gestureDetector.onTouchEvent(ev);

            // Do not trigger original behavior when double tapping
            // otherwise the controller would show/hide - it would flack
            if (!vp_consumed)
                return super.onTouchEvent(ev);
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // If the PlayerView is set by XML then call the corresponding setter method
        if (vp_controllerRef != -1) {
            try {
                View view = ((View)getParent()).findViewById(this.vp_controllerRef);
                if (view instanceof VP_PlayerDoubleTapListener) {
                    controller((VP_PlayerDoubleTapListener)view);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("VP_DoubleTapPlayerViewVP","controllerRef is either invalid or not VP_PlayerDoubleTapListener: ${e.message}");
            }
        }
    }

    /**
     * Gesture Listener for double tapping
     *
     * For more information which methods are called in certain situations look for
     * [GestureDetector.onTouchEvent][android.view.GestureDetector.onTouchEvent],
     * especially for ACTION_DOWN and ACTION_UP
     */
    private static final class vp_DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        private final Handler vp_mHandler;
        private final Runnable vp_mRunnable;

        private VP_PlayerDoubleTapListener vp_controls;
        private boolean vp_isDoubleTapping;
        private long vp_doubleTapDelay;
        public final boolean isVp_isDoubleTapping() {
            return vp_isDoubleTapping;
        }

        public final void setVp_isDoubleTapping(boolean var1) {
            vp_isDoubleTapping = var1;
        }

        public final long getVp_doubleTapDelay() {
            return vp_doubleTapDelay;
        }

        public final void setVp_doubleTapDelay(long var1) {
            vp_doubleTapDelay = var1;
        }

        private final VP_CustomStyledPlayerView rootView;

        public final VP_PlayerDoubleTapListener getVp_controls() {
            return vp_controls;
        }

        public final void setVp_controls(VP_PlayerDoubleTapListener var1) {
            vp_controls = var1;
        }

        private static final String TAG = ".DTGListener";
        private static boolean DEBUG = false;

        /**
         * Resets the timeout to keep in double tap mode.
         *
         * Called once in [VP_PlayerDoubleTapListener.onDoubleTapStarted]. Needs to be called
         * from outside if the double tap is customized / overridden to detect ongoing taps
         */
        public final void vp_keepInDoubleTapMode() {
            vp_isDoubleTapping = true;
            vp_mHandler.removeCallbacks(vp_mRunnable);
            vp_mHandler.postDelayed(vp_mRunnable, vp_doubleTapDelay);
        }

        /**
         * Cancels double tap mode instantly by calling [VP_PlayerDoubleTapListener.onDoubleTapFinished]
         */
        public final void vp_cancelInDoubleTapMode() {
            vp_mHandler.removeCallbacks(vp_mRunnable);
            vp_isDoubleTapping = false;
            if (vp_controls != null)
                vp_controls.onDoubleTapFinished();
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // Used to override the other methods
            if (vp_isDoubleTapping) {
                if (vp_controls != null)
                    vp_controls.onDoubleTapProgressDown(e.getX(), e.getY());
                return true;
            }
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (vp_isDoubleTapping) {
                if (DEBUG)
                    Log.d(TAG, "onSingleTapUp: isDoubleTapping = true");
                if (vp_controls != null)
                    vp_controls.onDoubleTapProgressUp(e.getX(), e.getY());
                return true;
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Ignore this event if double tapping is still active
            // Return true needed because this method is also called if you tap e.g. three times
            // in a row, therefore the controller would appear since the original behavior is
            // to hide and show on single tap
            if (vp_isDoubleTapping)
                return true;
            if (DEBUG)
                Log.d(TAG, "onSingleTapConfirmed: isDoubleTap = false");
            //return rootView.performClick()
            return rootView.vp_tap();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // First tap (ACTION_DOWN) of both taps
            if (DEBUG)
                Log.d(TAG, "onDoubleTap");
            if (!vp_isDoubleTapping) {
                vp_isDoubleTapping = true;
                vp_keepInDoubleTapMode();
                if (vp_controls != null)
                    vp_controls.onDoubleTapStarted(e.getX(), e.getY());
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // Second tap (ACTION_UP) of both taps
            if (e.getActionMasked() == MotionEvent.ACTION_UP && vp_isDoubleTapping) {
                if (DEBUG)
                    Log.d(TAG,"onDoubleTapEvent, ACTION_UP");
                if (vp_controls != null)
                    vp_controls.onDoubleTapProgressUp(e.getX(), e.getY());
                return true;
            }
            return super.onDoubleTapEvent(e);
        }

        public vp_DoubleTapGestureListener(VP_CustomStyledPlayerView vp_rootView) {
            super();
            this.rootView = vp_rootView;
            vp_mHandler = new Handler();
            vp_mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (DEBUG)
                        Log.d(TAG, "Runnable called");
                    setVp_isDoubleTapping(false);
                    vp_DoubleTapGestureListener.this.setVp_isDoubleTapping(false);
                    if (getVp_controls() != null)
                        getVp_controls().onDoubleTapFinished();
                }
            };
            vp_doubleTapDelay = 650L;
        }
    }

}
