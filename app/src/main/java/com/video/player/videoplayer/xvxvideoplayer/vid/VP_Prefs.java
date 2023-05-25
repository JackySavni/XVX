package com.video.player.videoplayer.xvxvideoplayer.vid;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Set;

class VP_Prefs {
    // Previously used
    // private static final String PREF_KEY_AUDIO_TRACK = "audioTrack";
    // private static final String PREF_KEY_AUDIO_TRACK_FFMPEG = "audioTrackFfmpeg";
    // private static final String PREF_KEY_SUBTITLE_TRACK = "subtitleTrack";

    private static final String VP_PREF_KEY_MEDIA_URI = "mediaUri";
    private static final String VP_PREF_KEY_MEDIA_TYPE = "mediaType";
    private static final String VP_PREF_KEY_BRIGHTNESS = "brightness";
    private static final String VP_PREF_KEY_FIRST_RUN = "firstRun";
    private static final String VP_PREF_KEY_SUBTITLE_URI = "subtitleUri";

    private static final String VP_PREF_KEY_AUDIO_TRACK_ID = "audioTrackId";
    private static final String VP_PREF_KEY_SUBTITLE_TRACK_ID = "subtitleTrackId";
    private static final String VP_PREF_KEY_RESIZE_MODE = "resizeMode";
    private static final String VP_PREF_KEY_ORIENTATION = "orientation";
    private static final String VP_PREF_KEY_SCALE = "scale";
    private static final String VP_PREF_KEY_SCOPE_URI = "scopeUri";
    private static final String VP_PREF_KEY_ASK_SCOPE = "askScope";
    private static final String VP_PREF_KEY_AUTO_PIP = "autoPiP";
    private static final String VP_PREF_KEY_TUNNELING = "tunneling";
    private static final String VP_PREF_KEY_SKIP_SILENCE = "skipSilence";
    private static final String VP_PREF_KEY_FRAMERATE_MATCHING = "frameRateMatching";
    private static final String VP_PREF_KEY_REPEAT_TOGGLE = "repeatToggle";
    private static final String VP_PREF_KEY_SPEED = "speed";
    private static final String VP_PREF_KEY_FILE_ACCESS = "fileAccess";
    private static final String VP_PREF_KEY_DECODER_PRIORITY = "decoderPriority";

    final Context vp_mContext;
    final SharedPreferences vp_mSharedPreferences;

    public Uri vp_mediaUri;
    public Uri vp_subtitleUri;
    public Uri vp_scopeUri;
    public String vp_mediaType;
    public int vp_resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT;
    public VP_Utils.Orientation vp_orientation = VP_Utils.Orientation.VP_UNSPECIFIED;
    public float vp_scale = 1.f;
    public float vp_speed = 1.f;

    public String vp_subtitleTrackId;
    public String vp_audioTrackId;

    public int vp_brightness = -1;
    public boolean vp_firstRun = true;
    public boolean vp_askScope = true;
    public boolean vp_autoPiP = false;

    public boolean vp_tunneling = false;
    public boolean vp_skipSilence = false;
    public boolean vp_frameRateMatching = false;
    public boolean vp_repeatToggle = false;
    public String vp_fileAccess = "auto";
    public int vp_decoderPriority = DefaultRenderersFactory.EXTENSION_RENDERER_MODE_ON;

    private LinkedHashMap vp_positions;

    public boolean vp_persistentMode = true;
    public long vp_nonPersitentPosition = -1L;

    public VP_Prefs(Context context) {
        vp_mContext = context;
        vp_mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        vp_loadSavedPreferences();
        vp_loadPositions();
    }

    private void vp_loadSavedPreferences() {
        if (vp_mSharedPreferences.contains(VP_PREF_KEY_MEDIA_URI))
            vp_mediaUri = Uri.parse(vp_mSharedPreferences.getString(VP_PREF_KEY_MEDIA_URI, null));
        if (vp_mSharedPreferences.contains(VP_PREF_KEY_MEDIA_TYPE))
            vp_mediaType = vp_mSharedPreferences.getString(VP_PREF_KEY_MEDIA_TYPE, null);
        vp_brightness = vp_mSharedPreferences.getInt(VP_PREF_KEY_BRIGHTNESS, vp_brightness);
        vp_firstRun = vp_mSharedPreferences.getBoolean(VP_PREF_KEY_FIRST_RUN, vp_firstRun);
        if (vp_mSharedPreferences.contains(VP_PREF_KEY_SUBTITLE_URI))
            vp_subtitleUri = Uri.parse(vp_mSharedPreferences.getString(VP_PREF_KEY_SUBTITLE_URI, null));
        if (vp_mSharedPreferences.contains(VP_PREF_KEY_AUDIO_TRACK_ID))
            vp_audioTrackId = vp_mSharedPreferences.getString(VP_PREF_KEY_AUDIO_TRACK_ID, vp_audioTrackId);
        if (vp_mSharedPreferences.contains(VP_PREF_KEY_SUBTITLE_TRACK_ID))
            vp_subtitleTrackId = vp_mSharedPreferences.getString(VP_PREF_KEY_SUBTITLE_TRACK_ID, vp_subtitleTrackId);
        if (vp_mSharedPreferences.contains(VP_PREF_KEY_RESIZE_MODE))
            vp_resizeMode = vp_mSharedPreferences.getInt(VP_PREF_KEY_RESIZE_MODE, vp_resizeMode);
        vp_orientation = VP_Utils.Orientation.values()[vp_mSharedPreferences.getInt(VP_PREF_KEY_ORIENTATION, vp_orientation.vp_value)];
        vp_scale = vp_mSharedPreferences.getFloat(VP_PREF_KEY_SCALE, vp_scale);
        if (vp_mSharedPreferences.contains(VP_PREF_KEY_SCOPE_URI))
            vp_scopeUri = Uri.parse(vp_mSharedPreferences.getString(VP_PREF_KEY_SCOPE_URI, null));
        vp_askScope = vp_mSharedPreferences.getBoolean(VP_PREF_KEY_ASK_SCOPE, vp_askScope);
        vp_speed = vp_mSharedPreferences.getFloat(VP_PREF_KEY_SPEED, vp_speed);
        vp_loadUserPreferences();
    }

