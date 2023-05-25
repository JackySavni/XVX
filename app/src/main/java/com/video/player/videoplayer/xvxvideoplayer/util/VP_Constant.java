package com.video.player.videoplayer.xvxvideoplayer.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VP_Constant {
    public static DatabaseReference vp_databaseReference, vp_databaseReferenceOffline;
    private Context vp_context;
    public static final String VP_EXTRA_FLOATING_VIDEO = "floating_video";
    public static final String VP_EXTRA_BACKGROUND_VIDEO_PLAY_POSITION = "background_video_play_position";
    public static final String VP_EXTRA_IS_FLOATING_VIDEO = "is_floating_video";
    public static final String VP_EXTRA_IS_CONTINUE_WATCHING_VIDEO = "is_continue_watching_video";
    public static final String VP_EXTRA_VIDEO_POSITION = "video_position";
    public static final String VP_EXTRA_VIDEO_LIST = "video_list";
    public static final String VP_EXTRA_TREND_VIDEO_URL = "trend_vid_url";
    public static final String VP_PREF_CONTINUE_WATCHING_VIDEO = "pref_continue_watching_video";
    public static String VP_HIDE_PATH = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).toString() + "/.Hide_Video");
    public static final String VP_PREF_IS_FLOATING_VIDEO = "pref_is_floating_video";
    public static final String VP_PREF_FLOATING_VIDEO_POSITION = "pref_floating_video_position";
    public static final String VP_PREF_VIDEO_LAST_POSITION = "pref_video_last_position";
    public static final String VP_PREF_LAST_PLAY_VIDEOS = "last_play_videos";
    public static final String VP_PREF_VIDEO_LIST = "pref_video_list";

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager vp_connectivityManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = vp_connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static ProgressDialog vp_adDialog;

    public static void showAdDialog(Context context) {
        vp_adDialog = new ProgressDialog(context);
        vp_adDialog.setMessage("Please wait.. Ad Showing");
        vp_adDialog.setCanceledOnTouchOutside(false);
        vp_adDialog.setCancelable(false);
        vp_adDialog.show();
    }

    public static void dismissAdDialog() {
        vp_adDialog.dismiss();
    }

    public VP_Constant(Context vp_context) {
        this.vp_context = vp_context;
        vp_databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public VP_Constant() {
        vp_databaseReference = FirebaseDatabase.getInstance().getReference().child("XVX_Video");
        vp_databaseReferenceOffline = FirebaseDatabase
                .getInstance("https://xvx-offline.firebaseio.com/").getReference().child("XVX_Video");
    }

    public DatabaseReference databaseRef() {
        return vp_databaseReference.child("Ads").child("v103");
    }

    public DatabaseReference vp_trendRef() {
        return vp_databaseReference.child("Trend");
    }

    public DatabaseReference vp_trendPathRef() {
        return vp_databaseReference.child("TrendPath");
    }

    public DatabaseReference vp_trendEntryRef() {
        return vp_databaseReference.child("TrendPath").child("-1TrendEntry");
    }
    public DatabaseReference trendSourceRef() {
        return vp_databaseReference.child("TrendSource");
    }

    public DatabaseReference directRef() {
        return vp_databaseReferenceOffline.child("DirectXVX");
    }

    public DatabaseReference updateRef() {
        return vp_databaseReferenceOffline.child("UpdateXVX");
    }

    public DatabaseReference newUpdateSetValRef() {
        return vp_databaseReferenceOffline.child("NewUpdateSetVal");
    }

    public DatabaseReference maintainRef() {
        return vp_databaseReferenceOffline.child("MaintainXVX");
    }

    public DatabaseReference migrateRef() {
        return vp_databaseReferenceOffline.child("MigrateXVX");
    }

}
