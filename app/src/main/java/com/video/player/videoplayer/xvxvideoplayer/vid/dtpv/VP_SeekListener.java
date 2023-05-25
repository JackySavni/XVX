package com.video.player.videoplayer.xvxvideoplayer.vid.dtpv;

public interface VP_SeekListener {
    /**
     * Called when video start reached during rewinding
     */
    void vp_onVideoStartReached();

    /**
     * Called when video end reached during forwarding
     */
    void vp_onVideoEndReached();
}
