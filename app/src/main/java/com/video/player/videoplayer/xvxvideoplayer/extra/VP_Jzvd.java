package com.video.player.videoplayer.xvxvideoplayer.extra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.core.widgets.analyzer.BasicMeasure;

import com.video.player.videoplayer.xvxvideoplayer.R;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class VP_Jzvd extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, View.OnTouchListener {
    public static LinkedList<ViewGroup> VP_CONTAINER_LIST = new LinkedList<>();
    public static int VP_FULLSCREEN_ORIENTATION = 6;
    public static int VP_NORMAL_ORIENTATION = 1;
    public static int VP_ON_PLAY_PAUSE_TMP_STATE = 0;
    public static boolean VP_SAVE_PROGRESS = true;
    public static boolean VP_TOOL_BAR_EXIST = true;
    public static int VP_VIDEO_IMAGE_DISPLAY_TYPE = 0;
    public static boolean VP_WIFI_TIP_DIALOG_SHOWED = false;
    public static int VP_backUpBufferState = -1;
    public static VP_Jzvd VP_currentVd = null;
    public static long VP_lastAutoFullscreenTime = 0;
    public static AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int i) {
            if (i == -2) {
                try {
                    VP_Jzvd vd = VP_Jzvd.VP_currentVd;
                    if (vd != null && vd.vp_state == 5) {
                        vd.vp_startButton.performClick();
                        vd.vp_app_video_play.performClick();
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                Log.d("JZVD", "AUDIOFOCUS_LOSS_TRANSIENT [" + hashCode() + "]");
            } else if (i == -1) {
                VP_Jzvd.releaseAllVideos();
                Log.d("JZVD", "AUDIOFOCUS_LOSS [" + hashCode() + "]");
            }
        }
    };
    protected Timer VP_UPDATE_PROGRESS_TIMER;
    public ImageView vp_app_video_n_skip;
    public ImageView vp_app_video_p_skip;
    public ImageView vp_app_video_play;
    protected int vp_aspectRatio = 0;
    protected int vp_blockHeight;
    protected int vp_blockIndex;
    protected ViewGroup.LayoutParams vp_blockLayoutParams;
    protected int vp_blockWidth;
    public ViewGroup vp_bottomContainer;
    public ViewGroup vp_centerContainer;
    public TextView vp_currentTimeTextView;
    public VP_DataSource VPDataSource;
    public ImageView vp_fullscreenButton;
    protected long vp_goBackFullscreenTime = 0;
    protected long vp_gotoFullscreenTime = 0;
    public int vp_heightRatio = 0;
    protected boolean vp_isLockScreen = false;
    protected boolean vp_isLooping = false;
    protected boolean vp_isMute = false;
    protected boolean vp_isShowMoreOption = false;
    protected boolean vp_isShuffle = false;
    protected boolean vp_isSpeed = false;
    protected Context vp_jzvdContext;
    protected AudioManager vp_mAudioManager;
    protected boolean vp_mChangeBrightness;
    protected boolean vp_mChangePosition;
    protected boolean vp_mChangeVolume;
    protected long vp_mCurrentPosition;
    protected float vp_mDownX;
    protected float vp_mDownY;
    protected float vp_mGestureDownBrightness;
    protected long vp_mGestureDownPosition;
    protected int vp_mGestureDownVolume;
    protected ProgressTimerTask vp_mProgressTimerTask;
    protected int vp_mScreenHeight;
    protected int vp_mScreenWidth;
    protected long vp_mSeekTimePosition;
    protected boolean vp_mTouchingProgressBar;
    public VP_MediaInterface VPMediaInterface;
    public Class vp_mediaInterfaceClass;
    public int vp_positionInList = -1;
    public boolean vp_preloading = false;
    public SeekBar vp_progressBar;
    RelativeLayout vp_rlAds;
    public int vp_screen = -1;
    public long vp_seekToInAdvance = 0;
    public int vp_seekToManulPosition = -1;
    public ImageView vp_startButton;
    public int vp_state = -1;
    public VPTextureView1 vp_textureView;
    public ViewGroup vp_textureViewContainer;
    public ViewGroup vp_topContainer;
    public TextView vp_totalTimeTextView;
    public int vp_videoRotation = 0;
    public int vp_widthRatio = 0;
    private int vp_videoWidth, vp_videoHeight;

    public void vp_dismissBrightnessDialog() {
    }

    public void vp_dismissProgressDialog() {
    }

    public void vp_dismissVolumeDialog() {
    }

    public abstract int getLayoutId();

    public void onSeekComplete() {
    }

    public void showBrightnessDialog(int i) {
    }

    public void showProgressDialog(float f, String str, long j, String str2, long j2) {
    }

    public void showVolumeDialog(float f, int i) {
    }

    public void showWifiDialog() {
    }

    public VP_Jzvd(Context context) {
        super(context);
        init(context);
    }

    public VP_Jzvd(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public static void goOnPlayOnResume() {
        VP_Jzvd vd = VP_currentVd;
        if (vd != null) {
            int i = vd.vp_state;
            if (i == 6) {
                if (VP_ON_PLAY_PAUSE_TMP_STATE == 6) {
                    vd.vp_onStatePause();
                    VP_currentVd.VPMediaInterface.pause();
                } else {
                    vd.vp_onStatePlaying();
                    VP_currentVd.VPMediaInterface.start();
                }
                VP_ON_PLAY_PAUSE_TMP_STATE = 0;
            } else if (i == 1) {
                vd.startVideo();
            }
            VP_Jzvd vd2 = VP_currentVd;
            if (vd2.vp_screen == 1) {
                VP_Utils.hideStatusBar(vd2.vp_jzvdContext);
                VP_Utils.hideSystemUI(VP_currentVd.vp_jzvdContext);
            }
        }
    }

    public static void goOnPlayOnPause() {
        VP_Jzvd vd = VP_currentVd;
        if (vd != null) {
            int i = vd.vp_state;
            if (i == 7 || i == 0 || i == 8) {
                releaseAllVideos();
            } else if (i == 1) {
                setVP_currentVd(vd);
                VP_currentVd.vp_state = 1;
            } else {
                VP_ON_PLAY_PAUSE_TMP_STATE = i;
                vd.vp_onStatePause();
                VP_currentVd.VPMediaInterface.pause();
            }
        }
    }

    public static void startFullscreenDirectly(Context context, Class cls, String str, String str2) {
        startFullscreenDirectly(context, cls, new VP_DataSource(str, str2));
    }

    public static void startFullscreenDirectly(Context context, Class cls, VP_DataSource VPDataSource2) {
        VP_Utils.hideStatusBar(context);
        VP_Utils.vp_setRequestedOrientation(context, VP_FULLSCREEN_ORIENTATION);
        VP_Utils.hideSystemUI(context);
        ViewGroup viewGroup = (ViewGroup) VP_Utils.vp_scanForActivity(context).getWindow().getDecorView();
        try {
            VP_Jzvd vd = (VP_Jzvd) cls.getConstructor(Context.class).newInstance(context);
            viewGroup.addView(vd, new LayoutParams(-1, -1));
            vd.setUp(VPDataSource2, 1);
            vd.startVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void releaseAllVideos() {
        VP_Jzvd vd = VP_currentVd;
        if (vd != null) {
            vd.reset();
            VP_currentVd = null;
        }
    }

    public static void setVP_currentVd(VP_Jzvd vd) {
        VP_Jzvd vd2 = VP_currentVd;
        if (vd2 != null) {
            vd2.reset();
        }
        VP_currentVd = vd;
    }

    public static void setTextureViewRotation(int i) {
        VPTextureView1 exTextureView1;
        VP_Jzvd vd = VP_currentVd;
        if (vd != null && (exTextureView1 = vd.vp_textureView) != null) {
            exTextureView1.setRotation((float) i);
        }
    }

    public static void vp_setVpVideoImageDisplayType(int i) {
        VPTextureView1 exTextureView1;
        VP_VIDEO_IMAGE_DISPLAY_TYPE = i;
        VP_Jzvd vd = VP_currentVd;
        if (vd != null && (exTextureView1 = vd.vp_textureView) != null) {
            exTextureView1.requestLayout();
        }
    }

    public void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        this.vp_jzvdContext = context;
        this.vp_rlAds = (RelativeLayout) findViewById(R.id.vp_rl_ad);
        this.vp_startButton = (ImageView) findViewById(R.id.vp_start);
        this.vp_app_video_play = (ImageView) findViewById(R.id.vp_app_video_play);
        this.vp_fullscreenButton = (ImageView) findViewById(R.id.vp_fullscreen);
        this.vp_app_video_p_skip = (ImageView) findViewById(R.id.vp_app_video_p_skip);
        this.vp_app_video_n_skip = (ImageView) findViewById(R.id.vp_app_video_n_skip);
        this.vp_progressBar = (SeekBar) findViewById(R.id.vp_bottom_seek_progress);
        this.vp_currentTimeTextView = (TextView) findViewById(R.id.vp_current);
        this.vp_totalTimeTextView = (TextView) findViewById(R.id.vp_total);
        this.vp_bottomContainer = (ViewGroup) findViewById(R.id.vp_layout_bottom);
        this.vp_centerContainer = (ViewGroup) findViewById(R.id.vp_layout_center);
        this.vp_textureViewContainer = (ViewGroup) findViewById(R.id.vp_surface_container);
        this.vp_topContainer = (ViewGroup) findViewById(R.id.vp_layout_top);
        if (this.vp_startButton == null) {
            this.vp_startButton = new ImageView(context);
        }
        if (this.vp_fullscreenButton == null) {
            this.vp_fullscreenButton = new ImageView(context);
        }
        if (this.vp_progressBar == null) {
            this.vp_progressBar = new SeekBar(context);
        }
        if (this.vp_currentTimeTextView == null) {
            this.vp_currentTimeTextView = new TextView(context);
        }
        if (this.vp_totalTimeTextView == null) {
            this.vp_totalTimeTextView = new TextView(context);
        }
        if (this.vp_bottomContainer == null) {
            this.vp_bottomContainer = new LinearLayout(context);
        }
        if (this.vp_textureViewContainer == null) {
            this.vp_textureViewContainer = new FrameLayout(context);
        }
        if (this.vp_topContainer == null) {
            this.vp_topContainer = new RelativeLayout(context);
        }
        if (this.vp_centerContainer == null) {
            this.vp_centerContainer = new RelativeLayout(context);
        }
        this.vp_startButton.setOnClickListener(this);
        this.vp_app_video_play.setOnClickListener(this);
        this.vp_fullscreenButton.setOnClickListener(this);
        this.vp_app_video_n_skip.setOnClickListener(this);
        this.vp_app_video_p_skip.setOnClickListener(this);
        this.vp_progressBar.setOnSeekBarChangeListener(this);
        this.vp_bottomContainer.setOnClickListener(this);
        this.vp_textureViewContainer.setOnClickListener(this);
        this.vp_textureViewContainer.setOnTouchListener(this);
        this.vp_mScreenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        this.vp_mScreenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        this.vp_state = -1;
    }

    public void setUp(String str, String str2) {
        setUp(new VP_DataSource(str, str2), 0);
    }

    public void setUp(String str, String str2, int i) {
        setUp(new VP_DataSource(str, str2), i);
    }

    public void setUp(VP_DataSource VPDataSource2, int i) {
        setUp(VPDataSource2, i, VPMediaSystem.class);
    }

    public void setUp(String str, String str2, int i, Class cls) {
        setUp(new VP_DataSource(str, str2), i, cls);
    }

    public void setUp(VP_DataSource VPDataSource2, int i, Class cls) {
        this.VPDataSource = VPDataSource2;
        this.vp_screen = i;
        vp_onStateNormal();
        this.vp_mediaInterfaceClass = cls;
    }

    public void setMediaInterface(Class cls) {
        reset();
        this.vp_mediaInterfaceClass = cls;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.vp_start || id == R.id.vp_app_video_play) {
            clickStart();
        } else if (id == R.id.vp_fullscreen) {
            clickFullscreen();
        }
    }

    @SuppressLint("WrongConstant")
    public void backPress() {
        VP_Jzvd vd;
        VP_Jzvd vd2;
        Log.i("JZVD", "backPress");
        if (VP_CONTAINER_LIST.size() != 0 && (vd2 = VP_currentVd) != null) {
            this.vp_rlAds.setVisibility(8);
//            vd2.gotoFullscreen();
            vd2.gotoAutoScreen(VP_FULLSCREEN_ORIENTATION);
        } else if (VP_CONTAINER_LIST.size() != 0 || (vd = VP_currentVd) == null || vd.vp_screen == 0) {
        } else {
            vd.vp_clearFloatScreen();
        }
    }

    public void clickFullscreen() {
        Log.i("JZVD", "onClick fullscreen [" + hashCode() + "] ");
        if (this.vp_state == 7) {
            return;
        }
//        if (videoWidth < videoHeight) {
//            screen = 0;
////                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            textureView.setRotation((float) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        } else {
//            screen = 1;
////                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            textureView.setRotation((float) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
        if (this.vp_screen == 1) {
            gotoAutoScreen(VP_FULLSCREEN_ORIENTATION);
        } else {
            gotoAutoScreen(VP_NORMAL_ORIENTATION);
        }
    }

    public void gotoAutoScreen(int orientation) {
        if (orientation == VP_FULLSCREEN_ORIENTATION) {
            vp_state = 0;
            vp_setScreenNormal();
            this.vp_gotoFullscreenTime = System.currentTimeMillis();
        } else {
            vp_state = 1;
            setScreenFullscreen();
            vp_goBackFullscreenTime = System.currentTimeMillis();
        }

        ViewGroup parent = (ViewGroup) getParent();
        this.vp_blockLayoutParams = getLayoutParams();
        this.vp_blockIndex = parent.indexOfChild(this);
        this.vp_blockWidth = getWidth();
        this.vp_blockHeight = getHeight();
        parent.removeView(this);
        cloneAJzvd(parent);
        VP_CONTAINER_LIST.add(parent);
        ((ViewGroup) VP_Utils.vp_scanForActivity(this.vp_jzvdContext).getWindow().getDecorView())
                .addView(this, new LayoutParams(-1, -1));
        VP_Utils.hideStatusBar(this.vp_jzvdContext);
        VP_Utils.vp_setRequestedOrientation(this.vp_jzvdContext, orientation);
        VP_Utils.hideSystemUI(this.vp_jzvdContext);
    }

    public void gotoNormalScreen() {
        this.vp_gotoFullscreenTime = System.currentTimeMillis();
        ViewGroup viewGroup = (ViewGroup) getParent();
        this.vp_blockLayoutParams = getLayoutParams();
        this.vp_blockIndex = viewGroup.indexOfChild(this);
        this.vp_blockWidth = getWidth();
        this.vp_blockHeight = getHeight();
        viewGroup.removeView(this);
        cloneAJzvd(viewGroup);
        VP_CONTAINER_LIST.add(viewGroup);
        ((ViewGroup) VP_Utils.vp_scanForActivity(this.vp_jzvdContext).getWindow().getDecorView())
                .addView(this, new LayoutParams(-1, -1));
        VP_Utils.hideStatusBar(this.vp_jzvdContext);
        VP_Utils.vp_setRequestedOrientation(this.vp_jzvdContext, VP_NORMAL_ORIENTATION);
        VP_Utils.hideSystemUI(this.vp_jzvdContext);
    }

    public void gotoFullscreen() {
        vp_goBackFullscreenTime = System.currentTimeMillis();
        ((ViewGroup) VP_Utils.vp_scanForActivity(this.vp_jzvdContext).getWindow().getDecorView()).removeView(this);
        LinkedList<ViewGroup> linkedList = VP_CONTAINER_LIST;
        if (!(linkedList == null || linkedList.getLast() == null)) {
            VP_CONTAINER_LIST.getLast().removeViewAt(this.vp_blockIndex);
            VP_CONTAINER_LIST.getLast().addView(this, this.vp_blockIndex, this.vp_blockLayoutParams);
            VP_CONTAINER_LIST.pop();
        }
        vp_setScreenNormal();
        VP_Utils.hideStatusBar(this.vp_jzvdContext);
        VP_Utils.vp_setRequestedOrientation(this.vp_jzvdContext, VP_FULLSCREEN_ORIENTATION);
        VP_Utils.hideSystemUI(this.vp_jzvdContext);
    }

    @SuppressLint("WrongConstant")
    public void clickStart() {
        Log.i("JZVD", "onClick start [" + hashCode() + "] ");
        VP_DataSource VPDataSource2 = this.VPDataSource;
        if (VPDataSource2 == null || VPDataSource2.vp_urlsMap.isEmpty() || this.VPDataSource.getCurrentUrl() == null) {
            Toast.makeText(getContext(), getResources().getString(R.string.no_url), 0).show();
            return;
        }
        int i = this.vp_state;
        if (i == 0) {
            if (this.VPDataSource.getCurrentUrl().toString().startsWith("file") || this.VPDataSource.getCurrentUrl().toString().startsWith("/") || VP_Utils.isWifiConnected(getContext()) || VP_WIFI_TIP_DIALOG_SHOWED) {
                startVideo();
            } else {
                showWifiDialog();
            }
        } else if (i == 5) {
            Log.e("JZVD", "pauseVideo [" + hashCode() + "] ");
            this.VPMediaInterface.pause();
            vp_onStatePause();
            if (1 == this.vp_screen) {
                this.vp_startButton.setImageResource(R.drawable.vp_jz_click_play_selector);
                this.vp_app_video_play.setImageResource(R.drawable.vp_hplib_ic_play_download);
                this.vp_rlAds.setVisibility(0);
            }
        } else if (i == 6) {
            Log.e("JZVD", "playVideo [" + hashCode() + "] ");
            this.VPMediaInterface.start();
            vp_onStatePlaying();
            if (1 == this.vp_screen) {
                this.vp_app_video_play.setImageResource(R.drawable.vp_hplib_ic_pause);
                this.vp_startButton.setImageResource(R.drawable.vp_jz_click_pause_selector);
                this.vp_rlAds.setVisibility(8);
            }
        } else if (i == 7) {
            startVideo();
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        if (view.getId() != R.id.vp_surface_container) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            touchActionDown(x, y);
            return false;
        } else if (action == 1) {
            touchActionUp();
            return false;
        } else if (action != 2) {
            return false;
        } else {
            vp_touchActionMove(x, y);
            return false;
        }
    }

    public void touchActionUp() {
        Log.i("JZVD", "onTouch surfaceContainer actionUp [" + hashCode() + "] ");
        this.vp_mTouchingProgressBar = false;
        vp_dismissProgressDialog();
        vp_dismissVolumeDialog();
        vp_dismissBrightnessDialog();
        if (this.vp_mChangePosition) {
            this.VPMediaInterface.seekTo(this.vp_mSeekTimePosition);
            long vp_duration = getDuration();
            long j = this.vp_mSeekTimePosition * 100;
            if (vp_duration == 0) {
                vp_duration = 1;
            }
            this.vp_progressBar.setProgress((int) (j / vp_duration));
        }
        startProgressTimer();
    }

    public void vp_touchActionMove(float f, float f2) {
        Log.i("JZVD", "onTouch surfaceContainer actionMove [" + hashCode() + "] ");
        float vp_f3 = f - this.vp_mDownX;
        float vp_f4 = f2 - this.vp_mDownY;
        float abs = Math.abs(vp_f3);
        float abs2 = Math.abs(vp_f4);
        int vp_i = this.vp_screen;
        if (vp_i != 1 || this.vp_isLockScreen) {
            if (vp_i == 0 && !this.vp_isLockScreen) {
                if (this.vp_mDownX <= ((float) VP_Utils.vp_getScreenWidth(getContext())) && this.vp_mDownY >= ((float) VP_Utils.vp_getStatusBarHeight(getContext()))) {
                    if (!this.vp_mChangePosition && !this.vp_mChangeVolume && !this.vp_mChangeBrightness && (abs > 80.0f || abs2 > 80.0f)) {
                        vp_cancelProgressTimer();
                        if (abs >= 80.0f) {
                            if (this.vp_state != 8) {
                                this.vp_mChangePosition = true;
                                this.vp_mGestureDownPosition = getCurrentPositionWhenPlaying();
                            }
                        } else if (this.vp_mDownX < ((float) this.vp_mScreenWidth) * 0.5f) {
                            this.vp_mChangeBrightness = true;
                            WindowManager.LayoutParams attributes = VP_Utils.vp_getWindow(getContext()).getAttributes();
                            if (attributes.screenBrightness < 0.0f) {
                                try {
                                    this.vp_mGestureDownBrightness = (float) Settings.System.getInt(getContext().getContentResolver(), "screen_brightness");
                                    Log.i("JZVD", "current system brightness: " + this.vp_mGestureDownBrightness);
                                } catch (Settings.SettingNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                this.vp_mGestureDownBrightness = attributes.screenBrightness * 255.0f;
                                Log.i("JZVD", "current activity brightness: " + this.vp_mGestureDownBrightness);
                            }
                        } else {
                            this.vp_mChangeVolume = true;
                            this.vp_mGestureDownVolume = this.vp_mAudioManager.getStreamVolume(3);
                        }
                    }
                } else {
                    return;
                }
            }
        } else if (this.vp_mDownX <= ((float) VP_Utils.vp_getScreenWidth(getContext())) && this.vp_mDownY >= ((float) VP_Utils.vp_getStatusBarHeight(getContext()))) {
            if (!this.vp_mChangePosition && !this.vp_mChangeVolume && !this.vp_mChangeBrightness && (abs > 80.0f || abs2 > 80.0f)) {
                vp_cancelProgressTimer();
                if (abs >= 80.0f) {
                    if (this.vp_state != 8) {
                        this.vp_mChangePosition = true;
                        this.vp_mGestureDownPosition = getCurrentPositionWhenPlaying();
                    }
                } else if (this.vp_mDownX < ((float) this.vp_mScreenHeight) * 0.5f) {
                    this.vp_mChangeBrightness = true;
                    WindowManager.LayoutParams attributes2 = VP_Utils.vp_getWindow(getContext()).getAttributes();
                    if (attributes2.screenBrightness < 0.0f) {
                        try {
                            this.vp_mGestureDownBrightness = (float) Settings.System.getInt(getContext().getContentResolver(), "screen_brightness");
                            Log.i("JZVD", "current system brightness: " + this.vp_mGestureDownBrightness);
                        } catch (Settings.SettingNotFoundException e2) {
                            e2.printStackTrace();
                        }
                    } else {
                        this.vp_mGestureDownBrightness = attributes2.screenBrightness * 255.0f;
                        Log.i("JZVD", "current activity brightness: " + this.vp_mGestureDownBrightness);
                    }
                } else {
                    this.vp_mChangeVolume = true;
                    this.vp_mGestureDownVolume = this.vp_mAudioManager.getStreamVolume(3);
                }
            }
        } else {
            return;
        }
        if (this.vp_mChangePosition) {
            long duration = getDuration();
            long j = (long) ((int) (((float) this.vp_mGestureDownPosition) + ((((float) duration) * vp_f3) / ((float) this.vp_mScreenWidth))));
            this.vp_mSeekTimePosition = j;
            if (j > duration) {
                this.vp_mSeekTimePosition = duration;
            }
            showProgressDialog(vp_f3, VP_Utils.vp_stringForTime(this.vp_mSeekTimePosition), this.vp_mSeekTimePosition, VP_Utils.vp_stringForTime(duration), duration);
        }
        if (this.vp_mChangeVolume) {
            vp_f4 = -vp_f4;
            int streamMaxVolume = this.vp_mAudioManager.getStreamMaxVolume(3);
            this.vp_mAudioManager.setStreamVolume(3, this.vp_mGestureDownVolume + ((int) (((((float) streamMaxVolume) * vp_f4) * 3.0f) / ((float) this.vp_mScreenHeight))), 0);
            showVolumeDialog(-vp_f4, (int) (((float) ((this.vp_mGestureDownVolume * 100) / streamMaxVolume)) + (((vp_f4 * 3.0f) * 100.0f) / ((float) this.vp_mScreenHeight))));
        }
        if (this.vp_mChangeBrightness) {
            float f5 = -vp_f4;
            WindowManager.LayoutParams attributes3 = VP_Utils.vp_getWindow(getContext()).getAttributes();
            float f6 = (this.vp_mGestureDownBrightness + ((float) ((int) (((f5 * 255.0f) * 3.0f) / ((float) this.vp_mScreenHeight))))) / 255.0f;
            if (f6 >= 1.0f) {
                attributes3.screenBrightness = 1.0f;
            } else if (f6 <= 0.0f) {
                attributes3.screenBrightness = 0.01f;
            } else {
                attributes3.screenBrightness = f6;
            }
            VP_Utils.vp_getWindow(getContext()).setAttributes(attributes3);
            showBrightnessDialog((int) (((this.vp_mGestureDownBrightness * 100.0f) / 255.0f) + (((f5 * 3.0f) * 100.0f) / ((float) this.vp_mScreenHeight))));
        }
    }

    public void touchActionDown(float f, float f2) {
        Log.i("JZVD", "onTouch surfaceContainer actionDown [" + hashCode() + "] ");
        this.vp_mTouchingProgressBar = true;
        this.vp_mDownX = f;
        this.vp_mDownY = f2;
        this.vp_mChangeVolume = false;
        this.vp_mChangePosition = false;
        this.vp_mChangeBrightness = false;
    }

    public void vp_onStateNormal() {
        Log.i("JZVD", "onStateNormal  [" + hashCode() + "] ");
        this.vp_state = 0;
        vp_cancelProgressTimer();
        VP_MediaInterface VPMediaInterface2 = this.VPMediaInterface;
        if (VPMediaInterface2 != null) {
            VPMediaInterface2.release();
        }
    }

    public void vp_onStatePreparing() {
        Log.i("JZVD", "onStatePreparing  [" + hashCode() + "] ");
        this.vp_state = 1;
        resetProgressAndTime();
    }

    public void vp_onStatePreparingPlaying() {
        Log.i("JZVD", "onStatePreparingPlaying  [" + hashCode() + "] ");
        this.vp_state = 3;
    }

    public void vp_onStatePreparingChangeUrl() {
        Log.i("JZVD", "onStatePreparingChangeUrl  [" + hashCode() + "] ");
        this.vp_state = 2;
        releaseAllVideos();
        startVideo();
    }

    public void changeUrl(VP_DataSource VPDataSource2, long j) {
        this.VPDataSource = VPDataSource2;
        this.vp_seekToInAdvance = j;
        vp_onStatePreparingChangeUrl();
    }

    public void onPrepared() {
        Log.i("JZVD", "onPrepared  [" + hashCode() + "] ");
        this.vp_state = 4;
        if (!this.vp_preloading) {
            this.VPMediaInterface.start();
            this.vp_preloading = false;
        }
        if (this.VPDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3") || this.VPDataSource.getCurrentUrl().toString().toLowerCase().contains("wma") || this.VPDataSource.getCurrentUrl().toString().toLowerCase().contains("aac") || this.VPDataSource.getCurrentUrl().toString().toLowerCase().contains("m4a") || this.VPDataSource.getCurrentUrl().toString().toLowerCase().contains("wav")) {
            vp_onStatePlaying();
        }
    }

    public void startPreloading() {
        this.vp_preloading = true;
        startVideo();
    }

    public void startVideoAfterPreloading() {
        if (this.vp_state == 4) {
            this.VPMediaInterface.start();
            return;
        }
        this.vp_preloading = false;
        startVideo();
    }

    public void vp_onStatePlaying() {
        Log.i("JZVD", "onStatePlaying  [" + hashCode() + "] ");
        if (this.vp_state == 4) {
            long j = this.vp_seekToInAdvance;
            if (j != 0) {
                this.VPMediaInterface.seekTo(j);
                this.vp_seekToInAdvance = 0;
            } else {
                long savedProgress = VP_Utils.vp_getSavedProgress(getContext(), this.VPDataSource.getCurrentUrl());
                if (savedProgress != 0) {
                    this.VPMediaInterface.seekTo(savedProgress);
                }
            }
        }
        this.vp_state = 5;
        startProgressTimer();
    }

    public void vp_onStatePause() {
        Log.i("JZVD", "onStatePause  [" + hashCode() + "] ");
        this.vp_state = 6;
        startProgressTimer();
    }

    public void vp_onStateError() {
        Log.i("JZVD", "onStateError  [" + hashCode() + "] ");
        this.vp_state = 8;
        vp_cancelProgressTimer();
    }

    public void vp_onStateAutoComplete() {
        Log.i("JZVD", "onStateAutoComplete  [" + hashCode() + "] ");
        this.vp_state = 7;
        vp_cancelProgressTimer();
        this.vp_progressBar.setProgress(100);
        this.vp_currentTimeTextView.setText(this.vp_totalTimeTextView.getText());
    }

    public void onInfo(int i, int i2) {
        Log.d("JZVD", "onInfo what - " + i + " Extra - " + i2);
        if (i == 3) {
            Log.d("JZVD", "MEDIA_INFO_VIDEO_RENDERING_START");
            int i3 = this.vp_state;
            if (i3 == 4 || i3 == 2 || i3 == 3) {
                vp_onStatePlaying();
            }
        } else if (i == 701) {
            Log.d("JZVD", "MEDIA_INFO_BUFFERING_START");
            VP_backUpBufferState = this.vp_state;
            setVp_state(3);
        } else if (i == 702) {
            Log.d("JZVD", "MEDIA_INFO_BUFFERING_END");
            int i4 = VP_backUpBufferState;
            if (i4 != -1) {
                setVp_state(i4);
                VP_backUpBufferState = -1;
            }
        }
    }

    public void onError(int i, int i2) {
        Log.e("JZVD", "onError " + i + " - " + i2 + " [" + hashCode() + "] ");
        if (i != 38 && i2 != -38 && i != -38 && i2 != 38 && i2 != -19) {
            vp_onStateError();
            this.VPMediaInterface.release();
        }
    }

    public void onCompletion() {
        Runtime.getRuntime().gc();
        Log.i("JZVD", "onAutoCompletion  [" + hashCode() + "] ");
        vp_cancelProgressTimer();
        vp_dismissBrightnessDialog();
        vp_dismissProgressDialog();
        vp_dismissVolumeDialog();
        vp_onStateAutoComplete();
        this.VPMediaInterface.release();
        VP_Utils.vp_scanForActivity(getContext()).getWindow().clearFlags(128);
        VP_Utils.vp_saveProgress(getContext(), this.VPDataSource.getCurrentUrl(), 0);
        if (this.vp_screen == 1) {
            if (VP_CONTAINER_LIST.size() == 0) {
                vp_clearFloatScreen();
            } else {
                vp_gotoNormalCompletion();
            }
        }
    }

    public void vp_gotoNormalCompletion() {
        this.vp_goBackFullscreenTime = System.currentTimeMillis();
        ((ViewGroup) VP_Utils.vp_scanForActivity(this.vp_jzvdContext).getWindow().getDecorView()).removeView(this);
        this.vp_textureViewContainer.removeView(this.vp_textureView);
        VP_CONTAINER_LIST.getLast().removeViewAt(this.vp_blockIndex);
        VP_CONTAINER_LIST.getLast().addView(this, this.vp_blockIndex, this.vp_blockLayoutParams);
        VP_CONTAINER_LIST.pop();
        vp_setScreenNormal();
        VP_Utils.showStatusBar(this.vp_jzvdContext);
        VP_Utils.vp_setRequestedOrientation(this.vp_jzvdContext, VP_NORMAL_ORIENTATION);
        VP_Utils.vp_showSystemUI(this.vp_jzvdContext);
    }

    @SuppressLint("WrongConstant")
    public void reset() {
        Log.i("JZVD", "reset  [" + hashCode() + "] ");
        int i = this.vp_state;
        if (i == 5 || i == 6) {
            VP_Utils.vp_saveProgress(getContext(), this.VPDataSource.getCurrentUrl(), getCurrentPositionWhenPlaying());
        }
        vp_cancelProgressTimer();
        vp_dismissBrightnessDialog();
        vp_dismissProgressDialog();
        vp_dismissVolumeDialog();
        vp_onStateNormal();
        this.vp_textureViewContainer.removeAllViews();
        ((AudioManager) getApplicationContext().getSystemService("audio")).abandonAudioFocus(onAudioFocusChangeListener);
        VP_Utils.vp_scanForActivity(getContext()).getWindow().clearFlags(128);
        VP_MediaInterface VPMediaInterface2 = this.VPMediaInterface;
        if (VPMediaInterface2 != null) {
            VPMediaInterface2.release();
        }
    }

    public void setVp_state(int i) {
        switch (i) {
            case 0:
                vp_onStateNormal();
                return;
            case 1:
                vp_onStatePreparing();
                return;
            case 2:
                vp_onStatePreparingChangeUrl();
                return;
            case 3:
                vp_onStatePreparingPlaying();
                return;
            case 4:
            default:
                return;
            case 5:
                vp_onStatePlaying();
                return;
            case 6:
                vp_onStatePause();
                return;
            case 7:
                vp_onStateAutoComplete();
                return;
            case 8:
                vp_onStateError();
                return;
        }
    }

    public void setVp_screen(int i) {
        if (i == 0) {
            vp_setScreenNormal();
        } else if (i == 1) {
            setScreenFullscreen();
        } else if (i == 2) {
            setScreenTiny();
        }
    }

    public void startVideo() {
        Log.d("JZVD", "startVideo [" + hashCode() + "] ");
        setVP_currentVd(this);
        try {
            this.VPMediaInterface = (VP_MediaInterface) this.vp_mediaInterfaceClass.getConstructor(VP_Jzvd.class).newInstance(this);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        addTextureView();
        @SuppressLint("WrongConstant")
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService("audio");
        this.vp_mAudioManager = audioManager;
        audioManager.requestAudioFocus(onAudioFocusChangeListener, 3, 2);
        VP_Utils.vp_scanForActivity(getContext()).getWindow().addFlags(128);
        vp_onStatePreparing();
        if (1 == this.vp_screen) {
            this.vp_app_video_play.setImageResource(R.drawable.vp_hplib_ic_pause);
            this.vp_startButton.setImageResource(R.drawable.vp_jz_click_pause_selector);
        }
    }

    public void muteAudio() {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                this.vp_mAudioManager.adjustStreamVolume(4, -100, 0);
                this.vp_mAudioManager.adjustStreamVolume(3, -100, 0);
                this.vp_mAudioManager.adjustStreamVolume(2, -100, 0);
                this.vp_mAudioManager.adjustStreamVolume(1, -100, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.vp_mAudioManager.setStreamMute(5, true);
                this.vp_mAudioManager.setStreamMute(4, true);
                this.vp_mAudioManager.setStreamMute(3, true);
                this.vp_mAudioManager.setStreamMute(2, true);
                this.vp_mAudioManager.setStreamMute(1, true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void unMuteAudio() {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                this.vp_mAudioManager.adjustStreamVolume(5, 100, 0);
                this.vp_mAudioManager.adjustStreamVolume(4, 100, 0);
                this.vp_mAudioManager.adjustStreamVolume(3, 100, 0);
                this.vp_mAudioManager.adjustStreamVolume(2, 100, 0);
                this.vp_mAudioManager.adjustStreamVolume(1, 100, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.vp_mAudioManager.setStreamMute(5, false);
                this.vp_mAudioManager.setStreamMute(4, false);
                this.vp_mAudioManager.setStreamMute(3, false);
                this.vp_mAudioManager.setStreamMute(2, false);
                this.vp_mAudioManager.setStreamMute(1, false);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void onMeasure(int i, int i2) {
        int i3 = this.vp_screen;
        if (i3 == 1 || i3 == 2) {
            super.onMeasure(i, i2);
        } else if (this.vp_widthRatio == 0 || this.vp_heightRatio == 0) {
            super.onMeasure(i, i2);
        } else {
            Log.d("CheckOri", "");
            int size = MeasureSpec.getSize(i);
            int i4 = (int) ((((float) size) * ((float) this.vp_heightRatio)) / ((float) this.vp_widthRatio));
            setMeasuredDimension(size, i4);
            getChildAt(0).measure(MeasureSpec.makeMeasureSpec(size, BasicMeasure.EXACTLY),
                    MeasureSpec.makeMeasureSpec(i4, BasicMeasure.EXACTLY));
        }
    }

    public void addTextureView() {
        Log.d("JZVD", "addTextureView [" + hashCode() + "] ");
        VPTextureView1 exTextureView1 = this.vp_textureView;
        if (exTextureView1 != null) {
            this.vp_textureViewContainer.removeView(exTextureView1);
        }
        VPTextureView1 exTextureView12 = new VPTextureView1(getContext().getApplicationContext());
        this.vp_textureView = exTextureView12;
        exTextureView12.setSurfaceTextureListener(this.VPMediaInterface);
        this.vp_textureViewContainer.addView(this.vp_textureView, new LayoutParams(-1, -1, 17));
    }

    public void vp_clearFloatScreen() {
        VP_Utils.showStatusBar(getContext());
        VP_Utils.vp_setRequestedOrientation(getContext(), VP_NORMAL_ORIENTATION);
        VP_Utils.vp_showSystemUI(getContext());
        ((ViewGroup) VP_Utils.vp_scanForActivity(getContext()).getWindow().getDecorView()).removeView(this);
        VP_MediaInterface VPMediaInterface2 = this.VPMediaInterface;
        if (VPMediaInterface2 != null) {
            VPMediaInterface2.release();
        }
        VP_currentVd = null;
    }


    public void onVideoSizeChanged(int width, int height) {
        Log.i("JZVD", "onVideoSizeChanged  [" + hashCode() + "] ");
        vp_videoWidth = width;
        vp_videoHeight = height;
        VPTextureView1 exTextureView1 = this.vp_textureView;
        if (exTextureView1 != null) {
            int i3 = this.vp_videoRotation;
            int orientation = -1;
            if (width < height) {
                orientation = 0;
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                exTextureView1.setRotation((float) ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                orientation = 1;
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                exTextureView1.setRotation((float) ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
            Log.d("CheckOri", "");
//            if (i3 != 0) {
//                textureView1.setRotation((float) i3);
//            }
            this.vp_textureView.setVideoSize(width, height);
        }
    }

    public void startProgressTimer() {
        Log.i("JZVD", "startProgressTimer:  [" + hashCode() + "] ");
        vp_cancelProgressTimer();
        this.VP_UPDATE_PROGRESS_TIMER = new Timer();
        ProgressTimerTask progressTimerTask = new ProgressTimerTask();
        this.vp_mProgressTimerTask = progressTimerTask;
        this.VP_UPDATE_PROGRESS_TIMER.schedule(progressTimerTask, 0, 300);
    }

    public void vp_cancelProgressTimer() {
        Timer timer = this.VP_UPDATE_PROGRESS_TIMER;
        if (timer != null) {
            timer.cancel();
        }
        ProgressTimerTask progressTimerTask = this.vp_mProgressTimerTask;
        if (progressTimerTask != null) {
            progressTimerTask.cancel();
        }
    }

    public void onProgress(int i, long j, long j2) {
        this.vp_mCurrentPosition = j;
        if (!this.vp_mTouchingProgressBar) {
            int i2 = this.vp_seekToManulPosition;
            if (i2 != -1) {
                if (i2 <= i) {
                    this.vp_seekToManulPosition = -1;
                } else {
                    return;
                }
            } else if (i != 0) {
                this.vp_progressBar.setProgress(i);
            }
        }
        if (j != 0) {
            this.vp_currentTimeTextView.setText(VP_Utils.vp_stringForTime(j));
        }
        this.vp_totalTimeTextView.setText(VP_Utils.vp_stringForTime(j2));
    }

    public void setBufferProgress(int i) {
        if (i != 0) {
            this.vp_progressBar.setSecondaryProgress(i);
        }
    }

    public void resetProgressAndTime() {
        vp_mCurrentPosition = 0;
        vp_progressBar.setProgress(0);
        vp_progressBar.setSecondaryProgress(0);
        vp_currentTimeTextView.setText(VP_Utils.vp_stringForTime(0));
        vp_totalTimeTextView.setText(VP_Utils.vp_stringForTime(0));
    }

    public long getCurrentPositionWhenPlaying() {
        int i = this.vp_state;
        if (i != 5 && i != 6 && i != 3) {
            return 0;
        }
        try {
            return this.VPMediaInterface.getCurrentPosition();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getCurrentDuration() {
        return this.VPMediaInterface.getCurrentPosition();
    }

    public void onPause() {
        this.VPMediaInterface.pause();
    }

    public void setSeekTo(int i) {
        VP_MediaInterface VPMediaInterface2 = this.VPMediaInterface;
        if (VPMediaInterface2 != null) {
            VPMediaInterface2.seekTo((long) i);
        }
    }

    public void onStart() {
        VP_MediaInterface VPMediaInterface2 = this.VPMediaInterface;
        if (VPMediaInterface2 != null) {
            VPMediaInterface2.start();
        }
    }

    public long getDuration() {
        try {
            return this.VPMediaInterface.getDuration();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.i("JZVD", "bottomProgress onStartTrackingTouch [" + hashCode() + "] ");
        vp_cancelProgressTimer();
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.i("JZVD", "bottomProgress onStopTrackingTouch [" + hashCode() + "] ");
        startProgressTimer();
        for (ViewParent parent = getParent(); parent != null; parent = parent.getParent()) {
            parent.requestDisallowInterceptTouchEvent(false);
        }
        int i = this.vp_state;
        if (i == 5 || i == 6) {
            long vp_progress = (((long) seekBar.getProgress()) * getDuration()) / 100;
            this.vp_seekToManulPosition = seekBar.getProgress();
            this.VPMediaInterface.seekTo(vp_progress);
            Log.i("JZVD", "seekTo " + vp_progress + " [" + hashCode() + "] ");
        }
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        if (z) {
            this.vp_currentTimeTextView.setText(VP_Utils.vp_stringForTime((((long) i) * getDuration()) / 100));
        }
    }

    public void cloneAJzvd(ViewGroup viewGroup) {
        try {
            VP_Jzvd vd = (VP_Jzvd) getClass().getConstructor(Context.class).newInstance(getContext());
            vd.setId(getId());
            vd.setMinimumWidth(this.vp_blockWidth);
            vd.setMinimumHeight(this.vp_blockHeight);
            viewGroup.addView(vd, this.vp_blockIndex, this.vp_blockLayoutParams);
            vd.setUp(this.VPDataSource.vp_cloneMe(), 0, this.vp_mediaInterfaceClass);
        } catch (IllegalAccessException | NoSuchMethodException |
                InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void vp_setScreenNormal() {
        this.vp_screen = 0;
    }

    public void setScreenFullscreen() {
        this.vp_screen = 1;
    }

    public void setScreenTiny() {
        this.vp_screen = 2;
    }

    public void autoFullscreen(float f) {
        int i;
        if (VP_currentVd != null) {
            int i2 = this.vp_state;
            if ((i2 == 5 || i2 == 6) && (i = this.vp_screen) != 1 && i != 2) {
                if (f > 0.0f) {
                    VP_Utils.vp_setRequestedOrientation(getContext(), 0);
                } else {
                    VP_Utils.vp_setRequestedOrientation(getContext(), 8);
                }
//                gotoNormalScreen();
                gotoAutoScreen(VP_NORMAL_ORIENTATION);
            }
        }
    }

    public void autoQuitFullscreen() {
        if (System.currentTimeMillis() - VP_lastAutoFullscreenTime > 2000 && this.vp_state == 5 && this.vp_screen == 1) {
            VP_lastAutoFullscreenTime = System.currentTimeMillis();
            backPress();
        }
    }

    public Context getApplicationContext() {
        Context applicationContext;
        Context context = getContext();
        return (context == null || (applicationContext = context.getApplicationContext()) == null) ? context : applicationContext;
    }

    public static class JZAutoFullscreenListener implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int i) {
        }

        public void onSensorChanged(SensorEvent sensorEvent) {
            float f = sensorEvent.values[0];
            if ((f < -12.0f || f > 12.0f) && System.currentTimeMillis() - VP_Jzvd.VP_lastAutoFullscreenTime > 2000) {
                if (VP_Jzvd.VP_currentVd != null) {
                    VP_Jzvd.VP_currentVd.autoFullscreen(f);
                }
                VP_Jzvd.VP_lastAutoFullscreenTime = System.currentTimeMillis();
            }
        }
    }

    public class ProgressTimerTask extends TimerTask {

        public void run() {
            if (VP_Jzvd.this.vp_state == 5 || VP_Jzvd.this.vp_state == 6 || VP_Jzvd.this.vp_state == 3) {
                VP_Jzvd.this.post(new Runnable() {
                    public final void run() {
                        long currentPositionWhenPlaying = VP_Jzvd.this.getCurrentPositionWhenPlaying();
                        long duration = VP_Jzvd.this.getDuration();
                        VP_Jzvd.this.onProgress((int) ((100 * currentPositionWhenPlaying) / (duration == 0 ? 1 : duration)), currentPositionWhenPlaying, duration);

                    }
                });
            }
        }
    }
}
