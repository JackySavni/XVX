package com.video.player.videoplayer.xvxvideoplayer.extra;

import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Surface;

import java.util.Arrays;
import java.util.Map;

public class VPMediaSystem extends VP_MediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {
    public MediaPlayer vp_mediaPlayer;

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public VPMediaSystem(VP_Jzvd vd) {
        super(vd);
    }

    @Override
    public void prepare() {
        release();
        this.vp_mMediaHandlerThread = new HandlerThread("JZVD");
        this.vp_mMediaHandlerThread.start();
        this.vp_mMediaHandler = new Handler(this.vp_mMediaHandlerThread.getLooper());
        this.vp_handler = new Handler();
        this.vp_mMediaHandler.post(VPMediaSystem.this::prepareMediaSystem);
    }

    public void prepareMediaSystem() {
        try {
            MediaPlayer vp_mediaPlayer2 = new MediaPlayer();
            this.vp_mediaPlayer = vp_mediaPlayer2;
            vp_mediaPlayer2.setAudioStreamType(3);
            this.vp_mediaPlayer.setLooping(this.vp_vd.VPDataSource.vp_looping);
            this.vp_mediaPlayer.setOnPreparedListener(this);
            this.vp_mediaPlayer.setOnCompletionListener(this);
            this.vp_mediaPlayer.setOnBufferingUpdateListener(this);
            this.vp_mediaPlayer.setScreenOnWhilePlaying(true);
            this.vp_mediaPlayer.setOnSeekCompleteListener(this);
            this.vp_mediaPlayer.setOnErrorListener(this);
            this.vp_mediaPlayer.setOnInfoListener(this);
            this.vp_mediaPlayer.setOnVideoSizeChangedListener(this);
            MediaPlayer.class.getDeclaredMethod("setDataSource", String.class, Map.class).invoke(this.vp_mediaPlayer, this.vp_vd.VPDataSource.getCurrentUrl().toString(), this.vp_vd.VPDataSource.vp_headerMap);
            this.vp_mediaPlayer.prepareAsync();
            this.vp_mediaPlayer.setSurface(new Surface(VP_SAVED_SURFACE));
            Log.e("mediaPlayer", "======" + this.vp_mediaPlayer.getTrackInfo().length);
            Log.e("mediaPlayer", "======" + Arrays.toString(this.vp_mediaPlayer.getTrackInfo()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if (this.vp_mMediaHandler != null) {
            this.vp_mMediaHandler.post(VPMediaSystem.this::startMediaSystem);
        }
    }

    public void startMediaSystem() {
        this.vp_mediaPlayer.start();
    }

    public void pauseMediaSystem() {
        this.vp_mediaPlayer.pause();
    }

    @Override
    public void pause() {
        try {
            this.vp_mMediaHandler.post(VPMediaSystem.this::pauseMediaSystem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPlaying() {
        return this.vp_mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(final long j) {
        this.vp_mMediaHandler.post(() -> VPMediaSystem.this.seekToMediaSystem(j));
    }

    public void seekToMediaSystem(long j) {
        try {
            this.vp_mediaPlayer.seekTo((int) j);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (this.vp_mMediaHandler != null && this.vp_mMediaHandlerThread != null && this.vp_mediaPlayer != null) {
            final HandlerThread vp_handlerThread = this.vp_mMediaHandlerThread;
            final MediaPlayer vp_mediaPlayer2 = this.vp_mediaPlayer;
            VP_SAVED_SURFACE = null;
            this.vp_mMediaHandler.post(() -> VPMediaSystem.release(vp_mediaPlayer2, vp_handlerThread));
            this.vp_mediaPlayer = null;
        }
    }

    static void release(MediaPlayer mediaPlayer2, HandlerThread handlerThread) {
        mediaPlayer2.setSurface(null);
        mediaPlayer2.release();
        handlerThread.quit();
    }

    @Override
    public long getCurrentPosition() {
        MediaPlayer vp_mediaPlayer2 = this.vp_mediaPlayer;
        if (vp_mediaPlayer2 != null) {
            return (long) vp_mediaPlayer2.getCurrentPosition();
        }
        return 0;
    }

    @Override
    public long getDuration() {
        MediaPlayer vp_mediaPlayer2 = this.vp_mediaPlayer;
        if (vp_mediaPlayer2 != null) {
            return (long) vp_mediaPlayer2.getDuration();
        }
        return 0;
    }

    @Override
    public void setVolume(final float f, final float f2) {
        if (this.vp_mMediaHandler != null) {
            this.vp_mMediaHandler.post(() -> VPMediaSystem.this.setVolumeMediaSystem(f, f2));
        }
    }

    public void setVolumeMediaSystem(float f, float f2) {
        MediaPlayer vp_mediaPlayer2 = this.vp_mediaPlayer;
        if (vp_mediaPlayer2 != null) {
            vp_mediaPlayer2.setVolume(f, f2);
        }
    }

    @Override
    public void setSpeed(float f) {
        if (Build.VERSION.SDK_INT >= 23) {
            PlaybackParams vp_playbackParams = this.vp_mediaPlayer.getPlaybackParams();
            vp_playbackParams.setSpeed(f);
            this.vp_mediaPlayer.setPlaybackParams(vp_playbackParams);
        }
    }

    public void onPreparedMediaSystem() {
        this.vp_vd.onPrepared();
    }

    public void onPrepared(MediaPlayer mediaPlayer2) {
        this.vp_handler.post(VPMediaSystem.this::onPreparedMediaSystem);
    }

    public void onCompletionMediaSystem() {
        this.vp_vd.onCompletion();
    }

    public void onCompletion(MediaPlayer mediaPlayer2) {
        this.vp_handler.post(VPMediaSystem.this::onCompletionMediaSystem);
    }

    public void onBufferingUpdateMediaSystem(int i) {
        this.vp_vd.setBufferProgress(i);
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer2, final int i) {
        this.vp_handler.post(() -> VPMediaSystem.this.onBufferingUpdateMediaSystem(i));
    }

    public void onSeekCompleteMediaSystem() {
        this.vp_vd.onSeekComplete();
    }

    public void onSeekComplete(MediaPlayer mediaPlayer2) {
        this.vp_handler.post(VPMediaSystem.this::onSeekCompleteMediaSystem);
    }

    public void onErrorMediaSystem(int i, int i2) {
        this.vp_vd.onError(i, i2);
    }

    public boolean onError(MediaPlayer mediaPlayer2, final int i, final int i2) {
        this.vp_handler.post(() -> VPMediaSystem.this.onErrorMediaSystem(i, i2));
        return true;
    }

    public void onInfoMediaSystem(int i, int i2) {
        this.vp_vd.onInfo(i, i2);
    }

    public boolean onInfo(MediaPlayer mediaPlayer2, final int i, final int i2) {
        this.vp_handler.post(() -> VPMediaSystem.this.onInfoMediaSystem(i, i2));
        return false;
    }

    public void onVideoSizeChangedMediaSystem(int i, int i2) {
        this.vp_vd.onVideoSizeChanged(i, i2);
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer2, final int width, final int height) {
        this.vp_handler.post(() -> VPMediaSystem.this.onVideoSizeChangedMediaSystem(width, height));
    }

    @Override
    public void setSurface(Surface surface) {
        this.vp_mediaPlayer.setSurface(surface);
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        if (VP_SAVED_SURFACE == null) {
            VP_SAVED_SURFACE = surfaceTexture;
            prepare();
            return;
        }
        this.vp_vd.vp_textureView.setSurfaceTexture(VP_SAVED_SURFACE);
    }
}
