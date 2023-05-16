package com.video.player.videoplayer.xvxvideoplayer.activities;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity.vp_ShowAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.trend.VP_VCrypt.pointer1;
import static com.video.player.videoplayer.xvxvideoplayer.trend.VP_VCrypt.pointer2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.ShowHomeNative;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.loadFbInter1Ad;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.loadInterTrend;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.showInterAd2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_1FBEnable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_AppOpenIntent;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GBanner1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_ShowTrending;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_ShowTrendingAll;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_TrendChangeAct;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_allString;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_falseString;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_getShowGameBtn;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_intent1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_loadInter;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_pPolicy;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showInterAd;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showTrendInterAd;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showTrendPointer;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showTrendPointerEdit;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_trueString;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.onesignal.OneSignal;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.fragments.VP_FolderFragment;
import com.video.player.videoplayer.xvxvideoplayer.fragments.VP_RecentFragment;
import com.video.player.videoplayer.xvxvideoplayer.fragments.VP_VideoFragment;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.VP_GalleryMainActivity;
import com.video.player.videoplayer.xvxvideoplayer.trend.VP_TrendSecure;
import com.video.player.videoplayer.xvxvideoplayer.trend.VP_TrendVidCats;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;

public class VP_Home extends AppCompatActivity implements View.OnClickListener {
    public AppUpdateManager appUpdateManager;
    private static final int MY_REQUEST_CODE = 98989;

