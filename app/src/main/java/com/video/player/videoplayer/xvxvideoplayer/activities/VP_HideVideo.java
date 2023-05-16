package com.video.player.videoplayer.xvxvideoplayer.activities;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.video.player.videoplayer.xvxvideoplayer.adapters.VP_HideAdapter;
import com.video.player.videoplayer.xvxvideoplayer.extra.MediaData;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_Constant;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import java.io.File;
import java.util.ArrayList;

public class VP_HideVideo extends AppCompatActivity implements View.OnClickListener {
    public static ImageView ex_ivNoData;
    public static RecyclerView ex_hideRecycler;
    private ProgressBar ex_progress;
    private ImageView ex_backHide;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.vp_ex_activity_hidevideo);
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            activeNetworkInfo.isConnected();
        }
        initView();
        initListener();
        new Thread(VP_HideVideo.this::getVideo).start();
    }

//    public void getVideo() {
//        final ArrayList arrayList = new ArrayList();
//        if (new File(Constant.HIDE_PATH).exists()) {
//            File[] listFiles = new File(Constant.HIDE_PATH).listFiles();
//            if (listFiles != null) {
//                for (int i = 0; i < listFiles.length; i++) {
//                    MediaData media_Data = new MediaData();
//                    media_Data.setName(listFiles[i].getName());
//                    media_Data.setPath(listFiles[i].getPath());
//                    media_Data.setFolder(listFiles[i].getParentFile().getName());
//                    media_Data.setLength(String.valueOf(listFiles[i].length()));
//                    media_Data.setAddeddate(String.valueOf(listFiles[i].lastModified()));
//                    media_Data.setModifieddate(String.valueOf(listFiles[i].lastModified()));
//                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
//                    mediaMetadataRetriever.setDataSource(listFiles[i].getPath());
//                    String extractMetadata = mediaMetadataRetriever.extractMetadata(9);
//                    int intValue = Integer.parseInt(mediaMetadataRetriever.extractMetadata(18));
//                    int intValue2 = Integer.parseInt(mediaMetadataRetriever.extractMetadata(19));
//                    String duration = VP_Utils.setDuration((long) Integer.parseInt(extractMetadata));
//                    media_Data.setResolution(intValue + "×" + intValue2);
//                    media_Data.setVideoDuration(Integer.parseInt(extractMetadata));
//                    mediaMetadataRetriever.release();
//                    arrayList.add(new MediaData(
//                            listFiles[i].getName(),
//                            listFiles[i].getPath(),
//                            listFiles[i].getParentFile().getName(),
//                            String.valueOf(listFiles[i].length()),
//                            String.valueOf(listFiles[i].lastModified()),
//                            String.valueOf(listFiles[i].lastModified()),
//                            intValue + "×" + intValue2,
//                            Integer.parseInt(extractMetadata),
//                            );
//                }
//            }
//            runOnUiThread(() -> HidevideoActivity.this.initAdapter(arrayList, MyApplication.getViewBy()));
//        }
//    }

    public void getVideo() {
        final ArrayList arrayList = new ArrayList();
        if (new File(VP_Constant.VP_HIDE_PATH).exists()) {
            File[] listFiles = new File(VP_Constant.VP_HIDE_PATH).listFiles();
            if (listFiles != null) {
                for (File listFile : listFiles) {
                    Uri collection;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                    } else {
                        collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    }

                    String[] projection = {MediaStore.Video.Media.DATA,
                            MediaStore.Video.Media.TITLE,
                            MediaStore.Video.Media.DATE_MODIFIED,
                            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                            MediaStore.Video.Media.SIZE,
                            MediaStore.Video.Media.DATE_ADDED,
                            MediaStore.Video.Media.DURATION,
                            MediaStore.Video.Media.RESOLUTION,
                            MediaStore.Video.Media._ID};

                    String selection = MediaStore.Audio.Media.DATA + " = ?";
                    String[] selectionArgs = new String[]{listFile.getPath()};

                    String sortOrder = MediaStore.Video.Media.DATE_MODIFIED + " DESC";
                    try (Cursor cursor = getContentResolver().query(
                            collection,
                            projection,
                            selection,
                            selectionArgs,
                            sortOrder
                    )) {
                        // Cache column indices.
                        while (cursor.moveToNext()) {
                            Uri uri = ContentUris.withAppendedId(
                                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));

                            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                            if (title == null) {
                                title = "";
                            }
                            String date_modified = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED));
                            String bucket_name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME));
                            int size = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                            String date_added = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED));
                            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                            String resolution = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                            if (resolution == null || TextUtils.isEmpty(resolution)) {
                                resolution = "0";
                            }
                            File file = null;
                            try {
                                file = new File(path);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (file != null && file.exists()) {
                                MediaData media_Data = new MediaData();
                                new MediaData(title, path, uri.toString(), bucket_name, String.valueOf(size), date_added,
                                        date_modified, duration, 2, resolution);

                                arrayList.add(media_Data);
                            }
                        }
                    }
                }
            }
            runOnUiThread(() -> VP_HideVideo.this.initAdapter(arrayList, VP_MyApplication.vp_getViewBy()));
        }
}

    @SuppressLint("WrongConstant")
    public void initAdapter(ArrayList<MediaData> arrayList, int i) {
        this.ex_progress.setVisibility(8);
        if (arrayList.size() > 0) {
            ex_hideRecycler.setVisibility(0);
            ex_ivNoData.setVisibility(8);
            VP_HideAdapter hide_Adapter = new VP_HideAdapter(this, arrayList, i);
            if (i == 0) {
                ex_hideRecycler.setLayoutManager(new LinearLayoutManager(this, 1, false));
            } else {
                ex_hideRecycler.setLayoutManager(new GridLayoutManager((Context) this, 2, 1, false));
            }
            ex_hideRecycler.setAdapter(hide_Adapter);
            return;
        }
        ex_hideRecycler.setVisibility(8);
        ex_ivNoData.setVisibility(0);
    }

    private void initListener() {
        this.ex_backHide.setOnClickListener(this);
    }

    private void initView() {
        ex_hideRecycler = (RecyclerView) findViewById(R.id.hide_recycler);
        ex_progress = (ProgressBar) findViewById(R.id.vp_progress);
        ex_backHide = (ImageView) findViewById(R.id.back_hide);
        ex_ivNoData = (ImageView) findViewById(R.id.vp_iv_nodata);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.back_hide) {
            onBackPressed();
        }
    }

    public void myStartActivity(Intent intent) {
        startActivity(intent);
    }
}
