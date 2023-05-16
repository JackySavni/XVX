package com.video.player.videoplayer.xvxvideoplayer.activities;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GBanner1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.adapters.VP_VideoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_EventBus;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_Folder;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class VP_Videolist extends AppCompatActivity implements View.OnClickListener {
    @SuppressLint("StaticFieldLeak")
    public static ImageView vp_ivNoData;
    private ImageView vp_backFolder;
    public static RecyclerView vp_videoList;
    private ProgressBar vp_progress;
    private VP_Folder vp_mediaData;
    private FrameLayout vp_adContainerView;
    private RelativeLayout vp_adLayout;
    private TextView vp_adTextBanner;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_activity_videolist);
        @SuppressLint("WrongConstant") NetworkInfo vp_activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (vp_activeNetworkInfo != null) {
            vp_activeNetworkInfo.isConnected();
        }
        vp_mediaData = (VP_Folder) getIntent().getSerializableExtra("data");
        initView();
        initListener();

        vp_adLayout = findViewById(R.id.adLayout);
        vp_adTextBanner = findViewById(R.id.adTextBanner);
        vp_adContainerView = findViewById(R.id.ad_view_container);

        if (vp_gEnabled(this)) {
            loadBanner();
        } else {
            loadQurekaBanner();
        }
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
                loadQurekaBanner();
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

    private void initListener() {
        this.vp_backFolder.setOnClickListener(this);
    }

    private void initView() {
        vp_videoList = findViewById(R.id.vp_video_list);
        vp_progress = findViewById(R.id.vp_progress);
        TextView vp_foldername = findViewById(R.id.vp_folder_name);
        vp_foldername.setSelected(true);
        this.vp_backFolder = findViewById(R.id.vp_back_folder);
        vp_ivNoData = findViewById(R.id.vp_iv_nodata);
        vp_foldername.setText(vp_mediaData.getVp_name());
        initAdapter(vp_mediaData.getMedia_data(), VP_MyApplication.vp_getViewBy());
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VP_EventBus event_Bus) {
        Log.d("TAG", "onMessageEventCheck: " + event_Bus.getVp_type() + " : " + event_Bus.getVp_value());
        int type = event_Bus.getVp_type();
        if (type == 1) {
            vp_mediaData.getMedia_data().remove(event_Bus.getVp_value());
            initAdapter(vp_mediaData.getMedia_data(), VP_MyApplication.vp_getViewBy());
        }
    }

    @SuppressLint("WrongConstant")
    private void initAdapter(ArrayList<MediaData> arrayList, int i) {
        vp_progress.setVisibility(8);
        if (arrayList.size() > 0) {
            vp_videoList.setVisibility(0);
            vp_ivNoData.setVisibility(8);
            VP_VideoAdapter vp_video_Adapter = new VP_VideoAdapter(this, arrayList, i, 1);
//            if (i == 0) {
//                ex_videoList.setLayoutManager(new LinearLayoutManager(this, 1, false));
//            } else {
                vp_videoList.setLayoutManager(new GridLayoutManager(this, 2, 1, false));
//            }
            vp_videoList.setAdapter(vp_video_Adapter);
            return;
        }
        vp_videoList.setVisibility(8);
        vp_ivNoData.setVisibility(0);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.vp_back_folder) {
            onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
