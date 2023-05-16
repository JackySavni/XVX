package com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.youtube;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SeekParameters;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.vid.VP_Player;
import com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.VP_DoubleTapPlayerViewVP;
import com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.VP_PlayerDoubleTapListener;
import com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.VP_SeekListener;
import com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.youtube.views.VP_CircleClipTapView;
import com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.youtube.views.VP_SecondsView;

/**
 * Overlay for [VP_DoubleTapPlayerViewVP] to create a similar UI/UX experience like the official
 * YouTube Android app.
 *
 * The overlay has the typical YouTube scaling circle animation and provides some configurations
 * which can't be accomplished with the regular Android Ripple (I didn't find any options in the
 * documentation ...).
 */
public final class VP_YouTubeOverlay extends ConstraintLayout implements VP_PlayerDoubleTapListener {

    private final AttributeSet vp_attrs;

    public VP_YouTubeOverlay(@NonNull Context context) {
        this(context, null);
        // Hide overlay initially when added programmatically
        setVisibility(View.INVISIBLE);
    }

    public VP_YouTubeOverlay(@NonNull Context context, @Nullable AttributeSet vp_attrs) {
        super(context, vp_attrs);
        this.vp_attrs = vp_attrs;
        vp_playerViewRef = -1;

        LayoutInflater.from(context).inflate(R.layout.vp_yt_overlay, this, true);

        // Initialize UI components
        vp_initializeAttributes();
        ((VP_SecondsView)findViewById(R.id.vp_seconds_view)).setVp_isForward(true);
        vp_changeConstraints(true);

        // This code snippet is executed when the circle scale animation is finished
        ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).setVp_performAtEnd(
                new Runnable() {
                    @Override
                    public void run() {
                        if (performListener != null)
                            performListener.onAnimationEnd();

                        VP_SecondsView VPSecondsView = findViewById(R.id.vp_seconds_view);
                        VPSecondsView.setVisibility(View.INVISIBLE);
                        VPSecondsView.setVp_seconds(0);
                        VPSecondsView.vp_stop();
                    }
                }
        );
    }

    private int vp_playerViewRef;

    // VP_Player behaviors
    private VP_DoubleTapPlayerViewVP vp_playerView;
    private ExoPlayer vp_player;

    /**
     * Sets all optional XML attributes and defaults
     */
    private void vp_initializeAttributes() {
        if (vp_attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(vp_attrs,
                    R.styleable.YouTubeOverlay, 0, 0);

            // PlayerView => see onAttachToWindow
            vp_playerViewRef = a.getResourceId(R.styleable.YouTubeOverlay_yt_playerView, -1);

            // Durations
            setVp_animationDuration((long)a.getInt(
                    R.styleable.YouTubeOverlay_yt_animationDuration, 650));

            seekSeconds = a.getInt(
                    R.styleable.YouTubeOverlay_yt_seekSeconds, 10);

            setVp_iconAnimationDuration((long)a.getInt(
                    R.styleable.YouTubeOverlay_yt_iconAnimationDuration, 750));

            // Arc size
            setVp_arcSize((float)a.getDimensionPixelSize(
                    R.styleable.YouTubeOverlay_yt_arcSize,
                    getContext().getResources().getDimensionPixelSize(R.dimen.dtpv_yt_arc_size))
            );

            // Colors
            vp_setTapCircleColor(a.getColor(
                    R.styleable.YouTubeOverlay_yt_tapCircleColor,
                    ContextCompat.getColor(getContext(), R.color.dtpv_yt_tap_circle_color))
            );

            vp_setCircleBackgroundColor(a.getColor(
                    R.styleable.YouTubeOverlay_yt_backgroundCircleColor,
                    ContextCompat.getColor(getContext(), R.color.dtpv_yt_background_circle_color))
            );

            // Seconds TextAppearance
            setVp_textAppearance(a.getResourceId(
                    R.styleable.YouTubeOverlay_yt_textAppearance,
                    R.style.YTOSecondsTextAppearance)
            );

            this.setVp_icon(a.getResourceId(
                    R.styleable.YouTubeOverlay_yt_icon,
                    R.drawable.vp_ic_play_triangle)
            );

            a.recycle();
        } else {
            // Set defaults
            setVp_arcSize((float)getContext().getResources().getDimensionPixelSize(R.dimen.dtpv_yt_arc_size));
            vp_setTapCircleColor(ContextCompat.getColor(getContext(), R.color.dtpv_yt_tap_circle_color));
            vp_setCircleBackgroundColor(ContextCompat.getColor(getContext(), R.color.dtpv_yt_background_circle_color));
            setVp_animationDuration(650L);
            setVp_iconAnimationDuration(750L);
            seekSeconds = 10;
            setVp_textAppearance(R.style.YTOSecondsTextAppearance);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // If the PlayerView is set by XML then call the corresponding setter method
        if (vp_playerViewRef != -1)
            playerView((VP_DoubleTapPlayerViewVP)((View)getParent()).findViewById(vp_playerViewRef));
    }

    /**
     * Obligatory call if playerView is not set via XML!
     *
     * Links the VP_DoubleTapPlayerViewVP to this view for recognizing the tapped position.
     *
     * @param playerView PlayerView which triggers the event
     */
    public VP_YouTubeOverlay playerView(VP_DoubleTapPlayerViewVP playerView) {
        this.vp_playerView = playerView;
        return this;
    }

    /**
     * Obligatory call! Needs to be called whenever the VP_Player changes.
     *
     * Performs seekTo-calls on the ExoPlayer's VP_Player instance.
     *
     * @param player PlayerView which triggers the event
     */
    public VP_YouTubeOverlay vp_player(ExoPlayer player) {
        this.vp_player = player;
        return this;
    }

        /*
        Properties
     */

    private VP_SeekListener VPSeekListener;

    /**
     * Optional: Sets a listener to observe whether double tap reached the start / end of the video
     */
    public VP_YouTubeOverlay seekListener(VP_SeekListener listener) {
        VPSeekListener = listener;
        return this;
    }

    private PerformListener performListener;

    /**
     * Sets a listener to execute some code before and after the animation
     * (for example UI changes (hide and show views etc.))
     */
    public VP_YouTubeOverlay performListener(PerformListener listener) {
        performListener = listener;
        return this;
    }

    /**
     * Forward / rewind duration on a tap in seconds.
     */
    private int seekSeconds;
    public final int vp_getSeekSeconds() {
        return seekSeconds;
    }

    public VP_YouTubeOverlay vp_seekSeconds(int seconds) {
        seekSeconds = seconds;
        return this;
    }

    /**
     * Color of the scaling circle on touch feedback.
     */
    public int vp_getTapCircleColor() {
        return ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_getCircleColor();
    }

    private void vp_setTapCircleColor(int value) {
        ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_setCircleColor(value);
    }

    public VP_YouTubeOverlay vp_tapCircleColorRes(@ColorRes int resId) {
        vp_setTapCircleColor(ContextCompat.getColor(getContext(), resId));
        return this;
    }

    public VP_YouTubeOverlay vp_tapCircleColorInt(@ColorInt int color) {
        vp_setTapCircleColor(color);
        return this;
    }

    /**
     * Color of the clipped background circle
     */
    public final int vp_getCircleBackgroundColor() {
        return ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_getCircleBackgroundColor();
    }

    private final void vp_setCircleBackgroundColor(int value) {
        ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_setCircleBackgroundColor(value);
    }

    public VP_YouTubeOverlay vp_circleBackgroundColorRes(@ColorRes int resId) {
        vp_setCircleBackgroundColor(ContextCompat.getColor(getContext(), resId));
        return this;
    }

    public VP_YouTubeOverlay vp_circleBackgroundColorInt(@ColorInt int color) {
        vp_setCircleBackgroundColor(color);
        return this;
    }

    /**
     * Duration of the circle scaling animation / speed in milliseconds.
     * The overlay keeps visible until the animation finishes.
     */
    private long vp_animationDuration;
    public final long getVp_animationDuration() {
        return ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_getAnimationDuration();
    }

    private void setVp_animationDuration(long value) {
        ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_setAnimationDuration(value);
    }

    public VP_YouTubeOverlay animationDuration(long duration) {
        setVp_animationDuration(duration);
        return this;
    }

    /**
     * Size of the arc which will be clipped from the background circle.
     * The greater the value the more roundish the shape becomes
     */
    private float vp_arcSize;
    public final float getVp_arcSize() {
        return ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).getVp_arcSize();
    }

    private void setVp_arcSize(float value) {
        ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).setVp_arcSize(value);
    }

    public VP_YouTubeOverlay vp_arcSize(@DimenRes int resId) {
        setVp_arcSize(getContext().getResources().getDimension(resId));
        return this;
    }

    public VP_YouTubeOverlay vp_arcSize(float px) {
        setVp_arcSize(px);
        return this;
    }

    /**
     * Duration the icon animation (fade in + fade out) for a full cycle in milliseconds.
     */
    private long vp_iconAnimationDuration = 750;
    public final long getVp_iconAnimationDuration() {
        return ((VP_SecondsView)findViewById(R.id.vp_seconds_view)).getVp_cycleDuration();
    }

    private void setVp_iconAnimationDuration(long value) {
        ((VP_SecondsView)findViewById(R.id.vp_seconds_view)).setVp_cycleDuration(value);
        vp_iconAnimationDuration = value;
    }

    public VP_YouTubeOverlay vp_iconAnimationDuration(long duration) {
        setVp_iconAnimationDuration(duration);
        return this;
    }

    /**
     * One of the three forward icons which will be animated above the seconds indicator.
     * The rewind icon will be the 180° mirrored version.
     *
     * Keep in mind that padding on the left and right of the drawable will be rendered which
     * could result in additional space between the three icons.
     */
    private int vp_icon;
    public final int getVp_icon() {
        return ((VP_SecondsView)findViewById(R.id.vp_seconds_view)).getVp_icon();
    }

    private void setVp_icon(int value) {
        ((VP_SecondsView)findViewById(R.id.vp_seconds_view)).setVp_icon(value);
        this.vp_icon = value;
    }

    public VP_YouTubeOverlay vp_icon(@DrawableRes int resId) {
        setVp_icon(resId);
        return this;
    }

    /**
     * Text appearance of the *xx seconds* text.
     */
    private int vp_textAppearance;
    public final int getVp_textAppearance() {
        return vp_textAppearance;
    }

    private final void setVp_textAppearance(int value) {
        TextViewCompat.setTextAppearance(((VP_SecondsView)findViewById(R.id.vp_seconds_view)).vp_getTextView(), value);
        vp_textAppearance = value;
    }

    public final VP_YouTubeOverlay vp_textAppearance(@StyleRes int resId) {
        setVp_textAppearance(resId);
        return this;
    }

    /**
     * TextView view for *xx seconds*.
     *
     * In case of you'd like to change some specific attributes of the TextView in runtime.
     */
    public final TextView vp_getSecondsTextView() {
        return ((VP_SecondsView)findViewById(R.id.vp_seconds_view)).vp_getTextView();
    }

    @Override
    public void onDoubleTapStarted(float posX, float posY) {

        if (VP_Player.vp_locked)
            return;

        if (vp_player != null && vp_player.getCurrentPosition() >= 0L && vp_playerView != null && vp_playerView.getWidth() > 0) {
            if (posX >= vp_playerView.getWidth() * 0.35 && posX <= vp_playerView.getWidth() * 0.65) {
                if (vp_player.isPlaying()) {
                    vp_player.pause();
                } else {
                    vp_player.play();
                    if (vp_playerView.isControllerFullyVisible())
                        vp_playerView.hideController();
                }
                return;
            }
        }

        //super.onDoubleTapStarted(posX, posY);
    }

    @Override
    public void onDoubleTapProgressUp(float posX, float posY) {

        if (VP_Player.vp_locked)
            return;

        // Check first whether forwarding/rewinding is "valid"
        if (vp_player == null || vp_player.getMediaItemCount() < 1 || vp_player.getCurrentPosition() < 0 || vp_playerView == null || vp_playerView.getWidth() < 0)
            return;

        long current = vp_player.getCurrentPosition();
        // Rewind and start of the video (+ 0.5 sec tolerance)
        if (posX < vp_playerView.getWidth() * 0.35 && current <= 500)
            return;

        // Forward and end of the video (- 0.5 sec tolerance)
        if (posX > vp_playerView.getWidth() * 0.65 && current >= (vp_player.getDuration() - 500))
            return;

        // YouTube behavior: show overlay on MOTION_UP
        // But check whether the first double tap is in invalid area
        if (getVisibility() != View.VISIBLE) {
            if (posX < vp_playerView.getWidth() * 0.35 || posX > vp_playerView.getWidth() * 0.65) {
                if (performListener != null)
                    performListener.onAnimationStart();
                VP_SecondsView VPSecondsView = findViewById(R.id.vp_seconds_view);
                VPSecondsView.setVisibility(View.VISIBLE);
                VPSecondsView.vp_start();
            } else
                return;
        }

        if (posX < vp_playerView.getWidth() * 0.35) {

            // First time tap or switched
            VP_SecondsView VPSecondsView = findViewById(R.id.vp_seconds_view);
            if (VPSecondsView.isVp_isForward()) {
                vp_changeConstraints(false);
                VPSecondsView.setVp_isForward(false);
                VPSecondsView.setVp_seconds(0);
            }

            // Cancel ripple and start new without triggering overlay disappearance
            // (resetting instead of ending)
            ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_resetAnimation(new Runnable() {
                @Override
                public void run() {
                    ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_updatePosition(posX, posY);
                }
            });
            rewinding();
        } else if (posX > vp_playerView.getWidth() * 0.65) {

            // First time tap or switched
            VP_SecondsView VPSecondsView = findViewById(R.id.vp_seconds_view);
            if (!VPSecondsView.isVp_isForward()) {
                vp_changeConstraints(true);
                VPSecondsView.setVp_isForward(true);
                VPSecondsView.setVp_seconds(0);
            }

            // Cancel ripple and start new without triggering overlay disappearance
            // (resetting instead of ending)
            ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_resetAnimation(new Runnable() {
                @Override
                public void run() {
                    ((VP_CircleClipTapView)findViewById(R.id.vp_circle_clip_tap_view)).vp_updatePosition(posX, posY);
                }
            });
            forwarding();
        } else {
            // Middle area tapped: do nothing
            //
            // playerView?.cancelInDoubleTapMode()
            // circle_clip_tap_view.endAnimation()
            // triangle_seconds_view.stop()
        }
    }

    /**
     * Seeks the video to desired position.
     * Calls interface functions when start reached ([VP_SeekListener.onVideoStartReached])
     * or when end reached ([VP_SeekListener.onVideoEndReached])
     *
     * @param newPosition desired position
     */
    private void seekToPosition(long newPosition) {
        if (vp_player == null || vp_playerView == null)
            return;

        vp_player.setSeekParameters(SeekParameters.EXACT);

        // Start of the video reached
        if (newPosition <= 0) {
            vp_player.seekTo(0);

            if (VPSeekListener != null)
                VPSeekListener.vp_onVideoStartReached();
            return;
        }

        // End of the video reached
        long total = vp_player.getDuration();
        if (newPosition >= total) {
            vp_player.seekTo(total);

            if (VPSeekListener != null)
                VPSeekListener.vp_onVideoEndReached();
            return;
        }

        // Otherwise
        vp_playerView.keepInDoubleTapMode();
        vp_player.seekTo(newPosition);
    }

    private void forwarding() {
        VP_SecondsView VPSecondsView = findViewById(R.id.vp_seconds_view);
        VPSecondsView.setVp_seconds(VPSecondsView.getVp_seconds() + seekSeconds);
        seekToPosition(vp_player != null ? vp_player.getCurrentPosition() + (long)(this.seekSeconds * 1000) : null);
    }

    private void rewinding() {
        VP_SecondsView VPSecondsView = findViewById(R.id.vp_seconds_view);
        VPSecondsView.setVp_seconds(VPSecondsView.getVp_seconds() + seekSeconds);
        seekToPosition(vp_player != null ? vp_player.getCurrentPosition() - (long)(this.seekSeconds * 1000) : null);
    }

    private void vp_changeConstraints(boolean forward) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone((ConstraintLayout)findViewById(R.id.root_constraint_layout));
        VP_SecondsView VPSecondsView = findViewById(R.id.vp_seconds_view);
        if (forward) {
            constraintSet.clear(VPSecondsView.getId(), ConstraintSet.START);
            constraintSet.connect(VPSecondsView.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            constraintSet.clear(VPSecondsView.getId(), ConstraintSet.END);
            constraintSet.connect(VPSecondsView.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
        }
        //VPSecondsView.start();
        constraintSet.applyTo((ConstraintLayout)findViewById(R.id.root_constraint_layout));
    }

    public interface PerformListener {
        void onAnimationStart();

        void onAnimationEnd();
    }
}
