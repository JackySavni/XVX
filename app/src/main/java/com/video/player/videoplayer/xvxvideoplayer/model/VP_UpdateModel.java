package com.video.player.videoplayer.xvxvideoplayer.model;

public class VP_UpdateModel {
    public String title, descp;

    public VP_UpdateModel(){}

    public VP_UpdateModel(String title, String descp) {
        this.title = title;
        this.descp = descp;
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
}
