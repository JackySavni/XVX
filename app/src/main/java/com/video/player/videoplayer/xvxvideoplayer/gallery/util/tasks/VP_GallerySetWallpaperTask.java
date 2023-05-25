package com.video.player.videoplayer.xvxvideoplayer.gallery.util.tasks;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

import java.lang.ref.WeakReference;

/**
 * task to change wallpaper
 * show loading animation
 * show finish animation
 */
public class VP_GallerySetWallpaperTask extends AsyncTask<Object, Void, Void> {

    private final WeakReference<LottieAnimationView> vp_animDoneWR, vp_animSettingWWR;
    private final WeakReference<Activity> vp_activityWR;

    public VP_GallerySetWallpaperTask(Activity vp_activity, LottieAnimationView vp_animDone, LottieAnimationView vp_animSetting) {
        this.vp_activityWR = new WeakReference<>(vp_activity);
        this.vp_animDoneWR = new WeakReference<>(vp_animDone);
        this.vp_animSettingWWR = new WeakReference<>(vp_animSetting);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    protected Void doInBackground(Object... objects) {

        Activity vp_activity = vp_activityWR.get();
        if (vp_activity != null) {
            vp_activity.runOnUiThread(() -> vp_showSettingAnimation(LottieDrawable.INFINITE, 0));
        }
        switch ((int) objects[1]) {
            case 1:
                vp_setAsWallpaperFront((Bitmap) objects[0]);
                break;
            case 2:
                vp_setAsWallpaperBack((Bitmap) objects[0]);
                break;
            case 3:
                vp_setAsWallpaperFrontBack((Bitmap) objects[0]);
                break;

        }
        return null;
    }

    protected void onPostExecute(Void param) {
        vp_showSettingAnimation(0, 20);
        vp_showDoneAnimation();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void vp_setAsWallpaperFront(final Bitmap bitmap) {
        try {
            Activity vp_activity = vp_activityWR.get();
            if (vp_activity != null) {
                WallpaperManager vp_wm = WallpaperManager.getInstance(vp_activity);
                vp_wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void vp_setAsWallpaperBack(final Bitmap bitmap) {
        try {
            Activity vp_activity = vp_activityWR.get();
            if (vp_activity != null) {
                WallpaperManager vp_wm = WallpaperManager.getInstance(vp_activity);
                vp_wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void vp_setAsWallpaperFrontBack(final Bitmap bitmap) {
        try {
            Activity vp_activity = vp_activityWR.get();
            if (vp_activity != null) {
                WallpaperManager vp_wm = WallpaperManager.getInstance(vp_activity);
                vp_wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                vp_wm.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void vp_showSettingAnimation(int vp_nrCount, int vp_nrDur) {
        final LottieAnimationView vp_animSettingW = vp_animSettingWWR.get();
        if (vp_animSettingW != null) {
            vp_animSettingW.setVisibility(View.VISIBLE);
            vp_animSettingW.playAnimation();
            vp_animSettingW.setRepeatCount(vp_nrCount);
            if (vp_nrDur > 10) {
                new Handler().postDelayed(() -> {
                    vp_animSettingW.cancelAnimation();
                    vp_animSettingW.setVisibility(View.GONE);
                }, vp_nrDur);
            }
        }
    }

    private void vp_showDoneAnimation() {
        final LottieAnimationView animDone = vp_animDoneWR.get();
        if (animDone != null) {
            animDone.setVisibility(View.VISIBLE);
            animDone.playAnimation();
            new Handler().postDelayed(() -> {
                animDone.cancelAnimation();
                animDone.setVisibility(View.GONE);
            }, 1500);
        }
    }

}