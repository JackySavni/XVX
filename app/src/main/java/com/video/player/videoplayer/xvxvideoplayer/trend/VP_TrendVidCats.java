package com.video.player.videoplayer.xvxvideoplayer.trend;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity.vp_ShowAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_QurekaInterAd.qurekaBigBannerArray;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_1FBEnable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_AppOpenIntent;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_FBNative1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GBanner2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GNative2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_MGL_ID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_SharedPrefName;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_ShowTrendPass;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_ShowTrendPassEdit;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_TrendChangeAct;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_intent1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_isValidContext;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_nativeOutSide;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_noOfNative;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showInterAd;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showTrendInterAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
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
import com.onesignal.OneSignal;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.model.VP_MediaObject;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VP_TrendVidCats extends AppCompatActivity {
    private final ArrayList<VP_TrendCateModel> vp_arrayList = new ArrayList<>();
    private final ArrayList<VP_TrendCateModel> vp_arrayListNew = new ArrayList<>();
    public static final ArrayList<VP_MediaObject> vp_arrayListAds = new ArrayList<>();
    private RecyclerView vp_catRecyclerView;
    private String vp_baseURL = "";
    private FrameLayout vp_adContainerView;
    private TextView vp_adTextBanner,ad_textNative;
    private RelativeLayout vp_adLayout;
    private SpinKitView vp_progressBar;
    public static String vp_para1 = "", vp_para2 = "";
    private ImageView vp_toolbarPasswordBtn;
    public static String vp_trendAdURL = "";
    private FrameLayout vp_nativeContainer;
    private NativeAd vp_mNativeAd;

    private com.facebook.ads.NativeAd nativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_activity_ex_trend_vid_cats);
        vp_Init();
        vp_getData();
    }

    private void getDataAds() {
        vp_arrayListAds.clear();
        try {
            new VP_RetrofitInstance(vp_para1).getRetrofitInstance(vp_para1).VPApiInterface.getGIFList(vp_trendAdURL).enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("Sheet2");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            vp_arrayListAds.add(new VP_MediaObject(
                                    jsonObject1.getString("title"),
                                    jsonObject1.getString("gif_url"),
                                    jsonObject1.getString("redirect_link"), true));
                        }
                        vp_progressBar.setVisibility(View.GONE);
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

    private void vp_Init() {
        Toolbar vp_toolbar = findViewById(R.id.vp_toolbar);
        setSupportActionBar(vp_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        vp_toolbarPasswordBtn = findViewById(R.id.toolbar_password_btn);
        RelativeLayout nativeMainContainer = findViewById(R.id.native_main_container);
        vp_nativeContainer = findViewById(R.id.native_container);
        vp_setOneSignal();
        vp_ShowTrendPointer();
        vp_toolbarPasswordBtn.setOnClickListener(v -> startActivity(new Intent(this, VP_TrendSecure.class)));
        vp_catRecyclerView = findViewById(R.id.vp_cat_recycler_view);
        vp_catRecyclerView.setHasFixedSize(true);
        vp_adLayout = findViewById(R.id.adLayout);
        vp_adTextBanner = findViewById(R.id.adTextBanner);
        vp_adContainerView = findViewById(R.id.ad_view_container);
        vp_progressBar = findViewById(R.id.vp_spinkit_progress_bar);
        ad_textNative = findViewById(R.id.ad_textNative);

        if (vp_gEnabled(this) && vp_1FBEnable(this)) {
            loadBanner();
            if (vp_nativeOutSide(this)) {
                nativeMainContainer.setVisibility(View.VISIBLE);
                loadNative();
            }
        } else if (vp_gEnabled(this) && !vp_1FBEnable(this)) {
            loadBanner();
            if (vp_nativeOutSide(this)) {
                nativeMainContainer.setVisibility(View.VISIBLE);
                loadNative();
            }
        } else if (!vp_gEnabled(this) && vp_1FBEnable(this)) {
            loadFBBannerAd();
            if (vp_nativeOutSide(this)) {
                nativeMainContainer.setVisibility(View.VISIBLE);
                loadFBNativeAd();
            }
        } else {
            nativeMainContainer.setVisibility(View.VISIBLE);
            loadQurekaNative();
            loadQurekaBanner();
        }

        if (!getSharedPreferences(vp_SharedPrefName, Context.MODE_PRIVATE).getBoolean("CheckTrendEntry", false)) {
            new VP_Constant().vp_trendEntryRef().push()
                    .setValue(String.valueOf(getSharedPreferences(vp_SharedPrefName, Context.MODE_PRIVATE)
                            .getBoolean("Trending", false)));
            getSharedPreferences(vp_SharedPrefName, Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("CheckTrendEntry", true)
                    .apply();
        }
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
                                if (vp_1FBEnable(VP_TrendVidCats.this)){
                                    loadFBNativeAd();
                                }else {
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

    private void loadFBNativeAd() {
        nativeAd = new com.facebook.ads.NativeAd(this, vp_FBNative1(VP_TrendVidCats.this));
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

                NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes(VP_TrendVidCats.this)
                        .setBackgroundColor(ContextCompat.getColor(VP_TrendVidCats.this, R.color.nav_bar_color))
                        .setTitleTextColor(Color.WHITE)
                        .setDescriptionTextColor(Color.LTGRAY)
                        .setButtonColor(Color.WHITE)
                        .setButtonTextColor(Color.BLACK);

                View adView = com.facebook.ads.NativeAdView.render(VP_TrendVidCats.this, nativeAd, viewAttributes);
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

    private void loadQurekaNative() {
        ad_textNative.setVisibility(View.GONE);
        LayoutInflater inflater = LayoutInflater.from(VP_TrendVidCats.this);
        View view = inflater.inflate(R.layout.vp_row_qureka_native_ad, vp_nativeContainer, false);
        ShimmerTextView adTitle = view.findViewById(R.id.vp_adTitle);
        Shimmer shimmer = new Shimmer();
        shimmer.start(adTitle);
        ImageView nativeImg = view.findViewById(R.id.qureka_native_img);
        nativeImg.setImageResource(qurekaBigBannerArray[new Random().nextInt(qurekaBigBannerArray.length)]);
        vp_nativeContainer.removeAllViews();
        vp_nativeContainer.addView(view);
        vp_nativeContainer.setOnClickListener(view1 ->
                VP_ChromeLauncher.vp_launchGame(VP_TrendVidCats.this,
                        vp_QurekaID(VP_TrendVidCats.this), ""));
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
                if (vp_1FBEnable(VP_TrendVidCats.this)){
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

    private void vp_getData() {
        new VP_Constant().vp_trendRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadCats(snapshot.child("trendURL").getValue(String.class),
                        snapshot.child("trendURL2").getValue(String.class),
                        snapshot.child("trendKEY").getValue(String.class));
                vp_trendAdURL = snapshot.child("TrendAdURL").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCats(final String trendUrl1, String trendUrl2, String trendKEY) {
        vp_arrayList.clear();
        vp_para2 = trendUrl2;
        try {
            vp_para1 = VP_VCrypt.vp_decrypt(trendUrl1, trendKEY, trendKEY);
            new VP_RetrofitInstance(vp_para1).getRetrofitInstance(vp_para1).VPApiInterface.getCats(trendUrl2).enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("Sheet1");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            vp_arrayList.add(new VP_TrendCateModel(
                                    jsonObject1.getString("cat_name"),
                                    jsonObject1.getString("image_url"),
                                    jsonObject1.getBoolean("ad")));
                        }
                        getDataAds();
                        vp_InitAdapter();
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

    private void vp_InitAdapter() {
        vp_arrayListNew.clear();
        int vp_j = 18, vp_k = 3;
        for (int vp_i = 0; vp_i < vp_arrayList.size(); vp_i++) {
            if (!vp_nativeOutSide(this) && vp_i != 0 && vp_i == vp_k && vp_k < (vp_noOfNative(this) * vp_j + 1)) {
                vp_arrayListNew.add(new VP_TrendCateModel("", "", false, true));
                vp_k = vp_k + vp_j;
            }
            vp_arrayListNew.add(new VP_TrendCateModel(
                    vp_arrayList.get(vp_i).getVp_cateName(),
                    vp_arrayList.get(vp_i).getVp_catThumb(),
                    vp_arrayList.get(vp_i).getVp_qurekaAd(),
                    false));
        }

        GridLayoutManager vp_gridLayoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        vp_gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (vp_arrayListNew.get(position).isVp_isAd()) {
                    return 3;
                } else {
                    return 1;
                }

            }
        });
        vp_catRecyclerView.setLayoutManager(vp_gridLayoutManager);
        vp_catRecyclerView.setAdapter(new CatRecyclerAdapter(this, vp_arrayListNew));
    }

    public class CatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final Activity vp_context;
        private final ArrayList<VP_TrendCateModel> vp_arrayList;
        private final int VP_ITEM = 1;
        private final int VP_NATIVE = 2;
        private NativeAd vp_mNativeAd;

        public CatRecyclerAdapter(Activity vp_context, ArrayList<VP_TrendCateModel> vp_arrayList) {
            this.vp_context = vp_context;
            this.vp_arrayList = vp_arrayList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view;
            if (i == VP_ITEM) {
                view = LayoutInflater.from(vp_context).inflate(R.layout.vp_layout_cat_list_item, viewGroup,
                        false);
                return new ItemHolder(view);
            } else if (i == VP_NATIVE) {
                view = LayoutInflater.from(vp_context).inflate(R.layout.vp_native_container, viewGroup
                        , false);
                return new NativeHolder(view);
            }
            return null;
        }

        @Override
        public int getItemViewType(int position) {
            if (vp_arrayList.get(position).isVp_isAd()) {
                return VP_NATIVE;
            } else {
                return VP_ITEM;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder.getItemViewType() == VP_ITEM) {
                ItemHolder vp_itemHolder = (ItemHolder) viewHolder;
                final VP_TrendCateModel VPTrendCateModel = vp_arrayList.get(viewHolder.getAbsoluteAdapterPosition());
                vp_itemHolder.vp_catName.setText(VPTrendCateModel.getVp_cateName());
                if (vp_isValidContext(vp_context)) {
                    Glide.with(vp_context).load(VPTrendCateModel.getVp_catThumb())
                            .placeholder(R.drawable.vp_placeholder)
                            .error(R.drawable.vp_placeholder)
                            .into(vp_itemHolder.vp_catThumb);
                }
                vp_itemHolder.itemView.setOnClickListener(v -> {
                            if (VPTrendCateModel.getVp_qurekaAd()) {
                                VP_ChromeLauncher.vp_launchGame(vp_context, vp_MGL_ID(vp_context), "");
                            } else {
                                vp_showTrendInterAd(
                                        VP_TrendVidCats.this,
                                        new Intent(vp_context, VP_TrendVidList.class)
                                                .putExtra("para1", vp_para1)
                                                .putExtra("para2", vp_para2)
                                                .putExtra("title", VPTrendCateModel.getVp_cateName()), false);
                            }
                        }
                );

            } else if (viewHolder.getItemViewType() == VP_NATIVE) {
                NativeHolder vp_nativeHolder = (NativeHolder) viewHolder;
                if (vp_gEnabled(vp_context)) {
                    AdLoader.Builder builder = new AdLoader.Builder(vp_context, vp_GNative2(vp_context));
                    builder.forNativeAd(nativeAd -> {
                        if (vp_mNativeAd != null) {
                            vp_mNativeAd.destroy();
                        }
                        vp_mNativeAd = nativeAd;
                        NativeAdView nativeAdView = (NativeAdView) getLayoutInflater().inflate(R.layout.vp_native_adunified, null);
                        populateUnifiedNativeAdView(vp_nativeHolder.vp_mAdContainer, nativeAd, nativeAdView);
                    });

                    AdLoader adLoader = builder.withAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            vp_nativeHolder.vp_adText.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            vp_nativeHolder.vp_adText.setVisibility(View.GONE);
                            loadQurekaNative(vp_context, vp_nativeHolder.vp_mAdContainer);
                        }
                    }).build();

                    adLoader.loadAd(new AdRequest.Builder().build());
                } else {
                    vp_nativeHolder.vp_adText.setVisibility(View.GONE);
                    loadQurekaNative(vp_context, vp_nativeHolder.vp_mAdContainer);
                }
            }

        }

        @Override
        public int getItemCount() {
            return vp_arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        public class ItemHolder extends ViewHolder {
            TextView vp_catName;
            ImageView vp_catThumb;

            public ItemHolder(View view) {
                super(view);
                vp_catName = view.findViewById(R.id.vp_cat_name);
                vp_catThumb = view.findViewById(R.id.vp_cat_thumb);
                vp_catName.setSelected(true);
            }
        }

        public class NativeHolder extends ViewHolder {
            private final LinearLayout vp_mAdContainer;
            private final TextView vp_adText;

            public NativeHolder(@NonNull View itemView) {
                super(itemView);
                vp_adText = itemView.findViewById(R.id.ad_text);
                vp_mAdContainer = itemView.findViewById(R.id.ad_container);
            }
        }

        private void loadQurekaNative(Activity context, LinearLayout mAdContainer) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view1;
            view1 = inflater.inflate(R.layout.vp_row_qureka_native_ad, mAdContainer, false);
            ShimmerTextView adTitle = view1.findViewById(R.id.vp_adTitle);
            Shimmer shimmer = new Shimmer();
            shimmer.start(adTitle);
            mAdContainer.removeAllViews();
            mAdContainer.addView(view1);
            view1.setOnClickListener(view -> VP_ChromeLauncher.vp_launchGame(context, vp_QurekaID(context), ""));
        }

        private void populateUnifiedNativeAdView(LinearLayout nativeContainer, NativeAd nativeAd, NativeAdView adView) {
            if (adView != null) {
                nativeContainer.removeAllViews();
                nativeContainer.addView(adView);
            }
            adView.setMediaView(adView.findViewById(R.id.ad_media));
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
    }

    private void vp_setOneSignal() {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId("0ced7bee-8202-4625-a79b-fcf83e1deac3");
    }

    private void vp_ShowTrendPointer() {
        if (vp_ShowTrendPass(this)) {
            return;
        }
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.toolbar_password_btn), "Set Password", "Set Privacy Password to This Videos")
                .cancelable(true)
                .drawShadow(true)
                .outerCircleColor(R.color.colorAccent1_transparent)
                .textColor(R.color.black)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                vp_ShowTrendPassEdit(VP_TrendVidCats.this);
                vp_showInterAd(VP_TrendVidCats.this, new Intent(VP_TrendVidCats.this, VP_TrendSecure.class));
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                vp_ShowTrendPassEdit(VP_TrendVidCats.this);
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                vp_ShowTrendPassEdit(VP_TrendVidCats.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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