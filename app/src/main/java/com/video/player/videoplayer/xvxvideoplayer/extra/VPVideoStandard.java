package com.video.player.videoplayer.xvxvideoplayer.extra;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkRequest;

import com.video.player.videoplayer.xvxvideoplayer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VPVideoStandard extends VP_Jzvd {
    protected static Timer VP_DISMISS_CONTROL_VIEW_TIMER = null;
    public static int VP_LAST_GET_BATTERYLEVEL_PERCENT = 70;
    public static long VP_LAST_GET_BATTERYLEVEL_TIME;
    public ImageView vp_app_video_crop;
    public ImageView vp_app_video_lock;
    public ImageView vp_app_video_more;
    public ImageView vp_app_video_mute;
    public ImageView vp_app_video_next;
    public ImageView vp_app_video_previous;
    public ImageView vp_app_video_repete;
    public ImageView vp_app_video_shuffle;
    public ImageView vp_app_video_speed;
    public ImageView vp_app_video_unlock;
    public ImageView vp_backButton;
    public BroadcastReceiver vp_battertReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                VPVideoStandard.VP_LAST_GET_BATTERYLEVEL_PERCENT = (intent.getIntExtra("level", 0) * 100) /
                        intent.getIntExtra("scale", 100);
                VPVideoStandard.this.setBatteryLevel();
                try {
                    vp_jzvdContext.unregisterReceiver(VPVideoStandard.this.vp_battertReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public ImageView vp_batteryLevel;
    public LinearLayout vp_batteryTimeLayout;
    public TextView vp_clarity;
    public PopupWindow vp_clarityPopWindow;
    protected ArrayDeque<Runnable> vp_delayTask = new ArrayDeque<>();
    protected long vp_doubleTime = 200;
    protected long vp_lastClickTime = 0;
    public ProgressBar vp_loadingProgressBar;
    protected Dialog vp_mBrightnessDialog;
    protected ProgressBar vp_mDialogBrightnessProgressBar;
    protected TextView vp_mDialogBrightnessTextView;
    protected ImageView vp_mDialogIcon;
    protected ProgressBar vp_mDialogProgressBar;
    protected TextView vp_mDialogSeekTime;
    protected TextView vp_mDialogTotalTime;
    protected ImageView vp_mDialogVolumeImageView;
    protected ProgressBar vp_mDialogVolumeProgressBar;
    protected TextView vp_mDialogVolumeTextView;
    protected DismissControlViewTimerTask vp_mDismissControlViewTimerTask;
    protected boolean vp_mIsWifi;
    protected Dialog vp_mProgressDialog;
    public TextView vp_mRetryBtn;
    public LinearLayout vp_mRetryLayout;
    protected Dialog vp_mVolumeDialog;
    public ArrayList<MediaData> vp_media_datas = new ArrayList<>();
    OnVideoCompleteListener vp_onVideoCompleteListener;
    public ImageView vp_play_back_speed_close;
    public ImageView vp_posterImageView;
    public TextView vp_replayTextView;
    Animation vp_slideDownAnimation;
    Animation vp_slideUpAnimation;
    public TextView vp_speed05;
    public TextView vp_speed10;
    public TextView vp_speed125;
    public TextView vp_speed15;
    public TextView vp_speed175;
    public TextView vp_speed20;
    public TextView vp_speed75;
    public LinearLayout vp_speed_layout;
    public ImageView vp_tinyBackImageView;
    public TextView vp_titleTextView;
    public TextView vp_videoCurrentTime;
//    public ImageView video_background_play;
    public ImageView vp_video_floating_mode;
    public LinearLayout vp_video_playback_options;
    private ImageView vp_video_playlist;
    private ImageView vp_video_playlist_close;
    public ImageView vp_video_screenshot;
    private RecyclerView vp_videoplaylist_recycler;

    public BroadcastReceiver vp_wifiReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean isWifiConnected;
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction()) && VPVideoStandard.this.vp_mIsWifi != (isWifiConnected = VP_Utils.isWifiConnected(context))) {
                VPVideoStandard.this.vp_mIsWifi = isWifiConnected;
                if (!VPVideoStandard.this.vp_mIsWifi && !VP_WIFI_TIP_DIALOG_SHOWED && VPVideoStandard.this.vp_state == 5) {
                    VPVideoStandard.this.vp_startButton.performClick();
                    VPVideoStandard.this.showWifiDialog();
                }
            }
        }
    };

    public interface OnVideoCompleteListener {
        void onBackClick();

        void onBackgroundEnable(boolean z);

        void onDeleteCallBack();

        void onFloatingwindowClick();

        void onNextClick();

        void onPreviousClick();

        void onSelectVideo(int i);

        void onShuffleClick(boolean z);

        void onTakescreenShot();

        void onVideoComplete();
    }

    @Override
    public int getLayoutId() {
        return R.layout.vp_jz_layout_std;
    }

    public VPVideoStandard(Context context) {
        super(context);
    }

    public VPVideoStandard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        this.vp_slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.vp_pop_from_bottom_anim_in);
        this.vp_slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.vp_pop_from_bottom_anim_out);
        this.vp_batteryTimeLayout = (LinearLayout) findViewById(R.id.vp_battery_time_layout);
        this.vp_titleTextView = (TextView) findViewById(R.id.vp_title);
        this.vp_backButton = (ImageView) findViewById(R.id.vp_back);
        this.vp_posterImageView = (ImageView) findViewById(R.id.vp_poster);
        this.vp_loadingProgressBar = (ProgressBar) findViewById(R.id.vp_loading);
        this.vp_tinyBackImageView = (ImageView) findViewById(R.id.vp_back_tiny);
        this.vp_batteryLevel = (ImageView) findViewById(R.id.vp_battery_level);
        this.vp_videoCurrentTime = (TextView) findViewById(R.id.vp_video_current_time);
        this.vp_replayTextView = (TextView) findViewById(R.id.vp_replay_text);
        this.vp_clarity = (TextView) findViewById(R.id.vp_clarity);
        this.vp_mRetryBtn = (TextView) findViewById(R.id.vp_retry_btn);
        this.vp_mRetryLayout = (LinearLayout) findViewById(R.id.vp_retry_layout);
        this.vp_app_video_lock = (ImageView) findViewById(R.id.vp_app_video_lock);
        this.vp_app_video_unlock = (ImageView) findViewById(R.id.vp_app_video_unlock);
        this.vp_app_video_crop = (ImageView) findViewById(R.id.vp_app_video_crop);
        this.vp_app_video_more = (ImageView) findViewById(R.id.vp_app_video_more);
        this.vp_app_video_next = (ImageView) findViewById(R.id.vp_app_video_next);
        this.vp_app_video_previous = (ImageView) findViewById(R.id.vp_app_video_previous);
        this.vp_video_playback_options = (LinearLayout) findViewById(R.id.vp_video_playback_options);
        this.vp_speed_layout = (LinearLayout) findViewById(R.id.vp_speed_layout);
        this.vp_video_playlist = (ImageView) findViewById(R.id.vp_video_playlist);
        this.vp_video_playlist_close = (ImageView) findViewById(R.id.vp_video_list_close);
        this.vp_videoplaylist_recycler = (RecyclerView) findViewById(R.id.vp_videoplaylist_recycler);
        this.vp_play_back_speed_close = (ImageView) findViewById(R.id.vp_play_back_speed_close);
        this.vp_video_screenshot = (ImageView) findViewById(R.id.vp_video_screenshot);
        this.vp_speed05 = (TextView) findViewById(R.id.vp_speed05);
        this.vp_speed75 = (TextView) findViewById(R.id.vp_speed075);
        this.vp_speed10 = (TextView) findViewById(R.id.vp_speed10);
        this.vp_speed125 = (TextView) findViewById(R.id.vp_speed125);
        this.vp_speed15 = (TextView) findViewById(R.id.vp_speed15);
        this.vp_speed175 = (TextView) findViewById(R.id.vp_speed175);
        this.vp_speed20 = (TextView) findViewById(R.id.vp_speed20);
        this.vp_app_video_shuffle = (ImageView) findViewById(R.id.vp_app_video_shuffle);
        this.vp_app_video_repete = (ImageView) findViewById(R.id.vp_app_video_repete);
        this.vp_app_video_speed = (ImageView) findViewById(R.id.vp_app_video_speed);
        this.vp_app_video_mute = (ImageView) findViewById(R.id.vp_app_video_mute);
        this.vp_video_floating_mode = (ImageView) findViewById(R.id.vp_video_floating_mode);
