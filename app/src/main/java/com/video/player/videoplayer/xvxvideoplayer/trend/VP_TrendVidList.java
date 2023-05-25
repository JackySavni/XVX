package com.video.player.videoplayer.xvxvideoplayer.trend;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity.vp_ShowAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.trend.VP_TrendVidCats.vp_arrayListAds;
import static com.video.player.videoplayer.xvxvideoplayer.trend.VP_TrendVidCats.vp_para2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_QurekaInterAd.qurekaBigBannerArray;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.RemoveAllAds;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_1FBEnable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_AppOpenIntent;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_FBNative1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GBanner2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GNative2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_TrendChangeAct;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_intent1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_nativeTrendOutSide;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_noOfNative;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonElement;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.VP_VideoPlayerRecyclerView;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.adapter.VP_VideoPlayerRecyclerAdapter;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.model.VP_MediaObject;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VP_TrendVidList extends AppCompatActivity {
    private VP_VideoPlayerRecyclerView VPVideoPlayerRecyclerView;
    private String vp_mParam1 = "", vp_mParam2 = "", vp_mParam3 = "";
    private FrameLayout vp_adContainerView;
    private TextView vp_adTextBanner, ad_textNative;
    private RelativeLayout vp_adLayout;
    private SpinKitView vp_progressBar;
    private FrameLayout vp_nativeContainer;
    private NativeAd vp_mNativeAd;
    private com.facebook.ads.NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_activity_ex_trend_vid_list);
        vp_Init();
    }

    private void vp_Init() {
        vp_mParam1 = getIntent().getStringExtra("para1");
        vp_mParam2 = getIntent().getStringExtra("para2");
        vp_mParam3 = getIntent().getStringExtra("title");
        vp_progressBar = findViewById(R.id.vp_spinkit_progress_bar);
        VPVideoPlayerRecyclerView = findViewById(R.id.vp_video_player_id);
        VPVideoPlayerRecyclerView.setHasFixedSize(true);
        vp_nativeContainer = findViewById(R.id.native_container);
        RelativeLayout vp_nativeMainContainer = findViewById(R.id.native_main_container);
        ImageView vp_backBtn = findViewById(R.id.vp_back_btn);
        TextView vp_toolbarTitle = findViewById(R.id.vp_toolbar_title);
        vp_toolbarTitle.setText(vp_mParam3);
        vp_adLayout = findViewById(R.id.adLayout);
        vp_adTextBanner = findViewById(R.id.adTextBanner);
        vp_adContainerView = findViewById(R.id.ad_view_container);
        ad_textNative = findViewById(R.id.ad_textNative);

        if (!RemoveAllAds(this)) {
            if (vp_gEnabled(this)) {
                loadBanner();
                if (vp_nativeTrendOutSide(this)) {
                    vp_nativeMainContainer.setVisibility(View.VISIBLE);
                    loadNative();
                }
            } else if (vp_1FBEnable(this)) {
                loadFBBannerAd();
                if (vp_nativeTrendOutSide(this)) {
                    vp_nativeMainContainer.setVisibility(View.VISIBLE);
                    loadFBNativeAd();
                }
            } else {
                vp_nativeMainContainer.setVisibility(View.VISIBLE);
                loadQurekaNative();
                loadQurekaBanner();
            }
        } else {
            vp_adLayout.setVisibility(View.GONE);
            vp_nativeMainContainer.setVisibility(View.GONE);
        }
        vp_backBtn.setOnClickListener(v -> finish());
        LinearLayoutManager vp_layoutManager = new LinearLayoutManager(this);
        VPVideoPlayerRecyclerView.setLayoutManager(vp_layoutManager);
        getVid(vp_mParam2);
    }

    private void loadFBNativeAd() {
        nativeAd = new com.facebook.ads.NativeAd(this, vp_FBNative1(VP_TrendVidList.this));
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                loadQurekaNative();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }

                if (vp_nativeContainer == null) {
                    return;
                }

                ad_textNative.setVisibility(View.GONE);
                nativeAd.unregisterView();

                NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes(VP_TrendVidList.this)
                        .setBackgroundColor(ContextCompat.getColor(VP_TrendVidList.this, R.color.nav_bar_color))
                        .setTitleTextColor(Color.WHITE)
                        .setDescriptionTextColor(Color.LTGRAY)
                        .setButtonColor(Color.WHITE)
                        .setButtonTextColor(Color.BLACK);

                View adView = com.facebook.ads.NativeAdView.render(VP_TrendVidList.this, nativeAd, viewAttributes);
                vp_nativeContainer.removeAllViews();
                vp_nativeContainer.addView(adView, new ViewGroup.LayoutParams(MATCH_PARENT, 800));
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };
        nativeAd.loadAd(
                nativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    private void loadNative() {
        AdLoader.Builder builder = new AdLoader.Builder(this, vp_GNative2(this));
        builder.forNativeAd(nativeAd -> {
            if (vp_mNativeAd != null) {
                vp_mNativeAd.destroy();
            }
            vp_mNativeAd = nativeAd;
            NativeAdView nativeAdView = (NativeAdView) this.getLayoutInflater()
                    .inflate(R.layout.vp_native_adunified, null);
            populateUnifiedNativeAdView(nativeAd, nativeAdView);
        });

        AdLoader ig_adLoader = builder.withAdListener(
                        new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                ad_textNative.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                                if (vp_1FBEnable(VP_TrendVidList.this)) {
                                    loadFBNativeAd();
                                } else {
                                    loadQurekaNative();
                                }
                            }
                        })
                .build();
        ig_adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        if (adView != null && vp_nativeContainer != null) {
            vp_nativeContainer.addView(adView);
        }
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.adcalltoaction));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
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

    private void loadQurekaNative() {
        ad_textNative.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(VP_TrendVidList.this);
        View view = inflater.inflate(R.layout.vp_row_qureka_native_ad, vp_nativeContainer, false);
        ShimmerTextView adTitle = view.findViewById(R.id.vp_adTitle);
        Shimmer shimmer = new Shimmer();
        shimmer.start(adTitle);
        ImageView nativeImg = view.findViewById(R.id.qureka_native_img);
        nativeImg.setImageResource(qurekaBigBannerArray[new Random().nextInt(qurekaBigBannerArray.length)]);
        vp_nativeContainer.removeAllViews();
        vp_nativeContainer.addView(view);
        vp_nativeContainer.setOnClickListener(view1 ->
                VP_ChromeLauncher.vp_launchGame(VP_TrendVidList.this,
                        vp_QurekaID(VP_TrendVidList.this), ""));
    }

    private void loadBanner() {
        AdView adView = new AdView(this);
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.setAdUnitId(vp_GBanner2(this));
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
                if (vp_1FBEnable(VP_TrendVidList.this)) {
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

    private RequestManager vp_initGlide() {
        RequestOptions vp_requestOptions = new RequestOptions()
                .placeholder(R.drawable.vp_white_ad_border)
                .error(R.drawable.vp_white_ad_border);

        return Glide.with(this).setDefaultRequestOptions(vp_requestOptions);
    }

    private void getVid(String para2) {
        String vp_url = new File(para2).getParent() + "/" + vp_mParam3 + ".json";
        try {
            new VP_RetrofitInstance(vp_mParam1).getRetrofitInstance(vp_mParam1).VPApiInterface.getVidList(vp_url).enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                    if (response.code() == 404) {
                        getData();
                        return;
                    }
                    try {
                        JSONObject vp_jsonObject = new JSONObject(response.body().toString());
                        JSONArray vp_jsonArray = vp_jsonObject.getJSONArray("Sheet1");
                        ArrayList<VP_MediaObject> vp_arrayList = new ArrayList<>();
                        int vp_j = 6, vp_k = 1, vp_l = 0;
                        for (int vp_i = 0; vp_i < vp_jsonArray.length(); vp_i++) {
                            JSONObject vp_jsonObject1 = (JSONObject) vp_jsonArray.get(vp_i);
                            if (vp_i != 0 && vp_i == vp_k) {
                                if (!vp_nativeTrendOutSide(VP_TrendVidList.this) && vp_gEnabled(VP_TrendVidList.this) && vp_k < (vp_noOfNative(VP_TrendVidList.this) * vp_j + 1)) {
                                    vp_arrayList.add(new VP_MediaObject("", "", "", "", true));
                                } else {
                                    if (vp_arrayListAds.size() > vp_l) {
                                        VP_MediaObject vp_adModel = vp_arrayListAds.get(vp_l);
                                        vp_arrayList.add(new VP_MediaObject(vp_adModel.getVp_title(), vp_adModel.getVp_gif_url(), vp_adModel.getVp_redirect_link(), true));
                                        vp_l++;
                                    } else if (vp_arrayListAds.size() != 0) {
                                        vp_arrayListAds.addAll(vp_arrayListAds);
                                    }
                                }
                                vp_k = vp_k + vp_j;
                            }
                            vp_arrayList.add(new VP_MediaObject(vp_jsonObject1.getString("title"),
                                    vp_jsonObject1.getString("thumbnail"),
                                    vp_jsonObject1.getString("preview"),
                                    vp_jsonObject1.getString("media_url"),
                                    false));
                        }
                        ArrayList<VP_MediaObject> vp_sourceVideos = new ArrayList<>(vp_arrayList);
                        VPVideoPlayerRecyclerView.setMediaObjects(vp_sourceVideos);

                        vp_progressBar.setVisibility(View.GONE);
                        VP_VideoPlayerRecyclerAdapter videoAdapter = new VP_VideoPlayerRecyclerAdapter(VP_TrendVidList.this, vp_arrayList, vp_initGlide());
                        VPVideoPlayerRecyclerView.setAdapter(videoAdapter);
                    } catch (Exception ignored) {
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {

                }
            });
        } catch (Exception ignored) {
        }
    }

    private void getData() {
        new VP_Constant().vp_trendRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                vp_para2 = snapshot.child("trendURL2").getValue(String.class);
                getVid(vp_para2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        vp_ShowAppOpen = true;
        if (!vp_AppOpenIntent(this) && vp_TrendChangeAct) {
            vp_TrendChangeAct = false;
            startActivity(vp_intent1);
        }
    }

}