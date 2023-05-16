package com.video.player.videoplayer.xvxvideoplayer.trend;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;

public class VP_TrendPlayer extends AppCompatActivity {
    ExoPlayer vp_exoPlayer;
    ImageView vp_bt_fullscreen, vp_bt_play, vp_bt_pause;
    boolean vp_isFullScreen = false;
    boolean vp_isLock = false;
    String vp_trendVidURL = "";
    private StyledPlayerView vp_playerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vp_activity_trend_player);
        if (getIntent().getStringExtra(VP_Constant.VP_EXTRA_TREND_VIDEO_URL) != null) {
            vp_trendVidURL = getIntent().getStringExtra(VP_Constant.VP_EXTRA_TREND_VIDEO_URL);
        }
        vp_playerView = findViewById(R.id.vp_player);
        ProgressBar progressBar = findViewById(R.id.vp_progress_bar);
        vp_bt_fullscreen = findViewById(R.id.bt_fullscreen);
        vp_bt_play = findViewById(R.id.exo_play);
        vp_bt_pause = findViewById(R.id.exo_pause);
        ImageView bt_lockscreen = findViewById(R.id.exo_lock);
        //toggle button with change icon fullscreen or exit fullscreen
        //the screen can rotate base on you angle direction sensor
        vp_bt_fullscreen.setOnClickListener(view ->
        {
            if (!vp_isFullScreen) {
                vp_bt_fullscreen.setImageDrawable(
                        ContextCompat
                                .getDrawable(getApplicationContext(), R.drawable.vp_galley_ic_fullscreen_exit));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else {
                vp_bt_fullscreen.setImageDrawable(ContextCompat
                        .getDrawable(getApplicationContext(), R.drawable.vp_galley_ic_fullscreen));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            vp_isFullScreen = !vp_isFullScreen;
        });
        bt_lockscreen.setOnClickListener(view ->
        {
            //change icon base on toggle lock screen or unlock screen
            if (!vp_isLock) {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vp_ic_lock_24dp));
            } else {
                bt_lockscreen.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vp_ic_lock_open_24dp));
            }
            vp_isLock = !vp_isLock;
            //method for toggle will do next
            vp_lockScreen(vp_isLock);
        });
        vp_bt_play.setOnClickListener(v -> {
            vp_exoPlayer.setPlayWhenReady(true);
            vp_exoPlayer.getPlaybackState();
            vp_bt_play.setVisibility(View.GONE);
            vp_bt_pause.setVisibility(View.VISIBLE);
        });
        vp_bt_pause.setOnClickListener(v -> {
            vp_exoPlayer.setPlayWhenReady(false);
            vp_exoPlayer.getPlaybackState();
            vp_bt_pause.setVisibility(View.GONE);
            vp_bt_play.setVisibility(View.VISIBLE);
        });
        //instance the player with skip back duration 5 second or forward 5 second
        //5000 millisecond = 5 second
        vp_exoPlayer = new ExoPlayer.Builder(this)
                .setSeekBackIncrementMs(5000)
                .setSeekForwardIncrementMs(5000)
                .build();
        vp_playerView.setPlayer(vp_exoPlayer);
        //screen alway active
        vp_playerView.setKeepScreenOn(true);
        //track state
        vp_exoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                //when data video fetch stream from internet
                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE);

                } else if (playbackState == Player.STATE_READY) {
                    //then if streamed is loaded we hide the progress bar
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        //pass the video link and play
        MediaItem media = MediaItem.fromUri(Uri.parse(vp_trendVidURL));
        vp_exoPlayer.setMediaItem(media);
        vp_exoPlayer.prepare();
        vp_exoPlayer.play();
    }

    void vp_lockScreen(boolean lock) {
        //just hide the control for lock screen and vise versa
        LinearLayout vp_sec_mid = findViewById(R.id.sec_controlvid1);
        LinearLayout vp_sec_bottom = findViewById(R.id.sec_controlvid2);
        if (lock) {
            vp_sec_mid.setVisibility(View.INVISIBLE);
            vp_sec_bottom.setVisibility(View.INVISIBLE);
        } else {
            vp_sec_mid.setVisibility(View.VISIBLE);
            vp_sec_bottom.setVisibility(View.VISIBLE);
        }
    }

    //when is in lock screen we not accept for backpress button
    @Override
    public void onBackPressed() {
        //on lock screen back press button not work
        if (vp_isLock) return;
        //if user is in landscape mode we turn to portriat mode first then we can exit the app.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            vp_bt_fullscreen.performClick();
        } else {
            if (vp_exoPlayer != null) {
                vp_exoPlayer.release();
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vp_exoPlayer != null) {
            vp_exoPlayer.pause();
            vp_exoPlayer.setPlayWhenReady(false);
            vp_exoPlayer.getPlaybackState();
            vp_bt_pause.setVisibility(View.GONE);
            vp_bt_play.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vp_playerView != null) {
            vp_playerView.onResume();
        }
    }

}