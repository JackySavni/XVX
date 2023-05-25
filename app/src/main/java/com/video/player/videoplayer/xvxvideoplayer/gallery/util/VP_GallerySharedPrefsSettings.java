package com.video.player.videoplayer.xvxvideoplayer.gallery.util;

import android.content.Context;
import android.content.SharedPreferences;

public class VP_GallerySharedPrefsSettings {

    public static final String VP_SHARED_PREFS_SETTINGS = "PrefsSettings";
    //
    public static final String VP_BRUSH_SIZE_KEY = "BrushSizeKey";
    public static final String VP_BRUSH_OPACITY_KEY = "BrushOpacityKey";

    /**
     * set brush size
     *
     * @param size
     * @param context
     */
    public static void vp_setBrushSize(int size, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(VP_GallerySharedPrefsSettings.VP_SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE).edit();
        editor.putInt(VP_GallerySharedPrefsSettings.VP_BRUSH_SIZE_KEY, size);
        editor.apply();
    }

    /**
     * set brush opacity
     *
     * @param opacity
     * @param context
     */
    public static void setBrushOpacity(int opacity, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(VP_GallerySharedPrefsSettings.VP_SHARED_PREFS_SETTINGS, Context.MODE_PRIVATE).edit();
        editor.putInt(VP_GallerySharedPrefsSettings.VP_BRUSH_OPACITY_KEY, opacity);
        editor.apply();
    }

    /**
     * @param context
     * @return brush size
     */
    public static int getBrushSize(Context context) {
        return context.getSharedPreferences(VP_GallerySharedPrefsSettings.VP_SHARED_PREFS_SETTINGS,
                Context.MODE_PRIVATE).getInt(VP_GallerySharedPrefsSettings.VP_BRUSH_SIZE_KEY, 20);
    }

    /**
     * @param context
     * @return brush opacity
     */
    public static int getBrushOpacity(Context context) {
        return context.getSharedPreferences(VP_GallerySharedPrefsSettings.VP_SHARED_PREFS_SETTINGS,
                Context.MODE_PRIVATE).getInt(VP_GallerySharedPrefsSettings.VP_BRUSH_OPACITY_KEY, 100);
    }

}
