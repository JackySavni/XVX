package com.video.player.videoplayer.xvxvideoplayer.adapters;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.showInterAd2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_1FBEnable;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_FBNative1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_FBNativeBanner1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GInter2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GNative1;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_QurekaID;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_isValidContext;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showInterAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdViewAttributes;
import com.facebook.ads.NativeBannerAd;
import com.facebook.ads.NativeBannerAdView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.activities.VP_Videolist;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_Folder;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;

import java.util.ArrayList;

public class VP_FolderAdapter extends RecyclerView.Adapter<VP_FolderAdapter.ViewHolder> {
    LayoutInflater vp_inflater;
    FragmentActivity vp_activity;
    int i;
    ArrayList<VP_Folder> VPFolders;
    private final int VP_ITEM = 123;
    private final int VP_NATIVE = 456;
    private NativeAd vp_mNativeAd;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public VP_FolderAdapter(FragmentActivity fragmentActivity, ArrayList<VP_Folder> arrayList, int i2) {
        this.vp_activity = fragmentActivity;
        this.VPFolders = arrayList;
        this.i = i2;
        this.vp_inflater = LayoutInflater.from(fragmentActivity);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == VP_ITEM) {
            if (this.i == 0) {
                view = this.vp_inflater.inflate(R.layout.vp_item_folder, viewGroup, false);
            } else {
                view = this.vp_inflater.inflate(R.layout.vp_item_folder_grid, viewGroup, false);
            }
            return new ItemHolder(view);
        } else if (viewType == VP_NATIVE) {
            view = this.vp_inflater.inflate(R.layout.vp_native_container_small, viewGroup, false);
            return new NativeHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (VPFolders.get(position) != null) {
            return VP_ITEM;
        } else {
            return VP_NATIVE;
        }
    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(final ViewHolder viewHolder, int i2) {
        if (viewHolder.getItemViewType() == VP_ITEM) {
            ItemHolder vp_itemHolder = (ItemHolder) viewHolder;
            vp_itemHolder.vp_folderName.setText(this.VPFolders.get(viewHolder.getAbsoluteAdapterPosition()).getVp_name());
            TextView vp_textView = vp_itemHolder.vp_videoCount;
            vp_textView.setText("(" + this.VPFolders.get(viewHolder.getAbsoluteAdapterPosition()).getMedia_data().size() + ")");
            vp_itemHolder.itemView.setOnClickListener(view ->
                    {
                        if (vp_GInter1(vp_activity).equals(vp_GInter2(vp_activity))) {
                            showInterAd2(vp_activity, new Intent(vp_activity, VP_Videolist.class).putExtra("data", VPFolders.get(viewHolder.getAbsoluteAdapterPosition())));
                        } else {
                            vp_showInterAd(vp_activity, new Intent(vp_activity, VP_Videolist.class).putExtra("data", VPFolders.get(viewHolder.getAbsoluteAdapterPosition())));
                        }
                    }
            );
        } else {
            NativeHolder nativeHolder = (NativeHolder) viewHolder;

            if (vp_gEnabled(vp_activity) && vp_1FBEnable(vp_activity)) {
                AdLoader.Builder builder = new AdLoader.Builder(vp_activity, vp_GNative1(vp_activity));
                builder.forNativeAd(nativeAd -> {
                    if (vp_mNativeAd != null) {
                        vp_mNativeAd.destroy();
                    }
                    vp_mNativeAd = nativeAd;
                    @SuppressLint("InflateParams") NativeAdView nativeAdView = (NativeAdView) vp_activity.getLayoutInflater().inflate(R.layout.vp_native_adunified_small, null);
                    populateNativeAdViewSmall(nativeHolder.vp_mAdContainer, nativeAd, nativeAdView);
                });

                AdLoader adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        nativeHolder.vp_adText.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        nativeHolder.vp_adText.setVisibility(View.GONE);

                        if (vp_1FBEnable(vp_activity)) {
                            NativeBannerAd nativeBannerAd = new NativeBannerAd(vp_activity, vp_FBNativeBanner1(vp_activity));
                            NativeAdListener nativeAdListener = new NativeAdListener() {
                                @Override
                                public void onMediaDownloaded(Ad ad) {
                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                    nativeHolder.vp_adText.setVisibility(View.GONE);
                                    if (vp_isValidContext(vp_activity)) {
                                        loadQurNative(vp_activity, nativeHolder.vp_mAdContainer);
                                    }
                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    nativeHolder.vp_adText.setVisibility(View.GONE);
                                    if (nativeBannerAd != ad) {
                                        return;
                                    }

                                    if (nativeHolder.vp_mAdContainer == null) {
                                        return;
                                    }

                                    NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes(vp_activity)
                                            .setBackgroundColor(ContextCompat.getColor(vp_activity, R.color.tab_bg))
                                            .setTitleTextColor(Color.WHITE)
                                            .setDescriptionTextColor(Color.LTGRAY)
                                            .setButtonColor(Color.WHITE)
                                            .setButtonTextColor(Color.BLACK);

                                    View adView = NativeBannerAdView.render(vp_activity, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_120, viewAttributes);
                                    nativeHolder.vp_mAdContainer.removeAllViews();
                                    nativeHolder.vp_mAdContainer.addView(adView);
                                }

                                @Override
                                public void onAdClicked(Ad ad) {
                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {
                                }
                            };
                            nativeBannerAd.loadAd(nativeBannerAd.buildLoadAdConfig()
                                    .withAdListener(nativeAdListener)
                                    .build());
                        } else {
                            nativeHolder.vp_adText.setVisibility(View.GONE);
                            loadQurNative(vp_activity, nativeHolder.vp_mAdContainer);
                        }
                    }
                }).build();

                adLoader.loadAd(new AdRequest.Builder().build());
            } else if (vp_gEnabled(vp_activity) && !vp_1FBEnable(vp_activity)) {
                if (vp_1FBEnable(vp_activity)) {
                    NativeBannerAd nativeBannerAd = new NativeBannerAd(vp_activity, vp_FBNativeBanner1(vp_activity));
                    NativeAdListener nativeAdListener = new NativeAdListener() {
                        @Override
                        public void onMediaDownloaded(Ad ad) {
                        }

                        @Override
                        public void onError(Ad ad, AdError adError) {
                            nativeHolder.vp_adText.setVisibility(View.GONE);
                            if (vp_isValidContext(vp_activity)) {
                                loadQurNative(vp_activity, nativeHolder.vp_mAdContainer);
                            }
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            nativeHolder.vp_adText.setVisibility(View.GONE);
                            if (nativeBannerAd != ad) {
                                return;
                            }

                            if (nativeHolder.vp_mAdContainer == null) {
                                return;
                            }

                            NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes(vp_activity)
                                    .setBackgroundColor(ContextCompat.getColor(vp_activity, R.color.tab_bg))
                                    .setTitleTextColor(Color.WHITE)
                                    .setDescriptionTextColor(Color.LTGRAY)
                                    .setButtonColor(Color.WHITE)
                                    .setButtonTextColor(Color.BLACK);

                            View adView = NativeBannerAdView.render(vp_activity, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_120, viewAttributes);
                            nativeHolder.vp_mAdContainer.removeAllViews();
                            nativeHolder.vp_mAdContainer.addView(adView);
                        }

                        @Override
                        public void onAdClicked(Ad ad) {
                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {
                        }
                    };
                    nativeBannerAd.loadAd(nativeBannerAd.buildLoadAdConfig()
                            .withAdListener(nativeAdListener)
                            .build());
                } else {
                    nativeHolder.vp_adText.setVisibility(View.GONE);
                    loadQurNative(vp_activity, nativeHolder.vp_mAdContainer);
                }
            } else if (!vp_gEnabled(vp_activity) && vp_1FBEnable(vp_activity)) {
                NativeBannerAd nativeBannerAd = new NativeBannerAd(vp_activity, vp_FBNativeBanner1(vp_activity));
                NativeAdListener nativeAdListener = new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        nativeHolder.vp_adText.setVisibility(View.GONE);
                        if (vp_isValidContext(vp_activity)) {
                            loadQurNative(vp_activity, nativeHolder.vp_mAdContainer);
                        }
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        nativeHolder.vp_adText.setVisibility(View.GONE);
                        if (nativeBannerAd != ad) {
                            return;
                        }

                        if (nativeHolder.vp_mAdContainer == null) {
                            return;
                        }

                        NativeAdViewAttributes viewAttributes = new NativeAdViewAttributes(vp_activity)
                                .setBackgroundColor(ContextCompat.getColor(vp_activity, R.color.tab_bg))
                                .setTitleTextColor(Color.WHITE)
                                .setDescriptionTextColor(Color.LTGRAY)
                                .setButtonColor(Color.WHITE)
                                .setButtonTextColor(Color.BLACK);

                        View adView = NativeBannerAdView.render(vp_activity, nativeBannerAd, NativeBannerAdView.Type.HEIGHT_120, viewAttributes);
                        nativeHolder.vp_mAdContainer.removeAllViews();
                        nativeHolder.vp_mAdContainer.addView(adView);
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                    }
                };
                nativeBannerAd.loadAd(nativeBannerAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
            } else {
                nativeHolder.vp_adText.setVisibility(View.GONE);
                loadQurNative(vp_activity, nativeHolder.vp_mAdContainer);
            }
        }
    }

    private void loadQurNative(Activity context, FrameLayout mAdContainer) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.vp_row_qureka_native_ad_small, mAdContainer, false);
        mAdContainer.removeAllViews();
        mAdContainer.addView(view);
        mAdContainer.setOnClickListener(view1 ->
                VP_ChromeLauncher.vp_launchGame(context, vp_QurekaID(context), ""));
    }

    public class ItemHolder extends ViewHolder {
        TextView vp_folderName;
        TextView vp_videoCount;

        public ItemHolder(View view) {
            super(view);
            this.vp_folderName = view.findViewById(R.id.vp_folder_name);
            this.vp_folderName.setSelected(true);
            this.vp_videoCount = view.findViewById(R.id.vp_video_count);
        }
    }

    public class NativeHolder extends ViewHolder {
        private final FrameLayout vp_mAdContainer;
        private final TextView vp_adText;

        public NativeHolder(@NonNull View itemView) {
            super(itemView);
            vp_adText = itemView.findViewById(R.id.ad_text);
            vp_mAdContainer = itemView.findViewById(R.id.ad_container);
        }
    }

    private void populateNativeAdViewSmall(FrameLayout nativeContainer, NativeAd nativeAd, NativeAdView adView) {
        if (adView != null) {
            nativeContainer.removeAllViews();
            nativeContainer.addView(adView);
        }
        adView.setHeadlineView(adView.findViewById(R.id.ads_title));
        adView.setBodyView(adView.findViewById(R.id.ads_body));
        adView.setCallToActionView(adView.findViewById(R.id.callaction_button));
        adView.setIconView(adView.findViewById(R.id.ads_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ads_star));
        adView.setAdvertiserView(adView.findViewById(R.id.ads_text));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

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
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
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
    public int getItemCount() {
        return this.VPFolders.size();
    }
}
