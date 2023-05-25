package com.video.player.videoplayer.xvxvideoplayer.gallery.model;

public class VP_MigrateModel {
    public String title, descp, app_title, app_thumb, app_url;

    public VP_MigrateModel() {
    }

    public VP_MigrateModel(String title, String descp, String app_title, String app_thumb, String app_url) {
        this.title = title;
        this.descp = descp;
        this.app_title = app_title;
        this.app_thumb = app_thumb;
        this.app_url = app_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public String getApp_title() {
        return app_title;
    }

    public void setApp_title(String app_title) {
        this.app_title = app_title;
    }

    public String getApp_thumb() {
        return app_thumb;
    }

    public void setApp_thumb(String app_thumb) {
        this.app_thumb = app_thumb;
    }

    public String getApp_url() {
        return app_url;
    }

    public void setApp_url(String app_url) {
        this.app_url = app_url;
    }
}
