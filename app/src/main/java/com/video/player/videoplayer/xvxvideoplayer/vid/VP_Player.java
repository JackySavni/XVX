package com.video.player.videoplayer.xvxvideoplayer.vid;

import static android.content.pm.PackageManager.FEATURE_EXPANDED_PICTURE_IN_PICTURE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.UriPermission;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Icon;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.LoudnessEnhancer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.accessibility.CaptioningManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SeekParameters;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ts.DefaultTsPayloadReaderFactory;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionOverride;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.CaptionStyleCompat;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.ui.SubtitleView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.material.snackbar.Snackbar;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.services.VP_VideoPlayAsAudioService;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.VP_DoubleTapPlayerViewVP;
import com.video.player.videoplayer.xvxvideoplayer.vid.dtpv.youtube.VP_YouTubeOverlay;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VP_Player extends Activity {
    private VD_PlayerListener vp_VD_playerListener;
    private BroadcastReceiver vp_mReceiver;
    private AudioManager vp_mAudioManager;
    private MediaSessionCompat vp_mediaSession;
    private DefaultTrackSelector vp_trackSelector;
    public static LoudnessEnhancer vp_loudnessEnhancer;
    public VP_CustomStyledPlayerView vp_playerView;
    public static ExoPlayer vp_player;
    private VP_YouTubeOverlay VPYouTubeOverlay;
    private Object vp_mPictureInPictureParamsBuilder;
    public VP_Prefs mVPPrefs;
    public VP_BrightnessControl mVPBrightnessControl;
    public static boolean vp_haveMedia;
    private boolean vp_videoLoading;
    public static boolean vp_controllerVisible;
    public static boolean vp_controllerVisibleFully;
    public static Snackbar vp_snackbar;
    private ExoPlaybackException vp_errorToShow;
    public static int vp_boostLevel = 0;
    private boolean vp_isScaling = false;
    private boolean vp_isScaleStarting = false;
    private float vp_scaleFactor = 1.0f;
    private static final int VP_REQUEST_CHOOSER_VIDEO = 1;
    private static final int VP_REQUEST_CHOOSER_SUBTITLE = 2;
    private static final int VP_REQUEST_CHOOSER_SCOPE_DIR = 10;
    private static final int VP_REQUEST_CHOOSER_VIDEO_MEDIASTORE = 20;
    private static final int VP_REQUEST_CHOOSER_SUBTITLE_MEDIASTORE = 21;
    private static final int VP_REQUEST_SETTINGS = 100;
    private static final int VP_REQUEST_SYSTEM_CAPTIONS = 200;
    public static final int VP_CONTROLLER_TIMEOUT = 3500;
    private static final String VP_ACTION_MEDIA_CONTROL = "media_control";
    private static final String VP_EXTRA_CONTROL_TYPE = "control_type";
    private static final int VP_REQUEST_PLAY = 1;
    private static final int VP_REQUEST_PAUSE = 2;
    private static final int VP_CONTROL_TYPE_PLAY = 1;
    private static final int VP_CONTROL_TYPE_PAUSE = 2;

    private CoordinatorLayout vp_coordinatorLayout;
    private TextView vp_titleView;
    private ImageButton vp_buttonOpen;
    private ImageButton vp_buttonPiP;
    private ImageButton vp_buttonAspectRatio;
    private ImageButton vp_buttonRotation;
    private ImageButton vp_exoSettings;
    private ImageButton vp_exoPlayPause;
    private ImageButton vp_exoRepeat;
    private ProgressBar vp_loadingProgressBar;
    private StyledPlayerControlView vp_controlView;
    private VP_CustomDefaultTimeBar vp_timeBar;
    private boolean vp_restoreOrientationLock;
    private boolean vp_restorePlayState;
    private boolean vp_restorePlayStateAllowed;
    private boolean vp_play;
    private float vp_subtitlesScale;
    private boolean vp_isScrubbing;
    private boolean vp_scrubbingNoticeable;
    private long vp_scrubbingStart;
    public boolean vp_frameRendered;
    private boolean vp_alive;
    public static boolean vp_focusPlay = false;
    private Uri vp_nextUri;
    private static boolean vp_isTvBox;
    public static boolean vp_locked = false;
    private Thread vp_nextUriThread;
    public Thread vp_frameRateSwitchThread;
    public Thread vp_chaptersThread;
    private long vp_lastScrubbingPosition;
    public static long[] vp_chapterStarts;

    public static boolean vp_restoreControllerTimeout = false;
    public static boolean vp_shortControllerTimeout = false;

    final Rational vp_rationalLimitWide = new Rational(239, 100);
    final Rational vp_rationalLimitTall = new Rational(100, 239);

    static final String VP_API_POSITION = "position";
    static final String VP_API_DURATION = "duration";
    static final String VP_API_RETURN_RESULT = "return_result";
    static final String VP_API_SUBS = "subs";
    static final String VP_API_SUBS_ENABLE = "subs.enable";
    static final String VP_API_SUBS_NAME = "subs.name";
    static final String VP_API_TITLE = "title";
    static final String VP_API_END_BY = "end_by";
    boolean vp_apiAccess;
    String vp_apiTitle;
    List<MediaItem.SubtitleConfiguration> vp_apiSubs = new ArrayList<>();
    boolean vp_intentReturnResult;
    boolean vp_playbackFinished;
    DisplayManager vp_displayManager;
    DisplayManager.DisplayListener vp_displayListener;
    VP_SubtitleFinder VPSubtitleFinder;

    ArrayList<MediaData> vp_videosList = new ArrayList<>();
    int vp_videoPosition = 0;
    ImageButton vp_previous, vp_next1;

    Runnable vp_barsHider = () -> {
        if (vp_playerView != null && !vp_controllerVisible) {
            VP_Utils.vp_toggleSystemUi(VP_Player.this, vp_playerView, false);
        }
    };

    public static Intent vp_getIntent(Context context, ArrayList<MediaData> arrayList, int i) {
        Intent intent = new Intent(context, VP_Player.class);
        intent.putExtra(VP_Constant.VP_EXTRA_VIDEO_LIST, arrayList);
        intent.putExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, i);
        return intent;
    }

    public static Intent vp_getInstance(Context context, int i, boolean z) {
        Intent intent = new Intent(context, VP_Player.class);
        intent.putExtra(VP_Constant.VP_EXTRA_FLOATING_VIDEO, i);
        intent.putExtra(VP_Constant.VP_EXTRA_IS_FLOATING_VIDEO, z);
        return intent;
    }

    public static Intent vp_getIntent(Context context, ArrayList<MediaData> arrayList, int i, boolean z) {
        Intent intent = new Intent(context, VP_Player.class);
        intent.putExtra(VP_Constant.VP_EXTRA_VIDEO_LIST, arrayList);
        intent.putExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, i);
        intent.putExtra(VP_Constant.VP_EXTRA_BACKGROUND_VIDEO_PLAY_POSITION, z);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Rotate ASAP, before super/inflating to avoid glitches with activity launch animation
        mVPPrefs = new VP_Prefs(this);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT == 28 && Build.MANUFACTURER.equalsIgnoreCase("xiaomi") &&
                (Build.DEVICE.equalsIgnoreCase("oneday") || Build.DEVICE.equalsIgnoreCase("once"))) {
            setContentView(R.layout.vp_activity_player_textureview);
        } else {
            setContentView(R.layout.vp_activity_player);
        }


        startService(new Intent(this, VP_VideoPlayAsAudioService.class).setAction(VP_VideoPlayAsAudioService.VP_NOTIFICATION_CLICK_ACTION));
        if (getIntent() != null) {
            vp_videosList = (ArrayList<MediaData>) getIntent().getSerializableExtra(VP_Constant.VP_EXTRA_VIDEO_LIST);
            vp_videoPosition = getIntent().getIntExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, 0);
        }
        if (vp_videosList == null) {
            vp_videosList = VP_MyApplication.vp_getVideoList();
        }

        if (vp_videosList != null && vp_videosList.size() > 0) {
            mVPPrefs.vp_updateMedia(this, vp_getUriFromPath(vp_videosList.get(vp_videoPosition).getPath()), null);
        }

        if (Build.VERSION.SDK_INT >= 31) {
            Window vp_window = getWindow();
            if (vp_window != null) {
                vp_window.setDecorFitsSystemWindows(false);
                WindowInsetsController windowInsetsController = vp_window.getInsetsController();
                if (windowInsetsController != null) {
                    // On Android 12 BEHAVIOR_DEFAULT allows system gestures without visible system bars
                    windowInsetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_DEFAULT);
                }
            }
        }

        vp_isTvBox = VP_Utils.vp_isTvBox(this);

        if (vp_isTvBox) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        vp_coordinatorLayout = findViewById(R.id.vp_coordinatorLayout);
        vp_mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        vp_playerView = findViewById(R.id.vp_video_view);
        vp_exoPlayPause = findViewById(R.id.exo_play_pause);
        vp_loadingProgressBar = findViewById(R.id.vp_loading);
        vp_previous = findViewById(R.id.vp_previous);
        vp_next1 = findViewById(R.id.next1);

        vp_previous.setOnClickListener(view -> {
            vp_next1.setVisibility(View.VISIBLE);
            if (vp_videoPosition - 1 >= 0) {
                this.vp_videoPosition--;
                if (vp_videosList != null && vp_videosList.size() > 0) {
                    if (vp_getUriFromPath(vp_videosList.get(vp_videoPosition).getPath()) != null) {
                        vp_releasePlayer();
                        mVPPrefs.vp_updateMedia(this, vp_getUriFromPath(vp_videosList.get(vp_videoPosition).getPath()), null);
                        vp_searchSubtitles();
                        vp_initializePlayer();
                    }
                }
            } else {
                Toast.makeText(this, "No previous video...", Toast.LENGTH_SHORT).show();
            }

        });

        vp_next1.setOnClickListener(view -> {
            vp_previous.setVisibility(View.VISIBLE);
            if (vp_videosList != null && vp_videosList.size() > 0) {
                if (this.vp_videoPosition + 1 < vp_videosList.size()) {
                    this.vp_videoPosition++;
                    if (vp_getUriFromPath(vp_videosList.get(vp_videoPosition).getPath()) != null) {
                        vp_releasePlayer();
                        mVPPrefs.vp_updateMedia(this, vp_getUriFromPath(vp_videosList.get(vp_videoPosition).getPath()), null);
                        vp_searchSubtitles();
                        vp_initializePlayer();
                    }
                } else {
                    Toast.makeText(this, "No next video...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        vp_playerView.setShowNextButton(false);
        vp_playerView.setShowPreviousButton(false);
        vp_playerView.setShowFastForwardButton(false);
        vp_playerView.setShowRewindButton(false);

        vp_playerView.setRepeatToggleModes(com.google.android.exoplayer2.Player.REPEAT_MODE_ONE);

        vp_playerView.setControllerHideOnTouch(false);
        vp_playerView.setControllerAutoShow(true);

        ((VP_DoubleTapPlayerViewVP) vp_playerView).setVp_isDoubleTapEnabled(false);

        vp_timeBar = vp_playerView.findViewById(R.id.exo_progress);
        vp_timeBar.addListener(new TimeBar.OnScrubListener() {
            @Override
            public void onScrubStart(TimeBar timeBar, long position) {
                if (vp_player == null) {
                    return;
                }
                vp_restorePlayState = vp_player.isPlaying();
                if (vp_restorePlayState) {
                    vp_player.pause();
                }
                vp_lastScrubbingPosition = position;
                vp_scrubbingNoticeable = false;
                vp_isScrubbing = true;
                vp_frameRendered = true;
                vp_playerView.setControllerShowTimeoutMs(-1);
                vp_scrubbingStart = vp_player.getCurrentPosition();
                vp_player.setSeekParameters(SeekParameters.CLOSEST_SYNC);
                vp_reportScrubbing(position);
            }

            @Override
            public void onScrubMove(TimeBar timeBar, long position) {
                vp_reportScrubbing(position);
                for (long start : vp_chapterStarts) {
                    if ((vp_lastScrubbingPosition < start && position >= start) || (vp_lastScrubbingPosition > start && position <= start)) {
                        vp_playerView.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK);
                    }
                }
                vp_lastScrubbingPosition = position;
            }

            @Override
            public void onScrubStop(TimeBar timeBar, long position, boolean canceled) {
                vp_playerView.setCustomErrorMessage(null);
                vp_isScrubbing = false;
                if (vp_restorePlayState) {
                    vp_restorePlayState = false;
                    vp_playerView.setControllerShowTimeoutMs(VP_Player.VP_CONTROLLER_TIMEOUT);
                    vp_player.setPlayWhenReady(true);
                }
            }
        });

        if (VP_Utils.vp_isPiPSupported(this)) {
            // TODO: Android 12 improvements:
            // https://developer.android.com/about/versions/12/features/pip-improvements
            vp_mPictureInPictureParamsBuilder = new PictureInPictureParams.Builder();
            boolean success = vp_updatePictureInPictureActions(R.drawable.vp_ic_play_arrow_24dp, R.string.exo_controls_play_description, VP_CONTROL_TYPE_PLAY, VP_REQUEST_PLAY);

            if (success) {
                vp_buttonPiP = new ImageButton(this, null, 0, R.style.ExoStyledControls_Button_Bottom);
                vp_buttonPiP.setContentDescription(getString(R.string.button_pip));
                vp_buttonPiP.setImageResource(R.drawable.vp_ic_picture_in_picture_alt_24dp);

                vp_buttonPiP.setOnClickListener(view -> vp_enterPiP());
            }
        }

        vp_buttonAspectRatio = new ImageButton(this, null, 0, R.style.ExoStyledControls_Button_Bottom);
        vp_buttonAspectRatio.setId(Integer.MAX_VALUE - 100);
        vp_buttonAspectRatio.setContentDescription(getString(R.string.button_crop));
        vp_updatebuttonAspectRatioIcon();
        vp_buttonAspectRatio.setOnClickListener(view -> {
            vp_playerView.vp_setScale(1.f);
            if (vp_playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_FIT) {
                vp_playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
                VP_Utils.vp_showText(vp_playerView, getString(R.string.video_resize_crop));
            } else {
                // Default mode
                vp_playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                VP_Utils.vp_showText(vp_playerView, getString(R.string.video_resize_fit));
            }
            vp_updatebuttonAspectRatioIcon();
            vp_resetHideCallbacks();
        });
        if (vp_isTvBox && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            vp_buttonAspectRatio.setOnLongClickListener(v -> {
                vp_scaleStart();
                vp_updatebuttonAspectRatioIcon();
                return true;
            });
        }
        vp_buttonRotation = new ImageButton(this, null, 0, R.style.ExoStyledControls_Button_Bottom);
        vp_buttonRotation.setContentDescription(getString(R.string.button_rotate));
        vp_updateButtonRotation();
        vp_buttonRotation.setOnClickListener(view -> {
            mVPPrefs.vp_orientation = VP_Utils.vp_getNextOrientation(mVPPrefs.vp_orientation);
            VP_Utils.vp_setOrientation(VP_Player.this, mVPPrefs.vp_orientation);
            vp_updateButtonRotation();
            vp_resetHideCallbacks();
        });

        final int vp_titleViewPaddingHorizontal = VP_Utils.vp_dpToPx(14);
        final int vp_titleViewPaddingVertical = getResources().getDimensionPixelOffset(R.dimen.exo_styled_bottom_bar_time_padding);
        FrameLayout vp_centerView = vp_playerView.findViewById(R.id.vp_exo_controls_background);
        vp_titleView = new TextView(this);
        vp_titleView.setBackgroundResource(R.color.ui_controls_background);
        vp_titleView.setTextColor(Color.WHITE);
        vp_titleView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        vp_titleView.setPadding(vp_titleViewPaddingHorizontal, vp_titleViewPaddingVertical, vp_titleViewPaddingHorizontal, vp_titleViewPaddingVertical);
        vp_titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        vp_titleView.setVisibility(View.GONE);
        vp_titleView.setMaxLines(1);
        vp_titleView.setEllipsize(TextUtils.TruncateAt.END);
        vp_titleView.setTextDirection(View.TEXT_DIRECTION_LOCALE);
        vp_centerView.addView(vp_titleView);

        vp_titleView.setOnLongClickListener(view -> {
            // Prevent FileUriExposedException
            if (mVPPrefs.vp_mediaUri != null && ContentResolver.SCHEME_FILE.equals(mVPPrefs.vp_mediaUri.getScheme())) {
                return false;
            }

            final Intent vp_shareIntent = new Intent(Intent.ACTION_SEND);
            vp_shareIntent.putExtra(Intent.EXTRA_STREAM, mVPPrefs.vp_mediaUri);
            if (mVPPrefs.vp_mediaType == null)
                vp_shareIntent.setType("video/*");
            else
                vp_shareIntent.setType(mVPPrefs.vp_mediaType);
            vp_shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Start without intent chooser to allow any target to be set as default
            startActivity(vp_shareIntent);

            return true;
        });

        vp_controlView = vp_playerView.findViewById(R.id.exo_controller);
        vp_controlView.setOnApplyWindowInsetsListener((view, vp_windowInsets) -> {
            if (vp_windowInsets != null) {
                if (Build.VERSION.SDK_INT >= 31) {
                    boolean visibleBars = vp_windowInsets.isVisible(WindowInsets.Type.statusBars());
                    if (visibleBars && !vp_controllerVisible) {
                        vp_playerView.postDelayed(vp_barsHider, 2500);
                    } else {
                        vp_playerView.removeCallbacks(vp_barsHider);
                    }
                }

                view.setPadding(0, vp_windowInsets.getSystemWindowInsetTop(),
                        0, vp_windowInsets.getSystemWindowInsetBottom());

                int vp_insetLeft = vp_windowInsets.getSystemWindowInsetLeft();
                int vp_insetRight = vp_windowInsets.getSystemWindowInsetRight();

                int vp_paddingLeft = 0;
                int vp_marginLeft = vp_insetLeft;

                int vp_paddingRight = 0;
                int vp_marginRight = vp_insetRight;

                if (Build.VERSION.SDK_INT >= 28 && vp_windowInsets.getDisplayCutout() != null) {
                    if (vp_windowInsets.getDisplayCutout().getSafeInsetLeft() == vp_insetLeft) {
                        vp_paddingLeft = vp_insetLeft;
                        vp_marginLeft = 0;
                    }
                    if (vp_windowInsets.getDisplayCutout().getSafeInsetRight() == vp_insetRight) {
                        vp_paddingRight = vp_insetRight;
                        vp_marginRight = 0;
                    }
                }

                VP_Utils.vp_setViewParams(vp_titleView, vp_paddingLeft + vp_titleViewPaddingHorizontal, vp_titleViewPaddingVertical, vp_paddingRight + vp_titleViewPaddingHorizontal, vp_titleViewPaddingVertical,
                        vp_marginLeft, vp_windowInsets.getSystemWindowInsetTop(), vp_marginRight, 0);

                VP_Utils.vp_setViewParams(findViewById(R.id.exo_bottom_bar), vp_paddingLeft, 0, vp_paddingRight, 0,
                        vp_marginLeft, 0, vp_marginRight, 0);

                findViewById(R.id.exo_progress).setPadding(vp_windowInsets.getSystemWindowInsetLeft(), 0,
                        vp_windowInsets.getSystemWindowInsetRight(), 0);

                VP_Utils.vp_setViewMargins(findViewById(R.id.exo_error_message), 0, vp_windowInsets.getSystemWindowInsetTop() / 2, 0, getResources().getDimensionPixelSize(R.dimen.exo_error_message_margin_bottom) + vp_windowInsets.getSystemWindowInsetBottom() / 2);

                vp_windowInsets.consumeSystemWindowInsets();
            }
            return vp_windowInsets;
        });
        vp_timeBar.setAdMarkerColor(Color.argb(0x00, 0xFF, 0xFF, 0xFF));
        vp_timeBar.setPlayedAdMarkerColor(Color.argb(0x98, 0xFF, 0xFF, 0xFF));

        try {
            VP_CustomDefaultTrackNameProvider VPCustomDefaultTrackNameProvider = new VP_CustomDefaultTrackNameProvider(getResources());
            final Field vp_field = StyledPlayerControlView.class.getDeclaredField("trackNameProvider");
            vp_field.setAccessible(true);
            vp_field.set(vp_controlView, VPCustomDefaultTrackNameProvider);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        vp_exoPlayPause.setOnClickListener(view -> dispatchPlayPause());

        // Prevent double tap actions in controller
        findViewById(R.id.exo_bottom_bar).setOnTouchListener((v, event) -> true);

        vp_VD_playerListener = new VD_PlayerListener();

        mVPBrightnessControl = new VP_BrightnessControl(this);
        if (mVPPrefs.vp_brightness >= 0) {
            mVPBrightnessControl.vp_currentBrightnessLevel = mVPPrefs.vp_brightness;
            mVPBrightnessControl.vp_setScreenBrightness(mVPBrightnessControl.vp_levelToBrightness(mVPBrightnessControl.vp_currentBrightnessLevel));
        }
        vp_playerView.vp_setBrightnessControl(mVPBrightnessControl);

        final LinearLayout vp_exoBasicControls = vp_playerView.findViewById(R.id.exo_basic_controls);

        vp_exoSettings = vp_exoBasicControls.findViewById(R.id.exo_settings);
        vp_exoBasicControls.removeView(vp_exoSettings);
        vp_exoRepeat = vp_exoBasicControls.findViewById(R.id.exo_repeat_toggle);
        vp_exoBasicControls.removeView(vp_exoRepeat);

        vp_exoSettings.setOnLongClickListener(view -> {
            //askForScope(false, false);
            Intent vp_intent = new Intent(this, VP_Settings.class);
            startActivityForResult(vp_intent, VP_REQUEST_SETTINGS);
            return true;
        });

        vp_updateButtons(false);

        final HorizontalScrollView vp_horizontalScrollView = (HorizontalScrollView) getLayoutInflater().inflate(R.layout.vp_controls, null);
        final LinearLayout controls = vp_horizontalScrollView.findViewById(R.id.vp_controls);

        controls.addView(vp_buttonAspectRatio);
        if (VP_Utils.vp_isPiPSupported(this) && vp_buttonPiP != null) {
            controls.addView(vp_buttonPiP);
        }
        if (mVPPrefs.vp_repeatToggle) {
            controls.addView(vp_exoRepeat);
        }
        if (!vp_isTvBox) {
            controls.addView(vp_buttonRotation);
        }
        controls.addView(vp_exoSettings);

        vp_exoBasicControls.addView(vp_horizontalScrollView);

        if (Build.VERSION.SDK_INT > 23) {
            vp_horizontalScrollView.setOnScrollChangeListener((view, i, i1, i2, i3) -> vp_resetHideCallbacks());
        }

        vp_playerView.setControllerVisibilityListener((StyledPlayerView.ControllerVisibilityListener) visibility -> {
            vp_controllerVisible = visibility == View.VISIBLE;
            vp_controllerVisibleFully = vp_playerView.isControllerFullyVisible();

            if (VP_Player.vp_restoreControllerTimeout) {
                vp_restoreControllerTimeout = false;
                if (vp_player == null || !vp_player.isPlaying()) {
                    vp_playerView.setControllerShowTimeoutMs(-1);
                } else {
                    vp_playerView.setControllerShowTimeoutMs(VP_Player.VP_CONTROLLER_TIMEOUT);
                }
            }

            if (visibility == View.VISIBLE) {
                // Because when using dpad controls, focus resets to first item in bottom controls bar
                findViewById(R.id.exo_play_pause).requestFocus();
            }

        });

        VPYouTubeOverlay = findViewById(R.id.youtube_overlay);
        VPYouTubeOverlay.performListener(new VP_YouTubeOverlay.PerformListener() {
            @Override
            public void onAnimationStart() {
                VPYouTubeOverlay.setAlpha(1.0f);
                VPYouTubeOverlay.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd() {
                VPYouTubeOverlay.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                VPYouTubeOverlay.setVisibility(View.GONE);
                                VPYouTubeOverlay.setAlpha(1.0f);
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        vp_alive = true;
        if (!(vp_isTvBox && Build.VERSION.SDK_INT >= 31)) {
            vp_updateSubtitleStyle(this);
        }
        if (Build.VERSION.SDK_INT >= 31) {
            vp_playerView.removeCallbacks(vp_barsHider);
            VP_Utils.vp_toggleSystemUi(this, vp_playerView, true);
        }
        vp_initializePlayer();
        vp_updateButtonRotation();
    }

    @Override
    public void onResume() {
        super.onResume();
        vp_restorePlayStateAllowed = true;
        if (vp_isTvBox && Build.VERSION.SDK_INT >= 31) {
            vp_updateSubtitleStyle(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        vp_savePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        vp_alive = false;
        if (Build.VERSION.SDK_INT >= 31) {
            vp_playerView.removeCallbacks(vp_barsHider);
        }
        vp_playerView.setCustomErrorMessage(null);
        vp_releasePlayer(false);
    }

    @Override
    public void onBackPressed() {
        vp_restorePlayStateAllowed = false;
        super.onBackPressed();
    }

    @Override
    public void finish() {
        if (vp_intentReturnResult) {
            Intent intent = new Intent("com.mxtech.intent.result.VIEW");
            intent.putExtra(VP_API_END_BY, vp_playbackFinished ? "playback_completion" : "user");
            if (!vp_playbackFinished) {
                if (vp_player != null) {
                    long duration = vp_player.getDuration();
                    if (duration != C.TIME_UNSET) {
                        intent.putExtra(VP_API_DURATION, (int) vp_player.getDuration());
                    }
                    if (vp_player.isCurrentMediaItemSeekable()) {
                        if (mVPPrefs.vp_persistentMode) {
                            intent.putExtra(VP_API_POSITION, (int) mVPPrefs.vp_nonPersitentPosition);
                        } else {
                            intent.putExtra(VP_API_POSITION, (int) vp_player.getCurrentPosition());
                        }
                    }
                }
            }
            setResult(Activity.RESULT_OK, intent);
        }

        super.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            final String vp_action = intent.getAction();
            final String vp_type = intent.getType();
            final Uri vp_uri = intent.getData();

            if (Intent.ACTION_VIEW.equals(vp_action) && vp_uri != null) {
                if (VP_SubtitleUtils.vp_isSubtitle(vp_uri, vp_type)) {
                    vp_handleSubtitles(vp_uri);
                } else {
                    mVPPrefs.vp_updateMedia(this, vp_uri, vp_type);
                    vp_searchSubtitles();
                }
                vp_focusPlay = true;
                vp_initializePlayer();
            } else if (Intent.ACTION_SEND.equals(vp_action) && "text/plain".equals(vp_type)) {
                String vp_text = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (vp_text != null) {
                    final Uri vp_parsedUri = Uri.parse(vp_text);
                    if (vp_parsedUri.isAbsolute()) {
                        mVPPrefs.vp_updateMedia(this, vp_parsedUri, null);
                        vp_focusPlay = true;
                        vp_initializePlayer();
                    }
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int vp_keyCode, KeyEvent vp_event) {
        switch (vp_keyCode) {
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                if (vp_player == null)
                    break;
                if (vp_keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                    vp_player.pause();
                } else if (vp_keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                    vp_player.play();
                } else if (vp_player.isPlaying()) {
                    vp_player.pause();
                } else {
                    vp_player.play();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                VP_Utils.vp_adjustVolume(this, vp_mAudioManager, vp_playerView, vp_keyCode == KeyEvent.KEYCODE_VOLUME_UP, vp_event.getRepeatCount() == 0, true);
                return true;
            case KeyEvent.KEYCODE_BUTTON_START:
            case KeyEvent.KEYCODE_BUTTON_A:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
            case KeyEvent.KEYCODE_SPACE:
                if (vp_player == null)
                    break;
                if (!vp_controllerVisibleFully) {
                    if (vp_player.isPlaying()) {
                        vp_player.pause();
                    } else {
                        vp_player.play();
                    }
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_BUTTON_L2:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                if (!vp_controllerVisibleFully || vp_keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
                    if (vp_player == null)
                        break;
                    vp_playerView.removeCallbacks(vp_playerView.vp_textClearRunnable);
                    long pos = vp_player.getCurrentPosition();
                    if (vp_playerView.vp_keySeekStart == -1) {
                        vp_playerView.vp_keySeekStart = pos;
                    }
                    long vp_seekTo = pos - 10_000;
                    if (vp_seekTo < 0)
                        vp_seekTo = 0;
                    vp_player.setSeekParameters(SeekParameters.PREVIOUS_SYNC);
                    vp_player.seekTo(vp_seekTo);
                    final String vp_message = VP_Utils.vp_formatMilisSign(vp_seekTo - vp_playerView.vp_keySeekStart) + "\n" + VP_Utils.vp_formatMilis(vp_seekTo);
                    vp_playerView.setCustomErrorMessage(vp_message);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_BUTTON_R2:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                if (!vp_controllerVisibleFully || vp_keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
                    if (vp_player == null)
                        break;
                    vp_playerView.removeCallbacks(vp_playerView.vp_textClearRunnable);
                    long pos = vp_player.getCurrentPosition();
                    if (vp_playerView.vp_keySeekStart == -1) {
                        vp_playerView.vp_keySeekStart = pos;
                    }
                    long vp_seekTo = pos + 10_000;
                    long vp_seekMax = vp_player.getDuration();
                    if (vp_seekMax != C.TIME_UNSET && vp_seekTo > vp_seekMax)
                        vp_seekTo = vp_seekMax;
                    VP_Player.vp_player.setSeekParameters(SeekParameters.NEXT_SYNC);
                    vp_player.seekTo(vp_seekTo);
                    final String vp_message = VP_Utils.vp_formatMilisSign(vp_seekTo - vp_playerView.vp_keySeekStart) + "\n" + VP_Utils.vp_formatMilis(vp_seekTo);
                    vp_playerView.setCustomErrorMessage(vp_message);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
                if (vp_isTvBox) {
                    if (vp_controllerVisible && vp_player != null && vp_player.isPlaying()) {
                        vp_playerView.hideController();
                        return true;
                    } else {
                        onBackPressed();
                    }
                }
                break;
            default:
                if (!vp_controllerVisibleFully) {
                    vp_playerView.showController();
                    return true;
                }
                break;
        }
        return super.onKeyDown(vp_keyCode, vp_event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                vp_playerView.postDelayed(vp_playerView.vp_textClearRunnable, VP_CustomStyledPlayerView.VP_MESSAGE_TIMEOUT_KEY);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_BUTTON_L2:
            case KeyEvent.KEYCODE_MEDIA_REWIND:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_BUTTON_R2:
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                if (!vp_isScrubbing) {
                    vp_playerView.postDelayed(vp_playerView.vp_textClearRunnable, 1000);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (vp_isScaling) {
            final int vp_keyCode = event.getKeyCode();
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (vp_keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        vp_scale(true);
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        vp_scale(false);
                        break;
                }
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                switch (vp_keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        break;
                    default:
                        if (vp_isScaleStarting) {
                            vp_isScaleStarting = false;
                        } else {
                            scaleEnd();
                        }
                }
            }
            return true;
        }

        if (vp_isTvBox && !vp_controllerVisibleFully) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                onKeyDown(event.getKeyCode(), event);
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                onKeyUp(event.getKeyCode(), event);
            }
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_SCROLL:
                    final float value = event.getAxisValue(MotionEvent.AXIS_VSCROLL);
                    VP_Utils.vp_adjustVolume(this, vp_mAudioManager, vp_playerView, value > 0.0f, Math.abs(value) > 1.0f, true);
                    return true;
            }
        } else if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {
            // TODO: This somehow works, but it would use better filtering
            float vp_value = event.getAxisValue(MotionEvent.AXIS_RZ);
            for (int vp_i = 0; vp_i < event.getHistorySize(); vp_i++) {
                float vp_historical = event.getHistoricalAxisValue(MotionEvent.AXIS_RZ, vp_i);
                if (Math.abs(vp_historical) > vp_value) {
                    vp_value = vp_historical;
                }
            }
            if (Math.abs(vp_value) == 1.0f) {
                VP_Utils.vp_adjustVolume(this, vp_mAudioManager, vp_playerView, vp_value < 0, true, true);
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    public void onPictureInPictureModeChanged(boolean vp_isInPictureInPictureMode, Configuration newConfig) {
        super.onPictureInPictureModeChanged(vp_isInPictureInPictureMode, newConfig);

        if (vp_isInPictureInPictureMode) {
            // On Android TV it is required to hide controller in this PIP change callback
            vp_playerView.hideController();
            setSubtitleTextSizePiP();
            vp_playerView.vp_setScale(1.f);
            vp_mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent == null || !VP_ACTION_MEDIA_CONTROL.equals(intent.getAction()) || vp_player == null) {
                        return;
                    }

                    switch (intent.getIntExtra(VP_EXTRA_CONTROL_TYPE, 0)) {
                        case VP_CONTROL_TYPE_PLAY:
                            vp_player.play();
                            break;
                        case VP_CONTROL_TYPE_PAUSE:
                            vp_player.pause();
                            break;
                    }
                }
            };
            registerReceiver(vp_mReceiver, new IntentFilter(VP_ACTION_MEDIA_CONTROL));
        } else {
            vp_setSubtitleTextSize();
            if (mVPPrefs.vp_resizeMode == AspectRatioFrameLayout.RESIZE_MODE_ZOOM) {
                vp_playerView.vp_setScale(mVPPrefs.vp_scale);
            }
            if (vp_mReceiver != null) {
                unregisterReceiver(vp_mReceiver);
                vp_mReceiver = null;
            }
            vp_playerView.setControllerAutoShow(true);
            if (vp_player != null) {
                if (vp_player.isPlaying())
                    VP_Utils.vp_toggleSystemUi(this, vp_playerView, false);
                else
                    vp_playerView.showController();
            }
        }
    }

    void vp_resetApiAccess() {
        vp_apiAccess = false;
        vp_apiTitle = null;
        vp_apiSubs.clear();
        mVPPrefs.vp_setPersistent(true);
    }

    public Uri vp_getUriFromPath(String videoPath) {
        Uri vp_withAppendedPath = null;
        Cursor vp_managedQuery = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{"_id"}, "_data like?", new String[]{videoPath}, null);
        vp_managedQuery.moveToFirst();
        if (vp_managedQuery.moveToFirst()) {
            vp_withAppendedPath = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, vp_managedQuery.getString(vp_managedQuery.getColumnIndexOrThrow("_id")));
        }
        vp_managedQuery.close();
        return vp_withAppendedPath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (vp_restoreOrientationLock) {
                android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, 0);
                vp_restoreOrientationLock = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resultCode == RESULT_OK && vp_alive) {
            vp_releasePlayer();
        }

        if (requestCode == VP_REQUEST_CHOOSER_VIDEO || requestCode == VP_REQUEST_CHOOSER_VIDEO_MEDIASTORE) {
            if (resultCode == RESULT_OK) {
                vp_resetApiAccess();
                vp_restorePlayState = false;

                final Uri vp_uri = data.getData();

                if (requestCode == VP_REQUEST_CHOOSER_VIDEO) {
                    boolean vp_uriAlreadyTaken = false;

                    // https://commonsware.com/blog/2020/06/13/count-your-saf-uri-permission-grants.html
                    final ContentResolver vp_contentResolver = getContentResolver();
                    for (UriPermission vp_persistedUri : vp_contentResolver.getPersistedUriPermissions()) {
                        if (vp_persistedUri.getUri().equals(mVPPrefs.vp_scopeUri)) {
                            continue;
                        } else if (vp_persistedUri.getUri().equals(vp_uri)) {
                            vp_uriAlreadyTaken = true;
                        } else {
                            try {
                                vp_contentResolver.releasePersistableUriPermission(vp_persistedUri.getUri(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (!vp_uriAlreadyTaken && vp_uri != null) {
                        try {
                            vp_contentResolver.takePersistableUriPermission(vp_uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                    }
                }

                mVPPrefs.vp_setPersistent(true);
                mVPPrefs.vp_updateMedia(this, vp_uri, data.getType());

                if (requestCode == VP_REQUEST_CHOOSER_VIDEO) {
                    vp_searchSubtitles();
                }
            }
        } else if (requestCode == VP_REQUEST_CHOOSER_SUBTITLE || requestCode == VP_REQUEST_CHOOSER_SUBTITLE_MEDIASTORE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();

                if (requestCode == VP_REQUEST_CHOOSER_SUBTITLE) {
                    try {
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }

                vp_handleSubtitles(uri);
            }
        } else if (requestCode == VP_REQUEST_CHOOSER_SCOPE_DIR) {
            if (resultCode == RESULT_OK) {
                final Uri uri = data.getData();
//
//                String outputFile = context.getExternalFilesDir("DirName") + "/fileName.extension";
//
//                File file = new File(outputFile);
//                Log.e("OutPutFile",outputFile);
//                Uri uri = FileProvider.getUriForFile(PlayerActivity.this,
//                        BuildConfig.APPLICATION_ID + ".provider",file);
                try {
                    getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    mVPPrefs.vp_updateScope(uri);
                    mVPPrefs.vp_markScopeAsked();
                    vp_searchSubtitles();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == VP_REQUEST_SETTINGS) {
            mVPPrefs.vp_loadUserPreferences();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        // Init here because onStart won't follow when app was only paused when file chooser was shown
        // (for example pop-up file chooser on tablets)
        if (resultCode == RESULT_OK && vp_alive) {
            vp_initializePlayer();
        }
    }

    private void vp_handleSubtitles(Uri uri) {
        // Convert subtitles to UTF-8 if necessary
        VP_SubtitleUtils.vp_clearCache(this);
        uri = VP_SubtitleUtils.vp_convertToUTF(this, uri);
        mVPPrefs.vp_updateSubtitle(uri);
    }

    public void vp_initializePlayer() {
        try {
            if (vp_restoreOrientationLock) {
                android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION, 0);
                vp_restoreOrientationLock = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean vp_isNetworkUri = mVPPrefs.vp_mediaUri != null && VP_Utils.vp_isSupportedNetworkUri(mVPPrefs.vp_mediaUri);
        vp_haveMedia = mVPPrefs.vp_mediaUri != null && (VP_Utils.vp_fileExists(this, mVPPrefs.vp_mediaUri) || vp_isNetworkUri);

        if (vp_player != null) {
            vp_player.removeListener(vp_VD_playerListener);
            vp_player.clearMediaItems();
            vp_player.release();
            vp_player = null;
        }

        vp_trackSelector = new DefaultTrackSelector(this);
        if (mVPPrefs.vp_tunneling) {
            vp_trackSelector.setParameters(vp_trackSelector.buildUponParameters()
                    .setTunnelingEnabled(true)
            );
        }
        vp_trackSelector.setParameters(vp_trackSelector.buildUponParameters()
                .setPreferredAudioLanguages(VP_Utils.vp_getDeviceLanguages())
        );
        // https://github.com/google/ExoPlayer/issues/8571
        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory()
                .setTsExtractorFlags(DefaultTsPayloadReaderFactory.FLAG_ENABLE_HDMV_DTS_AUDIO_STREAMS)
                .setTsExtractorTimestampSearchBytes(1500 * TsExtractor.TS_PACKET_SIZE);
        @SuppressLint("WrongConstant") RenderersFactory renderersFactory = new DefaultRenderersFactory(this)
                .setExtensionRendererMode(mVPPrefs.vp_decoderPriority);

        ExoPlayer.Builder vp_playerBuilder = new ExoPlayer.Builder(this, renderersFactory)
                .setTrackSelector(vp_trackSelector)
                .setMediaSourceFactory(new DefaultMediaSourceFactory(this, extractorsFactory));

        if (vp_haveMedia && vp_isNetworkUri) {
            if (mVPPrefs.vp_mediaUri.getScheme().toLowerCase().startsWith("http")) {
                HashMap<String, String> headers = new HashMap<>();
                String vp_userInfo = mVPPrefs.vp_mediaUri.getUserInfo();
                if (vp_userInfo != null && vp_userInfo.length() > 0 && vp_userInfo.contains(":")) {
                    headers.put("Authorization", "Basic " + Base64.encodeToString(vp_userInfo.getBytes(), Base64.NO_WRAP));
                    DefaultHttpDataSource.Factory defaultHttpDataSourceFactory = new DefaultHttpDataSource.Factory();
                    defaultHttpDataSourceFactory.setDefaultRequestProperties(headers);
                    vp_playerBuilder.setMediaSourceFactory(new DefaultMediaSourceFactory(defaultHttpDataSourceFactory, extractorsFactory));
                }
            }
        }

        vp_player = vp_playerBuilder.build();

        @SuppressLint("WrongConstant") AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build();
        vp_player.setAudioAttributes(audioAttributes, true);

        if (mVPPrefs.vp_skipSilence) {
            vp_player.setSkipSilenceEnabled(true);
        }

        VPYouTubeOverlay.vp_player(vp_player);
        vp_playerView.setPlayer(vp_player);

        vp_mediaSession = new MediaSessionCompat(this, getString(R.string.app_name));
        MediaSessionConnector vp_mediaSessionConnector = new MediaSessionConnector(vp_mediaSession);
        vp_mediaSessionConnector.setPlayer(vp_player);

        vp_mediaSessionConnector.setMediaMetadataProvider(player -> {
            if (mVPPrefs.vp_mediaUri == null)
                return null;
            final String vp_title = VP_Utils.vp_getFileName(VP_Player.this, mVPPrefs.vp_mediaUri);
            if (vp_title == null)
                return null;
            else
                return new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, vp_title)
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, vp_title)
                        .build();
        });

        vp_playerView.setControllerShowTimeoutMs(-1);

        vp_locked = false;

        vp_chapterStarts = new long[0];

        if (vp_haveMedia) {
            if (vp_isNetworkUri) {
                vp_timeBar.setBufferedColor(DefaultTimeBar.DEFAULT_BUFFERED_COLOR);
            } else {
                // https://github.com/google/ExoPlayer/issues/5765
                vp_timeBar.setBufferedColor(0x33FFFFFF);
            }

            vp_playerView.setResizeMode(mVPPrefs.vp_resizeMode);

            if (mVPPrefs.vp_resizeMode == AspectRatioFrameLayout.RESIZE_MODE_ZOOM) {
                vp_playerView.vp_setScale(mVPPrefs.vp_scale);
            } else {
                vp_playerView.vp_setScale(1.f);
            }
            vp_updatebuttonAspectRatioIcon();

            MediaItem.Builder vp_mediaItemBuilder = new MediaItem.Builder()
                    .setUri(mVPPrefs.vp_mediaUri)
                    .setMimeType(mVPPrefs.vp_mediaType);
            if (vp_apiAccess && vp_apiSubs.size() > 0) {
                vp_mediaItemBuilder.setSubtitleConfigurations(vp_apiSubs);
            } else if (mVPPrefs.vp_subtitleUri != null && VP_Utils.vp_fileExists(this, mVPPrefs.vp_subtitleUri)) {
                MediaItem.SubtitleConfiguration subtitle = VP_SubtitleUtils.vp_buildSubtitle(this, mVPPrefs.vp_subtitleUri, null, true);
                vp_mediaItemBuilder.setSubtitleConfigurations(Collections.singletonList(subtitle));
            }
            vp_player.setMediaItem(vp_mediaItemBuilder.build(), mVPPrefs.getPosition());

            if (vp_loudnessEnhancer != null) {
                vp_loudnessEnhancer.release();
            }
            try {
                vp_loudnessEnhancer = new LoudnessEnhancer(vp_player.getAudioSessionId());
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            vp_notifyAudioSessionUpdate(true);

            vp_videoLoading = true;

            vp_updateLoading(true);

            if (mVPPrefs.getPosition() == 0L || vp_apiAccess) {
                vp_play = true;
            }

            if (vp_apiTitle != null) {
                vp_titleView.setText(vp_apiTitle);
            } else {
                vp_titleView.setText(VP_Utils.vp_getFileName(this, mVPPrefs.vp_mediaUri));
            }
            vp_titleView.setVisibility(View.VISIBLE);

            vp_updateButtons(true);

            ((VP_DoubleTapPlayerViewVP) vp_playerView).setVp_isDoubleTapEnabled(true);

            if (!vp_apiAccess) {
                if (vp_nextUriThread != null) {
                    vp_nextUriThread.interrupt();
                }
                vp_nextUri = null;
                vp_nextUriThread = new Thread(() -> {
                    Uri uri = findNext();
                    if (!Thread.currentThread().isInterrupted()) {
                        vp_nextUri = uri;
                    }
                });
                vp_nextUriThread.start();
            }

            VP_Utils.vp_markChapters(this, mVPPrefs.vp_mediaUri, vp_controlView);

            vp_player.setHandleAudioBecomingNoisy(!vp_isTvBox);
            vp_mediaSession.setActive(true);
        } else {
            vp_playerView.showController();
        }

        vp_player.addListener(vp_VD_playerListener);
        vp_player.prepare();
        vp_restorePlayState = false;
        vp_playerView.showController();
        vp_playerView.setControllerShowTimeoutMs(VP_Player.VP_CONTROLLER_TIMEOUT);
        vp_player.setPlayWhenReady(true);
    }

    private void vp_savePlayer() {
        if (vp_player != null) {
            mVPPrefs.vp_updateBrightness(mVPBrightnessControl.vp_currentBrightnessLevel);
            mVPPrefs.vp_updateOrientation();

            if (vp_haveMedia) {
                // Prevent overwriting temporarily inaccessible media position
                if (vp_player.isCurrentMediaItemSeekable()) {
                    Log.d("TAG", "savePlayerDur: " + vp_player.getDuration());
                    if (vp_player.getDuration() >= 5000) {
                        if ((vp_player.getDuration() - vp_player.getCurrentPosition()) >= 5000) {
                            mVPPrefs.vp_updatePosition(vp_player.getCurrentPosition());
                        } else {
                            mVPPrefs.vp_updatePosition(vp_player.getDuration() - 5000);
                        }
                    } else {
                        mVPPrefs.vp_updatePosition(0);
                    }
                }
                mVPPrefs.vp_updateMeta(getSelectedTrack(C.TRACK_TYPE_AUDIO),
                        getSelectedTrack(C.TRACK_TYPE_TEXT),
                        vp_playerView.getResizeMode(),
                        vp_playerView.getVideoSurfaceView().getScaleX(),
                        vp_player.getPlaybackParameters().speed);
            }
        }
    }

    public void vp_releasePlayer() {
        vp_releasePlayer(true);
    }

    public void vp_releasePlayer(boolean save) {
        if (save) {
            vp_savePlayer();
        }

        if (vp_player != null) {
            vp_notifyAudioSessionUpdate(false);

            vp_mediaSession.setActive(false);
            vp_mediaSession.release();

            if (vp_player.isPlaying() && vp_restorePlayStateAllowed) {
                vp_restorePlayState = true;
            }
            vp_player.removeListener(vp_VD_playerListener);
            vp_player.clearMediaItems();
            vp_player.release();
            vp_player = null;
        }
        vp_titleView.setVisibility(View.GONE);
        vp_updateButtons(false);
    }

    private class VD_PlayerListener implements com.google.android.exoplayer2.Player.Listener {
        @Override
        public void onAudioSessionIdChanged(int audioSessionId) {
            if (vp_loudnessEnhancer != null) {
                vp_loudnessEnhancer.release();
            }
            try {
                vp_loudnessEnhancer = new LoudnessEnhancer(audioSessionId);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            vp_notifyAudioSessionUpdate(true);
        }

        @Override
        public void onIsPlayingChanged(boolean vp_isPlaying) {
            vp_playerView.setKeepScreenOn(vp_isPlaying);
            if (VP_Utils.vp_isPiPSupported(VP_Player.this)) {
                if (vp_isPlaying) {
                    vp_updatePictureInPictureActions(R.drawable.vp_ic_pause_24dp, R.string.exo_controls_pause_description, VP_CONTROL_TYPE_PAUSE, VP_REQUEST_PAUSE);
                } else {
                    vp_updatePictureInPictureActions(R.drawable.vp_ic_play_arrow_24dp, R.string.exo_controls_play_description, VP_CONTROL_TYPE_PLAY, VP_REQUEST_PLAY);
                }
            }

            if (!vp_isScrubbing) {
                if (vp_isPlaying) {
                    if (vp_shortControllerTimeout) {
                        vp_playerView.setControllerShowTimeoutMs(VP_CONTROLLER_TIMEOUT / 3);
                        vp_shortControllerTimeout = false;
                        vp_restoreControllerTimeout = true;
                    } else {
                        vp_playerView.setControllerShowTimeoutMs(VP_CONTROLLER_TIMEOUT);
                    }
                } else {
                    vp_playerView.setControllerShowTimeoutMs(-1);
                }
            }

            if (!vp_isPlaying) {
                VP_Player.vp_locked = false;
            }
        }

        @SuppressLint("SourceLockedOrientationActivity")
        @Override
        public void onPlaybackStateChanged(int state) {
            boolean vp_isNearEnd = false;
            final long vp_duration = vp_player.getDuration();
            if (vp_duration != C.TIME_UNSET) {
                final long vp_position = vp_player.getCurrentPosition();
                if (vp_position + 4000 >= vp_duration) {
                    vp_isNearEnd = true;
                } else {
                    // Last chapter is probably "Credits" chapter
                    final int vp_chapters = vp_chapterStarts.length;
                    if (vp_chapters > 1) {
                        final long vp_lastChapter = vp_chapterStarts[vp_chapters - 1];
                        if (vp_duration - vp_lastChapter < vp_duration / 10 && vp_position > vp_lastChapter) {
                            vp_isNearEnd = true;
                        }
                    }
                }
            }
            vp_setEndControlsVisible(vp_haveMedia && (state == com.google.android.exoplayer2.Player.STATE_ENDED || vp_isNearEnd));

            if (state == com.google.android.exoplayer2.Player.STATE_READY) {
                vp_frameRendered = true;

                if (vp_videoLoading) {
                    vp_videoLoading = false;

                    final Format vp_format = vp_player.getVideoFormat();
                    if (vp_format != null) {
                        Bitmap vp_bmp;
                        if (vp_videosList != null && vp_videosList.size() > 0) {
                            try {
                                MediaMetadataRetriever vp_retriever = new MediaMetadataRetriever();
                                //Declare the Bitmap
                                //Set the video Uri as data source for MediaMetadataRetriever
                                vp_retriever.setDataSource(VP_Player.this, vp_getUriFromPath(vp_videosList.get(vp_videoPosition).getPath()));
                                //Get one "frame"/bitmap - * NOTE - no time was set, so the first available frame will be used
                                vp_bmp = vp_retriever.getFrameAtTime();
                                vp_retriever.release();

                                //Get the bitmap width and height
                                int vp_videoWidth = vp_bmp.getWidth();
                                int vp_videoHeight = vp_bmp.getHeight();

                                //If the width is bigger then the height then it means that the video was taken in landscape mode and we should set the orientation to landscape
                                if (vp_videoWidth > vp_videoHeight) {
                                    //Set orientation to landscape
                                    VP_Player.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                                }
                                //If the width is smaller then the height then it means that the video was taken in portrait mode and we should set the orientation to portrait
                                if (vp_videoWidth < vp_videoHeight) {
                                    //Set orientation to portrait
                                    VP_Player.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        vp_updateSubtitleViewMargin(vp_format);
                    }
                    if (vp_duration != C.TIME_UNSET && vp_duration > TimeUnit.MINUTES.toMillis(20)) {
                        vp_timeBar.setKeyTimeIncrement(TimeUnit.MINUTES.toMillis(1));
                    } else {
                        vp_timeBar.setKeyCountIncrement(20);
                    }

                    boolean vp_switched = false;
                    if (mVPPrefs.vp_frameRateMatching) {
                        if (vp_play) {
                            if (vp_displayManager == null) {
                                vp_displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
                            }
                            if (vp_displayListener == null) {
                                vp_displayListener = new DisplayManager.DisplayListener() {
                                    @Override
                                    public void onDisplayAdded(int displayId) {

                                    }

                                    @Override
                                    public void onDisplayRemoved(int displayId) {

                                    }

                                    @Override
                                    public void onDisplayChanged(int displayId) {
                                        if (vp_play) {
                                            vp_play = false;
                                            vp_displayManager.unregisterDisplayListener(this);
                                            if (vp_player != null) {
                                                vp_player.play();
                                            }
                                            if (vp_playerView != null) {
                                                vp_playerView.hideController();
                                            }
                                        }
                                    }
                                };
                            }
                            vp_displayManager.registerDisplayListener(vp_displayListener, null);
                        }
                        vp_switched = VP_Utils.vp_switchFrameRate(VP_Player.this, mVPPrefs.vp_mediaUri, vp_play);
                    }
                    if (!vp_switched) {
                        if (vp_displayManager != null) {
                            vp_displayManager.unregisterDisplayListener(vp_displayListener);
                        }
                        if (vp_play) {
                            vp_play = false;
                            vp_player.play();
                            vp_playerView.hideController();
                        }
                    }

                    vp_updateLoading(false);

                    if (mVPPrefs.vp_speed <= 0.99f || mVPPrefs.vp_speed >= 1.01f) {
                        vp_player.setPlaybackSpeed(mVPPrefs.vp_speed);
                    }
                    if (!vp_apiAccess) {
                        vp_setSelectedTracks(mVPPrefs.vp_subtitleTrackId, mVPPrefs.vp_audioTrackId);
                    }
                }
            } else if (state == com.google.android.exoplayer2.Player.STATE_ENDED) {
                vp_playbackFinished = true;
                if (vp_apiAccess) {
                    finish();
                }
            }
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            vp_updateLoading(false);
            if (error instanceof ExoPlaybackException) {
                final ExoPlaybackException vp_exoPlaybackException = (ExoPlaybackException) error;
                if (vp_controllerVisible && vp_controllerVisibleFully) {
                    showError(vp_exoPlaybackException);
                } else {
                    vp_errorToShow = vp_exoPlaybackException;
                }
            }
        }
    }

    private TrackGroup getTrackGroupFromFormatId(int vp_trackType, String vp_id) {
        if ((vp_id == null && vp_trackType == C.TRACK_TYPE_AUDIO) || vp_player == null) {
            return null;
        }
        for (Tracks.Group group : vp_player.getCurrentTracks().getGroups()) {
            if (group.getType() == vp_trackType) {
                final TrackGroup vp_trackGroup = group.getMediaTrackGroup();
                final Format vp_format = vp_trackGroup.getFormat(0);
                if (Objects.equals(vp_id, vp_format.id)) {
                    return vp_trackGroup;
                }
            }
        }
        return null;
    }

    public void vp_setSelectedTracks(final String vp_subtitleId, final String vp_audioId) {
        if ("#none".equals(vp_subtitleId)) {
            if (vp_trackSelector == null) {
                return;
            }
            vp_trackSelector.setParameters(vp_trackSelector.buildUponParameters().setDisabledTextTrackSelectionFlags(C.SELECTION_FLAG_DEFAULT | C.SELECTION_FLAG_FORCED));
        }

        TrackGroup vp_subtitleGroup = getTrackGroupFromFormatId(C.TRACK_TYPE_TEXT, vp_subtitleId);
        TrackGroup vp_audioGroup = getTrackGroupFromFormatId(C.TRACK_TYPE_AUDIO, vp_audioId);

        TrackSelectionParameters.Builder vp_overridesBuilder = new TrackSelectionParameters.Builder(this);
        TrackSelectionOverride vp_trackSelectionOverride = null;
        final ArrayList<Integer> vp_tracks = new ArrayList<>();
        vp_tracks.add(0);
        if (vp_subtitleGroup != null) {
            vp_trackSelectionOverride = new TrackSelectionOverride(vp_subtitleGroup, vp_tracks);
            vp_overridesBuilder.addOverride(vp_trackSelectionOverride);
        }
        if (vp_audioGroup != null) {
            vp_trackSelectionOverride = new TrackSelectionOverride(vp_audioGroup, vp_tracks);
            vp_overridesBuilder.addOverride(vp_trackSelectionOverride);
        }

        if (vp_player != null) {
            TrackSelectionParameters.Builder vp_trackSelectionParametersBuilder = vp_player.getTrackSelectionParameters().buildUpon();
            if (vp_trackSelectionOverride != null) {
                vp_trackSelectionParametersBuilder.setOverrideForType(vp_trackSelectionOverride);
            }
            vp_player.setTrackSelectionParameters(vp_trackSelectionParametersBuilder.build());
        }
    }

    private boolean hasOverrideType(final int trackType) {
        TrackSelectionParameters vp_trackSelectionParameters = vp_player.getTrackSelectionParameters();
        for (TrackSelectionOverride vp_override : vp_trackSelectionParameters.overrides.values()) {
            if (vp_override.getType() == trackType)
                return true;
        }
        return false;
    }

    public String getSelectedTrack(final int trackType) {
        if (vp_player == null) {
            return null;
        }
        Tracks vp_tracks = vp_player.getCurrentTracks();

        // Disabled (e.g. selected subtitle "None" - different than default)
        if (!vp_tracks.isTypeSelected(trackType)) {
            return "#none";
        }

        // Audio track set to "Auto"
        if (trackType == C.TRACK_TYPE_AUDIO) {
            if (!hasOverrideType(C.TRACK_TYPE_AUDIO)) {
                return null;
            }
        }

        for (Tracks.Group vp_group : vp_tracks.getGroups()) {
            if (vp_group.isSelected() && vp_group.getType() == trackType) {
                Format format = vp_group.getMediaTrackGroup().getFormat(0);
                return format.id;
            }
        }

        return null;
    }

    void vp_setSubtitleTextSize() {
        vp_setSubtitleTextSize(getResources().getConfiguration().orientation);
    }

    void vp_setSubtitleTextSize(final int vp_orientation) {
        // Tweak text size as fraction size doesn't work well in portrait
        final SubtitleView vp_subtitleView = vp_playerView.getSubtitleView();
        if (vp_subtitleView != null) {
            final float vp_size;
            if (vp_orientation == Configuration.ORIENTATION_LANDSCAPE) {
                vp_size = SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * vp_subtitlesScale;
            } else {
                DisplayMetrics vp_metrics = getResources().getDisplayMetrics();
                float vp_ratio = ((float) vp_metrics.heightPixels / (float) vp_metrics.widthPixels);
                if (vp_ratio < 1)
                    vp_ratio = 1 / vp_ratio;
                vp_size = SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * vp_subtitlesScale / vp_ratio;
            }

            vp_subtitleView.setFractionalTextSize(vp_size);
        }
    }

    void vp_updateSubtitleViewMargin() {
        if (vp_player == null) {
            return;
        }

        vp_updateSubtitleViewMargin(vp_player.getVideoFormat());
    }

    // Set margins to fix PGS aspect as subtitle view is outside of content frame
    void vp_updateSubtitleViewMargin(Format vp_format) {
        if (vp_format == null) {
            return;
        }

        final Rational vp_aspectVideo = VP_Utils.vp_getRational(vp_format);
        final DisplayMetrics vp_metrics = getResources().getDisplayMetrics();
        final Rational vp_aspectDisplay = new Rational(vp_metrics.widthPixels, vp_metrics.heightPixels);

        int vp_marginHorizontal = 0;
        int vp_marginVertical = 0;

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (vp_aspectDisplay.floatValue() > vp_aspectVideo.floatValue()) {
                // Left & right bars
                int vp_videoWidth = vp_metrics.heightPixels / vp_aspectVideo.getDenominator() * vp_aspectVideo.getNumerator();
                vp_marginHorizontal = (vp_metrics.widthPixels - vp_videoWidth) / 2;
            }
        }

        VP_Utils.vp_setViewParams(vp_playerView.getSubtitleView(), 0, 0, 0, 0,
                vp_marginHorizontal, vp_marginVertical, vp_marginHorizontal, vp_marginVertical);
    }

    void setSubtitleTextSizePiP() {
        final SubtitleView vp_subtitleView = vp_playerView.getSubtitleView();
        if (vp_subtitleView != null)
            vp_subtitleView.setFractionalTextSize(SubtitleView.DEFAULT_TEXT_SIZE_FRACTION * 2);
    }

    @TargetApi(26)
    boolean vp_updatePictureInPictureActions(final int iconId, final int resTitle,
                                             final int controlType, final int requestCode) {
        try {
            final ArrayList<RemoteAction> vp_actions = new ArrayList<>();
            final PendingIntent vp_intent = PendingIntent.getBroadcast(VP_Player.this, requestCode,
                    new Intent(VP_ACTION_MEDIA_CONTROL).putExtra(VP_EXTRA_CONTROL_TYPE, controlType), PendingIntent.FLAG_IMMUTABLE);
            final Icon vp_icon = Icon.createWithResource(VP_Player.this, iconId);
            final String vp_title = getString(resTitle);
            vp_actions.add(new RemoteAction(vp_icon, vp_title, vp_title, vp_intent));
            ((PictureInPictureParams.Builder) vp_mPictureInPictureParamsBuilder).setActions(vp_actions);
            setPictureInPictureParams(((PictureInPictureParams.Builder) vp_mPictureInPictureParamsBuilder).build());
            return true;
        } catch (IllegalStateException e) {
            // On Samsung devices with Talkback active:
            // Caused by: java.lang.IllegalStateException: setPictureInPictureParams: Device doesn't support picture-in-picture mode.
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean vp_isInPip() {
        if (!VP_Utils.vp_isPiPSupported(this))
            return false;
        return isInPictureInPictureMode();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (!vp_isInPip()) {
            vp_setSubtitleTextSize(newConfig.orientation);
        }
        vp_updateSubtitleViewMargin();

        vp_updateButtonRotation();
    }

    void showError(ExoPlaybackException error) {
        final String vp_errorGeneral = error.getLocalizedMessage();
        String vp_errorDetailed;

        switch (error.type) {
            case ExoPlaybackException.TYPE_SOURCE:
                vp_errorDetailed = error.getSourceException().getLocalizedMessage();
                break;
            case ExoPlaybackException.TYPE_RENDERER:
                vp_errorDetailed = error.getRendererException().getLocalizedMessage();
                break;
            case ExoPlaybackException.TYPE_UNEXPECTED:
                vp_errorDetailed = error.getUnexpectedException().getLocalizedMessage();
                break;
            case ExoPlaybackException.TYPE_REMOTE:
            default:
                vp_errorDetailed = vp_errorGeneral;
                break;
        }

        vp_showSnack(vp_errorGeneral, vp_errorDetailed);
    }

    void vp_showSnack(final String textPrimary, final String textSecondary) {
        vp_snackbar = Snackbar.make(vp_coordinatorLayout, textPrimary, Snackbar.LENGTH_LONG);
        if (textSecondary != null) {
            vp_snackbar.setAction(R.string.error_details, v -> {
                final AlertDialog.Builder builder = new AlertDialog.Builder(VP_Player.this);
                builder.setMessage(textSecondary);
                builder.setPositiveButton(android.R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss());
                final AlertDialog dialog = builder.create();
                dialog.show();
            });
        }
        vp_snackbar.setAnchorView(R.id.exo_bottom_bar);
        vp_snackbar.show();
    }

    void vp_reportScrubbing(long position) {
        final long vp_diff = position - vp_scrubbingStart;
        if (Math.abs(vp_diff) > 1000) {
            vp_scrubbingNoticeable = true;
        }
        if (vp_scrubbingNoticeable) {
            vp_playerView.vp_clearIcon();
            vp_playerView.setCustomErrorMessage(VP_Utils.vp_formatMilisSign(vp_diff));
        }
        if (vp_frameRendered) {
            vp_frameRendered = false;
            vp_player.seekTo(position);
        }
    }

    void vp_updateSubtitleStyle(final Context context) {
        final CaptioningManager vp_captioningManager = (CaptioningManager) getSystemService(Context.CAPTIONING_SERVICE);
        final SubtitleView vp_subtitleView = vp_playerView.getSubtitleView();
        final boolean vp_isTablet = VP_Utils.vp_isTablet(context);
        vp_subtitlesScale = VP_SubtitleUtils.vp_normalizeFontScale(vp_captioningManager.getFontScale(), vp_isTvBox || vp_isTablet);
        if (vp_subtitleView != null) {
            final CaptioningManager.CaptionStyle vp_userStyle = vp_captioningManager.getUserStyle();
            final CaptionStyleCompat userStyleCompat = CaptionStyleCompat.createFromCaptionStyle(vp_userStyle);
            final CaptionStyleCompat captionStyle = new CaptionStyleCompat(
                    vp_userStyle.hasForegroundColor() ? userStyleCompat.foregroundColor : Color.WHITE,
                    vp_userStyle.hasBackgroundColor() ? userStyleCompat.backgroundColor : Color.TRANSPARENT,
                    vp_userStyle.hasWindowColor() ? userStyleCompat.windowColor : Color.TRANSPARENT,
                    vp_userStyle.hasEdgeType() ? userStyleCompat.edgeType : CaptionStyleCompat.EDGE_TYPE_OUTLINE,
                    vp_userStyle.hasEdgeColor() ? userStyleCompat.edgeColor : Color.BLACK,
                    userStyleCompat.typeface != null ? userStyleCompat.typeface : Typeface.DEFAULT_BOLD);
            vp_subtitleView.setStyle(captionStyle);

            if (vp_captioningManager.isEnabled()) {
                // Do not apply embedded style as currently the only supported color style is PrimaryColour
                // https://github.com/google/ExoPlayer/issues/8435#issuecomment-762449001
                // This may result in poorly visible text (depending on user's selected edgeColor)
                // The same can happen with style provided using setStyle but enabling CaptioningManager should be a way to change the behavior
                vp_subtitleView.setApplyEmbeddedStyles(false);
            } else {
                vp_subtitleView.setApplyEmbeddedStyles(true);
            }

            vp_subtitleView.setBottomPaddingFraction(SubtitleView.DEFAULT_BOTTOM_PADDING_FRACTION * 2f / 3f);
        }
        vp_setSubtitleTextSize();
    }

    void vp_searchSubtitles() {
        if (mVPPrefs.vp_mediaUri == null)
            return;

        if (VP_Utils.vp_isSupportedNetworkUri(mVPPrefs.vp_mediaUri) && VP_Utils.vp_isProgressiveContainerUri(mVPPrefs.vp_mediaUri)) {
            VP_SubtitleUtils.vp_clearCache(this);
            if (VP_SubtitleFinder.isUriCompatible(mVPPrefs.vp_mediaUri)) {
                VPSubtitleFinder = new VP_SubtitleFinder(VP_Player.this, mVPPrefs.vp_mediaUri);
                VPSubtitleFinder.vp_start();
            }
            return;
        }

        if (mVPPrefs.vp_scopeUri != null || vp_isTvBox) {
            DocumentFile vp_video = null;
            File vp_videoRaw = null;
            final String vp_scheme = mVPPrefs.vp_mediaUri.getScheme();

            if (mVPPrefs.vp_scopeUri != null) {
                if ("com.android.externalstorage.documents".equals(mVPPrefs.vp_mediaUri.getHost()) ||
                        "org.courville.nova.provider".equals(mVPPrefs.vp_mediaUri.getHost())) {
                    // Fast search based on path in uri
                    vp_video = VP_SubtitleUtils.vp_findUriInScope(this, mVPPrefs.vp_scopeUri, mVPPrefs.vp_mediaUri);
                } else {
                    // Slow search based on matching metadata, no path in uri
                    // Provider "com.android.providers.media.documents" when using "Videos" tab in file picker
                    DocumentFile vp_fileScope = DocumentFile.fromTreeUri(this, mVPPrefs.vp_scopeUri);
                    DocumentFile vp_fileMedia = DocumentFile.fromSingleUri(this, mVPPrefs.vp_mediaUri);
                    vp_video = VP_SubtitleUtils.vp_findDocInScope(vp_fileScope, vp_fileMedia);
                }
            } else if (ContentResolver.SCHEME_FILE.equals(vp_scheme)) {
                vp_videoRaw = new File(mVPPrefs.vp_mediaUri.getSchemeSpecificPart());
                vp_video = DocumentFile.fromFile(vp_videoRaw);
            }

            if (vp_video != null) {
                DocumentFile vp_subtitle = null;
                if (mVPPrefs.vp_scopeUri != null) {
                    vp_subtitle = VP_SubtitleUtils.vp_findSubtitle(vp_video);
                } else if (ContentResolver.SCHEME_FILE.equals(vp_scheme)) {
                    File parentRaw = vp_videoRaw.getParentFile();
                    DocumentFile dir = DocumentFile.fromFile(parentRaw);
                    vp_subtitle = VP_SubtitleUtils.vp_findSubtitle(vp_video, dir);
                }

                if (vp_subtitle != null) {
                    vp_handleSubtitles(vp_subtitle.getUri());
                }
            }
        }
    }

    Uri findNext() {
        // TODO: Unify with searchSubtitles()
        if (mVPPrefs.vp_scopeUri != null || vp_isTvBox) {
            DocumentFile vp_video = null;
            File vp_videoRaw = null;

            if (!vp_isTvBox && mVPPrefs.vp_scopeUri != null) {
                if ("com.android.externalstorage.documents".equals(mVPPrefs.vp_mediaUri.getHost())) {
                    // Fast search based on path in uri
                    vp_video = VP_SubtitleUtils.vp_findUriInScope(this, mVPPrefs.vp_scopeUri, mVPPrefs.vp_mediaUri);
                } else {
                    // Slow search based on matching metadata, no path in uri
                    // Provider "com.android.providers.media.documents" when using "Videos" tab in file picker
                    DocumentFile vp_fileScope = DocumentFile.fromTreeUri(this, mVPPrefs.vp_scopeUri);
                    DocumentFile vp_fileMedia = DocumentFile.fromSingleUri(this, mVPPrefs.vp_mediaUri);
                    vp_video = VP_SubtitleUtils.vp_findDocInScope(vp_fileScope, vp_fileMedia);
                }
            } else if (vp_isTvBox) {
                vp_videoRaw = new File(mVPPrefs.vp_mediaUri.getSchemeSpecificPart());
                vp_video = DocumentFile.fromFile(vp_videoRaw);
            }

            if (vp_video != null) {
                DocumentFile vp_next;
                if (!vp_isTvBox) {
                    vp_next = VP_SubtitleUtils.vp_findNext(vp_video);
                } else {
                    File vp_parentRaw = vp_videoRaw.getParentFile();
                    DocumentFile vp_dir = DocumentFile.fromFile(vp_parentRaw);
                    vp_next = VP_SubtitleUtils.vp_findNext(vp_video, vp_dir);
                }
                if (vp_next != null) {
                    return vp_next.getUri();
                }
            }
        }
        return null;
    }

    void vp_resetHideCallbacks() {
        if (vp_haveMedia && vp_player != null && vp_player.isPlaying()) {
            // Keep controller UI visible - alternative to resetHideCallbacks()
            vp_playerView.setControllerShowTimeoutMs(VP_Player.VP_CONTROLLER_TIMEOUT);
        }
    }

    private void vp_updateLoading(final boolean vp_enableLoading) {
        if (vp_enableLoading) {
            vp_loadingProgressBar.setVisibility(View.VISIBLE);
        } else {
            vp_loadingProgressBar.setVisibility(View.GONE);
            if (vp_focusPlay) {
                vp_focusPlay = false;
                vp_exoPlayPause.requestFocus();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onUserLeaveHint() {
        if (mVPPrefs != null && mVPPrefs.vp_autoPiP && vp_player != null && vp_player.isPlaying() && VP_Utils.vp_isPiPSupported(this))
            vp_enterPiP();
        else
            super.onUserLeaveHint();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void vp_enterPiP() {
        final AppOpsManager vp_appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        if (AppOpsManager.MODE_ALLOWED != vp_appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_PICTURE_IN_PICTURE, android.os.Process.myUid(), getPackageName())) {
            final Intent intent = new Intent("android.settings.PICTURE_IN_PICTURE_SETTINGS", Uri.fromParts("package", getPackageName(), null));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
            return;
        }

        if (vp_player == null) {
            return;
        }

        vp_playerView.setControllerAutoShow(false);
        vp_playerView.hideController();

        final Format vp_format = vp_player.getVideoFormat();

        if (vp_format != null) {
            // https://github.com/google/ExoPlayer/issues/8611
            // TODO: Test/disable on Android 11+
            final View vp_videoSurfaceView = vp_playerView.getVideoSurfaceView();
            if (vp_videoSurfaceView instanceof SurfaceView) {
                ((SurfaceView) vp_videoSurfaceView).getHolder().setFixedSize(vp_format.width, vp_format.height);
            }

            Rational vp_rational = VP_Utils.vp_getRational(vp_format);
            if (Build.VERSION.SDK_INT >= 33 &&
                    getPackageManager().hasSystemFeature(FEATURE_EXPANDED_PICTURE_IN_PICTURE) &&
                    (vp_rational.floatValue() > vp_rationalLimitWide.floatValue() || vp_rational.floatValue() < vp_rationalLimitTall.floatValue())) {
                ((PictureInPictureParams.Builder) vp_mPictureInPictureParamsBuilder).setExpandedAspectRatio(vp_rational);
            }
            if (vp_rational.floatValue() > vp_rationalLimitWide.floatValue())
                vp_rational = vp_rationalLimitWide;
            else if (vp_rational.floatValue() < vp_rationalLimitTall.floatValue())
                vp_rational = vp_rationalLimitTall;

            ((PictureInPictureParams.Builder) vp_mPictureInPictureParamsBuilder).setAspectRatio(vp_rational);
        }
        enterPictureInPictureMode(((PictureInPictureParams.Builder) vp_mPictureInPictureParamsBuilder).build());
    }

    void vp_setEndControlsVisible(boolean visible) {
        final int deleteVisible = (visible && vp_haveMedia && VP_Utils.vp_isDeletable(this, mVPPrefs.vp_mediaUri)) ? View.VISIBLE : View.INVISIBLE;
        final int nextVisible = (visible && vp_haveMedia && (vp_nextUri != null || (mVPPrefs.vp_askScope && !vp_isTvBox))) ? View.VISIBLE : View.INVISIBLE;
//        findViewById(R.id.delete).setVisibility(deleteVisible);
//        findViewById(R.id.next).setVisibility(nextVisible);
    }

    void askDeleteMedia() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(VP_Player.this);
        builder.setMessage(getString(R.string.delete_query));
        builder.setPositiveButton(R.string.delete_confirmation, (dialogInterface, i) -> {
            vp_releasePlayer();
            deleteMedia();
            if (vp_nextUri == null) {
                vp_haveMedia = false;
                vp_setEndControlsVisible(false);
                vp_playerView.setControllerShowTimeoutMs(-1);
            } else {
                skipToNext();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    void deleteMedia() {
        try {
            if (ContentResolver.SCHEME_CONTENT.equals(mVPPrefs.vp_mediaUri.getScheme())) {
                DocumentsContract.deleteDocument(getContentResolver(), mVPPrefs.vp_mediaUri);
            } else if (ContentResolver.SCHEME_FILE.equals(mVPPrefs.vp_mediaUri.getScheme())) {
                final File file = new File(mVPPrefs.vp_mediaUri.getSchemeSpecificPart());
                if (file.canWrite()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchPlayPause() {
        if (vp_player == null)
            return;

        @com.google.android.exoplayer2.Player.State int state = vp_player.getPlaybackState();
        String methodName;
        if (state == com.google.android.exoplayer2.Player.STATE_IDLE || state == com.google.android.exoplayer2.Player.STATE_ENDED || !vp_player.getPlayWhenReady()) {
            methodName = "dispatchPlay";
            vp_shortControllerTimeout = true;
        } else {
            methodName = "dispatchPause";
        }
        try {
            final Method vp_method = StyledPlayerControlView.class.getDeclaredMethod(methodName, com.google.android.exoplayer2.Player.class);
            vp_method.setAccessible(true);
            vp_method.invoke(vp_controlView, (com.google.android.exoplayer2.Player) vp_player);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                 IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    void skipToNext() {
        if (vp_nextUri != null) {
            vp_releasePlayer();
            mVPPrefs.vp_updateMedia(this, vp_nextUri, null);
            vp_searchSubtitles();
            vp_initializePlayer();
        }
    }

    void vp_notifyAudioSessionUpdate(final boolean active) {
        final Intent intent = new Intent(active ? AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION
                : AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        intent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, vp_player.getAudioSessionId());
        intent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        if (active) {
            intent.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MOVIE);
        }
        try {
            sendBroadcast(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    void vp_updateButtons(final boolean enable) {
        if (vp_buttonPiP != null) {
            VP_Utils.vp_setButtonEnabled(this, vp_buttonPiP, enable);
        }
        VP_Utils.vp_setButtonEnabled(this, vp_buttonAspectRatio, enable);
        if (vp_isTvBox) {
            VP_Utils.vp_setButtonEnabled(this, vp_exoSettings, true);
        } else {
            VP_Utils.vp_setButtonEnabled(this, vp_exoSettings, enable);
        }
    }

    private void vp_scaleStart() {
        vp_isScaling = true;
        if (vp_playerView.getResizeMode() != AspectRatioFrameLayout.RESIZE_MODE_ZOOM) {
            vp_playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        }
        vp_scaleFactor = vp_playerView.getVideoSurfaceView().getScaleX();
        vp_playerView.removeCallbacks(vp_playerView.vp_textClearRunnable);
        vp_playerView.vp_clearIcon();
        vp_playerView.setCustomErrorMessage((int) (vp_scaleFactor * 100) + "%");
        vp_playerView.hideController();
        vp_isScaleStarting = true;
    }

    private void vp_scale(boolean up) {
        if (up) {
            vp_scaleFactor += 0.01;
        } else {
            vp_scaleFactor -= 0.01;
        }
        vp_scaleFactor = VP_Utils.vp_normalizeScaleFactor(vp_scaleFactor, vp_playerView.vp_getScaleFit());
        vp_playerView.vp_setScale(vp_scaleFactor);
        vp_playerView.setCustomErrorMessage((int) (vp_scaleFactor * 100) + "%");
    }

    private void scaleEnd() {
        vp_isScaling = false;
        vp_playerView.postDelayed(vp_playerView.vp_textClearRunnable, 200);
        if (!vp_player.isPlaying()) {
            vp_playerView.showController();
        }
        if (Math.abs(vp_playerView.vp_getScaleFit() - vp_scaleFactor) < 0.01 / 2) {
            vp_playerView.vp_setScale(1.f);
            vp_playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
        }
        vp_updatebuttonAspectRatioIcon();
    }

    private void vp_updatebuttonAspectRatioIcon() {
        if (vp_playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_ZOOM) {
            vp_buttonAspectRatio.setImageResource(R.drawable.vp_ic_fit_screen_24dp);
        } else {
            vp_buttonAspectRatio.setImageResource(R.drawable.vp_ic_aspect_ratio_24dp);
        }
    }

    private void vp_updateButtonRotation() {
        boolean vp_portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        boolean vp_auto = false;
        try {
            vp_auto = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.ACCELEROMETER_ROTATION) == 1;
        } catch (android.provider.Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        if (mVPPrefs.vp_orientation == VP_Utils.Orientation.VP_VIDEO) {
            if (vp_auto) {
                vp_buttonRotation.setImageResource(R.drawable.vp_ic_screen_lock_rotation_24dp);
            } else if (vp_portrait) {
                vp_buttonRotation.setImageResource(R.drawable.vp_ic_screen_lock_portrait_24dp);
            } else {
                vp_buttonRotation.setImageResource(R.drawable.vp_ic_screen_lock_landscape_24dp);
            }
        } else {
            if (vp_auto) {
                vp_buttonRotation.setImageResource(R.drawable.vp_ic_screen_rotation_24dp);
            } else if (vp_portrait) {
                vp_buttonRotation.setImageResource(R.drawable.vp_ic_screen_portrait_24dp);
            } else {
                vp_buttonRotation.setImageResource(R.drawable.vp_ic_screen_landscape_24dp);
            }
        }
    }
}