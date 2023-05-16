package com.video.player.videoplayer.xvxvideoplayer.model;

import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;

public class VP_EventBus {
    int vp_type;
    int vp_value;
    MediaData VPMediaData;

    public int getVp_type() {
        return this.vp_type;
    }

    public void setVp_type(int i) {
        this.vp_type = i;
    }

    public int getVp_value() {
        return this.vp_value;
    }

    public void setVp_value(int i) {
        this.vp_value = i;
    }

    public MediaData geMediaData() {
        return this.VPMediaData;
    }

    public void setMediaData(MediaData VPMediaData) {
        this.VPMediaData = VPMediaData;
    }

}