//        this.video_background_play = (ImageView) findViewById(R.id.video_background_play);
        if (this.vp_batteryTimeLayout == null) {
            this.vp_batteryTimeLayout = new LinearLayout(context);
        }
        if (this.vp_titleTextView == null) {
            this.vp_titleTextView = new TextView(context);
        }
        if (this.vp_backButton == null) {
            this.vp_backButton = new ImageView(context);
        }
        if (this.vp_posterImageView == null) {
            this.vp_posterImageView = new ImageView(context);
        }
        if (this.vp_loadingProgressBar == null) {
            this.vp_loadingProgressBar = new ProgressBar(context);
        }
        if (this.vp_tinyBackImageView == null) {
            this.vp_tinyBackImageView = new ImageView(context);
        }
        if (this.vp_batteryLevel == null) {
            this.vp_batteryLevel = new ImageView(context);
        }
        if (this.vp_videoCurrentTime == null) {
            this.vp_videoCurrentTime = new TextView(context);
        }
        if (this.vp_replayTextView == null) {
            this.vp_replayTextView = new TextView(context);
        }
        if (this.vp_clarity == null) {
            this.vp_clarity = new TextView(context);
        }
        if (this.vp_mRetryBtn == null) {
            this.vp_mRetryBtn = new TextView(context);
        }
        if (this.vp_mRetryLayout == null) {
            this.vp_mRetryLayout = new LinearLayout(context);
        }
        this.vp_posterImageView.setOnClickListener(this);
        this.vp_backButton.setOnClickListener(this);
        this.vp_tinyBackImageView.setOnClickListener(this);
        this.vp_clarity.setOnClickListener(this);
        this.vp_mRetryBtn.setOnClickListener(this);
        this.vp_app_video_lock.setOnClickListener(this);
        this.vp_app_video_unlock.setOnClickListener(this);
        this.vp_app_video_crop.setOnClickListener(this);
        this.vp_app_video_next.setOnClickListener(this);
        this.vp_app_video_previous.setOnClickListener(this);
        this.vp_app_video_more.setOnClickListener(this);
        this.vp_app_video_shuffle.setOnClickListener(this);
        this.vp_app_video_repete.setOnClickListener(this);
        this.vp_app_video_speed.setOnClickListener(this);
        this.vp_app_video_mute.setOnClickListener(this);
        this.vp_video_playlist.setOnClickListener(this);
        this.vp_video_playlist_close.setOnClickListener(this);
        this.vp_video_floating_mode.setOnClickListener(this);
