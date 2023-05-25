package com.video.player.videoplayer.xvxvideoplayer.gallery.controller;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryAlbumsAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryPhotoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryAlbum;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VP_GalleryAlbumsController {

    /**
     * show button go back
     *
     * @param btnFinish
     */
    public static void ex_showBtnFinish(ImageButton btnFinish) {
        btnFinish.setVisibility(View.VISIBLE);
    }

    /**
     * set galleryPhotos from galleryAlbum to adapter
     *
     * @param adapter
     * @param VPGalleryAlbum
     */
    public static void ex_setPhotos(VP_GalleryPhotoAdapter adapter, VP_GalleryAlbum VPGalleryAlbum) {
        if (adapter != null && VPGalleryAlbum != null) {
            adapter.vp_setAlbumPhotos(VPGalleryAlbum.getPhotos(), VPGalleryAlbum.getVp_name());
        }
    }

    /**
     * set galleryAlbum name and number of galleryPhotos
     *
     * @param VPGalleryAlbum
     * @param txtTitle
     */
    @SuppressLint("SetTextI18n")
    public static void ex_setTitle(VP_GalleryAlbum VPGalleryAlbum, TextView txtTitle) {
        if (VPGalleryAlbum != null && txtTitle != null) {
            txtTitle.setText(VPGalleryAlbum.getVp_name() + " ∙ " + VPGalleryAlbum.getPhotos().size());
        }
    }

    /**
     * get all files names
     * <p>
     *
     * @param vp_contextWeakReference
     * @param vp_albumsAdapterWeakReference
     */
    public static void vp_getFilesNames(
            WeakReference<Context> vp_contextWeakReference,
            WeakReference<VP_GalleryAlbumsAdapter> vp_albumsAdapterWeakReference) {

        Context context = vp_contextWeakReference.get();
        VP_GalleryAlbumsAdapter VPGalleryAlbumsAdapter = vp_albumsAdapterWeakReference.get();

        if (context != null && VPGalleryAlbumsAdapter != null) {
            ex_getPhotos(vp_albumsAdapterWeakReference, vp_contextWeakReference);
        }
    }

    /**
     * get all galleryPhotos from folder
     * add galleryPhotos to album
     * <p>
     *
     * @param vp_albumsAdapterWeakReference
     * @param vp_contextWeakReference
     */
    @SuppressLint("InlinedApi")
    public static void ex_getPhotos(
            WeakReference<VP_GalleryAlbumsAdapter> vp_albumsAdapterWeakReference,
            WeakReference<Context> vp_contextWeakReference) {

        Context context = vp_contextWeakReference.get();
        VP_GalleryAlbumsAdapter VPGalleryAlbumsAdapter = vp_albumsAdapterWeakReference.get();
        HashMap<String, ArrayList<VP_GalleryPhoto>> vp_folderAlbum = new HashMap<>();

        if (context != null && VPGalleryAlbumsAdapter != null) {
            Uri vp_uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] vp_projection;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                vp_projection = new String[]{MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATE_MODIFIED,
                        MediaStore.Images.ImageColumns.RESOLUTION,
                        MediaStore.Images.ImageColumns.SIZE,
                        MediaStore.Images.ImageColumns.DISPLAY_NAME,
                        MediaStore.Images.ImageColumns._ID};
            } else {
                vp_projection = new String[]{MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATE_MODIFIED,
                        MediaStore.Images.ImageColumns.SIZE,
                        MediaStore.Images.ImageColumns.DISPLAY_NAME,
                        MediaStore.Images.ImageColumns._ID};
            }


            String vp_orderBy = MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC";
            Cursor vp_cursor = context.getContentResolver().query(vp_uri, vp_projection, null,
                    null, vp_orderBy);

            int vp_indexData = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
            int vp_indexFolderName = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME);
            int vp_indexDate = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_MODIFIED);
            int vp_indexSize = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.SIZE);
            int vp_indexDisplayName = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);
            int vp_indexID = vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);

            int vp_totalPhotos = 0;
            while (vp_cursor.moveToNext()) {
                String vp_path = vp_cursor.getString(vp_indexData);
                String vp_date = vp_cursor.getString(vp_indexDate);
                Long vp_size = vp_cursor.getLong(vp_indexSize);
                String vp_name = vp_cursor.getString(vp_indexDisplayName);
                String vp_folderName = vp_cursor.getString(vp_indexFolderName);
                if (vp_folderName == null) {
                    vp_folderName = "Unknown_0";
                }
                Uri vp_uriPhoto = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, vp_cursor.getLong(vp_indexID));

                if (vp_folderAlbum.containsKey(vp_folderName)) {
                    ArrayList<VP_GalleryPhoto> vp_album = vp_folderAlbum.get(vp_folderName);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (vp_album != null) {
                            vp_album.add(new VP_GalleryPhoto(vp_uriPhoto, vp_name, vp_path, vp_date,
                                    vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.RESOLUTION)),
                                    vp_size));
                        }
                    } else {
                        if (vp_album != null) {
                            vp_album.add(new VP_GalleryPhoto(vp_uriPhoto, vp_name, vp_path, vp_date, "resolution", vp_size));
                        }
                    }
                    vp_folderAlbum.put(vp_folderName, vp_album);
                } else {
                    ArrayList<VP_GalleryPhoto> vp_album = new ArrayList<>();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        vp_album.add(new VP_GalleryPhoto(vp_uriPhoto, vp_name, vp_path, vp_date,
                                vp_cursor.getString(vp_cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.RESOLUTION)),
                                vp_size));
                    } else {
                        vp_album.add(new VP_GalleryPhoto(vp_uriPhoto, vp_name, vp_path, vp_date, "resolution", vp_size));
                    }
                    vp_folderAlbum.put(vp_folderName, vp_album);
                }
            }
            int vp_nrPhotos = vp_cursor.getCount();
            if (vp_nrPhotos > 0) {
                VPGalleryAlbumsAdapter.clearAlbum();
                if (!vp_folderAlbum.entrySet().isEmpty()) {
                    for (Map.Entry<String, ArrayList<VP_GalleryPhoto>> entry : vp_folderAlbum.entrySet()) {
                        VP_GalleryAlbum VPGalleryAlbum = new VP_GalleryAlbum(entry.getKey(), entry.getValue().size(), 0L, entry.getValue());
                        VPGalleryAlbumsAdapter.setAlbum(VPGalleryAlbum);
                        vp_totalPhotos = vp_totalPhotos + entry.getValue().size();
                    }
                }

            }
            vp_cursor.close();
        }

    }

}
