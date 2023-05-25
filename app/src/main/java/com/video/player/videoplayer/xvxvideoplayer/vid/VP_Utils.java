package com.video.player.videoplayer.xvxvideoplayer.vid;

import static android.content.Context.UI_MODE_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.arthenica.ffmpegkit.Chapter;
import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.FFprobeKit;
import com.arthenica.ffmpegkit.MediaInformation;
import com.arthenica.ffmpegkit.MediaInformationSession;
import com.arthenica.ffmpegkit.StreamInformation;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.android.exoplayer2.util.MimeTypes;
import com.video.player.videoplayer.xvxvideoplayer.BuildConfig;
import com.video.player.videoplayer.xvxvideoplayer.R;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VP_Utils {
    public static final String VP_FEATURE_FIRE_TV = "amazon.hardware.fire_tv";

    public static final String[] vp_supportedExtensionsVideo = new String[]{"3gp", "avi", "m4v", "mkv", "mov", "mp4", "ts", "webm"};
    public static final String[] vp_supportedExtensionsSubtitle = new String[]{"srt", "ssa", "ass", "vtt", "ttml", "dfxp", "xml"};

    public static final String[] vp_supportedMimeTypesVideo = new String[]{
            // Local mime types on Android:
            MimeTypes.VIDEO_MATROSKA, // .mkv
            MimeTypes.VIDEO_MP4, // .mp4, .m4v
            MimeTypes.VIDEO_WEBM, // .webm
            "video/quicktime", // .mov
            "video/mp2ts", // .ts, but also incompatible .m2ts
            MimeTypes.VIDEO_H263, // .3gp
            "video/avi",
            // For remote storages:
            "video/x-m4v", // .m4v
    };
    public static final String[] vp_supportedMimeTypesSubtitle = new String[]{
            MimeTypes.APPLICATION_SUBRIP,
            MimeTypes.TEXT_SSA,
            MimeTypes.TEXT_VTT,
            MimeTypes.APPLICATION_TTML,
            "text/*",
            "application/octet-stream"
    };

    public static int vp_dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float vp_pxToDp(float px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    public static boolean vp_fileExists(final Context context, final Uri uri) {
        final String vp_scheme = uri.getScheme();
        if (ContentResolver.SCHEME_CONTENT.equals(vp_scheme)) {
            try {
                final InputStream vp_inputStream = context.getContentResolver().openInputStream(uri);
                vp_inputStream.close();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            String vp_path;
            if (ContentResolver.SCHEME_FILE.equals(vp_scheme)) {
                vp_path = uri.getPath();
            } else {
                vp_path = uri.toString();
            }
            final File vp_file = new File(vp_path);
            return vp_file.exists();
        }
    }

    public static void vp_toggleSystemUi(final Activity activity, final VP_CustomStyledPlayerView playerView, final boolean show) {
        if (Build.VERSION.SDK_INT >= 31) {
            Window vp_window = activity.getWindow();
            if (vp_window != null) {
                WindowInsetsController vp_windowInsetsController = vp_window.getInsetsController();
                if (vp_windowInsetsController != null) {
                    if (show) {
                        vp_windowInsetsController.show(WindowInsets.Type.systemBars());
                    } else {
                        vp_windowInsetsController.hide(WindowInsets.Type.systemBars());
                    }
                }
            }
        } else {
            if (show) {
                playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        }
    }

    public static String vp_getFileName(Context context, Uri uri) {
        String vp_result = null;
        try {
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                try (Cursor vp_cursor = context.getContentResolver().query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
                    if (vp_cursor != null && vp_cursor.moveToFirst()) {
                        final int vp_columnIndex = vp_cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (vp_columnIndex > -1)
                            vp_result = vp_cursor.getString(vp_columnIndex);
                    }
                }
            }
            if (vp_result == null) {
                vp_result = uri.getPath();
                int cut = vp_result.lastIndexOf('/');
                if (cut != -1) {
                    vp_result = vp_result.substring(cut + 1);
                }
            }
            if (vp_result.indexOf(".") > 0)
                vp_result = vp_result.substring(0, vp_result.lastIndexOf("."));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vp_result;
    }

    public static boolean vp_isVolumeMax(final AudioManager audioManager) {
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    public static boolean vp_isVolumeMin(final AudioManager audioManager) {
        int min = Build.VERSION.SDK_INT >= 28 ? audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC) : 0;
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) == min;
    }

    public static void vp_adjustVolume(final Context context, final AudioManager audioManager, final VP_CustomStyledPlayerView playerView, final boolean raise, boolean canBoost, boolean clear) {
        playerView.removeCallbacks(playerView.vp_textClearRunnable);

        final int vp_volume = vp_getVolume(context, false, audioManager);
        final int vp_volumeMax = vp_getVolume(context, true, audioManager);
        boolean vp_volumeActive = vp_volume != 0;

        // Handle volume changes outside the app (lose boost if volume is not maxed out)
        if (vp_volume != vp_volumeMax) {
            VP_Player.vp_boostLevel = 0;
        }

        if (VP_Player.vp_loudnessEnhancer == null)
            canBoost = false;

        if (vp_volume != vp_volumeMax || (VP_Player.vp_boostLevel == 0 && !raise)) {
            if (VP_Player.vp_loudnessEnhancer != null)
                VP_Player.vp_loudnessEnhancer.setEnabled(false);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, raise ? AudioManager.ADJUST_RAISE : AudioManager.ADJUST_LOWER, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            final int vp_volumeNew = vp_getVolume(context, false, audioManager);
            // Custom volume step on Samsung devices (Sound Assistant)
            if (raise && vp_volume == vp_volumeNew) {
                playerView.vp_volumeUpsInRow++;
            } else {
                playerView.vp_volumeUpsInRow = 0;
            }
            if (playerView.vp_volumeUpsInRow > 4 && !vp_isVolumeMin(audioManager)) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE | AudioManager.FLAG_SHOW_UI);
            } else {
                vp_volumeActive = vp_volumeNew != 0;
                playerView.setCustomErrorMessage(vp_volumeActive ? " " + vp_volumeNew : "");
            }
        } else {
            if (canBoost && raise && VP_Player.vp_boostLevel < 10)
                VP_Player.vp_boostLevel++;
            else if (!raise && VP_Player.vp_boostLevel > 0)
                VP_Player.vp_boostLevel--;

            if (VP_Player.vp_loudnessEnhancer != null) {
                try {
                    VP_Player.vp_loudnessEnhancer.setTargetGain(VP_Player.vp_boostLevel * 200);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
            playerView.setCustomErrorMessage(" " + (vp_volumeMax + VP_Player.vp_boostLevel));
        }

        playerView.vp_setIconVolume(vp_volumeActive);
        if (VP_Player.vp_loudnessEnhancer != null)
            VP_Player.vp_loudnessEnhancer.setEnabled(VP_Player.vp_boostLevel > 0);
        playerView.vp_setHighlight(VP_Player.vp_boostLevel > 0);

        if (clear) {
            playerView.postDelayed(playerView.vp_textClearRunnable, VP_CustomStyledPlayerView.VP_MESSAGE_TIMEOUT_KEY);
        }
    }

    private static int vp_getVolume(final Context context, final boolean max, final AudioManager audioManager) {
        if (Build.VERSION.SDK_INT >= 30 && Build.VERSION.SDK_INT <= 31 && Build.MANUFACTURER.equalsIgnoreCase("samsung")) {
            try {
                Method vp_method;
                Object vp_result;
                Class<?> vp_clazz = Class.forName("com.samsung.android.media.SemSoundAssistantManager");
                Constructor<?> vp_constructor = vp_clazz.getConstructor(Context.class);
                final Method vp_getMediaVolumeInterval = vp_clazz.getDeclaredMethod("getMediaVolumeInterval");
                vp_result = vp_getMediaVolumeInterval.invoke(vp_constructor.newInstance(context));
                if (vp_result instanceof Integer) {
                    int vp_mediaVolumeInterval = (int) vp_result;
                    if (vp_mediaVolumeInterval < 10) {
                        vp_method = AudioManager.class.getDeclaredMethod("semGetFineVolume", int.class);
                        vp_result = vp_method.invoke(audioManager, AudioManager.STREAM_MUSIC);
                        if (vp_result instanceof Integer) {
                            if (max) {
                                return 150 / vp_mediaVolumeInterval;
                            } else {
                                int fineVolume = (int) vp_result;
                                return fineVolume / vp_mediaVolumeInterval;
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        if (max) {
            return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } else {
            return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
    }

    public static void vp_setButtonEnabled(final Context context, final ImageButton button, final boolean enabled) {
        button.setEnabled(enabled);
        button.setAlpha(enabled ?
                (float) context.getResources().getInteger(R.integer.exo_media_button_opacity_percentage_enabled) / 100 :
                (float) context.getResources().getInteger(R.integer.exo_media_button_opacity_percentage_disabled) / 100
        );
    }

    public static void vp_showText(final VP_CustomStyledPlayerView playerView, final String text, final long timeout) {
        playerView.removeCallbacks(playerView.vp_textClearRunnable);
        playerView.vp_clearIcon();
        playerView.setCustomErrorMessage(text);
        playerView.postDelayed(playerView.vp_textClearRunnable, timeout);
    }

    public static void vp_showText(final VP_CustomStyledPlayerView playerView, final String text) {
        vp_showText(playerView, text, 1200);
    }

    public enum Orientation {
        VP_VIDEO(0, R.string.video_orientation_video),
        VP_SYSTEM(1, R.string.video_orientation_system),
        VP_UNSPECIFIED(2, R.string.video_orientation_system);

        public final int vp_value;
        public final int vp_description;

        Orientation(int type, int vp_description) {
            this.vp_value = type;
            this.vp_description = vp_description;
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public static void vp_setOrientation(Activity activity, Orientation orientation) {
        switch (orientation) {
            case VP_VIDEO:
                if (VP_Player.vp_player != null) {
                    final Format format = VP_Player.vp_player.getVideoFormat();
                    if (format != null && vp_isPortrait(format))
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    else
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                }

                break;
            case VP_SYSTEM:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                break;
            /*case SENSOR:
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;*/
        }
    }

    public static Orientation vp_getNextOrientation(Orientation vp_orientation) {
        switch (vp_orientation) {
            case VP_VIDEO:
                return Orientation.VP_SYSTEM;
            case VP_SYSTEM:
            default:
                return Orientation.VP_VIDEO;
        }
    }

    public static boolean vp_isRotated(final Format vp_format) {
        return vp_format.rotationDegrees == 90 || vp_format.rotationDegrees == 270;
    }

    public static boolean vp_isPortrait(final Format vp_format) {
        if (vp_isRotated(vp_format)) {
            return vp_format.width > vp_format.height;
        } else {
            return vp_format.height > vp_format.width;
        }
    }

    public static Rational vp_getRational(final Format format) {
        if (vp_isRotated(format))
            return new Rational(format.height, format.width);
        else
            return new Rational(format.width, format.height);
    }

    public static String vp_formatMilis(long time) {
        final int vp_totalSeconds = Math.abs((int) time / 1000);
        final int vp_seconds = vp_totalSeconds % 60;
        final int vp_minutes = vp_totalSeconds % 3600 / 60;
        final int vp_hours = vp_totalSeconds / 3600;

        return (vp_hours > 0 ? String.format("%d:%02d:%02d", vp_hours, vp_minutes, vp_seconds) : String.format("%02d:%02d", vp_minutes, vp_seconds));
    }

    public static String vp_formatMilisSign(long vp_time) {
        if (vp_time > -1000 && vp_time < 1000)
            return vp_formatMilis(vp_time);
        else
            return (vp_time < 0 ? "−" : "+") + vp_formatMilis(vp_time);
    }

    public static void vp_log(final String text) {
        if (BuildConfig.DEBUG) {
            Log.d("JustPlayer", text);
        }
    }

    public static void vp_setViewMargins(final View view, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        final FrameLayout.LayoutParams vp_layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        vp_layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        view.setLayoutParams(vp_layoutParams);
    }

    public static void vp_setViewParams(final View vp_view, int vp_paddingLeft, int vp_paddingTop, int vp_paddingRight, int vp_paddingBottom, int vp_marginLeft, int vp_marginTop, int vp_marginRight, int vp_marginBottom) {
        vp_view.setPadding(vp_paddingLeft, vp_paddingTop, vp_paddingRight, vp_paddingBottom);
        vp_setViewMargins(vp_view, vp_marginLeft, vp_marginTop, vp_marginRight, vp_marginBottom);
    }

    public static boolean vp_isDeletable(final Context context, final Uri uri) {
        try {
            if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                try (Cursor cursor = context.getContentResolver().query(uri, new String[]{DocumentsContract.Document.COLUMN_FLAGS}, null, null, null)) {
                    if (cursor != null && cursor.moveToFirst()) {
                        final int columnIndex = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_FLAGS);
                        if (columnIndex > -1) {
                            int flags = cursor.getInt(columnIndex);
                            return (flags & DocumentsContract.Document.FLAG_SUPPORTS_DELETE) == DocumentsContract.Document.FLAG_SUPPORTS_DELETE;
                        }
                    }
                }
            } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
                if (Build.VERSION.SDK_INT >= 23) {
                    boolean hasPermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED;
                    if (!hasPermission) {
                        return false;
                    }
                }
                final File file = new File(uri.getSchemeSpecificPart());
                return file.canWrite();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean vp_isSupportedNetworkUri(final Uri uri) {
        final String scheme = uri.getScheme();
        if (scheme == null)
            return false;
        return scheme.startsWith("http") || scheme.equals("rtsp");
    }

    public static boolean vp_isTvBox(Context context) {
        final PackageManager vp_pm = context.getPackageManager();

        // TV for sure
        UiModeManager vp_uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
        if (vp_uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true;
        }

        if (vp_pm.hasSystemFeature(VP_FEATURE_FIRE_TV)) {
            return true;
        }

        // Missing Files app (DocumentsUI) means box (some boxes still have non functional app or stub)
        if (!vp_hasSAFChooser(vp_pm)) {
            return true;
        }

        // Legacy storage no longer works on Android 11 (level 30)
        if (Build.VERSION.SDK_INT < 30) {
            // (Some boxes still report touchscreen feature)
            if (!vp_pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN)) {
                return true;
            }

            if (vp_pm.hasSystemFeature("android.hardware.hdmi.cec")) {
                return true;
            }

            if (Build.MANUFACTURER.equalsIgnoreCase("zidoo")) {
                return true;
            }
        }

        // Default: No TV - use SAF
        return false;
    }

    public static boolean vp_hasSAFChooser(final PackageManager pm) {
        final Intent vp_intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        vp_intent.addCategory(Intent.CATEGORY_OPENABLE);
        vp_intent.setType("video/*");
        return vp_intent.resolveActivity(pm) != null;
    }

    public static int vp_normRate(float rate) {
        return (int) (rate * 100f);
    }

    public static boolean vp_switchFrameRate(final VP_Player vp_activity, final Uri vp_uri, final boolean vp_play) {
        // preferredDisplayModeId only available on SDK 23+
        // ExoPlayer already uses Surface.setFrameRate() on Android 11+
        if (Build.VERSION.SDK_INT >= 23) {
            if (vp_activity.vp_frameRateSwitchThread != null) {
                vp_activity.vp_frameRateSwitchThread.interrupt();
            }
            vp_activity.vp_frameRateSwitchThread = new Thread(() -> {
                // Use ffprobe as ExoPlayer doesn't detect video frame rate for lots of videos
                // and has different precision than ffprobe (so do not mix that)
                float vp_frameRate = Format.NO_VALUE;
                MediaInformation vp_mediaInformation = vp_getMediaInformation(vp_activity, vp_uri);
                if (vp_mediaInformation == null) {
                    vp_activity.runOnUiThread(() -> {
                        vp_playIfCan(vp_activity, vp_play);
                    });
                    return;
                }
                List<StreamInformation> streamInformations = vp_mediaInformation.getStreams();
                for (StreamInformation vp_streamInformation : streamInformations) {
                    if (vp_streamInformation.getType().equals("video")) {
                        String vp_averageFrameRate = vp_streamInformation.getAverageFrameRate();
                        if (vp_averageFrameRate.contains("/")) {
                            String[] vp_vals = vp_averageFrameRate.split("/");
                            vp_frameRate = Float.parseFloat(vp_vals[0]) / Float.parseFloat(vp_vals[1]);
                            break;
                        }
                    }
                }
                vp_handleFrameRate(vp_activity, vp_frameRate, vp_play);
            });
            vp_activity.vp_frameRateSwitchThread.start();
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void vp_handleFrameRate(final VP_Player activity, float frameRate, boolean play) {
        activity.runOnUiThread(() -> {
            boolean vp_switchingModes = false;

            if (BuildConfig.DEBUG)
                Toast.makeText(activity, "Video frameRate: " + frameRate, Toast.LENGTH_LONG).show();

            if (frameRate > 0) {
                Display vp_display = activity.getWindow().getDecorView().getDisplay();
                if (vp_display == null) {
                    return;
                }
                Display.Mode[] vp_supportedModes = vp_display.getSupportedModes();
                Display.Mode vp_activeMode = vp_display.getMode();

                if (vp_supportedModes.length > 1) {
                    // Refresh rate >= video FPS
                    List<Display.Mode> modesHigh = new ArrayList<>();
                    // Max refresh rate
                    Display.Mode vp_modeTop = vp_activeMode;
                    int modesResolutionCount = 0;

                    // Filter only resolutions same as current
                    for (Display.Mode vp_mode : vp_supportedModes) {
                        if (vp_mode.getPhysicalWidth() == vp_activeMode.getPhysicalWidth() &&
                                vp_mode.getPhysicalHeight() == vp_activeMode.getPhysicalHeight()) {
                            modesResolutionCount++;

                            if (vp_normRate(vp_mode.getRefreshRate()) >= vp_normRate(frameRate))
                                modesHigh.add(vp_mode);

                            if (vp_normRate(vp_mode.getRefreshRate()) > vp_normRate(vp_modeTop.getRefreshRate()))
                                vp_modeTop = vp_mode;
                        }
                    }

                    if (modesResolutionCount > 1) {
                        Display.Mode vp_modeBest = null;
                        String vp_modes = "Available refreshRates:";

                        for (Display.Mode vp_mode : modesHigh) {
                            vp_modes += " " + vp_mode.getRefreshRate();
                            if (vp_normRate(vp_mode.getRefreshRate()) % vp_normRate(frameRate) <= 0.0001f) {
                                if (vp_modeBest == null || vp_normRate(vp_mode.getRefreshRate()) > vp_normRate(vp_modeBest.getRefreshRate())) {
                                    vp_modeBest = vp_mode;
                                }
                            }
                        }

                        Window vp_window = activity.getWindow();
                        WindowManager.LayoutParams vp_layoutParams = vp_window.getAttributes();

                        if (vp_modeBest == null)
                            vp_modeBest = vp_modeTop;

                        vp_switchingModes = !(vp_modeBest.getModeId() == vp_activeMode.getModeId());
                        if (vp_switchingModes) {
                            vp_layoutParams.preferredDisplayModeId = vp_modeBest.getModeId();
                            vp_window.setAttributes(vp_layoutParams);
                        }
                        if (BuildConfig.DEBUG)
                            Toast.makeText(activity, vp_modes + "\n" +
                                    "Video frameRate: " + frameRate + "\n" +
                                    "Current display refreshRate: " + vp_modeBest.getRefreshRate(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            if (!vp_switchingModes) {
                vp_playIfCan(activity, play);
            }
        });
    }

    private static void vp_playIfCan(final VP_Player vp_activity, boolean vp_play) {
        if (vp_play) {
            if (VP_Player.vp_player != null)
                VP_Player.vp_player.play();
            if (vp_activity.vp_playerView != null)
                vp_activity.vp_playerView.hideController();
        }
    }

    public static boolean vp_isPiPSupported(Context vp_context) {
        PackageManager packageManager = vp_context.getPackageManager();
        if ("amazon".equals("amazon") && packageManager.hasSystemFeature(VP_FEATURE_FIRE_TV)) {
            return false;
        }
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
    }

    public static Uri vp_getMoviesFolderUri() {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 26) {
            final String authority = "com.android.externalstorage.documents";
            final String documentId = "primary:" + Environment.DIRECTORY_MOVIES;
            uri = DocumentsContract.buildDocumentUri(authority, documentId);
        }
        return uri;
    }

    public static boolean vp_isProgressiveContainerUri(final Uri uri) {
        String vp_path = uri.getPath();
        if (vp_path == null) {
            return false;
        }
        vp_path = vp_path.toLowerCase();
        for (String vp_extension : vp_supportedExtensionsVideo) {
            if (vp_path.endsWith(vp_extension)) {
                return true;
            }
        }
        return false;
    }

    public static String[] vp_getDeviceLanguages() {
        final List<String> vp_locales = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 24) {
            final LocaleList vp_localeList = Resources.getSystem().getConfiguration().getLocales();
            for (int vp_i = 0; vp_i < vp_localeList.size(); vp_i++) {
                vp_locales.add(vp_localeList.get(vp_i).getISO3Language());
            }
        } else {
            final Locale vp_locale = Resources.getSystem().getConfiguration().locale;
            vp_locales.add(vp_locale.getISO3Language());
        }
        return vp_locales.toArray(new String[0]);
    }

    public static ComponentName vp_getSystemComponent(Context context, Intent intent) {
        List<ResolveInfo> vp_resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
        if (vp_resolveInfos.size() < 2) {
            return null;
        }
        int vp_systemCount = 0;
        ComponentName vp_componentName = null;
        for (ResolveInfo vp_resolveInfo : vp_resolveInfos) {
            int vp_flags = vp_resolveInfo.activityInfo.applicationInfo.flags;
            boolean vp_system = (vp_flags & ApplicationInfo.FLAG_SYSTEM) != 0;
            if (vp_system) {
                vp_systemCount++;
                vp_componentName = new ComponentName(vp_resolveInfo.activityInfo.packageName, vp_resolveInfo.activityInfo.name);
            }
        }
        if (vp_systemCount == 1) {
            return vp_componentName;
        }
        return null;
    }

    public static float vp_normalizeScaleFactor(float scaleFactor, float min) {
        return Math.max(min, Math.min(scaleFactor, 2.0f));
    }

    private static MediaInformation vp_getMediaInformation(final Activity activity, final Uri uri) {
        String vp_path;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            try {
                vp_path = FFmpegKitConfig.getSafParameterForRead(activity, uri);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            // TODO: FFprobeKit doesn't accept encoded uri (like %20) (?!)
            vp_path = uri.getSchemeSpecificPart();
        } else {
            vp_path = uri.toString();
        }
        MediaInformationSession vp_mediaInformationSession = FFprobeKit.getMediaInformation(vp_path);
        return vp_mediaInformationSession.getMediaInformation();
    }

    public static void vp_markChapters(final VP_Player vp_activity, final Uri vp_uri, StyledPlayerControlView vp_controlView) {
        if (vp_activity.vp_chaptersThread != null) {
            vp_activity.vp_chaptersThread.interrupt();
        }
        vp_activity.vp_chaptersThread = new Thread(() -> {
            MediaInformation vp_mediaInformation = vp_getMediaInformation(vp_activity, vp_uri);
            if (vp_mediaInformation == null)
                return;
            final List<Chapter> vp_chapters = vp_mediaInformation.getChapters();
            final long[] vp_starts = new long[vp_chapters.size()];
            final boolean[] vp_played = new boolean[vp_chapters.size()];

            for (int vp_i = 0; vp_i < vp_chapters.size(); vp_i++) {
                Chapter vp_chapter = vp_chapters.get(vp_i);
                final long vp_start = vp_chapter.getStart();
                if (vp_start > 0) {
                    vp_starts[vp_i] = vp_start / 1_000_000;
                    vp_played[vp_i] = true;
                }
            }
            vp_activity.vp_chapterStarts = vp_starts;
            vp_activity.runOnUiThread(() -> vp_controlView.setExtraAdGroupMarkers(vp_starts, vp_played));
        });
        vp_activity.vp_chaptersThread.start();
    }

    public static boolean vp_isTablet(Context context) {
        return context.getResources().getConfiguration().smallestScreenWidthDp >= 720;
    }
}
