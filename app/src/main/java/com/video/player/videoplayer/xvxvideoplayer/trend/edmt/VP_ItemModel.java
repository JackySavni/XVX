package com.video.player.videoplayer.xvxvideoplayer.trend.edmt;

public class VP_ItemModel {
    public String title, thumbnail, preview, video;

    public VP_ItemModel(String title, String thumbnail, String preview, String video) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.preview = preview;
        this.video = video;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
