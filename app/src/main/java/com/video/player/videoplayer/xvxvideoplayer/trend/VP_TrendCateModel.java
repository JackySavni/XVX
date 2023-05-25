package com.video.player.videoplayer.xvxvideoplayer.trend;

import com.google.gson.annotations.SerializedName;

public class VP_TrendCateModel {
    @SerializedName("cat_name")
    public String vp_cateName;
    @SerializedName("image_url")
    public String vp_catThumb;
    @SerializedName("ad")
    public boolean vp_qurekaAd;
    public boolean vp_isAd;

    public VP_TrendCateModel(String vp_cateName, String vp_catThumb) {
        this.vp_cateName = vp_cateName;
        this.vp_catThumb = vp_catThumb;
    }

    public VP_TrendCateModel(String vp_cateName, String vp_catThumb, boolean vp_qurekaAd) {
        this.vp_cateName = vp_cateName;
        this.vp_catThumb = vp_catThumb;
        this.vp_qurekaAd = vp_qurekaAd;
    }
    public VP_TrendCateModel(String vp_cateName, String vp_catThumb, boolean vp_qurekaAd, boolean vp_isAd) {
        this.vp_cateName = vp_cateName;
        this.vp_catThumb = vp_catThumb;
        this.vp_qurekaAd = vp_qurekaAd;
        this.vp_isAd = vp_isAd;
    }

    public String getVp_cateName() {
        return vp_cateName;
    }

    public void setVp_cateName(String vp_cateName) {
        this.vp_cateName = vp_cateName;
    }

    public String getVp_catThumb() {
        return vp_catThumb;
    }

    public void setVp_catThumb(String vp_catThumb) {
        this.vp_catThumb = vp_catThumb;
    }

    public boolean getVp_qurekaAd() {
        return vp_qurekaAd;
    }

    public void setVp_qurekaAd(boolean vp_qurekaAd) {
        this.vp_qurekaAd = vp_qurekaAd;
    }

    public boolean isVp_isAd() {
        return vp_isAd;
    }

    public void setVp_isAd(boolean vp_isAd) {
        this.vp_isAd = vp_isAd;
    }
}
