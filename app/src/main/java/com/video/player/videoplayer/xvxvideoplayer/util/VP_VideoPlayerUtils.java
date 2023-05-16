package com.video.player.videoplayer.xvxvideoplayer.util;

import android.content.Context;

public class VP_VideoPlayerUtils {
    public static final String vp_makeShortTimeString(Context context, long j) {
        int vp_i = (int) j;
        int vp_i2 = vp_i % 60;
        int vp_i3 = (vp_i / 60) % 60;
        int vp_i4 = vp_i / 3600;
        if (vp_i4 > 0) {
            return String.format("%02d:%02d:%02d", Integer.valueOf(vp_i4), Integer.valueOf(vp_i3), Integer.valueOf(vp_i2));
        }
        return String.format("%02d:%02d", Integer.valueOf(vp_i3), Integer.valueOf(vp_i2));
    }
}
