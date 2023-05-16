package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.view.GestureDetectorCompat;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.video.player.videoplayer.xvxvideoplayer.R;

import java.util.Collections;

public class VP_CustomStyledPlayerView extends StyledPlayerView implements GestureDetector.OnGestureListener, ScaleGestureDetector.OnScaleGestureListener {

    private final GestureDetectorCompat vp_mDetector;

    private Orientation vp_gestureOrientation = Orientation.UNKNOWN;
    private float vp_gestureScrollY = 0f;
    private float vp_gestureScrollX = 0f;
    private boolean vp_handleTouch;
    private long vp_seekStart;
    private long vp_seekChange;
    private long vp_seekMax;
    private long vp_seekLastPosition;
    public boolean vp_seekProgress;
    private boolean vp_canBoostVolume = false;
    private boolean vp_canSetAutoBrightness = false;

    private final float VP_IGNORE_BORDER = VP_Utils.vp_dpToPx(24);
    private final float VP_SCROLL_STEP = VP_Utils.vp_dpToPx(16);
    private final float VP_SCROLL_STEP_SEEK = VP_Utils.vp_dpToPx(8);
    @SuppressWarnings("FieldCanBeLocal")
    private final long VP_SEEK_STEP = 1000;
    public static final int VP_MESSAGE_TIMEOUT_TOUCH = 400;
    public static final int VP_MESSAGE_TIMEOUT_KEY = 800;
    public static final int VP_MESSAGE_TIMEOUT_LONG = 1400;

    private boolean vp_restorePlayState;
    private boolean vp_canScale = true;
    private boolean vp_isHandledLongPress = false;
    public long vp_keySeekStart = -1;
    public int vp_volumeUpsInRow = 0;

    private final ScaleGestureDetector vp_mScaleDetector;
    private float vp_mScaleFactor = 1.f;
    private float vp_mScaleFactorFit;
    Rect vp_systemGestureExclusionRect = new Rect();

    public final Runnable vp_textClearRunnable = () -> {
        setCustomErrorMessage(null);
        vp_clearIcon();
        vp_keySeekStart = -1;
    };

    private final AudioManager vp_mAudioManager;
    private VP_BrightnessControl VPBrightnessControl;

    private final TextView vp_exoErrorMessage;
    private final View vp_exoProgress;

    public VP_CustomStyledPlayerView(Context context) {
        this(context, null);
    }

    public VP_CustomStyledPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VP_CustomStyledPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        vp_mDetector = new GestureDetectorCompat(context,this);

        vp_mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        vp_exoErrorMessage = findViewById(R.id.exo_error_message);
        vp_exoProgress = findViewById(R.id.exo_progress);

        vp_mScaleDetector = new ScaleGestureDetector(context, this);

