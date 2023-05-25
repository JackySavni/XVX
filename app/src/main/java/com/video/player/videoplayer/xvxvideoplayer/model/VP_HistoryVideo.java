package com.video.player.videoplayer.xvxvideoplayer.model;

import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;

import java.util.ArrayList;

public class VP_HistoryVideo {
    ArrayList<MediaData> vp_videoList;

    public VP_HistoryVideo(ArrayList<MediaData> arrayList) {
        this.vp_videoList = arrayList;
    }

    public ArrayList<MediaData> getVp_videoList() {
        return this.vp_videoList;
    }

    public void setVp_videoList(ArrayList<MediaData> arrayList) {
        this.vp_videoList = arrayList;
    }
}
