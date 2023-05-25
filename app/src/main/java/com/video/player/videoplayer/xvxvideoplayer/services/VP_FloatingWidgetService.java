package com.video.player.videoplayer.xvxvideoplayer.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.video.player.videoplayer.xvxvideoplayer.vid.VP_Player;

import java.util.ArrayList;

public class VP_FloatingWidgetService extends Service {
    View vp_floatingView;
    ImageView vp_closeWindow;
    ImageView vp_next;
    ImageView vp_floatingWindow;
    ImageView vp_playPause;
    WindowManager.LayoutParams vp_params;
    MediaData vp_video;
    ImageView vp_previous;
    public int vp_videoPosition;
    public ArrayList<MediaData> vp_videoList;
    WindowManager vp_windowManager;
    VideoView vp_videoView;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        this.vp_videoPosition = intent.getIntExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, 0);
        return super.onStartCommand(intent, i, i2);
    }

    public void onCreate() {
        super.onCreate();
        this.vp_floatingView = LayoutInflater.from(this).inflate(R.layout.vp_floating_widget_layout, (ViewGroup) null);
        this.vp_videoList = VP_MyApplication.vp_getVideoList();
        this.vp_video = VP_MyApplication.vp_getLastPlayVideos();
        WindowManager.LayoutParams vp_layoutParams = new WindowManager.LayoutParams(-2, -2, Build.VERSION.SDK_INT >= 26 ? 2038 : 2002, 8, -3);
        this.vp_params = vp_layoutParams;
        vp_layoutParams.gravity = 51;
        this.vp_params.x = 0;
        this.vp_params.y = 100;
        @SuppressLint("WrongConstant")
        WindowManager vp_windowManager2 = (WindowManager) getSystemService("window");
        this.vp_windowManager = vp_windowManager2;
        vp_windowManager2.addView(this.vp_floatingView, this.vp_params);
        this.vp_playPause = (ImageView) this.vp_floatingView.findViewById(R.id.vp_playPause);
        this.vp_closeWindow = (ImageView) this.vp_floatingView.findViewById(R.id.vp_closeWindow);
        this.vp_floatingWindow = (ImageView) this.vp_floatingView.findViewById(R.id.vp_floatingWindow);
        this.vp_next = (ImageView) this.vp_floatingView.findViewById(R.id.vp_next);
        this.vp_previous = (ImageView) this.vp_floatingView.findViewById(R.id.vp_previous);
        VideoView videoView2 = (VideoView) this.vp_floatingView.findViewById(R.id.vp_videoView);
        this.vp_videoView = videoView2;
        MediaData vp_media_Data = this.vp_video;
        if (vp_media_Data != null) {
            videoView2.setVideoPath(vp_media_Data.path);
            this.vp_videoView.seekTo(this.vp_video.videoLastPlayPosition);
            this.vp_videoView.start();
        }
        this.vp_videoView.setOnCompletionListener(mediaPlayer -> {
            if (VP_FloatingWidgetService.this.vp_videoPosition < VP_FloatingWidgetService.this.vp_videoList.size() - 1) {
                VP_FloatingWidgetService.this.vp_videoPosition++;
                VP_FloatingWidgetService.this.vp_videoView.setVideoPath(VP_FloatingWidgetService.this.vp_videoList.get(VP_FloatingWidgetService.this.vp_videoPosition).path);
                VP_FloatingWidgetService.this.vp_videoView.start();
                return;
            }
            VP_FloatingWidgetService.this.vp_videoView.pause();
            VP_FloatingWidgetService.this.vp_playPause.setImageResource(R.drawable.vp_hplib_ic_play_download);
        });
        this.vp_next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (VP_FloatingWidgetService.this.vp_videoPosition < VP_FloatingWidgetService.this.vp_videoList.size() - 1) {
                    VP_FloatingWidgetService.this.vp_videoPosition++;
                    VP_FloatingWidgetService.this.vp_videoView.setVideoPath(VP_FloatingWidgetService.this.vp_videoList.get(VP_FloatingWidgetService.this.vp_videoPosition).path);
                    VP_FloatingWidgetService.this.vp_videoView.start();
                }
            }
        });
        this.vp_previous.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (VP_FloatingWidgetService.this.vp_videoPosition > 0) {
                    VP_FloatingWidgetService VPFloatingWidgetService = VP_FloatingWidgetService.this;
                    VPFloatingWidgetService.vp_videoPosition--;
                    VP_FloatingWidgetService.this.vp_videoView.setVideoPath(VP_FloatingWidgetService.this.vp_videoList.get(VP_FloatingWidgetService.this.vp_videoPosition).path);
                    VP_FloatingWidgetService.this.vp_videoView.start();
                }
            }
        });
        this.vp_closeWindow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                VP_MyApplication.vp_setIsFloatingVideo(false);
                VP_FloatingWidgetService.this.stopSelf();
            }
        });
        this.vp_playPause.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (VP_FloatingWidgetService.this.vp_videoView.isPlaying()) {
                    VP_FloatingWidgetService.this.vp_videoView.pause();
                    VP_FloatingWidgetService.this.vp_playPause.setImageResource(R.drawable.vp_hplib_ic_play_download);
                    return;
                }
                VP_FloatingWidgetService.this.vp_videoView.start();
                VP_FloatingWidgetService.this.vp_playPause.setImageResource(R.drawable.vp_hplib_ic_pause);
            }
        });
        this.vp_floatingWindow.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                VP_MyApplication.vp_setIsFloatingVideo(false);
                VP_FloatingWidgetService VPFloatingWidgetService = VP_FloatingWidgetService.this;
                VPFloatingWidgetService.startActivity(VP_Player.vp_getInstance(VPFloatingWidgetService.getApplicationContext(),
                        VP_FloatingWidgetService.this.vp_videoView.getCurrentPosition(), true).setFlags(268435456));
                VP_FloatingWidgetService.this.stopSelf();
            }
        });
        this.vp_floatingView.findViewById(R.id.vp_mainParentRelativeLayout).setOnTouchListener(new View.OnTouchListener() {
            float vp_mTouchX;
            float vp_mTouchY;
            int vp_mXAxis;
            int vp_mYAxis;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                int vp_action = motionEvent.getAction();
                if (vp_action != 0) {
                    if (vp_action != 1) {
                        if (vp_action != 2) {
                            return false;
                        }
                        VP_FloatingWidgetService.this.vp_params.x = this.vp_mXAxis + ((int) (motionEvent.getRawX() - this.vp_mTouchX));
                        VP_FloatingWidgetService.this.vp_params.y = this.vp_mYAxis + ((int) (motionEvent.getRawY() - this.vp_mTouchY));
                        VP_FloatingWidgetService.this.vp_windowManager.updateViewLayout(VP_FloatingWidgetService.this.vp_floatingView, VP_FloatingWidgetService.this.vp_params);
                    }
                    return true;
                }
                this.vp_mXAxis = VP_FloatingWidgetService.this.vp_params.x;
                this.vp_mYAxis = VP_FloatingWidgetService.this.vp_params.y;
                this.vp_mTouchX = motionEvent.getRawX();
                this.vp_mTouchY = motionEvent.getRawY();
                return true;
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        View view = this.vp_floatingView;
        if (view != null) {
            this.vp_windowManager.removeView(view);
        }
    }
}
