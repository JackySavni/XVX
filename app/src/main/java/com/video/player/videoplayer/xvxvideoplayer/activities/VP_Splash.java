package com.video.player.videoplayer.xvxvideoplayer.activities;

import static com.video.player.videoplayer.xvxvideoplayer.activities.VP_Permission.permissions;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant.isNetworkAvailable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication.VPAppOpenManager;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_1FBEnable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_SharePrefEditor;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_SharedPrefName;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_ShowInterLate;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_adTimerEditorGoogle;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_adTimerEditorQureka;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_checkedInstall;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_checkedInstallEdit;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_editShowTrending;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_isValidContext;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.video.player.videoplayer.xvxvideoplayer.BuildConfig;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_MigrateModel;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_AppOpenManager;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_AppStartAdDismissedCallback;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_GIFDialog;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VP_Splash extends AppCompatActivity implements VP_AppStartAdDismissedCallback {
    private InterstitialAd fbInterAd;
    private RelativeLayout vp_splash_layout;
    private SpinKitView vp_progressBar;
    private final Executor backgroundExecutor = Executors.newSingleThreadExecutor();
    private final ArrayList<String> arrayList = new ArrayList<>();
    private ProgressDialog progressBar;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_vp_splash);

        vp_progressBar = findViewById(R.id.spinkit_progress_bar);
        vp_splash_layout = findViewById(R.id.splash_layout);

        chk_network();
    }

    public void chk_network() {
        if (isNetworkAvailable(VP_Splash.this)) {
            adIdLoad();
        } else {
            vp_progressBar.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar
                    .make(vp_splash_layout, getResources().getString(R.string.noInternet), Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", view -> chk_network());
            snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent2));
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.WHITE);
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
            snackbar.show();
        }
    }

    void checkInstallReferrer() {
        if (vp_checkedInstall(this)) {
            showAd();
            return;
        }
        InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(this).build();
        backgroundExecutor.execute(() -> getInstallReferrerFromClient(referrerClient));
    }

    void getInstallReferrerFromClient(InstallReferrerClient referrerClient) {
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        ReferrerDetails response = null;
                        try {
                            response = referrerClient.getInstallReferrer();
                            final String referrerUrl = response.getInstallReferrer();

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("ReferrerUrl", referrerUrl);
                            runOnUiThread(() -> {
                                new VP_Constant().vp_trendEntryRef().push().updateChildren(hashMap);
                                new VP_Constant().trendSourceRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dsp : snapshot.getChildren()) {
                                            arrayList.add(String.valueOf(dsp.getValue()));
                                        }
                                        for (int i = 0; i < arrayList.size(); i++) {
                                            if (referrerUrl.contains(arrayList.get(i))) {
                                                vp_editShowTrending(VP_Splash.this);
                                            }
                                        }
                                        vp_checkedInstallEdit(VP_Splash.this);
                                        showAd();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            });
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            return;
                        }
                        referrerClient.endConnection();

                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        showAd();
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {

            }
        });
    }

    private void adIdLoad() {
        vp_progressBar.setVisibility(View.VISIBLE);
        DatabaseReference dataRef = new VP_Constant().databaseRef();
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SharedPreferences mSharedPreferences = getSharedPreferences(vp_SharedPrefName, Context.MODE_PRIVATE);
                vp_SharePrefEditor = mSharedPreferences.edit();

                vp_SharePrefEditor.putString("GAppOpen", snapshot.child("GAppOpen").getValue(String.class));
                vp_SharePrefEditor.putString("GInter1", snapshot.child("GInter1").getValue(String.class));
                vp_SharePrefEditor.putString("GInter2", snapshot.child("GInter2").getValue(String.class));
                vp_SharePrefEditor.putString("GNative1", snapshot.child("GNative1").getValue(String.class));
                vp_SharePrefEditor.putString("GNative2", snapshot.child("GNative2").getValue(String.class));
                vp_SharePrefEditor.putString("GBanner1", snapshot.child("GBanner1").getValue(String.class));
                vp_SharePrefEditor.putString("GBanner2", snapshot.child("GBanner2").getValue(String.class));

                vp_SharePrefEditor.putString("FBInterSplash", snapshot.child("FBInterSplash").getValue(String.class));
                vp_SharePrefEditor.putString("FBInter1", snapshot.child("FBInter1").getValue(String.class));
                vp_SharePrefEditor.putString("FBNative1", snapshot.child("FBNative1").getValue(String.class));
                vp_SharePrefEditor.putString("FBBanner1", snapshot.child("FBBanner1").getValue(String.class));
                vp_SharePrefEditor.putString("FBNativeBanner1", snapshot.child("FBNativeBanner1").getValue(String.class));

                vp_SharePrefEditor.putString("GameIconURL", snapshot.child("GameIconURL").getValue(String.class));
                vp_SharePrefEditor.putString("QurekaID", snapshot.child("QurekaID").getValue(String.class));
                vp_SharePrefEditor.putString("MGL_ID", snapshot.child("MGL_ID").getValue(String.class));
                vp_SharePrefEditor.putString("GameZopID", snapshot.child("GameZopID").getValue(String.class));
                vp_SharePrefEditor.putString("ShowTrending", snapshot.child("ShowTrending").getValue(String.class));

                vp_SharePrefEditor.putLong("ServerTime_FB", snapshot.child("Timer_FB").getValue(long.class));
                vp_SharePrefEditor.putLong("ServerTime_Qureka", snapshot.child("Timer_Qureka").getValue(long.class));
                vp_SharePrefEditor.putLong("ServerTime_Google", snapshot.child("Timer_Google").getValue(long.class));
                vp_SharePrefEditor.putLong("ServerTime_Trend_Qureka", snapshot.child("Timer_Trend_Qureka").getValue(long.class));
                vp_SharePrefEditor.putLong("ServerTime_Trend_Google", snapshot.child("Timer_Trend_Google").getValue(long.class));
                vp_SharePrefEditor.putLong("noOfNative", snapshot.child("noOfNative").getValue(long.class));
                vp_SharePrefEditor.putLong("clickCount", snapshot.child("clickCount").getValue(long.class));
                vp_SharePrefEditor.putLong("clickTrendCount", snapshot.child("clickTrendCount").getValue(long.class));
                vp_SharePrefEditor.putBoolean("1ShowGameBtn", Boolean.TRUE.equals(snapshot.child("1ShowGameBtn").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1FBEnable", Boolean.TRUE.equals(snapshot.child("1FBEnable").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1gEnabled", Boolean.TRUE.equals(snapshot.child("1gEnabled").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1dEnabled", Boolean.TRUE.equals(snapshot.child("1dEnabled").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1ShowInterLate", Boolean.TRUE.equals(snapshot.child("1ShowInterLate").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1SeparateAdEnabled", Boolean.TRUE.equals(snapshot.child("1SeparateAdEnabled").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1ShowHomeNative", Boolean.TRUE.equals(snapshot.child("1ShowHomeNative").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1AppOpenIntent", Boolean.TRUE.equals(snapshot.child("1AppOpenIntent").getValue(Boolean.class)));
                vp_SharePrefEditor.putBoolean("1NativeOutSide", Boolean.TRUE.equals(snapshot.child("1NativeOutSide").getValue(Boolean.class)));
                vp_SharePrefEditor.putString("pPolicy", snapshot.child("pPolicy").getValue(String.class));
                vp_SharePrefEditor.apply();

                if (vp_gEnabled(VP_Splash.this) && vp_1FBEnable(VP_Splash.this)) {
                    VPAppOpenManager = new VP_AppOpenManager(VP_Splash.this, new VP_MyApplication());
                    loadFbSplashAd();
                }else if (vp_gEnabled(VP_Splash.this) && !vp_1FBEnable(VP_Splash.this)){
                    VPAppOpenManager = new VP_AppOpenManager(VP_Splash.this, new VP_MyApplication());
                }else if (!vp_gEnabled(VP_Splash.this) && vp_1FBEnable(VP_Splash.this)){
                    loadFbSplashAd();
                }

                checkInstallReferrer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DatabaseReference directRef = new VP_Constant().directRef();
                directRef.keepSynced(true);
                directRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String keyName = dataSnapshot.child("key_name").getValue(String.class);
                        Integer vCode = dataSnapshot.child("v_code").getValue(Integer.class);
                        if (keyName.equals("T")) {
                            migrate();
                        } else if (BuildConfig.VERSION_CODE < vCode) {
                            updateApp();
                        } else if (keyName.equals("A")) {
                            underManitain();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void updateApp() {
        DatabaseReference maintainRef = new VP_Constant().updateRef();
        maintainRef.keepSynced(true);
        maintainRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (vp_isValidContext(VP_Splash.this)) {
                    try {
                        VP_GIFDialog.Builder rateDialog = new VP_GIFDialog.Builder(VP_Splash.this);
                        rateDialog.setTitle(snapshot.child("title").getValue(String.class))
                                .setMessage(snapshot.child("descp").getValue(String.class))
                                .setPositiveBtnText(getResources().getString(R.string.update))
                                .setGifResource("png", R.drawable.vp_rate, "")
                                .isCancellable(false)
                                .OnPositiveClicked(() -> {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                        finish();
                                    } catch (ActivityNotFoundException ignored) {
                                    }
                                })
                                .OnNegativeClicked(null)
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void underManitain() {
        DatabaseReference maintainRef = new VP_Constant().maintainRef();
        maintainRef.keepSynced(true);
        maintainRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                new VP_Constant().newUpdateSetValRef().push().setValue("" + BuildConfig.VERSION_CODE);
                if (vp_isValidContext(VP_Splash.this)) {
                    try {
                        VP_GIFDialog.Builder rateDialog = new VP_GIFDialog.Builder(VP_Splash.this);
                        rateDialog.setTitle(snapshot.child("title").getValue(String.class))
                                .setMessage(snapshot.child("descp").getValue(String.class))
                                .setPositiveBtnText(getResources().getString(R.string.ok))
                                .setGifResource("png", R.drawable.vp_maintenance, "")
                                .isCancellable(false)
                                .OnPositiveClicked(() -> finish())
                                .OnNegativeClicked(null)
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void migrate() {
        DatabaseReference migrateRef = new VP_Constant().migrateRef();
        migrateRef.keepSynced(true);
        migrateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (vp_isValidContext(VP_Splash.this)) {
                    try {
                        final VP_MigrateModel migrateModel = dataSnapshot.getValue(VP_MigrateModel.class);
                        VP_GIFDialog.Builder rateDialog = new VP_GIFDialog.Builder(VP_Splash.this);
                        rateDialog.setTitle(migrateModel.getApp_title())
                                .setMessage(migrateModel.getDescp())
                                .setPositiveBtnText("Install")
                                .setGifResource("png", R.drawable.vp_migrate,
                                        migrateModel.getApp_thumb())
                                .isCancellable(false)
                                .OnPositiveClicked(() -> {
                                    Intent intent =
                                            new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(migrateModel.getApp_url()));
                                    startActivity(intent);
                                    finish();
                                })
                                .OnNegativeClicked(null)
                                .build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showAd() {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            vp_progressBar.setVisibility(View.INVISIBLE);
            if (vp_gEnabled(this) && VPAppOpenManager != null && VPAppOpenManager.vp_isAdAvailable()) {
                VPAppOpenManager.vp_showAdIfAvailable(this);
            }else if (vp_1FBEnable(this) && fbInterAd != null && fbInterAd.isAdLoaded()){
                showAdLoadingProgress();
                final Handler handler1 = new Handler();
                handler1.postDelayed(() -> fbInterAd.show(), 1000);
            }else {
                nextActivity();
            }
        }, 7000);
    }

    private void loadFbSplashAd() {
        fbInterAd = new InterstitialAd(this, VP_SharePref.vp_FBInterSplash(this));
        InterstitialAdListener splashInterListner = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                dismissAdLoadingProgress();
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                nextActivity();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
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
        if (fbInterAd.isAdInvalidated() || !fbInterAd.isAdLoaded()) {
            fbInterAd.loadAd(fbInterAd.buildLoadAdConfig()
                    .withAdListener(splashInterListner)
                    .build());
        }
    }

    public void nextActivity() {
        if (checkPermission()) {
            startActivity(new Intent(VP_Splash.this, VP_Permission.class));
        } else {
            showInterLate();
            startActivity(new Intent(VP_Splash.this, VP_Home.class));
        }
    }

    public boolean checkPermission() {
        boolean isGranted = false;
        for (String permission : permissions()) {
            isGranted = ContextCompat.checkSelfPermission(this, permission) != 0;
        }
        return isGranted;
    }

    private void showInterLate() {
        if (vp_ShowInterLate(this)) {
            vp_adTimerEditorGoogle(this);
            vp_adTimerEditorQureka(this);
        }
    }

    @Override
    public void vp_appStartAdDismissed(boolean dismissed) {
        if (dismissed) {
            nextActivity();
        }
    }

    private void showAdLoadingProgress() {
        if (vp_isValidContext(VP_Splash.this)) {
            try {
                progressBar = new ProgressDialog(VP_Splash.this);
                progressBar.setMessage("Ad Showing...");
                progressBar.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void dismissAdLoadingProgress() {
        if (vp_isValidContext(this)) {
            try {
                if (progressBar != null) {
                    progressBar.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

