package com.video.player.videoplayer.xvxvideoplayer.util;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity.vp_ShowAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_AppOpenIntent;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_TrendChangeAct;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_intent1;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.video.player.videoplayer.xvxvideoplayer.activities.VP_Splash;

import java.util.Date;

public class VP_AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks, VP_AppStartAdDismissedCallback {
    private AppOpenAd vp_appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback vp_loadCallback;
    private final VP_MyApplication VPMyApplication;
    private Activity vp_currentAct;
    private Activity vp_activity;
    private static boolean vp_isShowingAd = false;
    private long vp_loadTime = 0;

    public VP_AppOpenManager(Activity vp_activity, VP_MyApplication VPMyApplication) {
        this.vp_activity = vp_activity;
        this.VPMyApplication = VPMyApplication;
        this.VPMyApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void vp_fetchAd() {
        if (!vp_gEnabled(vp_activity)) {
            return;
        }
        if (vp_isAdAvailable()) {
            return;
        }
        vp_loadCallback =
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(AppOpenAd ad) {
                        VP_AppOpenManager.this.vp_appOpenAd = ad;
                        VP_AppOpenManager.this.vp_loadTime = (new Date()).getTime();
                    }

                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                    }

                };
        AdRequest vp_request = vp_getAdRequest();
        AppOpenAd.load(
                vp_activity, vp_GAppOpen(vp_activity), vp_request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, vp_loadCallback);
    }

    private boolean vp_wasLoadTimeLessThanNHoursAgo(long numHours) {
        long vp_dateDifference = (new Date()).getTime() - this.vp_loadTime;
        long vp_numMilliSecondsPerHour = 3600000;
        return (vp_dateDifference < (vp_numMilliSecondsPerHour * numHours));
    }


    public boolean vp_isAdAvailable() {
        return vp_appOpenAd != null && vp_wasLoadTimeLessThanNHoursAgo(4);
    }

    public void vp_showAdIfAvailable() {
        if (!vp_gEnabled(vp_activity)) {
            return;
        }

        if (!vp_isShowingAd && vp_isAdAvailable()) {
            FullScreenContentCallback vp_fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            VP_AppOpenManager.this.vp_appOpenAd = null;
                            vp_isShowingAd = false;
                            vp_fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            vp_isShowingAd = true;
                        }
                    };
            vp_appOpenAd.setFullScreenContentCallback(vp_fullScreenContentCallback);
            if (vp_ShowAppOpen) {
                vp_appOpenAd.show(vp_currentAct);
            }
        } else {
            vp_fetchAd();
        }
    }

    public void vp_showAdIfAvailable(VP_AppStartAdDismissedCallback VPAppStartAdDismissedCallback) {
        if (!vp_gEnabled(vp_activity)) {
            return;
        }
        if (!vp_isShowingAd && vp_isAdAvailable()) {
            FullScreenContentCallback vp_fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            VP_AppOpenManager.this.vp_appOpenAd = null;
                            vp_isShowingAd = false;
                            VPAppStartAdDismissedCallback.vp_appStartAdDismissed(true);
                            vp_fetchAd();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            VPAppStartAdDismissedCallback.vp_appStartAdDismissed(true);
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            vp_isShowingAd = true;
                        }
                    };
            vp_appOpenAd.setFullScreenContentCallback(vp_fullScreenContentCallback);
            if (vp_ShowAppOpen) {
                vp_appOpenAd.show(vp_currentAct);
            }
        } else {
            vp_fetchAd();
            VPAppStartAdDismissedCallback.vp_appStartAdDismissed(true);
        }
    }

    private AdRequest vp_getAdRequest() {
        return new AdRequest.Builder().build();
    }

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        vp_showAdIfAvailable(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        this.vp_currentAct = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        this.vp_currentAct = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        this.vp_currentAct = null;
    }

    @Override
    public void vp_appStartAdDismissed(boolean value) {
        if (vp_AppOpenIntent(vp_activity) && vp_TrendChangeAct) {
            vp_TrendChangeAct = false;
            vp_activity.startActivity(vp_intent1);
        }
    }
}
