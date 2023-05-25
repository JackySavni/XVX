package com.video.player.videoplayer.xvxvideoplayer.util;

import static android.content.Context.MODE_PRIVATE;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_QurekaInterAd.isOdd;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_QurekaInterAd.vp_generateOddEven;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.facebook.ads.Ad;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class VP_SharePref {
    public static SharedPreferences vp_sharedPreferences;
    public static SharedPreferences.Editor vp_SharePrefEditor;
    public static String vp_SharedPrefName = "XVX_shared_pref";
    public static InterstitialAd vp_interAd, vp_interAdTrend;
    public static com.facebook.ads.InterstitialAd fbInterAd1;
    public static String vp_string = "";
    public static Activity vp_act;
    public static Intent vp_intent1;
    public static boolean vp_TrendChangeAct = false;
    public static boolean vp_isFinish = false, isInterTrendShown = false, isDInterShown = false;
    public static int vp_clickCount = 0;
    public static int vp_clickTrendCount = 0;
    private static ProgressDialog vp_adDialog;
    private static CountDownTimer vp_timer;

    public static String vp_GameIconURL(Context context) {
        String vp_qurekaID;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_qurekaID = vp_sharedPreferences.getString("GameIconURL", "https://5807.play.quizzop.com");
        return vp_qurekaID;
    }

    public static String vp_QurekaID(Activity context) {
        String vp_qurekaID;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_qurekaID = vp_sharedPreferences.getString("QurekaID", "https://178.win.qureka.com/");
        return vp_qurekaID;
    }

    public static String vp_MGL_ID(Activity context) {
        String vp_qurekaID;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_qurekaID = vp_sharedPreferences.getString("MGL_ID", "https://5810.play.quizzop.com");
        return vp_qurekaID;
    }

    public static String vp_GameZopID(Activity context) {
        String GameZopID;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GameZopID = vp_sharedPreferences.getString("GameZopID", "pdW8JtYY");
        return GameZopID;
    }

    //////////FB

    public static boolean vp_1FBEnable(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1FBEnable", true);
    }

    public static String vp_FBInterSplash(Activity context) {
        String GInterHomeActivity;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GInterHomeActivity = vp_sharedPreferences.getString("FBInterSplash", "YOUR_PLACEMENT_ID");
        return GInterHomeActivity;
    }

    public static String vp_FBInter1(Activity context) {
        String GInterHomeActivity;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GInterHomeActivity = vp_sharedPreferences.getString("FBInter1", "YOUR_PLACEMENT_ID");
        return GInterHomeActivity;
    }

    public static String vp_FBNative1(Activity context) {
        String GBanner1;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GBanner1 = vp_sharedPreferences.getString("FBNative1", "YOUR_PLACEMENT_ID");
        return GBanner1;
    }

    public static String vp_FBBanner1(Activity context) {
        String GBanner1;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GBanner1 = vp_sharedPreferences.getString("FBBanner1", "YOUR_PLACEMENT_ID");
        return GBanner1;
    }

    public static String vp_FBNativeBanner1(Activity context) {
        String GBanner1;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GBanner1 = vp_sharedPreferences.getString("FBNativeBanner1", "YOUR_PLACEMENT_ID");
        return GBanner1;
    }

    /////////GGL
    public static String vp_GAppOpen(Activity context) {
        String GInterSplash;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GInterSplash = vp_sharedPreferences.getString("GAppOpen", "");
        return GInterSplash;
    }

    public static String vp_GInter1(Activity context) {
        String GInterHomeActivity;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GInterHomeActivity = vp_sharedPreferences.getString("GInter1", "");
        return GInterHomeActivity;
    }

    public static String vp_GInter2(Activity context) {
        String GInterHomeActivity;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GInterHomeActivity = vp_sharedPreferences.getString("GInter2", "");
        return GInterHomeActivity;
    }

    public static String vp_GNative1(Activity context) {
        String GBanner1;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GBanner1 = vp_sharedPreferences.getString("GNative1", "");
        return GBanner1;
    }

    public static String vp_GNative2(Activity context) {
        String GBanner2;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GBanner2 = vp_sharedPreferences.getString("GNative2", "");
        return GBanner2;
    }

    public static String vp_GBanner1(Activity context) {
        String GBanner1;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GBanner1 = vp_sharedPreferences.getString("GBanner1", "");
        return GBanner1;
    }

    public static String vp_GBanner2(Activity context) {
        String GBanner2;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        GBanner2 = vp_sharedPreferences.getString("GBanner2", "");
        return GBanner2;
    }

    public static boolean vp_getShowGameBtn(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1ShowGameBtn", true);
    }

    public static boolean vp_gEnabled(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1gEnabled", true);
    }

    public static boolean vp_dEnabled(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1dEnabled", true);
    }

    public static boolean vp_ShowInterLate(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1ShowInterLate", true);
    }

    public static boolean vp_SeparateAdEnabled(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1SeparateAdEnabled", true);
    }

    public static boolean vp_nativeTrendOutSide(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1NativeTrendOutSide", false);
    }
    public static boolean ShowHomeNative(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1ShowHomeNative", false);
    }

    public static boolean RemoveAllAds(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1RemoveAllAds", false);
    }

    public static boolean vp_AppOpenIntent(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("1AppOpenIntent", false);
    }

    public static String vp_pPolicy(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getString("pPolicy", "");
    }

    public static String vp_ShowTrendingAll(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getString("ShowTrending", "false");
    }

    public static String vp_trueString() {
        return "true";
    }

    public static String vp_falseString() {
        return "false";
    }

    public static String vp_allString() {
        return "all";
    }

    public static boolean vp_ShowTrending(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("Trending", false);
    }

    public static void vp_editShowTrending(Activity context) {
        context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .edit().putBoolean("Trending", true).apply();
    }

    public static void vp_editShowTrendingFal(Activity context) {
        context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .edit().putBoolean("Trending", false).apply();
    }

    public static boolean vp_ShowTrendPass(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("ShowTrendPass", false);
    }

    public static void vp_ShowTrendPassEdit(Activity context) {
        context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .edit()
                .putBoolean("ShowTrendPass", true)
                .apply();
    }

    public static boolean vp_checkedInstall(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("veriedI", false);
    }

    public static void vp_checkedInstallEdit(Activity context) {
        context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .edit()
                .putBoolean("veriedI", true)
                .apply();
    }

    public static boolean vp_showTrendPointer(Activity context) {
        return context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .getBoolean("ShowTrendPointer1", false);
    }

    public static void vp_showTrendPointerEdit(Activity context) {
        context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE)
                .edit()
                .putBoolean("ShowTrendPointer1", true)
                .apply();
    }

    public static long vp_noOfNative(Activity context) {
        long noOfNative;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        noOfNative = vp_sharedPreferences.getLong("noOfNative", 1);
        return noOfNative;
    }

    public static long vp_clickCount(Activity context) {
        long Timer_Qureka;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        Timer_Qureka = vp_sharedPreferences.getLong("clickCount", 4);
        return Timer_Qureka;
    }

    public static long vp_clickTrendCount(Activity context) {
        long vp_Timer_Qureka;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_Timer_Qureka = vp_sharedPreferences.getLong("clickTrendCount", 4);
        return vp_Timer_Qureka;
    }

    public static long vp_Timer_FB(Activity context) {
        long vp_Timer_FB;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_Timer_FB = vp_sharedPreferences.getLong("ServerTime_FB", 15000);
        return vp_Timer_FB;
    }

    public static long vp_Timer_Google(Activity context) {
        long vp_Timer_Google;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_Timer_Google = vp_sharedPreferences.getLong("ServerTime_Google", 15000);
        return vp_Timer_Google;
    }

    public static long vp_Timer_Qureka(Activity context) {
        long vp_Timer_Qureka;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_Timer_Qureka = vp_sharedPreferences.getLong("ServerTime_Qureka", 10000);
        return vp_Timer_Qureka;
    }

    public static long vp_Timer_Trend_Google(Activity context) {
        long vp_Timer_Google;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_Timer_Google = vp_sharedPreferences.getLong("ServerTime_Trend_Google", 15000);
        return vp_Timer_Google;
    }

    public static long vp_Timer_Trend_Qureka(Activity context) {
        long vp_Timer_Qureka;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_Timer_Qureka = vp_sharedPreferences.getLong("ServerTime_Trend_Qureka", 10000);
        return vp_Timer_Qureka;
    }

    //////// AdTimer SharedPref

    public static boolean vp_adTimerPrefFB(Activity context) {
        long vp_sharedPeriod, vp_currentPeriod;
        boolean vp_ok = true;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_sharedPeriod = vp_sharedPreferences.getLong("Timer_FB", 0);
        vp_currentPeriod = System.currentTimeMillis();

        if (vp_currentPeriod - vp_sharedPeriod < VP_SharePref.vp_Timer_FB(context)) {
            vp_ok = false;
        }
        return vp_ok;
    }

    public static void vp_adTimerEditorFB(Activity context) {
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_SharePrefEditor = vp_sharedPreferences.edit();
        vp_SharePrefEditor.putLong("Timer_FB", System.currentTimeMillis());
        vp_SharePrefEditor.apply();
    }

    public static boolean vp_adTimerPrefGoogle(Activity context) {
        long vp_sharedPeriod, vp_currentPeriod;
        boolean vp_ok = true;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_sharedPeriod = vp_sharedPreferences.getLong("Timer_Google", 0);
        vp_currentPeriod = System.currentTimeMillis();

        if (vp_currentPeriod - vp_sharedPeriod < VP_SharePref.vp_Timer_Google(context)) {
            vp_ok = false;
        }
        return vp_ok;
    }

    public static void vp_adTimerEditorGoogle(Activity context) {
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_SharePrefEditor = vp_sharedPreferences.edit();
        vp_SharePrefEditor.putLong("Timer_Google", System.currentTimeMillis());
        vp_SharePrefEditor.apply();
    }

    public static boolean vp_adTimerPrefQureka(Activity context) {
        long vp_sharedPeriod, vp_currentPeriod;
        boolean vp_ok = true;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_sharedPeriod = vp_sharedPreferences.getLong("Timer_Qureka", 0);
        vp_currentPeriod = System.currentTimeMillis();
        if (vp_currentPeriod - vp_sharedPeriod < VP_SharePref.vp_Timer_Qureka(context)) {
            vp_ok = false;
        }
        return vp_ok;
    }

    public static void vp_adTimerEditorQureka(Activity context) {
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_SharePrefEditor = vp_sharedPreferences.edit();
        vp_SharePrefEditor.putLong("Timer_Qureka", System.currentTimeMillis());
        vp_SharePrefEditor.apply();
    }

    public static boolean vp_adTimerPrefTrendGoogle(Activity context) {
        long vp_sharedPeriod, vp_currentPeriod;
        boolean vp_ok = true;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_sharedPeriod = vp_sharedPreferences.getLong("Timer_Trend_Google", 0);
        vp_currentPeriod = System.currentTimeMillis();

        if (vp_currentPeriod - vp_sharedPeriod < VP_SharePref.vp_Timer_Trend_Google(context)) {
            vp_ok = false;
        }
        return vp_ok;
    }

    public static void vp_adTimerEditorTrendGoogle(Activity context) {
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_SharePrefEditor = vp_sharedPreferences.edit();
        vp_SharePrefEditor.putLong("Timer_Trend_Google", System.currentTimeMillis());
        vp_SharePrefEditor.apply();
    }

    public static boolean vp_adTimerPrefTrendQureka(Activity context) {
        long vp_sharedPeriod, vp_currentPeriod;
        boolean vp_ok = true;
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_sharedPeriod = vp_sharedPreferences.getLong("Timer_Trend_Qureka", 0);
        vp_currentPeriod = System.currentTimeMillis();
        if (vp_currentPeriod - vp_sharedPeriod < VP_SharePref.vp_Timer_Trend_Qureka(context)) {
            vp_ok = false;
        }
        return vp_ok;
    }

    public static void vp_adTimerEditorTrendQureka(Activity context) {
        vp_sharedPreferences = context.getSharedPreferences(vp_SharedPrefName, MODE_PRIVATE);
        vp_SharePrefEditor = vp_sharedPreferences.edit();
        vp_SharePrefEditor.putLong("Timer_Trend_Qureka", System.currentTimeMillis());
        vp_SharePrefEditor.apply();
    }

    public static void vp_loadInter(Activity context) {
        InterstitialAd.load(context, vp_GInter1(context), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                vp_interAd = interstitialAd;
                vp_interAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        vp_loadInter(context);
                        vp_act.startActivity(vp_intent1);
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        vp_interAd = null;
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

    public static void loadInterTrend(Activity context) {
        InterstitialAd.load(context, vp_GInter2(context), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                vp_interAdTrend = interstitialAd;
                vp_interAdTrend.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        loadInterTrend(context);
                        vp_act.startActivity(vp_intent1);
                        if (vp_isFinish) {
                            vp_act.finish();
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        vp_dismissAdDialog();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        vp_interAdTrend = null;
                        vp_dismissAdDialog();
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        });
    }

    public static void loadFbInter1Ad(Activity context) {
        fbInterAd1 = new com.facebook.ads.InterstitialAd(context, VP_SharePref.vp_FBInter1(context));
        InterstitialAdListener splashInterListner = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                vp_dismissAdDialog();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                loadFbInter1Ad(context);
                vp_act.startActivity(vp_intent1);
                if (vp_isFinish) {
                    vp_act.finish();
                }
            }

            @Override
            public void onError(Ad ad, com.facebook.ads.AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        if (fbInterAd1.isAdInvalidated() || !fbInterAd1.isAdLoaded()) {
            fbInterAd1.loadAd(fbInterAd1.buildLoadAdConfig()
                    .withAdListener(splashInterListner)
                    .build());
        }
    }

    public static void vp_showInterAd(Activity activity, Intent intent) {
        vp_act = activity;
        vp_intent1 = intent;
        if (!RemoveAllAds(activity)) {
            vp_clickCount++;
            long vp_click = vp_clickCount(activity);
            if (vp_clickCount >= vp_click && vp_gEnabled(activity) && vp_interAd != null && vp_adTimerPrefGoogle(activity)) {
                vp_clickCount = 0;
                vp_interAd.show(activity);
                vp_adTimerEditorGoogle(activity);
            } else if (vp_clickCount >= vp_click && vp_1FBEnable(activity) && fbInterAd1 != null && fbInterAd1.isAdLoaded() && vp_adTimerPrefFB(activity)) {
                vp_clickCount = 0;
                vp_showAdDialog(activity);
                final Handler handler1 = new Handler();
                handler1.postDelayed(() -> {
                    fbInterAd1.show();
                    vp_adTimerEditorFB(activity);
                }, 1000);
            } else if (vp_clickCount >= vp_click && vp_SeparateAdEnabled(activity) && vp_adTimerPrefQureka(activity)) {
                vp_clickCount = 0;
                VP_QurekaInterAd.showQurekaAd(activity, intent, false, false);
            } else if (vp_clickCount >= vp_click && !vp_SeparateAdEnabled(activity) && vp_adTimerPrefGoogle(activity)) {
                vp_clickCount = 0;
                VP_QurekaInterAd.showQurekaAd(activity, intent, false, false);
            } else {
                if (intent != null) {
                    activity.startActivity(intent);
                }
            }
        } else {
            if (intent != null) {
                activity.startActivity(intent);
            }
        }
    }

    public static void showInterAd2(Activity activity, Intent intent) {
        vp_act = activity;
        vp_intent1 = intent;
        if (!RemoveAllAds(activity)) {
            vp_clickCount++;
            long vp_click = vp_clickCount(activity);
            if (vp_clickCount >= vp_click && vp_gEnabled(activity) && vp_interAdTrend != null && vp_adTimerPrefGoogle(activity)) {
                vp_clickCount = 0;
                vp_interAdTrend.show(activity);
                vp_adTimerEditorGoogle(activity);
            } else if (vp_clickCount >= vp_click && vp_1FBEnable(activity) && fbInterAd1 != null && fbInterAd1.isAdLoaded() && vp_adTimerPrefFB(activity)) {
                vp_clickCount = 0;
                vp_showAdDialog(activity);
                final Handler handler1 = new Handler();
                handler1.postDelayed(() -> {
                    fbInterAd1.show();
                    vp_adTimerEditorFB(activity);
                }, 1000);
            } else if (vp_clickCount >= vp_click && vp_SeparateAdEnabled(activity) && vp_adTimerPrefQureka(activity)) {
                vp_clickCount = 0;
                VP_QurekaInterAd.showQurekaAd(activity, intent, false, false);
            } else if (vp_clickCount >= vp_click && !vp_SeparateAdEnabled(activity) && vp_adTimerPrefGoogle(activity)) {
                vp_clickCount = 0;
                VP_QurekaInterAd.showQurekaAd(activity, intent, false, false);
            } else {
                if (intent != null) {
                    activity.startActivity(intent);
                }
            }
        } else {
            if (intent != null) {
                activity.startActivity(intent);
            }
        }
    }

    public static void vp_showTrendInterAd(Activity vp_activity, Intent intent, boolean finish) {
        isInterTrendShown = false;
        vp_act = vp_activity;
        vp_intent1 = intent;
        vp_isFinish = finish;
        if (!RemoveAllAds(vp_activity)) {
            vp_clickTrendCount++;
            long vp_click = vp_clickTrendCount(vp_activity);
            if (vp_clickTrendCount >= vp_click && vp_gEnabled(vp_activity) && vp_interAdTrend != null && vp_adTimerPrefTrendGoogle(vp_activity)) {
                isInterTrendShown = true;
                vp_clickTrendCount = 0;
                vp_interAdTrend.show(vp_activity);
                vp_adTimerEditorTrendGoogle(vp_activity);
            } else if (vp_clickTrendCount >= vp_click && vp_1FBEnable(vp_activity) && fbInterAd1 != null && fbInterAd1.isAdLoaded() && vp_adTimerPrefFB(vp_activity)) {
                isInterTrendShown = true;
                vp_clickTrendCount = 0;
                vp_showAdDialog(vp_activity);
                final Handler handler1 = new Handler();
                handler1.postDelayed(() -> {
                    fbInterAd1.show();
                    vp_adTimerEditorFB(vp_activity);
                }, 1000);
            } else if (vp_dEnabled(vp_activity) && vp_adTimerPrefTrendQureka(vp_activity)) {
                isDInterShown = true;
                vp_TrendChangeAct = true;
                vp_generateOddEven();
                vp_adTimerEditorTrendQureka(vp_activity);
                if (isOdd) {
                    VP_ChromeLauncher.vp_launchGame(vp_activity, vp_MGL_ID(vp_activity), "");
                } else {
                    VP_ChromeLauncher.vp_launchGame(vp_activity, vp_QurekaID(vp_activity), "");
                }
            } else {
                if (intent != null) {
                    vp_dismissAdDialog();
                    vp_activity.startActivity(intent);
                    if (finish) {
                        vp_activity.finish();
                    }
                }
            }
        } else {
            if (intent != null) {
                vp_activity.startActivity(intent);
                if (finish) {
                    vp_activity.finish();
                }
            }
        }
    }

    public static void showInterAdWithoutProgress(Activity vp_activity, long vp_click, Intent intent, boolean finish) {
        if (vp_clickTrendCount >= vp_click && vp_gEnabled(vp_activity) && vp_interAdTrend != null && vp_adTimerPrefTrendGoogle(vp_activity)) {
            vp_clickTrendCount = 0;
            vp_interAdTrend.show(vp_activity);
            vp_adTimerEditorTrendGoogle(vp_activity);
        } else if (vp_clickTrendCount >= vp_click && vp_1FBEnable(vp_activity) && fbInterAd1 != null && fbInterAd1.isAdLoaded() && vp_adTimerPrefFB(vp_activity)) {
            vp_clickTrendCount = 0;
            vp_showAdDialog(vp_activity);
            final Handler handler1 = new Handler();
            handler1.postDelayed(() -> {
                fbInterAd1.show();
                vp_adTimerEditorFB(vp_activity);
            }, 1000);
        } else if (vp_clickTrendCount >= vp_click && vp_SeparateAdEnabled(vp_activity) && vp_adTimerPrefTrendQureka(vp_activity)) {
            vp_dismissAdDialog();
            vp_TrendChangeAct = true;
            vp_clickTrendCount = 0;
            vp_generateOddEven();
            vp_adTimerEditorTrendQureka(vp_activity);
            if (isOdd) {
                VP_ChromeLauncher.vp_launchGame(vp_activity, vp_MGL_ID(vp_activity), "");
            } else {
                VP_ChromeLauncher.vp_launchGame(vp_activity, vp_QurekaID(vp_activity), "");
            }
        } else if (vp_clickTrendCount >= vp_click && !vp_SeparateAdEnabled(vp_activity) && vp_adTimerPrefTrendGoogle(vp_activity)) {
            vp_dismissAdDialog();
            vp_TrendChangeAct = true;
            vp_clickTrendCount = 0;
            vp_generateOddEven();
            vp_adTimerEditorTrendGoogle(vp_activity);
            if (isOdd) {
                VP_ChromeLauncher.vp_launchGame(vp_activity, vp_MGL_ID(vp_activity), "");
            } else {
                VP_ChromeLauncher.vp_launchGame(vp_activity, vp_QurekaID(vp_activity), "");
            }
        } else {
            if (intent != null) {
                vp_dismissAdDialog();
                vp_activity.startActivity(intent);
                if (finish) {
                    vp_activity.finish();
                }
            }
        }
    }

    public static void vp_showAdDialog(Activity activity) {
        if (vp_isValidContext(activity)) {
            vp_adDialog = new ProgressDialog(activity);
            vp_adDialog.setMessage("Loading");
            vp_adDialog.setCancelable(false);
            vp_adDialog.setCanceledOnTouchOutside(false);
            if (vp_isValidContext(activity)) {
                vp_adDialog.show();
            }
        }
    }

    public static void vp_dismissAdDialog() {
        if (vp_adDialog != null) {
            vp_adDialog.dismiss();
        }
    }

    public static boolean vp_isValidContext(final Activity activity) {
        if (activity == null) {
            return false;
        }
        return !activity.isDestroyed() && !activity.isFinishing();
    }
}
