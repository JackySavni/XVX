package com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.adapter.VP_VideoPlayerRecyclerAdapter;
import com.video.player.videoplayer.xvxvideoplayer.trend.edmt.dev.videoplayer.model.VP_MediaObject;

import java.util.ArrayList;
import java.util.Objects;

public class VP_VideoPlayerRecyclerView extends RecyclerView {
    private static final String TAG = "VP_VideoPlayerRecyclerView";

    private enum VolumeState {ON, OFF}

    ;
    // ui
    private ImageView vp_thumbnail;
    //    private ProgressBar progressBar;
    private View vp_viewHolderParent;
    private FrameLayout vp_frameLayout;
    private StyledPlayerView vp_videoSurfaceView;
    private ExoPlayer vp_videoPlayer;

    // vars
    private ArrayList<VP_MediaObject> VPMediaObjects = new ArrayList<>();
    private int vp_videoSurfaceDefaultHeight = 0;
    private int vp_screenDefaultHeight = 0;
    private int vp_playPosition = -1;
    private boolean vp_isVideoViewAdded;
    private RequestManager vp_requestManager;

    // controlling playback state
    private VolumeState vp_volumeState;

    public VP_VideoPlayerRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public VP_VideoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        context = context.getApplicationContext();
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        vp_videoSurfaceDefaultHeight = point.x;
        vp_screenDefaultHeight = point.y;

        vp_videoSurfaceView = new StyledPlayerView(context);
        int px1 = (int) (200 * Resources.getSystem().getDisplayMetrics().density);
        vp_videoSurfaceView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, px1));
        vp_videoSurfaceView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);

        DefaultTrackSelector vp_trackSelector = new DefaultTrackSelector(context);

        // 2. Create the player

        vp_videoPlayer = new ExoPlayer.Builder(context).setTrackSelector(vp_trackSelector).build();
        // Bind the player to the view.
        vp_videoSurfaceView.setUseController(false);
        vp_videoSurfaceView.setPlayer(vp_videoPlayer);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d(TAG, "onScrollStateChanged: called.");
                    if (vp_thumbnail != null) { // show the old thumbnail
                        vp_thumbnail.setVisibility(VISIBLE);
                    }

                    // There's a special case when the end of the list has been reached.
                    // Need to handle that with this bit of logic
                    playVideo(!recyclerView.canScrollVertically(1));
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        addOnChildAttachStateChangeListener(new OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                if (vp_viewHolderParent != null && vp_viewHolderParent.equals(view)) {
                    resetVideoView();
                }

            }
        });

        vp_videoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                switch (playbackState) {

                    case Player.STATE_BUFFERING:
                        Log.e(TAG, "onPlayerStateChanged: Buffering video.");
//                        if (progressBar != null) {
//                            progressBar.setVisibility(VISIBLE);
//                        }

                        break;
                    case Player.STATE_ENDED:
                        Log.d(TAG, "onPlayerStateChanged: Video ended.");
                        vp_videoPlayer.seekTo(0);
                        break;
                    case Player.STATE_IDLE:

                        break;
                    case Player.STATE_READY:
                        Log.e(TAG, "onPlayerStateChanged: Ready to play.");
//                        if (progressBar != null) {
//                            progressBar.setVisibility(GONE);
//                        }
                        if (!vp_isVideoViewAdded) {
                            addVideoView();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void playVideo(boolean isEndOfList) {

        int vp_targetPosition;

        if (!isEndOfList) {
            int vp_startPosition = ((LinearLayoutManager) Objects.requireNonNull(getLayoutManager())).findFirstVisibleItemPosition();
            int vp_endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            // if there is more than 2 list-items on the screen, set the difference to be 1
            if (vp_endPosition - vp_startPosition > 1) {
                vp_endPosition = vp_startPosition + 1;
            }

            // something is wrong. return.
            if (vp_startPosition < 0 || vp_endPosition < 0) {
                return;
            }

            // if there is more than 1 list-item on the screen
            if (vp_startPosition != vp_endPosition) {
                int vp_startPositionVideoHeight = getVisibleVideoSurfaceHeight(vp_startPosition);
                int vp_endPositionVideoHeight = getVisibleVideoSurfaceHeight(vp_endPosition);

                vp_targetPosition = vp_startPositionVideoHeight > vp_endPositionVideoHeight ? vp_startPosition : vp_endPosition;
            } else {
                vp_targetPosition = vp_startPosition;
            }
        } else {
            vp_targetPosition = VPMediaObjects.size() - 1;
        }

        Log.d(TAG, "playVideo: target position: " + vp_targetPosition);

        // video is already playing so return
        if (vp_targetPosition == vp_playPosition) {
            return;
        }

        // set the position of the list-item that is to be played
        vp_playPosition = vp_targetPosition;
        if (vp_videoSurfaceView == null) {
            return;
        }

        // remove any old surface views from previously playing videos
        vp_videoSurfaceView.setVisibility(INVISIBLE);
        vp_removeVideoView(vp_videoSurfaceView);

        int vp_currentPosition = vp_targetPosition - ((LinearLayoutManager) Objects.requireNonNull(getLayoutManager())).findFirstVisibleItemPosition();

        View vp_child = getChildAt(vp_currentPosition);
        if (vp_child == null) {
            return;
        }

        VP_VideoPlayerRecyclerAdapter.ItemHolder holder = (VP_VideoPlayerRecyclerAdapter.ItemHolder) vp_child.getTag();
        if (holder == null) {
            vp_playPosition = -1;
            return;
        }
        vp_thumbnail = holder.vp_thumbnail;
//        progressBar = holder.progressBar;
        vp_viewHolderParent = holder.itemView;
        vp_requestManager = holder.vp_requestManager;
        vp_frameLayout = holder.itemView.findViewById(R.id.vp_media_container);

        vp_videoSurfaceView.setPlayer(vp_videoPlayer);

//        viewHolderParent.setOnClickListener(videoViewClickListener);

        if (!VPMediaObjects.get(vp_targetPosition).isVp_isAd()) {
            String preview = VPMediaObjects.get(vp_targetPosition).getVp_preview();
            if (preview != null) {
                vp_videoPlayer.prepare();
                vp_videoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(preview)));
                vp_videoPlayer.setPlayWhenReady(true);
            }
        }

    }

