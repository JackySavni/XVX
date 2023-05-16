package com.video.player.videoplayer.xvxvideoplayer.extra;

import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;
import android.view.TextureView;

public abstract class VP_MediaInterface implements TextureView.SurfaceTextureListener {
    public static SurfaceTexture VP_SAVED_SURFACE;
    public Handler vp_handler;
    public Handler vp_mMediaHandler;
    public HandlerThread vp_mMediaHandlerThread;
    public VP_Jzvd vp_vd;

    public abstract long getCurrentPosition();

    public abstract long getDuration();

    public abstract boolean isPlaying();

    public abstract void pause();

    public abstract void prepare();

    public abstract void release();

    public abstract void seekTo(long j);

    public abstract void setSpeed(float f);

    public abstract void setSurface(Surface surface);

    public abstract void setVolume(float f, float f2);

    public abstract void start();

    public VP_MediaInterface(VP_Jzvd vd2) {
        this.vp_vd = vd2;
    }
}