//        this.video_background_play.setOnClickListener(this);
        this.vp_speed_layout.setOnClickListener(this);
        this.vp_speed05.setOnClickListener(this);
        this.vp_speed75.setOnClickListener(this);
        this.vp_speed10.setOnClickListener(this);
        this.vp_speed125.setOnClickListener(this);
        this.vp_speed15.setOnClickListener(this);
        this.vp_speed175.setOnClickListener(this);
        this.vp_speed20.setOnClickListener(this);
        this.vp_play_back_speed_close.setOnClickListener(this);
        this.vp_video_screenshot.setOnClickListener(this);
    }

    @Override
    public void setUp(VP_DataSource VPDataSource, int i, Class cls) {
        if (System.currentTimeMillis() - this.vp_goBackFullscreenTime >= 200 &&
                System.currentTimeMillis() - this.vp_gotoFullscreenTime >= 200) {
            super.setUp(VPDataSource, i, cls);
            this.vp_titleTextView.setText(VPDataSource.vp_title);
            setVp_screen(i);
        }
    }

    @Override
    public void changeUrl(VP_DataSource VPDataSource, long j) {
        super.changeUrl(VPDataSource, j);
        this.vp_titleTextView.setText(VPDataSource.vp_title);
    }

    public void changeStartButtonSize(int i) {
        ViewGroup.LayoutParams vp_layoutParams = this.vp_startButton.getLayoutParams();
        vp_layoutParams.height = i;
        vp_layoutParams.width = i;
        ViewGroup.LayoutParams layoutParams2 = this.vp_loadingProgressBar.getLayoutParams();
        layoutParams2.height = i;
        layoutParams2.width = i;
    }

    @Override
    public void vp_onStateNormal() {
        super.vp_onStateNormal();
        vp_changeUiToNormal();
    }

    @Override
    public void vp_onStatePreparing() {
        super.vp_onStatePreparing();
        vp_changeUiToPreparing();
    }

    @Override
    public void vp_onStatePreparingPlaying() {
        super.vp_onStatePreparingPlaying();
        vp_changeUIToPreparingPlaying();
    }

    @Override
    public void vp_onStatePreparingChangeUrl() {
        super.vp_onStatePreparingChangeUrl();
        vp_changeUIToPreparingChangeUrl();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void vp_onStatePlaying() {
        super.vp_onStatePlaying();
        vp_changeUiToPlayingClear();
        vp_startDismissControlViewTimer();
        this.vp_app_video_play.setImageResource(R.drawable.vp_hplib_ic_pause);
        this.vp_startButton.setImageResource(R.drawable.vp_jz_click_pause_selector);
        this.vp_replayTextView.setVisibility(8);
    }

    @Override
    public void vp_onStatePause() {
        super.vp_onStatePause();
        changeUiToPauseShow();
        cancelDismissControlViewTimer();
    }

    @Override
    public void vp_onStateError() {
        super.vp_onStateError();
        changeUiToError();
    }

    @SuppressLint("WrongConstant")
    public void setAdapter() {
        VP_VideolistAdapter vp_video_list_Adapter = new VP_VideolistAdapter(getApplicationContext(), this.vp_media_datas, new VP_VideolistAdapter.OnClickVideo() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClickVideo(int i) {
                VPVideoStandard.this.vp_onVideoCompleteListener.onSelectVideo(i);
                VPVideoStandard.this.vp_videoplaylist_recycler.setVisibility(8);
                VPVideoStandard.this.vp_video_playlist_close.setVisibility(8);
            }
        });
        this.vp_videoplaylist_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        this.vp_videoplaylist_recycler.setAdapter(vp_video_list_Adapter);
    }

    public void setVp_onVideoCompleteListener(OnVideoCompleteListener onVideoCompleteListener2) {
        this.vp_onVideoCompleteListener = onVideoCompleteListener2;
    }

    @Override
    public void vp_onStateAutoComplete() {
        super.vp_onStateAutoComplete();
        this.vp_onVideoCompleteListener.onVideoComplete();
        changeUiToComplete();
        cancelDismissControlViewTimer();
    }

    @Override
    public void startVideo() {
        super.startVideo();
        registerWifiListener(getApplicationContext());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int id = view.getId();
        if (id == R.id.vp_surface_container) {
            int action = motionEvent.getAction();
            if (action == 1) {
                if (this.vp_screen != 1 || !this.vp_isLockScreen) {
                    vp_startDismissControlViewTimer();
                    if (this.vp_mChangePosition) {
                        long duration = getDuration();
                        long j = this.vp_mSeekTimePosition * 100;
                        if (duration == 0) {
                            duration = 1;
                        }
                        long j2 = j / duration;
                    }
                    Runnable r0 = () -> {
                        if (!vp_mChangePosition && !vp_mChangeVolume) {
                            onClickUiToggle();
                        }
                    };
                    view.postDelayed(r0, this.vp_doubleTime + 20);
                    this.vp_delayTask.add(r0);
                    while (this.vp_delayTask.size() > 2) {
                        this.vp_delayTask.pollFirst();
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - this.vp_lastClickTime < this.vp_doubleTime) {
                        for (Runnable runnable : this.vp_delayTask) {
                            view.removeCallbacks(runnable);
                        }
                        if (this.vp_state == 5 || this.vp_state == 6) {
                            Log.d("JZVD", "doublClick [" + hashCode() + "] ");
                            this.vp_startButton.performClick();
                        }
                    }
                    this.vp_lastClickTime = currentTimeMillis;
                } else {
                    if (motionEvent.getX() == motionEvent.getX() || motionEvent.getY() == motionEvent.getY()) {
                        vp_startDismissControlViewTimer();
                        onClickUiToggle();
                    }
                    return true;
                }
            }
            if (this.vp_screen == 1 && this.vp_isLockScreen) {
                return true;
            }
        } else if (id == R.id.vp_bottom_seek_progress) {
            int action2 = motionEvent.getAction();
            if (action2 == 0) {
                cancelDismissControlViewTimer();
            } else if (action2 == 1) {
                vp_startDismissControlViewTimer();
            }
        }
        return super.onTouch(view, motionEvent);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == R.id.vp_poster) {
            clickPoster();
        } else if (id == R.id.vp_surface_container) {
            vp_clickSurfaceContainer();
            PopupWindow vp_popupWindow = this.vp_clarityPopWindow;
            if (vp_popupWindow != null) {
                vp_popupWindow.dismiss();
            }
        } else if (id == R.id.vp_back) {
            vp_clickBack();
        } else if (id == R.id.vp_back_tiny) {
            vp_clickBackTiny();
        } else if (id == R.id.vp_clarity) {
            vp_clickClarity();
        } else if (id == R.id.vp_retry_btn) {
            vp_clickRetryBtn();
        } else if (id == R.id.vp_app_video_lock) {
            if (this.vp_screen == 1 || this.vp_screen == 0) {
                this.vp_app_video_lock.setTag(1);
                if (!this.vp_isLockScreen) {
                    this.vp_isLockScreen = true;
                    this.vp_app_video_unlock.setVisibility(0);
                    dissmissControlView(1);
                }
            }
        } else if (id == R.id.vp_app_video_unlock) {
            this.vp_app_video_lock.setVisibility(0);
            this.vp_app_video_unlock.setVisibility(8);
            this.vp_isLockScreen = false;
            this.vp_bottomContainer.setVisibility(0);
            this.vp_topContainer.setVisibility(0);
            this.vp_centerContainer.setVisibility(0);
            this.vp_startButton.setVisibility(0);
            this.vp_app_video_n_skip.setVisibility(0);
            this.vp_app_video_p_skip.setVisibility(0);
            this.vp_backButton.setVisibility(0);
        } else if (id == R.id.vp_app_video_crop) {
            if (this.vp_aspectRatio == 0) {
                vp_setVpVideoImageDisplayType(1);
                this.vp_aspectRatio = 1;
            } else if (this.vp_aspectRatio == 1) {
                vp_setVpVideoImageDisplayType(2);
                this.vp_aspectRatio = 2;
            } else if (this.vp_aspectRatio == 2) {
                vp_setVpVideoImageDisplayType(3);
                this.vp_aspectRatio = 3;
            } else {
                vp_setVpVideoImageDisplayType(0);
                this.vp_aspectRatio = 0;
            }
            this.vp_rlAds.setVisibility(8);
        } else if (id == R.id.vp_app_video_next) {
            this.vp_onVideoCompleteListener.onNextClick();
            this.vp_rlAds.setVisibility(8);
        } else if (id == R.id.vp_app_video_previous) {
            this.vp_onVideoCompleteListener.onPreviousClick();
            this.vp_rlAds.setVisibility(8);
        } else if (id == R.id.vp_app_video_shuffle) {
            if (this.vp_isShuffle) {
                this.vp_isShuffle = false;
                this.vp_app_video_shuffle.setBackgroundResource(R.drawable.vp_bg_circle_tansparant);
            } else {
                this.vp_isShuffle = true;
                this.vp_app_video_shuffle.setBackgroundResource(R.drawable.vp_bg_circle_selected);
            }
            this.vp_onVideoCompleteListener.onShuffleClick(this.vp_isShuffle);
        } else if (id == R.id.vp_app_video_repete) {
            if (this.vp_isLooping) {
                this.vp_app_video_repete.setImageResource(R.drawable.vp_ic_repeat_black_24dp);
                this.vp_isLooping = false;
                return;
            }
            this.vp_app_video_repete.setImageResource(R.drawable.vp_ic_repeat_one_black_24dp);
            this.vp_isLooping = true;
            this.vp_rlAds.setVisibility(8);
        } else if (id == R.id.vp_app_video_mute) {
            if (!this.vp_isMute) {
                this.vp_app_video_mute.setImageResource(R.drawable.vp_ic_volume_off_read);
                muteAudio();
                this.vp_isMute = true;
                return;
            }
            this.vp_app_video_mute.setImageResource(R.drawable.vp_ic_volume_off_white_36dp);
            unMuteAudio();
            this.vp_isMute = false;
        } else if (id == R.id.vp_video_floating_mode) {
            this.vp_onVideoCompleteListener.onFloatingwindowClick();
        }