//    private final OnClickListener videoViewClickListener = v -> toggleVolume();

    /**
     * Returns the visible region of the video surface on the screen.
     * if some is cut off, it will return less than the @videoSurfaceDefaultHeight
     *
     * @param playPosition
     * @return
     */
    private int getVisibleVideoSurfaceHeight(int playPosition) {
        int at = playPosition - ((LinearLayoutManager) Objects.requireNonNull(getLayoutManager())).findFirstVisibleItemPosition();
        Log.d(TAG, "getVisibleVideoSurfaceHeight: at: " + at);

        View child = getChildAt(at);
        if (child == null) {
            return 0;
        }

        int[] location = new int[2];
        child.getLocationInWindow(location);

        if (location[1] < 0) {
            return location[1] + vp_videoSurfaceDefaultHeight;
        } else {
            return vp_screenDefaultHeight - location[1];
        }
    }


    // Remove the old player
    private void vp_removeVideoView(StyledPlayerView videoView) {
        ViewGroup vp_parent = (ViewGroup) videoView.getParent();
        if (vp_parent == null) {
            return;
        }

        int index = vp_parent.indexOfChild(videoView);
        if (index >= 0) {
            vp_parent.removeViewAt(index);
            vp_isVideoViewAdded = false;
//            viewHolderParent.setOnClickListener(null);
        }

    }

    private void addVideoView() {
        vp_frameLayout.addView(vp_videoSurfaceView);
        vp_isVideoViewAdded = true;
        vp_videoSurfaceView.requestFocus();
        vp_videoSurfaceView.setVisibility(VISIBLE);
        vp_videoSurfaceView.setAlpha(1);
        vp_thumbnail.setVisibility(GONE);
    }

    private void resetVideoView() {
        if (vp_isVideoViewAdded) {
            vp_removeVideoView(vp_videoSurfaceView);
            vp_playPosition = -1;
            vp_videoSurfaceView.setVisibility(INVISIBLE);
            vp_thumbnail.setVisibility(VISIBLE);
        }
    }

    public void releasePlayer() {
        if (vp_videoPlayer != null) {
            vp_videoPlayer.release();
            vp_videoPlayer = null;
        }
        vp_viewHolderParent = null;
    }

    public void setMediaObjects(ArrayList<VP_MediaObject> VPMediaObjects) {
        this.VPMediaObjects = VPMediaObjects;
    }
}
