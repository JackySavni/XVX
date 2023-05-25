package com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.model;

public class VP_MediaObject {
    private String vp_title, vp_thumbnail, vp_media_url, vp_preview, vp_gif_url, vp_redirect_link;
    private boolean vp_isAd;
    public VP_MediaObject(String vp_title, String vp_thumbnail, String vp_preview, String vp_media_url, boolean vp_isAd) {
        this.vp_title = vp_title;
        this.vp_thumbnail = vp_thumbnail;
        this.vp_preview = vp_preview;
        this.vp_media_url = vp_media_url;
        this.vp_isAd = vp_isAd;
    }

    public VP_MediaObject(String vp_title, String vp_gif_url, String vp_redirect_link, boolean vp_isAd) {
        this.vp_title = vp_title;
        this.vp_gif_url = vp_gif_url;
        this.vp_redirect_link = vp_redirect_link;
        this.vp_isAd = vp_isAd;
    }

    public VP_MediaObject() {
    }

    public String getVp_title() {
        return vp_title;
    }

    public void setVp_title(String vp_title) {
        this.vp_title = vp_title;
    }
    public String getVp_thumbnail() {
        return vp_thumbnail;
    }

    public void setVp_thumbnail(String vp_thumbnail) {
        this.vp_thumbnail = vp_thumbnail;
    }

    public String getVp_preview() {
        return vp_preview;
    }

    public void setVp_preview(String vp_preview) {
        this.vp_preview = vp_preview;
    }

    public String getVp_media_url() {
        return vp_media_url;
    }

    public void setVp_media_url(String vp_media_url) {
        this.vp_media_url = vp_media_url;
    }

    public String getVp_gif_url() {
        return vp_gif_url;
    }

    public void setVp_gif_url(String vp_gif_url) {
        this.vp_gif_url = vp_gif_url;
    }

    public String getVp_redirect_link() {
        return vp_redirect_link;
    }

    public void setVp_redirect_link(String vp_redirect_link) {
        this.vp_redirect_link = vp_redirect_link;
    }

    public boolean isVp_isAd() {
        return vp_isAd;
    }

    public void setVp_isAd(boolean vp_isAd) {
        this.vp_isAd = vp_isAd;
    }
}