    public void vp_loadUserPreferences() {
        vp_autoPiP = vp_mSharedPreferences.getBoolean(VP_PREF_KEY_AUTO_PIP, vp_autoPiP);
        vp_tunneling = vp_mSharedPreferences.getBoolean(VP_PREF_KEY_TUNNELING, vp_tunneling);
        vp_skipSilence = vp_mSharedPreferences.getBoolean(VP_PREF_KEY_SKIP_SILENCE, vp_skipSilence);
        vp_frameRateMatching = vp_mSharedPreferences.getBoolean(VP_PREF_KEY_FRAMERATE_MATCHING, vp_frameRateMatching);
        vp_repeatToggle = vp_mSharedPreferences.getBoolean(VP_PREF_KEY_REPEAT_TOGGLE, vp_repeatToggle);
        vp_fileAccess = vp_mSharedPreferences.getString(VP_PREF_KEY_FILE_ACCESS, vp_fileAccess);
        vp_decoderPriority = Integer.parseInt(vp_mSharedPreferences.getString(VP_PREF_KEY_DECODER_PRIORITY, String.valueOf(vp_decoderPriority)));
    }

    public void vp_updateMedia(final Context context, final Uri uri, final String type) {
        vp_mediaUri = uri;
        vp_mediaType = type;
        vp_updateSubtitle(null);
        vp_updateMeta(null, null, AspectRatioFrameLayout.RESIZE_MODE_FIT, 1.f, 1.f);

        if (vp_mediaType != null && vp_mediaType.endsWith("/*")) {
            vp_mediaType = null;
        }

        if (vp_mediaType == null) {
            if (ContentResolver.SCHEME_CONTENT.equals(vp_mediaUri.getScheme())) {
                vp_mediaType = context.getContentResolver().getType(vp_mediaUri);
            }
        }

        if (vp_persistentMode) {
            final SharedPreferences.Editor vp_sharedPreferencesEditor = vp_mSharedPreferences.edit();
            if (vp_mediaUri == null)
                vp_sharedPreferencesEditor.remove(VP_PREF_KEY_MEDIA_URI);
            else
                vp_sharedPreferencesEditor.putString(VP_PREF_KEY_MEDIA_URI, vp_mediaUri.toString());
            if (vp_mediaType == null)
                vp_sharedPreferencesEditor.remove(VP_PREF_KEY_MEDIA_TYPE);
            else
                vp_sharedPreferencesEditor.putString(VP_PREF_KEY_MEDIA_TYPE, vp_mediaType);
            vp_sharedPreferencesEditor.apply();
        }
    }

    public void vp_updateSubtitle(final Uri uri) {
        vp_subtitleUri = uri;
        vp_subtitleTrackId = null;
        if (vp_persistentMode) {
            final SharedPreferences.Editor vp_sharedPreferencesEditor = vp_mSharedPreferences.edit();
            if (uri == null)
                vp_sharedPreferencesEditor.remove(VP_PREF_KEY_SUBTITLE_URI);
            else
                vp_sharedPreferencesEditor.putString(VP_PREF_KEY_SUBTITLE_URI, uri.toString());
            vp_sharedPreferencesEditor.remove(VP_PREF_KEY_SUBTITLE_TRACK_ID);
            vp_sharedPreferencesEditor.apply();
        }
    }

    public void vp_updatePosition(final long position) {
        if (vp_mediaUri == null)
            return;

        while (vp_positions.size() > 100)
            vp_positions.remove(vp_positions.keySet().toArray()[0]);

        if (vp_persistentMode) {
            vp_positions.put(vp_mediaUri.toString(), position);
            vp_savePositions();
        } else {
            vp_nonPersitentPosition = position;
        }
    }

    public void vp_updateBrightness(final int vp_brightness) {
        if (vp_brightness >= -1) {
            this.vp_brightness = vp_brightness;
            final SharedPreferences.Editor vp_sharedPreferencesEditor = vp_mSharedPreferences.edit();
            vp_sharedPreferencesEditor.putInt(VP_PREF_KEY_BRIGHTNESS, vp_brightness);
            vp_sharedPreferencesEditor.apply();
        }
    }

