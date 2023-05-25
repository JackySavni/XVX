package com.video.player.videoplayer.xvxvideoplayer.gallery.view.viewer;

import static com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryMediaController.REQ_CODE_DELETE;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.RemoveAllAds;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_1FBEnable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GBanner1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryPhotoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryAlbumsController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryPhotoController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryAlbum;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;

import java.util.ArrayList;

public class VP_GalleryAlbumActivity extends AppCompatActivity implements VP_GalleryPhotoAdapter.VP_PhotoListener {

    public static final String VP_ALBUM_KEY = "albumKey";
    //
    private VP_GalleryAlbum VPGalleryAlbum;
    //
    private RecyclerView vp_recyclerView;
    private VP_GalleryPhotoAdapter vp_adapter;
    private TextView vp_txtTitle;
    private ScaleGestureDetector vp_scaleGestureDetector;
    private ImageButton vp_btnFinish;
    private FrameLayout vp_adContainerView;
    private TextView vp_adTextBanner;
    private AdView vp_adView;
    private RelativeLayout vp_adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_gallery_activity_album);
        vp_init();
        if (!RemoveAllAds(this)) {
            if (vp_gEnabled(this)) {
                loadBanner();
            } else if (vp_1FBEnable(this)) {
                loadFBBannerAd();
            } else {
                loadQurekaBanner();
            }
        } else {
            vp_adLayout.setVisibility(View.GONE);
        }
        vp_setListeners();
        vp_setAdapter();
        VP_GalleryAlbumsController.ex_showBtnFinish(vp_btnFinish);
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
                if (vp_1FBEnable(VP_GalleryAlbumActivity.this)) {
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

    private void vp_init() {
        this.vp_recyclerView = findViewById(R.id.vp_recyclerViewPhotos);
        this.vp_txtTitle = findViewById(R.id.vp_txtTitle);
        ConstraintLayout vp_layoutPhotos = findViewById(R.id.layoutPhotos);
        vp_adLayout = findViewById(R.id.adLayout);
        vp_adTextBanner = findViewById(R.id.adTextBanner);
        vp_adContainerView = findViewById(R.id.ad_view_container);
        this.vp_scaleGestureDetector = new ScaleGestureDetector(this,
                VP_GalleryPhotoController.getScaleGestureDetector(vp_recyclerView, vp_adapter, vp_layoutPhotos));
        this.vp_btnFinish = findViewById(R.id.vp_btnFinish);
    }

    private void vp_setAdapter() {
        try {
            this.VPGalleryAlbum = ((VP_MyApplication) getApplicationContext()).VPGalleryAlbum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        vp_adapter = new VP_GalleryPhotoAdapter(this, VPGalleryAlbum.getPhotos(), VPGalleryAlbum.getVp_name(), true, this);
        vp_recyclerView.setAdapter(vp_adapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void vp_setListeners() {
        this.vp_recyclerView.setOnTouchListener((view1, motionEvent) -> {
            vp_scaleGestureDetector.onTouchEvent(motionEvent);
            return false;
        });

        this.vp_btnFinish.setOnClickListener(v -> onBackPressed());
    }

    public VP_GalleryPhotoAdapter getVp_adapter() {
        return vp_adapter;
    }

    @Override
    public void onBackPressed() {
        ((VP_MyApplication) getApplicationContext()).VPGalleryAlbum =
                new VP_GalleryAlbum("", 0, 0L, new ArrayList<>());
        ((VP_MyApplication) getApplicationContext()).vp_deleteList = new ArrayList<>();
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void notifyPhotosCount(String title, int photosCount) {
        vp_txtTitle.setText(title + " ∙ " + photosCount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_DELETE) {
            if (resultCode == RESULT_OK) {
                //delete photo from adapter and close this activity
                for (VP_GalleryPhoto VPGalleryPhoto : ((VP_MyApplication) getApplicationContext()).vp_deleteList) {
                    ((VP_MyApplication) getApplicationContext()).VPGalleryAlbum.getPhotos().remove(VPGalleryPhoto);
                }
                vp_setAdapter();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        vp_setAdapter();
        this.notifyPhotosCount(VPGalleryAlbum.getVp_name(), VPGalleryAlbum.getPhotos().size());
    }
}