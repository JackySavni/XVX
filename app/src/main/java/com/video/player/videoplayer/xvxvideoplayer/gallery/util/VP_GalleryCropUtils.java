package com.video.player.videoplayer.xvxvideoplayer.gallery.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.video.player.videoplayer.xvxvideoplayer.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.UUID;

public class VP_GalleryCropUtils {

    /**
     * show activity to crop photo
     * set compress quality
     * set compress format
     * set status bar color
     * set title
     *
     * @param uri
     * @param activity
     * @param codeRequest
     */
    public static void vp_cropImage(@NonNull Uri uri, Activity activity, int codeRequest) {
        String vp_fileName = UUID.randomUUID().toString() + ".jpg";
        UCrop vp_uCrop = UCrop.of(uri, Uri.fromFile(new File(activity.getCacheDir(), vp_fileName)));
        //
        UCrop.Options vp_options = new UCrop.Options();
        //
        vp_options.setCompressionQuality(100);
        vp_options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        vp_options.setStatusBarColor(ContextCompat.getColor(activity, R.color.black));
        vp_options.setToolbarTitle("Crop photo");
        //
        vp_uCrop.withOptions(vp_options);
        //
        vp_uCrop.start(activity, codeRequest);
    }

}
