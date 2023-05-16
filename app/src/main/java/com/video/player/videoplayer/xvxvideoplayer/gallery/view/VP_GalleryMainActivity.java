package com.video.player.videoplayer.xvxvideoplayer.gallery.view;

import static com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity.vp_ShowAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.showInterAd2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_1FBEnable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GBanner1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_ShowTrending;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_ShowTrendingAll;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_allString;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_falseString;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_getShowGameBtn;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showInterAd;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_trueString;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.tabs.TabLayout;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryViewPagerTitleAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryMainController;
import com.video.player.videoplayer.xvxvideoplayer.trend.VP_TrendSecure;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;

public class VP_GalleryMainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int VP_STORAGE_REQ_CODE = 111;
    private ViewPager vp_viewPager;
    public static VP_GalleryViewPagerTitleAdapter vp_adapter;
    private TabLayout vp_tabLayout;
    private FrameLayout vp_adContainerView;
    private TextView vp_adTextBanner;
    private RelativeLayout vp_adLayout, vp_gameBtnLyt;
    private ImageView vp_videoOptImg, vp_galleryOptImg, vp_trendOptImg, vp_gameOptImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_gallery_activity_main);

        vp_Init();
        if (vp_gEnabled(this) && vp_1FBEnable(this)) {
            loadBanner();
        } else if (vp_gEnabled(this) && !vp_1FBEnable(this)) {
            loadBanner();
        } else if (!vp_gEnabled(this) && vp_1FBEnable(this)) {
            loadFBBannerAd();
        } else {
            loadQurekaBanner();
        }
        vp_checkStoragePermission();
    }

    private void vp_Init() {
        vp_adLayout = findViewById(R.id.adLayout);
        vp_adTextBanner = findViewById(R.id.adTextBanner);
        vp_adContainerView = findViewById(R.id.ad_view_container);
        vp_videoOptImg = findViewById(R.id.vp_videoOptImg);
        vp_trendOptImg = findViewById(R.id.vp_trendOptImg);
        vp_galleryOptImg = findViewById(R.id.vp_glryOptImg);
        vp_gameOptImg = findViewById(R.id.vp_gameOptImg);
        this.vp_gameBtnLyt = findViewById(R.id.vp_gameBtnLyt);
        if (vp_getShowGameBtn(VP_GalleryMainActivity.this)) {
            vp_gameBtnLyt.setVisibility(View.VISIBLE);
        } else {
            vp_gameBtnLyt.setVisibility(View.GONE);
        }

        if (vp_ShowTrendingAll(this).equals(vp_trueString())) {
            if (!vp_ShowTrending(this)) {
                vp_trendOptImg.setVisibility(View.GONE);
            }
        } else if (vp_ShowTrendingAll(this).equals(vp_falseString())) {
            vp_trendOptImg.setVisibility(View.GONE);
        } else if (vp_ShowTrendingAll(this).equals(vp_allString())) {
            vp_trendOptImg.setVisibility(View.VISIBLE);
        }
        this.vp_videoOptImg.setOnClickListener(this);
        this.vp_galleryOptImg.setOnClickListener(this);
        this.vp_trendOptImg.setOnClickListener(this);
        this.vp_gameOptImg.setOnClickListener(this);
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
                if (vp_1FBEnable(VP_GalleryMainActivity.this)){
                    loadFBBannerAd();
                }else {
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

    /**
     * set adapter
     * set viewpager to tablayout
     * set viewpager page limit - 3 = number of fragments
     */
    private void vp_configViewPager() {
        this.vp_viewPager.setAdapter(vp_adapter);
        this.vp_tabLayout.setupWithViewPager(vp_viewPager);
        this.vp_viewPager.setOffscreenPageLimit(3);
    }

    private void vp_init() {
        this.vp_viewPager = findViewById(R.id.vp_viewPagerMain);
        this.vp_tabLayout = findViewById(R.id.vp_tabLayoutMain);
        vp_adapter = new VP_GalleryViewPagerTitleAdapter(getSupportFragmentManager(), VP_GalleryMainController.ex_getFragments(), VP_GalleryMainController.ex_getTitles());
    }

    private void vp_setListeners() {
        this.vp_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.vp_tabLayout));
        this.vp_tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public static VP_GalleryViewPagerTitleAdapter vp_getAdapter() {
        return vp_adapter;
    }

    /**
     * check storage permissions
     */
    private void vp_checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                vp_init();
                vp_configViewPager();
                vp_setListeners();
            } else {
                ActivityCompat.requestPermissions(VP_GalleryMainActivity.this,
                        vp_permissions(),
                        VP_GalleryMainActivity.VP_STORAGE_REQ_CODE);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                vp_init();
                vp_configViewPager();
                vp_setListeners();
            } else {
                ActivityCompat.requestPermissions(VP_GalleryMainActivity.this,
                        vp_permissions(),
                        VP_GalleryMainActivity.VP_STORAGE_REQ_CODE);
            }
        }
    }

    public static String[] vp_permissions() {
        String[] vp_p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            vp_p = storge_permissions_33;
        } else {
            vp_p = vp_storge_permissions;
        }
        return vp_p;
    }

    public static String[] vp_storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean vp_isGranted = false;
        //init views and show fragments or close app
        if (requestCode == VP_GalleryMainActivity.VP_STORAGE_REQ_CODE) {
            for (int grantResult : grantResults) {
                vp_isGranted = grantResult != PackageManager.PERMISSION_DENIED;
            }
            if (vp_isGranted) {
                vp_init();
                vp_configViewPager();
                vp_setListeners();
            } else {
                ActivityCompat.requestPermissions(VP_GalleryMainActivity.this,
                        vp_permissions(),
                        VP_GalleryMainActivity.VP_STORAGE_REQ_CODE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.vp_videoOptImg) {
            onBackPressed();
        } else if (id == R.id.vp_trendOptImg) {
            if (vp_GInter1(this).equals(vp_GInter2(this))) {
                showInterAd2(this, new Intent(this, VP_TrendSecure.class));
            } else {
                vp_showInterAd(this, new Intent(this, VP_TrendSecure.class));
            }
        } else if (id == R.id.vp_gameOptImg) {
            VP_ChromeLauncher.vp_launchGame(this, VP_SharePref.vp_GameIconURL(this), "");
            vp_ShowAppOpen = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vp_ShowAppOpen = true;
    }
}