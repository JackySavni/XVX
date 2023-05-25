package com.video.player.videoplayer.xvxvideoplayer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.adapters.VP_VideoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_EventBus;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VP_RecentFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static ImageView vp_ivNoData;
    ArrayList<MediaData> _mediaDataList = new ArrayList<>();
    public static RecyclerView vp_recentRecycler;
    private View vp_view;
    private ProgressBar vp_progress;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.vp_view = layoutInflater.inflate(R.layout.vp_fragment_recent, viewGroup, false);
        @SuppressLint("WrongConstant")
        NetworkInfo vp_activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        if (vp_activeNetworkInfo != null) {
            vp_activeNetworkInfo.isConnected();
        }
        vp_initView();
        vp_getVideo();
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
            vp_initAdapter(this._mediaDataList, event_Bus.getVp_value());
        } else if (vp_type == 1) {
            this.vp_progress.setVisibility(0);
            vp_recentRecycler.setVisibility(8);
            new Thread(new Runnable() {
                public void run() {
                    VP_RecentFragment.this.vp_getVideo();
                }
            }).start();
        } else if (vp_type == 2) {
            int value = event_Bus.getVp_value();
            if (value == 0) {
                Collections.sort(this._mediaDataList, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return (Long.compare(Long.parseLong(media_Data2.length), Long.parseLong(media_Data.length)));
                    }
                });
                vp_initAdapter(this._mediaDataList, VP_MyApplication.vp_getViewBy());
            } else if (value == 1) {
                Collections.sort(this._mediaDataList, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return Long.compare(media_Data2.videoDuration, media_Data2.videoDuration);
                    }
                });
                vp_initAdapter(this._mediaDataList, VP_MyApplication.vp_getViewBy());
            } else if (value == 2) {
                Collections.sort(this._mediaDataList, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return media_Data.name.compareTo(media_Data2.name);
                    }
                });
                vp_initAdapter(this._mediaDataList, VP_MyApplication.vp_getViewBy());
            } else if (value == 3) {
                Collections.sort(this._mediaDataList, new Comparator<MediaData>() {
                    public int compare(MediaData media_Data, MediaData media_Data2) {
                        return media_Data2.modifiedDate.compareTo(media_Data.modifiedDate);
                    }
                });
                vp_initAdapter(this._mediaDataList, VP_MyApplication.vp_getViewBy());
            }
        } else if (vp_type == 3) {
            this.vp_progress.setVisibility(0);
            vp_recentRecycler.setVisibility(8);
            new Thread(new Runnable() {
                public void run() {
                    VP_RecentFragment.this.vp_getVideo();
                }
            }).start();
        }
    }

    @SuppressLint("WrongConstant")
    public void vp_getVideo() {
        this._mediaDataList.clear();
        if (!TextUtils.isEmpty(VP_MyApplication.vp_getRecentPlay())) {
            final ArrayList<MediaData> arrayList = new Gson().fromJson(VP_MyApplication.vp_getRecentPlay(), new TypeToken<List<MediaData>>() {
            }.getType());
            this._mediaDataList.addAll(arrayList);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VP_RecentFragment.this.vp_initAdapter(arrayList, VP_MyApplication.vp_getViewBy());
                }
            });
            return;
        }
        this.vp_progress.setVisibility(8);
        vp_recentRecycler.setVisibility(8);
        vp_ivNoData.setVisibility(0);
    }

    @SuppressLint("WrongConstant")
    public void vp_initAdapter(ArrayList<MediaData> arrayList, int i) {
        this.vp_progress.setVisibility(8);
        if (arrayList.size() > 0) {
            vp_recentRecycler.setVisibility(0);
            vp_ivNoData.setVisibility(8);
            VP_VideoAdapter vp_video_Adapter = new VP_VideoAdapter(getActivity(), arrayList, i, 2);
//            if (i == 0) {
//                ex_recentRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
//            } else {
                vp_recentRecycler.setLayoutManager(new GridLayoutManager((Context) getActivity(), 2, 1, false));
//            }
            vp_recentRecycler.setAdapter(vp_video_Adapter);
            return;
        }
        vp_recentRecycler.setVisibility(8);
        vp_ivNoData.setVisibility(0);
    }

    private void vp_initView() {
        vp_recentRecycler = (RecyclerView) this.vp_view.findViewById(R.id.vp_recent_recycler);
        vp_progress = (ProgressBar) this.vp_view.findViewById(R.id.vp_progress);
        vp_ivNoData = (ImageView) this.vp_view.findViewById(R.id.vp_iv_nodata);
    }
}