//        else if (id == R.id.video_background_play) {
//            if (this.isBackgroundEnable) {
//                this.isBackgroundEnable = false;
//                this.video_background_play.setImageResource(R.drawable.ic_background_video);
//            } else {
//                this.isBackgroundEnable = true;
//                this.video_background_play.setImageResource(R.drawable.ic_background_video_read);
//            }
//            this.onVideoCompleteListener.onBackgroundEnable(this.isBackgroundEnable);
//        }
        else if (id == R.id.vp_app_video_more) {
            if (!this.vp_isShowMoreOption) {
                this.vp_isShowMoreOption = true;
                this.vp_slideUpAnimation.setAnimationListener(new Animation.AnimationListener() {


                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        VPVideoStandard.this.vp_video_playback_options.clearAnimation();
                    }
                });
                this.vp_video_playback_options.setVisibility(0);
                this.vp_video_playback_options.startAnimation(this.vp_slideUpAnimation);
                return;
            }
            this.vp_isShowMoreOption = false;
            this.vp_video_playback_options.startAnimation(this.vp_slideDownAnimation);
            this.vp_slideDownAnimation.setAnimationListener(new Animation.AnimationListener() {

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    VPVideoStandard.this.vp_video_playback_options.setVisibility(8);
                    VPVideoStandard.this.vp_video_playback_options.clearAnimation();
                }
            });
        } else if (id == R.id.vp_app_video_speed) {
            this.vp_rlAds.setVisibility(8);
            if (this.vp_speed_layout.getVisibility() == 0) {
                this.vp_speed_layout.setVisibility(8);
                this.vp_isSpeed = false;
                return;
            }
            if (this.vp_videoplaylist_recycler.getVisibility() == 0) {
                this.vp_videoplaylist_recycler.setVisibility(8);
                this.vp_video_playlist_close.setVisibility(8);
            }
            this.vp_speed_layout.setVisibility(0);
            this.vp_isSpeed = true;
        } else if (id == R.id.vp_speed05) {
            this.VPMediaInterface.setSpeed(0.5f);
            vp_setSpeedViewBackground(this.vp_speed05, this.vp_speed75, this.vp_speed10, this.vp_speed125, this.vp_speed15, this.vp_speed175, this.vp_speed20);
        } else if (id == R.id.vp_speed075) {
            this.VPMediaInterface.setSpeed(0.75f);
            vp_setSpeedViewBackground(this.vp_speed75, this.vp_speed05, this.vp_speed10, this.vp_speed125, this.vp_speed15, this.vp_speed175, this.vp_speed20);
        } else if (id == R.id.vp_speed10) {
            this.VPMediaInterface.setSpeed(1.0f);
            vp_setSpeedViewBackground(this.vp_speed10, this.vp_speed75, this.vp_speed05, this.vp_speed125, this.vp_speed15, this.vp_speed175, this.vp_speed20);
        } else if (id == R.id.vp_speed125) {
            this.VPMediaInterface.setSpeed(1.25f);
            vp_setSpeedViewBackground(this.vp_speed125, this.vp_speed75, this.vp_speed10, this.vp_speed05, this.vp_speed15, this.vp_speed175, this.vp_speed20);
        } else if (id == R.id.vp_speed15) {
            this.VPMediaInterface.setSpeed(1.5f);
            vp_setSpeedViewBackground(this.vp_speed15, this.vp_speed75, this.vp_speed10, this.vp_speed125, this.vp_speed05, this.vp_speed175, this.vp_speed20);
        } else if (id == R.id.vp_speed175) {
            this.VPMediaInterface.setSpeed(1.75f);
            vp_setSpeedViewBackground(this.vp_speed175, this.vp_speed75, this.vp_speed10, this.vp_speed125, this.vp_speed15, this.vp_speed05, this.vp_speed20);
        } else if (id == R.id.vp_speed20) {
            this.VPMediaInterface.setSpeed(2.0f);
            vp_setSpeedViewBackground(this.vp_speed20, this.vp_speed75, this.vp_speed10, this.vp_speed125, this.vp_speed15, this.vp_speed175, this.vp_speed05);
        } else if (id == R.id.vp_play_back_speed_close) {
            this.vp_speed_layout.setVisibility(8);
        } else if (id == R.id.vp_video_screenshot) {
            this.vp_onVideoCompleteListener.onTakescreenShot();
        } else if (id == R.id.vp_app_video_n_skip) {
            this.VPMediaInterface.seekTo(this.VPMediaInterface.getCurrentPosition() + WorkRequest.MIN_BACKOFF_MILLIS);
        } else if (id == R.id.vp_app_video_p_skip) {
            if (this.VPMediaInterface.getCurrentPosition() > WorkRequest.MIN_BACKOFF_MILLIS) {
                this.VPMediaInterface.seekTo(this.VPMediaInterface.getCurrentPosition() - WorkRequest.MIN_BACKOFF_MILLIS);
            }
        } else if (id == R.id.vp_video_list_close) {
            this.vp_videoplaylist_recycler.setVisibility(8);
            this.vp_video_playlist_close.setVisibility(8);
        } else if (id == R.id.vp_video_playlist) {
            if (this.vp_speed_layout.getVisibility() == 0) {
                this.vp_speed_layout.setVisibility(8);
            }
            this.vp_videoplaylist_recycler.setVisibility(0);
            this.vp_video_playlist_close.setVisibility(0);
            this.vp_rlAds.setVisibility(8);
        }
    }

    public void vp_setSpeedViewBackground(View view, View view2, View view3, View view4, View view5, View view6, View view7) {
        view.setBackgroundResource(R.drawable.vp_jz_retry);
        this.vp_state = 5;
        updateStartImage();
        view2.setBackgroundResource(0);
        view3.setBackgroundResource(0);
        view4.setBackgroundResource(0);
        view5.setBackgroundResource(0);
        view6.setBackgroundResource(0);
        view7.setBackgroundResource(0);
    }

    public boolean IsLoopingVideo() {
        return this.vp_isLooping;
    }

    @SuppressLint("WrongConstant")
    public void vp_clickRetryBtn() {
        if (this.VPDataSource.vp_urlsMap.isEmpty() || this.VPDataSource.getCurrentUrl() == null) {
            Toast.makeText(this.vp_jzvdContext, getResources().getString(R.string.no_url), 0).show();
        } else if (this.VPDataSource.getCurrentUrl().toString().startsWith("file") || this.VPDataSource.getCurrentUrl().toString().startsWith("/") || VP_Utils.isWifiConnected(this.vp_jzvdContext) || VP_WIFI_TIP_DIALOG_SHOWED) {
            this.vp_seekToInAdvance = this.vp_mCurrentPosition;
            startVideo();
        } else {
            showWifiDialog();
        }
    }

    public void vp_clickClarity() {
        onCLickUiToggleToClear();
        @SuppressLint("WrongConstant") final LinearLayout linearLayout = (LinearLayout) ((LayoutInflater)
                this.vp_jzvdContext.getSystemService("layout_inflater")).inflate(R.layout.vp_jz_layout_clarity, (ViewGroup) null);
        OnClickListener r2 = view -> {
            VPDataSource.vp_currentUrlIndex = (Integer) view.getTag();
            changeUrl(VPDataSource, getCurrentPositionWhenPlaying());
            vp_clarity.setText(VPDataSource.getCurrentKey().toString());
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (i == VPDataSource.vp_currentUrlIndex) {
                    ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#fff85959"));
                } else {
                    ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#ffffff"));
                }
            }
            PopupWindow vp_popupWindow = vp_clarityPopWindow;
            if (vp_popupWindow != null) {
                vp_popupWindow.dismiss();
            }
        };
        for (int i = 0; i < this.VPDataSource.vp_urlsMap.size(); i++) {
            String vp_keyFromDataSource = this.VPDataSource.vp_getKeyFromDataSource(i);
            TextView vp_textView = (TextView) inflate(this.vp_jzvdContext, R.layout.vp_jz_layout_clarity_item, null);
            vp_textView.setText(vp_keyFromDataSource);
            vp_textView.setTag(i);
            linearLayout.addView(vp_textView, i);
            vp_textView.setOnClickListener(r2);
            if (i == this.VPDataSource.vp_currentUrlIndex) {
                vp_textView.setTextColor(Color.parseColor("#fff85959"));
            }
        }
        PopupWindow vp_popupWindow = new PopupWindow((View) linearLayout, VP_Utils.dip2px(this.vp_jzvdContext, 240.0f), -1, true);
        this.vp_clarityPopWindow = vp_popupWindow;
        vp_popupWindow.setContentView(linearLayout);
        this.vp_clarityPopWindow.setAnimationStyle(R.style.pop_animation);
        this.vp_clarityPopWindow.showAtLocation(this.vp_textureViewContainer, GravityCompat.END, 0, 0);
    }

    public void lambda$clickClarity$1$JzvdStd(LinearLayout linearLayout, View view) {
        this.VPDataSource.vp_currentUrlIndex = ((Integer) view.getTag()).intValue();
        changeUrl(this.VPDataSource, getCurrentPositionWhenPlaying());
        this.vp_clarity.setText(this.VPDataSource.getCurrentKey().toString());
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (i == this.VPDataSource.vp_currentUrlIndex) {
                ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#fff85959"));
            } else {
                ((TextView) linearLayout.getChildAt(i)).setTextColor(Color.parseColor("#ffffff"));
            }
        }
        PopupWindow vp_popupWindow = this.vp_clarityPopWindow;
        if (vp_popupWindow != null) {
            vp_popupWindow.dismiss();
        }
    }

    public void vp_clickBackTiny() {
        vp_clearFloatScreen();
    }

    public void vp_clickBack() {
        this.vp_onVideoCompleteListener.onBackClick();
    }

    public void vp_clickSurfaceContainer() {
        vp_startDismissControlViewTimer();
    }

    @SuppressLint("WrongConstant")
    public void clickPoster() {
        if (this.VPDataSource == null || this.VPDataSource.vp_urlsMap.isEmpty() || this.VPDataSource.getCurrentUrl() == null) {
            Toast.makeText(this.vp_jzvdContext, getResources().getString(R.string.no_url), 0).show();
        } else if (this.vp_state == 0) {
            if (this.VPDataSource.getCurrentUrl().toString().startsWith("file") || this.VPDataSource.getCurrentUrl().toString().startsWith("/") || VP_Utils.isWifiConnected(this.vp_jzvdContext) || VP_WIFI_TIP_DIALOG_SHOWED) {
                startVideo();
            } else {
                showWifiDialog();
            }
        } else if (this.vp_state == 7) {
            onClickUiToggle();
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void vp_setScreenNormal() {
        super.vp_setScreenNormal();
        this.vp_app_video_lock.setVisibility(0);
        this.vp_fullscreenButton.setImageResource(R.drawable.vp_jz_enlarge);
        this.vp_backButton.setVisibility(0);
        this.vp_tinyBackImageView.setVisibility(4);
        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_normal));
        this.vp_batteryTimeLayout.setVisibility(8);
        this.vp_clarity.setVisibility(8);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        this.vp_app_video_lock.setImageResource(R.drawable.vp_ic_lock_open);
        this.vp_app_video_lock.setVisibility(0);
        this.vp_fullscreenButton.setImageResource(R.drawable.vp_jz_enlarge);
        this.vp_backButton.setVisibility(0);
        this.vp_tinyBackImageView.setVisibility(4);
        if (this.VPDataSource.vp_urlsMap.size() == 1) {
            this.vp_clarity.setVisibility(8);
        } else {
            this.vp_clarity.setText(this.VPDataSource.getCurrentKey().toString());
            this.vp_clarity.setVisibility(0);
        }
        changeStartButtonSize((int) getResources().getDimension(R.dimen.jz_start_button_w_h_fullscreen));
        vp_setSystemTimeAndBattery();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void setScreenTiny() {
        super.setScreenTiny();
        this.vp_tinyBackImageView.setVisibility(0);
        vp_setAllControlsVisiblity(4, 8, 4, 4, 4, 4, 4);
        this.vp_batteryTimeLayout.setVisibility(8);
        this.vp_clarity.setVisibility(8);
    }

    @Override
    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(this.vp_jzvdContext);
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                VPVideoStandard.this.lambda$showWifiDialog$2$JzvdStd(dialogInterface, i);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            public final void onClick(DialogInterface dialogInterface, int i) {
                VPVideoStandard.this.lambda$showWifiDialog$3$JzvdStd(dialogInterface, i);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
                releaseAllVideos();
                VPVideoStandard.this.vp_clearFloatScreen();
            }
        });
        builder.create().show();
    }

    public void lambda$showWifiDialog$2$JzvdStd(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        VP_WIFI_TIP_DIALOG_SHOWED = true;
        if (this.vp_state == 6) {
            this.vp_startButton.performClick();
        } else {
            startVideo();
        }
    }

    public void lambda$showWifiDialog$3$JzvdStd(DialogInterface dialogInterface, int i) {
        dialogInterface.dismiss();
        releaseAllVideos();
        vp_clearFloatScreen();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        vp_startDismissControlViewTimer();
    }

    @SuppressLint("WrongConstant")
    public void onClickUiToggle() {
        if (this.vp_bottomContainer.getVisibility() != 0) {
            vp_setSystemTimeAndBattery();
            this.vp_clarity.setText(this.VPDataSource.getCurrentKey().toString());
        }
        if ((this.vp_screen == 1 || this.vp_screen == 0) && this.vp_isLockScreen) {
            this.vp_app_video_unlock.setVisibility(0);
        } else if (this.vp_state == 1) {
            vp_changeUiToPreparing();
            if (this.vp_bottomContainer.getVisibility() != 0) {
                vp_setSystemTimeAndBattery();
            }
        } else if (this.vp_state == 5) {
            if (this.vp_bottomContainer.getVisibility() == 0) {
                vp_changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (this.vp_state == 6) {
            if (this.vp_bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        }
    }

    public void vp_setSystemTimeAndBattery() {
        this.vp_videoCurrentTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
        if (System.currentTimeMillis() - VP_LAST_GET_BATTERYLEVEL_TIME > WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS) {
            VP_LAST_GET_BATTERYLEVEL_TIME = System.currentTimeMillis();
            this.vp_jzvdContext.registerReceiver(this.vp_battertReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            return;
        }
        setBatteryLevel();
    }

    public void setBatteryLevel() {
        int i = VP_LAST_GET_BATTERYLEVEL_PERCENT;
        if (i < 15) {
            this.vp_batteryLevel.setBackgroundResource(R.drawable.vp_jz_battery_level_10);
        } else if (i >= 15 && i < 40) {
            this.vp_batteryLevel.setBackgroundResource(R.drawable.vp_jz_battery_level_30);
        } else if (i >= 40 && i < 60) {
            this.vp_batteryLevel.setBackgroundResource(R.drawable.vp_jz_battery_level_50);
        } else if (i >= 60 && i < 80) {
            this.vp_batteryLevel.setBackgroundResource(R.drawable.vp_jz_battery_level_70);
        } else if (i >= 80 && i < 95) {
            this.vp_batteryLevel.setBackgroundResource(R.drawable.vp_jz_battery_level_90);
        } else if (i >= 95 && i <= 100) {
            this.vp_batteryLevel.setBackgroundResource(R.drawable.vp_jz_battery_level_100);
        }
    }

    @SuppressLint("WrongConstant")
    public void onCLickUiToggleToClear() {
        if (this.vp_state == 1) {
            if (this.vp_bottomContainer.getVisibility() == 0) {
                vp_changeUiToPreparing();
            }
        } else if (this.vp_state == 5) {
            if (this.vp_bottomContainer.getVisibility() == 0) {
                vp_changeUiToPlayingClear();
            }
        } else if (this.vp_state == 6) {
            if (this.vp_bottomContainer.getVisibility() == 0) {
                changeUiToPauseClear();
            }
        } else if (this.vp_state == 7 && this.vp_bottomContainer.getVisibility() == 0) {
            changeUiToComplete();
        }
    }

    @Override
    public void onProgress(int i, long j, long j2) {
        super.onProgress(i, j, j2);
    }

    @Override
    public void setBufferProgress(int i) {
        super.setBufferProgress(i);
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
    }

    public void vp_changeUiToNormal() {
        int i = this.vp_screen;
        if (i == 0 || i == 1) {
            vp_setAllControlsVisiblity(0, 8, 0, 4, 0, 4, 4);
            updateStartImage();
        }
    }

    public void vp_changeUiToPreparing() {
        int i = this.vp_screen;
        if (i == 0 || i == 1) {
            vp_setAllControlsVisiblity(4, 8, 4, 0, 0, 4, 4);
            updateStartImage();
        }
    }

    public void vp_changeUIToPreparingPlaying() {
        int i = this.vp_screen;
        if (i == 0 || i == 1) {
            vp_setAllControlsVisiblity(0, 0, 4, 0, 4, 4, 4);
            updateStartImage();
        }
    }

    public void vp_changeUIToPreparingChangeUrl() {
        int i = this.vp_screen;
        if (i == 0 || i == 1) {
            vp_setAllControlsVisiblity(4, 8, 4, 0, 0, 4, 4);
            updateStartImage();
        }
    }

    @SuppressLint("WrongConstant")
    public void changeUiToPlayingShow() {
        if (this.vp_screen != 1) {
            int i = this.vp_screen;
            if (i == 0 || i == 1) {
                vp_setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
                updateStartImage();
            }
        } else if (this.vp_isLockScreen) {
            this.vp_topContainer.setVisibility(8);
            this.vp_centerContainer.setVisibility(8);
            this.vp_bottomContainer.setVisibility(8);
            this.vp_startButton.setVisibility(8);
            this.vp_app_video_n_skip.setVisibility(8);
            this.vp_app_video_p_skip.setVisibility(8);
        } else {
            this.vp_topContainer.setVisibility(0);
            this.vp_centerContainer.setVisibility(0);
            this.vp_bottomContainer.setVisibility(0);
            this.vp_startButton.setVisibility(0);
            this.vp_app_video_n_skip.setVisibility(0);
            this.vp_app_video_p_skip.setVisibility(0);
        }
    }

    public void vp_changeUiToPlayingClear() {
        int i = this.vp_screen;
        if (i == 0 || i == 1) {
            vp_setAllControlsVisiblity(4, 8, 4, 4, 4, 0, 4);
        }
    }

    @SuppressLint("WrongConstant")
    public void changeUiToPauseShow() {
        if (this.vp_isLockScreen) {
            this.vp_bottomContainer.setVisibility(8);
            this.vp_topContainer.setVisibility(8);
            this.vp_centerContainer.setVisibility(8);
            this.vp_startButton.setVisibility(8);
            this.vp_app_video_n_skip.setVisibility(8);
            this.vp_app_video_p_skip.setVisibility(8);
            return;
        }
        int i = this.vp_screen;
        if (i == 0 || i == 1) {
            vp_setAllControlsVisiblity(0, 0, 0, 4, 4, 4, 4);
            updateStartImage();
        }
    }

    public void changeUiToPauseClear() {
        int i = this.vp_screen;
        if (i == 0 || i == 1) {
            vp_setAllControlsVisiblity(4, 8, 4, 4, 4, 0, 4);
        }
    }

    public void changeUiToComplete() {
        vp_clickBack();
    }

    public void changeUiToError() {
        int i = this.vp_screen;
        if (i == 0) {
            vp_setAllControlsVisiblity(4, 8, 0, 4, 4, 4, 0);
            updateStartImage();
        } else if (i == 1) {
            vp_setAllControlsVisiblity(0, 8, 0, 4, 4, 4, 0);
            updateStartImage();
        }
    }

    public void vp_setAllControlsVisiblity(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.vp_topContainer.setVisibility(i);
        this.vp_centerContainer.setVisibility(i2);
        this.vp_bottomContainer.setVisibility(i2);
        this.vp_startButton.setVisibility(i3);
        this.vp_app_video_n_skip.setVisibility(i3);
        this.vp_app_video_p_skip.setVisibility(i3);
        this.vp_loadingProgressBar.setVisibility(i4);
        this.vp_posterImageView.setVisibility(i5);
        this.vp_mRetryLayout.setVisibility(i7);
    }

    @SuppressLint("WrongConstant")
    public void updateStartImage() {
        if (this.vp_state == 5) {
            this.vp_startButton.setVisibility(0);
            this.vp_app_video_n_skip.setVisibility(0);
            this.vp_app_video_p_skip.setVisibility(0);
            this.vp_app_video_play.setImageResource(R.drawable.vp_hplib_ic_pause);
            this.vp_startButton.setImageResource(R.drawable.vp_jz_click_pause_selector);
            this.vp_replayTextView.setVisibility(8);
        } else if (this.vp_state == 8) {
            this.vp_startButton.setVisibility(4);
            this.vp_app_video_n_skip.setVisibility(4);
            this.vp_app_video_p_skip.setVisibility(4);
            this.vp_replayTextView.setVisibility(8);
        } else if (this.vp_state == 7) {
            this.vp_startButton.setVisibility(0);
            this.vp_app_video_n_skip.setVisibility(0);
            this.vp_app_video_p_skip.setVisibility(0);
            this.vp_startButton.setImageResource(R.drawable.vp_jz_click_replay_selector);
            this.vp_replayTextView.setVisibility(0);
        } else {
            this.vp_startButton.setImageResource(R.drawable.vp_jz_click_play_selector);
            this.vp_app_video_play.setImageResource(R.drawable.vp_hplib_ic_play_download);
            this.vp_replayTextView.setVisibility(8);
        }
    }

    @Override
    public void showProgressDialog(float f, String str, long j, String str2, long j2) {
        super.showProgressDialog(f, str, j, str2, j2);
        if (this.vp_mProgressDialog == null) {
            View inflate = LayoutInflater.from(this.vp_jzvdContext).inflate(R.layout.vp_jz_dialog_progress, (ViewGroup) null);
            this.vp_mDialogProgressBar = (ProgressBar) inflate.findViewById(R.id.vp_duration_progressbar);
            this.vp_mDialogSeekTime = (TextView) inflate.findViewById(R.id.vp_tv_current);
            this.vp_mDialogTotalTime = (TextView) inflate.findViewById(R.id.vp_tv_duration);
            this.vp_mDialogIcon = (ImageView) inflate.findViewById(R.id.vp_duration_image_tip);
            this.vp_mProgressDialog = createDialogWithView(inflate);
        }
        if (!this.vp_mProgressDialog.isShowing()) {
            this.vp_mProgressDialog.show();
        }
        this.vp_mDialogSeekTime.setText(str);
        TextView textView = this.vp_mDialogTotalTime;
        textView.setText(" / " + str2);
        this.vp_mDialogProgressBar.setProgress(j2 <= 0 ? 0 : (int) ((j * 100) / j2));
        if (f > 0.0f) {
            this.vp_mDialogIcon.setBackgroundResource(R.drawable.vp_jz_forward_icon);
        } else {
            this.vp_mDialogIcon.setBackgroundResource(R.drawable.vp_jz_backward_icon);
        }
        onCLickUiToggleToClear();
    }

    @Override
    public void vp_dismissProgressDialog() {
        super.vp_dismissProgressDialog();
        Dialog dialog = this.vp_mProgressDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showVolumeDialog(float f, int i) {
        super.showVolumeDialog(f, i);
        if (this.vp_mVolumeDialog == null) {
            View inflate = LayoutInflater.from(this.vp_jzvdContext).inflate(R.layout.vp_jz_dialog_volume, (ViewGroup) null);
            this.vp_mDialogVolumeImageView = (ImageView) inflate.findViewById(R.id.vp_volume_image_tip);
            this.vp_mDialogVolumeTextView = (TextView) inflate.findViewById(R.id.vp_tv_volume);
            this.vp_mDialogVolumeProgressBar = (ProgressBar) inflate.findViewById(R.id.vp_volume_progressbar);
            this.vp_mVolumeDialog = createDialogWithViewL(inflate);
        }
        if (!this.vp_mVolumeDialog.isShowing()) {
            this.vp_mVolumeDialog.show();
        }
        if (i <= 0) {
            this.vp_mDialogVolumeImageView.setImageResource(R.drawable.vp_jz_close_volume);
        } else {
            this.vp_mDialogVolumeImageView.setImageResource(R.drawable.vp_jz_add_volume);
        }
        if (i > 100) {
            i = 100;
        } else if (i < 0) {
            i = 0;
        }
        TextView textView = this.vp_mDialogVolumeTextView;
        textView.setText(i + "%");
        this.vp_mDialogVolumeProgressBar.setProgress(i);
        onCLickUiToggleToClear();
    }

    @Override
    public void vp_dismissVolumeDialog() {
        super.vp_dismissVolumeDialog();
        Dialog dialog = this.vp_mVolumeDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void showBrightnessDialog(int i) {
        super.showBrightnessDialog(i);
        if (this.vp_mBrightnessDialog == null) {
            View inflate = LayoutInflater.from(this.vp_jzvdContext).inflate(R.layout.vp_jz_dialog_brightness, (ViewGroup) null);
            this.vp_mDialogBrightnessTextView = (TextView) inflate.findViewById(R.id.vp_tv_brightness);
            this.vp_mDialogBrightnessProgressBar = (ProgressBar) inflate.findViewById(R.id.vp_brightness_progressbar);
            this.vp_mBrightnessDialog = createDialogWithViewR(inflate);
        }
        if (!this.vp_mBrightnessDialog.isShowing()) {
            this.vp_mBrightnessDialog.show();
        }
        if (i > 100) {
            i = 100;
        } else if (i < 0) {
            i = 0;
        }
        TextView textView = this.vp_mDialogBrightnessTextView;
        textView.setText(i + "%");
        this.vp_mDialogBrightnessProgressBar.setProgress(i);
        onCLickUiToggleToClear();
    }

    @Override
    public void vp_dismissBrightnessDialog() {
        super.vp_dismissBrightnessDialog();
        Dialog dialog = this.vp_mBrightnessDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public Dialog createDialogWithView(View view) {
        Dialog dialog = new Dialog(this.vp_jzvdContext, R.style.jz_style_dialog_progress);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.CENTER;
        window.setAttributes(attributes);
        return dialog;
    }

    public Dialog createDialogWithViewL(View view) {
        Dialog dialog = new Dialog(this.vp_jzvdContext, R.style.jz_style_dialog_progress);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.LEFT;
        window.setAttributes(attributes);
        return dialog;
    }

    public Dialog createDialogWithViewR(View view) {
        Dialog dialog = new Dialog(this.vp_jzvdContext, R.style.jz_style_dialog_progress);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.addFlags(8);
        window.addFlags(32);
        window.addFlags(16);
        window.setLayout(-2, -2);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = Gravity.RIGHT;
        window.setAttributes(attributes);
        return dialog;
    }

    public void vp_startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        VP_DISMISS_CONTROL_VIEW_TIMER = new Timer();
        DismissControlViewTimerTask dismissControlViewTimerTask = new DismissControlViewTimerTask();
        this.vp_mDismissControlViewTimerTask = dismissControlViewTimerTask;
        VP_DISMISS_CONTROL_VIEW_TIMER.schedule(dismissControlViewTimerTask, 2500);
        Log.e("startDismiss", "=======");
    }

    public void cancelDismissControlViewTimer() {
        Timer timer = VP_DISMISS_CONTROL_VIEW_TIMER;
        if (timer != null) {
            timer.cancel();
        }
        DismissControlViewTimerTask dismissControlViewTimerTask = this.vp_mDismissControlViewTimerTask;
        if (dismissControlViewTimerTask != null) {
            dismissControlViewTimerTask.cancel();
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        cancelDismissControlViewTimer();
    }

    @Override
    public void reset() {
        super.reset();
        cancelDismissControlViewTimer();
        unregisterWifiListener(getApplicationContext());
    }

    public void dissmissControlView(final int i) {
        if (this.vp_state != 0 && this.vp_state != 8 && this.vp_state != 7) {
            post(new Runnable() {
                public final void run() {
                    VPVideoStandard.this.lambda$dissmissControlView$4$JzvdStd(i);
                }
            });
        }
    }

    @SuppressLint("WrongConstant")
    public void lambda$dissmissControlView$4$JzvdStd(int i) {
        this.vp_bottomContainer.setVisibility(8);
        this.vp_topContainer.setVisibility(4);
        this.vp_startButton.setVisibility(4);
        this.vp_app_video_n_skip.setVisibility(4);
        this.vp_app_video_p_skip.setVisibility(4);
        this.vp_centerContainer.setVisibility(4);
        if (i == 1) {
            this.vp_speed_layout.setVisibility(8);
            this.vp_videoplaylist_recycler.setVisibility(8);
            this.vp_video_playlist_close.setVisibility(8);
        }
        int i2 = this.vp_screen;
        vp_cancelProgressTimer();
    }

    public void registerWifiListener(Context context) {
        if (context != null) {
            this.vp_mIsWifi = VP_Utils.isWifiConnected(context);
            context.registerReceiver(this.vp_wifiReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.vp_app_video_play.setImageResource(R.drawable.vp_hplib_ic_pause);
            this.vp_startButton.setImageResource(R.drawable.vp_jz_click_pause_selector);
        }
    }

    public void unregisterWifiListener(Context context) {
        if (context != null) {
            try {
                context.unregisterReceiver(this.vp_wifiReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public class DismissControlViewTimerTask extends TimerTask {
        public DismissControlViewTimerTask() {
        }

        public void run() {
            VPVideoStandard.this.dissmissControlView(0);
        }
    }
}