        if (!VP_Utils.vp_isTvBox(getContext())) {
            vp_exoErrorMessage.setOnClickListener(v -> {
                if (VP_Player.vp_locked) {
                    VP_Player.vp_locked = false;
                    VP_Utils.vp_showText(VP_CustomStyledPlayerView.this, "", VP_MESSAGE_TIMEOUT_LONG);
                    vp_setIconLock(false);
                }
            });
        }
    }

    public void vp_clearIcon() {
        vp_exoErrorMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        vp_setHighlight(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (VP_Player.vp_restoreControllerTimeout) {
            setControllerShowTimeoutMs(VP_Player.VP_CONTROLLER_TIMEOUT);
            VP_Player.vp_restoreControllerTimeout = false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && vp_gestureOrientation == Orientation.UNKNOWN)
            vp_mScaleDetector.onTouchEvent(ev);

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (VP_Player.vp_snackbar != null && VP_Player.vp_snackbar.isShown()) {
                    VP_Player.vp_snackbar.dismiss();
                    vp_handleTouch = false;
                } else {
                    removeCallbacks(vp_textClearRunnable);
                    vp_handleTouch = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (vp_handleTouch) {
                    if (vp_gestureOrientation == Orientation.HORIZONTAL) {
                        setCustomErrorMessage(null);
                    } else {
                        postDelayed(vp_textClearRunnable, vp_isHandledLongPress ? VP_MESSAGE_TIMEOUT_LONG : VP_MESSAGE_TIMEOUT_TOUCH);
                    }

                    if (vp_restorePlayState) {
                        vp_restorePlayState = false;
                        VP_Player.vp_player.play();
                    }

                    setControllerAutoShow(true);

                    if (vp_seekProgress) {
                        vp_seekProgress = false;
                        hideControllerImmediately();
                    }
                    break;
                }
        }

        if (vp_handleTouch)
            vp_mDetector.onTouchEvent(ev);

        // Handle all events to avoid conflict with internal handlers
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        vp_gestureScrollY = 0;
        vp_gestureScrollX = 0;
        vp_gestureOrientation = Orientation.UNKNOWN;
        vp_isHandledLongPress = false;

        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }



    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    public boolean vp_tap() {
        if (VP_Player.vp_locked) {
            VP_Utils.vp_showText(this, "", VP_MESSAGE_TIMEOUT_LONG);
            vp_setIconLock(true);
            return true;
        }

        if (!VP_Player.vp_controllerVisibleFully) {
            showController();
            return true;
        } else if (VP_Player.vp_haveMedia && VP_Player.vp_player != null && VP_Player.vp_player.isPlaying()) {
            hideController();
            return true;
        }
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
        if (vp_mScaleDetector.isInProgress() || VP_Player.vp_player == null || VP_Player.vp_locked)
            return false;

        // Exclude edge areas
        if (motionEvent.getY() < VP_IGNORE_BORDER || motionEvent.getX() < VP_IGNORE_BORDER ||
                motionEvent.getY() > getHeight() - VP_IGNORE_BORDER || motionEvent.getX() > getWidth() - VP_IGNORE_BORDER)
            return false;

        if (vp_gestureScrollY == 0 || vp_gestureScrollX == 0) {
            vp_gestureScrollY = 0.0001f;
            vp_gestureScrollX = 0.0001f;
            return false;
        }

        if (vp_gestureOrientation == Orientation.HORIZONTAL || vp_gestureOrientation == Orientation.UNKNOWN) {
            vp_gestureScrollX += distanceX;
            if (Math.abs(vp_gestureScrollX) > VP_SCROLL_STEP || (vp_gestureOrientation == Orientation.HORIZONTAL && Math.abs(vp_gestureScrollX) > VP_SCROLL_STEP_SEEK)) {
                // Do not show controller if not already visible
                setControllerAutoShow(false);

                if (vp_gestureOrientation == Orientation.UNKNOWN) {
                    if (VP_Player.vp_player.isPlaying()) {
                        vp_restorePlayState = true;
                        VP_Player.vp_player.pause();
                    }
                    vp_clearIcon();
                    vp_seekLastPosition = vp_seekStart = VP_Player.vp_player.getCurrentPosition();
                    vp_seekChange = 0L;
                    vp_seekMax = VP_Player.vp_player.getDuration();

                    if (!isControllerFullyVisible()) {
                        vp_seekProgress = true;
                        showProgress();
                    }
                }

                vp_gestureOrientation = Orientation.HORIZONTAL;
                long position = 0;
                float distanceDiff = Math.max(0.5f, Math.min(Math.abs(VP_Utils.vp_pxToDp(distanceX) / 4), 10.f));

                if (VP_Player.vp_haveMedia) {
                    if (vp_gestureScrollX > 0) {
                        if (vp_seekStart + vp_seekChange - VP_SEEK_STEP * distanceDiff >= 0) {
                            VP_Player.vp_player.setSeekParameters(SeekParameters.PREVIOUS_SYNC);
                            vp_seekChange -= VP_SEEK_STEP * distanceDiff;
                            position = vp_seekStart + vp_seekChange;
                            VP_Player.vp_player.seekTo(position);
                        }
                    } else {
                        VP_Player.vp_player.setSeekParameters(SeekParameters.NEXT_SYNC);
                        if (vp_seekMax == C.TIME_UNSET) {
                            vp_seekChange += VP_SEEK_STEP * distanceDiff;
                            position = vp_seekStart + vp_seekChange;
                            VP_Player.vp_player.seekTo(position);
                        } else if (vp_seekStart + vp_seekChange + VP_SEEK_STEP < vp_seekMax) {
                            vp_seekChange += VP_SEEK_STEP * distanceDiff;
                            position = vp_seekStart + vp_seekChange;
                            VP_Player.vp_player.seekTo(position);
                        }
                    }
                    for (long start : VP_Player.vp_chapterStarts) {
                        if ((vp_seekLastPosition < start && position >= start) || (vp_seekLastPosition > start && position <= start)) {
                            performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
                        }
                    }
                    vp_seekLastPosition = position;
                    String message = VP_Utils.vp_formatMilisSign(vp_seekChange);
                    if (!isControllerFullyVisible()) {
                        message += "\n" + VP_Utils.vp_formatMilis(position);
                    }
                    setCustomErrorMessage(message);
                    vp_gestureScrollX = 0.0001f;
                }
            }
        }

        // LEFT = Brightness  |  RIGHT = Volume
        if (vp_gestureOrientation == Orientation.VERTICAL || vp_gestureOrientation == Orientation.UNKNOWN) {
            vp_gestureScrollY += distanceY;
            if (Math.abs(vp_gestureScrollY) > VP_SCROLL_STEP) {
                if (vp_gestureOrientation == Orientation.UNKNOWN) {
                    vp_canBoostVolume = VP_Utils.vp_isVolumeMax(vp_mAudioManager);
                    vp_canSetAutoBrightness = VPBrightnessControl.vp_currentBrightnessLevel <= 0;
                }
                vp_gestureOrientation = Orientation.VERTICAL;

                if (motionEvent.getX() < (float)(getWidth() / 2)) {
                    VPBrightnessControl.vp_changeBrightness(this, vp_gestureScrollY > 0, vp_canSetAutoBrightness);
                } else {
                    VP_Utils.vp_adjustVolume(getContext(), vp_mAudioManager, this, vp_gestureScrollY > 0, vp_canBoostVolume, false);
                }

                vp_gestureScrollY = 0.0001f;
            }
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        if (VP_Player.vp_locked || (getPlayer() != null && getPlayer().isPlaying())) {
            VP_Player.vp_locked = !VP_Player.vp_locked;
            vp_isHandledLongPress = true;
            VP_Utils.vp_showText(this, "", VP_MESSAGE_TIMEOUT_LONG);
            vp_setIconLock(VP_Player.vp_locked);

            if (VP_Player.vp_locked && VP_Player.vp_controllerVisible) {
                hideController();
            }
        }
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        if (VP_Player.vp_locked)
            return false;

        if (vp_canScale) {
            final float vp_factor = scaleGestureDetector.getScaleFactor();
            vp_mScaleFactor *= vp_factor + (1 - vp_factor) / 3 * 2;
            vp_mScaleFactor = VP_Utils.vp_normalizeScaleFactor(vp_mScaleFactor, vp_mScaleFactorFit);
            vp_setScale(vp_mScaleFactor);
            vp_restoreSurfaceView();
            vp_clearIcon();
            setCustomErrorMessage((int)(vp_mScaleFactor * 100) + "%");
            return true;
        }
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector vp_scaleGestureDetector) {
        if (VP_Player.vp_locked)
            return false;

        vp_mScaleFactor = getVideoSurfaceView().getScaleX();
        if (getResizeMode() != AspectRatioFrameLayout.RESIZE_MODE_ZOOM) {
            vp_canScale = false;
            setAspectRatioListener((targetAspectRatio, naturalAspectRatio, aspectRatioMismatch) -> {
                setAspectRatioListener(null);
                vp_mScaleFactor = vp_mScaleFactorFit = vp_getScaleFit();
                vp_canScale = true;
            });
            getVideoSurfaceView().setAlpha(0);
            setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        } else {
            vp_mScaleFactorFit = vp_getScaleFit();
            vp_canScale = true;
        }
        ImageButton vp_buttonAspectRatio = findViewById(Integer.MAX_VALUE - 100);
        vp_buttonAspectRatio.setImageResource(R.drawable.vp_ic_fit_screen_24dp);
        hideController();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector vp_scaleGestureDetector) {
        if (VP_Player.vp_locked)
            return;
        if (vp_mScaleFactor - vp_mScaleFactorFit < 0.001) {
            vp_setScale(1.f);
            setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);

            ImageButton buttonAspectRatio = findViewById(Integer.MAX_VALUE - 100);
            buttonAspectRatio.setImageResource(R.drawable.vp_ic_aspect_ratio_24dp);
        }
        if (VP_Player.vp_player != null && !VP_Player.vp_player.isPlaying()) {
            showController();
        }
        vp_restoreSurfaceView();
    }

    private void vp_restoreSurfaceView() {
        if (getVideoSurfaceView().getAlpha() != 1) {
            getVideoSurfaceView().setAlpha(1);
        }
    }

    public float vp_getScaleFit() {
        return Math.min((float)getHeight() / (float)getVideoSurfaceView().getHeight(),
                (float)getWidth() / (float)getVideoSurfaceView().getWidth());
    }

    private enum Orientation {
        HORIZONTAL, VERTICAL, UNKNOWN
    }

    public void vp_setIconVolume(boolean volumeActive) {
        vp_exoErrorMessage.setCompoundDrawablesWithIntrinsicBounds(volumeActive ? R.drawable.vp_ic_volume_up_24dp : R.drawable.vp_ic_volume_off_24dp, 0, 0, 0);
    }

    public void vp_setHighlight(boolean active) {
        if (active)
            vp_exoErrorMessage.getBackground().setTint(Color.RED);
        else
            vp_exoErrorMessage.getBackground().setTintList(null);
    }

    public void vp_setIconBrightness() {
        vp_exoErrorMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vp_ic_brightness_medium_24, 0, 0, 0);
    }

    public void vp_setIconBrightnessAuto() {
        vp_exoErrorMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vp_ic_brightness_auto_24dp, 0, 0, 0);
    }

    public void vp_setIconLock(boolean locked) {
        vp_exoErrorMessage.setCompoundDrawablesWithIntrinsicBounds(locked ? R.drawable.vp_ic_lock_24dp : R.drawable.vp_ic_lock_open_24dp, 0, 0, 0);
    }

    public void vp_setScale(final float scale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final View videoSurfaceView = getVideoSurfaceView();
            try {
                videoSurfaceView.setScaleX(scale);
                videoSurfaceView.setScaleY(scale);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            //videoSurfaceView.animate().setStartDelay(0).setDuration(0).scaleX(scale).scaleY(scale).start();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (Build.VERSION.SDK_INT >= 29) {
            vp_exoProgress.getGlobalVisibleRect(vp_systemGestureExclusionRect);
            vp_systemGestureExclusionRect.left = left;
            vp_systemGestureExclusionRect.right = right;
            setSystemGestureExclusionRects(Collections.singletonList(vp_systemGestureExclusionRect));
        }
    }

    public void vp_setBrightnessControl(VP_BrightnessControl VPBrightnessControl) {
        this.VPBrightnessControl = VPBrightnessControl;
    }
}