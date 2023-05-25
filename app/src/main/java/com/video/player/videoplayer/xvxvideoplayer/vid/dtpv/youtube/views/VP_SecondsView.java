package com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.youtube.views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Consumer;

import com.video.player.videoplayer.xvxvideoplayer.R;

/**
 * Layout group which handles the icon animation while forwarding and rewinding.
 *
 * Since it's based on view's alpha the fading effect is more fluid (more YouTube-like) than
 * using static drawables, especially when [cycleDuration] is low.
 *
 * Used by [VP_YouTubeOverlay][com.github.vkay94.dtpv.youtube.VP_YouTubeOverlay].
 */
public final class VP_SecondsView extends ConstraintLayout {

    private long vp_cycleDuration;
    private int vp_seconds;
    private boolean vp_isForward;
    private int vp_icon;
    private boolean vp_animate;

    private final ValueAnimator vp_firstAnimator;
    private final ValueAnimator vp_secondAnimator;
    private final ValueAnimator vp_thirdAnimator;
    private final ValueAnimator vp_fourthAnimator;
    private final ValueAnimator vp_fifthAnimator;

    public VP_SecondsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        vp_cycleDuration = 750L;
        vp_seconds = 0;
        vp_isForward = true;
        vp_icon = R.drawable.vp_ic_play_triangle;
        vp_animate = false;

        LayoutInflater.from(context).inflate(R.layout.vp_yt_seconds_view, this, true);

        vp_firstAnimator = new vp_CustomValueAnimator(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.vp_icon_1).setAlpha(0f);
                findViewById(R.id.vp_icon_2).setAlpha(0f);
                findViewById(R.id.vp_icon_3).setAlpha(0f);
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) {
                findViewById(R.id.vp_icon_1).setAlpha(aFloat);
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (vp_animate)
                    vp_secondAnimator.start();
            }
        });

        vp_secondAnimator = new vp_CustomValueAnimator(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.vp_icon_1).setAlpha(1f);
                findViewById(R.id.vp_icon_2).setAlpha(0f);
                findViewById(R.id.vp_icon_3).setAlpha(0f);
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) {
                findViewById(R.id.vp_icon_2).setAlpha(aFloat);
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (vp_animate)
                    vp_thirdAnimator.start();
            }
        });

        vp_thirdAnimator = new vp_CustomValueAnimator(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.vp_icon_1).setAlpha(1f);
                findViewById(R.id.vp_icon_2).setAlpha(1f);
                findViewById(R.id.vp_icon_3).setAlpha(0f);
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) {
                findViewById(R.id.vp_icon_1).setAlpha(1f - findViewById(R.id.vp_icon_3).getAlpha());
                findViewById(R.id.vp_icon_3).setAlpha(aFloat);
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (vp_animate)
                    vp_fourthAnimator.start();
            }
        });

        vp_fourthAnimator = new vp_CustomValueAnimator(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.vp_icon_1).setAlpha(0f);
                findViewById(R.id.vp_icon_2).setAlpha(1f);
                findViewById(R.id.vp_icon_3).setAlpha(1f);
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) {
                findViewById(R.id.vp_icon_2).setAlpha(1f - aFloat);
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (vp_animate)
                    vp_fifthAnimator.start();
            }
        });

        vp_fifthAnimator = new vp_CustomValueAnimator(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.vp_icon_1).setAlpha(0f);
                findViewById(R.id.vp_icon_2).setAlpha(0f);
                findViewById(R.id.vp_icon_3).setAlpha(1f);
            }
        }, new Consumer<Float>() {
            @Override
            public void accept(Float aFloat) {
                findViewById(R.id.vp_icon_3).setAlpha(1f - aFloat);
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (vp_animate)
                    vp_firstAnimator.start();
            }
        });
    }

    /**
     * Defines the duration for a full cycle of the triangle animation.
     * Each animation step takes 20% of it.
     */

    public final long getVp_cycleDuration() {
        return vp_cycleDuration;
    }

    public final void setVp_cycleDuration(long value) {
        vp_firstAnimator.setDuration(value / (long)5);
        vp_secondAnimator.setDuration(value / (long)5);
        vp_thirdAnimator.setDuration(value / (long)5);
        vp_fourthAnimator.setDuration(value / (long)5);
        vp_fifthAnimator.setDuration(value / (long)5);
        vp_cycleDuration = value;
    }

    /**
     * Sets the `TextView`'s seconds text according to the device`s language.
     */

    public final int getVp_seconds() {
        return vp_seconds;
    }

    public final void setVp_seconds(int value) {
        TextView textView = findViewById(R.id.vp_tv_seconds);
        textView.setText(getContext().getResources().getQuantityString(
                R.plurals.quick_seek_x_second, value, value
        ));
        vp_seconds = value;
    }

    /**
     * Mirrors the triangles depending on what kind of type should be used (forward/rewind).
     */

    public final boolean isVp_isForward() {
        return vp_isForward;
    }

    public final void setVp_isForward(boolean value) {
        LinearLayout linearLayout = findViewById(R.id.vp_triangle_container);
        linearLayout.setRotation(value ? 0f : 180f);
        vp_isForward = value;
    }

    public final TextView vp_getTextView() {
        return (TextView)findViewById(R.id.vp_tv_seconds);
    }

    public final int getVp_icon() {
        return vp_icon;
    }

    public final void setVp_icon(int value) {
        if (value > 0) {
            ((ImageView)findViewById(R.id.vp_icon_1)).setImageResource(value);
            ((ImageView)findViewById(R.id.vp_icon_2)).setImageResource(value);
            ((ImageView)findViewById(R.id.vp_icon_3)).setImageResource(value);
        }
        vp_icon = value;
    }

    /**
     * Starts the triangle animation
     */
    public final void vp_start() {
        vp_stop();
        vp_animate = true;
        vp_firstAnimator.start();
    }

    /**
     * Stops the triangle animation
     */
    public final void vp_stop() {
        vp_animate = false;
        vp_firstAnimator.cancel();
        vp_secondAnimator.cancel();
        vp_thirdAnimator.cancel();
        vp_fourthAnimator.cancel();
        vp_fifthAnimator.cancel();
        vp_reset();
    }

    private final void vp_reset() {
        findViewById(R.id.vp_icon_1).setAlpha(0f);
        findViewById(R.id.vp_icon_2).setAlpha(0f);
        findViewById(R.id.vp_icon_3).setAlpha(0f);
    }


    private final class vp_CustomValueAnimator extends ValueAnimator {
        public vp_CustomValueAnimator(Runnable start, Consumer<Float> update, Runnable end) {
            setDuration(getVp_cycleDuration() / (long)5);
            setFloatValues(0f, 1f);

            addUpdateListener(new AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    update.accept((Float)animation.getAnimatedValue());
                }
            });

            addListener(new AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    start.run();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    end.run();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }
}
