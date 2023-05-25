package com.video.player.videoplayer.xvxvideoplayer.gallery.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

public class VP_GalleryStorageUtils {

    /**
     * get image file with random name under directory pictures/gallery
     *
     * @return file
     */
    public static File getImageFile() {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File fileDir = new File(root + "/Gallery");
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return new File(fileDir, UUID.randomUUID().toString() + ".jpg");
    }

    /**
     * notify image saved
     * @param path
     * @param context
     */
    public static void notifyNewImage(String path, Context context) {
        Uri contentUri = Uri.fromFile(new File(path));
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

}
