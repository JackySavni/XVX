package com.video.player.videoplayer.xvxvideoplayer.gallery.util.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryAlbumsAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryAlbumsController;

import java.lang.ref.WeakReference;

/**
 * task to get albums and galleryPhotos
 */
public class VP_GalleryAlbumsAsyncTask extends AsyncTask<Void, Void, Void> {
    private final WeakReference<Context> vp_contextWeakReference;
    private final WeakReference<VP_GalleryAlbumsAdapter> vp_albumsAdapterWeakReference;

    public VP_GalleryAlbumsAsyncTask(Context context, VP_GalleryAlbumsAdapter VPGalleryAlbumsAdapter) {
        this.vp_contextWeakReference = new WeakReference<>(context);
        this.vp_albumsAdapterWeakReference = new WeakReference<>(VPGalleryAlbumsAdapter);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        VP_GalleryAlbumsController.vp_getFilesNames(
                vp_contextWeakReference,
                vp_albumsAdapterWeakReference);
        return null;
    }
}