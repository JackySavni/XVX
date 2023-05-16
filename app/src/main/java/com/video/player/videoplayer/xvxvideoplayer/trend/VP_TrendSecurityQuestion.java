package com.video.player.videoplayer.xvxvideoplayer.trend;

import static com.video.player.videoplayer.xvxvideoplayer.activities.bottom_options.VP_GamingActivity.vp_ShowAppOpen;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_AppOpenIntent;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GNative2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_TrendChangeAct;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_intent1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

public class VP_TrendSecurityQuestion extends AppCompatActivity {
    private RelativeLayout vp_root;
    public Spinner vp_questionSpinner;
    private int vp_type;
    public EditText vp_securityAnswer;
    private FrameLayout vp_nativeContainer;
    private NativeAd vp_mNativeAd;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_activity_securityquestion);
        this.vp_type = getIntent().getIntExtra("type", 0);
        initView();
    }

    private void initView() {
        vp_nativeContainer = findViewById(R.id.native_container);
        loadNative();
        vp_questionSpinner = findViewById(R.id.questionSpinner);
        TextView vp_saveQuestion = findViewById(R.id.saveQuestion);
        this.vp_securityAnswer = findViewById(R.id.securityAnswer);
        ImageView vp_backquestion = findViewById(R.id.back_question);
        this.vp_root = findViewById(R.id.root);
        ArrayAdapter<String> vp_arrayAdapter = new ArrayAdapter<>(this, R.layout.vp_spinner_list, getResources().getStringArray(R.array.security_question));
        vp_arrayAdapter.setDropDownViewResource(R.layout.vp_spinner_list);
        this.vp_questionSpinner.setAdapter(vp_arrayAdapter);
        if (this.vp_type == 1) {
            this.vp_questionSpinner.setSelection(Integer.parseInt(VP_MyApplication.vp_getTrendSecurityQuestion()));
            this.vp_questionSpinner.setEnabled(false);
        }
        vp_saveQuestion.setOnClickListener(view -> {
            String obj = VP_TrendSecurityQuestion.this.vp_securityAnswer.getText().toString();
            if (TextUtils.isEmpty(obj)) {
                Toast.makeText(VP_TrendSecurityQuestion.this, "Enter Answer", Toast.LENGTH_SHORT).show();
                return;
            }
            VP_TrendSecurityQuestion.this.hideSoftKeyboard();
            if (!VP_MyApplication.vp_getTrendSetQuestion()) {
                VP_MyApplication.vp_putTrendSetQuestion(true);
                VP_MyApplication.vp_putTrendSecurityQuestion(String.valueOf(VP_TrendSecurityQuestion.this.vp_questionSpinner.getSelectedItemPosition()));
                VP_MyApplication.vp_putTrendAnswerQuestion(obj);
                VP_TrendSecurityQuestion.this.setPasswordSuccess();
            } else if (VP_MyApplication.vp_getTrendAnswerQuestion().equals(obj)) {
                VP_MyApplication.vp_putTrendSetPass(false);
                VP_MyApplication.vp_putTrendPass("");
                VP_TrendSecurityQuestion.this.setResult(-1, new Intent());
                VP_TrendSecurityQuestion.this.finish();
            } else {
                Toast.makeText(VP_TrendSecurityQuestion.this, "Enter correct security answer!", Toast.LENGTH_SHORT).show();
            }
        });
        vp_backquestion.setOnClickListener(view -> VP_TrendSecurityQuestion.this.onBackPressed());
    }

    private void loadNative() {
        if (vp_gEnabled(this)) {
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
                                }

                                @Override
                                public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    loadQurekaNative(VP_TrendSecurityQuestion.this, vp_nativeContainer);
                                }
                            })
                    .build();
            ig_adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            loadQurekaNative(VP_TrendSecurityQuestion.this, vp_nativeContainer);
        }
    }

    private void loadQurekaNative(Activity context, FrameLayout mAdContainer) {
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

    public void setPasswordSuccess() {
        onBackPressed();
    }

    @SuppressLint("WrongConstant")
    public void hideSoftKeyboard() {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.vp_root.getWindowToken(), 0);
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        if (adView != null) {
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

    @Override
    protected void onResume() {
        super.onResume();
        vp_ShowAppOpen = true;
        if (!vp_AppOpenIntent(this) && vp_TrendChangeAct) {
            vp_TrendChangeAct = false;
            startActivity(vp_intent1);
            finish();
        }
    }
}
