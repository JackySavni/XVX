package com.video.player.videoplayer.xvxvideoplayer.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;

import com.facebook.ads.AdSettings;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryAlbum;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_HistoryVideo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VP_MyApplication extends Application {
    private static VP_MyApplication vp_mInstance;
    static SharedPreferences.Editor vp_prefEditor;
    static SharedPreferences vp_preferences;
    public String vp_ssh;
    public ArrayList<VP_GalleryPhoto> vp_arrayList = new ArrayList<>();
    public ArrayList<VP_GalleryPhoto> vp_deleteList = new ArrayList<>();
    public int vp_deleteAlbActPhotoPos = -1;
    public VP_GalleryAlbum VPGalleryAlbum = new VP_GalleryAlbum("", 0, 0L, vp_arrayList);
    @SuppressLint("StaticFieldLeak")
    public static VP_AppOpenManager VPAppOpenManager;

    public static synchronized VP_MyApplication getInstance() {
        VP_MyApplication VPMyApplication;
        synchronized (VP_MyApplication.class) {
            synchronized (VP_MyApplication.class) {
                VPMyApplication = vp_mInstance;
            }
            return VPMyApplication;
        }
    }

    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        vp_preferences = getSharedPreferences("XVX_shared_pref", 0);
        vp_prefEditor = vp_preferences.edit();
        vp_prefEditor.apply();
        MobileAds.initialize(this, initializationStatus -> {
        });

        AudienceNetworkInitializeHelper.initialize(this);
//        AdSettings.addTestDevice("ac2686c0-e20e-4a3a-ae7c-95bb5d16493b");

        setupActivityListener();
        vp_mInstance = this;
        StrictMode.VmPolicy.Builder vp_builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(vp_builder.build());
        vp_builder.detectFileUriExposure();
    }

    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }

    public static String vp_getRecentPlay() {
        return vp_preferences.getString("recent", "");
    }

    public static void vp_putRecentPlay(String str) {
        vp_prefEditor.putString("recent", str);
        vp_prefEditor.commit();
    }

    public static int vp_getViewBy() {
        return vp_preferences.getInt("view_by", 0);
    }

    public static void vp_putViewBy(int i) {
        vp_prefEditor.putInt("view_by", i);
        vp_prefEditor.commit();
    }

    public static String vp_getSecurityQuestion() {
        return vp_preferences.getString("security_question", "");
    }

    public static void vp_putSecurityQuestion(String str) {
        vp_prefEditor.putString("security_question", str);
        vp_prefEditor.commit();
    }

    public static String vp_getAnswerQuestion() {
        return vp_preferences.getString("security_answer", "");
    }

    public static void vp_putAnswerQuestion(String str) {
        vp_prefEditor.putString("security_answer", str);
        vp_prefEditor.commit();
    }

    public static String vp_getPass() {
        return vp_preferences.getString("pass", "");
    }

    public static void vp_putPass(String str) {
        vp_prefEditor.putString("pass", str);
        vp_prefEditor.commit();
    }

    public static boolean vp_getSetPass() {
        return vp_preferences.getBoolean("set_pass", false);
    }

    public static void vp_putSetPass(boolean z) {
        vp_prefEditor.putBoolean("set_pass", z);
        vp_prefEditor.commit();
    }

    public static boolean vp_getSetQuestion() {
        return vp_preferences.getBoolean("set_question", false);
    }

    public static void vp_putSetQuestion(boolean z) {
        vp_prefEditor.putBoolean("set_question", z);
        vp_prefEditor.commit();
    }

    //    TrendSecure
    public static String vp_getTrendSecurityQuestion() {
        return vp_preferences.getString("trend_security_question", "");
    }

    public static void vp_putTrendSecurityQuestion(String str) {
        vp_prefEditor.putString("trend_security_question", str);
        vp_prefEditor.commit();
    }

    public static String vp_getTrendAnswerQuestion() {
        return vp_preferences.getString("trend_security_answer", "");
    }

    public static void vp_putTrendAnswerQuestion(String str) {
        vp_prefEditor.putString("trend_security_answer", str);
        vp_prefEditor.commit();
    }

    public static String vp_getTrendPass() {
        return vp_preferences.getString("trend_pass", "");
    }

    public static void vp_putTrendPass(String str) {
        vp_prefEditor.putString("trend_pass", str);
        vp_prefEditor.commit();
    }

    public static boolean vp_getTrendSetPass() {
        return vp_preferences.getBoolean("trend_set_pass", false);
    }

    public static void vp_putTrendSetPass(boolean z) {
        vp_prefEditor.putBoolean("trend_set_pass", z);
        vp_prefEditor.apply();
    }

    public static boolean vp_getTrendSetQuestion() {
        return vp_preferences.getBoolean("trend_set_question", false);
    }

    public static void vp_putTrendSetQuestion(boolean z) {
        vp_prefEditor.putBoolean("trend_set_question", z);
        vp_prefEditor.commit();
    }

    public static HashMap<String, Integer> vp_getVideoLastPosition() {
        HashMap<String, Integer> vp_hashMap = new HashMap<>();
        String vp_string = vp_preferences.getString(VP_Constant.VP_PREF_VIDEO_LAST_POSITION, "");
        return vp_string != null ? (HashMap) new Gson().fromJson(vp_string, HashMap.class) : vp_hashMap;
    }

    public static void setVideoLastPosition(HashMap<String, Integer> hashMap) {
        HashMap vp_hashMap2 = new HashMap();
        if (hashMap != null) {
            String vp_string = vp_preferences.getString(VP_Constant.VP_PREF_VIDEO_LAST_POSITION, "");
            if (vp_string != null) {
                vp_hashMap2 = new Gson().fromJson(vp_string, HashMap.class);
            }
            if (vp_hashMap2 != null) {
                vp_hashMap2.putAll(hashMap);
            } else {
                vp_hashMap2 = new HashMap();
                vp_hashMap2.putAll(hashMap);
            }
        }
        SharedPreferences.Editor vp_edit = vp_preferences.edit();
        vp_edit.putString(VP_Constant.VP_PREF_VIDEO_LAST_POSITION, new Gson().toJson(vp_hashMap2));
        vp_edit.apply();
    }

    public static void vp_setVideoList(ArrayList<MediaData> arrayList) {
        VP_HistoryVideo VPHistoryVideo = new VP_HistoryVideo(arrayList);
        vp_prefEditor.remove(VP_Constant.VP_PREF_VIDEO_LIST);
        vp_prefEditor.commit();
        vp_prefEditor.putString(VP_Constant.VP_PREF_VIDEO_LIST, new Gson().toJson(VPHistoryVideo));
        vp_prefEditor.apply();
    }

    public static ArrayList<MediaData> vp_getVideoList() {
        VP_HistoryVideo VPHistoryVideo;
        ArrayList<MediaData> vp_arrayList = new ArrayList<>();
        String string = vp_preferences.getString(VP_Constant.VP_PREF_VIDEO_LIST, "");
        if (!(string == null || (VPHistoryVideo = new Gson().fromJson(string, VP_HistoryVideo.class)) == null)) {
            vp_arrayList.addAll(VPHistoryVideo.getVp_videoList());
        }
        return vp_arrayList;
    }

    public static void vp_setFloatingVideoPosition(int i) {
        vp_prefEditor.putInt(VP_Constant.VP_PREF_FLOATING_VIDEO_POSITION, i);
        vp_prefEditor.apply();
    }

    public static int vp_getFloatingVideoPosition() {
        return vp_preferences.getInt(VP_Constant.VP_PREF_FLOATING_VIDEO_POSITION, 0);
    }

    public static void vp_setIsFloatingVideo(boolean z) {
        vp_prefEditor.putBoolean(VP_Constant.VP_PREF_IS_FLOATING_VIDEO, z);
        vp_prefEditor.apply();
    }

    public boolean isFloatingVideo() {
        return vp_preferences.getBoolean(VP_Constant.VP_PREF_IS_FLOATING_VIDEO, false);
    }

    public static void vp_setLastPlayVideos(MediaData media_Data) {
        vp_prefEditor.putString(VP_Constant.VP_PREF_LAST_PLAY_VIDEOS, new Gson().toJson(media_Data));
        vp_prefEditor.apply();
    }

    public static MediaData vp_getLastPlayVideos() {
        String string = vp_preferences.getString(VP_Constant.VP_PREF_LAST_PLAY_VIDEOS, "");
        if (string != null) {
            return new Gson().fromJson(string, MediaData.class);
        }
        return null;
    }

    public static void vp_setContinueWatchingVideos(MediaData media_Data) {
        ArrayList vp_arrayList = new ArrayList();
        if (media_Data != null) {
            if (vp_getContinueWatchingVideos() != null) {
                vp_arrayList.addAll(vp_getContinueWatchingVideos());
                if (vp_arrayList.size() != 0) {
                    for (int i = 0; i < vp_arrayList.size(); i++) {
                        if (((MediaData) vp_arrayList.get(i)).path.equals(media_Data.path)) {
                            vp_arrayList.remove(i);
                        }
                    }
                }
            }
            vp_arrayList.add(0, media_Data);
        }
        vp_prefEditor.putString(VP_Constant.VP_PREF_CONTINUE_WATCHING_VIDEO, new Gson().toJson(new VP_HistoryVideo(vp_arrayList)));
        vp_prefEditor.apply();
    }

    public void updateContinueWatchingVideo(ArrayList<MediaData> arrayList) {
        vp_prefEditor.putString(VP_Constant.VP_PREF_CONTINUE_WATCHING_VIDEO, new Gson().toJson(new VP_HistoryVideo(arrayList)));
        vp_prefEditor.apply();
    }

    public static List<MediaData> vp_getContinueWatchingVideos() {
        VP_HistoryVideo VPHistoryVideo;
        ArrayList vp_arrayList = new ArrayList();
        String string = vp_preferences.getString(VP_Constant.VP_PREF_CONTINUE_WATCHING_VIDEO, "");
        if (!(string == null || (VPHistoryVideo = new Gson().fromJson(string, VP_HistoryVideo.class)) == null)) {
            vp_arrayList.addAll(VPHistoryVideo.getVp_videoList());
        }
        return vp_arrayList;
    }
}
