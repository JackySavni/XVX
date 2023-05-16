package com.video.player.videoplayer.xvxvideoplayer.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_VideoPlayerUtils;
import com.video.player.videoplayer.xvxvideoplayer.vid.VP_Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class VP_VideoPlayAsAudioService extends Service {
    public static final String VP_NEXT_ACTION = "com.video.player.videoplayer.xvxvideoplayer.next";
    static final String VP_CHANNEL_ID = "video_player_channel";
    public static final String VP_PAUSE_ACTION = "com.video.player.videoplayer.xvxvideoplayer.pause";
    public static final String VP_NOTIFICATION_CLICK_ACTION = "notification_click";
    public static final String VP_PREVIOUS_ACTION = "com.video.player.videoplayer.xvxvideoplayer.previous";
    public static final String VP_PLAY_ACTION = "com.video.player.videoplayer.xvxvideoplayer.play";
    public static int vp_videoPosition;
    public static final String VP_STOP_ACTION = "com.video.player.videoplayer.xvxvideoplayer.stop";
    boolean vp_isRefresh = false;
    Handler vp_handler = new Handler();
    private final BroadcastReceiver vp_mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            VP_VideoPlayAsAudioService.this.vp_handleCommandIntent(intent);
        }
    };
    boolean vp_isUpdate = false;
    private long vp_mNotificationPostTime = 0;
    private NotificationManagerCompat vp_mNotificationManager;
    private NotificationCompat.Builder vp_notificationBuilder;
    MediaPlayer vp_mediaPlayer;
    Runnable vp_runnable;
    int vp_notificationId;
    ArrayList<MediaData> vp_videoList = new ArrayList<>();

    private void vp_createChannel() {

    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.vp_mNotificationManager = NotificationManagerCompat.from(this);
        this.vp_notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), VP_CHANNEL_ID);
        MediaPlayer vp_mediaPlayer2 = new MediaPlayer();
        this.vp_mediaPlayer = vp_mediaPlayer2;
        vp_mediaPlayer2.stop();
        this.vp_mediaPlayer.setDisplay(null);
        this.vp_videoList = VP_MyApplication.vp_getVideoList();
        vp_createChannel();
        IntentFilter vp_intentFilter = new IntentFilter();
        vp_intentFilter.addAction(VP_PAUSE_ACTION);
        vp_intentFilter.addAction(VP_STOP_ACTION);
        vp_intentFilter.addAction(VP_NEXT_ACTION);
        vp_intentFilter.addAction(VP_PREVIOUS_ACTION);
        registerReceiver(this.vp_mIntentReceiver, vp_intentFilter);
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            if (intent.getAction() == null || !intent.getAction().equals(VP_NOTIFICATION_CLICK_ACTION)) {
                if (!this.vp_isUpdate) {
                    vp_videoPosition = intent.getIntExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, 0);
                    vp_videoPlay();
                    this.vp_isUpdate = true;
                }
                vp_handleCommandIntent(intent);
                vp_managePlayVideo();
            } else {
                this.vp_handler.removeCallbacks(this.vp_runnable);
                stopForeground(true);
                stopSelf();
            }
        }
        return 1;
    }

    public void vp_managePlayVideo() {
        if (this.vp_videoList != null) {
            this.vp_mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (VP_VideoPlayAsAudioService.vp_videoPosition != VP_VideoPlayAsAudioService.this.vp_videoList.size()) {
                        VP_VideoPlayAsAudioService.vp_videoPosition++;
                        if (VP_VideoPlayAsAudioService.this.vp_videoList.get(VP_VideoPlayAsAudioService.vp_videoPosition).layoutType == 1) {
                            VP_VideoPlayAsAudioService.vp_videoPosition++;
                        }
                        VP_VideoPlayAsAudioService.this.vp_isRefresh = true;
                        VP_VideoPlayAsAudioService.this.vp_handler.removeCallbacks(VP_VideoPlayAsAudioService.this.vp_runnable);
                        VP_VideoPlayAsAudioService.this.vp_videoPlay();
                    }
                }
            });
        }
    }

    public void vp_videoPlay() {
        try {
            this.vp_mediaPlayer.reset();
            this.vp_mediaPlayer.setDataSource(this.vp_videoList.get(vp_videoPosition).path);
            this.vp_mediaPlayer.setAudioStreamType(3);
            this.vp_mediaPlayer.prepareAsync();
            this.vp_mediaPlayer.setOnPreparedListener(mediaPlayer -> {
                VP_VideoPlayAsAudioService.this.vp_mediaPlayer.seekTo(VP_VideoPlayAsAudioService.this.vp_videoList.get(VP_VideoPlayAsAudioService.vp_videoPosition).getVideoLastPlayPosition());
                VP_VideoPlayAsAudioService.this.vp_mediaPlayer.start();
                VP_VideoPlayAsAudioService.this.vp_updateNotification();
                VP_VideoPlayAsAudioService.this.vp_runnable.run();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void vp_handleCommandIntent(Intent intent) {
        String vp_action = intent.getAction();
        if (VP_NEXT_ACTION.equals(vp_action)) {
            if (vp_videoPosition != this.vp_videoList.size()) {
                int vp_i = vp_videoPosition + 1;
                vp_videoPosition = vp_i;
                if (this.vp_videoList.get(vp_i).layoutType == 1) {
                    vp_videoPosition++;
                }
                this.vp_isRefresh = true;
                this.vp_handler.removeCallbacks(this.vp_runnable);
                vp_videoPlay();
            }
        } else if (VP_PREVIOUS_ACTION.equals(vp_action)) {
            int vp_i2 = vp_videoPosition;
            if (vp_i2 != 0) {
                int i3 = vp_i2 - 1;
                vp_videoPosition = i3;
                if (this.vp_videoList.get(i3).layoutType == 1) {
                    vp_videoPosition--;
                }
                this.vp_isRefresh = true;
                this.vp_handler.removeCallbacks(this.vp_runnable);
                vp_videoPlay();
            }
        } else if (VP_PAUSE_ACTION.equals(vp_action)) {
            this.vp_mediaPlayer.pause();
            this.vp_isRefresh = true;
            vp_updateNotification();
        } else if (VP_PLAY_ACTION.equals(vp_action)) {
            this.vp_mediaPlayer.start();
            this.vp_isRefresh = true;
            vp_updateNotification();
        } else if (VP_STOP_ACTION.equals(vp_action)) {
            this.vp_mediaPlayer.stop();
            this.vp_isRefresh = false;
            this.vp_handler.removeCallbacks(this.vp_runnable);
            stopForeground(true);
            stopSelf();
            this.vp_mNotificationManager.cancelAll();
            this.vp_mNotificationManager.cancel(this.vp_notificationId);
        }
    }

    public void vp_updateNotification() {
        this.vp_notificationId = hashCode();
        this.vp_mNotificationManager.cancelAll();
        if (!this.vp_isRefresh) {
            startForeground(this.vp_notificationId, vp_buildNotification());
        } else {
            this.vp_mNotificationManager.notify(this.vp_notificationId, vp_buildNotification());
        }
    }

    private Notification vp_buildNotification() {
        final RemoteViews vp_remoteViews = new RemoteViews(getPackageName(), (int) R.layout.vp_layout_notification_collapse);
        final RemoteViews vp_remoteViews2 = new RemoteViews(getPackageName(), (int) R.layout.vp_layout_notification);
        String vp_name = this.vp_videoList.get(vp_videoPosition).name;
        boolean vp_isPlaying = this.vp_mediaPlayer.isPlaying();
        String vp_str = VP_VideoPlayerUtils.vp_makeShortTimeString(this, (long) (this.vp_mediaPlayer.getCurrentPosition() / 1000)) + " / "
                + VP_VideoPlayerUtils.vp_makeShortTimeString(this, (long) (this.vp_videoList.get(vp_videoPosition).videoDuration / 1000));
        int i = vp_isPlaying ? R.drawable.vp_ic_pause : R.drawable.vp_ic_play;
        @SuppressLint("WrongConstant") PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(VP_Player
                .vp_getIntent(this, vp_videoList, vp_videoPosition, true)), 134217728);
        File vp_file = new File(this.vp_videoList.get(vp_videoPosition).path);
        Glide.with(this).load(this.vp_videoList.get(vp_videoPosition).path).apply((BaseRequestOptions<?>) ((RequestOptions)
                ((RequestOptions) ((RequestOptions) new RequestOptions().signature(new ObjectKey(vp_file.getAbsolutePath()
                        + vp_file.lastModified()))).priority(Priority.LOW)).diskCacheStrategy(DiskCacheStrategy.RESOURCE)))
                .transition(DrawableTransitionOptions.withCrossFade()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
                vp_remoteViews.setImageViewBitmap(R.id.vp_songImage, VP_VideoPlayAsAudioService.vp_drawableToBitmap(drawable));
                vp_remoteViews2.setImageViewBitmap(R.id.vp_songImage, VP_VideoPlayAsAudioService.vp_drawableToBitmap(drawable));
            }
        });
        if (this.vp_mNotificationPostTime == 0) {
            this.vp_mNotificationPostTime = System.currentTimeMillis();
        }
        vp_remoteViews.setTextViewText(R.id.vp_songName, vp_name);
        vp_remoteViews.setTextViewText(R.id.vp_artistName, vp_str);
        vp_remoteViews.setImageViewResource(R.id.vp_play_pause, i);
        vp_remoteViews2.setTextViewText(R.id.vp_songName, vp_name);
        vp_remoteViews2.setTextViewText(R.id.vp_artistName, vp_str);
        vp_remoteViews2.setImageViewResource(R.id.vp_play_pause, i);
        this.vp_notificationBuilder.setSmallIcon(R.mipmap.ic_launcher).setContentIntent(activity).setCustomContentView(vp_remoteViews).setCustomBigContentView(vp_remoteViews2);
        if (vp_isPlaying) {
            vp_remoteViews.setOnClickPendingIntent(R.id.vp_play_pause, retrievePlaybackAction(VP_PAUSE_ACTION));
            vp_remoteViews2.setOnClickPendingIntent(R.id.vp_play_pause, retrievePlaybackAction(VP_PAUSE_ACTION));
        } else {
            vp_remoteViews.setOnClickPendingIntent(R.id.vp_play_pause, retrievePlaybackAction(VP_PLAY_ACTION));
            vp_remoteViews2.setOnClickPendingIntent(R.id.vp_play_pause, retrievePlaybackAction(VP_PLAY_ACTION));
        }
        vp_remoteViews.setOnClickPendingIntent(R.id.vp_next, retrievePlaybackAction(VP_NEXT_ACTION));
        vp_remoteViews.setOnClickPendingIntent(R.id.vp_close, retrievePlaybackAction(VP_STOP_ACTION));
        vp_remoteViews2.setOnClickPendingIntent(R.id.vp_next, retrievePlaybackAction(VP_NEXT_ACTION));
        vp_remoteViews2.setOnClickPendingIntent(R.id.vp_previous, retrievePlaybackAction(VP_PREVIOUS_ACTION));
        vp_remoteViews2.setOnClickPendingIntent(R.id.vp_close, retrievePlaybackAction(VP_STOP_ACTION));
        this.vp_runnable = new Runnable() {
            public void run() {
                VP_VideoPlayAsAudioService.this.vp_isRefresh = true;
                VP_VideoPlayAsAudioService.this.vp_updateNotification();
                VP_VideoPlayAsAudioService.this.vp_handler.postDelayed(VP_VideoPlayAsAudioService.this.vp_runnable, 1000);
            }
        };
        return this.vp_notificationBuilder.build();
    }

    public static Bitmap vp_drawableToBitmap(Drawable drawable) {
        Bitmap vp_bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            vp_bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            vp_bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas vp_canvas = new Canvas(vp_bitmap);
        drawable.setBounds(0, 0, vp_canvas.getWidth(), vp_canvas.getHeight());
        drawable.draw(vp_canvas);
        return vp_bitmap;
    }

    private PendingIntent retrievePlaybackAction(String str) {
        ComponentName vp_componentName = new ComponentName(this, VP_VideoPlayAsAudioService.class);
        Intent vp_intent = new Intent(str);
        vp_intent.setComponent(vp_componentName);
        vp_intent.putExtra(VP_Constant.VP_EXTRA_VIDEO_POSITION, vp_videoPosition);
        return PendingIntent.getService(this, 0, vp_intent, 0);
    }

    public void onDestroy() {
        super.onDestroy();
        this.vp_handler.removeCallbacks(this.vp_runnable);
        this.vp_mediaPlayer.stop();
        stopSelf();
        stopForeground(true);
        this.vp_mNotificationManager.cancelAll();
        unregisterReceiver(this.vp_mIntentReceiver);
    }
}
