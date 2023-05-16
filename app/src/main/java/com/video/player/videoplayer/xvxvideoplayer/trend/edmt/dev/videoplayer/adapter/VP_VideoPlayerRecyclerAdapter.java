package com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.adapter;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_GNative2;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_gEnabled;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_nativeOutSide;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_noOfNative;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.vp_showTrendInterAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.trend.VP_TrendPlayer;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.model.VP_MediaObject;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_ChromeLauncher;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;

import java.util.ArrayList;

public class VP_VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity vp_context;
    private final ArrayList<VP_MediaObject> VPMediaObjects;
    private final RequestManager vp_requestManager;
    private final int VP_ITEM = 1;
    private final int VP_NATIVE = 2;
    private NativeAd vp_mNativeAd;

    public VP_VideoPlayerRecyclerAdapter(Activity vp_context, ArrayList<VP_MediaObject> VPMediaObjects, RequestManager vp_requestManager) {
        this.vp_context = vp_context;
        this.VPMediaObjects = VPMediaObjects;
        this.vp_requestManager = vp_requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vp_view;
        if (i == VP_ITEM) {
            vp_view = LayoutInflater.from(vp_context).inflate(R.layout.vp_layout_video_list_item, viewGroup,
                    false);
            return new ItemHolder(vp_view);
        } else if (i == VP_NATIVE) {
            vp_view = LayoutInflater.from(vp_context).inflate(R.layout.vp_native_container, viewGroup
                    , false);
            return new NativeHolder(vp_view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder.getItemViewType() == VP_ITEM) {
            ((ItemHolder) viewHolder).onBind(vp_context, VPMediaObjects.get(viewHolder.getAbsoluteAdapterPosition()), vp_requestManager);
        } else if (viewHolder.getItemViewType() == VP_NATIVE) {
            NativeHolder vp_nativeHolder = (NativeHolder) viewHolder;
            if (!vp_nativeOutSide((Activity) vp_context) && vp_gEnabled(vp_context) && i < (vp_noOfNative(vp_context) * 6 + 1)) {
                vp_nativeHolder.setIsRecyclable(false);
                AdLoader.Builder builder = new AdLoader.Builder(vp_context, vp_GNative2(vp_context));
                builder.forNativeAd(nativeAd -> {
                    if (vp_mNativeAd != null) {
                        vp_mNativeAd.destroy();
                    }
                    vp_mNativeAd = nativeAd;
                    NativeAdView vp_nativeAdView = (NativeAdView) ((Activity) vp_context).getLayoutInflater().inflate(R.layout.vp_native_adunified, null);
                    vp_populateUnifiedNativeAdView(vp_nativeHolder.vp_mAdContainer, nativeAd, vp_nativeAdView);
                });

                AdLoader vp_adLoader = builder.withAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        vp_nativeHolder.vp_adText.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        vp_nativeHolder.vp_nativeMainContainer.setVisibility(View.GONE);
                        vp_nativeHolder.vp_gifContainer.setVisibility(View.VISIBLE);
                        loadGIFNative(vp_context, vp_nativeHolder.vp_gifContainer, VPMediaObjects.get(viewHolder.getAbsoluteAdapterPosition()).getVp_title(), VPMediaObjects.get(viewHolder.getAbsoluteAdapterPosition()).getVp_gif_url(), VPMediaObjects.get(viewHolder.getAbsoluteAdapterPosition()).getVp_redirect_link());
                    }
                }).build();

                vp_adLoader.loadAd(new AdRequest.Builder().build());
            } else {
                vp_nativeHolder.vp_nativeMainContainer.setVisibility(View.GONE);
                vp_nativeHolder.vp_gifContainer.setVisibility(View.VISIBLE);
                loadGIFNative(vp_context, vp_nativeHolder.vp_gifContainer, VPMediaObjects.get(viewHolder.getAbsoluteAdapterPosition()).getVp_title(), VPMediaObjects.get(viewHolder.getAbsoluteAdapterPosition()).getVp_gif_url(), VPMediaObjects.get(viewHolder.getAbsoluteAdapterPosition()).getVp_redirect_link());
            }
        }
    }

    private void loadGIFNative(Activity context, RelativeLayout mAdContainer, String title, String gifURL, String redirectURL) {
        LayoutInflater vp_inflater = LayoutInflater.from(context);
        View view1;
        view1 = vp_inflater.inflate(R.layout.vp_layout_video_list_item, mAdContainer, false);
        ImageView vp_gifImg = view1.findViewById(R.id.vp_thumbnail);
        TextView vp_titleTV = view1.findViewById(R.id.vp_title);
        vp_titleTV.setText(title);
        Glide.with(context).asGif().load(gifURL).into(vp_gifImg);
        mAdContainer.removeAllViews();
        mAdContainer.addView(view1);
        view1.setOnClickListener(view -> VP_ChromeLauncher.vp_launchGame(context, redirectURL, ""));
    }

    @Override
    public int getItemViewType(int position) {
        if (VPMediaObjects.get(position).isVp_isAd()) {
            return VP_NATIVE;
        } else {
            return VP_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return VPMediaObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ItemHolder extends ViewHolder {
        public FrameLayout vp_media_container;
        public TextView vp_title;
        public ImageView vp_thumbnail;
        public View vp_parent;
        public RequestManager vp_requestManager;

        public ItemHolder(View view) {
            super(view);
            vp_parent = view;
            vp_media_container = itemView.findViewById(R.id.vp_media_container);
            vp_thumbnail = itemView.findViewById(R.id.vp_thumbnail);
            vp_title = itemView.findViewById(R.id.vp_title);
        }

        public void onBind(Context vp_context, VP_MediaObject VPMediaObject, RequestManager requestManager) {
            this.vp_requestManager = requestManager;
            vp_parent.setTag(this);
            vp_title.setText(VPMediaObject.getVp_title());
            this.vp_requestManager
                    .load(VPMediaObject.getVp_thumbnail())
                    .into(vp_thumbnail);
            vp_parent.setOnClickListener(v ->
                    vp_showTrendInterAd(
                            (Activity) vp_context,
                            new Intent(vp_context, VP_TrendPlayer.class)
                                    .putExtra(VP_Constant.VP_EXTRA_TREND_VIDEO_URL, VPMediaObject.getVp_media_url())
                            , false));
        }
    }

    public class NativeHolder extends ViewHolder {
        private final LinearLayout vp_mAdContainer;
        private final CardView vp_nativeMainContainer;
        private final RelativeLayout vp_gifContainer;
        private final TextView vp_adText;

        public NativeHolder(@NonNull View itemView) {
            super(itemView);
            vp_adText = itemView.findViewById(R.id.ad_text);
            vp_mAdContainer = itemView.findViewById(R.id.ad_container);
            vp_nativeMainContainer = itemView.findViewById(R.id.native_main_container);
            vp_gifContainer = itemView.findViewById(R.id.gif_container);
        }
    }

    private void vp_populateUnifiedNativeAdView(LinearLayout vp_nativeContainer, NativeAd vp_nativeAd, NativeAdView vp_adView) {
        if (vp_adView != null) {
            vp_nativeContainer.removeAllViews();
            vp_nativeContainer.addView(vp_adView);
        }
        vp_adView.setMediaView((MediaView) vp_adView.findViewById(R.id.ad_media));
        vp_adView.setHeadlineView(vp_adView.findViewById(R.id.ad_headline));
        vp_adView.setBodyView(vp_adView.findViewById(R.id.ad_body));
        vp_adView.setCallToActionView(vp_adView.findViewById(R.id.adcalltoaction));
        vp_adView.setIconView(vp_adView.findViewById(R.id.ad_app_icon));
        vp_adView.setStarRatingView(vp_adView.findViewById(R.id.ad_stars));
        vp_adView.setAdvertiserView(vp_adView.findViewById(R.id.ad_advertiser));

        ((TextView) vp_adView.getHeadlineView()).setText(vp_nativeAd.getHeadline());
        vp_adView.getMediaView().setMediaContent(vp_nativeAd.getMediaContent());

        if (vp_nativeAd.getBody() == null) {
            vp_adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            vp_adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) vp_adView.getBodyView()).setText(vp_nativeAd.getBody());
        }

        if (vp_nativeAd.getCallToAction() == null) {
            vp_adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            vp_adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) vp_adView.getCallToActionView()).setText(vp_nativeAd.getCallToAction());
        }

        if (vp_nativeAd.getIcon() == null) {
            vp_adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) vp_adView.getIconView()).setImageDrawable(
                    vp_nativeAd.getIcon().getDrawable());
            vp_adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (vp_nativeAd.getStarRating() == null) {
            vp_adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) vp_adView.getStarRatingView())
                    .setRating(vp_nativeAd.getStarRating().floatValue());
            vp_adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (vp_nativeAd.getAdvertiser() == null) {
            vp_adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) vp_adView.getAdvertiserView()).setText(vp_nativeAd.getAdvertiser());
            vp_adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        vp_adView.setNativeAd(vp_nativeAd);
    }

}

