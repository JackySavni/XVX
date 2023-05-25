package com.video.player.videoplayer.xvxvideoplayer.fragments;

import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.RemoveAllAds;
import static com.video.player.videoplayer.xvxvideoplayer.util.VP_SharePref.ShowHomeNative;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.adapters.VP_FolderAdapter;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_EventBus;
import com.video.player.videoplayer.xvxvideoplayer.model.VP_Folder;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VP_FolderFragment extends Fragment {
    private RecyclerView vp_folderRecycler;
    ArrayList<VP_Folder> vp_folderList = new ArrayList<>();
    private ProgressBar vp_progress;
    private ImageView vp_ivNoData;
    private View vp_view;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        org.greenrobot.eventbus.EventBus.getDefault().register(this);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.vp_view = layoutInflater.inflate(R.layout.vp_fragment_folder, viewGroup, false);
        @SuppressLint("WrongConstant")
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        vp_initView();
        new Thread(this::vp_getVideo).start();
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
        int type = event_Bus.getVp_type();
        if (type == 0) {
            vp_initAdapter(this.vp_folderList, event_Bus.getVp_value());
        } else if (type == 1) {
            this.vp_progress.setVisibility(0);
            this.vp_folderRecycler.setVisibility(8);
            new Thread(this::vp_getVideo).start();
        } else if (type == 2) {
            int value = event_Bus.getVp_value();
            if (value == 0) {
                Collections.sort(this.vp_folderList, (VPFolder, VPFolder2) -> Long.compare(VPFolder2.getMedia_data().size(), VPFolder.getMedia_data().size()));
                vp_initAdapter(this.vp_folderList, VP_MyApplication.vp_getViewBy());
            } else if (value == 2) {
                Collections.sort(this.vp_folderList, new Comparator<VP_Folder>() {
                    public int compare(VP_Folder VPFolder, VP_Folder VPFolder2) {
                        return VPFolder.getVp_name().compareTo(VPFolder2.getVp_name());
                    }
                });
                vp_initAdapter(this.vp_folderList, VP_MyApplication.vp_getViewBy());
            } else if (value == 3) {
                Collections.sort(this.vp_folderList, new Comparator<VP_Folder>() {
                    public int compare(VP_Folder VPFolder, VP_Folder VPFolder2) {
                        return (Long.compare(new File(VPFolder2.getMedia_data().get(0).path).getParentFile().lastModified(),
                                new File(VPFolder.getMedia_data().get(0).path).getParentFile().lastModified()));
                    }
                });
                vp_initAdapter(this.vp_folderList, VP_MyApplication.vp_getViewBy());
            }
        }
    }

    private void vp_initView() {
        this.vp_folderRecycler = (RecyclerView) this.vp_view.findViewById(R.id.vp_folder_recycler);
        this.vp_progress = (ProgressBar) this.vp_view.findViewById(R.id.vp_progress);
        this.vp_ivNoData = (ImageView) this.vp_view.findViewById(R.id.vp_iv_nodata);
    }

    public void vp_getVideo() {
        ArrayList<String> vp_arrayList = new ArrayList<>();
        ArrayList<MediaData> vp_arrayList2 = new ArrayList<>();
        vp_folderList.clear();
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
//                    if (string6 == null) {
//                        string6 = null;
//                    } else if (!string6.contains(":")) {
//                        string6 = VP_Utils.makeShortTimeString(getActivity(), Long.parseLong(string6) / 1000);
//                    }
                    vp_arrayList2.add(new MediaData(vp_title, vp_path, vp_uri.toString(), vp_bucket_name, String.valueOf(vp_size), vp_date_added,
                            vp_date_modified, vp_duration, 2, vp_resolution));
                }
            }
        }
        final ArrayList<VP_Folder> vp_arrayList3 = new ArrayList<>();
        final ArrayList<VP_Folder> vp_arrayListNew = new ArrayList<>();
        if (vp_arrayList.size() > 0) {
            for (int i = 0; i < vp_arrayList.size(); i++) {
                VP_Folder VPFolder = new VP_Folder();
                VPFolder.setVp_name((String) vp_arrayList.get(i));
                ArrayList<MediaData> arrayList4 = new ArrayList<>();
                for (int i2 = 0; i2 < vp_arrayList2.size(); i2++) {
                    if (vp_arrayList2.get(i2).getFolder().equals(vp_arrayList.get(i))) {
                        arrayList4.add(vp_arrayList2.get(i2));
                    }
                }
                VPFolder.setMedia_data(arrayList4);
                if (arrayList4.size() != 0) {
                    vp_arrayList3.add(VPFolder);
                }
            }
        }
        this.vp_folderList.addAll(vp_arrayList3);
        for (int i = 0; i < vp_arrayList3.size(); i++) {
            vp_arrayListNew.add(vp_arrayList3.get(i));
            if (i == 2 && !RemoveAllAds(getActivity())) {
                vp_arrayListNew.add(null);
            }
        }
        if (ShowHomeNative(getActivity())) {
            getActivity().runOnUiThread(() -> VP_FolderFragment.this.vp_initAdapter(vp_arrayListNew, VP_MyApplication.vp_getViewBy()));
        } else {
            getActivity().runOnUiThread(() -> VP_FolderFragment.this.vp_initAdapter(vp_arrayList3, VP_MyApplication.vp_getViewBy()));
        }
    }

    @SuppressLint("WrongConstant")
    public void vp_initAdapter(ArrayList<VP_Folder> arrayList, int i) {
        this.vp_progress.setVisibility(8);
        if (arrayList.size() > 0) {
            this.vp_folderRecycler.setVisibility(0);
            this.vp_ivNoData.setVisibility(8);
            VP_FolderAdapter folder_Adapter = new VP_FolderAdapter(getActivity(), arrayList, i);
            if (i == 0) {
                this.vp_folderRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), 1, false));
            } else {
                this.vp_folderRecycler.setLayoutManager(new GridLayoutManager((Context) getActivity(), 3, 1, false));
            }
            this.vp_folderRecycler.setAdapter(folder_Adapter);
            return;
        }
        this.vp_folderRecycler.setVisibility(8);
        this.vp_ivNoData.setVisibility(0);
    }
}
