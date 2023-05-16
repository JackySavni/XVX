package com.video.player.videoplayer.xvxvideoplayer.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.extra.VPVideoStandard;
import com.video.player.videoplayer.xvxvideoplayer.extra.VP_Jzvd;
import com.video.player.videoplayer.xvxvideoplayer.services.VP_FloatingWidgetService;
import com.video.player.videoplayer.xvxvideoplayer.services.VP_VideoPlayAsAudioService;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class VP_VideoPlayer extends AppCompatActivity {
    boolean vp_isBackgroundEnable = false;
    boolean vp_isAutoPlay = true;
    boolean vp_isFloatingVideo = false;
    boolean vp_isContinueWatching = false;
    boolean vp_isService = false;
    boolean vp_isResumeVideo = false;
    int vp_videoLastProgress;
    boolean vp_isShuffleClick = false;
    public VPVideoStandard vp_videoPlayer;
    HashMap<String, Integer> vp_videoPlayLastPositionList = new HashMap<>();
    ArrayList<MediaData> vp_videosList = new ArrayList<>();
    int vp_videoPosition;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            getWindow().setFlags(1024, 1024);
        }
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        supportRequestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.vp_activity_video_player);
        vp_videoPlayer = findViewById(R.id.vp_videoPlayer);
        startService(new Intent(this, VP_VideoPlayAsAudioService.class).setAction(VP_VideoPlayAsAudioService.VP_NOTIFICATION_CLICK_ACTION));
        if (getIntent() != null) {
            vp_videosList = (ArrayList<MediaData>) getIntent().getSerializableExtra(VP_Constant.VP_EXTRA_VIDEO_LIST);
            vp_videoPosition = getIntent().getIntExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, 0);
        }
        this.vp_videoPlayer.vp_media_datas = this.vp_videosList;
        this.vp_videoPlayer.setAdapter();
        if (vp_videosList == null) {
            vp_videosList = VP_MyApplication.vp_getVideoList();
        }
        boolean vp_booleanExtra = getIntent().getBooleanExtra(VP_Constant.VP_EXTRA_IS_FLOATING_VIDEO, false);
        vp_isFloatingVideo = vp_booleanExtra;
        if (vp_booleanExtra) {
            vp_videoPosition = VP_MyApplication.vp_getFloatingVideoPosition();
            vp_videoLastProgress = getIntent().getIntExtra(VP_Constant.VP_EXTRA_FLOATING_VIDEO, 0);
        }
        vp_isContinueWatching = getIntent().getBooleanExtra(VP_Constant.VP_EXTRA_IS_CONTINUE_WATCHING_VIDEO, false);
        boolean vp_booleanExtra2 = getIntent().getBooleanExtra(VP_Constant.VP_EXTRA_BACKGROUND_VIDEO_PLAY_POSITION, false);
        vp_isService = vp_booleanExtra2;
        if (vp_booleanExtra2) {
            vp_videoLastProgress = VP_MyApplication.vp_getFloatingVideoPosition();
        }
        initView();
    }

    public void initView() {
        vp_setVideoResume();
        vp_videoPlayer.setVp_onVideoCompleteListener(new VPVideoStandard.OnVideoCompleteListener() {

            @Override
            public void onDeleteCallBack() {
            }

            @Override
            public void onTakescreenShot() {
            }

            @Override
            public void onVideoComplete() {
                vp_isFloatingVideo = false;
                if (vp_videoPlayer.IsLoopingVideo()) {
                    vp_videoPlayer.startVideo();
                } else if (vp_isAutoPlay) {
                    if (vp_isShuffleClick) {
                        vp_getRandomVideoPosition();
                    }
                    if (vp_videoPosition != vp_videosList.size() - 1) {
                        vp_videoPosition++;
                        if (vp_videosList.size() > vp_videoPosition) {
                            if (vp_videosList.get(vp_videoPosition).layoutType == 1) {
                                vp_videoPosition++;
                            }
                            if (vp_videosList.size() > vp_videoPosition) {
                                vp_setVideoResume();
                            }
                        }
                    }
                    vp_videoPlayLastPositionList.put(vp_videosList.get(vp_videoPosition).path,
                            (int) vp_videoPlayer.getCurrentDuration());
                    VP_MyApplication.vp_setLastPlayVideos(vp_videosList.get(vp_videoPosition));
                }
            }

            @Override
            public void onNextClick() {
                VP_MyApplication.vp_setContinueWatchingVideos(VP_VideoPlayer.this.vp_videosList.get(VP_VideoPlayer.this.vp_videoPosition));
                if (VP_VideoPlayer.this.vp_isShuffleClick) {
                    VP_VideoPlayer.this.vp_getRandomVideoPosition();
                }
                if (VP_VideoPlayer.this.vp_videoPosition != VP_VideoPlayer.this.vp_videosList.size() - 1) {
                    VP_VideoPlayer.this.vp_videoPosition++;
                    if (VP_VideoPlayer.this.vp_videosList.get(VP_VideoPlayer.this.vp_videoPosition).layoutType == 1) {
                        VP_VideoPlayer.this.vp_videoPosition++;
                    }
                    if (VP_VideoPlayer.this.vp_videoPosition < VP_VideoPlayer.this.vp_videosList.size()) {
                        VP_VideoPlayer.this.vp_setVideoResume();
                    }
                }
                VP_VideoPlayer.this.vp_videoPlayLastPositionList.put(VP_VideoPlayer.this.vp_videosList.get(VP_VideoPlayer.this.vp_videoPosition).path,
                        (int) VP_VideoPlayer.this.vp_videoPlayer.getCurrentDuration());
            }

            @Override
            public void onPreviousClick() {
                VP_MyApplication.vp_setContinueWatchingVideos(VP_VideoPlayer.this.vp_videosList.get(VP_VideoPlayer.this.vp_videoPosition));
                if (VP_VideoPlayer.this.vp_isShuffleClick) {
                    VP_VideoPlayer.this.vp_getRandomVideoPosition();
                }
                if (VP_VideoPlayer.this.vp_videoPosition != 0) {
                    VP_VideoPlayer.this.vp_videoPosition--;
                    if (VP_VideoPlayer.this.vp_videosList.get(VP_VideoPlayer.this.vp_videoPosition).layoutType == 1) {
                        VP_VideoPlayer.this.vp_videoPosition--;
                    }
                    if (VP_VideoPlayer.this.vp_videoPosition != -1) {
                        VP_VideoPlayer.this.vp_setVideoResume();
                    }
                }
                VP_VideoPlayer.this.vp_videoPlayLastPositionList.put(VP_VideoPlayer.this.vp_videosList.get(VP_VideoPlayer.this.vp_videoPosition).path, Integer.valueOf((int) VP_VideoPlayer.this.vp_videoPlayer.getCurrentDuration()));
            }

            @SuppressLint("WrongConstant")
            @Override
            public void onFloatingwindowClick() {
                if (Build.VERSION.SDK_INT < 23) {
                    vp_setValue();
                    startService(new Intent(VP_VideoPlayer.this, VP_FloatingWidgetService.class).putExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, vp_videoPosition));
                    finish();
                } else if (Settings.canDrawOverlays(VP_VideoPlayer.this)) {
                    vp_setValue();
                    startService(new Intent(VP_VideoPlayer.this, VP_FloatingWidgetService.class).putExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, vp_videoPosition));
                    finish();
                } else {
                    vp_askForSystemOverlayPermission();
                    Toast.makeText(VP_VideoPlayer.this, "System Alert Window Permission Is Required For Floating Widget.", 1).show();
                }
            }

            @Override
            public void onBackClick() {
                try {
                    showLeaveDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    showLeaveDialog();
                }
            }

            @Override
            public void onShuffleClick(boolean z) {
                VP_VideoPlayer.this.vp_isShuffleClick = z;
            }

            @Override
            public void onBackgroundEnable(boolean z) {
                VP_VideoPlayer.this.vp_isBackgroundEnable = z;
            }

            @Override
            public void onSelectVideo(int i) {
                VP_VideoPlayer.this.vp_videoPosition = i;
                VP_VideoPlayer.this.vp_setVideoResume();
                VP_VideoPlayer.this.vp_videoPlayLastPositionList.put(VP_VideoPlayer.this.vp_videosList.get(VP_VideoPlayer.this.vp_videoPosition).path,
                        (int) VP_VideoPlayer.this.vp_videoPlayer.getCurrentDuration());
            }
        });
    }

    public void vp_setVideoResume() {
        vp_videoLastProgress = 0;
        if (this.vp_videosList.size() > this.vp_videoPosition) {
            HashMap<String, Integer> videoLastPosition = VP_MyApplication.vp_getVideoLastPosition();
            if (this.vp_isResumeVideo) {
                if (videoLastPosition.containsKey(this.vp_videosList.get(this.vp_videoPosition).path)) {
                    this.vp_videoLastProgress = (int) Double.parseDouble(String.valueOf(videoLastPosition.get(this.vp_videosList.get(this.vp_videoPosition).path)));
                }
            } else if (this.vp_isContinueWatching) {
                this.vp_videoLastProgress = (int) Double.parseDouble(String.valueOf(this.vp_videosList.get(this.vp_videoPosition).getVideoLastPlayPosition()));
            }
            final int i2 = this.vp_videoPlayer.vp_screen;
            this.vp_videoPlayer.setUp(this.vp_videosList.get(this.vp_videoPosition).path, this.vp_videosList.get(this.vp_videoPosition).name, 0);
            this.vp_videoPlayer.startVideo();
            new Handler().postDelayed(() -> {
                if (i2 != -1) {
                    VP_VideoPlayer.this.vp_videoPlayer.vp_screen = i2;
                }
            }, 500);

            MediaMetadataRetriever vp_retriever = new MediaMetadataRetriever();
            vp_retriever.setDataSource(this.vp_videosList.get(this.vp_videoPosition).path);
            int vp_width = Integer.parseInt(vp_retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int vp_height = Integer.parseInt(vp_retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            if (vp_width > vp_height) {
                this.vp_videoPlayer.gotoAutoScreen(VP_Jzvd.VP_FULLSCREEN_ORIENTATION);
            } else {
                this.vp_videoPlayer.gotoAutoScreen(VP_Jzvd.VP_NORMAL_ORIENTATION);
            }
            try {
                vp_retriever.release();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void vp_setValue() {
        MediaData vp_media_Data = this.vp_videosList.get(this.vp_videoPosition);
        vp_media_Data.setVideoLastPlayPosition((int) this.vp_videoPlayer.getCurrentDuration());
        VP_MyApplication.vp_setLastPlayVideos(vp_media_Data);
        VP_MyApplication.vp_setFloatingVideoPosition(this.vp_videoPosition);
        VP_MyApplication.vp_setVideoList(this.vp_videosList);
        VP_MyApplication.vp_setIsFloatingVideo(true);
    }

    public void vp_getRandomVideoPosition() {
        vp_videoPosition = new Random().nextInt(this.vp_videosList.size());
    }

    public void vp_askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 123);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        VPVideoStandard vdStd = this.vp_videoPlayer;
        if (vdStd != null) {
            vdStd.onPause();
        }
        startBackgroundVideoPlayService();
    }

    @Override
    public void onResume() {
        super.onResume();
        VPVideoStandard vdStd = this.vp_videoPlayer;
        if (vdStd != null) {
            vdStd.onStart();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.vp_videoPlayer != null) {
            if (((long) this.vp_videosList.get(this.vp_videoPosition).videoDuration) != this.vp_videoPlayer.getCurrentDuration()) {
                MediaData vp_media_Data = this.vp_videosList.get(this.vp_videoPosition);
                vp_media_Data.setVideoLastPlayPosition((int) this.vp_videoPlayer.getCurrentDuration());
                VP_MyApplication.vp_setContinueWatchingVideos(vp_media_Data);
            }
            this.vp_videoPlayLastPositionList.put(this.vp_videosList.get(this.vp_videoPosition).path, (int) this.vp_videoPlayer.getCurrentDuration());
            VP_MyApplication.setVideoLastPosition(this.vp_videoPlayLastPositionList);
            this.vp_videoPlayer.onPause();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    public void onBackPressed() {
        try {
            vp_videoPlayer.onPause();
            showLeaveDialog();
        } catch (Exception e) {
            e.printStackTrace();
            showLeaveDialog();
        }
    }

    public void showLeaveDialog() {
        finish();
    }

    public void startBackgroundVideoPlayService() {
        if (this.vp_isBackgroundEnable) {
            MediaData vp_media_Data = this.vp_videosList.get(this.vp_videoPosition);
            vp_media_Data.setVideoLastPlayPosition((int) this.vp_videoPlayer.getCurrentDuration());
            this.vp_videosList.set(this.vp_videoPosition, vp_media_Data);
            VP_MyApplication.vp_setVideoList(this.vp_videosList);
            stopService(new Intent(this, VP_VideoPlayAsAudioService.class));
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(new Intent(this, VP_VideoPlayAsAudioService.class).putExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, this.vp_videoPosition));
            } else {
                startService(new Intent(this, VP_VideoPlayAsAudioService.class).putExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, this.vp_videoPosition));
            }
        }
    }
}
