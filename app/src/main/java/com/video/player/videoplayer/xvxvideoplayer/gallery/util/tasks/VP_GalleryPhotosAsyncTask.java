package com.video.player.videoplayer.xvxvideoplayer.gallery.util.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.video.player.videoplayer.xvxvideoplayer.gallery.adapter.VP_GalleryPhotoAdapter;
import com.video.player.videoplayer.xvxvideoplayer.gallery.controller.VP_GalleryPhotoController;
import com.video.player.videoplayer.xvxvideoplayer.gallery.model.VP_GalleryPhoto;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * task to get all galleryPhotos
 */
public class VP_GalleryPhotosAsyncTask extends AsyncTask<Void, Void, ArrayList<VP_GalleryPhoto>> {

    private final WeakReference<Context> vp_contextWeakReference;
    private final WeakReference<VP_GalleryPhotoAdapter> vp_photoAdapterWeakReference;

    public VP_GalleryPhotosAsyncTask(Context context, VP_GalleryPhotoAdapter photoAdapter) {
        this.vp_contextWeakReference = new WeakReference<>(context);
        this.vp_photoAdapterWeakReference = new WeakReference<>(photoAdapter);
    }

    @Override
    protected ArrayList<VP_GalleryPhoto> doInBackground(Void... voids) {
        return VP_GalleryPhotoController.vp_getPhotos(vp_contextWeakReference);
    }

    @Override
    protected void onPostExecute(ArrayList<VP_GalleryPhoto> VPGalleryPhotos) {
        super.onPostExecute(VPGalleryPhotos);
        VP_GalleryPhotoAdapter vp_adapter = vp_photoAdapterWeakReference.get();
        if (vp_adapter != null) {
            vp_adapter.setPhotos(VPGalleryPhotos);
        }
    }

}