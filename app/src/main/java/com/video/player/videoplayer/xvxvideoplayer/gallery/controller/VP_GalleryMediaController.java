package com.video.player.videoplayer.xvxvideoplayer.gallery.controller;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryViewPagerTitleAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.VP_GalleryMainActivity;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.mainnavigation.VP_GalleryPhotosFragment;
import com.video.player.videoplayer.xvxvideoplayer.gallery.view.viewer.VP_GalleryAlbumActivity;
import com.video.player.videoplayer.xvxvideoplayer.util.VP_MyApplication;

import java.io.File;
import java.util.ArrayList;

public class VP_GalleryMediaController {
    public static final int REQ_CODE_DELETE = 222;

    /**
     * share media
     *
     * @param uri
     * @param title
     * @param mime
     * @param activity
     */
    public static void ex_shareMedia(Uri uri, String title, String mime, Activity activity) {
        if (activity != null && uri != null) {
            Intent vp_intent = new Intent(Intent.ACTION_SEND);
            vp_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            vp_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            vp_intent.putExtra(Intent.EXTRA_STREAM, uri);
            vp_intent.setType(mime);
            activity.startActivity(Intent.createChooser(vp_intent, title));
        }
    }

    /**
     * share list of medias
     *
     * @param uris
     * @param title
     * @param mime
     * @param activity
     */
    public static void vp_shareMultipleMedias(ArrayList<Uri> uris, String title, String mime, Activity activity) {
        if (activity != null && uris != null && uris.size() > 0) {
            Intent vp_intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            vp_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            vp_intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            vp_intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            vp_intent.setType(mime);
            activity.startActivity(Intent.createChooser(vp_intent, title));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static void ex_deleteMedia(ArrayList<Uri> uri, ArrayList<VP_GalleryPhoto> list, int position, Activity activity) {
        try {
            PendingIntent vp_pendingIntent = MediaStore.createDeleteRequest(activity.getContentResolver(), uri);
            activity.startIntentSenderForResult(vp_pendingIntent.getIntentSender(),
                    VP_GalleryMediaController.REQ_CODE_DELETE, null, 0, 0, 0);
            ((VP_MyApplication) activity.getApplicationContext()).vp_deleteList = list;
            ((VP_MyApplication) activity.getApplicationContext()).vp_deleteAlbActPhotoPos = position;
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    /**
     * delete media from folder
     *
     * @param vp_pathList
     * @param vp_indexList
     * @param vp_isPhoto
     * @param vp_activity
     */
    public static void vp_deleteMediaFile(ArrayList<String> vp_pathList, ArrayList<Integer> vp_indexList, boolean vp_isPhoto, Activity vp_activity) {
        if (vp_pathList != null && vp_indexList != null) {
            for (int vp_position : vp_indexList) {
                File vp_file = new File(vp_pathList.get(vp_position));
                vp_file.delete();
                MediaScannerConnection.scanFile(vp_activity,
                        new String[]{vp_file.toString()},
                        new String[]{vp_file.getName()}, null);
                if (vp_isPhoto) {
                    for (int pos : vp_indexList) {
                        vp_deletePhotoFromAdapter(vp_activity);
                    }
                }
            }
        }
    }

    /**
     * delete video from adapter
     *
     * @param videoPosition
     */
    /**
     * delete photo from adapter
     * <p>
     * //     * @param photoPosition
     */
    public static void vp_deletePhotoFromAdapter(Activity activity) {
        if (activity instanceof VP_GalleryMainActivity) {
            VP_GalleryViewPagerTitleAdapter vp_adapter = VP_GalleryMainActivity.vp_getAdapter();
            if (vp_adapter != null) {
                VP_GalleryPhotosFragment vp_fragment = (VP_GalleryPhotosFragment) vp_adapter.getItem(0);
                vp_fragment.getVp_adapter().vp_deletePhoto(activity);
            }
        } else if (activity instanceof VP_GalleryAlbumActivity) {
            ((VP_GalleryAlbumActivity) activity).getVp_adapter().vp_deletePhoto(activity);
        }

    }

}
