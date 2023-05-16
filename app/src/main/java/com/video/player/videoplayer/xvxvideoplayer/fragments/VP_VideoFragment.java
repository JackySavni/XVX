package com.video.player.videoplayer.xvxvideoplayer.fragments;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.adapters.VP_VideoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_EventBus;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class VP_VideoFragment extends Fragment {
    public static RecyclerView vp_videoRecycler;
    private static ImageView vp_ivNoData;
    private ProgressBar vp_progress;
    public static ArrayList<MediaData> _mediaDataList = new ArrayList<>();
    private View vp_view;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.vp_view = layoutInflater.inflate(R.layout.vp_fragment_video, viewGroup, false);
        initView();
        new Thread(VP_VideoFragment.this::getVideo).start();
        return this.vp_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        org.greenrobot.eventbus.EventBus.getDefault().unregister(this);
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VP_EventBus event_Bus) {
        int vp_type = event_Bus.getVp_type();
        if (vp_type == 0) {
            initAdapter(_mediaDataList, event_Bus.getVp_value());
        } else if (vp_type == 1) {
            this.vp_progress.setVisibility(0);
            vp_videoRecycler.setVisibility(8);
            new Thread(this::getVideo).start();
        } else if (vp_type == 2) {
            int vp_value = event_Bus.getVp_value();
            if (vp_value == 0) {
                Collections.sort(_mediaDataList, (vp_media_Data, media_Data2) -> Long.compare(Long.parseLong(media_Data2.length), Long.parseLong(vp_media_Data.length)));
                initAdapter(_mediaDataList, VP_MyApplication.vp_getViewBy());
            } else if (vp_value == 1) {
                Collections.sort(_mediaDataList, (vp_media_Data, media_Data2) -> Long.compare(media_Data2.videoDuration, media_Data2.videoDuration));
                initAdapter(_mediaDataList, VP_MyApplication.vp_getViewBy());
            } else if (vp_value == 2) {
                Collections.sort(_mediaDataList, (vp_media_Data, media_Data2) -> vp_media_Data.name.compareTo(media_Data2.name));
                initAdapter(_mediaDataList, VP_MyApplication.vp_getViewBy());
            } else if (vp_value == 3) {
                Collections.sort(_mediaDataList, (vp_media_Data, media_Data2) -> media_Data2.modifiedDate.compareTo(vp_media_Data.modifiedDate));
                initAdapter(_mediaDataList, VP_MyApplication.vp_getViewBy());
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void initAdapter(ArrayList<MediaData> vp_arrayList, int i) {
        this.vp_progress.setVisibility(8);
        if (vp_arrayList.size() > 0) {
            vp_videoRecycler.setVisibility(0);
            vp_ivNoData.setVisibility(8);
            VP_VideoAdapter video_Adapter = new VP_VideoAdapter(getActivity(), vp_arrayList, i, 0);
//            if (i == 0) {
//                ex_videoRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
//            } else {
                vp_videoRecycler.setLayoutManager(new GridLayoutManager((Context) getActivity(), 2, 1, false));
//            }
            vp_videoRecycler.setAdapter(video_Adapter);
            return;
        }
        vp_videoRecycler.setVisibility(8);
        vp_ivNoData.setVisibility(0);
    }

    private void initView() {
        vp_videoRecycler = (RecyclerView) this.vp_view.findViewById(R.id.vp_video_recycler);
        vp_progress = (ProgressBar) this.vp_view.findViewById(R.id.vp_progress);
        vp_ivNoData = (ImageView) this.vp_view.findViewById(R.id.vp_iv_nodata);
    }

    public void getVideo() {
        ArrayList<String> vp_arrayList = new ArrayList<>();
//        final ArrayList<MediaData> arrayList2 = new ArrayList<>();
        _mediaDataList.clear();
        Uri vp_collection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vp_collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            vp_collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        }

        String[] vp_projection = {MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.RESOLUTION,
                MediaStore.Video.Media._ID};
        String vp_sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " DESC";
        try (Cursor vp_cursor = getActivity().getContentResolver().query(
                vp_collection,
                vp_projection,
                null,
                null,
                vp_sortOrder
        )) {
            // Cache column indices.
            while (vp_cursor.moveToNext()) {
                Uri vp_uri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        vp_cursor.getLong(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));

                String vp_path = vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                String vp_title = vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                if (vp_title == null) {
                    vp_title = "";
                }
                String vp_date_modified = vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
                String vp_bucket_name = vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                int vp_size = vp_cursor.getInt(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                String vp_date_added = vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                int vp_duration = vp_cursor.getInt(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                String vp_resolution = vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                if (vp_resolution == null || TextUtils.isEmpty(vp_resolution)) {
                    vp_resolution = "0";
                }
                File vp_file = null;
                try {
                    vp_file = new File(vp_path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!vp_arrayList.contains(vp_bucket_name) && vp_bucket_name != null) {
                    vp_arrayList.add(vp_bucket_name);
                } else if (vp_bucket_name == null) {
                    if (!vp_arrayList.contains("XVX_Others")) {
                        vp_arrayList.add("XVX_Others");
                    }
                    vp_bucket_name = "XVX_Others";
                }
                if (vp_file != null && vp_file.exists()) {
                    _mediaDataList.add(new MediaData(vp_title, vp_path, vp_uri.toString(), vp_bucket_name, String.valueOf(vp_size), vp_date_added,
                            vp_date_modified, vp_duration, 2, vp_resolution));
                }
            }
        }
        _mediaDataList.addAll(_mediaDataList);
        getActivity().runOnUiThread(() -> VP_VideoFragment.this.initAdapter(_mediaDataList, VP_MyApplication.vp_getViewBy()));
    }
}