    public void markFirstRun() {
        this.vp_firstRun = false;
        final SharedPreferences.Editor sharedPreferencesEditor = vp_mSharedPreferences.edit();
        sharedPreferencesEditor.putBoolean(VP_PREF_KEY_FIRST_RUN, false);
        sharedPreferencesEditor.apply();
    }

    public void vp_markScopeAsked() {
        this.vp_askScope = false;
        final SharedPreferences.Editor vp_sharedPreferencesEditor = vp_mSharedPreferences.edit();
        vp_sharedPreferencesEditor.putBoolean(VP_PREF_KEY_ASK_SCOPE, false);
        vp_sharedPreferencesEditor.apply();
    }

    private void vp_savePositions() {
        try {
            FileOutputStream vp_fos = vp_mContext.openFileOutput("positions", Context.MODE_PRIVATE);
            ObjectOutputStream vp_os = new ObjectOutputStream(vp_fos);
            vp_os.writeObject(vp_positions);
            vp_os.close();
            vp_fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vp_loadPositions() {
        try {
            FileInputStream vp_fis = vp_mContext.openFileInput("positions");
            ObjectInputStream vp_is = new ObjectInputStream(vp_fis);
            vp_positions = (LinkedHashMap) vp_is.readObject();
            vp_is.close();
            vp_fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            vp_positions = new LinkedHashMap(10);
        }
    }

    public long getPosition() {
        if (!vp_persistentMode) {
            return vp_nonPersitentPosition;
        }

        Object vp_val = vp_positions.get(vp_mediaUri.toString());
        if (vp_val != null)
            return (long) vp_val;

        // Return position for uri from limited scope (loaded after using Next action)
        if (ContentResolver.SCHEME_CONTENT.equals(vp_mediaUri.getScheme())) {
            final String vp_searchPath = VP_SubtitleUtils.getTrailPathFromUri(vp_mediaUri);
            if (vp_searchPath == null || vp_searchPath.length() < 1)
                return 0L;
            final Set<String> vp_keySet = vp_positions.keySet();
            final Object[] vp_keys = vp_keySet.toArray();
            for (int i = vp_keys.length; i > 0; i--) {
                final String key = (String) vp_keys[i - 1];
                final Uri uri = Uri.parse(key);
                if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                    final String keyPath = VP_SubtitleUtils.getTrailPathFromUri(uri);
                    if (vp_searchPath.equals(keyPath)) {
                        return (long) vp_positions.get(key);
                    }
                }
            }
        }

        return 0L;
    }

    public void vp_updateOrientation() {
        final SharedPreferences.Editor vp_sharedPreferencesEditor = vp_mSharedPreferences.edit();
        vp_sharedPreferencesEditor.putInt(VP_PREF_KEY_ORIENTATION, vp_orientation.vp_value);
        vp_sharedPreferencesEditor.apply();
    }

    public void vp_updateMeta(final String vp_audioTrackId, final String vp_subtitleTrackId, final int resizeMode, final float scale, final float speed) {
        this.vp_audioTrackId = vp_audioTrackId;
        this.vp_subtitleTrackId = vp_subtitleTrackId;
        this.vp_resizeMode = resizeMode;
        this.vp_scale = scale;
        this.vp_speed = speed;
        if (vp_persistentMode) {
            final SharedPreferences.Editor vp_sharedPreferencesEditor = vp_mSharedPreferences.edit();
            if (vp_audioTrackId == null)
                vp_sharedPreferencesEditor.remove(VP_PREF_KEY_AUDIO_TRACK_ID);
            else
                vp_sharedPreferencesEditor.putString(VP_PREF_KEY_AUDIO_TRACK_ID, vp_audioTrackId);
            if (vp_subtitleTrackId == null)
                vp_sharedPreferencesEditor.remove(VP_PREF_KEY_SUBTITLE_TRACK_ID);
            else
                vp_sharedPreferencesEditor.putString(VP_PREF_KEY_SUBTITLE_TRACK_ID, vp_subtitleTrackId);
            vp_sharedPreferencesEditor.putInt(VP_PREF_KEY_RESIZE_MODE, resizeMode);
            vp_sharedPreferencesEditor.putFloat(VP_PREF_KEY_SCALE, scale);
            vp_sharedPreferencesEditor.putFloat(VP_PREF_KEY_SPEED, speed);
            vp_sharedPreferencesEditor.apply();
        }
    }

    public void vp_updateScope(final Uri vp_uri) {
        vp_scopeUri = vp_uri;
        final SharedPreferences.Editor vp_sharedPreferencesEditor = vp_mSharedPreferences.edit();
        if (vp_uri == null)
            vp_sharedPreferencesEditor.remove(VP_PREF_KEY_SCOPE_URI);
        else
            vp_sharedPreferencesEditor.putString(VP_PREF_KEY_SCOPE_URI, vp_uri.toString());
        vp_sharedPreferencesEditor.apply();
    }

    public void vp_setPersistent(boolean persistentMode) {
        this.vp_persistentMode = persistentMode;
    }
}