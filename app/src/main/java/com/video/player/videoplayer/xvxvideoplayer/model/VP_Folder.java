package com.video.player.videoplayer.xvxvideoplayer.model;


import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;

import java.io.Serializable;
import java.util.ArrayList;

public class VP_Folder implements Serializable {
    ArrayList<MediaData> _mediadata;
    String vp_name;

    public VP_Folder() {
    }

    public String getVp_name() {
        return this.vp_name;
    }

    public void setVp_name(String str) {
        this.vp_name = str;
    }

    public ArrayList<MediaData> getMedia_data() {
        return this._mediadata;
    }
    public void setMedia_data(ArrayList<MediaData> arrayList) {
        this._mediadata = arrayList;
    }
}
