package com.video.player.videoplayer.xvxvideoplayer.extra;

import static com.video.player.videoplayer.xvxvideoplayer.extra.VP_Jzvd.VP_TOOL_BAR_EXIST;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import java.util.Formatter;
import java.util.Locale;

public class VP_Utils {
    public static int VP_SYSTEM_UI = 0;
    public static final String TAG = "JZVD";

    public static void showStatusBar(Context context) {
        if (VP_TOOL_BAR_EXIST) {
            vp_getWindow(context).clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static String vp_stringForTime(long j) {
        if (j <= 0 || j >= 86400000) {
            return "00:00";
        }
        long vp_j2 = j / 1000;
        int vp_i = (int) (vp_j2 % 60);
        int vp_i2 = (int) ((vp_j2 / 60) % 60);
        int vp_i3 = (int) (vp_j2 / 3600);
        Formatter formatter = new Formatter(new StringBuilder(), Locale.getDefault());
        if (vp_i3 > 0) {
            return formatter.format("%d:%02d:%02d", vp_i3, vp_i2, vp_i).toString();
        }
        return formatter.format("%02d:%02d", vp_i2, vp_i).toString();
    }

    public static boolean isWifiConnected(Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.getType() == 1;
    }

    public static Activity vp_scanForActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return vp_scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    public static void vp_setRequestedOrientation(Context context, int i) {
        if (vp_scanForActivity(context) != null) {
            vp_scanForActivity(context).setRequestedOrientation(i);
        } else {
            vp_scanForActivity(context).setRequestedOrientation(i);
        }
    }

    public static Window vp_getWindow(Context context) {
        if (vp_scanForActivity(context) != null) {
            return vp_scanForActivity(context).getWindow();
        }
        return vp_scanForActivity(context).getWindow();
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static void vp_saveProgress(Context context, Object obj, long j) {
        if (VP_Jzvd.VP_SAVE_PROGRESS) {
            Log.i("JZVD", "saveProgress: " + j);
            if (j < 5000) {
                j = 0;
            }
            SharedPreferences.Editor vp_edit = context.getSharedPreferences("JZVD_PROGRESS", 0).edit();
            vp_edit.putLong("newVersion:" + obj.toString(), j).apply();
        }
    }

    public static long vp_getSavedProgress(Context context, Object obj) {
        if (!VP_Jzvd.VP_SAVE_PROGRESS) {
            return 0;
        }
        SharedPreferences vp_sharedPreferences = context.getSharedPreferences("JZVD_PROGRESS", 0);
        return vp_sharedPreferences.getLong("newVersion:" + obj.toString(), 0);
    }

    public static void clearSavedProgress(Context context, Object obj) {
        if (obj == null) {
            context.getSharedPreferences("JZVD_PROGRESS", 0).edit().clear().apply();
            return;
        }
        SharedPreferences.Editor vp_edit = context.getSharedPreferences("JZVD_PROGRESS", 0).edit();
        vp_edit.putLong("newVersion:" + obj.toString(), 0).apply();
    }

    public static void hideStatusBar(Context context) {
        if (VP_TOOL_BAR_EXIST) {
            vp_getWindow(context).setFlags(1024, 1024);
        }
    }

    public static void hideSystemUI(Context context) {
        int i = 5638;
        VP_SYSTEM_UI = vp_getWindow(context).getDecorView().getSystemUiVisibility();
        vp_getWindow(context).getDecorView().setSystemUiVisibility(i);
    }

    public static void vp_showSystemUI(Context context) {
        vp_getWindow(context).getDecorView().setSystemUiVisibility(VP_SYSTEM_UI);
    }

    public static int vp_getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"));
    }

    public static int getNavigationBarHeight(Context context) {
        boolean hasPermanentMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        int identifier = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier <= 0 || hasPermanentMenuKey) {
            return 0;
        }
        return context.getResources().getDimensionPixelSize(identifier);
    }

    public static int vp_getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