    public DrawerLayout vp_backHide;
    private NavigationView vp_navLay;
    private ImageView vp_navigation, vp_videoOptImg, vp_galleryOptImg, vp_gameOptImg, vp_trendOptImg;
    private FrameLayout vp_adContainerView;
    private TextView vp_adTextBanner;
    private RelativeLayout vp_adLayout, vp_gameBtnLyt;
    private static final int VP_TIME_DELAY = 2000;
    private static long vp_back_pressed;
    private LinearLayout vp_tab_folder, vp_allvideo_tab_layout, vp_recent_tab_layout;
    private TextView vp_txt_folder, vp_allvideo_txet, vp_recent_text;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_ex_activity_home);
        request_notification_api13_permission();
        checkForNewUpdate();
        initView();

        if (ShowHomeNative(this)) {
            if (vp_gEnabled(this) && vp_1FBEnable(this)) {
                loadBanner();
            } else if (vp_gEnabled(this) && !vp_1FBEnable(this)) {
                loadBanner();
            } else if (!vp_gEnabled(this) && vp_1FBEnable(this)) {
                loadFBBannerAd();
            } else {
                loadQurekaBanner();
            }
        } else {
            vp_adLayout.setVisibility(View.GONE);
        }

        if (vp_gEnabled(this) && vp_1FBEnable(this)) {
            if (vp_GInter1(this).equals(vp_GInter2(this))) {
                loadInterTrend(this);
            } else {
                vp_loadInter(this);
                loadInterTrend(this);
            }
            loadFbInter1Ad(VP_Home.this);
        } else if (vp_gEnabled(this) && !vp_1FBEnable(this)) {
            if (vp_GInter1(this).equals(vp_GInter2(this))) {
                loadInterTrend(this);
            } else {
                vp_loadInter(this);
                loadInterTrend(this);
            }
        } else if (!vp_gEnabled(this) && vp_1FBEnable(this)) {
            loadFbInter1Ad(VP_Home.this);
        }

        initListener();
    }

    private void loadBanner() {
        AdView adView = new AdView(this);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.setAdUnitId(vp_GBanner1(this));
        vp_adContainerView.removeAllViews();
        vp_adContainerView.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                vp_adTextBanner.setVisibility(View.GONE);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                if (vp_1FBEnable(VP_Home.this)) {
                    loadFBBannerAd();
                } else {
                    loadQurekaBanner();
                }
            }
        });
    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadFBBannerAd() {
        com.facebook.ads.AdView fAdView = new com.facebook.ads.AdView(this,
                VP_SharePref.vp_FBBanner1(this), com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                loadQurekaBanner();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                vp_adTextBanner.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        fAdView.loadAd(fAdView.buildLoadAdConfig().withAdListener(adListener).build());
        vp_adContainerView.removeAllViews();
        vp_adContainerView.addView(fAdView);
    }

    private void loadQurekaBanner() {
        vp_adTextBanner.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view;
        view = inflater.inflate(R.layout.vp_row_qureka_banner_ad, vp_adContainerView, false);
        ShimmerTextView adTitle = view.findViewById(R.id.vp_adTitle);
        Shimmer shimmer = new Shimmer();
        shimmer.start(adTitle);
        vp_adContainerView.removeAllViews();
        vp_adContainerView.addView(view);
        view.setOnClickListener(view1 -> VP_ChromeLauncher.vp_launchGame(this, vp_QurekaID(this), ""));
    }

    @SuppressLint("NonConstantResourceId")
    private void initListener() {
        this.vp_navigation.setOnClickListener(this);
        this.vp_videoOptImg.setOnClickListener(this);
        this.vp_galleryOptImg.setOnClickListener(this);
        this.vp_gameOptImg.setOnClickListener(this);
        this.vp_trendOptImg.setOnClickListener(this);

        this.vp_navLay.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.vp_nav_gallery:
                    if (vp_GInter1(this).equals(vp_GInter2(this))) {
                        showInterAd2(this, new Intent(this, VP_GalleryMainActivity.class));
                    } else {
                        vp_showInterAd(this, new Intent(this, VP_GalleryMainActivity.class));
                    }
                    vp_videoOptImg.setImageResource(R.drawable.vp_video);
                    vp_galleryOptImg.setImageResource(R.drawable.vp_gallery_icon);
                    vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
                    break;
                case R.id.vp_nav_folder:
                    vp_txt_folder.setBackgroundResource(R.drawable.vp_tab_selected);
                    vp_allvideo_txet.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
                    vp_recent_text.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
                    vp_txt_folder.setTextColor(ContextCompat.getColor(this,R.color.font_color));
                    vp_allvideo_txet.setTextColor(ContextCompat.getColor(this,R.color.white));
                    vp_recent_text.setTextColor(ContextCompat.getColor(this,R.color.white));
                    VP_FolderFragment VPFolderFragment = new VP_FolderFragment();
                    showFragment(VPFolderFragment, false);
                    vp_videoOptImg.setImageResource(R.drawable.vp_video_icon);
                    vp_galleryOptImg.setImageResource(R.drawable.vp_gallery);
                    vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
                    break;
                case R.id.vp_nav_recent:
                    vp_recent_text.setBackgroundResource(R.drawable.vp_tab_selected);
                    vp_txt_folder.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
                    vp_allvideo_txet.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
                    vp_txt_folder.setTextColor(ContextCompat.getColor(this,R.color.white));
                    vp_allvideo_txet.setTextColor(ContextCompat.getColor(this,R.color.white));
                    vp_recent_text.setTextColor(ContextCompat.getColor(this,R.color.font_color));
                    VP_RecentFragment VPRecentFragment = new VP_RecentFragment();
                    showFragment(VPRecentFragment, false);
                    vp_videoOptImg.setImageResource(R.drawable.vp_video_icon);
                    vp_galleryOptImg.setImageResource(R.drawable.vp_gallery);
                    vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
                    break;
                case R.id.vp_nav_privacy_policy:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(vp_pPolicy(this))));
                    break;
                case R.id.vp_nav_share:
                    try {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                        String st1 = "\n" + getResources().getString(R.string.share_descp) + "\n";
                        String st2 = st1 + getString(R.string.play_store_url) + getPackageName();
                        intent.putExtra(Intent.EXTRA_TEXT, st2);
                        startActivity(Intent.createChooser(intent, "choose one"));
                        break;
                    } catch (Exception unused2) {
                        break;
                    }
                case R.id.vp_nav_video:
                    vp_txt_folder.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
                    vp_allvideo_txet.setBackgroundResource(R.drawable.vp_tab_selected);
                    vp_recent_text.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));

                    vp_txt_folder.setTextColor(ContextCompat.getColor(this,R.color.white));
                    vp_allvideo_txet.setTextColor(ContextCompat.getColor(this,R.color.font_color));
                    vp_recent_text.setTextColor(ContextCompat.getColor(this,R.color.white));
                    VP_VideoFragment VPVideoFragment = new VP_VideoFragment();
                    showFragment(VPVideoFragment, false);
                    vp_videoOptImg.setImageResource(R.drawable.vp_video_icon);
                    vp_galleryOptImg.setImageResource(R.drawable.vp_gallery);
                    vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
                    break;
            }
            VP_Home.this.vp_backHide.closeDrawer(GravityCompat.START);
            return false;
        });
    }


    private void initView() {
        this.vp_navigation = findViewById(R.id.vp_navigation);
        this.vp_backHide = findViewById(R.id.drawer_lay);
        vp_adLayout = findViewById(R.id.adLayout);
        vp_adTextBanner = findViewById(R.id.adTextBanner);
        vp_adContainerView = findViewById(R.id.ad_view_container);
        NavigationView navigationView = findViewById(R.id.vp_nav_lay);
        this.vp_navLay = navigationView;
        navigationView.setItemIconTintList(null);

        this.vp_tab_folder = findViewById(R.id.vp_tab_folder);
        this.vp_allvideo_tab_layout = findViewById(R.id.vp_allvideo_tab_layout);
        this.vp_recent_tab_layout = findViewById(R.id.vp_recent_tab_layout);
        this.vp_txt_folder = findViewById(R.id.vp_txt_folder);
        this.vp_allvideo_txet = findViewById(R.id.vp_allvideo_text);
        this.vp_recent_text = findViewById(R.id.vp_recent_text);
        this.vp_trendOptImg = findViewById(R.id.vp_trendOptImg);

        vp_txt_folder.setBackgroundResource(R.drawable.vp_tab_selected);
        vp_allvideo_txet.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
        vp_recent_text.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
        vp_txt_folder.setTextColor(ContextCompat.getColor(this,R.color.font_color));
        vp_allvideo_txet.setTextColor(ContextCompat.getColor(this,R.color.white));
        vp_recent_text.setTextColor(ContextCompat.getColor(this,R.color.white));
        VP_FolderFragment VPFolderFragment = new VP_FolderFragment();
        showFragment(VPFolderFragment, false);

        vp_tab_folder.setOnClickListener(view -> {
            vp_txt_folder.setBackgroundResource(R.drawable.vp_tab_selected);
            vp_allvideo_txet.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
            vp_recent_text.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
            vp_txt_folder.setTextColor(ContextCompat.getColor(this,R.color.font_color));
            vp_allvideo_txet.setTextColor(ContextCompat.getColor(this,R.color.white));
            vp_recent_text.setTextColor(ContextCompat.getColor(this,R.color.white));
            VP_FolderFragment VPFolderFragment1 = new VP_FolderFragment();
            showFragment(VPFolderFragment1, false);
        });
        vp_allvideo_tab_layout.setOnClickListener(view -> {
            vp_txt_folder.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
            vp_allvideo_txet.setBackgroundResource(R.drawable.vp_tab_selected);
            vp_recent_text.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
            vp_txt_folder.setTextColor(ContextCompat.getColor(this,R.color.white));
            vp_allvideo_txet.setTextColor(ContextCompat.getColor(this,R.color.font_color));
            vp_recent_text.setTextColor(ContextCompat.getColor(this,R.color.white));
            VP_VideoFragment VPVideoFragment = new VP_VideoFragment();
            showFragment(VPVideoFragment, false);
        });
        vp_recent_tab_layout.setOnClickListener(view -> {
            vp_recent_text.setBackgroundResource(R.drawable.vp_tab_selected);
            vp_txt_folder.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
            vp_allvideo_txet.setBackgroundColor(ContextCompat.getColor(this,R.color.tab_bg));
            vp_txt_folder.setTextColor(ContextCompat.getColor(this,R.color.white));
            vp_allvideo_txet.setTextColor(ContextCompat.getColor(this,R.color.white));
            vp_recent_text.setTextColor(ContextCompat.getColor(this,R.color.font_color));
            VP_RecentFragment VPRecentFragment = new VP_RecentFragment();
            showFragment(VPRecentFragment, false);
        });

        this.vp_videoOptImg = findViewById(R.id.vp_videoOptImg);
        this.vp_galleryOptImg = findViewById(R.id.vp_glryOptImg);
        this.vp_gameOptImg = findViewById(R.id.vp_gameOptImg);
        this.vp_gameBtnLyt = findViewById(R.id.vp_gameBtnLyt);

        vp_videoOptImg.setImageResource(R.drawable.vp_video_icon);
        vp_galleryOptImg.setImageResource(R.drawable.vp_gallery);
        vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
        if (vp_getShowGameBtn(VP_Home.this)) {
            vp_gameBtnLyt.setVisibility(View.VISIBLE);
        } else {
            vp_gameBtnLyt.setVisibility(View.GONE);
        }

        if (vp_ShowTrendingAll(this).equals(vp_trueString())) {
            if (!vp_ShowTrending(this)) {
                setOneSignal();
                vp_trendOptImg.setVisibility(View.GONE);
            } else {
                vp_trendOptImg.setVisibility(View.VISIBLE);
                ShowTrendPointer();
            }
        } else if (vp_ShowTrendingAll(this).equals(vp_falseString())) {
            setOneSignal();
            vp_trendOptImg.setVisibility(View.GONE);
        } else if (vp_ShowTrendingAll(this).equals(vp_allString())) {
            setOneSignal();
            vp_trendOptImg.setVisibility(View.VISIBLE);
            ShowTrendPointer();
        } else {
            setOneSignal();
            vp_trendOptImg.setVisibility(View.GONE);
        }
    }

    private void ShowTrendPointer() {
        if (vp_showTrendPointer(this)) {
            return;
        }
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.vp_trendOptImg), pointer1, pointer2)
                .cancelable(false)
                .drawShadow(true)
                .outerCircleColor(R.color.colorAccent1_transparent)
                .textColor(R.color.black)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                vp_showTrendPointerEdit(VP_Home.this);
                vp_showTrendInterAd(VP_Home.this, new Intent(VP_Home.this, VP_TrendVidCats.class), false);
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
            }
        });
    }

    private void setOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId("4f7c87f9-e182-4f82-afa6-f003215ccbab");
    }

    public void showFragment(Fragment fragment, boolean z) {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.setReorderingAllowed(true);
        if (z) {
            beginTransaction.addToBackStack(fragment.getTag());
        } else {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
                getSupportFragmentManager().popBackStack();
            }
        }
        beginTransaction.replace(R.id.vp_frame, fragment);
        beginTransaction.commit();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.vp_navigation) {
            this.vp_backHide.openDrawer(GravityCompat.START);
        } else if (id == R.id.vp_videoOptImg) {
            vp_videoOptImg.setImageResource(R.drawable.vp_video_icon);
            vp_galleryOptImg.setImageResource(R.drawable.vp_gallery);
            vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
        } else if (id == R.id.vp_glryOptImg) {
            if (vp_GInter1(this).equals(vp_GInter2(this))) {
                vp_videoOptImg.setImageResource(R.drawable.vp_video);
                vp_galleryOptImg.setImageResource(R.drawable.vp_gallery_icon);
                vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
                showInterAd2(this, new Intent(this, VP_GalleryMainActivity.class));
            } else {
                vp_videoOptImg.setImageResource(R.drawable.vp_video);
                vp_galleryOptImg.setImageResource(R.drawable.vp_gallery_icon);
                vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);
                vp_showInterAd(this, new Intent(this, VP_GalleryMainActivity.class));
            }
        } else if (id == R.id.vp_gameOptImg) {
            vp_ShowAppOpen = false;
            vp_videoOptImg.setImageResource(R.drawable.vp_video);
            vp_galleryOptImg.setImageResource(R.drawable.vp_gallery);
            vp_gameOptImg.setImageResource(R.drawable.vp_game_controller_icon);
            VP_ChromeLauncher.vp_launchGame(this, VP_SharePref.vp_GameIconURL(this), "");
        } else if (id == R.id.vp_trendOptImg) {
            if (VP_MyApplication.vp_getTrendSetPass()) {
                vp_showTrendInterAd(this, new Intent(this, VP_TrendSecure.class), false);
            } else {
                vp_showTrendInterAd(this, new Intent(this, VP_TrendVidCats.class), false);
            }
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        try {
            if (this.vp_backHide.isDrawerOpen(GravityCompat.START)) {
                this.vp_backHide.closeDrawer(GravityCompat.START);
            } else {
                if (vp_back_pressed + VP_TIME_DELAY > System.currentTimeMillis()) {
                    finishAffinity();
                } else {
                    Toast.makeText(this, "Press once again to exit!",
                            Toast.LENGTH_SHORT).show();
                }
                vp_back_pressed = System.currentTimeMillis();
            }
        } catch (Exception e) {
            finishAffinity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vp_ShowAppOpen = true;
        if (!vp_AppOpenIntent(this) && vp_TrendChangeAct) {
            vp_TrendChangeAct = false;
            startActivity(vp_intent1);
        }

        vp_videoOptImg.setImageResource(R.drawable.vp_video_icon);
        vp_galleryOptImg.setImageResource(R.drawable.vp_gallery);
        vp_gameOptImg.setImageResource(R.drawable.vp_game_controller);

        appUpdateManager.getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo, IMMEDIATE, this, MY_REQUEST_CODE);
                                } catch (IntentSender.SendIntentException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
    }

    private void checkForNewUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(VP_Home.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, IMMEDIATE, this, MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void request_notification_api13_permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (this.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 22);
            }
        }
    }
}
